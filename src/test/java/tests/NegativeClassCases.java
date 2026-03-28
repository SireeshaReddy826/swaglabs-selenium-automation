package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

public class NegativeClassCases extends BaseClass {

    public void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test(description = "Invalid login with wrong username should show error message")
    public void invalidLoginWrongUsername() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Attempt login with wrong username
        loginPage.enterUsername("wrong_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
        
        // Verify error message appears
        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for wrong username");
        
        // Verify still on login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page after failed login");
    }

    @Test(description = "Invalid login with wrong password should show error message")
    public void invalidLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Attempt login with wrong password
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLoginButton();
        
        // Verify error message appears
        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for wrong password");
        
        // Verify still on login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page after failed login");
    }

    @Test(description = "Invalid login with both wrong credentials should show error message")
    public void invalidLoginBothWrong() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Attempt login with both wrong credentials
        loginPage.enterUsername("wrong_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLoginButton();
        
        // Verify error message appears
        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for both wrong credentials");
        
        // Verify still on login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page after failed login");
    }

    @Test(description = "Login with locked out user should show error message")
    public void lockedOutUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Attempt login with locked out user
        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
        
        // Verify locked out error message
        String actualError = loginPage.getErrorMessage();
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        Assert.assertEquals(actualError, expectedError, 
            "Should show locked out error message");
        
        // Verify still on login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page after locked out attempt");
    }

    @Test(description = "Missing checkout info - leave postal code blank should show error")
    public void missingPostalCodeCheckout() {
        // Login and add product
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add product and go to checkout
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        
        // Fill info with blank postal code
        checkoutPage.fillCheckoutInformation("John", "Doe", "");
        checkoutPage.clickContinue();
        
        // Wait a moment for error to appear
        waitFor(1000);
        
        // Verify error message for missing postal code
        String actualError = checkoutPage.getErrorMessage();
        String expectedError = "Error: Postal Code is required";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for missing postal code");
        
        // Verify still on checkout step one (validation should prevent navigation)
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), 
            "User should remain on checkout step one with missing info");
    }

    @Test(description = "Missing checkout info - leave first name blank should show error")
    public void missingFirstNameCheckout() {
        // Login and add product
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add product and go to checkout
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        
        // Fill info with blank first name
        checkoutPage.fillCheckoutInformation("", "Doe", "12345");
        checkoutPage.clickContinue();
        
        // Wait a moment for error to appear
        waitFor(1000);
        
        // Verify error message for missing first name
        String actualError = checkoutPage.getErrorMessage();
        String expectedError = "Error: First Name is required";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for missing first name");
        
        // Verify still on checkout step one (validation should prevent navigation)
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), 
            "User should remain on checkout step one with missing info");
    }

    @Test(description = "Missing checkout info - leave last name blank should show error")
    public void missingLastNameCheckout() {
        // Login and add product
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add product and go to checkout
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.clickCartIcon();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        
        // Fill info with blank last name
        checkoutPage.fillCheckoutInformation("John", "", "12345");
        checkoutPage.clickContinue();
        
        // Wait a moment for error to appear
        waitFor(1000);
        
        // Verify error message for missing last name
        String actualError = checkoutPage.getErrorMessage();
        String expectedError = "Error: Last Name is required";
        Assert.assertEquals(actualError, expectedError, 
            "Should show error for missing last name");
        
        // Verify still on checkout step one (validation should prevent navigation)
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), 
            "User should remain on checkout step one with missing info");
    }

    @Test(description = "Remove item from cart should decrement cart badge")
    public void removeItemFromCart() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add multiple products
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        
        // Verify cart badge shows 2 items
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, 
            "Cart badge should show 2 items after adding products");
        
        // Remove one product
        inventoryPage.removeProductFromCart("Sauce Labs Backpack");
        
        // Verify cart badge decremented to 1
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, 
            "Cart badge should show 1 item after removing product");
        
        // Verify removed product is no longer in cart
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertFalse(cartPage.isItemInCart("Sauce Labs Backpack"), 
            "Removed product should not be in cart");
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bike Light"), 
            "Remaining product should still be in cart");
    }

    @Test(description = "Remove all items from cart should empty cart")
    public void removeAllItemsFromCart() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add products
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        
        // Verify cart badge shows items
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, 
            "Cart badge should show 2 items");
        
        // Remove all products from inventory page
        inventoryPage.removeProductFromCart("Sauce Labs Backpack");
        inventoryPage.removeProductFromCart("Sauce Labs Bike Light");
        
        // Verify cart badge is 0 (no badge shown)
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, 
            "Cart badge should show 0 items after removing all products");
        
        // Verify cart is empty
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertEquals(cartPage.getCartItemCount(), 0, 
            "Cart should be empty");
    }

    @Test(description = "Try to checkout with empty cart should still allow checkout")
    public void checkoutWithEmptyCart() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Go to cart without adding items
        CartPage cartPage = inventoryPage.clickCartIcon();
        
        // Verify cart is empty
        Assert.assertEquals(cartPage.getCartItemCount(), 0, 
            "Cart should be empty");
        
        // Swag Labs allows checkout even with empty cart
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(), 
            "Should navigate to checkout step one even with empty cart");
    }

    @Test(description = "Logout flow should return to login page")
    public void logoutFlow() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Verify on inventory page
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), 
            "User should be on inventory page after login");
        
        // Logout
        inventoryPage.logout();
        
        // Verify returned to login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should be returned to login page after logout");
    }

    @Test(description = "Continue shopping from cart should return to inventory page")
    public void continueShoppingFlow() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add product to cart
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        
        // Go to cart
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertTrue(cartPage.isOnCartPage(), 
            "User should be on cart page");
        
        // Click continue shopping
        InventoryPage returnedInventoryPage = cartPage.clickContinueShopping();
        
        // Verify back on inventory page
        Assert.assertTrue(returnedInventoryPage.isOnInventoryPage(), 
            "User should be returned to inventory page after clicking continue shopping");
        
        // Verify cart badge still shows the item
        Assert.assertEquals(returnedInventoryPage.getCartBadgeCount(), 1, 
            "Cart badge should still show 1 item after continue shopping");
    }

    @Test(description = "Remove items from cart page should decrement badge and remove items")
    public void removeItemsFromCartPage() {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Add multiple products
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        inventoryPage.addProductToCart("Sauce Labs Bolt T-Shirt");
        
        // Verify cart badge shows 3 items
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 3, 
            "Cart badge should show 3 items after adding products");
        
        // Go to cart
        CartPage cartPage = inventoryPage.clickCartIcon();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, 
            "Cart should contain 3 items");
        
        // Verify all items are in cart
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"), 
            "Backpack should be in cart");
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bike Light"), 
            "Bike Light should be in cart");
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bolt T-Shirt"), 
            "Bolt T-Shirt should be in cart");
        
        // Remove one item from cart page
        cartPage.removeItem("Sauce Labs Bike Light");
        
        // Verify cart item count decreased to 2
        Assert.assertEquals(cartPage.getCartItemCount(), 2, 
            "Cart should contain 2 items after removal");
        
        // Verify removed item is no longer in cart
        Assert.assertFalse(cartPage.isItemInCart("Sauce Labs Bike Light"), 
            "Removed item should not be in cart");
        
        // Verify other items are still in cart
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"), 
            "Backpack should still be in cart");
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Bolt T-Shirt"), 
            "Bolt T-Shirt should still be in cart");
        
        // Go back to inventory and verify cart badge updated
        inventoryPage = cartPage.clickContinueShopping();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, 
            "Cart badge should show 2 items after removing from cart page");
    }
}
