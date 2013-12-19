package edu.oregonstate.cope.eclipse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import edu.oregonstate.cope.clientRecorder.Uninstaller;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

public class Installer {

	private static final String SURVEY_FILENAME = "survey.txt";

	private String workspaceDirectory;
	private String permanentDirectory;
	private Uninstaller uninstaller;
	private String installationConfigFileName;

	private abstract class Operation {

		public void perform(String fileName) throws IOException {
			File workspaceFile = Paths.get(workspaceDirectory, fileName).toFile();
			File permanentFile = Paths.get(permanentDirectory, fileName).toFile();

			if (workspaceFile.exists() && permanentFile.exists()) {
				System.out.println("BOTH FILES EXIST");
			}

			else if (!workspaceFile.exists() && permanentFile.exists()) {
				System.out.println("ONLY PERMANENT EXISTS");
				Files.copy(permanentFile.toPath(), workspaceFile.toPath());
			}

			else if (workspaceFile.exists() && !permanentFile.exists()) {
				System.out.println("ONLY WORKSPACE EXISTS");
				Files.copy(workspaceFile.toPath(), permanentFile.toPath());
			}

			else if (!workspaceFile.exists() && !permanentFile.exists()) {
				System.out.println("NO FILE EXISTS");
				doNoFileExists(workspaceFile, permanentFile);
			}
		}

		protected abstract void doNoFileExists(File workspaceFile, File permanentFile) throws IOException;

		protected void writeContentsToFile(Path filePath, String fileContents) throws IOException {
			Files.write(filePath, fileContents.getBytes(), StandardOpenOption.CREATE);
		}
	}

	private class SurveyOperation extends Operation {

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
			System.out.println("GIVE SURVEY");

			SurveyWizard sw = new SurveyWizard();
			WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), sw);
			wizardDialog.open();

			writeContentsToFile(workspaceFile.toPath(), sw.getSurveyResults());
			writeContentsToFile(permanentFile.toPath(), sw.getSurveyResults());
		}
	}

	private class ConfigInstallOperation extends Operation {

		@Override
		protected void doNoFileExists(File workspaceFile, File permanentFile) throws IOException {
			uninstaller.initUninstallInMonths(3);

			Files.copy(permanentFile.toPath(), workspaceFile.toPath());
		}

	}

	public Installer(String workspaceDirectory, String permanentDirectory,
			Uninstaller uninstaller, String installationConfigFileName) {
		this.workspaceDirectory = workspaceDirectory;
		this.permanentDirectory = permanentDirectory;
		this.uninstaller = uninstaller;
		this.installationConfigFileName = installationConfigFileName;
		
		System.err.println(workspaceDirectory);
		System.err.println(permanentDirectory);
	}

	public void doInstall() {
		try {
			new SurveyOperation().perform(SURVEY_FILENAME);
			new ConfigInstallOperation().perform(installationConfigFileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
