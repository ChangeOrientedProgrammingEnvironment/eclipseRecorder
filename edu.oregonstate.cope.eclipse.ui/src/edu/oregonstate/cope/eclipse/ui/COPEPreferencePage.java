package edu.oregonstate.cope.eclipse.ui;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.jcraft.jsch.JSchException;

import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.fileSender.FTPConnectionProperties;
import edu.oregonstate.cope.fileSender.SFTPUploader;

public class COPEPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private ProjectSelectionComposite projectSelectionComposite;
	private FTPPropertiesComposite ftpPropertiesComposite;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		projectSelectionComposite = new ProjectSelectionComposite(composite, SWT.NONE, getListOfWorkspaceProjects(), COPEPlugin.getDefault().getIgnoreProjectsList());
		ftpPropertiesComposite = new FTPPropertiesComposite(composite, SWT.NONE);
		
		return composite;
	}
	
	@Override
	protected void performApply() {
		saveSelection();
		super.performApply();
	}
	
	@Override
	public boolean performOk() {
		try {
			saveSelection();
			saveFTPProperties();
			saveUploadOptions();
			return super.performOk();
		} catch (UnknownHostException e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageBox mBox = new MessageBox(shell, SWT.ICON_WARNING);
			mBox.setText("Warning");
			mBox.setMessage("Unable to connect to host ");
			mBox.open();
		} catch (JSchException e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageBox mBox = new MessageBox(shell, SWT.ICON_WARNING);
			mBox.setText("Warning");
			mBox.setMessage("Unable to establish connection using specified credentials ");
			mBox.open();
		} catch (NumberFormatException e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageBox mBox = new MessageBox(shell, SWT.ICON_WARNING);
			mBox.setText("Error");
			mBox.setMessage("The value for the upload limit is invalid!");
			mBox.open();
		}
		return false;
	}
	
	private void saveSelection() {
		List<String> ignoredProjects = projectSelectionComposite.getIgnoredProjects();
		COPEPlugin.getDefault().setIgnoredProjectsList(ignoredProjects);
	}

	private void saveFTPProperties() throws UnknownHostException, JSchException {
		String hostname = ftpPropertiesComposite.getHostname();
		int port = Integer.parseInt(ftpPropertiesComposite.getPort());
		String username = ftpPropertiesComposite.getUsername();
		FTPConnectionProperties ftpConnectionProperties = new FTPConnectionProperties();
		String password = ftpConnectionProperties.getPassword();
		COPEPlugin.getDefault().setHostname(hostname);
		COPEPlugin.getDefault().setPort(port);
		COPEPlugin.getDefault().setUsername(username);
		COPEPlugin.getDefault().setPassword(password);
	}
	
	private void saveUploadOptions() throws NumberFormatException {
		boolean shouldLimitBandwith = ftpPropertiesComposite.shouldLimitBandwith();
		COPEPlugin.getDefault().setShouldLimitUploadRate(shouldLimitBandwith);
		if (shouldLimitBandwith)
			COPEPlugin.getDefault().setUploadLimit(ftpPropertiesComposite.getUploadLimit());
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
