package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.COPEPlugin;

/**
 * Runs plugin installation mode. This is implemented by storing files both in
 * the eclipse installation path and in the workspace path. When either one does
 * not exist, it is copied there from the other place. When none exists, the one
 * time installation code is run.
 * 
 */
public class Installer {

	public static final String LAST_PLUGIN_VERSION = "LAST_PLUGIN_VERSION";
	public static final String SURVEY_FILENAME = "survey.txt";
	public final static String EMAIL_FILENAME = "email.txt";

	private Path workspaceDirectory;
	private Path permanentDirectory;
	private Uninstaller uninstaller;
	private String installationConfigFileName;

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

		this.workspaceDirectory = COPEPlugin.getDefault().getVersionedLocalStorage().toPath().toAbsolutePath();
		this.permanentDirectory = COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath();
		this.uninstaller = COPEPlugin.getDefault().getUninstaller();
		this.installationConfigFileName = COPEPlugin.getDefault()._getInstallationConfigFileName();
	}

	public void doInstall() throws IOException {
//		new SurveyOperation(workspaceDirectory, permanentDirectory).perform(SURVEY_FILENAME);
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
