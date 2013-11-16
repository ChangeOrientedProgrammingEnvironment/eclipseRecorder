package edu.oregonstate.cope.eclipse.ui.handlers;

import java.awt.Rectangle;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Color;

public class DemoSurveyDialog extends Dialog {

	public DemoSurveyDialog(Shell parentShell) {
	    super(parentShell);
	  }
	
	@Override
	  protected Control createDialogArea(Composite parent) {

		Composite content = (Composite) super.createDialogArea(parent);
        content.setLayout(new FillLayout());

        ScrolledComposite sc = new ScrolledComposite(content, SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER);

        Composite composite = new Composite(sc, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.VERTICAL));

        Composite cl1 =  new Composite(composite,SWT.NONE);
        cl1.setLayout(new GridLayout());
	    Label l1 = new Label(cl1, SWT.BORDER);
	    //Color c1 = new Color(null, 50, 50, 200);
	    //label.setBackground(c1);
	    l1.setSize(300,30);
	    //l1.setLocation(100, 50);
	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
	    l1.setFont(font_bold);
	    l1.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
	    
	    
	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
	    Label l2 = new Label(cl1, SWT.BORDER);
	    l2.setSize(300,30);
	    //l2.setLocation(100, 50);
	    l2.setFont(font);
	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
        
        
        Composite q1 = new Composite(composite,SWT.NONE);
        q1.setLayout(new GridLayout());
      //Question 1
	    Label l3 = new Label(q1, SWT.BORDER);
	    //l3.setFont(font_bold_small);
	    l3.setText("What is the extent of your programming experience?");
	    Button q1b1 = new Button(q1,SWT.RADIO);
	    q1b1.setText("0 - 2 years");
	    Button q1b2 = new Button(q1,SWT.RADIO);
	    q1b2.setText("2 - 5 years");
	    Button q1b3 = new Button(q1,SWT.RADIO);
	    q1b3.setText("5 - 10 years");
	    Button q1b4 = new Button(q1,SWT.RADIO);
	    q1b4.setText("10 - 15 years");
	    Button q1b5 = new Button(q1,SWT.RADIO);
	    q1b5.setText("15 - 20 years");
	    Button q1b6 = new Button(q1,SWT.RADIO);
	    q1b6.setText("More than 20 years");
	    
	    Composite q2 = new Composite(composite,SWT.NONE);
        q2.setLayout(new GridLayout());
      //Question 2
	    Label q2l1 = new Label(q2, SWT.BORDER);
	    //q2l1.setFont(font_bold_small);
	    q2l1.setText("What is the extent of your programming experience with Java?");
	    Button q2b1 = new Button(q2,SWT.RADIO);
	    q2b1.setText("0 - 2 years");
	    Button q2b2 = new Button(q2,SWT.RADIO);
	    q2b2.setText("2 - 5 years");
	    Button q2b3 = new Button(q2,SWT.RADIO);
	    q2b3.setText("5 - 10 years");
	    Button q2b4 = new Button(q2,SWT.RADIO);
	    q2b4.setText("10 - 15 years");
	    Button q2b5 = new Button(q2,SWT.RADIO);
	    q2b5.setText("15 - 20 years");
	    Button q2b6 = new Button(q2,SWT.RADIO);
	    q2b6.setText("More than 20 years");
	    
	    
	    Composite q3 = new Composite(composite,SWT.NONE);
        q3.setLayout(new GridLayout());
	    //Question 3
	    Label q3l1 = new Label(q3, SWT.BORDER);
	    //q3l1.setFont(font_bold_small);
	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
	    Button q3b1 = new Button(q3,SWT.RADIO);
	    q3b1.setText("0 - 2 years");
	    Button q3b2 = new Button(q3,SWT.RADIO);
	    q3b2.setText("2 - 5 years");
	    Button q3b3 = new Button(q3,SWT.RADIO);
	    q3b3.setText("5 - 10 years");
	    Button q3b4 = new Button(q3,SWT.RADIO);
	    q3b4.setText("10 - 15 years");

	    Composite q4 = new Composite(composite,SWT.NONE);
        q4.setLayout(new GridLayout());   
	    //Question 4
	    Label q4l1 = new Label(q4, SWT.BORDER);
	    //q4l1.setFont(font_bold_small);
	    q4l1.setText("Select the project type you spend most of your time on");
	    Button q4b1 = new Button(q4,SWT.RADIO);
	    q4b1.setText("Proprietary software Project");
	    Button q4b2 = new Button(q4,SWT.RADIO);
	    q4b2.setText("Open Source Project");
	    Button q4b3 = new Button(q4,SWT.RADIO);
	    q4b3.setText("Research Project");
	    Button q4b4 = new Button(q4,SWT.RADIO);
	    q4b4.setText("Personal/Class Project");
	    Button q4b5 = new Button(q4,SWT.RADIO);
	    q4b5.setText("Other: ");
	    
	    Composite q5 = new Composite(composite,SWT.NONE);
        q5.setLayout(new GridLayout());
	    //Question 5
	    Label q5l1 = new Label(q5, SWT.BORDER);
	   // q5l1.setFont(font_bold_small);
	    q5l1.setText("What is the extent of your experience using TDD?");
	    Button q5b1 = new Button(q5,SWT.RADIO);
	    q5b1.setText("0 - 2 years");
	    Button q5b2 = new Button(q5,SWT.RADIO);
	    q5b2.setText("2 - 5 years");
	    Button q5b3 = new Button(q5,SWT.RADIO);
	    q5b3.setText("5 - 10 years");
	    Button q5b4 = new Button(q5,SWT.RADIO);
	    q5b4.setText("10 - 15 years");
	    
	    Composite q6 = new Composite(composite,SWT.NONE);
        q6.setLayout(new GridLayout());
	    //Question 6
	    Label q6l1 = new Label(q6, SWT.BORDER);
	   // q6l1.setFont(font_bold_small);
	    q6l1.setText("How often does your code development follow TDD practices?");
	    Button q6b1 = new Button(q6,SWT.RADIO);
	    q6b1.setText("Always");
	    Button q6b2 = new Button(q6,SWT.RADIO);
	    q6b2.setText("Very Often");
	    Button q6b3 = new Button(q6,SWT.RADIO);
	    q6b3.setText("Sometimes");
	    Button q6b4 = new Button(q6,SWT.RADIO);
	    q6b4.setText("Rarely");
	    Button q6b5 = new Button(q6,SWT.RADIO);
	    q6b5.setText("Never");
	    
	    Composite q7 = new Composite(composite,SWT.NONE);
        q7.setLayout(new GridLayout());
	    //Question 7
	    Label q7l1 = new Label(q7, SWT.BORDER);
	   // q7l1.setFont(font_bold_small);
	    q7l1.setText("What is your gender?");
	    Button q7b1 = new Button(q7,SWT.RADIO);
	    q7b1.setText("Male");
	    Button q7b2 = new Button(q7,SWT.RADIO);
	    q7b2.setText("Female");
	    Button q7b3 = new Button(q7,SWT.RADIO);
	    q7b3.setText("Decline to State");
	    
	    
	    Composite q8 = new Composite(composite,SWT.NONE);
        q8.setLayout(new GridLayout());   
	    //Question 8
	    Label q8l1 = new Label(q8, SWT.BORDER);
	    //q8l1.setFont(font_bold_small);
	    q8l1.setText("What is your current age?");
	    Button q8b1 = new Button(q8,SWT.RADIO);
	    q8b1.setText("less than 18");
	    Button q8b2 = new Button(q8,SWT.RADIO);
	    q8b2.setText("18-25");
	    Button q8b3 = new Button(q8,SWT.RADIO);
	    q8b3.setText("26-29");
	    Button q8b4 = new Button(q8,SWT.RADIO);
	    q8b4.setText("30-39");
	    Button q8b5 = new Button(q8,SWT.RADIO);
	    q8b5.setText("40-49");
	    Button q8b6 = new Button(q8,SWT.RADIO);
	    q8b6.setText("50 or older");

	    Composite cl2 =  new Composite(composite,SWT.NONE);
        cl2.setLayout(new GridLayout());
	    Label l4 = new Label(cl2, SWT.BORDER);
	    l4.setSize(300,30);
	    //l4.setFont(font_bold);
	    l4.setText("If you choose to give us your email, we will send you a personalized version of the report\n"
	    		+ " so you can see how your practices compare to the rest of the anonymous participants.\n "
	    		+ "We know that spam is annoying, and we promise not to give out your email to anyone.");
	    
	    
	    

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
	  protected Point getInitialSize() {
	    return new Point(600, 600);
	  }
	  
	    @Override
	    protected boolean isResizable() {
	        return true; // Allow the user to change the dialog size!
	    }


}
