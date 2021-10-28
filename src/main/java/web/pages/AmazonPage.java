package web.pages;

import org.openqa.selenium.WebDriver;

import core.DriverActions;
import utils.DriverUtils;

/**
 * This is Amazon page class, it contains all the business methods for amazon
 * @author bmunegow
 *
 */
public class AmazonPage extends DriverActions{

	public static final String XPATH_SEATCH_FLD = "//input[@id='twotabsearchtextbox']";
	public static final String XPATH_SEATCH_BTN = "//input[@type='submit' and @value='Go'] ";
	
	public AmazonPage(WebDriver driver) {
		super(driver);		
	}

	public void searchItem(String item){
		
		setValue(XPATH_SEATCH_FLD, item, "Search filed", "xpath");
		click(XPATH_SEATCH_BTN, "Search Btn", "xpath");
		DriverUtils.waitExplicit(20);
	}
	
}
