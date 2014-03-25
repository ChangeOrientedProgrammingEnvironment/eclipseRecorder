package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.clientRecorder.Properties;
import edu.oregonstate.cope.clientRecorder.RecorderFacade;
import edu.oregonstate.cope.clientRecorder.StorageManager;
import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.clientRecorder.util.LoggerInterface;

/**
 * The activator class controls the plug-in life cycle
 */
public class COPEPlugin extends AbstractUIPlugin implements StorageManager {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.oregonstate.edu.eclipse"; //$NON-NLS-1$

	private static final String PREFERENCES_IGNORED_PROJECTS = "ignoredProjects";

	// The shared instance
	static COPEPlugin plugin;

	// The ID of the current workspace
	String workspaceID;

	private RecorderFacade recorderFacade;

	private SnapshotManager snapshotManager;
	
	private List<String> ignoredProjects;

	private ClientRecorder clientRecorder;

	public static final List<String> knownTextFiles = Arrays.asList(new String[]{"txt", "java", "xml", "mf", "c", "cpp", "c", "h"});

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
		
		initializeRecorder();
		initializeSnapshotManager();
		readIgnoredProjects();
		
		UIJob uiJob = new StartPluginUIJob(this, "Registering listeners");
		uiJob.schedule();
	}

	private void initializeSnapshotManager() {
		snapshotManager = new SnapshotManager(getLocalStorage().getAbsolutePath());
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
		if (snapshotManager != null)
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
		return clientRecorder;
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

	private void initializeRecorder() {
		String IDE = ClientRecorder.ECLIPSE_IDE;

		recorderFacade = RecorderFacade.instance().initialize(this, IDE);
		clientRecorder = recorderFacade.getClientRecorder();

		this.workspaceID = getWorkspaceID();
	}

	protected File getWorkspaceIdFile() {
		return recorderFacade.getWorkspaceIdFile();
	}

	public String getWorkspaceID() {
		return recorderFacade.getWorkspaceID();
	}

	public File getLocalStorage() {
		return COPEPlugin.plugin.getStateLocation().toFile();
	}

	public File getBundleStorage() {
		return COPEPlugin.getDefault().getBundle().getDataFile("");
	}
	
	public File getVersionedLocalStorage() {
		return getVersionedPath(getLocalStorage().toPath()).toFile();
	}

	public File getVersionedBundleStorage() {
		return getVersionedPath(getBundleStorage().toPath()).toFile();
	}

	private Path getVersionedPath(Path path) {
		String version = getPluginVersion().toString();
		return path.resolve(version);
	}

	public Version getPluginVersion() {
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
	
	public List<String> getIgnoreProjectsList() {
		return ignoredProjects;
	}
	
	public void setIgnoredProjectsList(List<String> ignoredProjects) {
		StringBuffer value = new StringBuffer();
		for (String project : ignoredProjects) {
			value.append(project);
			value.append(";");
		}
		COPEPlugin.getDefault().getWorkspaceProperties().addProperty(PREFERENCES_IGNORED_PROJECTS, value.toString());
		this.ignoredProjects = ignoredProjects;
	}

	protected void readIgnoredProjects() {
		String ignoredProjectsString = getWorkspaceProperties().getProperty(PREFERENCES_IGNORED_PROJECTS);
		if (ignoredProjectsString == null) {
			ignoredProjects = new ArrayList<>();
			return;
		}
		String[] projectNames = ignoredProjectsString.split(";");
		ignoredProjects = new ArrayList<>();
		for (String project : projectNames) {
			ignoredProjects.add(project);
		}
	}

	public IProject getProjectForEditor(IEditorInput editorInput) {
		IProject project;
		IFile file = ((FileEditorInput) editorInput).getFile();
		project = file.getProject();
		return project;
	}

	public List<String> getListOfWorkspaceProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<String> projectNames = new ArrayList<String>();
		for (IProject project : projects) {
			projectNames.add(project.getName());
		}
		
		return projectNames;
	}
	
	public void setClientRecorder(ClientRecorder recorder) {
		clientRecorder = recorder;
	}
}
