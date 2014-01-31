package edu.oregonstate.cope.fileSender;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DeleteOldFilesTest {

	private DeleteOldFilesUtil deleteUtil = null;
	private Date testDate = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private String getPathForTestFiles() {
		return "testFiles";
	}
	private File createFile(String name, String[] contents, String creationDate) throws IOException  {
		File file = new File(name);
		
		file.getParentFile().mkdirs();
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
		
		setModifiedDateForFile(file, creationDate);
		
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
		deleteUtil = new DeleteOldFilesUtil(getPathForTestFiles());
	}

	@Test
	public void testTxtFilesDeletion() {
		try {
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.txt", new String[]{"line1", "line2"}, "2013-12-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.txt", new String[]{"line3", "line4"}, "2013-12-15");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.txt", new String[]{"line5", "line6"}, "2014-01-01");
			
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
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.zip", new String[]{""}, "2013-01-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.zip", new String[]{""}, "2014-01-10");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.zip", new String[]{""}, "2013-12-30");
			File file4 = createFile(getPathForTestFiles() + File.separator + "file4.txt", new String[]{"line1", "line2"}, "2013-12-30");

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
			File file1 = createFile(getPathForTestFiles() + File.separator + "file1.zip", new String[]{""}, "2013-01-01");
			File file2 = createFile(getPathForTestFiles() + File.separator + "file2.zip", new String[]{""}, "2014-01-10");
			File file3 = createFile(getPathForTestFiles() + File.separator + "file3.zip", new String[]{""}, "2013-12-30");
			File file4 = createFile(getPathForTestFiles() + File.separator + "file4.txt", new String[]{"line1", "line2"}, "2013-12-30");
			File file5 = createFile(getPathForTestFiles() + File.separator + "file5.zip-libs", new String[]{""}, "2013-12-29");
			File file6 = createFile(getPathForTestFiles() + File.separator + "file6.zip-libs", new String[]{""}, "2014-06-05");
			File file7 = createFile(getPathForTestFiles() + File.separator + "file7.zipl", new String[]{""}, "2014-06-05");

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
	
	@Test
	public void testHierarchy() throws Exception {
		File event1 = createFile(Paths.get(getPathForTestFiles(), "0.7.0.201401211052", "eventFiles", "2010-01-01").toAbsolutePath().toString(), new String[]{"1", "2"}, "2010-01-01");
		File event2 = createFile(Paths.get(getPathForTestFiles(), "0.7.0.201401211052", "eventFiles", "2010-01-02").toAbsolutePath().toString(), new String[]{"1", "2"}, "2010-01-02");
		File event3 = createFile(Paths.get(getPathForTestFiles(), "0.9.0.201234321052", "eventFiles", "2013-01-02").toAbsolutePath().toString(), new String[]{"1", "2"}, "2013-01-02");
		
		File zip1 = createFile(getPathForTestFiles() + File.separator + "file1.zip", new String[]{"dfsdfdsf"}, "2010-01-01");
		File zip2 = createFile(getPathForTestFiles() + File.separator + "file2.zip", new String[]{"dsfsdfsdf"}, "2013-01-01");
		
		Date referenceDate = sdf.parse("2013-01-02");
		deleteUtil.deleteFilesOlderThanNdays(100, referenceDate);
		
		assertFalse(event1.exists());
		assertFalse(event2.exists());
		assertFalse(zip1.exists());
		
		assertTrue(event3.exists());
		assertTrue(zip2.exists());
		
		FileUtils.cleanDirectory(new File(getPathForTestFiles()));
	}
	
	@After
	public void tearDown() throws Exception {
		FileUtils.cleanDirectory(new File(getPathForTestFiles()));
		new File(getPathForTestFiles()).delete();
	}
}
