package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				//Activator.getDefault().getStatusLineUpdater().updateStatusLine();
			}
		});
	}

}
