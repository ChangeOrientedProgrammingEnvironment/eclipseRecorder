package edu.oregonstate.cope.fileSender;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class FileSenderJob implements Job
{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			System.out.println("Connecting to host " + FTPConnectionProperties.getHost() + " ...");
			SFTPUploader uploader = new SFTPUploader(
				FTPConnectionProperties.getHost(), 
				FTPConnectionProperties.getUsername(), 
				FTPConnectionProperties.getPassword()
			);
			String localPath = COPEPlugin.getLocalStorage().getAbsolutePath();
			// using eclipse workspace ID as a remote dir to store data
			String remotePath = "COPE/" + COPEPlugin.getDefault().getWorkspaceID();
			System.out.println("Sending files from " + localPath + " to " + FTPConnectionProperties.getHost() + ":" + remotePath + " ...");
			uploader.upload(localPath, remotePath);
			System.out.println("Upload finished");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}