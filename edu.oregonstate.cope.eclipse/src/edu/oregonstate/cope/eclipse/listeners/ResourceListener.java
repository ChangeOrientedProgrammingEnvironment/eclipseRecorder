package edu.oregonstate.cope.eclipse.listeners;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {
	
	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorder();
	
	private Map<String, Long> lastSavedVersion;
	
	public ResourceListener() {
		lastSavedVersion = new HashMap<String, Long>();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		recordChangedDelta(delta);
	}

	private void recordChangedDelta(IResourceDelta delta) {
		if (delta == null)
			return;
		
		IResource affectedResource = delta.getResource();
		if (affectedResource.getType() == IResource.FILE) {
			if (isProjectIgnored(affectedResource))
				return;
			IFile affectedFile = (IFile) affectedResource;
			String filePath = affectedFile.getFullPath().toPortableString();
			if (isClassFile(affectedFile))
				return;
			if (delta.getKind() == IResourceDelta.REMOVED) {
				recorder.recordResourceDelete(filePath);
				return;
			}
			if (delta.getKind() == IResourceDelta.ADDED) {
				recorder.recordResourceAdd(filePath, getFileContentents(affectedFile));
				return;
			}
			
			if (isSavedAction() || isRefactoringInProgress())
				recordFileSave(delta, filePath);
			else
				recordFileRefresh(affectedFile);
			return;
		}
		
		IResourceDelta[] affectedChildren = delta.getAffectedChildren();
		for (IResourceDelta child : affectedChildren) {
			recordChangedDelta(child);
		}
	}

	private void recordFileSave(IResourceDelta delta, String filePath) {
		long modificationStamp = delta.getResource().getModificationStamp();
		recorder.recordFileSave(filePath, modificationStamp);
		lastSavedVersion.put(filePath, modificationStamp);
	}

	public boolean isProjectIgnored(IResource affectedResource) {
		IProject project = affectedResource.getProject();
		if (project == null)
			return true;
		return COPEPlugin.getDefault().getIgnoreProjectsList().contains(project.getName());
	}

	private void recordFileRefresh(IFile affectedFile) {
		String filePath = affectedFile.getFullPath().toPortableString();
		long modificationStamp = affectedFile.getModificationStamp();
		
		Long lastVersion = lastSavedVersion.get(filePath);
		if (lastVersion != null && modificationStamp == lastVersion)
			return;
		
		String contents = getFileContentents(affectedFile);
		recorder.recordRefresh(contents, filePath, modificationStamp);
	}

	@SuppressWarnings("resource")
	private String getFileContentents(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		InputStream inputStream;
		try {
			inputStream = affectedFile.getContents();
			if (COPEPlugin.knownTextFiles.contains(fileExtension))
				return getTextFileContents(inputStream);
			else
				return getBinaryFileContents(inputStream);
		} catch (CoreException | IOException e) {
			COPEPlugin.getDefault().getLogger().error(this, "Could not get contents of file", e);
		}
		return "";
	}

	/**
	 * I return the contents of the file as a String.
	 * @param inputStream the input stream to read from
	 * @return the String containg the file contents, or gibberish if the file is a binary file
	 * @throws IOException if I cannot read from the file
	 */
	private String getTextFileContents(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		readFromTo(inputStream, byteArrayOutputStream);
		byte[] bytes = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return new String(bytes);
	}

	/**
	 * I return a base64 encoding of the file contents.
	 * @param inputStream the InputStream I have to read from.
	 * @return the base64 string containing the encoded file contents.
	 * @throws IOException if I cannot read from the InputStream.
	 */
	private String getBinaryFileContents(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, true, 0, null);
		readFromTo(inputStream, base64OutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		base64OutputStream.close();
		byteArrayOutputStream.close();
		return new String(byteArray);
	}

	private void readFromTo(InputStream inputStream, OutputStream outputStream) throws IOException {
		do {
			byte[] b = new byte[1024];
			int read = inputStream.read(b, 0, 1024);
			if (read == -1)
				break;
			outputStream.write(b, 0, read);
		} while (true);
	}

	private boolean isClassFile(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		if (fileExtension.equals("class"))
			return true;
		return false;
	}

	private boolean isSavedAction() {
		return CommandExecutionListener.isSaveInProgress();
	}

	private boolean isRefactoringInProgress() {
		return RefactoringExecutionListener.isRefactoringInProgress();
	}
}
