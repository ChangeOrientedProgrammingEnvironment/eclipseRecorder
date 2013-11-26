package edu.oregonstate.cope.eclipse.ui.handlers;



import org.eclipse.jface.wizard.Wizard;


public class SurveyWizard extends Wizard {

  protected SurveyPage surveyPage;
  protected Page2 two;
  
  public String email = "test";
  public String surveyAnswers = "q1:1";

  public SurveyWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  @Override
  public void addPages() {
    surveyPage = new SurveyPage();
    two = new Page2();
    addPage(surveyPage);
    addPage(two);
  }

  @Override
  public boolean performFinish() {
    // Print the result to the console
//	  surveyAnswers = surveyPage.SurveyResults;
//	  email = surveyPage.emailAddress;
    return true;
  }
}
 
