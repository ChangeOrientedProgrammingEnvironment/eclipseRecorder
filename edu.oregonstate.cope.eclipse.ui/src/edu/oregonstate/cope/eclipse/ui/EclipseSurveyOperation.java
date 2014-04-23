package edu.oregonstate.cope.eclipse.ui;

import org.eclipse.core.runtime.Platform;

import edu.oregonstate.cope.clientRecorder.installer.SurveyOperation;
import edu.oregonstate.cope.clientRecorder.installer.SurveyProvider;
import edu.oregonstate.cope.eclipse.ui.handlers.SurveyWizard;

public class EclipseSurveyOperation extends SurveyOperation {
	
	protected SurveyProvider runSurvey() {
		SurveyProvider sw;
		if (Platform.inDevelopmentMode())
			sw = SurveyWizard.takeFakeSurvey();
		else
			sw = SurveyWizard.takeRealSurvey();
		return sw;
	}
}
