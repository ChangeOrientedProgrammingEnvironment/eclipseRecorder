package edu.oregonstate.cope.eclipse.installer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;

public abstract class Installer {

	public static final String LAST_PLUGIN_VERSION = "LAST_PLUGIN_VERSION";
	public static final String SURVEY_FILENAME = "survey.txt";
	public static final String EMAIL_FILENAME = "email.txt";
	public static final String INSTALLER_EXTENSION_ID = "edu.oregonstate.cope.eclipse.installeroperation";
	protected RecorderFacadeInterface recorder;
	protected InstallerHelper installerHelper;

	public Installer() {
		super();
	}

	public void doInstall() throws IOException {
	
		ArrayList<InstallerOperation> installerOperations = getInstallOperations();
		
		for (InstallerOperation installOperation : installerOperations) {
			initInstallOperation(installOperation);
			installOperation.perform();
		}
	}

	public void run() throws IOException {
		doInstall();
		doUpdate(recorder.getWorkspaceProperties().getProperty(LAST_PLUGIN_VERSION), installerHelper.getPluginVersion());
	}
	
	protected abstract ArrayList<InstallerOperation> getInstallOperations();

	protected void doUpdate(String propertiesVersion, String currentPluginVersion) {
		if (propertiesVersion == null || !propertiesVersion.equals(currentPluginVersion)) {
			recorder.getWorkspaceProperties().addProperty(LAST_PLUGIN_VERSION, currentPluginVersion.toString());
			performPluginUpdate();
		}
	}

	private void initInstallOperation(InstallerOperation installerOperation) {
		Path permanentDirectory = recorder.getStorageManager().getBundleStorage().toPath();
		Path workspaceDirectory = recorder.getStorageManager().getLocalStorage().toPath();
	
		installerOperation.init(recorder, permanentDirectory, workspaceDirectory);
	}

	private void performPluginUpdate() {
		installerHelper.takeSnapshotOfAllProjects();
	}

}