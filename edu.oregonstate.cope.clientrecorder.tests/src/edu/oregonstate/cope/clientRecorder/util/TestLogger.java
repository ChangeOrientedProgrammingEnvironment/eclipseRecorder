package edu.oregonstate.cope.clientRecorder.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLogger {

	private static final String LOG_FILE_NAME = "log";
	private static final Path PARENT_DIR = Paths.get("testLogger").toAbsolutePath();
	private COPELogger copeLogger;

	@Before
	public void setUp() {
		copeLogger = new COPELogger();
		copeLogger.enableFileLogging(PARENT_DIR.toAbsolutePath().toString(), LOG_FILE_NAME);
		copeLogger.disableConsoleLogging();
	}

	@After
	public void tearDown() throws IOException {
		copeLogger.testDisableFileLogging();

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				Files.delete(file);

				return FileVisitResult.CONTINUE;
			}
		};

		Files.walkFileTree(PARENT_DIR, fv);
		Files.delete(PARENT_DIR);
	}

	@Test
	public void testLoggerWithError() throws Exception {
		copeLogger.logEverything();
		
		copeLogger.info(this, "test");
		copeLogger.info(this, "test");
		copeLogger.info(this, "test");
		
		assertEquals(3, numberOfLoggedLines());

		copeLogger.error(this, "oh no", new NullPointerException());
		
		assertTrue(numberOfLoggedLines() > 4);

	}
	
	private int numberOfLoggedLines() throws IOException {
		return Files.readAllLines(PARENT_DIR.resolve(LOG_FILE_NAME), Charset.defaultCharset()).size();
	}

	@Test
	public void testLogger() throws Exception {
		copeLogger.logEverything();
		copeLogger.info(this, "test");
		copeLogger.info(this, "test");
		copeLogger.info(this, "test");

		assertEquals(3, numberOfLoggedLines());
	}

	@Test
	public void testOnlyErrors() throws Exception {
		copeLogger.logOnlyErrors();

		copeLogger.info(this, "test");
		copeLogger.info(this, "test");
		copeLogger.error(this, "nooo");
		copeLogger.info(this, "test");

		assertEquals(1, numberOfLoggedLines());
	}
}
