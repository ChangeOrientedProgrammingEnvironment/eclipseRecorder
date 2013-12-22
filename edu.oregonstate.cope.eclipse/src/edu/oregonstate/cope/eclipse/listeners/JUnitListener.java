package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import edu.oregonstate.cope.clientRecorder.ClientRecorder;
import edu.oregonstate.cope.eclipse.COPEPlugin;

/**
 * I listen to results.
 * 
 * @author Caius Brindescu
 * 
 */
public class JUnitListener extends TestRunListener {

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		double elapsedTimeInSeconds = testCaseElement.getElapsedTimeInSeconds();
		ClientRecorder clientRecorderInstance = COPEPlugin.getDefault()
				.getClientRecorder();
		clientRecorderInstance.recordTestRun(testCaseElement.getTestClassName()
				+ "." + testCaseElement.getTestMethodName(), testCaseElement
				.getTestResult(true).toString(), elapsedTimeInSeconds);
	}
}
