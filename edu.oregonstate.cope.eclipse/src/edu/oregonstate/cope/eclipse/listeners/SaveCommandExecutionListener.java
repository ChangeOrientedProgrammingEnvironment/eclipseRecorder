package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;

public class SaveCommandExecutionListener implements IExecutionListener {

	private static long lastSaveAction = 0;

	/**
	 * Returns the time (in milliseconds since The Epoch) of the last save
	 * action.
	 * 
	 * @return
	 */
	public static long getLastSaveAction() {
		return lastSaveAction;
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		if (isFileSave(commandId)) {
			lastSaveAction = System.currentTimeMillis();
		}
	}

	private boolean isFileSave(String commandId) {
		return commandId.equals("org.eclipse.ui.file.save") || commandId.equalsIgnoreCase("org.eclipse.ui.file.saveAll");
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
	}

	@Override
	public void postExecuteFailure(String commandId,
			ExecutionException exception) {
	}

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
	}
}