package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

public class CheckoutPage {
    private WebDriver driver;
    private WaitHelper waitHelper;

    // Locators
    private By firstNameInput = By.id("first-name");
    private By lastNameInput = By.id("last-name");
    private By postalCodeInput = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By cancelButton = By.id("cancel");
    private By errorMessage = By.cssSelector("[data-test='error']");
    private By finishButton = By.id("finish");
    private By backToHomeButton = By.id("back-to-products");
    private By completeHeader = By.className("complete-header");
    private By completeText = By.className("complete-text");
    private By ponyExpress = By.className("pony_express");
    private By subtotalLabel = By.className("summary_subtotal_label");
    private By taxLabel = By.className("summary_tax_label");
    private By totalLabel = By.className("summary_total_label");
    private By pageTitle = By.className("title");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    // Step One form filling
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        waitHelper.waitForElementToBeVisible(firstNameInput);
        driver.findElement(firstNameInput).clear();
        driver.findElement(firstNameInput).sendKeys(firstName);

        driver.findElement(lastNameInput).clear();
        driver.findElement(lastNameInput).sendKeys(lastName);

        driver.findElement(postalCodeInput).clear();
        driver.findElement(postalCodeInput).sendKeys(postalCode);
    }

    public void clickContinue() {
        waitHelper.waitForElementToBeClickable(continueButton);
        driver.findElement(continueButton).click();

        By pageTitle = By.className("title");
        waitHelper.waitForElementToBeVisible(pageTitle);

        // Wait until header text is correct or timeout
        String header = driver.findElement(pageTitle).getText();
        int retries = 0;
        while (!header.equals("Checkout: Overview") && !header.equals("Checkout: Your Information") && retries < 3) {
            waitHelper.waitFor(500); // short pause
            header = driver.findElement(pageTitle).getText();
            retries++;
        }

        // If still on step one, validation likely failed - don't throw exception
        if (!header.equals("Checkout: Overview")) {
            System.out.println("Validation likely failed - staying on checkout step one. Header: " + header);
        } else {
            System.out.println("Navigation to checkout step two completed. Current URL: " + driver.getCurrentUrl());
        }
    }
    public void clickCancel() {
        waitHelper.waitForElementToBeClickable(cancelButton);
        driver.findElement(cancelButton).click();
    }

    public void clickFinish() {
        waitHelper.waitForElementToBeClickable(finishButton);
        driver.findElement(finishButton).click();
        waitHelper.waitForUrlToContain("checkout-complete");
    }

    public void clickBackToHome() {
        waitHelper.waitForElementToBeClickable(backToHomeButton);
        driver.findElement(backToHomeButton).click();
        waitHelper.waitForUrlToContain("inventory");
    }

    // Error handling
    public String getErrorMessage() {
        try {
            waitHelper.waitForElementToBeVisible(errorMessage);
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Confirmation page
    public String getCompleteHeader() {
        waitHelper.waitForElementToBeVisible(completeHeader);
        return driver.findElement(completeHeader).getText();
    }

    public String getCompleteText() {
        waitHelper.waitForElementToBeVisible(completeText);
        return driver.findElement(completeText).getText();
    }

    public boolean isPonyExpressImageDisplayed() {
        try {
            return driver.findElement(ponyExpress).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Order summary
    public String getSubtotal() {
        waitHelper.waitForElementToBeVisible(subtotalLabel);
        return driver.findElement(subtotalLabel).getText();
    }

    public String getTax() {
        waitHelper.waitForElementToBeVisible(taxLabel);
        return driver.findElement(taxLabel).getText();
    }

    public String getTotal() {
        waitHelper.waitForElementToBeVisible(totalLabel);
        return driver.findElement(totalLabel).getText();
    }

    // Page checks (header-based)
    public boolean isOnCheckoutStepOne() {
        try {
            waitHelper.waitForElementToBeVisible(pageTitle);
            String header = driver.findElement(pageTitle).getText();
            return header.equals("Checkout: Your Information");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnCheckoutStepTwo() {
        try {
            waitHelper.waitForElementToBeVisible(pageTitle);
            String header = driver.findElement(pageTitle).getText();
            return header.equals("Checkout: Overview");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnCheckoutComplete() {
        try {
            waitHelper.waitForElementToBeVisible(pageTitle);
            String header = driver.findElement(pageTitle).getText();
            return header.equals("Checkout: Complete!");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCompleteHeaderDisplayed() {
        try {
            return driver.findElement(completeHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}