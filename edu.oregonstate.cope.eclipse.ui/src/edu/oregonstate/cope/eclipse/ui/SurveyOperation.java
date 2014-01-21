package edu.oregonstate.cope.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.installer.Installer;
import edu.oregonstate.cope.eclipse.installer.InstallerOperation;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyProvider;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

public class SurveyOperation extends InstallerOperation {
	
	private static final String SURVEY_FILENAME = "survey.txt";

	public SurveyOperation() {
		super(COPEPlugin.getDefault().getLocalStorage().toPath().toAbsolutePath(), 
				COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath());
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		SurveyProvider sw = SurveyWizard.takeRealSurvey();
//		SurveyProvider sw = SurveyWizard.takeFakeSurvey();

		writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
		writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());

		handleEmail(sw.getEmail());
	}

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