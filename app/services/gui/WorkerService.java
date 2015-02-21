package services.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.StudyModel;
import models.StudyResult;
import models.UserModel;
import models.workers.JatosWorker;
import models.workers.Worker;
import persistance.IStudyResultDao;
import play.mvc.Controller;
import play.mvc.Http;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import exceptions.gui.JatosGuiException;

/**
 * Utility class for all JATOS Controllers (not Publix).
 * 
 * @author Kristian Lange
 */
@Singleton
public class WorkerService extends Controller {

	private final JatosGuiExceptionThrower jatosGuiExceptionThrower;
	private final StudyService studyService;
	private final IStudyResultDao studyResultDao;

	@Inject
	WorkerService(JatosGuiExceptionThrower jatosGuiExceptionThrower,
			StudyService studyService, IStudyResultDao studyResultDao) {
		this.jatosGuiExceptionThrower = jatosGuiExceptionThrower;
		this.studyService = studyService;
		this.studyResultDao = studyResultDao;
	}

	/**
	 * Throws a JatosGuiException in case the worker doesn't exist.
	 * Distinguishes between normal and Ajax request.
	 */
	public void checkWorker(Worker worker, Long workerId)
			throws JatosGuiException {
		if (worker == null) {
			String errorMsg = MessagesStrings.workerNotExist(workerId);
			jatosGuiExceptionThrower.throwHome(errorMsg,
					Http.Status.BAD_REQUEST);
		}
	}

	/**
	 * Check whether the removal of this worker is allowed.
	 */
	public void checkRemovalAllowed(Worker worker, UserModel loggedInUser)
			throws JatosGuiException {
		// JatosWorker associated to a JATOS user must not be removed
		if (worker instanceof JatosWorker) {
			JatosWorker maWorker = (JatosWorker) worker;
			String errorMsg = MessagesStrings.removeJatosWorkerNotAllowed(
					worker.getId(), maWorker.getUser().getName(), maWorker
							.getUser().getEmail());
			jatosGuiExceptionThrower.throwAjax(errorMsg, Http.Status.FORBIDDEN);
		}

		// Check for every study if removal is allowed
		for (StudyResult studyResult : worker.getStudyResultList()) {
			StudyModel study = studyResult.getStudy();
			studyService.checkStandardForStudy(study, study.getId(),
					loggedInUser);
			studyService.checkStudyLocked(study);
		}
	}

	/**
	 * Retrieve all workersProvider that did this study.
	 */
	public Set<Worker> retrieveWorkers(StudyModel study) {
		List<StudyResult> studyResultList = studyResultDao
				.findAllByStudy(study);
		Set<Worker> workerSet = new HashSet<>();
		for (StudyResult studyResult : studyResultList) {
			workerSet.add(studyResult.getWorker());
		}
		return workerSet;
	}

}