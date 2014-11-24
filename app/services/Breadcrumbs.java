package services;

import java.util.LinkedHashMap;
import java.util.Map;

import models.ComponentModel;
import models.StudyModel;
import models.UserModel;
import models.workers.Worker;
import play.mvc.Call;
import controllers.routes;

public class Breadcrumbs {

	private Map<String, String> breadcrumbs = new LinkedHashMap<>();

	public Map<String, String> getBreadcrumbs() {
		return breadcrumbs;
	}

	public Breadcrumbs put(String name, String url) {
		this.breadcrumbs.put(name, url);
		return this;
	}

	public Breadcrumbs put(String name, Call call) {
		this.breadcrumbs.put(name, call.url());
		return this;
	}

	public static Breadcrumbs generateForHome() {
		return generateForHome(null);
	}

	public static Breadcrumbs generateForHome(String last) {
		Breadcrumbs breadcrumbs = new Breadcrumbs();
		if (last != null) {
			breadcrumbs = new Breadcrumbs().put("Home", routes.Home.home())
					.put(last, "");
		} else {
			breadcrumbs.put("Home", "");
		}
		return breadcrumbs;
	}

	public static Breadcrumbs generateForUser(UserModel user) {
		return generateForUser(user, null);
	}

	public static Breadcrumbs generateForUser(UserModel user, String last) {
		Breadcrumbs breadcrumbs = new Breadcrumbs().put("Home",
				routes.Home.home());
		if (last != null) {
			breadcrumbs.put(user.toString(),
					routes.Users.profile(user.getEmail())).put(last, "");
		} else {
			breadcrumbs.put(user.toString(), "");
		}
		return breadcrumbs;
	}

	public static Breadcrumbs generateForStudy(StudyModel study) {
		return generateForStudy(study, null);
	}

	public static Breadcrumbs generateForStudy(StudyModel study, String last) {
		Breadcrumbs breadcrumbs = new Breadcrumbs().put("Home",
				routes.Home.home());
		if (last != null) {
			breadcrumbs.put(study.getTitle(),
					routes.Studies.index(study.getId(), null)).put(last, "");
		} else {
			breadcrumbs.put(study.getTitle(), "");
		}
		return breadcrumbs;
	}
	
	public static Breadcrumbs generateForWorker(Worker worker) {
		return generateForWorker(worker, null);
	}

	public static Breadcrumbs generateForWorker(Worker worker, String last) {
		Breadcrumbs breadcrumbs = new Breadcrumbs()
				.put("Home", routes.Home.home())
				.put("Worker " + worker.getId(), "")
				.put(last, "");
		return breadcrumbs;
	}

	public static Breadcrumbs generateForComponent(StudyModel study,
			ComponentModel component, String last) {
		Breadcrumbs breadcrumbs = new Breadcrumbs()
				.put("Home", routes.Home.home())
				.put(study.getTitle(),
						routes.Studies.index(study.getId(), null))
				.put(component.getTitle(), "").put(last, "");
		return breadcrumbs;
	}

}