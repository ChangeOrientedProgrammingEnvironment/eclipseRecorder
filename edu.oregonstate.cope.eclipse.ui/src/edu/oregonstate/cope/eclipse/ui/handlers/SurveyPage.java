package edu.oregonstate.cope.eclipse.ui.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SurveyPage extends WizardPage {

	/**
	 * This contains the button for all the questions. Since it is an array list, the order is preserved.
	 * So the first questions is at index 0, second at 1 and so on. The buttons are Radio Buttons, so only one
	 * will be selected at one time. Again, the order of the buttons corresponds to the order of the answers
	 * in the initial list.
	 */
	private List<List<Button>> allButtons;
	private Text emailInput;

	protected SurveyPage() {
		super("Survey");
		this.setDescription("Thank you for bla bla bla");
		this.setTitle("COPE survey");
	}

	@Override
	public void createControl(Composite parent) {
		Composite overall = new Composite(parent, SWT.NONE);
		addGridLayout(overall);

		ScrolledComposite scrolledComposite = new ScrolledComposite(overall, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		addGridLayout(scrolledComposite);

		Composite questions = new Composite(scrolledComposite, SWT.NONE);
		addGridLayout(questions);

		allButtons = new ArrayList<List<Button>>();

		allButtons.add(createQuestion(questions, "What is the extent of your programming experience?", Arrays.asList(new String[] { "0-2 years", "2-5 years", "5-10 years", "10-15 years", "15-20 years", "More than 20 years" })));
		allButtons.add(createQuestion(questions, "What is the extent of your programming experience with Java?", Arrays.asList(new String[] { "0-2 years", "2-5 years", "5-10 years", "10-15 years", "15-20 years", "More than 20 years" })));
		allButtons.add(createQuestion(questions, "What is the extent of your experience with Eclipse or IntelliJ IDEA IDE?", Arrays.asList(new String[] { "0-2 years", "2-5 years", "5-10 years", "10-15 years" })));
		allButtons.add(createQuestion(questions, "Select the project type you spend most of your time on", Arrays.asList(new String[] { "Proprietary Software Project", "Open Source Project", "Research Project", "Personal/Class project", "Other" })));
		allButtons.add(createQuestion(questions, "What is the extent of your experience using TDD?", Arrays.asList(new String[] { "0-2 years", "2-5 years", "5-10 years", "10-15 years" })));
		allButtons.add(createQuestion(questions, "How often does your code development follow TDD practices?", Arrays.asList(new String[] { "Always", "Very often", "Sometimes", "Rarely", "Nevers" })));
		allButtons.add(createQuestion(questions, "What is your gender", Arrays.asList(new String[] { "Male", "Female", "Decline to State" })));
		allButtons.add(createQuestion(questions, "What is your current age", Arrays.asList(new String[] { "less than 18", "18-25", "26-29", "30-39", "40-49", "50 or older" })));

		Composite emailComposite = new Composite(questions, SWT.NONE);
		emailComposite.setLayout(new GridLayout(1, false));
		Label emailQuestionText = new Label(emailComposite, SWT.NONE);
		emailQuestionText.setText("If you choose to give us your email, we will send you a personalized version of the report\n" 
				+ " so you can see how your practices compare to the rest of the anonymous participants.\n " 
				+ "We know that spam is annoying, and we promise not to give out your email to anyone.");
		emailInput = new Text(emailComposite, SWT.SINGLE | SWT.FILL | SWT.BORDER);

		scrolledComposite.setContent(questions);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(questions.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		setControl(overall);
		setPageComplete(false);
	}

	private void addGridLayout(Composite element) {
		element.setLayout(new GridLayout(1, false));
		element.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private List<Button> createQuestion(Composite parent, String question, List<String> answersList) {
		Composite questionComposite = new Composite(parent, SWT.NONE);
		questionComposite.setLayout(new GridLayout(1, false));
		Label questionText = new Label(questionComposite, SWT.NONE);
		questionText.setText(question);
		Composite answers = new Composite(questionComposite, SWT.NONE);
		answers.setLayout(new GridLayout(1, false));
		List<Button> answerButtons = new ArrayList<Button>();
		for (String s : answersList) {
			Button answer = new Button(answers, SWT.RADIO);
			answer.setText(s);
			answerButtons.add(answer);
			answer.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (isPageComplete())
						setPageComplete(true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		return answerButtons;
	}

	@Override
	public boolean isPageComplete() {
		for (List<Button> questionButtons : allButtons) {
			boolean enabled = false;
			for (Button button : questionButtons) {
				if (button.getSelection())
					enabled = true;
			}
			if (!enabled)
				return false;
		}
		return true;
	}
}
