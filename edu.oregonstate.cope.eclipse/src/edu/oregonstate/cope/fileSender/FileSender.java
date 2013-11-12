package edu.oregonstate.cope.fileSender;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class FileSender {
	
	public FileSender() throws ParseException, SchedulerException  {
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setName("ftpUploadTrigger");
		String cronJobConfig = FTPConnectionProperties.getCronConfiguration();
		cronTrigger.setCronExpression(cronJobConfig);
		
		Trigger nowTrigger = new SimpleTrigger();
		nowTrigger.setName("File Send Now");
		nowTrigger.setStartTime(new Date());
		
		JobDetail jobCron = new JobDetail();
		jobCron.setName("File Sender Job");
		jobCron.setJobClass(FileSenderJob.class);
		
		JobDetail jobNow = new JobDetail();
		jobNow.setName("File Send Now");
		jobNow.setJobClass(FileSenderJob.class);
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobCron, cronTrigger);
		scheduler.scheduleJob(jobNow, nowTrigger);
		
	}
}