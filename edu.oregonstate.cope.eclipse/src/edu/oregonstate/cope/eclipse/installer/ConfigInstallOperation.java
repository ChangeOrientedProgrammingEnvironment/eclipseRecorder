package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ConfigInstallOperation extends InstallerOperation {

	/**
	 * 
	 */
	private Installer installer;

	public ConfigInstallOperation(Installer installer, Path workspaceDirectory,
			Path permanentDirectory) {
		super(workspaceDirectory, permanentDirectory);
		this.installer = installer;
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		this.installer.uninstaller.initUninstallInMonths(3);

		Files.copy(permanentFile.toPath(), workspaceFile.toPath());
	}

}