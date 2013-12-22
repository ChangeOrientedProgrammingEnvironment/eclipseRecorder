package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;
import edu.oregonstate.cope.clientRecorder.fileOps.SimpleFileProvider;
import edu.oregonstate.cope.clientRecorder.util.COPELogger;

public class RecorderFacade {
	private static final String LOG_FILE_NAME = "log";
	private static final String WORKSPACE_CONFIG_FILENAME = "config";
	private static final String INSTALLATION_CONFIG_FILENAME = "config-install";

	private static class Instance {
		public static final RecorderFacade _instance = new RecorderFacade();
	}

	private Properties workspaceProperties;
	private Properties installationProperties;

	private ClientRecorder clientRecorder;
	private Uninstaller uninstaller;
	private COPELogger copeLogger;

	private RecorderFacade() {
		initLogger();
	}

	public RecorderFacade initialize(String workspaceDirectory, String permanentDirectory, String IDE) {
		initFileLogging(workspaceDirectory);

		initProperties(workspaceDirectory, permanentDirectory);
		initUninstaller();
		initPersister(workspaceDirectory);
		initClientRecorder(IDE);

		return this;
	}

	private void initFileLogging(String rootDirectory) {
		initLogger();
		copeLogger.enableFileLogging(rootDirectory, LOG_FILE_NAME);
	}

	private void initLogger() {
		copeLogger = new COPELogger();
		
		// copeLogger.logOnlyErrors();
		copeLogger.logEverything();
		copeLogger.disableConsoleLogging();
	}

	private void initClientRecorder(String IDE) {
		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE(IDE);
	}

	private void initUninstaller() {
		uninstaller = new Uninstaller(installationProperties);
	}

	private void initProperties(String workspaceDirectory, String permanentDirectory) {
		workspaceProperties = createProperties(workspaceDirectory, WORKSPACE_CONFIG_FILENAME);
		installationProperties = createProperties(permanentDirectory, INSTALLATION_CONFIG_FILENAME);
	}

	private Properties createProperties(String rootDirectory, String fileName) {
		SimpleFileProvider configFileProvider = new SimpleFileProvider(fileName);
		configFileProvider.setRootDirectory(rootDirectory);
		return new Properties(configFileProvider);
	}

	private void initPersister(String rootDirectory) {
		EventFilesProvider eventFileProvider = new EventFilesProvider();
		eventFileProvider.setRootDirectory(rootDirectory);
		ChangePersister.instance().setFileManager(eventFileProvider);
	}

	public ClientRecorder getClientRecorder() {
		return clientRecorder;
	}

	public Properties getWorkspaceProperties() {
		return workspaceProperties;
	}

	public Properties getInstallationProperties() {
		return installationProperties;
	}

	public Uninstaller getUninstaller() {
		return uninstaller;
	}

	public COPELogger getLogger() {
		return copeLogger;
	}

	public static RecorderFacade instance() {
		return Instance._instance;
	}

	public String getInstallationConfigFilename() {
		return INSTALLATION_CONFIG_FILENAME;
	}

}
