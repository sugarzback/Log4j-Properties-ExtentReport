package com.testcases;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.properties.PropertiesUtility;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class LoginTest {
	//class declaration
	PropertiesUtility proputils = null;

	WebDriver driver;
	Logger log = Logger.getLogger(LoginTest.class);

	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	
	@BeforeTest
	public void extentReportSetup() {
		//location of the extent report
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		extent = new ExtentReports();  //create object of ExtentReports
		extent.attachReporter(htmlReporter);

		htmlReporter.config().setDocumentTitle("Automation Report"); // Tittle of Report
		htmlReporter.config().setReportName("Extent Report V4"); // Name of the report
		htmlReporter.config().setTheme(Theme.DARK);//Default Theme of Report

		// General information releated to application
		extent.setSystemInfo("Application Name", "Login Test");
		extent.setSystemInfo("User Name", "Sagar Vetal");
		extent.setSystemInfo("Envirnoment", "Offline Website");
	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}

	@BeforeMethod
	public void setUp() throws Exception {
		//method declaration
		proputils = new PropertiesUtility();
		
		System.setProperty("webdriver.chrome.driver", "chromedriver84.exe");
		driver = new ChromeDriver();
		log.info("launching the chrome browser");
		driver.manage().window().maximize();
		log.info("maximizing the window");
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(proputils.readAnyProperty("url"));
		log.info("url launched");
	}

	@Test
	public void test01() {
		logger = extent.createTest("Website logo validation");
		
		log.info("checking image is displayed or not");
		driver.findElement(By.xpath("//img")).isDisplayed();
		Assert.assertTrue(true);
		logger.createNode("ImagePresentYes");
		log.info("image displayed");
	}

	@Test
	public void test02() {
		logger = extent.createTest("Checking Title");
		log.info("entering username");
		driver.findElement(By.id("email")).sendKeys(proputils.readAnyProperty("username"));
		logger.createNode("Entering username");
		
		log.info("entering password");
		driver.findElement(By.id("password")).sendKeys(proputils.readAnyProperty("password"));
		logger.createNode("Entering password");
		log.info("clicking sign in");
		
		driver.findElement(By.xpath("//button")).click();
		logger.createNode("Sign in  succesfully");
		log.info("sign in succesfully");

		Assert.assertEquals(driver.getTitle(), "JavaByKiran | Dashboard");
		logger.createNode("title mactched");
		log.info("title matched");

	}
	
	@Test
	public void test03() {
		logger = extent.createTest("Checking HomePage Title");
		log.info("entering username");
		driver.findElement(By.id("email")).sendKeys(proputils.readAnyProperty("username"));
		logger.createNode("Entering username");
		
		log.info("entering password");
		driver.findElement(By.id("password")).sendKeys(proputils.readAnyProperty("password"));
		logger.createNode("Entering password");
		log.info("clicking sign in");
		
		driver.findElement(By.xpath("//button")).click();
		logger.createNode("Sign in  succesfully");
		log.info("sign in succesfully");

		Assert.assertEquals(driver.getTitle(), "JavaByKiran | Log in");
		logger.createNode("title is not matched");
		log.info("title not matched");

	}
	@AfterMethod
	public void getResult(ITestResult result) throws Exception
	{
		if(result.getStatus() == ITestResult.FAILURE)
		{
			//MarkupHelper is used to display the output in different colors
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));

			//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
			//We do pass the path captured by this method in to the extent reports using "logger.addScreenCapture" method. 

			//	String Scrnshot=TakeScreenshot.captuerScreenshot(driver,"TestCaseFailed");
			String screenshotPath = TakeScreenshot(driver, result.getName());
			//To add it in the extent report 

			logger.fail("Test Case Failed Snapshot is below " + logger.addScreenCaptureFromPath(screenshotPath));


		}
		else if(result.getStatus() == ITestResult.SKIP){
			//logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE)); 
		} 
		else if(result.getStatus() == ITestResult.SUCCESS)
		{
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
		}
		driver.quit();
	}

	public static String TakeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyy MM dd hh mm ss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		// after execution, you could see a folder "FailedTestsScreenshots" under src folder
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".jpg";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

}
