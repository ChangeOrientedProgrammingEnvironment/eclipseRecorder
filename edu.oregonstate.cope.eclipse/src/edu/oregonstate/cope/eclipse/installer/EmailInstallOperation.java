package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class EmailInstallOperation extends InstallerOperation {
	
	 private static final String EMAIL_FILENAME = "email.txt";

	public EmailInstallOperation() {
		super(COPEPlugin.getDefault().getVersionedLocalStorage().toPath().toAbsolutePath(), 
				COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath());
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
	}

	@Override
	protected String getFileName() {
		return EMAIL_FILENAME;
	}

}