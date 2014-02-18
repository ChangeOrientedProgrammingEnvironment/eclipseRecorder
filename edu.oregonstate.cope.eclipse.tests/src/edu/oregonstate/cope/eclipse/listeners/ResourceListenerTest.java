package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.eclipse.FileUtil;

public class ResourceListenerTest {
	
	private static final int FILE_SIZE_BYTES = 1000000;
	private IProject project;
	private IFile binaryFile;
	private byte[] randomBytes;
	private IFile textFile;
	private String textFileContents;
	private ResourceListener listener;

	@Before
	public void before() throws Exception {
		project = FileUtil.createProject("TestProject");
		createBinaryFile();
		createTextFile();
		listener = new ResourceListener();
	}

	private void createTextFile() throws Exception {
		textFile = FileUtil.createFile("smth.txt", project);
		textFileContents = "Some human readable text";
		textFile.appendContents(new ByteArrayInputStream(textFileContents.getBytes()), true, false, new NullProgressMonitor());
	}

	private void createBinaryFile() throws CoreException {
		binaryFile = FileUtil.createFile("bla.jar", project);
		randomBytes = new byte[FILE_SIZE_BYTES];
		new Random().nextBytes(randomBytes);
		binaryFile.appendContents(new ByteArrayInputStream(randomBytes), true, false, new NullProgressMonitor());
	}
	
	@After
	public void after() throws Exception {
		FileUtil.deleteProject(project);
	}
	
	@Test
	public void testReadBinaryFile() throws Exception {
		String contents = listener.getFileContents(binaryFile);
		
		byte[] actualBytes = Base64.decodeBase64(contents.getBytes());
		
		assertTrue(Arrays.equals(randomBytes, actualBytes));
	}
	
	@Test
	public void testReadTextFile() throws Exception {
		String fileContents = listener.getFileContents(textFile);
		assertEquals(textFileContents, fileContents);
	}

}
