package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SnapshotManagerTest extends PopulatedWorkspaceTest {
	
	private SnapshotManager snapshotManager;

	@Before
	public void setUp() throws Exception {
		File file = new File(COPEPlugin.getLocalStorage(), "known-projects");
		file.createNewFile();
		Files.write(file.toPath(), "known1\nknown2\n".getBytes(), StandardOpenOption.WRITE);
		snapshotManager = new SnapshotManager(COPEPlugin.getLocalStorage().getAbsolutePath());
	}
	
	@After
	public void tearDown() throws Exception {
		File[] zipFiles = listZipFilesInDir(COPEPlugin.getLocalStorage());
		for (File zipFile : zipFiles) {
			zipFile.delete();
		}
	}

	@Test
	public void testNotKnowProject() {
		assertFalse(snapshotManager.isProjectKnown("test"));
	}
	
	@Test
	public void testIsProjectKnown() {
		assertTrue(snapshotManager.isProjectKnown("known1"));
		assertTrue(snapshotManager.isProjectKnown("known2"));
	}
	
	@Test
	public void testKnowProject() throws Exception {
		snapshotManager.knowProject("known3");
		assertTrue(snapshotManager.isProjectKnown("known3"));
		assertEquals("known1\nknown2\nknown3\n",new String(Files.readAllBytes(Paths.get(COPEPlugin.getLocalStorage().getAbsolutePath(), "known-projects"))));
	}
	
	@Test
	public void testTouchProjectInSession() throws Exception {
		String projectName = javaProject.getProject().getName();
		snapshotManager.isProjectKnown(projectName);
		snapshotManager.takeSnapshotOfKnownProjects();
		File fileDir = COPEPlugin.getLocalStorage();
		assertTrue(fileDir.isDirectory());
		File[] listFiles = listZipFilesInDir(fileDir);
		assertEquals(1,listFiles.length);
		assertTrue(listFiles[0].getName().matches(projectName + "-[0-9]*\\.zip"));
		assertZipFileContentsIsNotEmpty(listFiles[0]);
	}
	
	private void assertZipFileContentsIsNotEmpty(File zipFile) throws Exception {
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
		while (zipInputStream.available() == 1) {
			ZipEntry nextEntry = zipInputStream.getNextEntry();
			if (nextEntry == null)
				break;
			int count = 0;
			byte[] contents = new byte[1000];
			do {
				int read = zipInputStream.read(contents, count, 1000);
				if (read != -1)
					count += read;
				else 
					break;
			} while(true);
			System.out.println(count);
			assertTrue(count != 0);
		}
		zipInputStream.close();
	}

	private File[] listZipFilesInDir(File fileDir) {
		File[] listFiles = fileDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".zip");
			}
		});
		return listFiles;
	}
	
}
