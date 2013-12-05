package edu.oregonstate.cope.eclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.clientRecorder.util.COPELogger;

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

	private SnapshotManager snapshotManager;

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
		snapshotManager = new SnapshotManager(COPEPlugin.getLocalStorage().getAbsolutePath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		snapshotManager.takeSnapshotOfKnownProjects();
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

	public ClientRecorder getClientRecorder() {
		return recorderFacade.getClientRecorder();
	}

	public Properties getProperties() {
		return recorderFacade.getProperties();
	}

	Uninstaller getUninstaller() {
		return recorderFacade.getUninstaller();
	}

	public void initializeRecorder(String rootDirectory, String workspaceID, String IDE) {
		this.workspaceID = workspaceID;
		recorderFacade = RecorderFacade.instance().initialize(rootDirectory, IDE);
	}

	protected File getWorkspaceIdFile() {
		File pluginStoragePath = COPEPlugin.getLocalStorage();
		return new File(pluginStoragePath.getAbsolutePath() + File.separator + "workspace_id");
	}

	public String getWorkspaceID() {
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

	public static File getLocalStorage() {
		return COPEPlugin.plugin.getStateLocation().toFile();
	}
	
	public static File getBundleStorage() {
	    return COPEPlugin.getDefault().getBundle().getDataFile("");
	  }

	public COPELogger getLogger() {
		return recorderFacade.getLogger();
	}

	public SnapshotManager getSnapshotManager() {
		return snapshotManager;
	}
}
