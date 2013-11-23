package edu.oregonstate.cope.clientRecorder.fileOps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleFileProviderTest {

	private static final String FILE_NAME = "testFile";
	private FileProvider simpleFileProvider;

	@Before
	public void setUp() throws Exception {
		simpleFileProvider = new SimpleFileProvider(FILE_NAME);
		simpleFileProvider.setRootDirectory(".");
		
		testInit();
	}
	
	@After
	public void delete() throws IOException {
		simpleFileProvider.deleteFiles();
		assertFalse(Files.exists(simpleFileProvider.getCurrentFilePath()));
	}
	
	@Test
	public void testInit() throws IOException {
		assertEquals(FILE_NAME, simpleFileProvider.getCurrentFilePath().getFileName().toString());
		assertTrue(simpleFileProvider.isCurrentFileEmpty());
	}
	
	@Test
	public void testAppend() throws Exception {
		simpleFileProvider.appendToCurrentFile("1");
		simpleFileProvider.appendToCurrentFile("\n");
		simpleFileProvider.appendToCurrentFile("2");
		simpleFileProvider.appendToCurrentFile("\n");
		simpleFileProvider.appendToCurrentFile("3");
		
		assertThreeLines();
	}

	private void assertThreeLines() {
		assertFalse(simpleFileProvider.isCurrentFileEmpty());

		List<String> lines = simpleFileProvider.readAllLines();
		assertEquals(3, lines.size());
		
		assertEquals("1", lines.get(0));
		assertEquals("2", lines.get(1));
		assertEquals("3", lines.get(2));
	}
	
	@Test
	public void testWrite() throws Exception {
		simpleFileProvider.writeToCurrentFile("1\n2\n3");
		assertThreeLines();
		
		simpleFileProvider.writeToCurrentFile("1\n2\n3");
		assertThreeLines();
	}
}
