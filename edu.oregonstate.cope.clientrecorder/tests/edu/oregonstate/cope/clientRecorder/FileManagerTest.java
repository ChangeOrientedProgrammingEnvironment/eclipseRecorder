package edu.oregonstate.cope.clientRecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileManagerTest {

	private static FileManager fm;

	@Before
	public void setup() {
		fm = new FileManager();
	}

	@After
	public void tearDown() throws IOException {
		Path parent = fm.getFilePath().getParent();
		fm.deleteEventFiles();

		assertEquals(0, parent.toFile().listFiles().length);
	}

	@Test
	public void testFileExists() throws Exception {
		assertTrue(Files.exists(fm.getFilePath()));
		assertTrue(fm.isCurrentFileEmpty());
	}

	@Test
	public void testFileName() {
		Calendar cal = Calendar.getInstance();
		String expected = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		assertEquals(expected, fm.getFileName());
	}

	@Test
	public void testWriteOnce() throws IOException {
		String expected = "test";

		assertTrue(fm.isCurrentFileEmpty());

		fm.write(expected);

		assertFileContents(expected);
	}

	@Test
	public void testWriteTwice() throws IOException {
		String expected = "test";

		assertTrue(fm.isCurrentFileEmpty());

		fm.write(expected + "\n");
		fm.write(expected);

		assertFileContents(expected + "\n" + expected);
	}

	private void assertFileContents(String expected) throws IOException {
		byte[] fileBytes = Files.readAllBytes(fm.getFilePath());

		if (expected.isEmpty())
			assertTrue(fm.isCurrentFileEmpty());
		else
			assertFalse(fm.isCurrentFileEmpty());

		assertEquals(expected, new String(fileBytes));
	}
}
