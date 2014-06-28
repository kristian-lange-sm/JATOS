package controllers;

import java.util.List;
import java.util.Map;

import models.MAExperiment;
import models.MAUser;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security;

public class Experiments extends MAController {

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result get(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, user,
					experimentList);
		}
		if (!experiment.isMember(user)) {
			return forbiddenNotMember(user, experiment, experimentList);
		}

		return ok(views.html.admin.experiment.experiment.render(experimentList,
				null, user, experiment));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result create() {
		List<MAExperiment> experimentList = MAExperiment.findAll();
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		return ok(views.html.admin.experiment.create.render(experimentList,
				null, user, Form.form(MAExperiment.class)));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result submit() {
		Form<MAExperiment> form = Form.form(MAExperiment.class)
				.bindFromRequest();
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		if (form.hasErrors()) {
			List<MAExperiment> experimentList = MAExperiment.findAll();
			return badRequest(views.html.admin.experiment.create.render(
					experimentList, null, user, form));
		} else {
			MAExperiment experiment = form.get();
			experiment.addMember(user);
			experiment.persist();
			return redirect(routes.Experiments.get(experiment.id));
		}
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result update(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, user,
					experimentList);
		}
		if (!experiment.isMember(user)) {
			return forbiddenNotMember(user, experiment, experimentList);
		}

		Form<MAExperiment> form = Form.form(MAExperiment.class)
				.fill(experiment);
		return ok(views.html.admin.experiment.update.render(experimentList,
				experiment, null, user, form));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result submitUpdated(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, user,
					experimentList);
		}
		if (!experiment.isMember(user)) {
			return forbiddenNotMember(user, experiment, experimentList);
		}

		Form<MAExperiment> form = Form.form(MAExperiment.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.admin.experiment.update.render(
					experimentList, experiment, null, user, form));
		}

		DynamicForm requestData = Form.form().bindFromRequest();
		experiment.title = requestData.get("title");
		experiment.merge();
		return redirect(routes.Experiments.get(experimentId));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result delete(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, user,
					experimentList);
		}
		if (!experiment.isMember(user)) {
			return forbiddenNotMember(user, experiment, experimentList);
		}

		experiment.remove();
		return redirect(routes.Admin.index());
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result updateMembers(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser user = MAUser.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, user,
					experimentList);
		}
		if (!experiment.isMember(user)) {
			return forbiddenNotMember(user, experiment, experimentList);
		}

		List<MAUser> userList = MAUser.findAll();
		return ok(views.html.admin.experiment.updateMembers.render(
				experimentList, experiment, userList, null, user, null));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result submitUpdatedMembers(Long experimentId) {
		MAExperiment experiment = MAExperiment.findById(experimentId);
		MAUser loggedInUser = MAUser
				.findByEmail(session(MAController.COOKIE_EMAIL));
		List<MAExperiment> experimentList = MAExperiment.findAll();
		if (experiment == null) {
			return badRequestExperimentNotExist(experimentId, loggedInUser,
					experimentList);
		}
		if (!experiment.isMember(loggedInUser)) {
			return forbiddenNotMember(loggedInUser, experiment, experimentList);
		}

		Map<String, String[]> formMap = request().body().asFormUrlEncoded();
		String[] checkedUsers = formMap.get("user");
		if (checkedUsers == null || checkedUsers.length < 1) {
			String errorMsg = "An experiment should have at least one member.";
			List<MAUser> userList = MAUser.findAll();
			return badRequest(views.html.admin.experiment.updateMembers.render(
					experimentList, experiment, userList, null,
					loggedInUser, errorMsg));
		}
		experiment.memberList.clear();
		for (String email : checkedUsers) {
			MAUser user = MAUser.findByEmail(email);
			if (user != null) {
				experiment.memberList.add(user);
			}
		}

		experiment.merge();
		return redirect(routes.Experiments.get(experimentId));
	}

}