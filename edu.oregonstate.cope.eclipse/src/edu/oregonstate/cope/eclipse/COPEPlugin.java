package edu.oregonstate.cope.eclipse;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import edu.oregonstate.cope.eclipse.listeners.DocumentListener;
import edu.oregonstate.cope.eclipse.listeners.FileBufferListener;
import edu.oregonstate.cope.eclipse.listeners.ResourceListener;
import edu.oregonstate.cope.eclipse.listeners.SaveCommandExecutionListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class COPEPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.oregonstate.edu.eclipse"; //$NON-NLS-1$

	// The shared instance
	private static COPEPlugin plugin;
	
	/**
	 * The constructor
	 */
	public COPEPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		UIJob uiJob = new UIJob("Registering listeners") {
			
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				monitor.beginTask("Registering listeners", 1);
				registerDocumentListenersForOpenEditors();
				FileBuffers.getTextFileBufferManager().addFileBufferListener(new FileBufferListener());
				monitor.done();
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				workspace.addResourceChangeListener(
						new ResourceListener(),
						IResourceChangeEvent.PRE_REFRESH
								| IResourceChangeEvent.POST_CHANGE);
				ICommandService commandService = (ICommandService) PlatformUI
						.getWorkbench().getActiveWorkbenchWindow()
						.getService(ICommandService.class);
				commandService.addExecutionListener(new SaveCommandExecutionListener());
				return Status.OK_STATUS;
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
		};
		
		uiJob.schedule();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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

}
