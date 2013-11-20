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
	

	public SurveyManager(){
		
	}
	
	public void checkAndRunSurvey(String WorkspaceDir,String BundleDir,Shell parent){
		int workspaceFileExists = 0;
		int bundleFileExists = 0;
		Path workspacePath = Paths.get(WorkspaceDir,"survey.txt");
		Path bundleDir = Paths.get(BundleDir,"survey.txt") ;
		
		
		if(Files.exists(workspacePath)){
			workspaceFileExists = 1;
		}
		if(Files.exists(bundleDir)){
			workspaceFileExists = 1;
		}
		if(workspaceFileExists>0 && bundleFileExists>0){
			System.out.println("BOTH FILES EXIST");
		}else if(workspaceFileExists<1 && bundleFileExists>0){
			
			//String sampleSurvey = "Question Number: 0 answer:-1 Question Number: 1 answer:-1";
			List<String> lines = null;
			try {
				 lines = Files.readAllLines(workspacePath, Charset.defaultCharset() );
				 writeListToFile(Paths.get(WorkspaceDir,"survey.txt"),lines);
				 System.out.println("ONLY BUNDLE EXISTS");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			
		}else if(workspaceFileExists>0 && bundleFileExists<1){
			System.out.println("ONLY WORKSPACE EXISTS");
			String sampleSurvey = "Question Number: 0 answer:-1 Question Number: 1 answer:-1";
			writeContentsToFile(bundleDir,sampleSurvey);
		}else if(workspaceFileExists<1 && bundleFileExists<1){
			System.out.println("NO FILE EXISTS, GIVE SURVEY");
			
//			DemoSurveyDialog dsd = new DemoSurveyDialog(parent);
//			dsd.open();
			

			
			
			String sampleSurvey = "Question Number: 0 answer:-1 Question Number: 1 answer:-1";
			writeContentsToFile(workspacePath,sampleSurvey);
			writeContentsToFile(bundleDir,sampleSurvey);
		}
		
		WizardDialog wizardDialog = new WizardDialog(parent.getShell(),
			      new SurveyWizard());
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
	protected void writeListToFile(Path filePath,List<String> contents){
		try {
            Files.write(filePath, contents,Charset.defaultCharset(),StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
	}
	
}
