package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.Rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class OutsideLibrariesTest extends PopulatedWorkspaceTest {

	@Rule
	public TestName name = new TestName();
	
	@Test
	public void testGetNonWorkspaceLibrary() throws Exception {
		List<String> nonWorkspaceLibraries = snapshotManager.getNonWorkspaceLibraries(javaProject);
		assertEquals(1, nonWorkspaceLibraries.size());
		assertEquals("/Users/caius/osu/COPE/clientRecorder/edu.oregonstate.cope.eclipse.tests/projects/json-simple-1.1.1.jar",nonWorkspaceLibraries.get(0));
	}
	
	@Test
	public void testAddLibraryToZip() throws Exception {
		String zipFilePath = snapshotManager.takeSnapshot(javaProject.getProject());
		assertNotNull(zipFilePath);
		
		ArrayList<String> initialEntries = getEntriesInZipFile(zipFilePath);
		System.out.println(initialEntries);
		
		List<String> libraries = snapshotManager.getNonWorkspaceLibraries(javaProject);
		snapshotManager.addLibsToZipFile(libraries, zipFilePath);
		
		ArrayList<String> entriesNames = getEntriesInZipFile(zipFilePath);
		System.out.println(entriesNames);
		assertTrue(entriesNames.containsAll(initialEntries));
		assertTrue(entriesNames.contains("libs/json-simple-1.1.1.jar"));
	}

	private ArrayList<String> getEntriesInZipFile(String zipFilePath) throws IOException {
		ZipFile zipFile = new ZipFile(zipFilePath);
		ArrayList<String> entriesNames = getEntriesInZipFile(zipFile);
		zipFile.close();
		return entriesNames;
	}

	private ArrayList<String> getEntriesInZipFile(ZipFile zipFile) {
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		ArrayList<? extends ZipEntry> usefulEntries = Collections.list(entries);
		ArrayList<String> entriesNames = new ArrayList<>();
		for (ZipEntry zipEntry : usefulEntries) {
			entriesNames.add(zipEntry.getName());
		}
		return entriesNames;
	}

}
