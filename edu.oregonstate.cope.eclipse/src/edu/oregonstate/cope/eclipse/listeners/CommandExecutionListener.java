package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.part.FileEditorInput;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class CommandExecutionListener implements IExecutionListener {

	private static boolean saveInProgress = false;

	@SuppressWarnings("restriction")
	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		if (commandId.equalsIgnoreCase(IWorkbenchCommandConstants.EDIT_COPY)) {
			recordCopy();
		}
		if (isFileSave(commandId))
			saveInProgress  = true;
	}

	private void recordCopy() {
		ISelection selection = UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			int offset = textSelection.getOffset();
			int length = textSelection.getLength();
			String text = textSelection.getText();
			String sourceFile = getSourceFile();
			COPEPlugin.getDefault().getClientRecorder().recordCopy(sourceFile, offset, length, text);
		}
	}

	@SuppressWarnings("restriction")
	private String getSourceFile() {
		IEditorPart activeEditor = UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput editorInput = activeEditor.getEditorInput();
		if (editorInput instanceof FileEditorInput)
			return ((FileEditorInput)editorInput).getFile().getFullPath().toPortableString();
		return "";
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