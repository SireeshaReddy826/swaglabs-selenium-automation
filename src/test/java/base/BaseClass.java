package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ScreenshotHelper;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.Duration;

public class BaseClass {
    protected WebDriver driver;
    protected ExtentReports extent;
    protected ExtentTest test;
    protected String baseUrl;
    
    @BeforeSuite
    public void setupExtent() {
        extent = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent.attachReporter(reporter);
        
        // Set base URL from system property or default
        baseUrl = System.getProperty("baseUrl", "https://www.saucedemo.com/");
    }
    
    @BeforeMethod
    public void setUp() {
        test = extent.createTest("Test");
        
        // Setup Chrome with incognito mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);
        
        test.log(Status.INFO, "Test setup completed. URL: " + baseUrl);
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (test != null && result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            String testName = result.getName() != null ? result.getName() : "Test";
            String screenshotPath = takeScreenshot(testName);
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } else if (test != null) {
            test.log(Status.PASS, "Test Passed");
        }
        
        if (driver != null) {
            driver.quit();
        }
    }
    
    @AfterSuite
    public void flushExtent() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    public void waitFor(int milliseconds) {
        // Deprecated - use explicit waits instead
        System.out.println("WARNING: waitFor() is deprecated. Use waitForElementVisible() instead.");
    }
    
    // Proper explicit wait methods
    protected void waitForElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    protected void waitForElementClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    private String takeScreenshot(String testName) {
        try {
            ScreenshotHelper screenshotHelper = new ScreenshotHelper(driver);
            return screenshotHelper.captureScreenshot(testName + "_FAILED");
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    // Simple logging method for tests
    protected void logInfo(String message) {
        if (test != null) {
            test.log(Status.INFO, message);
        }
        System.out.println("INFO: " + message);
    }
    
    // Simple step logging method for tests
    protected void logStep(String stepName, String description) {
        if (test != null) {
            test.log(Status.INFO, stepName + ": " + description);
        }
        System.out.println(stepName + ": " + description);
    }
}
