package edu.oregonstate.cope.eclipse.ui.handlers;



import org.eclipse.jface.wizard.Wizard;


public class SurveyWizard extends Wizard {

  protected Page1 one;
  protected Page2 two;

  public SurveyWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  @Override
  public void addPages() {
    one = new Page1();
    two = new Page2();
    addPage(one);
    addPage(two);
  }

  @Override
  public boolean performFinish() {
    // Print the result to the console
    System.out.println(one.getText1());
    System.out.println(two.getText1());

    return true;
  }
}
 