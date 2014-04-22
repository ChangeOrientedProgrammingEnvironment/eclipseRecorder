package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;

public class EmailInstallOperation extends InstallerOperation {
	
	private static final String EMAIL_FILENAME = "email.txt";

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
	}

	@Override
	protected String getFileName() {
		return EMAIL_FILENAME;
	}

}