package core;

import static utils.DriverUtils.waitImplicit;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeTest;

import utils.CommonUtils;
import utils.Constants;
import utils.Report;

public class DriverBase {

	protected WebDriver driver;

	@BeforeTest(alwaysRun = true)
//	@Parameters(value = { "browser" })
	public void launchBrowser(){
		driver = Driver.getDriverInstance();

		if(driver != null){
			String url = CommonUtils.getProperty("loginHost");
//			Report.info("Enter URL: "+url);
			driver.get(url);
			waitImplicit(driver, Constants.WAIT_IN_SECONDS_10);

			login();
		}
	}

	private void login(){		
		String loginName = CommonUtils.getProperty("loginUser");
		String loginPassword = CommonUtils.getProperty("loginPassword");

		if(loginName != null && !loginName.isEmpty() && loginPassword != null && !loginPassword.isEmpty()){
			//TODO
		}else{
			Report.info("User Credentilas are empty please check...");
		}
	}

	@AfterTest(alwaysRun=true)
	public void quitDriver(){
		if(driver!=null){
			driver.close();
			driver.quit();
			Report.info("Driver quit successfully");
		}
	}
}
