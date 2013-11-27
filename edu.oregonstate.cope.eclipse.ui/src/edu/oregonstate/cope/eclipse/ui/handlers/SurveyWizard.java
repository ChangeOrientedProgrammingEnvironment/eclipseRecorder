package edu.oregonstate.cope.eclipse.ui.handlers;

import java.math.BigInteger;
import java.util.Random;

import org.eclipse.jface.wizard.Wizard;
import org.json.simple.JSONObject;

public class SurveyWizard extends Wizard {

	protected SurveyPage surveyPage;
	protected Page2 two;

	private JSONObject surveyAnswers;

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
		surveyAnswers = surveyPage.getSurveyResults();
		putRandomEmailIfAbsent();
		return true;
	}

	@Override
	public boolean performCancel() {
		surveyAnswers = new JSONObject();
		putRandomEmailIfAbsent();
		return true;
	}
	
	private void putRandomEmailIfAbsent() {
		String email = (String) surveyAnswers.get("email");
		if (email == null || email.isEmpty()) {
			email = new BigInteger(96, new Random()).toString(32);
			surveyAnswers.put("email", email);
		}
	}

	public String getSurveyResults() {
		return surveyAnswers.toJSONString();
	}
}
