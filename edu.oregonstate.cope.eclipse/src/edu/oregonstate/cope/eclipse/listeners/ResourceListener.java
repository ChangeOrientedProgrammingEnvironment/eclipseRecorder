package edu.oregonstate.cope.eclipse.listeners;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

public class ResourceListener implements IResourceChangeListener {
	
	/**
	 * Class that handles the save recording in a different thread.
	 * 
	 * @author Caius Brindescu
	 *
	 */
	private class SaveRecorder implements Runnable {
		
		private IResourceChangeEvent event;
		private ClientRecorder recorder;

		public SaveRecorder(IResourceChangeEvent event, ClientRecorder recorder) {
			this.event = event;
			this.recorder = recorder;
		}

		@Override
		public void run() {
			IResourceDelta delta = event.getDelta();
			recordFileSave(delta);
		}

		private void recordFileSave(IResourceDelta delta) {
			IResource affectedResource = delta.getResource();
			if (affectedResource.getType() == IResource.FILE) {
				String filePath = affectedResource.getFullPath().toPortableString();
				if (filePath.endsWith(".class"))
					return;
				recorder.recordFileSave(filePath);
				return;
			}
			
			IResourceDelta[] affectedChildren = delta.getAffectedChildren();
			for (IResourceDelta child : affectedChildren) {
				recordFileSave(child);
			}
		}

	}


	ClientRecorder recorder = COPEPlugin.getDefault().getClientRecorder();
	ForkJoinPool pool = new ForkJoinPool(1);

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("" + System.currentTimeMillis() + event);
		if (isSavedAction() || isRefactoringInProgress()) {
			recordFileSave(event);
		} else {}
			recordRefresh(event.getDelta());
	}

	/**
	 * Records a save. It creates a new {@link SaveRecorder} and
	 * launches it in a separate thread. I need this to ensure
	 * a speedy resolution of the save, so I can catch following
	 * events. This is needed when dealing with multiple save,
	 * since each one is sent as a different event.
	 * r
	 * @param event the event being recorded
	 */
	private void recordFileSave(IResourceChangeEvent event) {
		pool.execute(new SaveRecorder(event, recorder));
	}

	private void recordRefresh(IResourceDelta delta) {
		if (delta == null)
			return ;
		
		IResource affectedResource = delta.getResource();
		int type = affectedResource.getType();
		if (type == IResource.FILE) {
			IFile affectedFile = (IFile) affectedResource;
			if (isClassFile(affectedFile)) {
				return;
			}
			InputStream inputStream;
			try {
				inputStream = affectedFile.getContents();
				Scanner scanner = new Scanner(inputStream, affectedFile.getCharset());
				String contents = scanner.useDelimiter("\\A").next();
				scanner.close();
				recorder.recordTextChange(contents, 0, 0,affectedFile.getFullPath().toPortableString(), ClientRecorder.CHANGE_ORIGIN_REFRESH);
			} catch (CoreException e) {
			}
		}
		IResourceDelta[] children = delta.getAffectedChildren();
		for (IResourceDelta child : children) {
			recordRefresh(child);
		}
	}

	private boolean isClassFile(IFile affectedFile) {
		String fileExtension = affectedFile.getFileExtension();
		if (fileExtension.equals("class"))
			return true;
		return false;
	}

	private boolean isSavedAction() {
		return SaveCommandExecutionListener.isSaveInProgress();
	}

	private boolean isRefactoringInProgress() {
		return RefactoringExecutionListener.isRefactoringInProgress();
	}
}
