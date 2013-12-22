package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

/**
 * Runs plugin installation mode. This is implemented by storing files both in
 * the eclipse installation path and in the workspace path. When either one does
 * not exist, it is copied there from the other place. When none exists, the one
 * time installation code is run.
 * 
 */
public class Installer {

	private static final String SURVEY_FILENAME = "survey.txt";
	private static final String EMAIL_FILENAME = "email.txt";

	private Path workspaceDirectory;
	private Path permanentDirectory;
	private Uninstaller uninstaller;
	private String installationConfigFileName;

	private class SurveyOperation extends InstallerOperation {


		public SurveyOperation(Path workspaceDirectory, Path permanentDirectory) {
			super(workspaceDirectory, permanentDirectory);
		}

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
			System.out.println("GIVE SURVEY");

			SurveyWizard sw = new SurveyWizard();
			WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), sw);
			wizardDialog.open();

			writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
			writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());
			
			writeContentsToFile(permanentDirectory.resolve(EMAIL_FILENAME), sw.getEmail());
		}

	}

	private class ConfigInstallOperation extends InstallerOperation {

		public ConfigInstallOperation(Path workspaceDirectory,
				Path permanentDirectory) {
			super(workspaceDirectory, permanentDirectory);
		}

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
			uninstaller.initUninstallInMonths(3);

			Files.copy(permanentFile.toPath(), workspaceFile.toPath());
		}

	}
	
	private class EmailInstallOperation extends InstallerOperation{

		public EmailInstallOperation(Path workspaceDirectory,
				Path permanentDirectory) {
			super(workspaceDirectory, permanentDirectory);
		}

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		}
		
	}

	public Installer(Path workspaceDirectory, Path permanentDirectory,
			Uninstaller uninstaller, String installationConfigFileName) {
		this.workspaceDirectory = workspaceDirectory;
		this.permanentDirectory = permanentDirectory;
		this.uninstaller = uninstaller;
		this.installationConfigFileName = installationConfigFileName;

		System.err.println(workspaceDirectory);
		System.err.println(permanentDirectory);
	}

	public void doInstall() {
		try {
			new SurveyOperation(workspaceDirectory, permanentDirectory).perform(SURVEY_FILENAME);
			new ConfigInstallOperation(workspaceDirectory, permanentDirectory).perform(installationConfigFileName);
			new EmailInstallOperation(workspaceDirectory, permanentDirectory).perform(EMAIL_FILENAME);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
