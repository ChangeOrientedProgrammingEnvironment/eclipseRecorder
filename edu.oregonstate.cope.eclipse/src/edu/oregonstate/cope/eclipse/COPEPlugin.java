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
import org.eclipse.ltk.internal.core.refactoring.history.RefactoringHistoryService;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;
import edu.oregonstate.cope.eclipse.listeners.FileBufferListener;
import edu.oregonstate.cope.eclipse.listeners.LaunchListener;
import edu.oregonstate.cope.eclipse.listeners.RefactoringExecutionListener;
import edu.oregonstate.cope.eclipse.listeners.ResourceListener;
import edu.oregonstate.cope.eclipse.listeners.SaveCommandExecutionListener;

/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("restriction")
public class COPEPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.oregonstate.edu.eclipse"; //$NON-NLS-1$

	// The shared instance
	private static COPEPlugin plugin;
	
	// The ID of the current workspace
	private static String workspaceID;
	
	private ClientRecorder clientRecorder;

	/**
	 * The constructor
	 */
	public COPEPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		UIJob uiJob = new UIJob("Registering listeners") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				monitor.beginTask("Starting Recorder", 2);
				getInitialSnapshot();
				monitor.worked(1);
				workspaceID = getWorkspaceID();
				clientRecorder = new ClientRecorder();
				clientRecorder.setIDE(ClientRecorder.ECLIPSE_IDE);
				registerDocumentListenersForOpenEditors();
				FileBuffers.getTextFileBufferManager().addFileBufferListener(
						new FileBufferListener());
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				workspace.addResourceChangeListener(
						new ResourceListener(),
						IResourceChangeEvent.PRE_REFRESH
								| IResourceChangeEvent.POST_CHANGE);
				ICommandService commandService = (ICommandService) PlatformUI
						.getWorkbench().getActiveWorkbenchWindow()
						.getService(ICommandService.class);
				commandService.addExecutionListener(new SaveCommandExecutionListener());
				
				RefactoringHistoryService refactoringHistoryService = RefactoringHistoryService.getInstance();
				refactoringHistoryService.addExecutionListener(new RefactoringExecutionListener());
				
				DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchListener());;
				
				return Status.OK_STATUS;
			}

			private void getInitialSnapshot() {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				String zipFile = getLocalStorage().getAbsolutePath() + "/" + System.currentTimeMillis() + ".zip";
				System.out.println(zipFile);
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
				File pluginStoragePath = getLocalStorage();
				File workspaceIdFile = new File (pluginStoragePath.getAbsolutePath() + "workspace_id");
				String workspaceID = "";
				if (workspaceIdFile.exists()) {
					BufferedReader reader;
					try {
						reader = new BufferedReader(new FileReader(workspaceIdFile));
						workspaceID = reader.readLine();
						reader.close();
					} catch (IOException e) {
					}
				} else {
					try {
						workspaceIdFile.createNewFile();
						workspaceID = UUID.randomUUID().toString();
						BufferedWriter writer = new BufferedWriter(new FileWriter(workspaceIdFile));
						writer.write(workspaceID);
						writer.close();
					} catch (IOException e) {
					}
				}
				return workspaceID;
			}

			private File getLocalStorage() {
				return plugin.getBundle().getDataFile("");
			}

			private void registerDocumentListenersForOpenEditors() {
				IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IEditorReference[] editorReferences = activeWindow
						.getActivePage().getEditorReferences();
				for (IEditorReference editorReference : editorReferences) {
					IDocument document = getDocumentForEditor(editorReference);
					document.addDocumentListener(new DocumentListener());
				}
			}

			private IDocument getDocumentForEditor(
					IEditorReference editorReference) {
				IEditorPart editorPart = editorReference.getEditor(true);
				ISourceViewer sourceViewer = (ISourceViewer) editorPart
						.getAdapter(ITextOperationTarget.class);
				IDocument document = sourceViewer.getDocument();
				return document;
			}
		};

		uiJob.schedule();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static COPEPlugin getDefault() {
		return plugin;
	}
	
	public ClientRecorder getClientRecorderInstance() {
		return clientRecorder;
	}
	
	public String getWorkspaceID() {
		return workspaceID;
	}

}
