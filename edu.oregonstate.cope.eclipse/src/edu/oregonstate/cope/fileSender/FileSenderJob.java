package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
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
				deleteFilesOlderThanNdays(2, lastUploadDate);
			}
			
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
			RecorderFacade.instance().getWorkspaceProperties().addProperty(LAST_UPLOAD_DATE, formatter.format(new Date()));
			
			COPEPlugin.getDefault().getLogger().info(this, "Upload finished");
		} catch (UnknownHostException | JSchException e) {
			COPEPlugin.getDefault().getLogger().warning(FTPConnectionProperties.class, "Cannot connect to host: " + FTPConnectionProperties.getHost());
		} catch (Exception e) {
			COPEPlugin.getDefault().getLogger().error(FTPConnectionProperties.class, e.getMessage(), e);
		} 
	}
		
	public void deleteFilesOlderThanNdays(int daysBack, Date date) {
		long purgeTime = date.getTime() - (daysBack * 24 * 60 * 60 * 1000);
		String dirWay = COPEPlugin.getLocalStorage().getAbsolutePath() + File.separator + EventFilesProvider.EVENTFILE_ROOTDIR;
		File eventsDirectory = new File(dirWay);
		if(eventsDirectory.exists()) {
			File[] listFiles = eventsDirectory.listFiles();			 
			for(File listFile : listFiles) {
				if(listFile.lastModified() < purgeTime) {
					try {
						FileUtils.forceDelete(listFile);
					} catch (Exception e) {
						COPEPlugin.getDefault().getLogger().error(this, "Unable to delete file: " + listFile);
					}
				}
			}
		} else {
			COPEPlugin.getDefault().getLogger().info(this, "Files were not deleted, directory " + dirWay + " does'nt exist!");
		}
		
		File rootDirectory = new File(COPEPlugin.getLocalStorage().getAbsolutePath());
		
		if(rootDirectory.exists()) {
			File[] listFiles = rootDirectory.listFiles();			 
			if(listFiles != null) {
				for(File listFile : listFiles) {
					if(listFile.lastModified() < purgeTime &&  
							(listFile.getName().matches(".*\\.zip") || listFile.getName().matches(".*\\.zip-libs"))) {
						try {
							FileUtils.forceDelete(listFile);
						} catch (Exception e) {
							COPEPlugin.getDefault().getLogger().error(this, "Unable to delete file: " + listFile);
						}
					} 
				}
			} 
		} 
	}
}