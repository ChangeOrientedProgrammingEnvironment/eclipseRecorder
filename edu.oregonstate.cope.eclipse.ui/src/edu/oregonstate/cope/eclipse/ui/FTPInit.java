package edu.oregonstate.cope.eclipse.ui;

import java.io.File;
import java.io.IOException;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import edu.oregonstate.cope.clientRecorder.installer.InstallerOperation;

public class FTPInit extends InstallerOperation {

	public FTPInit() {
		super();
	}
	
	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile)
			throws IOException {
		PreferencesUtil.createPreferenceDialogOn(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "edu.oregonstate.cope.eclipse.ui.prefenrencePage", null, null).open();
	}

	@Override
	protected String getFileName() {
		return "";
	}

}
