package edu.oregonstate.cope.eclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.clientRecorder.util.LoggerInterface;

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
	}

	public void initializeSnapshotManager() {
		snapshotManager = new SnapshotManager(COPEPlugin.getVersionedLocalStorage().getAbsolutePath());
	}

	public void takeSnapshotOfKnownProjects() {
		snapshotManager.takeSnapshotOfKnownProjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		snapshotManager.takeSnapshotOfSessionTouchedProjects();
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

	public Properties getWorkspaceProperties() {
		return recorderFacade.getWorkspaceProperties();
	}

	public Properties getInstallationProperties() {
		return recorderFacade.getInstallationProperties();
	}

	public Uninstaller getUninstaller() {
		return recorderFacade.getUninstaller();
	}

	public void initializeRecorder(String workspaceDirectory, String permanentDirectory, String workspaceID, String IDE) {
		this.workspaceID = workspaceID;
		recorderFacade = RecorderFacade.instance().initialize(workspaceDirectory, permanentDirectory, IDE);
	}

	protected File getWorkspaceIdFile() {
		File pluginStoragePath = COPEPlugin.getVersionedLocalStorage();
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
	
	public static File getVersionedLocalStorage() {
		return getVersionedPath(getLocalStorage().toPath()).toFile();
	}

	public static File getVersionedBundleStorage() {
		return getVersionedPath(getBundleStorage().toPath()).toFile();
	}

	private static Path getVersionedPath(Path path) {
		String version = getPluginVersion().toString();
		return path.resolve(version);
	}

	public static Version getPluginVersion() {
		return COPEPlugin.plugin.getBundle().getVersion();
	}

	public LoggerInterface getLogger() {
		return recorderFacade.getLogger();
	}

	public SnapshotManager getSnapshotManager() {
		return snapshotManager;
	}
	
	/**
	 * Used only by the Installer.
	 * TODO something is fishy here, this string should not leak outside
	 */
	public String _getInstallationConfigFileName() {
		return RecorderFacade.instance().getInstallationConfigFilename();
	}
}
