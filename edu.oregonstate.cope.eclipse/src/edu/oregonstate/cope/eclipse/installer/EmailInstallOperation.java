package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

class EmailInstallOperation extends InstallerOperation {

	public EmailInstallOperation(Path workspaceDirectory,
			Path permanentDirectory) {
		super(workspaceDirectory, permanentDirectory);
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
	}

}