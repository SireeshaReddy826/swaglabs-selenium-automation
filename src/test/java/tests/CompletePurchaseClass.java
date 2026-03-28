package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CompletePurchaseClass extends BaseClass {

    // Helper method to parse price strings and convert to double
    private double parsePrice(String priceString) {
        // Remove currency symbols and convert to double
        String cleanedPrice = priceString.replaceAll("[^\\d.]", "");
        return Double.parseDouble(cleanedPrice);
    }

    // Helper method to format double to currency string
    private String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
        return df.format(price);
    }

    // DataProvider for multiple user datasets
    @DataProvider(name = "purchaseData")
    public Object[][] getPurchaseData() {
        return new Object[][] {
            {
                "standard_user", "secret_sauce", 
                "John", "Doe", "12345",
                "Sauce Labs Backpack", "Sauce Labs Bike Light"
            },
            {
                "standard_user", "secret_sauce", 
                "Jane", "Smith", "54321",
                "Sauce Labs Bolt T-Shirt", "Sauce Labs Fleece Jacket"
            },
            {
                "standard_user", "secret_sauce", 
                "Alice", "Johnson", "98765",
                "Sauce Labs Onesie", "Test.allTheThings() T-Shirt (Red)"
            }
        };
    }

    @Test(description = "Data-driven complete purchase flow with multiple datasets", 
          dataProvider = "purchaseData")
    public void dataDrivenPurchaseFlow(String username, String password, 
                                   String firstName, String lastName, String postalCode,
                                   String product1, String product2) {
        String expectedConfirmationMessage = "Thank you for your order!";

        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);

        // Step 1: Login
        logStep("Login", "Attempting to login with user: " + username);
        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be on inventory page after login");
        logInfo("Login successful for user: " + username);

        // Step 2: Add products
        logStep("Add Products", "Adding products to cart");
        inventoryPage.addProductToCart(product1);
        logInfo("Added " + product1 + " to cart. Cart badge: " + inventoryPage.getCartBadgeCount());

        inventoryPage.addProductToCart(product2);
        logInfo("Added " + product2 + " to cart. Cart badge: " + inventoryPage.getCartBadgeCount());

        // Step 3: Verify cart badge
        logStep("Verify Cart Badge", "Checking cart item count");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items");
        logInfo("Cart badge verification passed");

        // Step 4: Navigate to cart
        logStep("Navigate to Cart", "Clicking cart icon");
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
        Assert.assertTrue(cartPage.isItemInCart(product1), "First product should be in cart");
        Assert.assertTrue(cartPage.isItemInCart(product2), "Second product should be in cart");
        logInfo("Navigation to cart successful");

        // Step 5: Checkout
        logStep("Checkout", "Initiating checkout process");
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), "User should be on checkout step one");
        logInfo("Checkout step one loaded");

        // Step 6: Fill info
        logStep("Fill Information", "Entering user details");
        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepTwo(), "User should be on checkout step two");
        logInfo("User information filled successfully");

        // Step 7: Verify summary and validate calculations
        logStep("Price Validation", "Validating subtotal and total calculations");
        String subtotalText = checkoutPage.getSubtotal();
        String taxText = checkoutPage.getTax();
        String totalText = checkoutPage.getTotal();
        
        Assert.assertNotNull(subtotalText, "Subtotal should be displayed");
        Assert.assertNotNull(taxText, "Tax should be displayed");
        Assert.assertNotNull(totalText, "Total should be displayed");
        
        logInfo("Price details - Subtotal: " + subtotalText + ", Tax: " + taxText + ", Total: " + totalText);
        
        // Parse prices
        double subtotal = parsePrice(subtotalText);
        double tax = parsePrice(taxText);
        double total = parsePrice(totalText);
        
        // Calculate expected subtotal from cart items
        double expectedSubtotal = 0.0;
        String product1Price = cartPage.getItemPrice(product1);
        String product2Price = cartPage.getItemPrice(product2);
        expectedSubtotal += parsePrice(product1Price);
        expectedSubtotal += parsePrice(product2Price);
        
        logInfo("Expected subtotal: " + expectedSubtotal + ", Actual subtotal: " + subtotal);
        
        // Validate subtotal matches sum of item prices
        Assert.assertEquals(subtotal, expectedSubtotal, 0.01, 
            "Subtotal should equal sum of item prices");
        logInfo("Subtotal validation passed");
        
        // Validate total = subtotal + tax
        double expectedTotal = subtotal + tax;
        logInfo("Expected total: " + expectedTotal + ", Actual total: " + total);
        Assert.assertEquals(total, expectedTotal, 0.01, 
            "Total should equal subtotal plus tax");
        logInfo("Total validation passed");

        // Step 8: Finish
        logStep("Complete Purchase", "Finishing checkout process");
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOnCheckoutComplete(), "User should be on checkout complete page");
        Assert.assertTrue(checkoutPage.isCompleteHeaderDisplayed(), "Complete header should be displayed");
        Assert.assertTrue(checkoutPage.isPonyExpressImageDisplayed(), "Pony Express image should be displayed");
        logInfo("Purchase completed successfully");

        // Step 9: Confirmation
        logStep("Verify Confirmation", "Checking order confirmation");
        Assert.assertEquals(checkoutPage.getCompleteHeader(), expectedConfirmationMessage,
                "Confirmation message should match expected");
        Assert.assertNotNull(checkoutPage.getCompleteText(), "Complete text should be displayed");
        logInfo("Order confirmation verified");

        // Step 10: Back to home
        logStep("Return Home", "Navigating back to inventory page");
        checkoutPage.clickBackToHome();
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be back on inventory page");
        
        System.out.println("Purchase flow completed successfully for user: " + firstName + " " + lastName);
        logInfo("Test completed successfully for user: " + firstName + " " + lastName);
    }

    @Test(description = "Complete purchase flow: Login, add products, checkout, and verify confirmation")
    public void completePurchaseFlow() {
        // Test data
        String username = "standard_user";
        String password = "secret_sauce";
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";
        String expectedConfirmationMessage = "Thank you for your order!";

        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);

        // Step 1: Login
        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be on inventory page after login");

        // Step 2: Add products
        String product1 = "Sauce Labs Backpack";
        String product2 = "Sauce Labs Bike Light";
        inventoryPage.addProductToCart(product1);
        System.out.println("Cart badge after product1: " + inventoryPage.getCartBadgeCount());

        inventoryPage.addProductToCart(product2);
        System.out.println("Cart badge after product2: " + inventoryPage.getCartBadgeCount());


        // Step 3: Verify cart badge
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items");

        // Step 4: Navigate to cart
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
        Assert.assertTrue(cartPage.isItemInCart(product1), "First product should be in cart");
        Assert.assertTrue(cartPage.isItemInCart(product2), "Second product should be in cart");

        // Step 5: Checkout
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), "User should be on checkout step one");

        // Step 6: Fill info
        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepTwo(), "User should be on checkout step two");

        // Step 7: Verify summary and validate calculations
        String subtotalText = checkoutPage.getSubtotal();
        String taxText = checkoutPage.getTax();
        String totalText = checkoutPage.getTotal();
        
        Assert.assertNotNull(subtotalText, "Subtotal should be displayed");
        Assert.assertNotNull(taxText, "Tax should be displayed");
        Assert.assertNotNull(totalText, "Total should be displayed");
        
        // Parse prices
        double subtotal = parsePrice(subtotalText);
        double tax = parsePrice(taxText);
        double total = parsePrice(totalText);
        
        // Calculate expected subtotal from cart items
        double expectedSubtotal = 0.0;
        String product1Price = cartPage.getItemPrice(product1);
        String product2Price = cartPage.getItemPrice(product2);
        expectedSubtotal += parsePrice(product1Price);
        expectedSubtotal += parsePrice(product2Price);
        
        // Validate subtotal matches sum of item prices
        Assert.assertEquals(subtotal, expectedSubtotal, 0.01, 
            "Subtotal should equal sum of item prices");
        
        // Validate total = subtotal + tax
        double expectedTotal = subtotal + tax;
        Assert.assertEquals(total, expectedTotal, 0.01, 
            "Total should equal subtotal plus tax");

        // Step 8: Finish
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOnCheckoutComplete(), "User should be on checkout complete page");
        Assert.assertTrue(checkoutPage.isCompleteHeaderDisplayed(), "Complete header should be displayed");
        Assert.assertTrue(checkoutPage.isPonyExpressImageDisplayed(), "Pony Express image should be displayed");

        // Step 9: Confirmation
        Assert.assertEquals(checkoutPage.getCompleteHeader(), expectedConfirmationMessage,
                "Confirmation message should match expected");
        Assert.assertNotNull(checkoutPage.getCompleteText(), "Complete text should be displayed");

        // Step 10: Back to home
        checkoutPage.clickBackToHome();
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be back on inventory page");
    }

    @Test(description = "Complete purchase flow with product index selection")
    public void completePurchaseFlowWithIndexSelection() {
        // Test data
        String username = "standard_user";
        String password = "secret_sauce";
        String firstName = "Jane";
        String lastName = "Smith";
        String postalCode = "54321";
        String expectedConfirmationMessage = "Thank you for your order!";

        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "User should be on inventory page");

        // Add products by index
        inventoryPage.addProductToCartByIndex(0);
        inventoryPage.addProductToCartByIndex(1);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items");

        // Checkout
        CartPage cartPage = inventoryPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), "User should be on checkout step one");

        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepTwo(), "User should be on checkout step two");

        // Verify summary and validate calculations
        String subtotalText = checkoutPage.getSubtotal();
        String taxText = checkoutPage.getTax();
        String totalText = checkoutPage.getTotal();
        
        // Parse prices
        double subtotal = parsePrice(subtotalText);
        double tax = parsePrice(taxText);
        double total = parsePrice(totalText);
        
        // Validate total = subtotal + tax
        double expectedTotal = subtotal + tax;
        Assert.assertEquals(total, expectedTotal, 0.01, 
            "Total should equal subtotal plus tax");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOnCheckoutComplete(), "User should be on checkout complete page");

        // Confirmation
        Assert.assertEquals(checkoutPage.getCompleteHeader(), expectedConfirmationMessage,
                "Confirmation message should match expected");
    }
}