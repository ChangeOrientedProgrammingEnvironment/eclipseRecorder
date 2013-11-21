package edu.oregonstate.cope.eclipse.branding;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class TrimControlContribution extends WorkbenchWindowControlContribution {

	public TrimControlContribution() {
	}

	public TrimControlContribution(String id) {
		super(id);
	}

	@Override
	protected Control createControl(Composite parent) {
		@SuppressWarnings("unused")
		Control[] children = parent.getParent().getParent().getChildren();
		for (Control control : children) {
		}
		return parent;
	}

	
}