package edu.oregonstate.cope.fileSender;

import java.io.*;

import com.jcraft.jsch.*;

public class SFTPUploader {
	
	private ChannelSftp channelSftp = null;
	private Channel channel = null;
	private Session session = null;
	
	public SFTPUploader(String host, String username, String password) throws Exception{
		try {
			JSch jsch = new JSch();
			this.session = jsch.getSession(username, host, 22);
			this.session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			this.session.setConfig(config);
			this.session.connect();
			this.channel = session.openChannel("sftp");
			this.channel.connect();
			this.channelSftp = (ChannelSftp) channel;
			
		} catch (Exception ex) {
//			 System.out.println("Exception found while tranfer the response.");
		}
		finally{
			channelSftp.exit();
			channel.disconnect();
			this.session.disconnect();
		}
	}
	
	public void upload(String localPath, String remotePath) throws FileNotFoundException, SftpException {
		this.uploadPathToFTP(localPath, remotePath);
	}
	
	private void uploadPathToFTP(String localPath, String remotePath) throws FileNotFoundException, SftpException {
		File[] files = new File(localPath).listFiles();
		for(File file : files) {
			if(file.isFile()) {
				this.channelSftp.put(new FileInputStream(file), remotePath + '/' + file.getName());
			} else {
				this.channelSftp.mkdir(remotePath + '/' + file.getName());
				this.uploadPathToFTP(localPath + '/' + file.getName(), remotePath + '/' + file.getName());
			}
		}
	}

}
