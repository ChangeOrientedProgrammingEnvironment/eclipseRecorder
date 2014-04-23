package edu.oregonstate.cope.eclipse.installer;

import edu.oregonstate.cope.clientRecorder.installer.InstallerHelper;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class EclipseInstallerHelper implements InstallerHelper{

	@Override
	public String getPluginVersion() {
		return COPEPlugin.getDefault().getPluginVersion().toString();
	}

	@Override
	public void takeSnapshotOfAllProjects() {
		COPEPlugin.getDefault().takeSnapshotOfKnownProjects();
	}

}
