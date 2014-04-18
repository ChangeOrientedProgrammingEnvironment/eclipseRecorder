package edu.oregonstate.cope.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.installer.InstallerOperation;

public class FTPInit extends InstallerOperation {

	public FTPInit() {
		super(COPEPlugin.getDefault().getLocalStorage().toPath().toAbsolutePath(), 
				COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath());
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
