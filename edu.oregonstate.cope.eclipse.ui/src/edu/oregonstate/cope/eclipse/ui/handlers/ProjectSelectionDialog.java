package edu.oregonstate.cope.eclipse.ui.handlers;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.oregonstate.cope.eclipse.ui.ProjectSelectionComposite;

public class ProjectSelectionDialog extends Dialog {

	private List<String> projects;
	private List<String> ignoredProjects;
	private ProjectSelectionComposite selectionComposite;

	public ProjectSelectionDialog(Shell parentShell, List<String> projects) {
		super(parentShell);
		this.projects = projects;
	}

	@Override
	protected Control createContents(Composite parent) {
		selectionComposite = new ProjectSelectionComposite(parent, SWT.NONE, projects);
		return super.createContents(parent);
	}
	
	@Override
	protected void okPressed() {
		ignoredProjects = selectionComposite.getIgnoredProjects();
		super.okPressed();
	}
	
	public List<String> getIgnoredProjects() {
		return ignoredProjects;
	}
}
