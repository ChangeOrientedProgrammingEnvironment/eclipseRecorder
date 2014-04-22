package edu.oregonstate.cope.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;
import edu.oregonstate.cope.eclipse.installer.Installer;
import edu.oregonstate.cope.eclipse.installer.InstallerOperation;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyProvider;

public abstract class SurveyOperation extends InstallerOperation {

	private static final String SURVEY_FILENAME = "survey.txt";

	public SurveyOperation() {
		super();
	}

	public SurveyOperation(RecorderFacadeInterface recorderFacade, Path permanentDirectory, Path workspaceDirectory) {
		super(recorderFacade, permanentDirectory, workspaceDirectory);
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		SurveyProvider sw = runSurvey();

		writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
		writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());

		handleEmail(sw.getEmail());
	}

	/**
	 * This method runs the survey and returns the results via a {@link SurveyProvider}
	 * @return
	 */
	protected abstract SurveyProvider runSurvey();

	private void handleEmail(String email) throws IOException {
		doFor(permanentDirectory, email);
		doFor(workspaceDirectory, email);
	}

	private void doFor(Path parentDirectory, String email) throws IOException {
		Path emailFile = parentDirectory.resolve(Installer.EMAIL_FILENAME);
		Files.deleteIfExists(emailFile);
		writeContentsToFile(emailFile, email);
	}

	@Override
	protected String getFileName() {
		return SURVEY_FILENAME;
	}

}