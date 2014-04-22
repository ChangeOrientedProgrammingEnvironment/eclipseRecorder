package edu.oregonstate.cope.eclipse.installer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.installer.InstallerOperation;
import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.MockRecorder;
import edu.oregonstate.cope.eclipse.MockRecorderFacade;

public class InstallerOperationTest {

	private static final String WORKSPACE = "workspace";
	private static final String PERMANENT = "permanent";
	private static final String TEST_DATA = "testData";
	private static final String FILE = "persistedFile";

	private static final Path PERMANENT_PATH = Paths.get(TEST_DATA, PERMANENT);
	private static final Path WORKSPACE_PATH = Paths.get(TEST_DATA, WORKSPACE);

	private static final Path WORKSPACE_FILE_PATH = Paths.get(TEST_DATA, WORKSPACE, FILE).toAbsolutePath();
	private static final Path PERMANENT_FILE_PATH = Paths.get(TEST_DATA, PERMANENT, FILE).toAbsolutePath();

	private TestOperation testOperation;
	
	private class TestOperation extends InstallerOperation {

		private String status;

		public TestOperation(Path workspaceDirectory, Path permanentDirectory) {
			super(new MockRecorderFacade(), permanentDirectory, workspaceDirectory);
		}

		@Override
		protected void doBothFilesExists() {
			this.status = "both";
			super.doBothFilesExists();
		}

		@Override
		protected void doOnlyPermanentFileExists(File workspaceFile, File permanentFile) throws IOException {
			this.status = "permanent";
			super.doOnlyPermanentFileExists(workspaceFile, permanentFile);
		}

		@Override
		protected void doOnlyWorkspaceFileExists(File workspaceFile, File permanentFile) throws IOException {
			this.status = "workspace";
			super.doOnlyWorkspaceFileExists(workspaceFile, permanentFile);
		}

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
			this.status = "none";
			writeToFile(workspaceFile.toPath());
			writeToFile(permanentFile.toPath());
		}

		@Override
		protected String getFileName() {
			return FILE;
		}

	}

	private void writeToFile(Path path) throws IOException {
		Files.createDirectories(path.getParent());
		Files.write(path, "contents".getBytes(), StandardOpenOption.CREATE);
	}

	@Before
	public void setUp() throws IOException {
		Files.createDirectories(WORKSPACE_PATH);
		Files.createDirectories(PERMANENT_PATH);

		testOperation = new TestOperation(WORKSPACE_PATH, PERMANENT_PATH);
	}

	@After
	public void tearDown() throws IOException {
		Files.deleteIfExists(PERMANENT_FILE_PATH);
		Files.deleteIfExists(WORKSPACE_FILE_PATH);

		Files.deleteIfExists(WORKSPACE_PATH.toAbsolutePath());
		Files.deleteIfExists(PERMANENT_PATH.toAbsolutePath());

		Files.deleteIfExists(Paths.get(TEST_DATA).toAbsolutePath());
	}

	private void assertFileExists(Path path) throws IOException {
		assertTrue(Files.exists(path));
		assertEquals("contents", new String(Files.readAllBytes(path)));
	}

	@Test
	public void testBoth() throws IOException {
		writeToFile(PERMANENT_FILE_PATH);
		writeToFile(WORKSPACE_FILE_PATH);

		testOperation.perform();

		assertFileExists(PERMANENT_FILE_PATH);
		assertFileExists(WORKSPACE_FILE_PATH);
		assertEquals("both", testOperation.status);
	}

	@Test
	public void testOnlyPermanent() throws IOException {
		writeToFile(PERMANENT_FILE_PATH);

		testOperation.perform();

		assertFileExists(PERMANENT_FILE_PATH);
		assertFileExists(WORKSPACE_FILE_PATH);
		assertEquals("permanent", testOperation.status);
	}

	@Test
	public void testOnlyWorkspace() throws IOException {
		writeToFile(WORKSPACE_FILE_PATH);

		testOperation.perform();

		assertFileExists(PERMANENT_FILE_PATH);
		assertFileExists(WORKSPACE_FILE_PATH);
		assertEquals("workspace", testOperation.status);
	}

	@Test
	public void testNoFileExists() throws IOException {
		testOperation.perform();

		assertFileExists(PERMANENT_FILE_PATH);
		assertFileExists(WORKSPACE_FILE_PATH);
		assertEquals("none", testOperation.status);
	}
}
