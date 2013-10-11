package edu.oregonstate.cope.eclipse.listeners;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

/**
 * I listen to results.
 * 
 * @author Caius Brindescu
 *
 */
public class JUnitListener extends TestRunListener {

	@Override
	public void sessionFinished(ITestRunSession session) {
		System.out.println("Session " + session.getTestRunName()
				+ " has finished with the result "
				+ session.getTestResult(true));
	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		System.out.println("Test case " + testCaseElement.getTestMethodName()
				+ " has finished with the result "
				+ testCaseElement.getTestResult(true));
	}
}
