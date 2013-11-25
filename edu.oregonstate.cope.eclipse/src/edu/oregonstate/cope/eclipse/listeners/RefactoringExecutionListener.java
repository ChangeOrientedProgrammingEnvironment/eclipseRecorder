package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptorProxy;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;

import edu.oregonstate.cope.eclipse.COPEPlugin;

/**
 * I listen for refactoring executions.
 * 
 * @author Caius Brindescu
 *
 */
public class RefactoringExecutionListener implements
		IRefactoringExecutionListener {
	
	private static boolean isRefactoringInProgress = false;
	private static String refactoringName = "";
	
	@Override
	public void executionNotification(RefactoringExecutionEvent event) {
		if (event.getEventType() == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			isRefactoringInProgress = true;
			refactoringName = getRefactoringID(event);
			
			COPEPlugin.getDefault().getLogger().info(this, getRefactoringID(event) + " refactoring started");
		}
		if (event.getEventType() == RefactoringExecutionEvent.PERFORMED) {
			isRefactoringInProgress = false;
			refactoringName = "";
			
			COPEPlugin.getDefault().getLogger().info(this, getRefactoringID(event) + " refactoring done");
		}
	}

	private String getRefactoringID(RefactoringExecutionEvent event) {
		RefactoringDescriptorProxy refactoringDescriptorProxy = event.getDescriptor();
		RefactoringDescriptor refactoringDescriptor = refactoringDescriptorProxy.requestDescriptor(new NullProgressMonitor());
		String refactoringId = refactoringDescriptor.getID();
		return refactoringId;
	}
	
	/**
	 * I return true if a refactoring is in progress.
	 * @return true if a refactoring is in progress
	 */
	public static boolean isRefactoringInProgress() {
		return isRefactoringInProgress;
	}
	
	/**
	 * I return the name of the current refactoring that is being executed, 
	 * or the empty String if no refactoring is being executed.
	 * 
	 * @return the ID of the refactoring being executed
	 */
	public static String getRefactoringName() {
		return refactoringName;
	}
}