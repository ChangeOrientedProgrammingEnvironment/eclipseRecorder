package edu.oregonstate.cope.eclipse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.UIJob;
import org.quartz.SchedulerException;

import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.branding.LogoManager;
import edu.oregonstate.cope.eclipse.installer.EclipseInstallerHelper;
import edu.oregonstate.cope.eclipse.installer.EclipseInstaller;
import edu.oregonstate.cope.eclipse.listeners.CommandExecutionListener;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;
import edu.oregonstate.cope.eclipse.listeners.FileBufferListener;
import edu.oregonstate.cope.eclipse.listeners.GitRepoListener;
import edu.oregonstate.cope.eclipse.listeners.JavaElementChangedListener;
import edu.oregonstate.cope.eclipse.listeners.LaunchListener;
import edu.oregonstate.cope.eclipse.listeners.MultiEditorPageChangedListener;
import edu.oregonstate.cope.eclipse.listeners.RefactoringExecutionListener;
import edu.oregonstate.cope.eclipse.listeners.ResourceListener;
import edu.oregonstate.cope.fileSender.FileSender;
import edu.oregonstate.cope.fileSender.FileSenderParams;

@SuppressWarnings("restriction")
class StartPluginUIJob extends UIJob {
	
	private static final String WORKSPACE_INIT_EXTENSION_ID = "edu.oregonstate.cope.eclipse.workspaceinitoperation";
	
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
		Uninstaller uninstaller = copePlugin.getUninstaller();

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
		
		String title = "COPE recorder shutting down";
		String message = "The time allotted for the study has expired. "
				+ "The recorder plugin has shut down permanently and you may delete it if you wish to do so. "
				+ "\n\nThank you for your participation!";
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), title, message);
	}

	private void performStartup(IProgressMonitor monitor) {
		monitor.beginTask("Starting Recorder", 2);

		if (!isDevelopementCOPE() && Platform.inDevelopmentMode())
			return;
		
		doInstall();

		if (COPEPlugin.getDefault().getRecorder().isFirstStart())
			initializeWorkspace();
		
		monitor.worked(1);

		LogoManager logoManager = LogoManager.getInstance();
		logoManager.showLogo();
		registerDocumentListenersForOpenEditors();
		FileBuffers.getTextFileBufferManager().addFileBufferListener(new FileBufferListener());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new ResourceListener(), IResourceChangeEvent.PRE_REFRESH | IResourceChangeEvent.POST_CHANGE);
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
		commandService.addExecutionListener(new CommandExecutionListener());
		JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_CHANGE);

		RefactoringHistoryService refactoringHistoryService = RefactoringHistoryService.getInstance();
		refactoringHistoryService.addExecutionListener(new RefactoringExecutionListener());

		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener());

		GitRepoListener gitChangeListener = new GitRepoListener(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		Repository.getGlobalListenerList().addRefsChangedListener(gitChangeListener);
		Repository.getGlobalListenerList().addIndexChangedListener(gitChangeListener);

		if (!isDevelopementCOPE())
			initializeFileSender();
		
		logoManager.checkForUpdates();
	}

	private boolean isDevelopementCOPE() {
		return COPEPlugin.getDefault().getBundle().getVersion().getQualifier().equals("qualifier");
	}

	private void initializeWorkspace() {
		IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(WORKSPACE_INIT_EXTENSION_ID);
		for (IConfigurationElement extension : extensions) {
			try {
				Object executableExtension = extension.createExecutableExtension("InitializeWorkspaceOperation");
				if (executableExtension instanceof InitializeWorkspaceOperation)
					((InitializeWorkspaceOperation)executableExtension).doInit();
			} catch (CoreException e) {
				COPEPlugin.getDefault().getLogger().error(this, "Could not load Workspace Init extension", e);
			}
		}
	}

	private void doInstall() {
		try {
			new EclipseInstaller(copePlugin.getRecorder(), new EclipseInstallerHelper()).run();
		} catch (IOException e) {
			copePlugin.getLogger().error(this, "Installer failed", e);
		}
	}

	private void registerDocumentListenersForOpenEditors() {
		SnapshotManager snapshotManager = COPEPlugin.getDefault().getSnapshotManager();
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		List<String> ignoredProjects = COPEPlugin.getDefault().getIgnoreProjectsList();
		IEditorReference[] editorReferences = activeWindow.getActivePage().getEditorReferences();
		for (IEditorReference editorReference : editorReferences) {
			IDocument document = getDocumentForEditor(editorReference);
			if (document == null) {
				copePlugin.getLogger().info(this, "Could not find project for editor " + editorReference.getName());
				continue;
			}
			IProject project = getProjectFromEditor(editorReference);
			if (project == null)
				continue;
			if (ignoredProjects.contains(project.getName()))
				continue;
			document.addDocumentListener(new DocumentListener());
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
				project = copePlugin.getProjectForEditor(editorInput);
			}
		} catch (PartInitException e) {
			copePlugin.getLogger().error(this, e.getMessage(), e);
		}
		return project;
	}

	private IDocument getDocumentForEditor(IEditorReference editorReference) {
		IEditorPart editorPart = editorReference.getEditor(true);
		if (editorPart instanceof MultiPageEditorPart) {
			((MultiPageEditorPart) editorPart).addPageChangedListener(new MultiEditorPageChangedListener());
			return null;
		}
		ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
		if (sourceViewer == null)
			return null;
		IDocument document = sourceViewer.getDocument();
		return document;
	}

	private void initializeFileSender() {
		try {
			new FileSender(new FileSenderParams(
				copePlugin.getLogger(),
				copePlugin.getLocalStorage(),
				copePlugin.getRecorder().getWorkspaceProperties(),
				copePlugin.getRecorder().getWorkspaceID()
			));
		} catch (ParseException e) {
			copePlugin.getLogger().error(this, e.getMessage(), e);
		} catch (SchedulerException e) {
			copePlugin.getLogger().error(this, e.getMessage(), e);
		}
	}
}