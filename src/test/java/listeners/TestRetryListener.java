package listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;
import core.Driver;
import utils.CommonUtils;
import utils.DriverUtils;
import utils.Report;

/**
 * Retry Listener for every test class.
 * @author palkrish
 *
 */
public class TestRetryListener implements ITestListener,IRetryAnalyzer,IAnnotationTransformer { 
	
	int retryCount = 1;
	int maxRetryCount = 3;
	String NO_RETRY_ON_ERROR_1 = "AssertionFailedError";
	String NO_RETRY_ON_ERROR_2 = "AssertionError";
	
	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();
		if (retry == null) {
			testannotation.setRetryAnalyzer(TestRetryListener.class); 
		}
	}
	
	@Override
	public void onTestStart(ITestResult result) {
		Reporter.setCurrentTestResult(result);
		Reporter.log("TEST : " + result.getName() + " STARTED.", true);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		Reporter.setCurrentTestResult(result);
		Reporter.log("TEST : " + result.getName() + " PASSED.", true);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		Reporter.setCurrentTestResult(result);
		Reporter.log("TEST : " + result.getName() + " FAILED.", true);
		String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
		if (browser == null || browser.isEmpty()) {
			browser = CommonUtils.getProperty("browserType");
		}
		DriverUtils.writeScreenshotToFile(Driver.getDriverInstance(), result.getName());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
		//result.getAttribute("skip");
		Reporter.setCurrentTestResult(result);		
		Reporter.log("TEST : " + result.getName() + " SKIPPED.");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
			}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFinish(ITestContext context) {
		
		/*//Remove Pass or Fail Tests from Skipped Tests
		Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
		Iterator<ITestResult> passedTestCases = context.getPassedTests().getAllResults().iterator();
		Iterator<ITestResult> failedTestCases = context.getFailedTests().getAllResults().iterator();

		while (passedTestCases.hasNext()) {
			while (skippedTestCases.hasNext()) {

				if (skippedTestCases.next().getMethod().equals(passedTestCases.next().getMethod())) {
					skippedTestCases.remove();
				} else if (skippedTestCases.next().getMethod().equals(failedTestCases.next().getMethod())) {
					skippedTestCases.remove();
				}
			}
		}*/
	}

	@Override
	public boolean retry(ITestResult result) {

		Report.info("Exception Stacktrace Given Below...");
		result.getThrowable().printStackTrace();

		if (!(result.getThrowable().toString().contains(NO_RETRY_ON_ERROR_1) || result.getThrowable().toString().contains(NO_RETRY_ON_ERROR_2))) {
			Report.info("Retrying to execute the failed test : {" + result.getName() + "} at iteration #" + retryCount);

			// Do not skip retried tests
			Iterator<ITestResult> skippedTestCases = result.getTestContext().getSkippedTests().getAllResults().iterator();
			while (skippedTestCases.hasNext()) {
				if (skippedTestCases.next().getMethod().equals(result.getMethod())) {
					skippedTestCases.remove();
				}
			}

			//Refresh the Page
			String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
			if (browser == null || browser.isEmpty()) {
				browser = CommonUtils.getProperty("browserType");
			}
			DriverUtils.refreshPage(Driver.getDriverInstance());
			DriverUtils.waitExplicit(10);

			if (retryCount < maxRetryCount) {
				retryCount++;
				return true;
			}
		} else {
			retryCount = maxRetryCount;
		}
		return false;
	}
	
}
