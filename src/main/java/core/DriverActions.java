package core;

import static utils.DriverUtils.switchToDefaultPage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import utils.Constants;
import utils.DriverUtils;
import utils.Report;

/**
 * This class Contains browser actions for web elements. It must be extended by
 * every page class.
 * 
 * @author bmunegow
 *
 */
abstract public class DriverActions {

	protected WebDriver driver;
	private static final String CHECKED = "checked";
	private static final String ARIA_CHECKED = "aria-checked";
	private static final String JAVA_SCRIPT_SCROLL_INTO_VIEW = "arguments[0].scrollIntoView(true);";

	/**
	 * Initialize base class constructor with driver.
	 * @param driver
	 */
	public DriverActions(WebDriver driver) {
		this.driver = driver;	
	}

	
	protected void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	public void closeWindow() {
		Report.log("Closing window - " + driver.getTitle());
		DriverUtils.closeWindow(driver);
	}
	
	
	public void takeScreenshots() {
		DriverUtils.takeScreenshot(driver);
	}
	
	/***
	 * Check if the checkbox is checked
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 * @return isChecked boolean value returned informing is checked or not.
	 */
	protected boolean isChecked(String locator, String objectName, String locatorType) {

		boolean isChecked = false;
		try {
			WebElement webElement = getWebElement(locator, locatorType);
			if (webElement != null) {
				isChecked = webElement.isSelected();
				if (isChecked)
					Report.info("'" + objectName + "' is Checked.");
				else
					Report.info("'" + objectName + "' is not Checked.");
			} else {
				Report.info("'" + objectName + "' is not present.");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found.");
		}
		return isChecked;
	}
	
	/**
	 * Click and holds the specified element
	 * @param sourceLocator
	 * @param objectName
	 * @param locatorType
	 */
	protected void clickAndHold(String sourceLocator, String objectName, String locatorType) {

		WebElement sourceElement = getWebElement(sourceLocator, locatorType);

		if (sourceElement != null) {
			try {
				new Actions(driver).clickAndHold(sourceElement).perform();
				Report.pass("Click and hold the element '" + objectName + "'.");
			} catch (Exception e) {
				Report.error("Could not Click and hold the element '" + objectName + "'.");
			}
		} else {
			Report.error("Could not Click and hold the element '" + objectName + "'.");
		}
	}
	
	/**
	 * Move the mouse to specified element
	 * @param sourceLocator
	 * @param objectName
	 */
	protected void moveToElement(String sourceLocator, String objectName, String locatorType) {

		WebElement sourceElement = getWebElement(sourceLocator, locatorType);

		if (sourceElement != null) {
			try {
				new Actions(driver).moveToElement(sourceElement).build().perform();
				Report.pass("Move to element '" + objectName + "'.");
			} catch (Exception e) {
				Report.error("Could not Move to element '" + objectName + "'.");
			}
		} else {
			Report.error("Could not Move to element '" + objectName + "'.");
		}
	}
	
	/**
	 * This method scrolls down to the web element.
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 * @return TRUE if the successfully scrolled to the given element else FALSE.
	 */
	protected boolean scrollDownToElement(String locator, String objectName, String locatorType) {

		WebElement element = getElement(locator, locatorType);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isElementPresent = false;

		if (element != null) {
			js.executeScript(JAVA_SCRIPT_SCROLL_INTO_VIEW, element);
			Report.pass("Scrolled down to the element '" + objectName + "'");
			isElementPresent = true;
		}
		return isElementPresent;
	}
	
	/**
	 * Check the check box if not already checked or uncheck the check box if  already checked - action Click/Unclick
	 * @param locator
	 * @param tobeChecked
	 * @param objectName
	 * @param locatorType
	 */
	protected void actionClickOrUnclickCheckBox(String locator, boolean tobeChecked, String objectName, String locatorType) {
		WebElement element = getWebElement(locator, locatorType);
		if (element != null) {
			String checked = element.getAttribute(CHECKED);
			DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
			if (checked == null || checked.isEmpty())
				checked = element.getAttribute(ARIA_CHECKED);
			DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);

			// Already checked.
			if (checked != null && !checked.isEmpty() && checked.equalsIgnoreCase("true")) {
				// Uncheck the checkbox
				if (!tobeChecked) {
					new Actions(driver).moveToElement(element).click().perform();
					Report.pass("'" + objectName + "' checkbox is unchecked.");
				} else {
					Report.pass("'" + objectName + "' checkbox is already checked.");
				}
			}
			// Already unchecked
			else {
				// Check the checkbox
				if (tobeChecked) {
					new Actions(driver).moveToElement(element).click().perform();
					Report.pass("'" + objectName + "' checkbox is checked.");
				} else {
					Report.pass("'" + objectName + "' checkbox is already unchecked.");
				}
			}
		} else {
			Report.fail("'" + objectName + "' is not found");
		}
	}
	
	/**
	 * Check if the check box if not already checked or uncheck the check box if already checked.
	 * @param locator
	 * @param tobeChecked
	 * @param objectName
	 * @param locatorType
	 */
	protected void clickOrUnclickCheckBox(String locator, boolean tobeChecked, String objectName, String locatorType) {
		WebElement element = getWebElement(locator, locatorType);
		if (element != null) {
			String checked = element.getAttribute(CHECKED);
			DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
			if (checked == null || checked.isEmpty())
				checked = element.getAttribute(ARIA_CHECKED);
			DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);

			try {
				// Already checked.
				if (checked != null && !checked.isEmpty() && checked.equalsIgnoreCase("true")) {
					// Uncheck the checkbox
					if (!tobeChecked) {
						element.click();
						Report.pass("'" + objectName + "' checkbox is unchecked.");
					} else {
						Report.pass("'" + objectName + "' checkbox is already checked.");
					}
				}
				// Already unchecked
				else {
					// Check the checkbox
					if (tobeChecked) {
						element.click();
						Report.pass("'" + objectName + "' checkbox is checked.");
					} else {
						Report.pass("'" + objectName + "' checkbox is already unchecked.");
					}
				}
			} catch (StaleElementReferenceException e) {
				Report.fail("'" + objectName + "' could not be clicked.");
			}
		} else {
			Report.fail("'" + objectName + "' is not found");
		}
	}

	
	/**
	 * This method gets the WebElement for the given locator and returns the HTML inner text value
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 * @return the inner text of an element
	 */
	protected String getInnerHTML(String locator, String objectName, String locatorType) {
		String text = "";
		WebElement webElement = getWebElement(locator, locatorType);
		try {
			if (webElement != null) {
				DriverUtils.waitExplicit(Constants.WAIT_IN_SECONDS_2);
				text = webElement.getAttribute("innerHTML");
				Report.pass("'" + objectName + "' is found");
			} else {
				Report.fail("'" + objectName + "' is not found");
			}
		} catch (StaleElementReferenceException e) {
			Report.error("'" + objectName + "' is not found");
		}
		return text != null ? text.trim() : text;
	}

	
	/**
	 * This method gets the WebElement for the given locator and returns the text value
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 * @return the test for the element as a string.
	 */
	protected String getText(String locator, String objectName, String locatorType) {
		String text = "";
		WebElement webElement = getWebElement(locator, locatorType);
		try {
			if (webElement != null) {
				DriverUtils.waitExplicit(Constants.WAIT_IN_SECONDS_2);
				text = webElement.getText();
				Report.pass("The text for '" + objectName + "' is '" + text + "'");
			} else {
				Report.fail("The text for '" + objectName + "' is not found");
			}
		} catch (StaleElementReferenceException e) {
			Report.error("'" + objectName + "' is not found");
		}
		return text != null ? text.trim() : text;
	}
	
	/**
	 * This method returns attribute value of web element
	 * @param locator
	 * @param attributeName
	 * @param objectName
	 * @return attribute value of web element
	 */
	protected String getAttribute(String locator, String attributeName, String objectName, String locatorType) {

		WebElement element = getWebElement(locator, locatorType);
		if (element != null) {
			String value = element.getAttribute(attributeName);
			Report.pass("'" + objectName + "' has value " + value);
			return value;
		} else {
			Report.error("'" + objectName + "' is not found");
			return "";
		}
	}

	
	/**
	 * This method finds the WebElement and checks if the web element is visible or not 
	 * @param locator           
	 * @param objectName
	 * @param locatorType            
	 * @return true if a web element is found, else return false
	 */
	protected boolean isVisible(String locator, String objectName, String locatorType) {
		boolean isVisible = false;
		WebElement element = getWebElement(locator, locatorType);
		if (element != null) {
			isVisible = element.isDisplayed();
			if (isVisible)
				Report.info("'" + objectName + "' is visible.");
			else
				Report.info("'" + objectName + "' is not visible.");
		} else {
			Report.info("'" + objectName + "' is not present.");
		}
		return isVisible;
	}
	
	
	/**
	 * This method performs keyboard ENTER
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 */
	protected void sendEnter(String locator, String objectName, String locatorType) {
		try {
			WebElement element = getWebElement(locator, locatorType);
			if (element != null) {
				element.sendKeys(Keys.ENTER);
			}
		} catch (Exception e) {
			Report.fail("Couldn't find object:" + objectName);
		}
	}
	
	/**
	 * This method finds webElement then move to element. 
	 * This can be used for mouse hover actions.
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 */
	protected void mouseOver(String locator, String objectName, String locatorType) {
		try {
			WebElement webElement = getWebElement(locator, locatorType);
			if (webElement != null) {
				new Actions(driver).moveToElement(webElement).build().perform();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.pass("Moved to " + "'" + objectName + "'.");
			} else {
				Report.fail("'" + objectName + "' is not found.");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found.");
		}
	}
	
	/**
	 * This method finds webElement based on elementType and double clicks on the element
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 */
	protected void doubleClick(String locator, String objectName, String locatorType) {
		try {
			WebElement webElement = getWebElement(locator, locatorType);
			if (webElement != null) {
				new Actions(driver).doubleClick(webElement).perform();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.pass("'" + objectName + "'" + " is double clicked.");
			} else {
				Report.fail("'" + objectName + "' is not found.");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found.");
		}
	}
	
	/**
	 * This method finds webElement based on elementType and right clicks on the element
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 */
	protected void rightClick(String locator, String objectName, String locatorType) {
		try {
			WebElement webElement = getWebElement(locator, locatorType);
			if (webElement != null) {
				new Actions(driver).moveToElement(webElement).contextClick().build().perform();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.log("'" + objectName + "'" + " is right clicked.");
			} else {
				Report.fail("'" + objectName + "' is not found.");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found.");
		}
	}
	
	
	protected void selectMultipleElements(String locator, String [] optionNames, String objectName, String locatorType) {
		WebElement element = getWebElement(locator, locatorType);
		if (element != null) {
			String optionName = null;
			try {
				new Actions(driver).keyDown(Keys.CONTROL).build().perform();
				Select select = new Select(element);
				for (String optionNameTemp : optionNames) {
					optionName = optionNameTemp;
					select.selectByVisibleText(optionName);
					Report.pass(objectName + ": '" + optionName + "' is selected.");
				}
				new Actions(driver).keyUp(Keys.CONTROL).build().perform();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
			} catch (UnexpectedTagNameException | NoSuchElementException e) {
				Report.fail(objectName + ": '" + optionName + "' is not found");
			}
		} else {
			Report.fail("'" + objectName + "' is not found");
		}
	}
	
	/**
	 * It finds the webElement then move to element and perform click on the element - Actions Click
	 * @param locator
	 * @param objectName
	 */
	protected void actionClick(String locator, String objectName, String locatorType) {
		try {
			WebElement webElement = getWebElement(locator, locatorType);
			if (webElement != null) {
				new Actions(driver).moveToElement(webElement).click().perform();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.pass("Move to " + "'" + objectName + "'" + " and clicked.");
			} else {
				Report.fail("'" + objectName + "' is not found.");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found.");
		}
	}
	
			
	/**
	 * This method finds webElement based on elementType and Click on the
	 * element through JavaScript
	 * 
	 * @param locator
	 * @param objectName
	 * @param locatorType
	 */
	protected void jsClick(String locator, String objectName, String locatorType) {
		try {
			WebElement element = getWebElement(locator, locatorType);
			if (element != null) {
				Report.info("Element : " + objectName + " is found");
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				Report.pass("'" + objectName + "'" + " is jsClicked");
			} else {
				Report.fail("'" + objectName + "' is not found");
			}
		} catch (Exception e) {
			Report.fail("Unable to jsClick " + "'" + objectName + "'");
		}
	}
	
	
	protected void setValue(String locator, String value, String objectName, String locatorType) {
		try {
			WebElement element = getWebElement(locator, locatorType);
			if (element != null) {
				// Clear the text first in the text box.
				String textPresent = element.getAttribute("value");
				if (textPresent != null && !textPresent.isEmpty()) {
					String selectAll = Keys.chord(Keys.CONTROL, "a");
					element.sendKeys(selectAll);
					element.sendKeys(Keys.BACK_SPACE);
				} else {
					textPresent = element.getText();
					if (textPresent != null && !textPresent.isEmpty())
						element.clear();
				}
				// Input the new text.
				element.sendKeys(value);
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.pass("'" + value + "' text entered into " + objectName);
			} else {
				Report.fail("'" + objectName + "' is not found");
			}
		} catch (IllegalArgumentException e) {
			Report.fail("Invalid value " + value + " passed.");
		}
	}
	
	protected void click(String locator, String objectName, String locatorType) {
		if(driver != null)
			System.out.println("Not Null");
		else
			System.out.println("Null");
		try {
			WebElement element = getWebElement(locator, locatorType);
			if (element != null) {
				element.click();
				DriverUtils.waitImplicit(driver, Constants.WAIT_IN_SECONDS_5);
				Report.pass("'" + objectName + "'" + " is clicked");
			} else {
				Report.fail("'" + objectName + "' is not found");
			}
		} catch (StaleElementReferenceException e) {
			Report.fail("'" + objectName + "' is not found");
		} catch (WebDriverException e) {
			Report.fail("Unable to click " + "'" + objectName + "'");
		}
	}


	protected WebElement getWebElement(String locator, String locatorType) {

		List<WebElement> elements = getElements(locator, locatorType);
		if (!elements.isEmpty()) {
			return elements.get(0);
		}

		switchToDefaultPage(driver);
		elements = getElements(locator, locatorType);

		if (!elements.isEmpty()) {
			return elements.get(0);
		} else {
			return switchToFrameAndReturnElement(locator, locatorType);
		}
	}

	protected List<WebElement> getElements(String locator, String locatorType) {

		List<WebElement> elements = new ArrayList<WebElement>();
		switch (locatorType) {

		case "xpath":
			elements = driver.findElements(By.xpath(locator));
			break;
		case "css":
			elements = driver.findElements(By.cssSelector(locator));
			break;
		case "name":
			elements = driver.findElements(By.name(locator));
			break;
		case "id":
			elements = driver.findElements(By.id(locator));
			break;
		case "tagname":
			elements = driver.findElements(By.tagName(locator));
			break;
		default:
			elements = null;
			Report.info("Invalid locator type... Please check");
			break;
		}
		return elements;	
	}


	/**
	 * Switch to a frame and check for the element. If element exists in the frame return it,
	 * else recursively switch to next frame and check for element
	 * @param locator
	 * @param locatorType
	 * @return web element if present else null.
	 */
	protected WebElement switchToFrameAndReturnElement(String locator, String locatorType) {

		int index = 0;
		List<WebElement> frameElements;

		frameElements = getElements("iframe", "tag name");
		// driver.findElements(By.tagName("iframe"));
		Iterator<WebElement> itr = frameElements.iterator();

		while (itr.hasNext()) {
			try {
				driver.switchTo().frame(index);

				List<WebElement> elements = getElements(locator, locatorType);
				if (!elements.isEmpty()) {
					return elements.get(0);
				} else {
					return switchToFrameAndReturnElement(locator, locatorType);
				}
			} catch (NoSuchFrameException e) {
				switchToDefaultPage(driver);
			}
			itr.next();
		}
		return null;
	}

	protected WebElement getElement(String locator, String locatorType) {
		try {
			DriverUtils.waitExplicit(Constants.WAIT_IN_SECONDS_2);
			List<WebElement> element = getElements(locator, locatorType);
			return element.get(0);
		} catch (NoSuchElementException e) {
			return null;
		}
	}


}

