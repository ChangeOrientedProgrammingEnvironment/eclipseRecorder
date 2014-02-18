package edu.oregonstate.cope.eclipse.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class JavaElementChangedListener implements IElementChangedListener {

	@Override
	public void elementChanged(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();
		try {
			processDelta(delta);
		} catch (JavaModelException | IOException e) {
		}
	}

	private void processDelta(IJavaElementDelta delta) throws JavaModelException, IOException {
		IJavaElementDelta[] affectedChildren = delta.getAffectedChildren();
		for (IJavaElementDelta child : affectedChildren) {
			processDelta(child);
		}
		
		if (delta.getKind() == IJavaElementDelta.ADDED | delta.getKind() == IJavaElementDelta.CHANGED)
			if (delta.getFlags() == IJavaElementDelta.F_ADDED_TO_CLASSPATH) {
				IJavaElement changedElement = delta.getElement();
				if (changedElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
					IClasspathEntry classpathEntry = ((IPackageFragmentRoot) changedElement).getRawClasspathEntry();
					if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
						String jarPath = classpathEntry.getPath().toOSString();
						byte[] jarBytes = Files.readAllBytes(Paths.get(jarPath));
						COPEPlugin.getDefault().getClientRecorder().recordExternalLibraryAdd(jarPath, new String(Base64.encodeBase64(jarBytes)));
					}
				}
			}
	}

}
