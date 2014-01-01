package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IWorkbenchCommandConstants;

public class SaveCommandExecutionListener implements IExecutionListener {

	private static boolean saveInProgress = false;

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		System.out.println("Starting executing: " + commandId);
		if (isFileSave(commandId))
			saveInProgress  = true;
	}

	private boolean isFileSave(String commandId) {
		return commandId.equals(IWorkbenchCommandConstants.FILE_SAVE) || commandId.equalsIgnoreCase(IWorkbenchCommandConstants.FILE_SAVE_ALL);
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		if (isFileSave(commandId))
			saveInProgress = false;
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
		if (isFileSave(commandId))
			saveInProgress = false;
	}

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
	}

	public static boolean isSaveInProgress() {
		return saveInProgress;
	}
}