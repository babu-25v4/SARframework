package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.LaunchSuite;

public class DriverUtils {

	public static WebDriver driver;
	private static ArrayList<File> screenShotsBucket = new ArrayList<File>();


	@BeforeTest(alwaysRun=true)
	public static WebDriver initDriver() {
		CommonUtils.loadProperties();
		String browserType = CommonUtils.getProperty("browserType");
		if (browserType.equalsIgnoreCase("chrome")) {
			String osName = System.getProperty("os.name");
			if (osName != null && !osName.isEmpty() && (osName.indexOf("Windows") >= 0 )) {
				System.setProperty("webdriver.chrome.driver", Constants.DRIVER_PATH_CHROME);
			} else {
				// We can check for Mac/Linux
			}				
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");			
			driver = new ChromeDriver(options);			
		}	
		return driver;
	}
	
	
	/**
	 * This method is created to add multiple screenshots for a test in case of failure for better investigation
	 * @param driver
	 */
	public static void takeScreenshot(WebDriver driver) {
		Report.log("Taking screenshot");
		File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		addScreenShotToBucket(screenShot);
	}
	
	private static void addScreenShotToBucket(File screenShot) {
		screenShotsBucket.add(screenShot);
	}
	
	/**
	 * Implicitly wait in seconds.
	 * 
	 * @param driver
	 * @param seconds - integer value for specifying seconds.
	 */
	public static void waitImplicit(WebDriver driver, int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	public static void switchToDefaultPage(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public static void waitExplicit(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public static void writeScreenshotToFile(WebDriver driver, String testName) {

		String screenShotLocalPath = Constants.REPORTS_SCREENSHOTS_PATH + System.currentTimeMillis() + "_" + testName
				+ ".png";
		String dScreenShotSharedPath = System.getProperty("screenshotpath");

		// if shared path is present then store screenshots in shared path -
		// teamCity
		if (dScreenShotSharedPath != null && !dScreenShotSharedPath.isEmpty()) {
			screenShotLocalPath = dScreenShotSharedPath.trim() + "\\" + System.currentTimeMillis() + "_" + testName
					+ ".png";
		}

		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotLocalPath));
			Report.log("<br><img src='" + screenShotLocalPath + "' height='400' width='900'/><br>");
		} catch (IOException e) {
			Report.fail("Unable to write to " + screenShotLocalPath);
		}
	}
	
	public static void refreshPage(WebDriver driver) {
		driver.navigate().refresh();
		waitExplicit(5);
	}
	
	/**
	 * This method closes the recently opened tab on current browser
	 * @param driver
	 */
	public static void closeWindow(WebDriver driver) {
		driver.close();
		DriverUtils.switchToDefaultWindow(driver);
	}
	
	/**
	 * Switch to the main window.
	 * @param driver
	 * @param windowTitle
	 */
	public static void switchToDefaultWindow(WebDriver driver) {
		for (String windowHandle : driver.getWindowHandles()) {
			if (driver.switchTo().window(windowHandle).getTitle().equals(/*TEXT_ORACLE_RESPONSYS_TITLE*/"")) //TODO
				{
				Report.pass("Switched to default window");
				break;
			}
		}
	}
	
	/*@AfterSuite(alwaysRun=true)
	public void quitDriver(){
		if(driver!=null){
			driver.close();
			driver.quit();
			Report.info("Driver quit successfully");
		}
	}
*/
}
