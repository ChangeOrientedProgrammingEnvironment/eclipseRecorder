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

        sc.setContent(composite);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return parent; 
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 1;
//
//        // set the minimum width and height of the scrolled content - method 2
//        final ScrolledComposite sc2 = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FILL);
//        sc2.setExpandHorizontal(true);
//        sc2.setExpandVertical(true);
//        final Composite container = new Composite(sc2, SWT.NONE);
//        sc2.setContent(container);
//        layout = new GridLayout();
//        layout.numColumns = 1;
//        container.setLayout(layout);
//        
//        //sc2.setSize(getInitialSize());
//        
//       //Question 1
//	    Label l3 = new Label(container, SWT.BORDER);
//	    //l3.setFont(font_bold_small);
//	    l3.setText("What is the extent of your programming experience?");
//	    Button q1b1 = new Button(container,SWT.RADIO);
//	    q1b1.setText("0 - 2 years");
//	    Button q1b2 = new Button(container,SWT.RADIO);
//	    q1b2.setText("2 - 5 years");
//	    Button q1b3 = new Button(container,SWT.RADIO);
//	    q1b3.setText("5 - 10 years");
//	    Button q1b4 = new Button(container,SWT.RADIO);
//	    q1b4.setText("10 - 15 years");
//	    Button q1b5 = new Button(container,SWT.RADIO);
//	    q1b5.setText("15 - 20 years");
//	    Button q1b6 = new Button(container,SWT.RADIO);
//	    q1b6.setText("More than 20 years");
//	    
//	  //Question 2
//	    Label q2l1 = new Label(container, SWT.BORDER);
//	    //q2l1.setFont(font_bold_small);
//	    q2l1.setText("What is the extent of your programming experience with Java?");
//	    Button q2b1 = new Button(container,SWT.RADIO);
//	    q2b1.setText("0 - 2 years");
//	    Button q2b2 = new Button(container,SWT.RADIO);
//	    q2b2.setText("2 - 5 years");
//	    Button q2b3 = new Button(container,SWT.RADIO);
//	    q2b3.setText("5 - 10 years");
//	    Button q2b4 = new Button(container,SWT.RADIO);
//	    q2b4.setText("10 - 15 years");
//	    Button q2b5 = new Button(container,SWT.RADIO);
//	    q2b5.setText("15 - 20 years");
//	    Button q2b6 = new Button(container,SWT.RADIO);
//	    q2b6.setText("More than 20 years");
//	    
//	    //Question 3
//	    Label q3l1 = new Label(container, SWT.BORDER);
//	    //q3l1.setFont(font_bold_small);
//	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
//	    Button q3b1 = new Button(container,SWT.RADIO);
//	    q3b1.setText("0 - 2 years");
//	    Button q3b2 = new Button(container,SWT.RADIO);
//	    q3b2.setText("2 - 5 years");
//	    Button q3b3 = new Button(container,SWT.RADIO);
//	    q3b3.setText("5 - 10 years");
//	    Button q3b4 = new Button(container,SWT.RADIO);
//	    q3b4.setText("10 - 15 years");
//	    
//	    //Question 4
//	    Label q4l1 = new Label(container, SWT.BORDER);
//	    //q4l1.setFont(font_bold_small);
//	    q4l1.setText("Select the project type you spend most of your time on");
//	    Button q4b1 = new Button(container,SWT.RADIO);
//	    q4b1.setText("Proprietary software Project");
//	    Button q4b2 = new Button(container,SWT.RADIO);
//	    q4b2.setText("Open Source Project");
//	    Button q4b3 = new Button(container,SWT.RADIO);
//	    q4b3.setText("Research Project");
//	    Button q4b4 = new Button(container,SWT.RADIO);
//	    q4b4.setText("Personal/Class Project");
//	    Button q4b5 = new Button(container,SWT.RADIO);
//	    q4b5.setText("Other: ");
//	    
//	    //Question 5
//	    Label q5l1 = new Label(container, SWT.BORDER);
//	   // q5l1.setFont(font_bold_small);
//	    q5l1.setText("What is the extent of your experience using TDD?");
//	    Button q5b1 = new Button(container,SWT.RADIO);
//	    q5b1.setText("0 - 2 years");
//	    Button q5b2 = new Button(container,SWT.RADIO);
//	    q5b2.setText("2 - 5 years");
//	    Button q5b3 = new Button(container,SWT.RADIO);
//	    q5b3.setText("5 - 10 years");
//	    Button q5b4 = new Button(container,SWT.RADIO);
//	    q5b4.setText("10 - 15 years");
//        
//        Button b2 = new Button (container, SWT.PUSH);
//        b2.setText("first button");
//        
//        
//        sc2.setContent(container);
//        sc2.setExpandVertical(true);
//        sc2.setExpandHorizontal(true);
//        //sc2.setMinSize(container.computeSize(SWT.DEFAULT,SWT.DEFAULT));
//        sc2.setMinWidth(100);
//        sc2.setMinHeight(100);
//        sc2.layout();
//        
//        		
//        
//       // sc2.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//
//       // sc2.setExpandVertical(true);
//       // sc2.setExpandHorizontal(true);
//
//        
//        //sc2.setMaxSize(100,100);
//      //container.layout();
//      // sc2.layout();
//        
//        Button add = new Button (parent, SWT.PUSH);
//        add.setText("add children");
//        final int[] index = new int[]{0};
//        add.addListener(SWT.Selection, new Listener() {
//            public void handleEvent(Event e) {
//                index[0]++;
//                //Button button = new Button(container, SWT.PUSH);
//                //button.setText("button "+index[0]);
//                
//                //Question 1
//        	    Label l3 = new Label(container, SWT.BORDER);
//        	    //l3.setFont(font_bold_small);
//        	    l3.setText("What is the extent of your programming experience?");
//        	    Button q1b1 = new Button(container,SWT.RADIO);
//        	    q1b1.setText("0 - 2 years");
//        	    Button q1b2 = new Button(container,SWT.RADIO);
//        	    q1b2.setText("2 - 5 years");
//        	    Button q1b3 = new Button(container,SWT.RADIO);
//        	    q1b3.setText("5 - 10 years");
//        	    Button q1b4 = new Button(container,SWT.RADIO);
//        	    q1b4.setText("10 - 15 years");
//        	    Button q1b5 = new Button(container,SWT.RADIO);
//        	    q1b5.setText("15 - 20 years");
//        	    Button q1b6 = new Button(container,SWT.RADIO);
//        	    q1b6.setText("More than 20 years");
//        	    
//        	  //Question 2
//        	    Label q2l1 = new Label(container, SWT.BORDER);
//        	    //q2l1.setFont(font_bold_small);
//        	    q2l1.setText("What is the extent of your programming experience with Java?");
//        	    Button q2b1 = new Button(container,SWT.RADIO);
//        	    q2b1.setText("0 - 2 years");
//        	    Button q2b2 = new Button(container,SWT.RADIO);
//        	    q2b2.setText("2 - 5 years");
//        	    Button q2b3 = new Button(container,SWT.RADIO);
//        	    q2b3.setText("5 - 10 years");
//        	    Button q2b4 = new Button(container,SWT.RADIO);
//        	    q2b4.setText("10 - 15 years");
//        	    Button q2b5 = new Button(container,SWT.RADIO);
//        	    q2b5.setText("15 - 20 years");
//        	    Button q2b6 = new Button(container,SWT.RADIO);
//        	    q2b6.setText("More than 20 years");
//        	    
//        	    //Question 3
//        	    Label q3l1 = new Label(container, SWT.BORDER);
//        	    //q3l1.setFont(font_bold_small);
//        	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
//        	    Button q3b1 = new Button(container,SWT.RADIO);
//        	    q3b1.setText("0 - 2 years");
//        	    Button q3b2 = new Button(container,SWT.RADIO);
//        	    q3b2.setText("2 - 5 years");
//        	    Button q3b3 = new Button(container,SWT.RADIO);
//        	    q3b3.setText("5 - 10 years");
//        	    Button q3b4 = new Button(container,SWT.RADIO);
//        	    q3b4.setText("10 - 15 years");
//        	    
//        	    //Question 4
//        	    Label q4l1 = new Label(container, SWT.BORDER);
//        	    //q4l1.setFont(font_bold_small);
//        	    q4l1.setText("Select the project type you spend most of your time on");
//        	    Button q4b1 = new Button(container,SWT.RADIO);
//        	    q4b1.setText("Proprietary software Project");
//        	    Button q4b2 = new Button(container,SWT.RADIO);
//        	    q4b2.setText("Open Source Project");
//        	    Button q4b3 = new Button(container,SWT.RADIO);
//        	    q4b3.setText("Research Project");
//        	    Button q4b4 = new Button(container,SWT.RADIO);
//        	    q4b4.setText("Personal/Class Project");
//        	    Button q4b5 = new Button(container,SWT.RADIO);
//        	    q4b5.setText("Other: ");
//        	    
//        	    //Question 5
//        	    Label q5l1 = new Label(container, SWT.BORDER);
//        	   // q5l1.setFont(font_bold_small);
//        	    q5l1.setText("What is the extent of your experience using TDD?");
//        	    Button q5b1 = new Button(container,SWT.RADIO);
//        	    q5b1.setText("0 - 2 years");
//        	    Button q5b2 = new Button(container,SWT.RADIO);
//        	    q5b2.setText("2 - 5 years");
//        	    Button q5b3 = new Button(container,SWT.RADIO);
//        	    q5b3.setText("5 - 10 years");
//        	    Button q5b4 = new Button(container,SWT.RADIO);
//        	    q5b4.setText("10 - 15 years");
//        	    
//        	    
//                sc2.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//                container.layout();
//            }
//        });
//
//      return sc2;
//        
//		
		
//		// this button is always 400 x 400. Scrollbars appear if the window is resized to be
//	    // too small to show part of the button
//	    ScrolledComposite c1 = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//	    Button b1 = new Button(c1, SWT.PUSH);
//	    b1.setText("fixed size button");
//	    b1.setSize(400, 400);
//	    c1.setContent(b1);
//
//	    // this button has a minimum size of 400 x 400. If the window is resized to be big
//	    // enough to show more than 400 x 400, the button will grow in size. If the window
//	    // is made too small to show 400 x 400, scrollbars will appear.
//	    ScrolledComposite c2 = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//	    Button b2 = new Button(c2, SWT.PUSH);
//	    b2.setText("expanding button");
//	    c2.setContent(b2);
//	    c2.setExpandHorizontal(true);
//	    c2.setExpandVertical(true);
//	    c2.setMinSize(400, 400);
//
//		return c2;
		

//		  GridLayout layout = new GridLayout();
//          layout.numColumns = 4;
//
//          // set the minimum width and height of the scrolled content - method 2
//          final ScrolledComposite sc2 = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//          sc2.setExpandHorizontal(true);
//          sc2.setExpandVertical(true);
//          final Composite c2 = new Composite(sc2, SWT.NONE);
//          sc2.setContent(c2);
//          //layout = new GridLayout();
//          //layout.numColumns = 7;
//          //c2.setLayout(layout);
//          
//          //sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//
//          Label label = new Label(c2, SWT.BORDER);
//  	    //Color c1 = new Color(null, 50, 50, 200);
//  	    //label.setBackground(c1);
//  	    label.setSize(300,30);
//  	    label.setLocation(100, 50);
//  	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
//  	    label.setFont(font_bold);
//  	    label.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
//        
//	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
//	    Label l2 = new Label(c2, SWT.BORDER);
//	    l2.setSize(300,30);
//	    l2.setLocation(100, 50);
//	    l2.setFont(font);
//	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
//	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
//	    
//	    sc2.setMinSize(100,100);
  	    
  	    
//          Button add = new Button (parent, SWT.PUSH);
//          add.setText("add children");
//          final int[] index = new int[]{0};
//          add.addListener(SWT.Selection, new Listener() {
//              public void handleEvent(Event e) {
//                  index[0]++;
//                  Button button = new Button(c2, SWT.PUSH);
//                  button.setText("button "+index[0]);
//                  sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//                  c2.layout();
//              }
//          });
          
        //  return sc2;
		
		
//		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		sc.setExpandHorizontal(true);
//        sc.setExpandVertical(true);
//        Composite container = new Composite(sc, SWT.NONE);
//        sc.setContent(container);
//        
//	    Label label = new Label(container, SWT.BORDER);
//	    //Color c1 = new Color(null, 50, 50, 200);
//	    //label.setBackground(c1);
//	    label.setSize(300,30);
//	    label.setLocation(100, 50);
//	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
//	    label.setFont(font_bold);
//	    label.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
//	    
//	    
//	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
//	    Label l2 = new Label(container, SWT.BORDER);
//	    l2.setSize(300,30);
//	    l2.setLocation(100, 50);
//	    l2.setFont(font);
//	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
//	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
//	    
//	    
//	    //Question 1
//	    Label l3 = new Label(container, SWT.BORDER);
//	    l3.setFont(font_bold_small);
//	    l3.setText("What is the extent of your programming experience?");
//	    Button q1b1 = new Button(container,SWT.RADIO);
//	    q1b1.setText("0 - 2 years");
//	    Button q1b2 = new Button(container,SWT.RADIO);
//	    q1b2.setText("2 - 5 years");
//	    Button q1b3 = new Button(container,SWT.RADIO);
//	    q1b3.setText("5 - 10 years");
//	    Button q1b4 = new Button(container,SWT.RADIO);
//	    q1b4.setText("10 - 15 years");
//	    Button q1b5 = new Button(container,SWT.RADIO);
//	    q1b5.setText("15 - 20 years");
//	    Button q1b6 = new Button(container,SWT.RADIO);
//	    q1b6.setText("More than 20 years");
//	    
//        sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//        return sc;
        
		
		//ScrolledComposite scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		
//	    Composite container = (Composite) super.createDialogArea(parent);
//	    
//  
//	    Label label = new Label(container, SWT.BORDER);
//	    //Color c1 = new Color(null, 50, 50, 200);
//	    //label.setBackground(c1);
//	    label.setSize(300,30);
//	    label.setLocation(100, 50);
//	    Font font_bold = new Font(null, "Helvetica", 14, SWT.BOLD);
//	    label.setFont(font_bold);
//	    label.setText("Thanks you for participating in the study titled: Understanding TDD Practices.");
//	    
//	    
//	    Font font = new Font(null, "Helvetica", 12, SWT.NORMAL);
//	    Label l2 = new Label(container, SWT.BORDER);
//	    l2.setSize(300,30);
//	    l2.setLocation(100, 50);
//	    l2.setFont(font);
//	    l2.setText("Please answer just a few demographic questions before you begin using the plugin");
//	    Font font_bold_small = new Font(null, "Helvetica", 12, SWT.BOLD);
//	    
//	    
//	    //Question 1
//	    Label l3 = new Label(container, SWT.BORDER);
//	    l3.setFont(font_bold_small);
//	    l3.setText("What is the extent of your programming experience?");
//	    Button q1b1 = new Button(container,SWT.RADIO);
//	    q1b1.setText("0 - 2 years");
//	    Button q1b2 = new Button(container,SWT.RADIO);
//	    q1b2.setText("2 - 5 years");
//	    Button q1b3 = new Button(container,SWT.RADIO);
//	    q1b3.setText("5 - 10 years");
//	    Button q1b4 = new Button(container,SWT.RADIO);
//	    q1b4.setText("10 - 15 years");
//	    Button q1b5 = new Button(container,SWT.RADIO);
//	    q1b5.setText("15 - 20 years");
//	    Button q1b6 = new Button(container,SWT.RADIO);
//	    q1b6.setText("More than 20 years");
//
//	    //Question 2
//	    Label q2l1 = new Label(container, SWT.BORDER);
//	    q2l1.setFont(font_bold_small);
//	    q2l1.setText("What is the extent of your programming experience with Java?");
//	    Button q2b1 = new Button(container,SWT.RADIO);
//	    q2b1.setText("0 - 2 years");
//	    Button q2b2 = new Button(container,SWT.RADIO);
//	    q2b2.setText("2 - 5 years");
//	    Button q2b3 = new Button(container,SWT.RADIO);
//	    q2b3.setText("5 - 10 years");
//	    Button q2b4 = new Button(container,SWT.RADIO);
//	    q2b4.setText("10 - 15 years");
//	    Button q2b5 = new Button(container,SWT.RADIO);
//	    q2b5.setText("15 - 20 years");
//	    Button q2b6 = new Button(container,SWT.RADIO);
//	    q2b6.setText("More than 20 years");
//	    
//	    //Question 3
//	    Label q3l1 = new Label(container, SWT.BORDER);
//	    q3l1.setFont(font_bold_small);
//	    q3l1.setText("What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?");
//	    Button q3b1 = new Button(container,SWT.RADIO);
//	    q3b1.setText("0 - 2 years");
//	    Button q3b2 = new Button(container,SWT.RADIO);
//	    q3b2.setText("2 - 5 years");
//	    Button q3b3 = new Button(container,SWT.RADIO);
//	    q3b3.setText("5 - 10 years");
//	    Button q3b4 = new Button(container,SWT.RADIO);
//	    q3b4.setText("10 - 15 years");
//	    
//	    //Question 4
//	    Label q4l1 = new Label(container, SWT.BORDER);
//	    q4l1.setFont(font_bold_small);
//	    q4l1.setText("Select the project type you spend most of your time on");
//	    Button q4b1 = new Button(container,SWT.RADIO);
//	    q4b1.setText("Proprietary software Project");
//	    Button q4b2 = new Button(container,SWT.RADIO);
//	    q4b2.setText("Open Source Project");
//	    Button q4b3 = new Button(container,SWT.RADIO);
//	    q4b3.setText("Research Project");
//	    Button q4b4 = new Button(container,SWT.RADIO);
//	    q4b4.setText("Personal/Class Project");
//	    Button q4b5 = new Button(container,SWT.RADIO);
//	    q4b5.setText("Other: ");
//	    
//	    //Question 5
//	    Label q5l1 = new Label(container, SWT.BORDER);
//	    q5l1.setFont(font_bold_small);
//	    q5l1.setText("What is the extent of your experience using TDD?");
//	    Button q5b1 = new Button(container,SWT.RADIO);
//	    q5b1.setText("0 - 2 years");
//	    Button q5b2 = new Button(container,SWT.RADIO);
//	    q5b2.setText("2 - 5 years");
//	    Button q5b3 = new Button(container,SWT.RADIO);
//	    q5b3.setText("5 - 10 years");
//	    Button q5b4 = new Button(container,SWT.RADIO);
//	    q5b4.setText("10 - 15 years");
//
////	    scrollComposite.setContent(q5l1);
////	    scrollComposite.setContent(q5b2);
////	    scrollComposite.setContent(q5b3);
////	    scrollComposite.setContent(q5b4);
////	    
//	    
//	    Button button = new Button(container, SWT.PUSH);
//	    button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
//	        false));
//	    button.setText("Press me");
//	    button.addSelectionListener(new SelectionAdapter() {
//	      @Override
//	      public void widgetSelected(SelectionEvent e) {
//	        System.out.println("Pressed");
//	      }
//	    });
//
//	    
//
//	    //scrollComposite.setContent(container);
//	    return container;

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
