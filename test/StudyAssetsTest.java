import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;

import java.io.File;
import java.io.IOException;

import models.StudyModel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import common.Global;

import play.mvc.Result;
import services.IOUtils;
import controllers.publix.StudyAssets;

/**
 * Testing controller.Studies
 * 
 * @author Kristian Lange
 */
public class StudyAssetsTest {

	private static ControllerTestUtils utils = Global.INJECTOR
			.getInstance(ControllerTestUtils.class);
	private static StudyModel studyTemplate;
	
	@BeforeClass
	public static void startApp() throws Exception {
		utils.startApp();
		studyTemplate = utils.importExampleStudy();
	}

	@AfterClass
	public static void stopApp() throws IOException {
		IOUtils.removeStudyAssetsDir(studyTemplate.getDirName());
		utils.stopApp();
	}
	
	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertThat(a).isEqualTo(2);
	}

	@Test
	public void testStudyAssetsRootPath() {
		File studyAssetsRoot = new File(StudyAssets.STUDY_ASSETS_ROOT_PATH);
		assertThat(studyAssetsRoot.exists());
		assertThat(studyAssetsRoot.isDirectory());
		assertThat(studyAssetsRoot.isAbsolute());
	}

	@Test
	public void testAt() throws IOException {
		StudyModel studyClone = utils.cloneAndPersistStudy(studyTemplate);

		Result result = StudyAssets.at("basic_example_study/hello_world.html");
		assertThat(status(result)).isEqualTo(OK);

		// Clean up
		utils.removeStudy(studyClone);
	}

	@Test
	public void testAtNotFound() {
		Result result = StudyAssets.at("non/existend/filepath");
		assertThat(status(result)).isEqualTo(NOT_FOUND);

		result = StudyAssets.at("non/&?/filepath");
		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testAtPathTraversalAttack() throws IOException {
		StudyModel studyClone = utils.cloneAndPersistStudy(studyTemplate);

		// Although this file exists, it shouldn't be found
		Result result = StudyAssets.at("../../conf/application.conf");
		assertThat(status(result)).isEqualTo(NOT_FOUND);

		// Clean up
		utils.removeStudy(studyClone);
	}

	@Test
	public void testGetUrlWithRequestQueryString() throws IOException {
		String url = StudyAssets.getUrlWithQueryString(
				"oldCall?para=foo&puru=bar", "localhost:9000/", "newCall");
		assertThat(url).isEqualTo(
				"http://localhost:9000/newCall?para=foo&puru=bar");
	}

}
