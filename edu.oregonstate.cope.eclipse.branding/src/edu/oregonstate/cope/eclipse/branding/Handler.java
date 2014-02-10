package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;

public class Handler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) {
		LogoManager.getInstance().showUpdateIsAvailable();
		return null;
	}

}
