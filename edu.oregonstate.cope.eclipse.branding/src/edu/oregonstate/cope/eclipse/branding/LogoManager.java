package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.texteditor.StatusLineContributionItem;

@SuppressWarnings("restriction")
public class LogoManager {
	
	private static LogoManager instance;
	
	private final static String STATUS_LINE_CONTRIBUTION_ITEM_ID= "edu.illinois.codingspectator.branding.StatusLine";
	
	private final static String NORMAL_LOGO = "icons/cope-logo-normal.png";
	private final static String UPDATE_LOGO = "icons/cope-logo-update.png";

	private IStatusLineManager statusLineManager;
	
	public static LogoManager getInstance() {
		if (instance == null)
			instance = new LogoManager();
		return instance;
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

	private boolean logoExists() {
		return getStatusLineManager().find(STATUS_LINE_CONTRIBUTION_ITEM_ID) != null;
	}

	private void addLogoToStatusLine() {
		addLogoToStatusLine(NORMAL_LOGO);
	}

	private void addLogoToStatusLine(String imageFilePath) {
		Image codingspectatorLogo= Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imageFilePath).createImage(); //$NON-NLS-1$
		StatusLineContributionItem contributionItem= new StatusLineContributionItem(STATUS_LINE_CONTRIBUTION_ITEM_ID);
		contributionItem.setImage(codingspectatorLogo);
		contributionItem.setToolTipText("COPE recorder");
		getStatusLineManager().add(contributionItem);
		getStatusLineManager().update(false);

		try {
			Assert.isTrue(logoExists());
		} catch (AssertionFailedException e) {
			// add loggin stuff
		}
	}

	public void showLogo() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				addLogoToStatusLine();
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
		addLogoToStatusLine(UPDATE_LOGO);
	}

}
