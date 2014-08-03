package models.workers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import models.results.StudyResult;
import models.results.StudyResult.StudyState;
import play.db.jpa.JPA;

@Entity
@Inheritance
@DiscriminatorColumn(name = "workerType")
public abstract class Worker {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "worker_id")
	private List<StudyResult> studyResultList = new ArrayList<StudyResult>();

	public Worker() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setStudyResultList(List<StudyResult> studyResultList) {
		this.studyResultList = studyResultList;
	}

	public List<StudyResult> getStudyResultList() {
		return this.studyResultList;
	}

	public void addStudyResult(StudyResult studyResult) {
		studyResultList.add(studyResult);
	}

	public boolean didStudy(Long studyId) {
		for (StudyResult studyResult : studyResultList) {
			if (studyResult.getId() == studyId) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean isAllowedToStartStudy(Long studyId);

	public StudyResult getStartedStudyResult(Long studyId) {
		for (StudyResult studyResult : studyResultList) {
			if (studyResult.getStudy().getId() == studyId
					&& studyResult.getState() == StudyState.STARTED) {
				// Since there is only one study result of the same study
				// allowed to be in STARTED, return the first one
				return studyResult;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	public static Worker findById(Long id) {
		return JPA.em().find(Worker.class, id);
	}

	public void persist() {
		JPA.em().persist(this);
	}

	public void merge() {
		JPA.em().merge(this);
	}

	public void remove() {
		JPA.em().remove(this);
	}

}