package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

/**
 * Runs plugin installation mode. This is implemented by storing files both in
 * the eclipse installation path and in the workspace path. When either one does
 * not exist, it is copied there from the other place. When none exists, the one
 * time installation code is run.
 * 
 */
public class Installer {

	protected static final String LAST_PLUGIN_VERSION = "LAST_PLUGIN_VERSION";
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
			SurveyWizard sw = new SurveyWizard();
			WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), sw);
			wizardDialog.open();

			writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
			writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());

			handleEmail(sw.getEmail());
		}

		private void handleEmail(String email) throws IOException {
			doFor(permanentDirectory, email);
			doFor(workspaceDirectory, email);
		}

		private void doFor(Path parentDirectory, String email) throws IOException {
			Path emailFile = parentDirectory.resolve(EMAIL_FILENAME);
			Files.deleteIfExists(emailFile);
			writeContentsToFile(emailFile, email);
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

	private class EmailInstallOperation extends InstallerOperation {

		public EmailInstallOperation(Path workspaceDirectory,
				Path permanentDirectory) {
			super(workspaceDirectory, permanentDirectory);
		}

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		}

	}

	public Installer() {

		this.workspaceDirectory = COPEPlugin.getVersionedLocalStorage().toPath().toAbsolutePath();
		this.permanentDirectory = COPEPlugin.getBundleStorage().toPath().toAbsolutePath();
		this.uninstaller = COPEPlugin.getDefault().getUninstaller();
		this.installationConfigFileName = COPEPlugin.getDefault()._getInstallationConfigFileName();
	}

	public void doInstall() throws IOException {
		new SurveyOperation(workspaceDirectory, permanentDirectory).perform(SURVEY_FILENAME);
		new ConfigInstallOperation(workspaceDirectory, permanentDirectory).perform(installationConfigFileName);
		new EmailInstallOperation(workspaceDirectory, permanentDirectory).perform(EMAIL_FILENAME);
		
		checkForPluginUpdate(COPEPlugin.getDefault().getWorkspaceProperties().getProperty(LAST_PLUGIN_VERSION), COPEPlugin.getDefault().getPluginVersion().toString());
	}

	protected void checkForPluginUpdate(String propertiesVersion, String currentPluginVersion) {
		if(propertiesVersion == null || !propertiesVersion.equals(currentPluginVersion)){
			COPEPlugin.getDefault().getWorkspaceProperties().addProperty(LAST_PLUGIN_VERSION, currentPluginVersion.toString());
			performPluginUpdate();
		}	
	}

	private void performPluginUpdate() {
		COPEPlugin.getDefault().takeSnapshotOfKnownProjects();
	}
}
