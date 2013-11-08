package edu.oregonstate.cope.fileSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.jcraft.jsch.SftpException;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class FileSender {
	
	class FileSenderJob implements Job
	{
		public void execute(JobExecutionContext context) throws JobExecutionException {
			try {
				SFTPUploader uploader = new SFTPUploader(
						FTPConnectionProperties.getHost(), 
						FTPConnectionProperties.getUsername(), 
						FTPConnectionProperties.getPassword()
				);
				// using eclipse workspace ID as a remote dir to store data
				uploader.upload(COPEPlugin.getLocalStorage().getAbsolutePath(), COPEPlugin.getDefault().getWorkspaceID());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public FileSender() throws ParseException, SchedulerException  {
		CronTrigger trigger = new CronTrigger();
		trigger.setName("ftpUploadTrigger");
		trigger.setCronExpression(FTPConnectionProperties.getCronConfiguration()); 
		
		JobDetail job = new JobDetail();
		job.setName("File Sender Job");
		job.setJobClass(FileSenderJob.class);
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
	}
}