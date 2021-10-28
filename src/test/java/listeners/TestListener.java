package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import core.Driver;
import utils.DriverUtils;
import utils.Report;

/**
 * Listener for every test class.
 * 
 * @author psanthar
 *
 */
public class TestListener implements ITestListener {
	static boolean loginFromWSSuite = false;

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
		// Commenting the code as for UI tests we always pass browser parameter
		// from XML

		/*
		 * if (browser == null || browser.isEmpty()) { browser =
		 * CommonUtils.getProperty("browserType"); }
		 */
		if (browser == null || browser.isEmpty()) {
			// Do not call the driver instance if browser is null(used for
			// Webservices)
		} else {
			DriverUtils.writeScreenshotToFile(Driver.getDriverInstance(), result.getName());
			// DriverUtils.refreshPage(Driver.getDriverInstance(browser, "",
			// ""));
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
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
		// TODO Auto-generated method stub
	}
}
