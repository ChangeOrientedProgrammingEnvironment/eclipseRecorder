package edu.oregonstate.cope.eclipse.ui;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.fileSender.FTPConnectionProperties;

public class ProjectSelectionComposite extends Composite {

	private List<TableItem> tableItems;
	private Text hostname;
	private Text port;
	private Text username;
	private Text password;
	
	public ProjectSelectionComposite(Composite parent, int style, List<String> projects, List<String> ignoredProjects) {
		super(parent, style);
		
		Composite composite = this;
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setSize(400, 600);
		
		Group sftpGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		sftpGroup.setLayout(new GridLayout(4, false));
		sftpGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		sftpGroup.setSize(400,100);
		sftpGroup.setLocation(0,0);
		sftpGroup.setText("Credentials for data upload (SFTP)");
		//Label sftpPropertieslabel = new Label(sftpGroup, SWT.NONE);	
		//sftpPropertieslabel.setText("SFTP upload credentials");
		Label hostnameLabel = new Label(sftpGroup, SWT.NONE);	
		hostnameLabel.setText("Hostname:");
		hostname = new Text(sftpGroup, SWT.BORDER);
		Label portLabel = new Label(sftpGroup, SWT.NONE);	
		portLabel.setText("Port:");
		port = new Text(sftpGroup, SWT.BORDER);
		Label usernameLabel = new Label(sftpGroup, SWT.NONE);	
		usernameLabel.setText("Username:");
		username = new Text(sftpGroup, SWT.BORDER);
		Label passwordLabel = new Label(sftpGroup, SWT.NONE);	
		passwordLabel.setText("Password:");
		password = new Text(sftpGroup, SWT.PASSWORD | SWT.BORDER);
		
		// Getting stored FTP properties
		Properties workspaceProperties = COPEPlugin.getDefault().getWorkspaceProperties();
		FTPConnectionProperties ftpProperties = new FTPConnectionProperties();
		String preferencesHostname = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_HOSTNAME);
		String preferencesPort = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_PORT);
		String preferencesUsername = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_USERNAME);
		String preferencesPassword = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_PASSWORD);
		if(preferencesHostname != null && !preferencesHostname.isEmpty() 
			&& preferencesPort != null && !preferencesPort.isEmpty()
			&& preferencesUsername != null && !preferencesUsername.isEmpty()
			&& preferencesPassword != null && !preferencesPassword.isEmpty()
		) {
			hostname.setText(preferencesHostname);
			port.setText(preferencesPort);
			username.setText(preferencesUsername);
			try {
				password.setText(ftpProperties.decrypt(preferencesPassword));
			} catch (GeneralSecurityException | IOException e1) {
				e1.printStackTrace();
			}
		} else {
			hostname.setText(ftpProperties.getHost());
			port.setText(Integer.toString(ftpProperties.getPort()));
			username.setText(ftpProperties.getUsername());
			password.setText(ftpProperties.getPassword());
		}
		sftpGroup.pack();
		
		Group projectsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		projectsGroup.setLayout(new GridLayout(1, true));
		projectsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		projectsGroup.setSize(400,500);
		projectsGroup.setLocation(0,100);
		projectsGroup.setText("Projects recording configuration");
		Label label = new Label(projectsGroup, SWT.BORDER);	
		label.setText("Please select the project you would like us to record");
		
		Composite buttonsParent = new Composite(projectsGroup, SWT.NONE);
		buttonsParent.setLayout(new GridLayout(2, false));
		buttonsParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Button selectAll = new Button(buttonsParent, SWT.NONE);
		selectAll.setText("Select all");
		selectAll.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				for(TableItem item : tableItems)
					item.setChecked(true);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		Button selectNone = new Button(buttonsParent, SWT.NONE);
		selectNone.setText("Select none");
		selectNone.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				for(TableItem item : tableItems)
					item.setChecked(false);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Composite projectList = new Composite(projectsGroup, SWT.BORDER);
		projectList.setLayout(new GridLayout(1, true));
		projectList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Table table = new Table(projectList, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL);
		table.setLayout(new GridLayout(1,true));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderVisible(false);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText("Project");
		
		tableItems = new ArrayList<TableItem>();
		for (String project : projects) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(project);
			if (ignoredProjects.contains(project))
				tableItem.setChecked(false);
			else
				tableItem.setChecked(true);
			tableItems.add(tableItem);
		}
		tableColumn.pack();
		projectsGroup.pack();
	}

	public ProjectSelectionComposite(Composite parent, int style, List<String> projects) {
		this(parent, style, projects, new ArrayList<String>());
	}
	
	public List<String> getIgnoredProjects() {
		ArrayList<String> ignoredProjects = new ArrayList<String>();
		for (TableItem item : tableItems) {
			if(!item.getChecked())
				ignoredProjects.add(item.getText());
		}
		
		return ignoredProjects;
	}

	public String getHostname() {
		return hostname.getText();
	}

	public String getPort() {
		return port.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	public String getPassword() {
		return password.getText();
	}

}
