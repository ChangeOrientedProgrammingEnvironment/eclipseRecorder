package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class Handler extends AbstractHandler {
	
	private void runCommand(String commandId) {
		ICommandService commandService= (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command= commandService.getCommand(commandId);
		if (!command.isDefined()) {
			return;
		}
		IHandlerService handlerService= (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(commandId, null);
		} catch (ExecutionException e) {
		} catch (NotDefinedException e) {
		} catch (NotEnabledException e) {
		} catch (NotHandledException e) {
		}
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		String commandID = LogoManager.getInstance().getCommandToExecute();
		runCommand(commandID);
		return null;
	}

}
