package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class DeleteOldFilesUtil {
	
	private static final String ZIP_LIBS_REGEX = ".*\\.zip(-libs)?";
	private static final String EVENTFILE_REGEX = ".*" + File.separator + "eventFiles" + File.separator + ".*";
	private String rootPath;
	
	public DeleteOldFilesUtil(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public void deleteFilesOlderThanNdays(int daysBack, Date date) {
		
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(date);
		calendarDate.add(Calendar.DAY_OF_MONTH, -daysBack);
		
		long purgeTime = calendarDate.getTimeInMillis();

		File rootDirectory = new File(rootPath);
		
		deleteFilesInDirByPattern(rootDirectory, EVENTFILE_REGEX, purgeTime);
		
		deleteFilesInDirByPattern(rootDirectory, ZIP_LIBS_REGEX, purgeTime);
	}
	
	protected void deleteFilesInDirByPattern(File dir, final String pattern, final long purgeTime) {
		try {
			Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					File listFile = file.toFile();

					boolean nameMatches = listFile.getAbsolutePath().matches(pattern);
					
					boolean isOld = listFile.lastModified() < purgeTime;
					
					if (isOld && nameMatches) {
						try {
							FileUtils.forceDelete(listFile);
						} catch (Exception e) {
							COPEPlugin.getDefault().getLogger().error(this, "Unable to delete file: " + listFile);
						}

					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//	    if(dir.exists()) {
//            File[] listFiles = dir.listFiles();            
//            if(listFiles != null) {
//                for(File listFile : listFiles) {
//                    if(listFile.lastModified() < purgeTime &&  
//                            (listFile.getName().matches(pattern))) {
//                        try {
//                            FileUtils.forceDelete(listFile);
//                        } catch (Exception e) {
//                            COPEPlugin.getDefault().getLogger().error(this, "Unable to delete file: " + listFile);
//                        }
//                    } 
//                }
//            } 
//        } else {
//            COPEPlugin.getDefault().getLogger().info(this, "Files were not deleted. Directory " + dir.getAbsolutePath() + " does not exist!");
//        }
	}
}
