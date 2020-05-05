package com.amazon.pages;

import static com.amazon.common.Constants.PROPERTY_FILE_PATH;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.amazon.common.PropertyUtility;
import com.amazon.test.ProductCheckout;
import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;


/**
 * Actual class where product search functionality handled
 * 
 * @author Karthika
 * 
 *         History : 
 *         2020-May-04 Karthika : Added Search method to perform search functions
 *         2020-May-05 Karthika : Handled scroll event & Refactored the code
 * 
 */

public class ProductSearchPage extends BaseClass{
	PropertyUtility putility = new PropertyUtility(PROPERTY_FILE_PATH);
	private static final Logger LOGGER = Logger.getLogger(ProductCheckout.class.getName());
	By search = putility.getObject("search");
	By filter = putility.getObject("filter");
	By opt_Filter = putility.getObject("opt_Filter");
	By opt_Catogries = putility.getObject("opt_Catogries");
	By opt_Sort = putility.getObject("opt_Sort");
	By opt_Television = putility.getObject("opt_Television");
	By opt_TelevisionSize = putility.getObject("opt_TelevisionSize");
	By priceinSearchPage = putility.getObject("priceinSearchPage");
	
	MobileElement element;
	
	/* Method to perform Search operation */
	public void searchTV(String productSearchType) {
		setValue(search, productSearchType);
		((RemoteWebDriver) driver).executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));
		LOGGER.info("Performed search");
	}
	
	/* Method to scroll to the particular element */
	public MobileElement scrollToTheProduct(String eleText,String resourceId) {
		element = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\""+resourceId+"\")).scrollIntoView("
						+ "new UiSelector().text(\""+eleText+"\"))"));
		return element;
	}
	
	/* Method to get the product title */
	public String getProductText() {
		return getText(element);
	}
	
	public String getProductPrice() {
		MobileElement priceEle = (MobileElement) driver.findElement(priceinSearchPage);
		return getText(priceEle);
	}
	
}
