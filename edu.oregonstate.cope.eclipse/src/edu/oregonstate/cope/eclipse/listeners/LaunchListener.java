package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchListener;

public class LaunchListener implements ILaunchListener {
	
	@Override
	public void launchRemoved(ILaunch launch) {
	}

	@Override
	public void launchChanged(ILaunch launch) {
		System.out.println("Launch started: "
				+ launch.getLaunchConfiguration().getName());
	}

	@Override
	public void launchAdded(ILaunch launch) {
	}
}