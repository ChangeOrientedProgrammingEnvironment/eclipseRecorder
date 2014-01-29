package edu.oregonstate.cope.fileSender;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteOldFilesTest {

	private DeleteOldFilesUtil deleteUtil = null;
	private Date testDate = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private String getPathForTestFiles() {
		return "testFiles";
	}
	private File createFile(String name, String[] contents) throws IOException  {
		File file = new File(name);
		file.createNewFile();
		PrintWriter writer;
		try {
			writer = new PrintWriter(name, "UTF-8");
			for(String line : contents) {
				writer.println(line);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File(name);
	}
	
	private void setModifiedDateForFile(File file, String newLastModified) {
		
		try {
			Date newDate = sdf.parse(newLastModified);
			file.setLastModified(newDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private String[] getListOfFilesInDir(String dirPath) {
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> namesList = new ArrayList();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				namesList.add(listOfFiles[i].getName());
			} 
		}
		String[] listArr = new String[namesList.size()];
		return namesList.toArray(listArr);
	}
	
	@Before
	public void setUp() throws Exception {
		new File(getPathForTestFiles()).mkdir();
		deleteUtil = new DeleteOldFilesUtil();
	}

	@Test
	public void testTxtFilesDeletion() {
		try {
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.txt", new String[]{"line1", "line2"});
			setModifiedDateForFile(file1, "2013-12-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.txt", new String[]{"line3", "line4"});
			setModifiedDateForFile(file2, "2013-12-15");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.txt", new String[]{"line5", "line6"});
			setModifiedDateForFile(file3, "2014-01-01");
			Date cutoffDate = sdf.parse("2013-12-31");
			deleteUtil.deleteFilesInDirByPattern(new File(getPathForTestFiles()), ".*\\.txt", cutoffDate.getTime());
			for(String f: getListOfFilesInDir(getPathForTestFiles())) {
				System.out.println(f);
			}
			assertArrayEquals(getListOfFilesInDir(getPathForTestFiles()), new String[] {"file3.txt"});
			FileUtils.cleanDirectory(new File(getPathForTestFiles()));
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		for(String line : getListOfFilesInDir(getPathForTestFiles())) {
			System.out.println(line);
		}
	}
	
	@Test
	public void testZipFilesDeletion() {
		try {
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.zip", new String[]{""});
			setModifiedDateForFile(file1, "2013-01-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.zip", new String[]{""});
			setModifiedDateForFile(file2, "2014-01-10");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.zip", new String[]{""});
			setModifiedDateForFile(file3, "2013-12-30");
			File file4 = createFile(getPathForTestFiles() + File.separator + "file4.txt", new String[]{"line1", "line2"});
			setModifiedDateForFile(file4, "2013-12-30");
			Date cutoffDate = sdf.parse("2013-12-31");
			deleteUtil.deleteFilesInDirByPattern(new File(getPathForTestFiles()), ".*\\.zip", cutoffDate.getTime());
			for(String f: getListOfFilesInDir(getPathForTestFiles())) {
				System.out.println(f);
			}
			assertArrayEquals(getListOfFilesInDir(getPathForTestFiles()), new String[] {"file2.zip", "file4.txt"});
			FileUtils.cleanDirectory(new File(getPathForTestFiles()));
		} catch (IOException | ParseException e) {	
			e.printStackTrace();
		}
	}
	
	@Test
	public void testZipLibFilesDeletion() {
		try {
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.zip", new String[]{""});
			setModifiedDateForFile(file1, "2013-01-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.zip", new String[]{""});
			setModifiedDateForFile(file2, "2014-01-10");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.zip", new String[]{""});
			setModifiedDateForFile(file3, "2013-12-30");
			File file4 = createFile(getPathForTestFiles() + File.separator + "file4.txt", new String[]{"line1", "line2"});
			setModifiedDateForFile(file4, "2013-12-30");
			File file5 = createFile(getPathForTestFiles() + File.separator + "file5.zip-libs", new String[]{""});
			setModifiedDateForFile(file5, "2013-12-29");
			File file6 = createFile(getPathForTestFiles() + File.separator + "file6.zip-libs", new String[]{""});
			setModifiedDateForFile(file6, "2014-06-05");
			File file7 = createFile(getPathForTestFiles() + File.separator + "file7.zipl", new String[]{""});
			setModifiedDateForFile(file7, "2014-06-05");
			Date cutoffDate = sdf.parse("2013-12-31");
			deleteUtil.deleteFilesInDirByPattern(new File(getPathForTestFiles()), ".*\\.zip(-libs)?", cutoffDate.getTime());
			for(String f: getListOfFilesInDir(getPathForTestFiles())) {
				System.out.println(f);
			}
			assertArrayEquals(getListOfFilesInDir(getPathForTestFiles()), new String[] {"file2.zip", "file4.txt", "file6.zip-libs", "file7.zipl"});
			FileUtils.cleanDirectory(new File(getPathForTestFiles()));
		} catch (IOException | ParseException e) {	
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		FileUtils.cleanDirectory(new File(getPathForTestFiles()));
		new File(getPathForTestFiles()).delete();
	}
	

}
