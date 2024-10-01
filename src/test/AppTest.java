package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import main.pages.HomePage;
import main.pages.LoginPage;

public class AppTest {
	
	private HomePage homePage = new HomePage();
	private LoginPage loginPage = new LoginPage();
	private List<WebElement> links;
	Scanner sc = new Scanner(System.in);
	
	ExtentReports extent;
    ExtentTest test;
    
    private static final String REPORT_PATH = "test-output" + File.separator + "AutomationTestReport.html";

	@BeforeSuite
	public void setup() {
		System.out.println("Enter mode for testing (mobile/desktop). Default is Desktop : ");
		String mode = sc.nextLine().equalsIgnoreCase("mobile") ? "mobile" : "desktop"; 
		System.out.println("Selected Mode : " + mode);
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(REPORT_PATH);

        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("App Test Report");

        // Initialize ExtentReports and attach the reporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Add some system information to the report
        extent.setSystemInfo("Mode", mode);
		
		this.homePage.setupDriver(mode);
		this.homePage.navigateToHome();
	}
	
	@Test(description = "This test clicks on Login button and navigates to Login Page", priority = 1) 
	public void verifyLoginPageNavigation() {
		test = extent.createTest("Login Page Navigation", "Verifies Navigation is Success");
		boolean navigated = this.homePage.navigateToLoginPage();
		Assert.assertTrue(navigated);
		test.pass("Successfuly navigated to Login Page");
	}
	
	@Test(description = "This test checks if the title of the login page says 'Login'.", priority = 2)
	public void verifyLoginPageTitle() {
		test = extent.createTest("Login Page Title", "Login Page Title should be 'Login'");
		String pageTitle = "";
		try {
			pageTitle = this.loginPage.getPageTitle();
		} catch (Exception e) {
			test.fail(e.getMessage());
		}
		Assert.assertEquals(pageTitle, "Login", "Page Title DOES NOT MATCH");
		test.pass("Login Page title matches");
	}
	
	@Test(description = "Check if email input field is present", priority = 3)
	public void verifyEmailFieldIsPresent() {
		test = extent.createTest("Email Input Field", "Verify Presence of Email Input Field'");
		boolean isEmailFieldPresent = this.loginPage.isEmailInputPresent();
		Assert.assertTrue(isEmailFieldPresent, "Email Field is NOT PRESENT");
		test.pass("Email Input Field is Present");
	}
	
	@Test(description = "Check if password input fields is present", priority = 4)
	public void verifyPasswordFieldIsPresent() {
		test = extent.createTest("Password Input Field", "Verify Presence of Password Input Field'");
		boolean isPasswordFieldPresent = this.loginPage.isPasswordInputPresent();
		Assert.assertTrue(isPasswordFieldPresent, "Password Field is NOT PRESENT");
		test.pass("Password Input Field is Present");
	}
	
	@Test(description = "Switch back to main demo tab and verify the same", priority = 5)
	public void switchBackToMainTab() {
		test = extent.createTest("Switch Back to Home", "Verify switch back to Home Page");
		boolean switchedBack = this.homePage.switchBackHome();
		Assert.assertTrue(switchedBack, "Switch Back Home Failed");
		test.pass("Switched back to Home Page");
	}
	
	@Test(description = "Get list of links in the webpage", priority = 6)
	public void getAllLinks() {
		test = extent.createTest("Webpage Links", "Get links in webpage");
		this.links = this.homePage.getAllLinks();
		Assert.assertTrue(links.size() != 0);
		test.pass("Got the list of links in webpage");
	}
	
	@Test(description = "Make API requests to the links and verify status codes", priority = 7)
	public void verifyAPIStatusCodes() {
		test = extent.createTest("Webpage Links Verification", "Make API Requests to all links and verify status codes");
		List<Integer> statusCodes = this.homePage.getRequestStatusCodes();
		if(statusCodes.size() == 0) {
			Assert.fail("Verifying Status Codes FAILED!!!");
			test.fail("Verifying Status Codes FAILED!!!");
		}
		Integer successStatusCodesCount = 0;
		Integer failureStatusCodesCount = 0;
		for(Integer statusCode: statusCodes) {
			if(statusCode == 200) {
				successStatusCodesCount += 1;
			}
			else {
				failureStatusCodesCount += 1;
			}
		}
		test.pass("Successful API calls : " + successStatusCodesCount.toString() + ". Failed API Calls: " + failureStatusCodesCount.toString());
	}
	
	@AfterMethod
    public void getResult(org.testng.ITestResult result) throws IOException {
        // Log test status to the report
		String screenshotPath = this.homePage.takeScreenshot(result.getName());
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped: " + result.getThrowable()); 
        }
        test.addScreenCaptureFromPath(screenshotPath);
        extent.flush();
    }
	
	@AfterSuite
	public void tearDown() {
		this.homePage.endTest();
	}

	public static void main(String[] args) {
        // Setting up TestNG Test Runner
        org.testng.TestNG testng = new org.testng.TestNG();
        testng.setTestClasses(new Class[] { AppTest.class });
        testng.run();
    }

}

