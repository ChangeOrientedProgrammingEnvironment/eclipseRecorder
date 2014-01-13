package edu.oregonstate.cope.eclipse.ui.handlers;

import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ProjectSelectionPage extends Dialog {

	private List<String> projects;

	public ProjectSelectionPage(Shell parentShell, List<String> projects) {
		super(parentShell);
		this.projects = projects;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setSize(400, 600);
		Label label = new Label(composite, SWT.BORDER);	
		label.setText("Please select the project you would like us to record");
		
		Composite buttonsParent = new Composite(composite, SWT.NONE);
		buttonsParent.setLayout(new GridLayout(2, false));
		buttonsParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Button selectAll = new Button(buttonsParent, SWT.NONE);
		selectAll.setText("Select all");
		Button selectNone = new Button(buttonsParent, SWT.NONE);
		selectNone.setText("Select none");
		return composite;
	}
}
