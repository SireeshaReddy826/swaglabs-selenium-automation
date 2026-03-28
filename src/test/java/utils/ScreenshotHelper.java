package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotHelper {
    private WebDriver driver;
    private String screenshotPath = "screenshots/";
    
    public ScreenshotHelper(WebDriver driver) {
        this.driver = driver;
        createScreenshotDirectory();
    }
    
    public ScreenshotHelper(WebDriver driver, String screenshotPath) {
        this.driver = driver;
        this.screenshotPath = screenshotPath;
        createScreenshotDirectory();
    }
    
    private void createScreenshotDirectory() {
        File directory = new File(screenshotPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    public String captureScreenshot(String testName) {
        try {
            // Create screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = screenshotPath + fileName;
            
            // Save screenshot
            FileHandler.copy(screenshot, new File(filePath));
            
            System.out.println("Screenshot saved: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public String captureScreenshotOnFailure(String testName, Exception e) {
        String screenshotPath = captureScreenshot(testName + "_FAILED");
        if (screenshotPath != null) {
            System.err.println("Test failed: " + e.getMessage());
            System.err.println("Screenshot captured: " + screenshotPath);
        }
        return screenshotPath;
    }
    
    public void cleanupOldScreenshots(int keepDays) {
        File directory = new File(screenshotPath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                long cutoffTime = System.currentTimeMillis() - (keepDays * 24L * 60L * 60L * 1000L);
                
                for (File file : files) {
                    if (file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            System.out.println("Deleted old screenshot: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}
