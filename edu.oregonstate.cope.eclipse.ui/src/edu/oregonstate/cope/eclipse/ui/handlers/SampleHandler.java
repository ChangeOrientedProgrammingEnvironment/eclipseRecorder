package edu.oregonstate.cope.eclipse.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Ui",
//				"Hello, Eclipse world");
		DemoSurveyDialog dsd = new DemoSurveyDialog(window.getShell());
		dsd.open();
//		
//		// customized MessageDialog with configured buttons
//		MessageDialog dialog = new MessageDialog(window.getShell(), "My Title", null,
//		    "My message", MessageDialog.ERROR, new String[] { "First",
//		  "Second", "Third" }, 0);
//		int result = dialog.open();
//		System.out.println(result); 
		
		
		
		return null;
	}
}
