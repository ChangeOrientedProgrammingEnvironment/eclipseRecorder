package edu.oregonstate.cope.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class COPEPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	@Override
	protected Control createContents(Composite parent) {
		ProjectSelectionComposite composite = new ProjectSelectionComposite(parent, SWT.NONE, getListOfWorkspaceProjects());
		return composite;
	}
	
	@Override
	protected void performApply() {
		super.performApply();
	}
	
	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
	private List<String> getListOfWorkspaceProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<String> projectNames = new ArrayList<String>();
		for (IProject project : projects) {
			projectNames.add(project.getName());
		}
		
		return projectNames;
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}
