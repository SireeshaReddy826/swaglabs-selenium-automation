package utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserOptions {
    
    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Use incognito mode to disable password manager and breach detection
        options.addArguments("--incognito");
        
        // Additional test-specific options
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        
        return options;
    }
    
    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        // Use private browsing mode for Firefox
        options.addArguments("-private");
        
        return options;
    }
}
