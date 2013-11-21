package edu.oregonstate.cope.eclipse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.UIJob;
import org.quartz.SchedulerException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;
import edu.oregonstate.cope.eclipse.listeners.FileBufferListener;
import edu.oregonstate.cope.eclipse.listeners.LaunchListener;
import edu.oregonstate.cope.eclipse.listeners.MultiEditorPageChangedListener;
import edu.oregonstate.cope.eclipse.listeners.RefactoringExecutionListener;
import edu.oregonstate.cope.eclipse.listeners.ResourceListener;
import edu.oregonstate.cope.eclipse.listeners.SaveCommandExecutionListener;
import edu.oregonstate.cope.fileSender.FileSender;

@SuppressWarnings("restriction")
class StartPluginUIJob extends UIJob {
	/**
	 * 
	 */
	final COPEPlugin copePlugin;
	private File workspaceIdFile;

	StartPluginUIJob(COPEPlugin copePlugin, String name) {
		super(name);
		this.copePlugin = copePlugin;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		COPEPlugin.getDefault().initializeRecorder(COPEPlugin.getLocalStorage().getAbsolutePath(), COPEPlugin.getDefault().getWorkspaceID(), ClientRecorder.ECLIPSE_IDE);
		Uninstaller uninstaller = COPEPlugin.getDefault().getUninstaller();

		if (uninstaller.isUninstalled())
			return Status.OK_STATUS;

		if (uninstaller.shouldUninstall())
			performUninstall(uninstaller);
		else
			performStartup(monitor);
		
		return Status.OK_STATUS;
	}
	
	private void performUninstall(Uninstaller uninstaller) {
		uninstaller.setUninstall();
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Recording shutting down", "Thank you for your participation. The recorder has shut down permanently. You may delete it if you wish to do so.");
	}

	private void performStartup(IProgressMonitor monitor) {
		monitor.beginTask("Starting Recorder", 2);
		
		if (!isWorkspaceKnown()) {
			getToKnowWorkspace();
			getInitialSnapshot();
		}

		monitor.worked(1);

		
		registerDocumentListenersForOpenEditors();
		FileBuffers.getTextFileBufferManager().addFileBufferListener(new FileBufferListener());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new ResourceListener(), IResourceChangeEvent.PRE_REFRESH | IResourceChangeEvent.POST_CHANGE);
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
		commandService.addExecutionListener(new SaveCommandExecutionListener());

		RefactoringHistoryService refactoringHistoryService = RefactoringHistoryService.getInstance();
		refactoringHistoryService.addExecutionListener(new RefactoringExecutionListener());

		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener());

		initializeFileSender();
	}

	protected boolean isWorkspaceKnown() {
		workspaceIdFile = copePlugin.getWorkspaceIdFile();
		return workspaceIdFile.exists();
	}

	protected void getToKnowWorkspace() {
		try {
			workspaceIdFile.createNewFile();
			String workspaceID = UUID.randomUUID().toString();
			BufferedWriter writer = new BufferedWriter(new FileWriter(workspaceIdFile));
			writer.write(workspaceID);
			writer.close();
		} catch (IOException e) {
		}
	}

	protected String getInitialSnapshot() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		if (projects.length == 0)
			return null; //don't take snapshot of empty workspace
		String zipFile = COPEPlugin.getLocalStorage().getAbsolutePath() + "/" + System.currentTimeMillis() + ".zip";
		ArchiveFileExportOperation archiveFileExportOperation = new ArchiveFileExportOperation(root, zipFile);
		archiveFileExportOperation.setUseCompression(true);
		archiveFileExportOperation.setUseTarFormat(false);
		archiveFileExportOperation.setCreateLeadupStructure(true);
		try {
			archiveFileExportOperation.run(new NullProgressMonitor());
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		return zipFile;
	}

	private void registerDocumentListenersForOpenEditors() {
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorReference[] editorReferences = activeWindow.getActivePage().getEditorReferences();
		for (IEditorReference editorReference : editorReferences) {
			IDocument document = getDocumentForEditor(editorReference);
			if (document == null)
				continue;
			document.addDocumentListener(new DocumentListener());
		}
	}

	private IDocument getDocumentForEditor(IEditorReference editorReference) {
		IEditorPart editorPart = editorReference.getEditor(true);
		if (editorPart instanceof MultiPageEditorPart) {
			((MultiPageEditorPart)editorPart).addPageChangedListener(new MultiEditorPageChangedListener());
			return null;
		}
		ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
		IDocument document = sourceViewer.getDocument();
		return document;
	}
	
	private void initializeFileSender() {
		try {
			new FileSender();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getNonWorkspaceLibraries(IJavaProject project) {
		IClasspathEntry[] resolvedClasspath = null;
		try {
			resolvedClasspath = project.getRawClasspath();
		} catch (JavaModelException e) {
			return new ArrayList<String>();
		}
		List<String> pathsOfLibraries = new ArrayList<String>();
		for (IClasspathEntry iClasspathEntry : resolvedClasspath) {
			if (iClasspathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				pathsOfLibraries.add(iClasspathEntry.getPath().toPortableString());
			}
		}
		return pathsOfLibraries;
	}
	
	@SuppressWarnings("resource")
	public void addLibsToZipFile(List<String> pathOfLibraries, String zipFilePath) {
		try {
			String libFolder = "libs/";
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath+"-libs", true));
			copyExistingEntries(zipFilePath, zipOutputStream);
			for (String library : pathOfLibraries) {
				ZipEntry libraryZipEntry = new ZipEntry(libFolder + Paths.get(library).getFileName());
				zipOutputStream.putNextEntry(libraryZipEntry);
				byte[] libraryContents = Files.readAllBytes(Paths.get(library));
				zipOutputStream.write(libraryContents);
			}
			zipOutputStream.close();
			new File(zipFilePath).delete();
			new File(zipFilePath+"-libs").renameTo(new File(zipFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void copyExistingEntries(String zipFilePath, ZipOutputStream zipOutputStream) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
			while(zipInputStream.available() == 1) {
				ZipEntry entry = zipInputStream.getNextEntry();
				if (entry == null)
					continue;
				zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
				long entrySize = entry.getSize();
				if (entrySize < 0)
					continue;
				byte[] contents = new byte[(int) entrySize];
				int count = 0;
				do {
					count = zipInputStream.read(contents, count, (int) entrySize);
				} while (count < entrySize);
				zipOutputStream.write(contents);
			}
		} catch (IOException e) {
		}
	}

}