package edu.oregonstate.cope.eclipse.ui.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class DemoSurveyDialog extends Dialog {
	
	private Button[] q1Arr = new Button[6]; 
	private Button[] q2Arr = new Button[6]; 
	private Button[] q3Arr = new Button[4]; 
	private Button[] q4Arr = new Button[5]; 
	private Button[] q5Arr = new Button[4];
	private Button[] q6Arr = new Button[5]; 
	private Button[] q7Arr = new Button[3]; 
	private Button[] q8Arr = new Button[6]; 
	private Text email; 
	
	public DemoSurveyDialog(Shell parentShell) {
	    super(parentShell);
	  }
	
	@Override
	  protected Control createDialogArea(Composite parent) {

		Composite content = (Composite) super.createDialogArea(parent);
        content.setLayout(new FillLayout(SWT.VERTICAL));

        ScrolledComposite sc = new ScrolledComposite(content, SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER);

        Composite composite = new Composite(sc, SWT.NONE);
        composite.setLayout(new RowLayout(SWT.VERTICAL));

        Composite cl1 =  new Composite(composite,SWT.NONE);
        cl1.setLayout(new RowLayout(SWT.VERTICAL));
	    Label l1 = new Label(cl1, SWT.BORDER);
	    Color c1 = new Color(null, 50, 50, 200);
	    cl1.setBackground(c1);
	    cl1.setSize(300,10);
	    l1.setSize(300,10);
	    //l1.setLocation(100, 50);
	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
	    l1.setFont(font_bold);
	    l1.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
	    l1.setBackground(new Color(null, 155, 155, 0));
	    
	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
	    Label l2 = new Label(cl1, SWT.BORDER);
	    l2.setSize(300,30);
	    //l2.setLocation(100, 50);
	    l2.setFont(font);
	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
	    l2.setBackground(new Color(null, 0, 155, 0));
        

	    //Q1
	    Composite q1 = new Composite(composite,SWT.NONE);
        q1.setLayout(new FillLayout(SWT.VERTICAL));
	    Label l3 = new Label(q1, SWT.BORDER);
	    l3.setFont(font_bold_small);
	    l3.setText("What is the extent of your programming experience?");
	    q1Arr[0] = new Button(q1,SWT.RADIO);
	    q1Arr[0].setText("0 - 2 years");
	    q1Arr[1] = new Button(q1,SWT.RADIO);
	    q1Arr[1].setText("2 - 5 years");
	    q1Arr[2] = new Button(q1,SWT.RADIO);
	    q1Arr[2].setText("5 - 10 years");
	    q1Arr[3] = new Button(q1,SWT.RADIO);
	    q1Arr[3].setText("10 - 15 years");
	    q1Arr[4] = new Button(q1,SWT.RADIO);
	    q1Arr[4].setText("15 - 20 years");
	    q1Arr[5] = new Button(q1,SWT.RADIO);
	    q1Arr[5].setText("More than 20 years");
	    
	    //Q2
	    Composite q2 = new Composite(composite,SWT.NONE);
        q2.setLayout(new GridLayout());
	    Label q2l1 = new Label(q2, SWT.BORDER);
	    q2l1.setFont(font_bold_small);
	    q2l1.setText("What is the extent of your programming experience with Java?");
	    q2Arr[0] = new Button(q2,SWT.RADIO);
	    q2Arr[0].setText("0 - 2 years");
	    q2Arr[1] = new Button(q2,SWT.RADIO);
	    q2Arr[1].setText("2 - 5 years");
	    q2Arr[2] = new Button(q2,SWT.RADIO);
	    q2Arr[2].setText("5 - 10 years");
	    q2Arr[3] = new Button(q2,SWT.RADIO);
	    q2Arr[3].setText("10 - 15 years");
	    q2Arr[4] = new Button(q2,SWT.RADIO);
	    q2Arr[4].setText("15 - 20 years");
	    q2Arr[5] = new Button(q2,SWT.RADIO);
	    q2Arr[5].setText("More than 20 years");
	    
	    
	    //Q3
	    Composite q3 = new Composite(composite,SWT.NONE);
        q3.setLayout(new GridLayout());
	    Label q3l1 = new Label(q3, SWT.BORDER);
	    q3l1.setFont(font_bold_small);
	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
	    q3Arr[0] = new Button(q3,SWT.RADIO);
	    q3Arr[0].setText("0 - 2 years");
	    q3Arr[1] = new Button(q3,SWT.RADIO);
	    q3Arr[1].setText("2 - 5 years");
	    q3Arr[2] = new Button(q3,SWT.RADIO);
	    q3Arr[2].setText("5 - 10 years");
	    q3Arr[3] = new Button(q3,SWT.RADIO);
	    q3Arr[3].setText("10 - 15 years");

	    //Q4
	    Composite q4 = new Composite(composite,SWT.NONE);
        q4.setLayout(new GridLayout());   
	    Label q4l1 = new Label(q4, SWT.BORDER);
	    q4l1.setFont(font_bold_small);
	    q4l1.setText("Select the project type you spend most of your time on");
	    q4Arr[0] = new Button(q4,SWT.RADIO);
	    q4Arr[0].setText("Proprietary software Project");
	    q4Arr[1] = new Button(q4,SWT.RADIO);
	    q4Arr[1].setText("Open Source Project");
	    q4Arr[2] = new Button(q4,SWT.RADIO);
	    q4Arr[2].setText("Research Project");
	    q4Arr[3] = new Button(q4,SWT.RADIO);
	    q4Arr[3].setText("Personal/Class Project");
	    q4Arr[4] = new Button(q4,SWT.RADIO);
	    q4Arr[4].setText("Other: ");
	    
	    //Q5
	    Composite q5 = new Composite(composite,SWT.NONE);
        q5.setLayout(new GridLayout());
	    Label q5l1 = new Label(q5, SWT.BORDER);
	    q5l1.setFont(font_bold_small);
	    q5l1.setText("What is the extent of your experience using TDD?");
	    q5Arr[0] = new Button(q5,SWT.RADIO);
	    q5Arr[0].setText("0 - 2 years");
	    q5Arr[1] = new Button(q5,SWT.RADIO);
	    q5Arr[1].setText("2 - 5 years");
	    q5Arr[2] = new Button(q5,SWT.RADIO);
	    q5Arr[2].setText("5 - 10 years");
	    q5Arr[3] = new Button(q5,SWT.RADIO);
	    q5Arr[3].setText("10 - 15 years");
	    
	    //Q6
	    Composite q6 = new Composite(composite,SWT.NONE);
        q6.setLayout(new GridLayout());
	    Label q6l1 = new Label(q6, SWT.BORDER);
	    q6l1.setFont(font_bold_small);
	    q6l1.setText("How often does your code development follow TDD practices?");
	    q6Arr[0]= new Button(q6,SWT.RADIO);
	    q6Arr[0].setText("Always");
	    q6Arr[1] = new Button(q6,SWT.RADIO);
	    q6Arr[1].setText("Very Often");
	    q6Arr[2] = new Button(q6,SWT.RADIO);
	    q6Arr[2].setText("Sometimes");
	    q6Arr[3] = new Button(q6,SWT.RADIO);
	    q6Arr[3].setText("Rarely");
	    q6Arr[4] = new Button(q6,SWT.RADIO);
	    q6Arr[4].setText("Never");
	    
	    //Q7
	    Composite q7 = new Composite(composite,SWT.NONE);
        q7.setLayout(new GridLayout());
	    Label q7l1 = new Label(q7, SWT.BORDER);
	    q7l1.setFont(font_bold_small);
	    q7l1.setText("What is your gender?");
	    q7Arr[0] = new Button(q7,SWT.RADIO);
	    q7Arr[0].setText("Male");
	    q7Arr[1]= new Button(q7,SWT.RADIO);
	    q7Arr[1].setText("Female");
	    q7Arr[2] = new Button(q7,SWT.RADIO);
	    q7Arr[2].setText("Decline to State");
	    
	    //Q8
	    Composite q8 = new Composite(composite,SWT.NONE);
        q8.setLayout(new GridLayout());   
	    Label q8l1 = new Label(q8, SWT.BORDER);
	    q8l1.setFont(font_bold_small);
	    q8l1.setText("What is your current age?");
	    q8Arr[0]= new Button(q8,SWT.RADIO);
	    q8Arr[0].setText("less than 18");
	    q8Arr[1] = new Button(q8,SWT.RADIO);
	    q8Arr[1].setText("18-25");
	    q8Arr[2] = new Button(q8,SWT.RADIO);
	    q8Arr[2].setText("26-29");
	    q8Arr[3] = new Button(q8,SWT.RADIO);
	    q8Arr[3].setText("30-39");
	    q8Arr[4] = new Button(q8,SWT.RADIO);
	    q8Arr[4].setText("40-49");
	    q8Arr[5] = new Button(q8,SWT.RADIO);
	    q8Arr[5].setText("50 or older");

	    Composite cl2 =  new Composite(composite,SWT.NONE);
        cl2.setLayout(new GridLayout());
	    Label l4 = new Label(cl2, SWT.BORDER);
	    l4.setSize(300,30);
	    //l4.setFont(font_bold);
	    l4.setText("If you choose to give us your email, we will send you a personalized version of the report\n"
	    		+ " so you can see how your practices compare to the rest of the anonymous participants.\n "
	    		+ "We know that spam is annoying, and we promise not to give out your email to anyone.");
	    
	    Composite cl2email =  new Composite(cl2,SWT.NONE);
	    cl2email.setLayout(new GridLayout());
	    email = new Text (cl2email, SWT.SINGLE| SWT.BORDER | SWT.FILL);
		
	    
	    
        sc.setContent(composite);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return parent; 

	  }

	  // overriding this methods allows you to set the
	  // title of the custom dialog
	  @Override
	  protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("Selection dialog");
	  }
	  
	  @Override
	  protected void okPressed(){
		  System.out.println("OK PRESSED");
		  int[] qAnswers = new int[8];
		  for(int i = 0; i < qAnswers.length; i++){
			  qAnswers[i]=-1;
		  }
		  
		  for(int i = 0; i < q1Arr.length; i++){
			  if(q1Arr[i].getSelection()==true){
				  qAnswers[0] = i;
			  }
		  }
		  for(int i = 0; i < q2Arr.length; i++){
			  if(q2Arr[i].getSelection()==true){
				  qAnswers[1] = i;
			  }
		  }
		  for(int i = 0; i < q3Arr.length; i++){
			  if(q3Arr[i].getSelection()==true){
				  qAnswers[2] = i;
			  }
		  }
		  for(int i = 0; i < q4Arr.length; i++){
			  if(q4Arr[i].getSelection()==true){
				  qAnswers[3] = i;
			  }
		  }
		  for(int i = 0; i < q5Arr.length; i++){
			  if(q5Arr[i].getSelection()==true){
				  qAnswers[4] = i;
			  }
		  }
		  for(int i = 0; i < q6Arr.length; i++){
			  if(q6Arr[i].getSelection()==true){
				  qAnswers[5] = i;
			  }
		  }
		  for(int i = 0; i < q7Arr.length; i++){
			  if(q7Arr[i].getSelection()==true){
				  qAnswers[6] = i;
			  }
		  }
		  for(int i = 0; i < q8Arr.length; i++){
			  if(q8Arr[i].getSelection()==true){
				  qAnswers[7] = i;
			  }
		  }
		 
		  for(int i = 0; i < qAnswers.length; i++){
			  System.out.println("Question Number: "+i+" answer:"+qAnswers[i]);
		  }
		  System.out.println("email:" +email.getText());
		  
//		  SurveyFileProvider sfp = new SurveyFileProvider();
//		  sfp.writeToCurrentFile("TEST FILE WRITE");
		  
	  }

	  @Override
	  protected Point getInitialSize() {
	    return new Point(600, 600);
	  }
	  
	    @Override
	    protected boolean isResizable() {
	        return true; // Allow the user to change the dialog size!
	    }


}
