package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.keyboard.SWTKeyboardStrategy;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.tests.harness.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.oregonstate.cope.clientRecorder.ChangeOrigin;
import edu.oregonstate.cope.eclipse.listeners.DocumentListener;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditorRecordingTest {

	private MockRecorder recorder;
	private IDocument document;
	private IEditorPart editor;
	private static SWTWorkbenchBot bot;
	private IProject project;
	
	private boolean done;
	
	@BeforeClass
	public static void beforeClass() {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
	}

	@Before
	public void before() throws Exception {
		recorder = new MockRecorder();
		COPEPlugin.getDefault().setClientRecorder(recorder);
		this.done = false;
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				try {
					project = FileUtil.createProject("testProject");
					IFile file = FileUtil.createFile("testFile.java", project);
					editor = workbenchWindow.getActivePage().openEditor(new FileEditorInput(file), JavaUI.ID_CU_EDITOR, true);
					document = getDocumentForEditor(editor);
					document.addDocumentListener(new DocumentListener());
					document.set("Hello there");
					done = true;
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		});
		
		while (!done)
			Thread.sleep(100);
		
		assertEquals(editor, bot.activeEditor().getReference().getEditor(true));
	}
	
	@After
	public void after() throws Exception {
		bot.activeEditor().toTextEditor().saveAndClose();
		FileUtil.deleteProject(project);
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
		SWTBotEclipseEditor textEditor = bot.activeEditor().toTextEditor();
		String text = textEditor.getText();
		textEditor.typeText(0, 0, "H");
		text = textEditor.getText();
		
		KeyStroke command = KeyStroke.getInstance(SWT.COMMAND, KeyStroke.NO_KEY);
		KeyStroke z = KeyStroke.getInstance("Z");

		KeyboardFactory.getSWTKeyboard().pressShortcut(command, z);
		
		Thread.sleep(100);
		
		assertEquals(ChangeOrigin.UNDO, recorder.recordedChangeOrigin);
		assertEquals(1,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("",recorder.recordedText);
	}
}
