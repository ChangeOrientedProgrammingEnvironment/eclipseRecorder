package edu.oregonstate.cope.eclipse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.part.FileEditorInput;
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
import edu.oregonstate.cope.eclipse.ui.SurveyManager;
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
		
		SurveyManager sm = new SurveyManager();
		sm.checkAndRunSurvey(COPEPlugin.getLocalStorage().getAbsolutePath(), COPEPlugin.getBundleStorage().getAbsolutePath(), UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());

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
			copePlugin.getLogger().error(this, e.getMessage(), e);
		}
	}

	private void registerDocumentListenersForOpenEditors() {
		SnapshotManager snapshotManager = COPEPlugin.getDefault().getSnapshotManager();
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorReference[] editorReferences = activeWindow.getActivePage().getEditorReferences();
		for (IEditorReference editorReference : editorReferences) {
			IDocument document = getDocumentForEditor(editorReference);
			if (document == null)
				continue;
			document.addDocumentListener(new DocumentListener());
			IProject project = getProjectFromEditor(editorReference);
			if (!snapshotManager.isProjectKnown(project))
				snapshotManager.takeSnapshot(project);
		}
	}

	private IProject getProjectFromEditor(IEditorReference editorReference) {
		IEditorInput editorInput;
		IProject project = null;
		try {
			editorInput = editorReference.getEditorInput();
			if (editorInput instanceof FileEditorInput) {
				IFile file = ((FileEditorInput)editorInput).getFile();
				project = file.getProject();
			}
		} catch (PartInitException e) {
			copePlugin.getLogger().error(this, e.getMessage(), e);
		}
		return project;
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
			copePlugin.getLogger().error(this, e.getMessage(), e);
		} catch (SchedulerException e) {
			copePlugin.getLogger().error(this, e.getMessage(), e);
		}
	}
}