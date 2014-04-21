package edu.oregonstate.cope.eclipse.ui;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
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

	private ProjectSelectionComposite composite;

	@Override
	protected Control createContents(Composite parent) {
		composite = new ProjectSelectionComposite(parent, SWT.NONE, getListOfWorkspaceProjects(), COPEPlugin.getDefault().getIgnoreProjectsList());
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
		}
		return false;
	}
	
	private void saveSelection() {
		List<String> ignoredProjects = composite.getIgnoredProjects();
		COPEPlugin.getDefault().setIgnoredProjectsList(ignoredProjects);
	}

	private void saveFTPProperties() throws UnknownHostException, JSchException {
		String hostname = composite.getHostname();
		int port = Integer.parseInt(composite.getPort());
		String username = composite.getUsername();
		FTPConnectionProperties ftpConnectionProperties = new FTPConnectionProperties();
		String password = composite.getPassword();
		SFTPUploader sftpUploader = new SFTPUploader(hostname, port, username, password);
		COPEPlugin.getDefault().setHostname(hostname);
		COPEPlugin.getDefault().setPort(port);
		COPEPlugin.getDefault().setUsername(username);
		COPEPlugin.getDefault().setPassword(password);
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
