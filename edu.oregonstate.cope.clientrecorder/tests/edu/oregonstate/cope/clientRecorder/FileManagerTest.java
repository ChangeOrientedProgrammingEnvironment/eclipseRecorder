package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest {

	private static FileManager fm;

	@Before
	public void setup() {
		fm = new FileManager();
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		Path parent = fm.getFilePath().getParent();
		fm.deleteEventFiles();
		
		assertEquals(0, parent.toFile().listFiles().length);
	}

	@Test
	public void testFileExists() throws Exception {
		assertTrue(Files.exists(fm.getFilePath()));
	}

	@Test
	public void testFileName() throws Exception {
		Calendar cal = Calendar.getInstance();
		String expected = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		assertEquals(expected, fm.getFileName());
	}

	@Test
	public void testGetSameFileAfterEdit() throws IOException{
		String expected = "test";
		
		try (FileWriter fw = new FileWriter(fm.getFilePath().toFile())) {
			fw.write(expected);
		}
		
		byte[] lines = Files.readAllBytes(fm.getFilePath());
		assertTrue(lines.length == expected.length());
		assertTrue(Arrays.equals(lines, expected.getBytes()));
	}
}
