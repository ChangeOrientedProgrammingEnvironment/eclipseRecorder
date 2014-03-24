package edu.oregonstate.cope.eclipse;

import java.io.File;

public interface StorageManager {

	public File getLocalStorage();
	
	public File getBundleStorage();
	
	public File getVersionedLocalStorage();
	
	public File getVersionedBundleStorage();
}
