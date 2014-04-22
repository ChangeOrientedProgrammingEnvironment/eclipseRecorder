package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigInstallOperation extends InstallerOperation {

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		recorder.getUninstaller().initUninstallInMonths(3);

		Files.copy(permanentFile.toPath(), workspaceFile.toPath());
	}

	@Override
	protected String getFileName() {
		return recorder.getInstallationConfigFilename();
	}

}