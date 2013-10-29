package edu.oregonstate.cope.eclipse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;
import org.eclipse.ui.progress.UIJob;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;
import edu.oregonstate.cope.eclipse.listeners.FileBufferListener;
import edu.oregonstate.cope.eclipse.listeners.LaunchListener;
import edu.oregonstate.cope.eclipse.listeners.RefactoringExecutionListener;
import edu.oregonstate.cope.eclipse.listeners.ResourceListener;
import edu.oregonstate.cope.eclipse.listeners.SaveCommandExecutionListener;

@SuppressWarnings("restriction")
class StartPluginUIJob extends UIJob {
	/**
	 * 
	 */
	private final COPEPlugin copePlugin;
	private File workspaceIdFile;

	StartPluginUIJob(COPEPlugin copePlugin, String name) {
		super(name);
		this.copePlugin = copePlugin;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask("Starting Recorder", 2);
		if (!isWorkspaceKnown()) {
			getToKnowWorkspace();
			getInitialSnapshot();
		}
		monitor.worked(1);
		COPEPlugin.workspaceID = getWorkspaceID();
		this.copePlugin.setClientRecorder(new ClientRecorder());
		this.copePlugin.clientRecorder().setIDE(ClientRecorder.ECLIPSE_IDE);
		this.copePlugin.setEventFilesRootDirectory("eventFiles");
		registerDocumentListenersForOpenEditors();
		FileBuffers.getTextFileBufferManager().addFileBufferListener(new FileBufferListener());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new ResourceListener(), IResourceChangeEvent.PRE_REFRESH | IResourceChangeEvent.POST_CHANGE);
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
		commandService.addExecutionListener(new SaveCommandExecutionListener());

		RefactoringHistoryService refactoringHistoryService = RefactoringHistoryService.getInstance();
		refactoringHistoryService.addExecutionListener(new RefactoringExecutionListener());

		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener());

		Repository.getGlobalListenerList().addRefsChangedListener(new GitListener());

		return Status.OK_STATUS;
	}

	protected boolean isWorkspaceKnown() {
		workspaceIdFile = getWorkspaceIdFile();
		return workspaceIdFile.exists();
	}

	protected File getWorkspaceIdFile() {
		File pluginStoragePath = getLocalStorage();
		return new File(pluginStoragePath.getAbsolutePath() + "workspace_id");
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

	private void getInitialSnapshot() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String zipFile = getLocalStorage().getAbsolutePath() + "/" + System.currentTimeMillis() + ".zip";
		ArchiveFileExportOperation archiveFileExportOperation = new ArchiveFileExportOperation(root, zipFile);
		archiveFileExportOperation.setUseCompression(true);
		archiveFileExportOperation.setUseTarFormat(false);
		archiveFileExportOperation.setCreateLeadupStructure(true);
		try {
			archiveFileExportOperation.run(new NullProgressMonitor());
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String getWorkspaceID() {
		File workspaceIdFile = getWorkspaceIdFile();
		String workspaceID = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(workspaceIdFile));
			workspaceID = reader.readLine();
			reader.close();
		} catch (IOException e) {
		}
		return workspaceID;
	}

	private File getLocalStorage() {
		return COPEPlugin.plugin.getBundle().getDataFile("");
	}

	private void registerDocumentListenersForOpenEditors() {
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorReference[] editorReferences = activeWindow.getActivePage().getEditorReferences();
		for (IEditorReference editorReference : editorReferences) {
			IDocument document = getDocumentForEditor(editorReference);
			document.addDocumentListener(new DocumentListener());
		}
	}

	private IDocument getDocumentForEditor(IEditorReference editorReference) {
		IEditorPart editorPart = editorReference.getEditor(true);
		ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
		IDocument document = sourceViewer.getDocument();
		return document;
	}
}