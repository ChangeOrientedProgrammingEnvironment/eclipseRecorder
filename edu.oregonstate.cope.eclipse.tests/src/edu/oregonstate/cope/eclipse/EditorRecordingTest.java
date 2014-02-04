package edu.oregonstate.cope.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.oregonstate.cope.clientRecorder.ChangeOrigin;
import edu.oregonstate.cope.clientRecorder.Events;
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
				try {
					project = FileUtil.createProject("testProject");
					IFile file = FileUtil.createFile("testFile.java", project);
					openJavaEditorOnFile(file);
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
	
	private void openJavaEditorOnFile(IFile file) throws PartInitException {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		editor = workbenchWindow.getActivePage().openEditor(new FileEditorInput(file), JavaUI.ID_CU_EDITOR, true);
		document = getDocumentForEditor(editor);
		document.addDocumentListener(new DocumentListener());
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
		typeHAtTheBeginning();
		doUndo();
		
		Thread.sleep(100);
		
		assertEquals(ChangeOrigin.UNDO, recorder.recordedChangeOrigin);
		assertEquals(1,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("",recorder.recordedText);
	}

	private void typeHAtTheBeginning() {
		SWTBotEclipseEditor textEditor = bot.activeEditor().toTextEditor();
		textEditor.typeText(0, 0, "H");
	}

	private void doUndo() throws ParseException {
		KeyStroke command = KeyStroke.getInstance(SWT.COMMAND, KeyStroke.NO_KEY);
		KeyStroke z = KeyStroke.getInstance("Z");

		KeyboardFactory.getSWTKeyboard().pressShortcut(command, z);
	}
	
	@Test
	public void testRedo() throws Exception {
		typeHAtTheBeginning();
		doUndo();
		
		SWTBotMenu redoMenu = bot.menu("Edit").menu("Redo Typing");
		if (!redoMenu.isEnabled())
			fail("Redo option is not active");
		redoMenu.click();
		
		Thread.sleep(100);
		
		assertEquals(ChangeOrigin.REDO, recorder.recordedChangeOrigin);
		assertEquals(0,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("H",recorder.recordedText);
	}
	
	@Test
	public void testCut() throws Exception {
		doCut();
		
		Thread.sleep(100);
		
		assertEquals(ChangeOrigin.CUT, recorder.recordedChangeOrigin);
		assertEquals(11,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("",recorder.recordedText);
		
	}

	private void doCut() {
		bot.activeEditor().toTextEditor().selectCurrentLine();
		bot.menu("Edit").menu("Cut").click();
	}
	
	@Test
	public void testCopy() throws Exception {
		bot.activeEditor().toTextEditor().selectCurrentLine();
		bot.menu("Edit").menu("Copy").click();
		
		Thread.sleep(100);
		
		assertEquals(Events.copy, recorder.recordedEvent);
		assertEquals(11,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("Hello there",recorder.recordedText);
		
	}
	
	public void testPaste() throws Exception {
		doCut();
		bot.menu("Edit").menu("Paste").click();
		assertEquals(Events.textChange, recorder.recordedEvent);
		assertEquals(ChangeOrigin.PASTE, recorder.recordedChangeOrigin);
		assertEquals(11,recorder.recordedLength);
		assertEquals(0,recorder.recordedOffset);
		assertEquals("Hello there",recorder.recordedText);
	}
	
	@Test
	public void testRefactoring() throws Exception {
		IJavaProject javaProject = FileUtil.createTestJavaProject("JavaProject");
		IPackageFragmentRoot srcFolderPkg = FileUtil.createSourceFolder(javaProject);
		IPackageFragment packageFragment = srcFolderPkg.createPackageFragment("test", true, new NullProgressMonitor());
		final ICompilationUnit compilationUnit = packageFragment.createCompilationUnit("TestFile.java", "package test;\n\npublic class TestFile{private int x;private int z=x;}\n", true, new NullProgressMonitor());
		javaProject.getProject().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				try {
					openJavaEditorOnFile((IFile) compilationUnit.getResource());
				} catch (PartInitException e) {
				}
			}
		});
		
		Thread.sleep(200);

		bot.activeEditor().toTextEditor().selectRange(2, 34, 1);
		bot.menu("Refactor").menu("Rename...").click();
		KeyboardFactory.getSWTKeyboard().typeText("y");
		KeyboardFactory.getSWTKeyboard().typeCharacter((char) Character.LINE_SEPARATOR);
		
		Thread.sleep(400);
		
		bot.button("Continue").click();
		
		Thread.sleep(200);
		
		assertEquals(ChangeOrigin.REFACTORING, recorder.recordedChangeOrigin);
	}
}
