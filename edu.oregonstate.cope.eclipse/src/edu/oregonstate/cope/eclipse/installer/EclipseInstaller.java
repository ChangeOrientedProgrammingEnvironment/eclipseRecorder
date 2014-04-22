package edu.oregonstate.cope.eclipse.installer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import edu.oregonstate.cope.clientRecorder.RecorderFacadeInterface;

/**
 * Runs plugin installation mode. This is implemented by storing files both in
 * the eclipse installation path and in the workspace path. When either one does
 * not exist, it is copied there from the other place. When none exists, the one
 * time installation code is run.
 * 
 */
public class EclipseInstaller {
	public static final String LAST_PLUGIN_VERSION = "LAST_PLUGIN_VERSION";
	public static final String SURVEY_FILENAME = "survey.txt";
	public final static String EMAIL_FILENAME = "email.txt";

	public static final String INSTALLER_EXTENSION_ID = "edu.oregonstate.cope.eclipse.installeroperation";
	private RecorderFacadeInterface recorder;
	private InstallerHelper installerHelper;

	public EclipseInstaller(RecorderFacadeInterface recorder, InstallerHelper installerHelper) {
		this.recorder = recorder;
		this.installerHelper = installerHelper;
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

	protected ArrayList<InstallerOperation> getInstallOperations() {
		ArrayList<InstallerOperation> installerOperations = new ArrayList<>();

		IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(INSTALLER_EXTENSION_ID);
		for (IConfigurationElement extension : extensions) {
			try {
				Object executableExtension = extension.createExecutableExtension("InstallerOperation");
				if (executableExtension instanceof InstallerOperation) {
					installerOperations.add((InstallerOperation) executableExtension);
				}
			} catch (CoreException e) {
				System.out.println(e);
			}
		}
		
		return installerOperations;
	}

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
