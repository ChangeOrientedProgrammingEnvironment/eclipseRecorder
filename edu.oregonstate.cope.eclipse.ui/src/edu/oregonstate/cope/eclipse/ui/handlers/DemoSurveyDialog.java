package edu.oregonstate.cope.eclipse.ui.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;

public class DemoSurveyDialog extends Dialog {

	public DemoSurveyDialog(Shell parentShell) {
	    super(parentShell);
	  }
	
	@Override
	  protected Control createDialogArea(Composite parent) {
	    Composite container = (Composite) super.createDialogArea(parent);
	    
  
	    Label label = new Label(container, SWT.BORDER);
	    //Color c1 = new Color(null, 50, 50, 200);
	    //label.setBackground(c1);
	    label.setSize(300,30);
	    label.setLocation(100, 50);
	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
	    label.setFont(font_bold);
	    label.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
	    
	    
	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
	    Label l2 = new Label(container, SWT.BORDER);
	    l2.setSize(300,30);
	    l2.setLocation(100, 50);
	    l2.setFont(font);
	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
	    
	    
	    //Question 1
	    Label l3 = new Label(container, SWT.BORDER);
	    l3.setFont(font_bold_small);
	    l3.setText("What is the extent of your programming experience?");
	    Button q1b1 = new Button(container,SWT.RADIO);
	    q1b1.setText("0 - 2 years");
	    Button q1b2 = new Button(container,SWT.RADIO);
	    q1b2.setText("2 - 5 years");
	    Button q1b3 = new Button(container,SWT.RADIO);
	    q1b3.setText("5 - 10 years");
	    Button q1b4 = new Button(container,SWT.RADIO);
	    q1b4.setText("10 - 15 years");
	    Button q1b5 = new Button(container,SWT.RADIO);
	    q1b5.setText("15 - 20 years");
	    Button q1b6 = new Button(container,SWT.RADIO);
	    q1b6.setText("More than 20 years");

	    //Question 2
	    Label q2l1 = new Label(container, SWT.BORDER);
	    q2l1.setFont(font_bold_small);
	    q2l1.setText("What is the extent of your programming experience with Java?");
	    Button q2b1 = new Button(container,SWT.RADIO);
	    q2b1.setText("0 - 2 years");
	    Button q2b2 = new Button(container,SWT.RADIO);
	    q2b2.setText("2 - 5 years");
	    Button q2b3 = new Button(container,SWT.RADIO);
	    q2b3.setText("5 - 10 years");
	    Button q2b4 = new Button(container,SWT.RADIO);
	    q2b4.setText("10 - 15 years");
	    Button q2b5 = new Button(container,SWT.RADIO);
	    q2b5.setText("15 - 20 years");
	    Button q2b6 = new Button(container,SWT.RADIO);
	    q2b6.setText("More than 20 years");
	    
	    //Question 3
	    Label q3l1 = new Label(container, SWT.BORDER);
	    q3l1.setFont(font_bold_small);
	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
	    Button q3b1 = new Button(container,SWT.RADIO);
	    q3b1.setText("0 - 2 years");
	    Button q3b2 = new Button(container,SWT.RADIO);
	    q3b2.setText("2 - 5 years");
	    Button q3b3 = new Button(container,SWT.RADIO);
	    q3b3.setText("5 - 10 years");
	    Button q3b4 = new Button(container,SWT.RADIO);
	    q3b4.setText("10 - 15 years");
	    
	    //Question 4
	    Label q4l1 = new Label(container, SWT.BORDER);
	    q4l1.setFont(font_bold_small);
	    q4l1.setText("Select the project type you spend most of your time on");
	    Button q4b1 = new Button(container,SWT.RADIO);
	    q4b1.setText("Proprietary software Project");
	    Button q4b2 = new Button(container,SWT.RADIO);
	    q4b2.setText("Open Source Project");
	    Button q4b3 = new Button(container,SWT.RADIO);
	    q4b3.setText("Research Project");
	    Button q4b4 = new Button(container,SWT.RADIO);
	    q4b4.setText("Personal/Class Project");
	    Button q4b5 = new Button(container,SWT.RADIO);
	    q4b5.setText("Other: ");
	    
	    //Question 5
	    Label q5l1 = new Label(container, SWT.BORDER);
	    q5l1.setFont(font_bold_small);
	    q5l1.setText("What is the extent of your experience using TDD?");
	    Button q5b1 = new Button(container,SWT.RADIO);
	    q5b1.setText("0 - 2 years");
	    Button q5b2 = new Button(container,SWT.RADIO);
	    q5b2.setText("2 - 5 years");
	    Button q5b3 = new Button(container,SWT.RADIO);
	    q5b3.setText("5 - 10 years");
	    Button q5b4 = new Button(container,SWT.RADIO);
	    q5b4.setText("10 - 15 years");

	    
	    Button button = new Button(container, SWT.PUSH);
	    button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
	        false));
	    button.setText("Press me");
	    button.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println("Pressed");
	      }
	    });

	    

	    
	    return container;
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
	    return new Point(650, 600);
	  }


}
