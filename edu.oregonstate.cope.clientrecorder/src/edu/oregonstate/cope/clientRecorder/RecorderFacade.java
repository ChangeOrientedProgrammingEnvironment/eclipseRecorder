package edu.oregonstate.cope.clientRecorder;

import edu.oregonstate.cope.clientRecorder.fileOps.ConfigFileProvider;

public class RecorderFacade {
	private ClientRecorder clientRecorder;
	private RecorderProperties recorderProperties;

	public RecorderFacade initialize(String rootDirectory, String IDE) {
		ChangePersister.instance().setRootDirectory(rootDirectory);
		
		ConfigFileProvider configFileProvider = new ConfigFileProvider();
		configFileProvider.setRootDirectory(rootDirectory);
		recorderProperties = new RecorderProperties(configFileProvider);

		clientRecorder = new ClientRecorder();
		clientRecorder.setIDE(IDE);
		
		return this;
	}

	public ClientRecorder getClientRecorder() {
		return clientRecorder;
	}

	public RecorderProperties getRecorderProperties() {
		return recorderProperties;
	}
}
