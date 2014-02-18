package edu.oregonstate.cope.eclipse.listeners;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.eclipse.FileUtil;

public class ResourceListenerTest {
	
	private static final int FILE_SIZE_BYTES = 10000;
	private IProject project;
	private IFile file;
	private byte[] randomBytes;

	@Before
	public void before() throws Exception {
		project = FileUtil.createProject("TestProject");
		file = FileUtil.createFile("bla.jar", project);
		randomBytes = new byte[FILE_SIZE_BYTES];
		new Random().nextBytes(randomBytes);
		file.appendContents(new ByteArrayInputStream(randomBytes), true, false, new NullProgressMonitor());
	}
	
	@After
	public void after() throws Exception {
		FileUtil.deleteProject(project);
	}
	
	@Test
	public void testReadBinaryFile() throws Exception {
		ResourceListener listener = new ResourceListener();
		String contents = listener.getFileContents(file);
		
		byte[] actualBytes = Base64.decodeBase64(contents.getBytes());
		
		assertTrue(Arrays.equals(randomBytes, actualBytes));
	}

}
