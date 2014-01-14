package edu.oregonstate.cope.eclipse.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.COPEPlugin;

class ConfigInstallOperation extends InstallerOperation {

	/**
	 * 
	 */
	private Uninstaller uninstaller;

	public ConfigInstallOperation() {
		super(COPEPlugin.getDefault().getVersionedLocalStorage().toPath().toAbsolutePath(), 
				COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath());
		uninstaller = COPEPlugin.getDefault().getUninstaller();
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		uninstaller.initUninstallInMonths(3);

		Files.copy(permanentFile.toPath(), workspaceFile.toPath());
	}

}