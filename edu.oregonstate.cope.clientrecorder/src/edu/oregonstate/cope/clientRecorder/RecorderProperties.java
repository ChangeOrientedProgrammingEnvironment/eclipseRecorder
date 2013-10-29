package edu.oregonstate.cope.clientRecorder;

import java.util.HashMap;
import java.util.List;

import edu.oregonstate.cope.clientRecorder.fileOps.FileProvider;

public class RecorderProperties {

	private FileProvider fileProvider;
	private HashMap<String, String> properties = new HashMap<>();

	public RecorderProperties(FileProvider fileProvider) {
		this.fileProvider = fileProvider;
		initFromProvider(fileProvider);
	}

	private void initFromProvider(FileProvider fileProvider) {
		List<String> lines = fileProvider.readAllLines(); 

		for (String line : lines) {
			int separatorIndex = line.indexOf("=");
			String key = line.substring(0, separatorIndex);
			String value = line.substring(separatorIndex + 1);

			properties.put(key, value);
		}
	}

	public void addProperty(String key, String value) {
		if(key == null)
			return;
		
		if(key.isEmpty() && value.isEmpty())
			return;
		
		properties.put(key, value);
		persist();
	}

	private void persist() {
		StringBuffer sb = new StringBuffer();

		for (String key : properties.keySet()) {
			sb.append(key + "=" + properties.get(key) + "\n");
		}

		fileProvider.writeToCurrentFile(sb.toString());
	}

	public String getProperty(String key) {
		if (key == null)
			return null;
		
		if(key.isEmpty() && properties.get(key).isEmpty())
			return "";
		
		return properties.get(key);
	}
}
