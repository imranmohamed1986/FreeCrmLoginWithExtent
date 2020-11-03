package CognimentoCRM.Tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
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
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class LoginTest {
	 public WebDriver driver;
	 public ExtentHtmlReporter htmlReporter;
	 public ExtentReports extent;
	 public ExtentTest test;

	 @BeforeTest
	 public void setExtent() {
	  // specify location of the report
	  htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/myReport.html");

	  htmlReporter.config().setDocumentTitle("Automation Report"); // Tile of report
	  htmlReporter.config().setReportName("Functional Testing"); // Name of the report
	  htmlReporter.config().setTheme(Theme.DARK);
	  
	  extent = new ExtentReports();
	  extent.attachReporter(htmlReporter);
	  
	  // Passing General information
	  extent.setSystemInfo("Host name", "localhost");
	  extent.setSystemInfo("Environemnt", "QA");
	  extent.setSystemInfo("user", "Moe");
	 }

	 @AfterTest
	 public void endReport() {
	  extent.flush();
	 }
	 
	 
	@BeforeMethod
	public void setUp() {

		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get("https://ui.freecrm.com/");

	}

	@Test(priority = 1)
	public void validatingPageTitle() {
		 test = extent.createTest("validatingPageTitle");
		String PageTitle = driver.getTitle();
		System.out.println("The Page Title is: " + PageTitle);
		Assert.assertEquals(PageTitle, "Cogmento CRM", "Title is Verified");

	}

	
	@Test(priority = 2)
	public void verifyForgotPasswordLink() {
		 test = extent.createTest("verifyForgotPasswordLink");
		boolean Flag = driver.findElement(By.linkText("Forgot your password?")).isDisplayed();
		Assert.assertTrue(Flag, "Successfully Verified The Link");

	}

	@Test(priority = 3)
	public void validatingLoginLink() {
		 test = extent.createTest("validatingLoginLink");
		String login = driver.findElement(By.linkText("Classic CRM")).getText();
		System.out.println("The login link is: " + login);
		Assert.assertEquals(login, "Classic CRM", "Succefully Verified The Button");
		boolean Flag2 = driver.findElement(By.linkText("Classic CRM")).isDisplayed();
		Assert.assertTrue(Flag2, "Verified");
	}


	@Test(priority = 4)
	public void validatingSignUpLink() {
		 test = extent.createTest("validatingSignUpLink");
		Assert.assertTrue(driver.findElement(By.linkText("Sign Up")).isDisplayed());
	}


	@Test(priority = 5)
	public void enteringLoginCredentials() throws InterruptedException {
		 test = extent.createTest("enteringLoginCredentials");
		driver.findElement(By.name("email")).sendKeys("imran_mohamed1986@yahoo.com");
		driver.findElement(By.name("password")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='ui fluid large blue submit button']")).click();
		Thread.sleep(3000);
		String Confirmation = driver.findElement(By.xpath("//span[text()='Mohamed imran']")).getText();
		Assert.assertEquals(Confirmation, "Mohamed imran45652", "User Created");

	}

	 @AfterMethod
	 public void tearDown(ITestResult result) throws IOException {
	  if (result.getStatus() == ITestResult.FAILURE) {
	   test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getName()); // to add name in extent report
	   test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); // to add error/exception in extent report
	   String screenshotPath = LoginTest.getScreenshot(driver, result.getName());
	   test.addScreenCaptureFromPath(screenshotPath);// adding screen shot
	  } else if (result.getStatus() == ITestResult.SKIP) {
	   test.log(Status.SKIP, "Test Case SKIPPED IS " + result.getName());
	  }
	  else if (result.getStatus() == ITestResult.SUCCESS) {
	   test.log(Status.PASS, "Test Case PASSED IS " + result.getName());
	  }
	  driver.quit();
	 }
	 
	 public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
	  String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	  TakesScreenshot ts = (TakesScreenshot) driver;
	  File source = ts.getScreenshotAs(OutputType.FILE);
	  
	  // after execution, you could see a folder "FailedTestsScreenshots" under src folder
	  String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
	  File finalDestination = new File(destination);
	  FileUtils.copyFile(source, finalDestination);
	  return destination;
	 }
	}
