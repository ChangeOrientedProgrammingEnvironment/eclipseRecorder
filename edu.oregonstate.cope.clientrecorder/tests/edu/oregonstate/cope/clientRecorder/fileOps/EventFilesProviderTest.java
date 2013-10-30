package edu.oregonstate.cope.clientRecorder.fileOps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventFilesProviderTest {

	private static FileProvider fm;

	@Before
	public void setup() throws IOException {
		fm = new EventFilesProvider();
		fm.setRootDirectory("outputFiles");
	}

	@After
	public void tearDown() throws IOException {
		Path parent = fm.getCurrentFilePath().getParent();
		fm.deleteFiles();

		assertEquals(0, parent.toFile().listFiles().length);

		Files.delete(parent);
	}

	@Test
	public void testRootDirectory() throws Exception {
		assertEquals(EventFilesProvider.EVENTFILE_ROOTDIR, fm.rootDirectory.getFileName().toString());
	}

	@Test
	public void testFileName() {
		Calendar cal = Calendar.getInstance();
		String expected = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

		assertEquals(expected, fm.getFileName());
	}

	@Test
	public void testFileEmpty() throws Exception {
		assertTrue(fm.isCurrentFileEmpty());
	}

	@Test
	public void testAppendOnce() throws IOException {
		String expected = "test";

		assertTrue(fm.isCurrentFileEmpty());

		fm.appendToCurrentFile(expected);

		assertFileContents(expected);
	}

	@Test
	public void testAppendTwice() throws IOException {
		String expected = "test";

		assertTrue(fm.isCurrentFileEmpty());

		fm.appendToCurrentFile(expected + "\n");
		fm.appendToCurrentFile(expected);

		assertFileContents(expected + "\n" + expected);
	}

	private void assertFileContents(String expected) throws IOException {
		byte[] fileBytes = Files.readAllBytes(fm.getCurrentFilePath());

		if (expected.isEmpty())
			assertTrue(fm.isCurrentFileEmpty());
		else
			assertFalse(fm.isCurrentFileEmpty());

		assertEquals(expected, new String(fileBytes));
	}
}
