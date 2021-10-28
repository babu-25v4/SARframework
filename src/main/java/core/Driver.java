package core;

import org.openqa.selenium.WebDriver;
import utils.DriverUtils;

public class Driver {

	private static WebDriver driver;

	/**
	 * Private constructor to prevent overloading.
	 */
	private Driver() {
	}

	public static WebDriver getDriverInstance(){

		if (driver == null ) {
			driver = DriverUtils.initDriver();
		}	
		return driver;
	}
}
