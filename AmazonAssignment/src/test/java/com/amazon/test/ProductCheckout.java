package com.amazon.test;

import static com.amazon.common.Constants.PROPERTY_FILE_PATH;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.amazon.common.PropertyUtility;
import com.amazon.pages.BaseClass;
import com.amazon.pages.LoginPage;
import com.amazon.pages.ProductDetailPage;
import com.amazon.pages.ProductSearchPage;

import io.appium.java_client.MobileElement;

/**
 * Actual class where product checkout test case handled
 * 
 * @author Karthika
 * 
 *         History : 
 *         2020-May-04 Karthika : Added simple test case to check
 *         positive flow of selecting the product and navigating to checkout
 *         screen & Handled Swipe event and Re-factored the testcase
 *         2020-May-05 Karthika : Added data provider to read input data from excel & Re-factored the code
 * 
 */

public class ProductCheckout extends BaseClass {
	PropertyUtility putility = new PropertyUtility(PROPERTY_FILE_PATH);
	private static final Logger LOGGER = Logger.getLogger(ProductCheckout.class.getName());
	By languageSelection = putility.getObject("languageSelection");
	By saveChanges = putility.getObject("saveChanges");
	By search = putility.getObject("search");
	By ratings = putility.getObject("ratings");
	By checkoutProductName = putility.getObject("checkoutProductName");
	By addToCart = putility.getObject("addToCart");

	/*
	 * Testcase to compare the details of product search page with product checkout page
	 */
	@Test(dataProvider="productType")
	public void testProductCheckout(String productSearchType) {
		System.out.println(System.getProperty("user.dir"));
		LoginPage loginPage = new LoginPage();
		loginPage.loginToAmazon();
		// to select the language
		languageSelection();
		waitForElementPresence(search);
		ProductSearchPage searchPage = new ProductSearchPage();
		System.out.println("Added method to read input data from excel"+productSearchType);
		// pass product search type from data provider
		searchPage.searchTV(productSearchType);
		waitForElementPresence(ratings);

		// Rotate the screen from default portrait to landscape
		rotateScreen(ScreenOrientation.LANDSCAPE);

		// Identify Elememt using Text usimg scroll gesture
		ProductSearchPage productSearchPage = new ProductSearchPage();
		String eleText = "Tulsi California Almonds 1kg";
		String resourceId ="com.amazon.mShop.android.shopping:id/rs_search_results_root";
		MobileElement element = productSearchPage.scrollToTheProduct(eleText,resourceId);
		boolean response = element.isDisplayed();
		assertTrue(response);
		LOGGER.info("Scrolled to particular product");

		String searchProductName = productSearchPage.getProductText();
		String searchProductPrice = split(productSearchPage.getProductPrice());
		element.click();
		rotateScreen(ScreenOrientation.PORTRAIT);

		languageSelection();

		waitForElementPresence(checkoutProductName);
		ProductDetailPage productDetailPage = new ProductDetailPage();
		String checkoutProductName = productDetailPage.getProductTitle();
		String[] checkoutProductPrice = productDetailPage.getProductPrice().split(" ");
		assertEquals(searchProductName, checkoutProductName);
		assertEquals(searchProductPrice, checkoutProductPrice[1]);
		if (searchProductName.equalsIgnoreCase(checkoutProductName) && searchProductPrice.equalsIgnoreCase(checkoutProductPrice[1])) {
			LOGGER.info("Test case ran successfully");
		}
		
	}
	public void languageSelection() {
		try {
          if( driver.findElement(languageSelection).isDisplayed() && driver.findElement(languageSelection).isEnabled()) {
        	  clickElement(languageSelection);
        	  waitForElementPresence(saveChanges);
        	  clickElement(saveChanges);
          }
		}
		catch(NoSuchElementException igNoSuchElementException) {
			
		}
		LOGGER.info("languageSelection radio button" + languageSelection + "  is not visible");
	}
	
	@DataProvider(name="productType")
	public Object[][] searchInputData() throws IOException {
		Object[][] arrayObject = readInputFromExcel(System.getProperty("user.dir")+"\\src\\main\\resources\\configs\\TestData\\ProductSearchType.xlsx");
		return arrayObject;
	}

}