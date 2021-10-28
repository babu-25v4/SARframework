package utils;

import java.io.File;

/**
 * All the constants like file paths etc need to be placed here.
 *
 * @author bmunegow
 */
public class Constants {

	// Project Properties file and test data file paths
	public static final String PROJECT_BASE_PATH = CommonUtils.getProjectBasePath();
	public static final String PROJECT_FULL_PATH = PROJECT_BASE_PATH + File.separator + "SARframework" + File.separator;
	public static final String TEST_PROPERTIES_FILE_PATH = PROJECT_FULL_PATH + "config" + File.separator + "test.properties";
	public static final String REPORTS_SCREENSHOTS_PATH = Constants.PROJECT_FULL_PATH + File.separator + "reports" + File.separator + "screenshots" + File.separator;
	// Driver paths.
	public static final String DRIVER_PATH_CHROME = PROJECT_FULL_PATH + File.separator + "drivers" + File.separator	+ "chromedriver.exe";
	
	// Test data paths
	public static final String TEST_DATA_FOLDER_PATH = PROJECT_FULL_PATH + "data" +File.separator;
	public static final String TEST_DATA_AMAZON_PATH = TEST_DATA_FOLDER_PATH + "amazon" +File.separator;
	
	// Wait time in seconds. To be used for implicit / explicit waits.
	public static final int WAIT_IN_SECONDS_1 = 1;
	public static final int WAIT_IN_SECONDS_2 = 2;
	public static final int WAIT_IN_SECONDS_5 = 5;
	public static final int WAIT_IN_SECONDS_10 = 10;
	
}
