package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jcraft.jsch.JSchException;

import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class FileSenderJob implements Job
{
	private static final String LAST_UPLOAD_DATE = "lastUploadDate";
	private static final String LAST_UPLOAD_DATE_FORMAT = "yyyy-MM-dd HH:mm";
			
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(LAST_UPLOAD_DATE_FORMAT);
			String lastUploadDateStr = RecorderFacade.instance().getWorkspaceProperties().getProperty(LAST_UPLOAD_DATE);
			if(lastUploadDateStr != null) {
				Date lastUploadDate = formatter.parse(lastUploadDateStr);
				// delete files created at least 2 days earlier before last upload date
				DeleteOldFilesUtil deleteUtil = new DeleteOldFilesUtil(COPEPlugin.getDefault().getLocalStorage().getAbsolutePath());
				deleteUtil.deleteFilesOlderThanNdays(2, lastUploadDate);
			}
			
			COPEPlugin.getDefault().getLogger().info(this, "Connecting to host " + FTPConnectionProperties.getHost() + " ...");
			
			SFTPUploader uploader = new SFTPUploader(
				FTPConnectionProperties.getHost(), 
				FTPConnectionProperties.getUsername(), 
				FTPConnectionProperties.getPassword()
			);
			String localPath = COPEPlugin.getDefault().getLocalStorage().getAbsolutePath();
			// using eclipse workspace ID as a remote dir to store data
			String remotePath = "COPE" + File.separator + COPEPlugin.getDefault().getWorkspaceID();
			
			COPEPlugin.getDefault().getLogger().info(this, "Sending files from " + localPath + " to " + FTPConnectionProperties.getHost() + ":" + remotePath + " ...");
			
			uploader.createRemoteDir(remotePath);
			uploader.upload(localPath, remotePath);
			RecorderFacade.instance().getWorkspaceProperties().addProperty(LAST_UPLOAD_DATE, formatter.format(new Date()));
			
			COPEPlugin.getDefault().getLogger().info(this, "Upload finished");
			
		} catch (UnknownHostException | JSchException e) {
			COPEPlugin.getDefault().getLogger().warning(FTPConnectionProperties.class, "Cannot connect to host: " + FTPConnectionProperties.getHost());
		} catch (Exception e) {
			COPEPlugin.getDefault().getLogger().error(FTPConnectionProperties.class, e.getMessage(), e);
		} 
	}
	
}