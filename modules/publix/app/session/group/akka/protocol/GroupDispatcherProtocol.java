package session.group.akka.protocol;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import models.common.GroupResult.GroupState;

/**
 * Contains all messages that can be used by the GroupDispatcher Akka Actor.
 * Each message is a static class.
 * 
 * @author Kristian Lange (2015)
 */
public class GroupDispatcherProtocol {

	/**
	 * Message to a GroupDispatcher. The GroupDispatcher will tell all other
	 * members of its group about the new member. This will NOT open a new group
	 * channel (a group channel is opened by the WebSocketBuilder and registers
	 * only with a GroupDispatcher).
	 */
	public static class Joined {

		public final long studyResultId;

		public Joined(long studyResultId) {
			this.studyResultId = studyResultId;
		}
	}

	/**
	 * Message to a GroupDispatcher. The GroupDispatcher will just tell all
	 * other members of its GroupResult about the left member. This will NOT
	 * close the group channel (a group channel is closed by sending a
	 * PoisonChannel message.
	 */
	public static class Left {

		public final long studyResultId;

		public Left(long studyResultId) {
			this.studyResultId = studyResultId;
		}
	}

	/**
	 * Message an GroupChannel can send to its GroupDispatcher to indicate it's
	 * closure.
	 */
	public static class UnregisterChannel {

		public final long studyResultId;

		public UnregisterChannel(long studyResultId) {
			this.studyResultId = studyResultId;
		}
	}

	/**
	 * Message format used for communication in the group channel between the
	 * GroupDispatcher and the group members. A GroupMsg contains a JSON node.
	 * If the JSON node has a key named 'recipient' the message is intended for
	 * only one group member - otherwise it's a broadcast message.
	 * 
	 * For system messages the special GroupActionMsg is used. For sending an
	 * error message the special GroupErrorMsg is used.
	 * 
	 */
	public static class GroupMsg {

		public static final String RECIPIENT = "recipient";

		public final ObjectNode jsonNode;

		public GroupMsg(ObjectNode jsonNode) {
			this.jsonNode = jsonNode;
		}

		@Override
		public String toString() {
			return jsonNode.asText();
		}
	}

	/**
	 * Special GroupMsg that contains a GroupAction. A group action message is
	 * like a system event and used solely for messages between the
	 * GroupDispatcher and its group members. A GroupActionMsg is specified by
	 * an key named 'action' in the JSON node. Optionally it can define an field
	 * of type TellWhom, which is used by the dispatcher to determine the
	 * recipients.
	 */
	public static class GroupActionMsg extends GroupMsg {

		public enum TellWhom {
			ALL, ALL_BUT_SENDER, SENDER_ONLY
		};

		public TellWhom tellWhom;

		public GroupActionMsg(ObjectNode jsonNode, TellWhom tellWhom) {
			super(jsonNode);
			this.tellWhom = tellWhom;
		}

		/**
		 * All possible group actions a group action message can have. They are
		 * used as values in JSON message's action field.
		 */
		public enum GroupAction {
			JOINED, // Signals to every group member that a new member joined
			LEFT, // Signals to every member that a member left
			OPENED, // Signals to every member that a new group channel opened
			CLOSED, // Signals to every member that a group channel was closed
			SESSION, // Signals this message contains a group session update
			SESSION_ACK, // Signals that the session update was successful
			SESSION_FAIL, // Signals that the session update failed
			FIXED, // Signals that this group is now fixed (no new members)
			ERROR // Used to send an error back to the sender
		};

		/**
		 * JSON key name for an action (mandatory for an GroupActionMsg)
		 */
		public static final String ACTION = "action";
		/**
		 * JSON key name for the group result ID
		 */
		public static final String GROUP_RESULT_ID = "groupResultId";
		/**
		 * JSON key name containing the {@link GroupState}
		 */
		public static final String GROUP_STATE = "groupState";
		/**
		 * JSON key name containing the group member ID (which is the study
		 * result ID)
		 */
		public static final String MEMBER_ID = "memberId";
		/**
		 * JSON key name containing all active members of the group defined by
		 * their study result ID
		 */
		public static final String MEMBERS = "members";
		/**
		 * JSON key name containing all open group channels defined by their
		 * study result ID
		 */
		public static final String CHANNELS = "channels";
		/**
		 * JSON key name for session data (must be accompanied with a session
		 * version)
		 */
		public static final String GROUP_SESSION_DATA = "sessionData";
		/**
		 * JSON key name for a session patches (must be accompanied with a
		 * session version)
		 */
		public static final String GROUP_SESSION_PATCHES = "sessionPatches";
		/**
		 * JSON key name for the group session version (always together with
		 * either session data or patches)
		 */
		public static final String GROUP_SESSION_VERSION = "sessionVersion";
		/**
		 * JSON key name for an error message
		 */
		public static final String ERROR_MSG = "errorMsg";

	}

	/**
	 * Message a GroupChannel can send to register in a GroupDispatcher.
	 */
	public static class RegisterChannel {

		public final long studyResultId;

		public RegisterChannel(long studyResultId) {
			this.studyResultId = studyResultId;
		}
	}

	/**
	 * Message to signal that a GroupChannel has to change its GroupDispatcher.
	 * It originates in the GroupChannelService and send to the GroupDispatcher
	 * who currently handles the GroupChannel. There it is forwarded to the
	 * actual GroupChannel.
	 */
	public static class ReassignChannel {

		public final long studyResultId;
		public final ActorRef differentGroupDispatcher;

		public ReassignChannel(long studyResultId,
				ActorRef differentGroupDispatcher) {
			this.studyResultId = studyResultId;
			this.differentGroupDispatcher = differentGroupDispatcher;
		}
	}

	/**
	 * Message that forces a GroupChannel to close itself. Send to a
	 * GroupDispatcher it will be forwarded to the right GroupChannel.
	 */
	public static class PoisonChannel {

		public final long studyResultIdOfTheOneToPoison;

		public PoisonChannel(long studyResultIdOfTheOneToPoison) {
			this.studyResultIdOfTheOneToPoison = studyResultIdOfTheOneToPoison;
		}
	}

}
