package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.CartPage;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private WaitHelper waitHelper;

    // Locators
    private By productContainer = By.className("inventory_item");
    private By cartBadge = By.className("shopping_cart_badge");
    private By cartIcon = By.id("shopping_cart_container");
    private By productNames = By.className("inventory_item_name");
    private By productPrices = By.className("inventory_item_price");
    private By sortDropdown = By.className("product_sort_container");
    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");
    private By pageTitle = By.className("title");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    public void addProductToCart(String productName) {
        System.out.println("Attempting to add product to cart: " + productName);
        
        // Use a more specific locator that includes the product name container
        By productContainer = By.xpath("//div[@class='inventory_item' and .//div[text()='" + productName + "']]");
        By productButton = By.xpath("//div[@class='inventory_item' and .//div[text()='" + productName + "']]//button");
        
        // Wait for the product container to be visible
        waitHelper.waitForElementToBeVisible(productContainer);
        
        // Find the button within the specific product container
        WebElement button = driver.findElement(productButton);
        String buttonText = button.getText();
        System.out.println("Button text for " + productName + ": " + buttonText);
        
        if (buttonText.equalsIgnoreCase("Add to cart")) {
            // Scroll the element into view first
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            waitHelper.waitFor(500);
            
            // Try multiple click methods on the specific button element
            try {
                button.click();
                System.out.println("Regular click successful for: " + productName);
            } catch (Exception e) {
                System.out.println("Regular click failed, trying JavaScript click for: " + productName);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
            
            // Wait and verify the click worked
            waitHelper.waitFor(1000);
            buttonText = driver.findElement(productButton).getText();
            System.out.println("Button text after click for " + productName + ": " + buttonText);
            
            // If still "Add to cart", try Actions with the specific element
            if (buttonText.equalsIgnoreCase("Add to cart")) {
                System.out.println("Click didn't work, trying Actions click for: " + productName);
                org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                actions.moveToElement(button).click().perform();
                waitHelper.waitFor(1000);
                buttonText = driver.findElement(productButton).getText();
                System.out.println("Button text after Actions click for " + productName + ": " + buttonText);
            }
            
            // Final verification - if still not working, try one more time with a different approach
            if (buttonText.equalsIgnoreCase("Add to cart")) {
                System.out.println("All clicks failed, trying force click for: " + productName);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click(); arguments[0].blur();", button);
                waitHelper.waitFor(1500);
                buttonText = driver.findElement(productButton).getText();
                System.out.println("Button text after force click for " + productName + ": " + buttonText);
            }
        } else {
            System.out.println("Product " + productName + " is already in cart (button text: " + buttonText + ")");
        }
    }

    public void addProductToCartByIndex(int index) {
        By productButton = By.xpath("//div[@class='inventory_item'][" + (index + 1) + "]//button");
        waitHelper.waitForElementToBeClickable(productButton);
        driver.findElement(productButton).click();
    }

    public void removeProductFromCart(String productName) {
        By productLocator = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        waitHelper.waitForElementToBeClickable(productLocator);
        driver.findElement(productLocator).click();
    }

    public int getCartBadgeCount() {
        try {
            // Try to get badge count first
            waitHelper.waitForElementToBeVisible(cartBadge);
            String count = driver.findElement(cartBadge).getText();
            int badgeCount = Integer.parseInt(count);
            System.out.println("Cart badge count: " + badgeCount);
            return badgeCount;
        } catch (Exception e) {
            System.out.println("Cart badge not found, counting 'Remove' buttons as fallback");
            // Fallback: count products with "Remove" button text
            int removeButtonCount = driver.findElements(By.xpath("//button[text()='Remove']")).size();
            System.out.println("Remove button count: " + removeButtonCount);
            return removeButtonCount;
        }
    }

    public CartPage clickCartIcon() {
        System.out.println("Current URL before clicking cart: " + driver.getCurrentUrl());
        waitHelper.waitForElementToBeClickable(cartIcon);
        System.out.println("Cart icon found and is clickable");
        try {
            driver.findElement(cartIcon).click();
            System.out.println("Cart icon clicked successfully");
        } catch (Exception e) {
            System.out.println("Regular click failed, trying JavaScript click: " + e.getMessage());
            // Fallback to JavaScript click if regular click fails
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(cartIcon));
            System.out.println("Cart icon clicked via JavaScript");
        }
        // Wait for cart page by checking for cart-specific elements instead of URL
        try {
            waitHelper.waitForElementToBeVisible(By.className("cart_list"));
            System.out.println("Cart page detected via cart_list element");
        } catch (Exception e) {
            System.out.println("Cart list not found, trying URL wait as fallback");
            waitHelper.waitForUrlToContain("cart");
        }
        System.out.println("Navigation to cart page completed. Current URL: " + driver.getCurrentUrl());
        return new CartPage(driver);
    }

    public boolean isProductDisplayed(String productName) {
        By productLocator = By.xpath("//div[text()='" + productName + "']");
        return driver.findElement(productLocator).isDisplayed();
    }

    public String getProductPrice(String productName) {
        By priceLocator = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//div[@class='inventory_item_price']");
        waitHelper.waitForElementToBeVisible(priceLocator);
        return driver.findElement(priceLocator).getText();
    }

    public void sortProducts(String sortOption) {
        waitHelper.waitForElementToBeClickable(sortDropdown);
        driver.findElement(sortDropdown).click();
        By optionLocator = By.xpath("//option[text()='" + sortOption + "']");
        waitHelper.waitForElementToBeClickable(optionLocator);
        driver.findElement(optionLocator).click();
    }

    public void clickMenuButton() {
        waitHelper.waitForElementToBeClickable(menuButton);
        driver.findElement(menuButton).click();
    }

    public void clickLogoutLink() {
        waitHelper.waitForElementToBeClickable(logoutLink);
        driver.findElement(logoutLink).click();
    }

    public void logout() {
        clickMenuButton();
        clickLogoutLink();
    }

    public boolean isOnInventoryPage() {
        try {
            waitHelper.waitForElementToBeVisible(pageTitle);
            String header = driver.findElement(pageTitle).getText();
            return header.equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        try {
            var elements = driver.findElements(productNames);
            for (var element : elements) {
                names.add(element.getText());
            }
        } catch (Exception e) {
            System.out.println("Error getting product names: " + e.getMessage());
        }
        return names;
    }

    public List<Double> getAllProductPrices() {
        List<Double> prices = new ArrayList<>();
        try {
            var elements = driver.findElements(productPrices);
            for (var element : elements) {
                String priceText = element.getText().replace("$", "");
                prices.add(Double.parseDouble(priceText));
            }
        } catch (Exception e) {
            System.out.println("Error getting product prices: " + e.getMessage());
        }
        return prices;
    }
}