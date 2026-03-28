package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitHelper;

public class LoginPage {
    private WebDriver driver;
    private WaitHelper waitHelper;
    
    // Locators
    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }
    
    public void enterUsername(String username) {
        waitHelper.waitForElementToBeVisible(usernameInput);
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }
    
    public void enterPassword(String password) {
        waitHelper.waitForElementToBeVisible(passwordInput);
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }
    
    public void clickLoginButton() {
        waitHelper.waitForElementToBeClickable(loginButton);
        driver.findElement(loginButton).click();
    }
    
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        // Wait for navigation to inventory page
        waitHelper.waitForUrlToContain("inventory");
        return new InventoryPage(driver);
    }
    
    public String getErrorMessage() {
        waitHelper.waitForElementToBeVisible(errorMessage);
        return driver.findElement(errorMessage).getText();
    }
    
    public boolean isLoginButtonDisplayed() {
        return driver.findElement(loginButton).isDisplayed();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public boolean isOnLoginPage() {
        try {
            waitHelper.waitForElementToBeVisible(loginButton);
            return driver.getCurrentUrl().contains("saucedemo.com") && 
                   !driver.getCurrentUrl().contains("inventory");
        } catch (Exception e) {
            return false;
        }
    }
}
