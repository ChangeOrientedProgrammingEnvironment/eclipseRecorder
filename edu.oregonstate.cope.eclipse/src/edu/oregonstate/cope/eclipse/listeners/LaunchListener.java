package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.core.DebugCoreMessages;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class LaunchListener implements ILaunchListener {

	@Override
	public void launchRemoved(ILaunch launch) {
	}

	@Override
	public void launchChanged(ILaunch launch) {
	}

	@Override
	public void launchAdded(ILaunch launch) {
		ClientRecorder clientRecorderInstance = COPEPlugin.getDefault().getClientRecorder();
		ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
		String mainType = getMainType(launchConfiguration);
		String launchMode = launch.getLaunchMode();
		String launchTime = launch.getAttribute(DebugPlugin.ATTR_LAUNCH_TIMESTAMP);
		try {
			if (launchMode.equals(ILaunchManager.RUN_MODE))
				clientRecorderInstance.recordNormalLaunch(launchTime, mainType, launchConfiguration.getAttributes());
			if (launchMode.equals(ILaunchManager.DEBUG_MODE))
				clientRecorderInstance.recordDebugLaunch(launchTime, mainType, launchConfiguration.getAttributes());
		} catch (CoreException e) {
			COPEPlugin.getDefault().getLogger().error(this, "Error retrievieng the launch config", e);
		}
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
}