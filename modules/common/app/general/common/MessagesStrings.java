package general.common;

import javax.inject.Singleton;

import models.common.ComponentResult.ComponentState;
import models.common.StudyResult.StudyState;
import models.common.workers.MTWorker;
import models.common.workers.PersonalMultipleWorker;
import models.common.workers.PersonalSingleWorker;

/**
 * Helper class with methods that generate error strings.
 * 
 * @author Kristian Lange
 */
@Singleton
public class MessagesStrings {

	// General
	public static final String MISSING_TITLE = "Missing title";
	public static final String TITLE_TOO_LONG = "Title too long";
	public static final String MISSING_FILE_PATH = "Missing file path";
	public static final String MISSING_NAME = "Missing name";
	public static final String NAME_TOO_LONG = "Name too long";
	public static final String MISSING_EMAIL = "Missing email";
	public static final String EMAIL_TOO_LONG = "Email too long";
	public static final String NO_USER_LOGGED_IN = "No user logged in";
	public static final String MISSING_DIRNAME = "Missing directory name";
	public static final String DIRNAME_TOO_LONG = "Directory name too long";
	public static final String INVALID_DIR_NAME = "Invalid directory name";
	public static final String COMPONENT_INVALID = "Component is invalid";
	public static final String STUDY_INVALID = "Study is invalid";

	// Edit HTML forms
	public static final String INVALID_JSON_FORMAT = "Invalid JSON format";
	public static final String NO_HTML_ALLOWED = "No HTML allowed";
	public static final String PATH_TOO_LONG = "Path too long";
	public static final String COMMENT_TOO_LONG = "Comment too long";
	public static final String NOT_A_VALID_PATH_YOU_CAN_LEAVE_IT_EMPTY = "Not a valid path or filename. Remember to use '/' as folder separator, and to include the file extension. You can leave it empty for now.";
	public static final String JSON_DATA_MISSING_OR_INVALID_JSON_FORMAT = "JSON data missing or invalid JSON format";
	public static final String STUDY_AT_LEAST_ONE_USER = "A study needs at least one user.";
	public static final String STUDY_IS_LOCKED = "Study is locked. It's not possible to edit.";
	public static final String STUDY_WASNT_SAVED = "Study wasn't saved";
	public static final String COMPONENT_WASNT_SAVED = "Component wasn't saved";
	public static final String BATCH_WASNT_SAVED = "Data weren't saved";
	public static final String BATCH_NOT_ALLOWED_DELETE_DEFAULT = "It's not allowed to delete the default batch.";
	public static final String BATCH_MAX_ACTIVE_MEMBERS_SET = "Group's max acitive members size must be set if you want to limit it.";
	public static final String BATCH_MAX_ACTIVE_MEMBERS = "Group's max active member size must be greater or equal than the min one.";
	public static final String BATCH_MAX_TOTAL_MEMBERS_SET = "Group's max total members size must be set if you want to limit it.";
	public static final String BATCH_MAX_TOTAL_MEMBERS = "Group's max total members size must be greater or equal than the max active member size.";
	public static final String BATCH_MAX_TOTAL_WORKER_SET = "Batch's max total worker size must be set if you want to limit it.";
	public static final String BATCH_MAX_TOTAL_WORKERS = "Batch's max total worker size must be at least 1.";

	// User
	public static final String INVALID_USER_OR_PASSWORD = "Invalid user or password";
	public static final String FAILED_THREE_TIMES = "You failed three times. Now you have to wait for a minute before you can try again.";
	public static final String WRONG_PASSWORD = "Wrong password";
	public static final String NOT_ALLOWED_TO_CHANGE_PASSWORDS = "You are not allowed to change passwords.";
	public static final String NOT_ALLOWED_TO_DELETE_USER = "You are not allowed to delete this user.";
	public static final String WRONG_OLD_PASSWORD = "Wrong old password";
	public static final String PASSWORDS_DONT_MATCH = "Passwords don't match";
	public static final String PASSWORDS_SHOULDNT_BE_EMPTY_STRINGS = "Passwords shouldn't be empty strings";
	public static final String THIS_EMAIL_IS_ALREADY_REGISTERED = "This email address is already registered.";
	public static final String ONLY_ADMIN_CAN_SEE_LOGS = "Only an admin can see the logs";
	public static final String COULDNT_OPEN_LOG = "Couldn't open log file";
	public static final String ADMIN_NOT_ALLOWED_TO_REMOVE_HIS_OWN_ADMIN_ROLE = "Sorry, it's not possible to remove your own admin rights. Although you can ask another admin to remove them for you.";
	public static final String NOT_ALLOWED_REMOVE_ADMINS_ADMIN_RIGHTS = "It's not possible to remove 'admin's admin rights.";
	public static final String NOT_ALLOWED_DELETE_ADMIN = "It's not possible to remove user 'admin'.";
	public static final String NOT_ALLOWED_CHANGE_PW_ADMIN = "It's not possible to change 'admin's password.";

	// Export / import
	public static final String NO_COMPONENT_UPLOAD = "Uploaded file isn't intended for components";
	public static final String NO_STUDY_UPLOAD = "Uploaded file isn't intended for studies";
	public static final String MORE_THAN_ONE_DIR_IN_ZIP = "There are more than one directory in the ZIP file.";
	public static final String NO_DIR_IN_ZIP_CREATED_NEW = "There is no directory in the ZIP file - new study assets created.";
	public static final String COULDNT_READ_JSON = "Couldn't read JSON string.";
	public static final String COULDNT_READ_FILE = "Couldn't read file";
	public static final String FILE_MISSING = "File missing";
	public static final String FILE_NOT_EXIST_OR_DIR = "File doesn't exists or is a directory";
	public static final String IMPORT_OF_STUDY_FAILED = "Import of study failed";
	public static final String IMPORT_OF_COMPONENT_FAILED = "Import of component failed";
	public static final String NO_RESULTS_SELECTED = "No results selected";
	public static final String PROBLEM_GENERATING_JSON_DATA = "Problem generate JSON data";
	public static final String TOO_NEW_STUDY_VERSION = "This study is from an newer version of JATOS. Try updating your JATOS.";
	public static final String UNSUPPORTED_STUDY_VERSION = "This study is from an unsupported version of JATOS.";
	public static final String TOO_NEW_COMPONENT_VERSION = "This component is from an newer version of JATOS. Try updating your JATOS.";
	public static final String UNSUPPORTED_COMPONENT_VERSION = "This component is from an unsupported version of JATOS.";

	// Results
	public static final String RESULTS_COLUMN_VISIBILITY = "Show/hide columns";
	public static final String RESULTS_SELECT_ALL = "Select all results (including the ones on different table pages)";
	public static final String RESULTS_SELECT_FILTERED = "Select only the filtered results (including the ones on different table pages)";
	public static final String RESULTS_DESELECT_ALL = "Deselect all results";
	public static final String RESULTS_EXPORT_DATA = "Export selected results as a text file to your local file system";
	public static final String RESULTS_EXPORT_ALL_DATA = "Export all results as a text file to your local file system";
	public static final String RESULTS_DELETE = "Delete selected results";
	public static final String RESULTS_DELETE_ALL = "Delete all results";
	public static final String RESULTS_INFO = "Select a result for export or deletion by clicking anywhere on its row.";
	public static final String RESULTS_DATA = "Click on the + icon to open the result's data";
	public static final String RESULTS_START_TIME = "Time according to the JATOS server";
	public static final String RESULTS_LAST_SEEN_TIME = "Time of the last heartbeat according to the JATOS server";
	public static final String RESULTS_WORKER_TYPE = "Type of worker, e.g. "
			+ MTWorker.UI_WORKER_TYPE + ", "
			+ PersonalMultipleWorker.UI_WORKER_TYPE + ", "
			+ PersonalSingleWorker.UI_WORKER_TYPE;
	public static final String RESULTS_WORKER_ID = "Click on the ID to go to this worker's results";
	public static final String RESULTS_STUDY_ID = "Click on the ID to go to this study";
	public static final String RESULTS_MT_WORKER_ID_CONFIRMATION_CODE = "Mechanical Turk's worker ID and in brackets the confirmation code for Mechanical Turk";
	public static final String RESULTS_CONFIRMATION_CODE = "Confirmation code for Mechanical Turk";
	public static final String RESULTS_STUDY_STATE = "Current state of this study's run, like "
			+ StudyState.allStatesAsString();
	public static final String RESULTS_STUDY_MESSAGES = "Messages that occured during the run of this study";
	public static final String RESULTS_COMPONENT_STATE = "Current state of this component's run, like "
			+ ComponentState.allStatesAsString();
	public static final String RESULTS_COMPONENT_MESSAGES = "Messages that occured during the run of this component";

	// Other
	public static final String MTWORKER_ALLOWANCE_MISSING = "Right now workers from Mechnical Turk are not allowed to run this study. You should change this in this study's properties before you run it from Mechanical Turk.";
	public static final String COULDNT_GENERATE_JATOS_URL = "Couldn't generate JATOS' URL. Try to reload this page.";
	public static final String COULDNT_CHANGE_POSITION_OF_COMPONENT = "Couldn't change position of componet.";
	public static final String COMPONENT_DELETED_BUT_FILES_NOT = "Component deleted, but all files (e.g. its HTML file) in study assets remain untouched.";
	public static final String PROBLEM_GENERATING_BREADCRUMBS = "Problem generating breadcrumbs";
	public static final String BATCH_DELETED = "Batch successfully deleted";

	public static String workerNotExist(Long workerId) {
		return workerNotExist(String.valueOf(workerId));
	}

	public static String workerNotExist(String workerIdStr) {
		String errorMsg = "A worker with ID " + workerIdStr + " doesn't exist.";
		return errorMsg;
	}

	public static String studyNotExist(Long studyId) {
		String errorMsg = "An study with ID " + studyId + " doesn't exist.";
		return errorMsg;
	}

	public static String studyNotUser(String username, String email,
			Long studyId, String studyTitle) {
		String errorMsg = username + " (" + email + ") isn't user of study "
				+ studyId + " \"" + studyTitle + "\".";
		return errorMsg;
	}

	public static String studyReorderUnknownPosition(String position,
			Long studyId) {
		String errorMsg = "Unknown position " + position + ". Couldn't reorder "
				+ "components in study with ID " + studyId + ".";
		return errorMsg;
	}

	public static String studyImportNotUser(String studyTitle) {
		return "The study \"" + studyTitle + "\" you're trying "
				+ "to upload already exists but you aren't a user of it. "
				+ "You can always import this study in another JATOS instance "
				+ "(e.g. <a href=\"http://www.jatos.org/Installation.html\">"
				+ "in your local instance</a>), clone it there, export it, "
				+ "and import it here again.";
	}

	public static String studyAssetsOverwritten(String studyAssetsName,
			Long studyId) {
		String errorMsg = "Assets \"" + studyAssetsName + "\" of study with ID "
				+ studyId + " were overwritten.";
		return errorMsg;
	}

	public static String studysPropertiesOverwritten(Long studyId) {
		String errorMsg = "Properties of study with ID " + studyId
				+ " were overwritten.";
		return errorMsg;
	}

	public static String importedNewStudy(String studyAssetsName,
			Long studyId) {
		String errorMsg = "New study imported: ID " + studyId
				+ " and study assets \"" + studyAssetsName + "\".";
		return errorMsg;
	}

	public static String componentExportFailure(Long componentId) {
		String errorMsg = "Failure during export of component with ID "
				+ componentId + ".";
		return errorMsg;
	}

	public static String componentsPropertiesOverwritten(Long componentId,
			String title) {
		String errorMsg = "Properties of component \"" + title + "\"  with ID "
				+ componentId + " were overwritten.";
		return errorMsg;
	}

	public static String importedNewComponent(Long componentId, String title) {
		String errorMsg = "New component \"" + title + "\" with ID "
				+ componentId + " imported.";
		return errorMsg;
	}

	public static String componentNotBelongToStudy(Long studyId,
			Long componentId) {
		String errorMsg = "There is no study with ID " + studyId
				+ " that has a component with ID " + componentId + ".";
		return errorMsg;
	}

	public static String componentHasNoStudy(Long componentId) {
		String errorMsg = "The component with ID " + componentId
				+ " doesn't belong to any study.";
		return errorMsg;
	}

	public static String userNotExist(String email) {
		String errorMsg = "An user with email " + email + " doesn't exist.";
		return errorMsg;
	}

	public static String userNotAllowedToGetData(String email) {
		return "You are not allowed to get data for user " + email + ".";
	}

	public static String componentNotExist(Long componentId) {
		String errorMsg = "An component with ID " + componentId
				+ " doesn't exist.";
		return errorMsg;
	}

	public static String componentNotExist(Long studyId, Long componentId) {
		String errorMsg = "An component with ID " + componentId + " of study "
				+ studyId + " doesn't exist.";
		return errorMsg;
	}

	public static String componentCloneHtmlNotCloned(String htmlFilePath) {
		return "Successfully cloned component but couldn't copy HTML file \""
				+ htmlFilePath + "\".";
	}

	public static String componentResultNotExist(Long componentResultId) {
		String errorMsg = "An component result with ID " + componentResultId
				+ " doesn't exist.";
		return errorMsg;
	}

	public static String componentResultNotExist(String componentResultIdStr) {
		String errorMsg = "An component result with ID \""
				+ componentResultIdStr + "\" doesn't exist.";
		return errorMsg;
	}

	public static String resultNotExist(String resultIdStr) {
		String errorMsg = "An result with ID \"" + resultIdStr
				+ "\" doesn't exist.";
		return errorMsg;
	}

	public static String resultIdMalformed(String resultIdStr) {
		String errorMsg = "The result ID \"" + resultIdStr + "\" is malformed.";
		return errorMsg;
	}

	public static String studyResultNotFromWorker(Long studyResultId,
			Long workerId) {
		String errorMsg = "Study result (ID " + studyResultId
				+ ") doesn't belong to worker (ID " + workerId + ")";
		return errorMsg;
	}

	public static String studyResultNotExist(Long studyResultId) {
		String errorMsg = "A study result with ID " + studyResultId
				+ " doesn't exist.";
		return errorMsg;
	}

	public static String studyResultNotExist(String studyResultIdStr) {
		String errorMsg = "A study result with ID \"" + studyResultIdStr
				+ "\" doesn't exist.";
		return errorMsg;
	}

	public static String removeJatosWorkerNotAllowed(Long workerId,
			String username, String email) {
		String errorMsg = "Worker (ID: " + workerId + ") is a worker of "
				+ "JATOS, associated to the user " + username + " (" + email
				+ ") and can't be deleted.";
		return errorMsg;
	}

	public static String htmlFilePathEmpty(Long componentId) {
		String errorMsg = "Component " + componentId
				+ "'s HTML file path is empty.";
		return errorMsg;
	}

	public static String htmlFilePathNotExist(String studyDirName,
			String htmlFilePath) {
		String errorMsg = "HTML file " + htmlFilePath + " in study assets "
				+ studyDirName + " couldn't be found.";
		return errorMsg;
	}

	public static String studyExportFailure(Long studyId) {
		String errorMsg = "Export of study with ID " + studyId + " failed.";
		return errorMsg;
	}

	public static String studyLocked(Long studyId) {
		return "Study " + studyId
				+ " is locked. Unlock it if you want to make changes.";
	}

	public static String studyCreationOfPersonalSingleRunFailed(Long studyId) {
		return "The creation of a Personal Single Run for study " + studyId
				+ " failed.";
	}

	public static String studyCreationOfPersonalMultipleRunFailed(
			Long studyId) {
		return "The creation of a Personal Multiple Run for study " + studyId
				+ " failed.";
	}

	public static String couldntGeneratePathToFileOrDir(String fileStr) {
		return "Couldn't generate path to file or directory \"" + fileStr
				+ "\".";
	}

	public static String couldntGenerateStudyAssetsDir(String path) {
		return "Couldn't generate path to study assets' directory (" + path
				+ ").";
	}

	public static String pathNotAbsolute(String path) {
		return "Path \"" + path + "\" should be absolute.";
	}

	public static String pathNotRelative(String path) {
		return "Path \"" + path + "\" should be relative.";
	}

	public static String filePathIsntFile(String fileName) {
		return "File path \"" + fileName + "\" doesn't lead to a file.";
	}

	public static String dirPathIsntDir(String dirName) {
		return "Directory path \"" + dirName
				+ "\" doesn't lead to a directory.";
	}

	public static String studyAssetsDirNotCreatedBecauseExists(String dirName) {
		return "Study assets' directory (" + dirName
				+ ") couldn't be created because it already exists.";
	}

	public static String studyAssetsDirExistsBelongsToDifferentStudy(
			String dirName) {
		return "The uploaded study assets' directory \"" + dirName
				+ "\" already exists but belongs to another study. Try renaming"
				+ " the study assets' directory name in the study properties.";
	}

	public static String studyAssetsDirNotRenamed(String oldDirName,
			String newDirName) {
		return "Couldn't rename study assets' directory from \"" + oldDirName
				+ "\" to \"" + newDirName + "\".";
	}

	public static String studyAssetsDirNotCreated(String dirName) {
		return "Couldn't create new study assets' directory (" + dirName + ").";
	}

	public static String studyAssetsNotRenamedBecauseExists(String oldDirName,
			String newDirName) {
		return "Study assets directory \"" + oldDirName
				+ "\" couldn't be renamed to \"" + newDirName
				+ "\" because it already exists.";
	}

	public static String htmlFileNotRenamedBecauseExists(String oldFilePath,
			String newFilePath) {
		return "HTML file \"" + oldFilePath + "\" couldn't be renamed to \""
				+ newFilePath + "\" because it already exists.";
	}

	public static String htmlFileNotRenamed(String oldFilePath,
			String newFilePath) {
		return "Couldn't rename HTML file from \"" + oldFilePath + "\" to \""
				+ newFilePath + "\".";
	}

	public static String fileNotUploaded(String fileName) {
		return "Couldn't upload file \"" + fileName + "\".";
	}

	public static String batchNotExist(Long batchId) {
		String errorMsg = "A batch with ID " + batchId + " doesn't exist.";
		return errorMsg;
	}

	public static String batchNotInStudy(Long batchId, Long studyId) {
		String errorMsg = "A batch with ID " + batchId
				+ " is not a batch of the study with ID " + studyId + ".";
		return errorMsg;
	}

}
