package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.EventFilesProvider;
import edu.oregonstate.cope.clientRecorder.fileOps.SimpleFileProvider;
import edu.oregonstate.cope.clientRecorder.util.COPELogger;

public class RecorderFacade {
	private static final String LOG_FILE_NAME = "log";
	private static final String CONFIG_FILE_NAME = "config";
	private static RecorderFacade _instance;
	
	private ClientRecorder clientRecorder;
	private Properties properties;
	private Uninstaller uninstaller;
	private COPELogger copeLogger;

	public RecorderFacade initialize(String rootDirectory, String IDE) {
		//TODO this is horrible. Fix it.
		_instance = this;

		initLogger(rootDirectory);
		
		initPersister(rootDirectory);
		initProperties(rootDirectory);
		initUninstaller();
		initClientRecorder(IDE);

		return this;
	}

	private void initLogger(String rootDirectory) {
		copeLogger = new COPELogger();
		copeLogger.enableFileLogging(rootDirectory, LOG_FILE_NAME);
		//copeLogger.logOnlyErrors();
		copeLogger.logEverything();
		
	}

	private void initClientRecorder(String IDE) {
		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE(IDE);
	}

	private void initUninstaller() {
		uninstaller = new Uninstaller(properties);
	}

	private void initProperties(String rootDirectory) {
		SimpleFileProvider configFileProvider = new SimpleFileProvider(CONFIG_FILE_NAME);
		configFileProvider.setRootDirectory(rootDirectory);
		properties = new Properties(configFileProvider);
	}

	private void initPersister(String rootDirectory) {
		EventFilesProvider eventFileProvider = new EventFilesProvider();
		eventFileProvider.setRootDirectory(rootDirectory);
		ChangePersister.instance().setFileManager(eventFileProvider);
	}

	public ClientRecorder getClientRecorder() {
		return clientRecorder;
	}

	public Properties getProperties() {
		return properties;
	}

	public Uninstaller getUninstaller() {
		return uninstaller;
	}
	
	public COPELogger getLogger(){
		return copeLogger;
	}
	
	//TODO this is ugly. Needs to be fixed. Maybe turn this class into a configurable singleton?
	public static RecorderFacade instance(){
		return _instance;
	}
}
