package edu.oregonstate.cope.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import edu.oregonstate.cope.clientRecorder.ChangePersister;
import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.RecorderProperties;

/**
 * The activator class controls the plug-in life cycle
 */
public class COPEPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.oregonstate.edu.eclipse"; //$NON-NLS-1$

	// The shared instance
	static COPEPlugin plugin;

	// The ID of the current workspace
	static String workspaceID;

	private RecorderFacade recorderFacade;

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

		UIJob uiJob = new StartPluginUIJob(this, "Registering listeners");
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

	public String getWorkspaceID() {
		return workspaceID;
	}

	public ClientRecorder getClientRecorderInstance() {
		return recorderFacade.getClientRecorder();
	}
	
	public RecorderProperties getClientProperties(){
		return recorderFacade.getRecorderProperties();
	}

	public void initializeRecorder(String rootDirectory, String workspaceID, String IDE) {
		this.workspaceID = workspaceID;
		recorderFacade = new RecorderFacade().initialize(rootDirectory, IDE);
	}
}
