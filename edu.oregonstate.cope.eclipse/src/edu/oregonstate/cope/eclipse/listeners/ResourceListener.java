package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

public class ResourceListener implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (isSavedAction())
			System.out.println("This is a save action, so I will ignore it");
		else
			System.out.println("This is a genuine refresh");
	}

	private boolean isSavedAction() {
		long currentTime = System.currentTimeMillis();
		return currentTime - SaveCommandExecutionListener.getLastSaveAction() < 500;
	}
}
