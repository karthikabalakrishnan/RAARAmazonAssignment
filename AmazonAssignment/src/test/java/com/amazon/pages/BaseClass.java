package com.amazon.pages;

import static com.amazon.common.Constants.APP_PROPERTIES_FILE_PATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.amazon.common.PropertyUtility;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * This class is used to generate the common actions which performs on mobile
 * device
 * 
 * @author Karthika
 * 
 *         History : 
 *         2020-May-04 Karthika : Added before suite method to
 *         invoke the driver with desired capablities
 *         2020-May-05 Karthika : Added common methods used across the app
 *        
 */

public class BaseClass {
	public static WebDriver driver;
	private static final Logger LOGGER = Logger.getLogger(BaseClass.class.getName());
	// to load all the common properties
	PropertyUtility prop = new PropertyUtility(APP_PROPERTIES_FILE_PATH);
	XSSFWorkbook wb= null;

	/*
	 * Returns all the capabilities that can be used as prerequisites and Invoke the
	 * driver based on the (@Param Browser)
	 * 
	 * @param Browser- an appropriate platform should be provided
	 */
	@BeforeSuite
	@Parameters("Browser")
	public void setup(String Browser) throws InterruptedException {
		if (Browser.equals("Android")) {
			try {
				// to push the apk for the first time when the apk is not installed in the
				// device
				File apkDir = new File("C:\\Users\\dk\\Downloads");
				File apk = new File(apkDir, "Amazon_shopping.apk");

				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("platformName", prop.getProperty("platformName"));
				capabilities.setCapability("udid", prop.getProperty("udid"));
				capabilities.setCapability("deviceName", prop.getProperty("deviceName"));
				capabilities.setCapability("platformVersion", prop.getProperty("platformVersion"));
				// Enable below line only for the first time apk is pushed
				capabilities.setCapability("app", apk.getAbsolutePath());

				capabilities.setCapability("appPackage", prop.getProperty("appPackage"));
				capabilities.setCapability("appActivity", prop.getProperty("appActivity"));
				capabilities.setCapability("noRest", true);
				capabilities.setCapability("autoGrantPermissions", true);
				driver = new AndroidDriver(new URL(prop.getProperty("hostURL")), capabilities);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Method to get the text based on the element
	 * 
	 * @param element - the mobile element in which need to capture text of it
	 */
	public String getText(MobileElement element) {
		return element.getText().trim();
	}

	/*
	 * Method to click the element based on locator
	 * 
	 * @param selector - By element to pass the locator
	 */
	public void clickElement(By selector) {
		try {
			driver.findElement(selector).click();
			LOGGER.info("Element " + selector + "  clicked");
		} catch (NoSuchElementException e) {
			LOGGER.info("Element NOT present - " + selector);
			Assert.fail("Elelemnt " + selector + " could not be clicked because - " + e);
		} catch (StaleElementReferenceException e1) {
			LOGGER.info("Element  " + selector + "   NOT present");
			Assert.fail("Elelemnt " + selector + " could not be clicked because - " + e1);
		}
	}

	/*
	 * Method to set the value in the text box based on locator
	 * 
	 * @param selector - By element to pass the locator
	 * 
	 * @param val - value to type in Mobile element
	 */
	public void setValue(By selector, String val) {
		try {
			MobileElement ele = (MobileElement) driver.findElement(selector);
			ele.clear();
			ele.sendKeys(val);
		} catch (NoSuchElementException e) {
			LOGGER.info("Element NOT present - " + selector);
			Assert.fail("Value could not be set in element - " + selector + "   because - " + e);
		} catch (StaleElementReferenceException e1) {
			LOGGER.info("Element NOT present");
			Assert.fail("Value could not be set in element - " + selector + "  because - " + e1);
		} catch (Exception e2) {
			LOGGER.info("Value could not be set because of error:  " + e2);
			Assert.fail("Value could not be set in element - " + selector + "  because - " + e2);

		}
	}

	/*
	 * Method to check element displayed
	 * 
	 * @param selector - By element to pass the locator
	 */
	protected boolean isElementDisplayed(By selector) {
		return driver.findElement(selector).isDisplayed();
	}

	/*
	 * Method to check element presence using explicit wait condition
	 * 
	 * @param selector - By element to pass the locator
	 */
	protected void waitForElementPresence(By selector) {
		WebDriverWait wait = new WebDriverWait(driver, 2000);
		wait.until(ExpectedConditions.presenceOfElementLocated(selector));
	}

	/* Method to test the app in landscape mode 
	 * @param Orientation - Landscape / portrait 
	 * */
	protected void rotateScreen(ScreenOrientation orientation) {
		((AppiumDriver) driver).rotate(orientation);
	}

	/* Method to take screensht if any test case fails */
	protected void takeScreenshot() {
		TakesScreenshot scrShot = (TakesScreenshot) driver;
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File("failureScreenshot.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Method to read data from excel 
	 * @param fileName - test data input file name
	 * */
	protected static Object[][] readInputFromExcel(String fileName) throws IOException {

		File src = new File(fileName);
		// Excel sheet file location get mentioned here
		FileInputStream fileInputStream = new FileInputStream(src); 
		// get my workbook
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream); 
		// get my sheet from workbook
		XSSFSheet worksheet = workbook.getSheet("Sheet1");
		// get my Row which start from 0
		XSSFRow Row = worksheet.getRow(0); 
		DataFormatter formatter = new DataFormatter();
		// count my number of Rows
		int RowNum = worksheet.getPhysicalNumberOfRows();
		// get last ColNum
		int ColNum = Row.getLastCellNum(); 
		// pass my count data in array
		Object Data[][] = new Object[RowNum - 1][ColNum]; 
		// Loop work for Rows
		for (int i = 0; i < RowNum - 1; i++) 
		{
			XSSFRow row = worksheet.getRow(i + 1);
			// Loop work for colNum
			for (int j = 0; j < ColNum; j++) 
			{
				if (row == null)
					Data[i][j] = "";
				else {
					XSSFCell cell = row.getCell(j);
					if (cell == null)
						// if it get Null value it pass no data
						Data[i][j] = ""; 
					else {
						String value = formatter.formatCellValue(cell);
						// This formatter get my all values as string i.e integer, float all type
						// data value
						Data[i][j] = value; 
					}
				}
			}
		}

		return Data;
	}

	/* Method to split the string */
	public String split(String textToSplit) {
		String[] arrOfStr = textToSplit.split(" ");
		String number = "";
		String letter = "";
		for (int i = 0; i < arrOfStr[0].length(); i++) {
			char a = arrOfStr[0].charAt(i);
			if (Character.isDigit(a)) {
				number = number + a;

			} else {
				letter = letter + a;
			}
		}
		return number;
	}

	/* Methd to quit the driver after the test executed */
	@AfterSuite
	public void teardown() throws IOException {
		driver.quit();
	}
}
