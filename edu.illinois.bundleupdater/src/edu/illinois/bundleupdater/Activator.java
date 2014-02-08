/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.bundleupdater;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID= "edu.illinois.bundleupdater"; //$NON-NLS-1$

	private static Activator plugin;

	private ServiceReference provisioningAgentProviderServiceReference;

	private IProvisioningAgentProvider provisioningAgentProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		provisioningAgentProviderServiceReference= bundleContext.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
		if (provisioningAgentProviderServiceReference != null) {
			provisioningAgentProvider= (IProvisioningAgentProvider)bundleContext.getService(provisioningAgentProviderServiceReference);
		}
		plugin= this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		provisioningAgentProviderServiceReference= null;
		provisioningAgentProvider= null;
		plugin= null;
	}

	public static Activator getDefault() {
		return plugin;
	}

	public ServiceReference getAgentProviderServiceReference() {
		return provisioningAgentProviderServiceReference;
	}

	public IProvisioningAgentProvider getProvisioningAgentProvider() {
		return provisioningAgentProvider;
	}

	private ILog getLog() {
		return Platform.getLog(Platform.getBundle(PLUGIN_ID));
	}

	public void log(IStatus status) {
		getLog().log(status);
	}

	public void logErrorStatus(String message, Exception e) {
		log(new Status(Status.ERROR, PLUGIN_ID, message, e));
	}

}
