package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.actions.CommandAction;
import org.eclipse.ui.texteditor.StatusLineContributionItem;

import edu.illinois.bundleupdater.BundleUpdater;

@SuppressWarnings("restriction")
public class LogoManager {
	
	private static LogoManager instance;
	
	private final static String STATUS_LINE_CONTRIBUTION_ITEM_ID= "edu.illinois.codingspectator.branding.StatusLine";
	private static final String COMMAND_ID = "edu.oregonstate.edu.cope.eclipse.branding.logoAction";
	
	private final static String NORMAL_LOGO = "icons/cope-logo-normal.png";
	private final static String UPDATE_LOGO = "icons/cope-logo-update.png";

	private IStatusLineManager statusLineManager;
	private String commandToExecute = "org.eclipse.ui.help.aboutAction";
	
	private class CheckForUpdatesJob extends Job {


		public CheckForUpdatesJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			if(new BundleUpdater("http://cope.eecs.oregonstate.edu/client-recorder", "org.oregonstate.edu.eclipse").shouldUpdate()) {
				showUpdateIsAvailable();
				commandToExecute = "org.eclipse.equinox.p2.ui.sdk.update";
			}
			return Status.OK_STATUS;
		}
		
	}
	
	public static LogoManager getInstance() {
		if (instance == null)
			instance = new LogoManager();
		return instance;
	}
	
	private LogoManager() {
	}
	
	private IStatusLineManager getStatusLineManager() {
		if (statusLineManager != null)
			return statusLineManager;
		IWorkbenchWindow activeWindow= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow == null)
			return null;
		
		statusLineManager = ((WorkbenchWindow)activeWindow).getStatusLineManager();
		
		return statusLineManager;
	}

	private void addLogoToStatusLine(String imageFilePath) {
		Image codingspectatorLogo= Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imageFilePath).createImage(); //$NON-NLS-1$
		StatusLineContributionItem contributionItem= new StatusLineContributionItem(STATUS_LINE_CONTRIBUTION_ITEM_ID);
		contributionItem.setImage(codingspectatorLogo);
		contributionItem.setToolTipText("COPE recorder");
		IWorkbench workbench = PlatformUI.getWorkbench();
		getStatusLineManager().add(contributionItem);
		getStatusLineManager().update(false);
		
		CommandAction commandAction = new CommandAction(workbench, COMMAND_ID);
		contributionItem.setActionHandler(commandAction);
		
		new CheckForUpdatesJob("Checking for COPE updates").schedule();
	}
	
	public void showLogo() {
		showLogo(NORMAL_LOGO);
	}

	public synchronized void showLogo(final String imageFilePath) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				addLogoToStatusLine(imageFilePath);
			}
		});
	}
	
	public void removeLogo() {
		getStatusLineManager().remove(STATUS_LINE_CONTRIBUTION_ITEM_ID);
		getStatusLineManager().markDirty();
		getStatusLineManager().update(false);
	}
	
	public void showUpdateIsAvailable() {
		removeLogo();
		showLogo(UPDATE_LOGO);
	}

	public String getCommandToExecute() {
		return commandToExecute;
	}

}
