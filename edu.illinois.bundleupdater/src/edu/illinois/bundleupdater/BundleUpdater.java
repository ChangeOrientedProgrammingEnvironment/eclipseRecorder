/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.bundleupdater;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

public class BundleUpdater {

	private String updateSite;

	private String pluginID;

	public BundleUpdater(String updateSite, String pluginID) {
		this.updateSite= updateSite;
		this.pluginID= pluginID;
	}

	private URI getUpdateSiteURI(String updateSite) {
		try {
			return new URI(updateSite);
		} catch (URISyntaxException e) {
			Activator.getDefault().logErrorStatus("Invalid update site URI", e);
		}
		return null;
	}

	public boolean shouldUpdate() {
		try {
			IProvisioningAgentProvider agentProvider= Activator.getDefault().getProvisioningAgentProvider();
			if (agentProvider == null) {
				Activator.getDefault().logErrorStatus("Could not find a provisioning agent provider.", new RuntimeException());
				return false;
			}

			final IProvisioningAgent agent= agentProvider.createAgent(null);

			IMetadataRepositoryManager metadataRepositoryManager= (IMetadataRepositoryManager)agent.getService(IMetadataRepositoryManager.SERVICE_NAME);

			if (metadataRepositoryManager == null) {
				Activator.getDefault().logErrorStatus("Could not find the meta data repository manager.", new RuntimeException());
				return false;
			}

			IArtifactRepositoryManager artifactRepositoryManager= (IArtifactRepositoryManager)agent.getService(IArtifactRepositoryManager.SERVICE_NAME);

			if (artifactRepositoryManager == null) {
				Activator.getDefault().logErrorStatus("Could not find the artifact repository manager.", new RuntimeException());
				return false;
			}

			metadataRepositoryManager.addRepository(getUpdateSiteURI(updateSite));
			artifactRepositoryManager.addRepository(getUpdateSiteURI(updateSite));

			metadataRepositoryManager.loadRepository(getUpdateSiteURI(updateSite), new NullProgressMonitor());

			final IProfileRegistry registry= (IProfileRegistry)agent.getService(IProfileRegistry.SERVICE_NAME);

			if (registry == null) {
				Activator.getDefault().logErrorStatus("Could not find the profile registry.", new RuntimeException());
				return false;
			}

			final IProfile profile= registry.getProfile(IProfileRegistry.SELF);

			if (profile == null) {
				Activator.getDefault().logErrorStatus("Could not find the profile.", new RuntimeException());
				return false;
			}

			IQuery<IInstallableUnit> query= QueryUtil.createIUQuery(pluginID);
			Collection<IInstallableUnit> iusToUpdate= profile.query(query, null).toUnmodifiableSet();

			ProvisioningSession provisioningSession= new ProvisioningSession(agent);

			final UpdateOperation updateOperation= new UpdateOperation(provisioningSession, iusToUpdate);

			IStatus modalResolution= updateOperation.resolveModal(new NullProgressMonitor());

			if (modalResolution.isOK())
				return true;
		} catch (ProvisionException e) {
			Activator.getDefault().logErrorStatus("A provisioning exception occured while checking for updates.", e);
		}
		return false;
	}
}
