package edu.oregonstate.cope.eclipse.ui;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.fileSender.FTPConnectionProperties;

public class FTPPropertiesComposite extends Composite {

	private Text hostname;
	private Text port;
	private Text username;
	private Text password;
	private Text limitText;
	private Button limitButton;

	public FTPPropertiesComposite(Composite parent, int style) {
		super(parent, style);

		Composite composite = this;
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Properties workspaceProperties = COPEPlugin.getDefault()
				.getWorkspaceProperties();

		populateFTPOptions(composite, workspaceProperties);
		populateUploadSettings(composite, workspaceProperties);
	}

	private void populateFTPOptions(Composite composite,
			Properties workspaceProperties) {
		Group sftpGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		sftpGroup.setLayout(new GridLayout(4, false));
		sftpGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		sftpGroup.setText("Credentials for data upload (SFTP)");
		// Label sftpPropertieslabel = new Label(sftpGroup, SWT.NONE);
		// sftpPropertieslabel.setText("SFTP upload credentials");
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
		FTPConnectionProperties ftpProperties = new FTPConnectionProperties();
		String preferencesHostname = workspaceProperties.getProperty(
				COPEPlugin.PREFERENCES_HOSTNAME, "");
		String preferencesPort = workspaceProperties.getProperty(
				COPEPlugin.PREFERENCES_PORT, "");
		String preferencesUsername = workspaceProperties.getProperty(
				COPEPlugin.PREFERENCES_USERNAME, "");
		String preferencesPassword = workspaceProperties.getProperty(
				COPEPlugin.PREFERENCES_PASSWORD, "");
		if (!preferencesHostname.isEmpty() && !preferencesPort.isEmpty()
				&& !preferencesUsername.isEmpty()
				&& !preferencesPassword.isEmpty()) {
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
	}

	private void populateUploadSettings(Composite composite,
			Properties workspaceProperties) {
		Group uploadSettings = new Group(composite, SWT.SHADOW_ETCHED_IN);
		uploadSettings.setLayout(new GridLayout(2, false));
		uploadSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		uploadSettings.setText("Upload options");
		Label limitLabel = new Label(uploadSettings, SWT.NONE);
		limitLabel.setText("Limit Upload");
		limitButton = new Button(uploadSettings, SWT.CHECK);
		Label speedLabel = new Label(uploadSettings, SWT.NONE);
		speedLabel.setText("Limit rate (KB/s):");
		limitText = new Text(uploadSettings, SWT.BORDER);
		limitText.setEnabled(false);
		limitText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		String shouldUploadString = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_SHOULD_LIMIT_UPLOAD, "false");
		boolean shouldUpload = Boolean.parseBoolean(shouldUploadString);
		if (shouldUpload) {
			limitButton.setSelection(true);
			limitText.setEnabled(true);
		}
		
		String limitRate = workspaceProperties.getProperty(COPEPlugin.PREFERENCES_UPLOAD_LIMIT, "200");
		limitText.setText(limitRate);
		
		limitButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				limitText.setEnabled(limitButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
		uploadSettings.pack();
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

	public boolean shouldLimitBandwith() {
		return limitButton.getSelection();
	}

	public int getUploadLimit() throws NumberFormatException {
		try {
			return Integer.parseInt(limitText.getText());
		} catch (NumberFormatException e) {
			throw e;
		}
	}
}
