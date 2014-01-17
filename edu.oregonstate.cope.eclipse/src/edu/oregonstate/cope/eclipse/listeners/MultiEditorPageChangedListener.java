package edu.oregonstate.cope.eclipse.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import edu.oregonstate.cope.eclipse.COPEPlugin;

public class MultiEditorPageChangedListener implements IPageChangedListener {
	
	private static COPEPlugin plugin = COPEPlugin.getDefault();
	private List<Object> seenPages = new ArrayList<Object>();

	@Override
	public void pageChanged(PageChangedEvent event) {
		Object selectedPage = event.getSelectedPage();
		if (seenPages.contains(selectedPage))
			return;
		
		seenPages.add(selectedPage);
		if (selectedPage instanceof AbstractTextEditor) {
			IEditorPart editorPart = (IEditorPart) selectedPage;
			IProject project = plugin.getProjectForEditor(editorPart.getEditorInput());
			if (plugin.getIgnoreProjectsList().contains(project.getName()))
				return;
			ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
			sourceViewer.getDocument().addDocumentListener(new DocumentListener());
		}
	}

}
