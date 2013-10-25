package edu.oregonstate.cope.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import edu.oregonstate.cope.clientRecorder.ChangePersister;
import edu.oregonstate.cope.clientRecorder.ClientRecorder;

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

	public ClientRecorder getClientRecorderInstance() {
		return clientRecorder;
	}

	public String getWorkspaceID() {
		return workspaceID;
	}

	public void setClientRecorder(ClientRecorder clientRecorder) {
		this.clientRecorder = clientRecorder;
	}

	public ClientRecorder clientRecorder() {
		return clientRecorder;
	}

	public void setEventFilesRootDirectory(String rootDirectory) {
		ChangePersister.instance().setRootDirectory(rootDirectory);
	}
}
