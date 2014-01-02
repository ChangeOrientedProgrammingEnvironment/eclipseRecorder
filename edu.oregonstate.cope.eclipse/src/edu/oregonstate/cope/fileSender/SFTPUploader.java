package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUploader {
	
	private ChannelSftp channelSftp = null;
	private Session session = null;
	
	private String host = "";
	private String username = "";
	private String password = "";
	
	private void initializeSession(String host, String username, String password) throws UnknownHostException, JSchException {
		this.host = host;
		this.username = username;
		this.password = password;
		
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
	
	}
	
	public SFTPUploader(String host, String username, String password) throws UnknownHostException, JSchException {
		this.initializeSession(host, username, password);
	}
	
	public void upload(String localPath, String remotePath) throws FileNotFoundException, SftpException, JSchException {
		if(this.channelSftp != null) {
			this.uploadPathToFTP(localPath, remotePath);			
		}
	}
	
	public void createRemoteDir(String path) throws SftpException {
		String[] folders = path.split( java.util.regex.Pattern.quote(File.separator) );
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

		for(File file : files) {
			if(file.isFile()) {
				this.channelSftp.put(new FileInputStream(file), file.getName());
			} else {	
				this.createRemoteDir(file.getName());
				this.uploadPathToFTP(localPath + File.separator + file.getName(),  file.getName());
				this.channelSftp.cd("..");
			}
		}
	}
}
