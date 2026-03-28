package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WaitHelper waitHelper;

    // Locators
    private By cartItems = By.className("cart_item");
    private By itemName = By.className("inventory_item_name");
    private By itemPrice = By.className("inventory_item_price");
    private By itemQuantity = By.className("cart_quantity");
    private By continueShoppingButton = By.id("continue-shopping");
    private By checkoutButton = By.id("checkout");
    private By cartBadge = By.className("shopping_cart_badge");
    private By pageTitle = By.className("title");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    public List<String> getCartItems() {
        List<String> items = new ArrayList<>();
        waitHelper.waitForElementToBeVisible(cartItems);
        driver.findElements(itemName).forEach(element -> items.add(element.getText()));
        return items;
    }

    public String getItemPrice(String itemName) {
        By priceLocator = By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='cart_item']//div[@class='inventory_item_price']");
        waitHelper.waitForElementToBeVisible(priceLocator);
        return driver.findElement(priceLocator).getText();
    }

    public int getItemQuantity(String itemName) {
        By quantityLocator = By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='cart_item']//div[@class='cart_quantity']");
        waitHelper.waitForElementToBeVisible(quantityLocator);
        String quantity = driver.findElement(quantityLocator).getText();
        return Integer.parseInt(quantity);
    }

    public void removeItem(String itemName) {
        By removeLocator = By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='cart_item']//button");
        waitHelper.waitForElementToBeClickable(removeLocator);
        driver.findElement(removeLocator).click();
    }

    public InventoryPage clickContinueShopping() {
        waitHelper.waitForElementToBeClickable(continueShoppingButton);
        driver.findElement(continueShoppingButton).click();
        return new InventoryPage(driver);
    }

    public CheckoutPage clickCheckout() {
        System.out.println("Current URL before clicking checkout: " + driver.getCurrentUrl());
        waitHelper.waitForElementToBeClickable(checkoutButton);
        System.out.println("Checkout button found and is clickable");
        
        // Try multiple click methods
        try {
            driver.findElement(checkoutButton).click();
            System.out.println("Regular checkout click successful");
        } catch (Exception e) {
            System.out.println("Regular click failed, trying JavaScript click");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(checkoutButton));
        }
        
        // Wait and verify navigation worked
        waitHelper.waitFor(1000);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL after first click attempt: " + currentUrl);
        
        // If still on cart page, try Actions click
        if (currentUrl.contains("cart.html")) {
            System.out.println("Still on cart page, trying Actions click");
            org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
            actions.moveToElement(driver.findElement(checkoutButton)).click().perform();
            waitHelper.waitFor(1000);
            currentUrl = driver.getCurrentUrl();
            System.out.println("URL after Actions click: " + currentUrl);
        }
        
        // Wait for checkout step one page by checking for form elements
        try {
            waitHelper.waitForElementToBeVisible(By.id("first-name"));
            System.out.println("Checkout step one detected via first-name field");
        } catch (Exception e) {
            System.out.println("First name field not found, trying URL wait as fallback");
            waitHelper.waitForUrlToContain("checkout-step-one");
        }
        System.out.println("Navigation to checkout step one completed. Current URL: " + driver.getCurrentUrl());
        return new CheckoutPage(driver);
    }

    public int getCartItemCount() {
        try {
            return driver.findElements(cartItems).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isItemInCart(String itemName) {
        return getCartItems().contains(itemName);
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    public boolean isOnCartPage() {
        return driver.getCurrentUrl().contains("cart");
    }

    public boolean isCheckoutButtonDisplayed() {
        try {
            return driver.findElement(checkoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}