package controllers.gui;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.FORBIDDEN;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

import daos.common.UserDao;
import general.TestHelper;
import models.gui.ChangePasswordModel;
import models.gui.ChangeUserProfileModel;
import models.gui.NewUserModel;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.db.jpa.JPAApi;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;
import services.gui.AuthenticationService;
import services.gui.BreadcrumbsService;
import services.gui.UserService;

/**
 * Testing actions of controller.gui.Users: basic integration tests
 * 
 * @author Kristian Lange (2017)
 */
public class UsersControllerTest {

	@Inject
	private static Application fakeApplication;

	@Inject
	private TestHelper testHelper;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private UserDao userDao;

	@Before
	public void startApp() throws Exception {
		fakeApplication = Helpers.fakeApplication();

		GuiceApplicationBuilder builder = new GuiceApplicationLoader()
				.builder(new ApplicationLoader.Context(Environment.simple()));
		Guice.createInjector(builder.applicationModule()).injectMembers(this);

		Helpers.start(fakeApplication);
	}

	@After
	public void stopApp() throws Exception {
		// Clean up
		testHelper.removeAllStudies();

		Helpers.stop(fakeApplication);
		testHelper.removeStudyAssetsRootDir();
	}

	/**
	 * Action Users.userManager()
	 */
	@Test
	public void callUserManager() throws Exception {
		RequestBuilder request = new RequestBuilder().method("GET")
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users.userManager().url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
		assertThat(result.charset().get()).isEqualToIgnoringCase("utf-8");
		assertThat(result.contentType().get()).isEqualTo("text/html");
		assertThat(contentAsString(result))
				.contains(BreadcrumbsService.USER_MANAGER);
	}

	/**
	 * Action Users.allUserData()
	 */
	@Test
	public void callAllUserData() throws Exception {
		RequestBuilder request = new RequestBuilder().method("GET")
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users.allUserData().url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
		assertThat(result.charset().get()).isEqualToIgnoringCase("utf-8");
		assertThat(result.contentType().get()).isEqualTo("application/json");
	}

	/**
	 * Action Users.toggleAdmin()
	 */
	@Test
	public void callToggleAdmin() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");
		RequestBuilder request = new RequestBuilder().method("POST")
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users
						.toggleAdmin("bla@bla.org", true).url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
		assertThat(result.charset().get()).isEqualToIgnoringCase("UTF-8");
		assertThat(result.contentType().get()).isEqualTo("application/json");

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.profile()
	 */
	@Test
	public void callProfile() throws Exception {
		RequestBuilder request = new RequestBuilder().method("GET")
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users
						.profile(UserService.ADMIN_EMAIL).url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
		assertThat(result.charset().get()).isEqualToIgnoringCase("UTF-8");
		assertThat(result.contentType().get()).isEqualTo("text/html");
		assertThat(contentAsString(result)).contains(UserService.ADMIN_EMAIL);
	}

	/**
	 * Action Users.singleUserData()
	 */
	@Test
	public void callSingleUserData() throws Exception {
		RequestBuilder request = new RequestBuilder().method("GET")
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users
						.singleUserData(UserService.ADMIN_EMAIL).url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
		assertThat(result.charset().get()).isEqualToIgnoringCase("UTF-8");
		assertThat(result.contentType().get()).isEqualTo("application/json");
	}

	/**
	 * Action Users.submitCreated()
	 */
	@Test
	public void callSubmitCreated() throws Exception {
		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(NewUserModel.ADMIN_PASSWORD, UserService.ADMIN_PASSWORD);
		formMap.put(NewUserModel.ADMIN_ROLE, "true");
		formMap.put(NewUserModel.EMAIL, "foo@foo.org");
		formMap.put(NewUserModel.NAME, "Foo Fool");
		formMap.put(NewUserModel.PASSWORD, "foo");
		formMap.put(NewUserModel.PASSWORD_REPEAT, "foo");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users.submitCreated().url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);
	}

	/**
	 * Action Users.submitEditedProfile()
	 */
	@Test
	public void callSubmitEditedProfile() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(ChangeUserProfileModel.NAME, "Different Name");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users
						.submitEditedProfile("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.submitChangedPassword(): there are two uses: 1) user changes
	 * his own password, 2) an admin changes the password of another user. This
	 * tests the first case. Here old password field must be filled and the
	 * admin password can be left empty.
	 */
	@Test
	public void callSubmitChangedPasswordByUserSelf() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(ChangePasswordModel.ADMIN_PASSWORD, "");
		formMap.put(ChangePasswordModel.NEW_PASSWORD, "Different Password");
		formMap.put(ChangePasswordModel.NEW_PASSWORD_REPEAT,
				"Different Password");
		formMap.put(ChangePasswordModel.OLD_PASSWORD, "bla");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users
						.submitChangedPassword("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.submitChangedPassword(): there are two uses: 1) user changes
	 * his own password, 2) an admin changes the password of another user. This
	 * tests the second case. Here the admin password must be filled and the old
	 * password can be left empty.
	 */
	@Test
	public void callSubmitChangedPasswordByAdmin() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(ChangePasswordModel.ADMIN_PASSWORD,
				UserService.ADMIN_PASSWORD);
		formMap.put(ChangePasswordModel.NEW_PASSWORD, "Different Password");
		formMap.put(ChangePasswordModel.NEW_PASSWORD_REPEAT,
				"Different Password");
		formMap.put(ChangePasswordModel.OLD_PASSWORD, "");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users
						.submitChangedPassword("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.submitChangedPassword(): there are two uses: 1) user changes
	 * his own password, 2) an admin changes the password of another user. This
	 * tests the second case.
	 * 
	 * If the wrong admin password is given an 403 is returned.
	 */
	@Test
	public void callSubmitChangedPasswordByAdminWrongPassword()
			throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(ChangePasswordModel.ADMIN_PASSWORD, "wrong password");
		formMap.put(ChangePasswordModel.NEW_PASSWORD, "Different Password");
		formMap.put(ChangePasswordModel.NEW_PASSWORD_REPEAT,
				"Different Password");
		formMap.put(ChangePasswordModel.OLD_PASSWORD, "");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users
						.submitChangedPassword("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(FORBIDDEN);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.submitChangedPassword(): there are two uses: 1) user changes
	 * his own password, 2) an admin changes the password of another user. This
	 * tests the second case.
	 * 
	 * If the wrong user password is given an 403 is returned.
	 */
	@Test
	public void callSubmitChangedPasswordByUserSelfWrongPassword()
			throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put(ChangePasswordModel.ADMIN_PASSWORD, "");
		formMap.put(ChangePasswordModel.NEW_PASSWORD, "Different Password");
		formMap.put(ChangePasswordModel.NEW_PASSWORD_REPEAT,
				"Different Password");
		formMap.put(ChangePasswordModel.OLD_PASSWORD, "wrong password");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users
						.submitChangedPassword("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(FORBIDDEN);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.remove(): this action can be used in two ways: 1) by an
	 * admin (with ADMIN role) to delete another user, or 2) by the user himself
	 * to delete its own user account. This tests the first way. Here the
	 * password in the form has to be admin's password.
	 */
	@Test
	public void callRemoveByAdmin() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("password", UserService.ADMIN_PASSWORD);

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						UserService.ADMIN_EMAIL)
				.uri(controllers.gui.routes.Users.remove("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);

		jpaApi.withTransaction(() -> {
			assertThat(userDao.findByEmail("bla@bla.org")).isNull();
		});
	}

	/**
	 * Action Users.remove(): this action can be used in two ways: 1) by an
	 * admin (with ADMIN role) to delete another user, or 2) by the user himself
	 * to delete its own user account. This tests the second way. Here the
	 * password in the form has to be the user's own password.
	 */
	@Test
	public void callRemoveByUserSelf() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("password", "bla");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users.remove("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(OK);

		jpaApi.withTransaction(() -> {
			assertThat(userDao.findByEmail("bla@bla.org")).isNull();
		});
	}

	/**
	 * Action Users.remove(): this action can be used in two ways: 1) by an
	 * admin (with ADMIN role) to delete another user, or 2) by the user himself
	 * to delete its own user account. This tests the second way.
	 * 
	 * In case the user has no ADMIN role but wants to delete another user a 403
	 * is returned.
	 */
	@Test
	public void callRemoveButNoAdminRole() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("password", "bla");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users.remove("foo@foo.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(FORBIDDEN);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

	/**
	 * Action Users.remove(): in case the wrong password is given an 403 is
	 * returned
	 */
	@Test
	public void callRemoveWrongPassword() throws Exception {
		testHelper.createAndPersistUser("bla@bla.org", "Bla Bla", "bla");

		Map<String, String> formMap = new HashMap<String, String>();
		formMap.put("password", "wrong password");

		RequestBuilder request = new RequestBuilder().method("POST")
				.bodyForm(formMap)
				.session(AuthenticationService.SESSION_USER_EMAIL,
						"bla@bla.org")
				.uri(controllers.gui.routes.Users.remove("bla@bla.org").url());
		Result result = route(request);

		assertThat(result.status()).isEqualTo(FORBIDDEN);

		// Clean-up
		testHelper.removeUser("bla@bla.org");
	}

}
