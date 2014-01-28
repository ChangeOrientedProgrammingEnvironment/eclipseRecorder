package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.tests.harness.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import edu.oregonstate.cope.clientRecorder.ChangeOrigin;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;

public class EditorRecordingTest {

	private MockRecorder recorder;
	private IDocument document;
	private IEditorPart editor;

	@Before
	public void before() throws Exception {
		recorder = new MockRecorder();
		COPEPlugin.getDefault().setClientRecorder(recorder);
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IProject project = FileUtil.createProject("testProject");
		IFile file = FileUtil.createFile("testFile.java", project);
		editor = workbenchWindow.getActivePage().openEditor(new FileEditorInput(file), JavaUI.ID_CU_EDITOR, true);
		document = getDocumentForEditor(editor);
		document.addDocumentListener(new DocumentListener());
		document.set("Hello there");
	}
	
	private IDocument getDocumentForEditor(IEditorPart editorPart) {
		if (editorPart instanceof MultiPageEditorPart) {
			return null;
		}
		ISourceViewer sourceViewer = (ISourceViewer) editorPart.getAdapter(ITextOperationTarget.class);
		IDocument document = sourceViewer.getDocument();
		return document;
	}
	
	@Test
	public void testAddCharacter() throws Exception {
		document.replace(0, 1, "!");
		assertEquals(0, recorder.recordedOffset);
		assertEquals(1, recorder.recordedLength);
		assertEquals("!", recorder.recordedText);
		assertEquals("/testProject/testFile.java", recorder.recordedSourceFile);
		assertEquals(ChangeOrigin.USER, recorder.recordedChangeOrigin);
	}
	
	@Test
	public void testUndo() throws Exception {
		document.replace(0, 1, "!");

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(editor);
		
//		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getAdapter(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) editor.getSite().getService(IHandlerService.class);
		handlerService.executeCommand(IWorkbenchCommandConstants.EDIT_UNDO, null);
		
		assertEquals(ChangeOrigin.UNDO, recorder.recordedChangeOrigin);
	}
}
