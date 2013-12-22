package edu.oregonstate.cope.eclipse.ui.handlers;

import java.math.BigInteger;
import java.util.Random;

import org.eclipse.jface.wizard.Wizard;
import org.json.simple.JSONObject;

public class SurveyWizard extends Wizard {

	protected SurveyPage surveyPage;
	protected Page2 two;

	private JSONObject surveyAnswers;
	private String email;

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
		String email = (String) surveyAnswers.get("email");
		surveyAnswers.remove("email");
		
		this.email = getRandomEmailIfAbsent(email);
		
		return true;
	}

	@Override
	public boolean performCancel() {
		surveyAnswers = new JSONObject();
		this.email = getRandomEmailIfAbsent(null);
		
		return true;
	}
	
	private String getRandomEmailIfAbsent(String email) {
		if (email == null || email.isEmpty())
			return new BigInteger(96, new Random()).toString(32);
		else
			return email;
	}

	public String getSurveyResults() {
		return surveyAnswers.toJSONString();
	}
	
	public String getEmail(){
		return email;
	}
}
