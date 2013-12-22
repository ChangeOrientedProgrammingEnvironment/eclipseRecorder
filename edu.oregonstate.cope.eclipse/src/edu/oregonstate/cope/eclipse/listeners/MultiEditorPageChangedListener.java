package edu.oregonstate.cope.eclipse.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class MultiEditorPageChangedListener implements IPageChangedListener {
	
	private List<Object> seenPages = new ArrayList<Object>();

	@Override
	public void pageChanged(PageChangedEvent event) {
		Object selectedPage = event.getSelectedPage();
		if (seenPages.contains(selectedPage))
			return;
		
		seenPages.add(selectedPage);
		if (selectedPage instanceof AbstractTextEditor) {
			IEditorPart editorPart = (IEditorPart) selectedPage;
			ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
			sourceViewer.getDocument().addDocumentListener(new DocumentListener());
		}
	}

}
