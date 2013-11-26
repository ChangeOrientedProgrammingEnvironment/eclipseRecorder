package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.net.UnknownHostException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jcraft.jsch.JSchException;

import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class FileSenderJob implements Job
{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			COPEPlugin.getDefault().getLogger().info(this, "Connecting to host " + FTPConnectionProperties.getHost() + " ...");
			
			SFTPUploader uploader = new SFTPUploader(
				FTPConnectionProperties.getHost(), 
				FTPConnectionProperties.getUsername(), 
				FTPConnectionProperties.getPassword()
			);
			String localPath = COPEPlugin.getLocalStorage().getAbsolutePath();
			// using eclipse workspace ID as a remote dir to store data
			String remotePath = "COPE" + File.separator + COPEPlugin.getDefault().getWorkspaceID();
			
			COPEPlugin.getDefault().getLogger().info(this, "Sending files from " + localPath + " to " + FTPConnectionProperties.getHost() + ":" + remotePath + " ...");
			
			uploader.createRemoteDir(remotePath);
			uploader.upload(localPath, remotePath);
			
			COPEPlugin.getDefault().getLogger().info(this, "Upload finished");
			
		} catch (UnknownHostException | JSchException e) {
			COPEPlugin.getDefault().getLogger().warning(FTPConnectionProperties.class, "Cannot connect to host: " + FTPConnectionProperties.getHost());
		} catch (Exception e) {
			COPEPlugin.getDefault().getLogger().error(FTPConnectionProperties.class, e.getMessage(), e);
		} 
	}
}