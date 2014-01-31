package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SnapshotManagerTest extends PopulatedWorkspaceTest {
	
	private SnapshotManager snapshotManager;

	@Before
	public void setUp() throws Exception {
		File file = new File(COPEPlugin.getDefault().getLocalStorage(), "known-projects");
		file.createNewFile();
		Files.write(file.toPath(), "known1\nknown2\n".getBytes(), StandardOpenOption.WRITE);
		snapshotManager = new SnapshotManager(COPEPlugin.getDefault().getLocalStorage().getAbsolutePath());
	}
	
	@After
	public void tearDown() throws Exception {
		File[] zipFiles = listZipFilesInDir(COPEPlugin.getDefault().getLocalStorage());
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
		assertEquals("known1\nknown2\nlibrariesTest\nknown3\n",new String(Files.readAllBytes(Paths.get(COPEPlugin.getDefault().getLocalStorage().getAbsolutePath(), "known-projects"))));
	}
	
	@Test
	public void testTouchProjectInSession() throws Exception {
		String projectName = javaProject.getProject().getName();
		snapshotManager.isProjectKnown(projectName);
		snapshotManager.takeSnapshotOfSessionTouchedProjects();
		Thread.sleep(300);
		File fileDir = COPEPlugin.getDefault().getLocalStorage();
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
			ArrayList<Byte> contents = new ArrayList<Byte>();
			do {
				byte[] block = new byte[1024];
				int read = zipInputStream.read(block, 0, 1024);
				if (read != -1) {
					count += read;
					contents.addAll(Arrays.asList(boxByteArray(block)));
				} else {
					assertContentsAreTheSameAsTheOriginal(contents, nextEntry.getName());
					break;
				}
			} while(true);
			System.out.println(count);
			assertTrue(count != 0);
		}
		zipInputStream.close();
	}

	private Byte[] boxByteArray(byte[] block) {
		Byte[] boxedBlock = new Byte[1024];
		for (int i=0; i< 1024; i++) {
			boxedBlock[i] = block[i];
		}
		return boxedBlock;
	}

	private void assertContentsAreTheSameAsTheOriginal(ArrayList<Byte> contents, String fileName) throws Exception {
		IFile file = javaProject.getProject().getFile(fileName);
		byte[] actualFileContents = null;
		try {
			actualFileContents = Files.readAllBytes(Paths.get(file.getLocation().toPortableString()));
		} catch (NoSuchFileException e) {
			return;
		}
		assertEquals(actualFileContents, contents);
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
	
	@Test
	public void testCompleteSnapshot() throws Exception {
		String snapshotFile = snapshotManager.takeSnapshot(javaProject.getProject());
		Thread.sleep(200);
		ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(new ZipFile(snapshotFile));
		ZipEntry rootEntry = structureProvider.getRoot();
		
		List children = structureProvider.getChildren(rootEntry);
		ZipEntry projectRoot = getProjectEntry(children);
		assertEntryMatchesDir(structureProvider, javaProject.getProject(), projectRoot);
	}

	private ZipEntry getProjectEntry(List children) {
		ZipEntry projectRoot = null;
		for (Object child : children) {
			if(((ZipEntry) child).getName().equals("librariesTest/"))
				projectRoot = (ZipEntry) child;
		}
		return projectRoot;
	}

	private void assertEntryMatchesDir(ZipFileStructureProvider structureProvider, IContainer folder, ZipEntry parentEntry) throws Exception {
		List unexportedItems = Arrays.asList("bin");
		IResource[] members = folder.members();
		List children = structureProvider.getChildren(parentEntry);
		for (IResource member : members) {
			String memberName = member.getName();
			if (unexportedItems.contains(memberName))
				continue;
			boolean found = false;
			for (Object child : children) {
				ZipEntry entry = (ZipEntry) child;
				String entryName = getEntryName(entry);
				if (entryName.equals(memberName)) {
					found = true;
					if (member instanceof IFolder)
						if (entry.isDirectory())
							assertEntryMatchesDir(structureProvider, (IContainer) member, entry);
						else
							fail("Directories don't match");
				}
			} 
			assertTrue(found);
		}
	}

	private String getEntryName(ZipEntry entry) {
		String entryName = entry.getName();
		if (entryName.endsWith("/"))
			entryName = entryName.substring(0, entryName.length() - 1);
		entryName = entryName.substring(entryName.lastIndexOf("/") + 1);
		return entryName;
	}
}
