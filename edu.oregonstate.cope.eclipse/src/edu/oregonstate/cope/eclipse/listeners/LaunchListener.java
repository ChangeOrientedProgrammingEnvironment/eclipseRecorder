package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class LaunchListener implements ILaunchListener {
	
	@Override
	public void launchRemoved(ILaunch launch) {
	}

	@Override
	public void launchChanged(ILaunch launch) {
		ClientRecorder clientRecorderInstance = COPEPlugin.getDefault().getClientRecorder();
		ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
		String mainType = getMainType(launchConfiguration);
		String launchMode = launch.getLaunchMode();
		if (isTestLauch(launchConfiguration))
			return;
		if (launchMode.equals(ILaunchManager.RUN_MODE))
			clientRecorderInstance.recordNormalLaunch(mainType);
		if (launchMode.equals(ILaunchManager.DEBUG_MODE))
			clientRecorderInstance.recordDebugLaunch(mainType);
		
	}

	private boolean isTestLauch(ILaunchConfiguration launchConfiguration) {
		try {
			String attribute = launchConfiguration.getAttribute("org.eclipse.jdt.junit.TEST_KIND", "");
			if (!attribute.equals(""))
				return true;
		} catch (CoreException e) {
		}
		return false;
	}

	private String getMainType(ILaunchConfiguration launchConfiguration) {
		try {
			return launchConfiguration.getAttribute("org.eclipse.jdt.launching.MAIN_TYPE", "");
		} catch (CoreException e) {
		}
		return "";
	}

	@Override
	public void launchAdded(ILaunch launch) {
	}
}