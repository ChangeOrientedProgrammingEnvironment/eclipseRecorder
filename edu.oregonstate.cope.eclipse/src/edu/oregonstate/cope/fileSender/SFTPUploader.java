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
	
	private void uploadPathToFTP(String localPath, String remotePath) throws FileNotFoundException, SftpException {
		File[] files = new File(localPath).listFiles();
		try {
			channelSftp.cd( remotePath );
		} catch ( SftpException e ) {
			this.createRemoteDir(remotePath);
		}
//		channelSftp.cd( remotePath );
		for(File file : files) {
			if(file.isFile()) {
				channelSftp.put(new FileInputStream(file), file.getName());
			} else {
				channelSftp.mkdir(remotePath + '/' + file.getName());
				this.uploadPathToFTP(localPath + '/' + file.getName(), file.getName());
			}
		}
	}

}
