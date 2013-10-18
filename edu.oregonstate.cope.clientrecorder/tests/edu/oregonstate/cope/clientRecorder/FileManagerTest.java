package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileManagerTest {

	private FileManager fm;

	@Before
	public void setup() {
		fm = new FileManager();
	}

	@Test
	public void testFileExists() throws Exception {
		assertTrue(Files.exists(fm.getFilePath()));
	}

	@Test
	public void testFileName() throws Exception {
		Calendar cal = Calendar.getInstance();
		String expected = cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		assertEquals(fm.getFileName(), expected);
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
