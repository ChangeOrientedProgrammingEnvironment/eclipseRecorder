package edu.oregonstate.cope.eclipse.ui;
import java.util.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import edu.oregonstate.cope.eclipse.ui.handlers.DemoSurveyDialog;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;


public class SurveyManager {
	

	private static final String SAMPLE_SURVEY = "Question Number: 0 answer:-1 Question Number: 1 answer:-1";

	public SurveyManager(){
		
	}
	
	public void checkAndRunSurvey(String WorkspaceDir,String BundleDir,Shell parent){
		boolean workspaceFileExists = false;
		boolean bundleFileExists = false;
		Path workspaceSurveyPath = Paths.get(WorkspaceDir,"survey.txt");
		Path bundleSurveyPath = Paths.get(BundleDir,"survey.txt") ;
		
		
		if(Files.exists(workspaceSurveyPath)){
			workspaceFileExists = true;
		}
		
		if(Files.exists(bundleSurveyPath)){
			bundleFileExists = true;
		}
		
		if(workspaceFileExists && bundleFileExists){
			System.out.println("BOTH FILES EXIST");
		}else if(!workspaceFileExists && bundleFileExists){
			
			//String sampleSurvey = "Question Number: 0 answer:-1 Question Number: 1 answer:-1";
			List<String> lines = null;
			try {
				 Files.copy(bundleSurveyPath, workspaceSurveyPath);
				 System.out.println("ONLY BUNDLE EXISTS");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(workspaceFileExists && !bundleFileExists){
			System.out.println("ONLY WORKSPACE EXISTS");
			String sampleSurvey = SAMPLE_SURVEY;
			writeContentsToFile(bundleSurveyPath,sampleSurvey);
		}else if(!workspaceFileExists && !bundleFileExists){
			System.out.println("NO FILE EXISTS, GIVE SURVEY");
			
//			DemoSurveyDialog dsd = new DemoSurveyDialog(parent);
//			dsd.open();
			
			String sampleSurvey = SAMPLE_SURVEY;
			writeContentsToFile(workspaceSurveyPath,sampleSurvey);
			writeContentsToFile(bundleSurveyPath,sampleSurvey);
		}
		
		WizardDialog wizardDialog = new WizardDialog(parent.getShell(), new SurveyWizard());
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}
	
	protected void writeContentsToFile(Path filePath, String currFileContents) {
        try {
            Files.write(filePath, currFileContents.getBytes(),StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
