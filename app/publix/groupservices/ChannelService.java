package publix.groupservices;

import static akka.pattern.Patterns.ask;
import models.GroupModel;
import models.StudyResult;
import play.mvc.WebSocket;
import publix.exceptions.InternalServerErrorPublixException;
import publix.groupservices.akka.actors.GroupDispatcherRegistry;
import publix.groupservices.akka.messages.GroupDispatcherProtocol.Joined;
import publix.groupservices.akka.messages.GroupDispatcherProtocol.Left;
import publix.groupservices.akka.messages.GroupDispatcherProtocol.PoisonChannel;
import publix.groupservices.akka.messages.GroupDispatcherRegistryProtocol.Get;
import publix.groupservices.akka.messages.GroupDispatcherRegistryProtocol.GetOrCreate;
import publix.groupservices.akka.messages.GroupDispatcherRegistryProtocol.ItsThisOne;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.util.Timeout;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import common.Global;

/**
 * Service class that handles opening and closing of GroupChannels.
 * 
 * @author Kristian Lange (2015)
 */
@Singleton
public class ChannelService {

	/**
	 * Time to wait for an answer after asking a Akka actor
	 */
	private static final Timeout TIMEOUT = new Timeout(Duration.create(5000l,
			"millis"));

	/**
	 * Akka Actor of the GroupDispatcherRegistry. It exists only one and it's
	 * created during startup of JATOS.
	 */
	private final ActorRef groupDispatcherRegistry = Global.INJECTOR
			.getInstance(Key.get(ActorRef.class,
					Names.named(GroupDispatcherRegistry.ACTOR_NAME)));

	/**
	 * Opens a new group channel WebSocket for the given StudyResult.
	 */
	public WebSocket<JsonNode> openGroupChannel(StudyResult studyResult)
			throws InternalServerErrorPublixException {
		// Get the GroupDispatcher that will handle this Group.
		ActorRef groupDispatcher = getOrCreateGroupDispatcher(studyResult
				.getGroup());
		// If this GroupDispatcher already has a group channel for this
		// StudyResult, close the old one before opening a new one.
		closeGroupChannel(studyResult, groupDispatcher);
		return WebSocketBuilder.withGroupChannel(studyResult.getId(),
				groupDispatcher);
	}

	/**
	 * Close the group channel that belongs to the given StudyResult and group.
	 * It just sends the closing message to the GroupDispatcher without waiting
	 * for an answer. We don't use the StudyResult's group but ask for a
	 * separate parameter for the group because the StudyResult's group might
	 * already be null in the process of leaving a group.
	 */
	public void closeGroupChannel(StudyResult studyResult, GroupModel group)
			throws InternalServerErrorPublixException {
		if (group == null) {
			return;
		}
		sendMsg(studyResult, new PoisonChannel(studyResult.getId()));
	}

	/**
	 * Sends a message to each member of the group (the group this studyResult
	 * is in). This message tells that this member has joined the group.
	 */
	public void sendJoinedMsg(StudyResult studyResult)
			throws InternalServerErrorPublixException {
		sendMsg(studyResult, new Joined(studyResult.getId()));
	}

	/**
	 * Sends a message to each member of the group (the group this studyResult
	 * is in). This message tells that this member has left the group.
	 */
	public void sendLeftMsg(StudyResult studyResult)
			throws InternalServerErrorPublixException {
		sendMsg(studyResult, new Left(studyResult.getId()));
	}

	private void sendMsg(StudyResult studyResult, Object msg)
			throws InternalServerErrorPublixException {
		ActorRef groupDispatcher = getGroupDispatcher(studyResult.getGroup());
		if (groupDispatcher != null) {
			groupDispatcher.tell(msg, ActorRef.noSender());
		}
	}

	/**
	 * Get the GroupDispatcher to this group.
	 * 
	 * @param group
	 * @return ActorRef of the GroupDispatcher
	 * @throws InternalServerErrorPublixException
	 */
	private ActorRef getGroupDispatcher(GroupModel group)
			throws InternalServerErrorPublixException {
		Object answer = askGroupDispatcherRegistry(new Get(group.getId()));
		return ((ItsThisOne) answer).groupDispatcher;
	}

	/**
	 * Create a new GroupDispatcher to this group or get the already existing
	 * one.
	 * 
	 * @param group
	 * @return ActorRef of the GroupDispatcher
	 * @throws InternalServerErrorPublixException
	 */
	private ActorRef getOrCreateGroupDispatcher(GroupModel group)
			throws InternalServerErrorPublixException {
		Object answer = askGroupDispatcherRegistry(new GetOrCreate(
				group.getId()));
		return ((ItsThisOne) answer).groupDispatcher;
	}

	/**
	 * Asks the GroupDispatcherRegistry. Waits until it receives an answer.
	 * 
	 * @param msg
	 *            Message Object to ask
	 * @return Answer Object
	 * @throws InternalServerErrorPublixException
	 */
	private Object askGroupDispatcherRegistry(Object msg)
			throws InternalServerErrorPublixException {
		Future<Object> future = ask(groupDispatcherRegistry, msg, TIMEOUT);
		try {
			return Await.result(future, TIMEOUT.duration());
		} catch (Exception e) {
			throw new InternalServerErrorPublixException(e.getMessage());
		}
	}

	/**
	 * Closes the group channel that belongs to the given StudyResult and is
	 * managed by the given GroupDispatcher. Waits until it receives a result
	 * from the GroupDispatcher actor.
	 * 
	 * @param studyResult
	 * @param groupDispatcher
	 * @return true if the GroupChannel was managed by the GroupDispatcher and
	 *         was successfully removed from the GroupDispatcher, false
	 *         otherwise (it was probably never managed by the dispatcher).
	 * @throws InternalServerErrorPublixException
	 */
	private boolean closeGroupChannel(StudyResult studyResult,
			ActorRef groupDispatcher) throws InternalServerErrorPublixException {
		Future<Object> future = ask(groupDispatcher, new PoisonChannel(
				studyResult.getId()), TIMEOUT);
		try {
			return (boolean) Await.result(future, TIMEOUT.duration());
		} catch (Exception e) {
			throw new InternalServerErrorPublixException(e.getMessage());
		}
	}

}