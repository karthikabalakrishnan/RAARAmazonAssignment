package com.amazon.common;

import java.util.logging.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.amazon.pages.BaseClass;

/**
* This class is used to generate Listeners methods
* 
* @author Karthika
* 
*         History : 
*         2020-May-04 Karthika : Implemented initial version of Listeners methods 
*/       

public class Listeners extends BaseClass implements ITestListener  {
	private static final Logger LOGGER = Logger.getLogger(Listeners.class.getName());

	public void onTestStart(ITestResult result) {
		LOGGER.info("Test case"+result.getName()+" started");
	}

	public void onTestSuccess(ITestResult result) {
		LOGGER.info("Test case "+result.getName()+" is passed");
	}

	public void onTestFailure(ITestResult result) {
		LOGGER.info("Test case "+result.getName()+" is failed");
		takeScreenshot();
	}

	public void onTestSkipped(ITestResult result) {
		LOGGER.info("Test case "+result.getName()+" is skipped");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		LOGGER.info("Test case "+result.getName()+" is failed");
	}

	public void onStart(ITestContext context) {
		LOGGER.info("Execution started");
	}

	public void onFinish(ITestContext context) {
		LOGGER.info("Execution Finished");
	}
}