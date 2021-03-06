package services.gui;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import controllers.gui.Authentication;
import controllers.gui.actionannotations.AuthenticationAction;
import daos.common.UserDao;
import general.common.Common;
import general.common.RequestScope;
import models.common.User;
import play.Logger;
import play.Logger.ALogger;
import play.mvc.Http;
import utils.common.HashUtils;

/**
 * Service class around authentication, session cookie and session cache
 * handling. It works together with the {@link Authentication} controller and
 * the @Authenticated annotation defined in {@link AuthenticationAction}.
 * 
 * If a user is authenticated (same password as stored in the database) a user
 * session ID is generated and stored in Play's session cookie and in the the
 * cache. With each subsequent request this session is checked in the
 * AuthenticationAction.
 * 
 * @author Kristian Lange (2017)
 */
@Singleton
public class AuthenticationService {

	private static final ALogger LOGGER = Logger
			.of(AuthenticationService.class);

	/**
	 * Parameter name in Play's session cookie: It contains the email of the
	 * logged in user
	 */
	public static final String SESSION_ID = "sessionID";

	/**
	 * Parameter name in Play's session cookie: It contains the email of the
	 * logged in user
	 */
	public static final String SESSION_USER_EMAIL = "userEmail";

	/**
	 * Parameter name in Play's session cookie: It contains the timestamp of the
	 * login time
	 */
	public static final String SESSION_LOGIN_TIME = "loginTime";

	/**
	 * Parameter name in Play's session cookie: It contains a timestamp of the
	 * time of the last HTTP request done by the browser with this cookie
	 */
	public static final String SESSION_LAST_ACTIVITY_TIME = "lastActivityTime";

	/**
	 * Key name used in RequestScope to store the logged-in User
	 */
	public static final String LOGGED_IN_USER = "loggedInUser";

	private static SecureRandom random = new SecureRandom();

	private final UserDao userDao;
	private final UserSessionCacheAccessor userSessionCacheAccessor;

	@Inject
	AuthenticationService(UserDao userDao,
			UserSessionCacheAccessor userSessionCacheAccessor) {
		this.userDao = userDao;
		this.userSessionCacheAccessor = userSessionCacheAccessor;
	}

	/**
	 * Authenticates the user specified by the email with the given password.
	 */
	public boolean authenticate(String email, String password) {
		String passwordHash = HashUtils.getHashMDFive(password);
		return userDao.authenticate(email, passwordHash);
	}

	/**
	 * Checks the user session cache whether this user tries to login repeatedly
	 */
	public boolean isRepeatedLoginAttempt(String email) {
		userSessionCacheAccessor.addLoginAttempt(email);
		return userSessionCacheAccessor.isRepeatedLoginAttempt(email);
	}

	/**
	 * Retrieves the logged-in user from Play's session. If a user is logged-in
	 * his email is stored in the Play's session cookie. With the email a user
	 * can be retrieved from the database. Returns null if the session doesn't
	 * contains an email or if the user doesn't exists in the database.
	 * 
	 * In most cases getLoggedInUser() is faster since it doesn't has to query
	 * the database.
	 */
	public User getLoggedInUserBySessionCookie(Http.Session session) {
		String email = session.get(AuthenticationService.SESSION_USER_EMAIL);
		User loggedInUser = null;
		if (email != null) {
			loggedInUser = userDao.findByEmail(email);
		}
		return loggedInUser;
	}

	/**
	 * Gets the logged-in user from the RequestScope. It was put into the
	 * RequestScope by the AuthenticationAction. Therefore this method works
	 * only if you use the @Authenticated annotation at your action.
	 */
	public User getLoggedInUser() {
		return (User) RequestScope.get(LOGGED_IN_USER);
	}

	/**
	 * Prepares Play's session cookie and the user session cache for the user
	 * with the given email to be logged-in. Does not authenticate the user (use
	 * authenticate() for this).
	 */
	public void writeSessionCookieAndSessionCache(Http.Session session,
			String email, String remoteAddress) {
		String sessionId = generateSessionId();
		userSessionCacheAccessor.setUserSessionId(email, remoteAddress,
				sessionId);
		session.put(SESSION_ID, sessionId);
		session.put(SESSION_USER_EMAIL, email);
		session.put(SESSION_LOGIN_TIME,
				String.valueOf(Instant.now().toEpochMilli()));
		session.put(SESSION_LAST_ACTIVITY_TIME,
				String.valueOf(Instant.now().toEpochMilli()));
	}

	/**
	 * Used StackOverflow for this session ID generator: "This works by choosing
	 * 130 bits from a cryptographically secure random bit generator, and
	 * encoding them in base-32" (http://stackoverflow.com/questions/41107)
	 */
	private String generateSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	/**
	 * Refreshes the last activity timestamp in Play's session cookie. This is
	 * usually done with each HTTP call of the user.
	 */
	public void refreshSessionCookie(Http.Session session) {
		session.put(SESSION_LAST_ACTIVITY_TIME,
				String.valueOf(Instant.now().toEpochMilli()));
	}

	/**
	 * Deletes the session cookie. This is usual done during a user logout.
	 */
	public void clearSessionCookie(Http.Session session) {
		session.clear();
	}

	/**
	 * Deletes the session cookie and removes the cache entry. This is usual
	 * done during a user logout.
	 */
	public void clearSessionCookieAndSessionCache(Http.Session session,
			String email, String remoteAddress) {
		userSessionCacheAccessor.removeUserSessionId(email, remoteAddress);
		session.clear();
	}

	/**
	 * Checks the session ID stored in Play's session cookie whether it is the
	 * same as stored in the cache during the last login.
	 */
	public boolean isValidSessionId(Http.Session session, String email,
			String remoteAddress) {
		String cookieSessionId = session.get(SESSION_ID);
		String cachedSessionId = userSessionCacheAccessor
				.getUserSessionId(email, remoteAddress);
		return cookieSessionId != null && cachedSessionId != null
				&& cookieSessionId.equals(cachedSessionId);
	}

	/**
	 * Returns true if the session login time as saved in Play's session cookie
	 * is older than allowed.
	 */
	public boolean isSessionTimeout(Http.Session session) {
		try {
			Instant loginTime = Instant.ofEpochMilli(
					Long.parseLong(session.get(SESSION_LOGIN_TIME)));
			Instant now = Instant.now();
			Instant allowedUntil = loginTime
					.plus(Common.getUserSessionTimeout(), ChronoUnit.MINUTES);
			return allowedUntil.isBefore(now);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			// In case of any exception: timeout
			return true;
		}
	}

	/**
	 * Returns true if the session inactivity time as saved in Play's session
	 * cookie is older than allowed.
	 */
	public boolean isInactivityTimeout(Http.Session session) {
		try {
			Instant lastActivityTime = Instant.ofEpochMilli(
					Long.parseLong(session.get(SESSION_LAST_ACTIVITY_TIME)));
			Instant now = Instant.now();
			Instant allowedUntil = lastActivityTime.plus(
					Common.getUserSessionInactivity(), ChronoUnit.MINUTES);
			return allowedUntil.isBefore(now);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			// In case of any exception: timeout
			return true;
		}
	}

}
