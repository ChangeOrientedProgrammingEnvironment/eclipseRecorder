package edu.oregonstate.cope.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import edu.oregonstate.cope.eclipse.COPEPlugin;
import edu.oregonstate.cope.eclipse.installer.Installer;
import edu.oregonstate.cope.eclipse.installer.InstallerOperation;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

public class SurveyOperation extends InstallerOperation {

	public SurveyOperation() {
		super(COPEPlugin.getDefault().getVersionedLocalStorage().toPath().toAbsolutePath(), 
				COPEPlugin.getDefault().getBundleStorage().toPath().toAbsolutePath());
	}

	@Override
	protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
		SurveyWizard sw = new SurveyWizard();
		WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), sw);
		wizardDialog.open();

		writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
		writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());

		handleEmail(sw.getEmail());
	}

	private void handleEmail(String email) throws IOException {
		doFor(permanentDirectory, email);
		doFor(workspaceDirectory, email);
	}

	private void doFor(Path parentDirectory, String email) throws IOException {
		Path emailFile = parentDirectory.resolve(Installer.EMAIL_FILENAME);
		Files.deleteIfExists(emailFile);
		writeContentsToFile(emailFile, email);
	}

}