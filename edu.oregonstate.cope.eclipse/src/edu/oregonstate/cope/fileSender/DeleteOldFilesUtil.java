package edu.oregonstate.cope.fileSender;

import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class DeleteOldFilesUtil {
	
	private static final String ZIP_LIBS_REGEX = ".*\\.zip(-libs)?";
	private static final String EVENTFILE_REGEX = ".*" + File.separator + "eventFiles" + File.separator + ".*";
	
	public String getRootDirPath() {
		return COPEPlugin.getDefault().getLocalStorage().getAbsolutePath();
	}
	
	public void deleteFilesOlderThanNdays(int daysBack, Date date) {
		long purgeTime = date.getTime() - (daysBack * 24 * 60 * 60 * 1000);
		File rootDirectory = new File(getRootDirPath());

		deleteFilesInDirByPattern(rootDirectory, EVENTFILE_REGEX, purgeTime);
		
		deleteFilesInDirByPattern(rootDirectory, ZIP_LIBS_REGEX, purgeTime);
	}
	
	protected void deleteFilesInDirByPattern(File dir, String pattern, long purgeTime) {
	    if(dir.exists()) {
            File[] listFiles = dir.listFiles();            
            if(listFiles != null) {
                for(File listFile : listFiles) {
                    if(listFile.lastModified() < purgeTime &&  
                            (listFile.getName().matches(pattern))) {
                        try {
                            FileUtils.forceDelete(listFile);
                        } catch (Exception e) {
                            COPEPlugin.getDefault().getLogger().error(this, "Unable to delete file: " + listFile);
                        }
                    } 
                }
            } 
        } else {
            COPEPlugin.getDefault().getLogger().info(this, "Files were not deleted. Directory " + dir.getAbsolutePath() + " does not exist!");
        }
	}
}
