package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;

/**
 * I listen for refactoring executions.
 * 
 * @author Caius Brindescu
 *
 */
public class RefactoringExecutionListener implements
		IRefactoringExecutionListener {
	
	private static boolean isRefactoringInProgress = false;
	
	@Override
	public void executionNotification(RefactoringExecutionEvent event) {
		if (event.getEventType() == RefactoringExecutionEvent.ABOUT_TO_PERFORM)
			isRefactoringInProgress = true;
		if (event.getEventType() == RefactoringExecutionEvent.PERFORMED)
			isRefactoringInProgress = false;
	}
	
	/**
	 * I return true if a refactoring is in progress.
	 * @return true if a refactoring is in progress
	 */
	public static boolean isRefactoringInProgress() {
		return isRefactoringInProgress;
	}
}