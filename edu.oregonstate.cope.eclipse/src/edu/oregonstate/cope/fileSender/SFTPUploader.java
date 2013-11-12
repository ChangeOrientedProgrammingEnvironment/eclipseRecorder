package edu.oregonstate.cope.fileSender;

import java.io.*;

import com.jcraft.jsch.*;

public class SFTPUploader {
	
	private ChannelSftp channelSftp = null;
	private Session session = null;
	
	private String host = "";
	private String username = "";
	private String password = "";
	
	private void initializeSession(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		
		try {
			JSch jsch = new JSch();
			this.session = jsch.getSession(this.username, this.host, 22);
			this.session.setPassword(this.password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			this.session.setConfig(config);
			this.session.setServerAliveInterval(70000);
			this.session.connect();
			Channel channel = this.session.openChannel("sftp");
			channel.connect();
			this.channelSftp = (ChannelSftp) channel;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			this.channelSftp.exit();
			this.channelSftp.disconnect();
			this.session.disconnect();
		}
	}
	
	public SFTPUploader(String host, String username, String password) throws Exception{
		this.initializeSession(host, username, password);
	}
	
	public void upload(String localPath, String remotePath) throws FileNotFoundException, SftpException, JSchException {
		this.uploadPathToFTP(localPath, remotePath);
	}
	
	private void createRemoteDir(String path) throws SftpException {
		String[] folders = path.split( "/" );
		for ( String folder : folders ) {
		    if ( folder.length() > 0 ) {
		        try {
		            this.channelSftp.cd( folder );
		        }
		        catch ( SftpException e ) {
		        	this.channelSftp.mkdir( folder );
		            this.channelSftp.cd( folder );
		        }
		    }
		}
	}
	
	private void uploadPathToFTP(String localPath, String remotePath) throws FileNotFoundException, SftpException, JSchException {
		File[] files = new File(localPath).listFiles();
		try {
			this.channelSftp.cd( remotePath );
		} catch ( SftpException e ) {
			this.createRemoteDir(remotePath);
		}
		for(File file : files) {
			if(file.isFile()) {
				this.channelSftp.put(new FileInputStream(file), file.getName());
			} else {	
				this.uploadPathToFTP(localPath + '/' + file.getName(),  file.getName());
			}
		}
	}

}
