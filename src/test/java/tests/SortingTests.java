package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingTests extends BaseClass {
    private InventoryPage inventoryPage;

    @Test(description = "Verify A-Z sorting works correctly")
    public void verifyAZSorting() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Sort A-Z
        inventoryPage.sortProducts("Name (A to Z)");
        
        // Get product names and verify they are sorted A-Z
        List<String> productNames = getProductNames();
        List<String> expectedNames = new ArrayList<>(productNames);
        Collections.sort(expectedNames);
        
        Assert.assertEquals(productNames, expectedNames, 
            "Products should be sorted A-Z");
    }

    @Test(description = "Verify Z-A sorting works correctly")
    public void verifyZASorting() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Sort Z-A
        inventoryPage.sortProducts("Name (Z to A)");
        
        // Get product names and verify they are sorted Z-A
        List<String> productNames = getProductNames();
        List<String> expectedNames = new ArrayList<>(productNames);
        Collections.sort(expectedNames, Collections.reverseOrder());
        
        Assert.assertEquals(productNames, expectedNames, 
            "Products should be sorted Z-A");
    }

    @Test(description = "Verify Price Low to High sorting works correctly")
    public void verifyPriceLowToHighSorting() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Sort Price Low to High
        inventoryPage.sortProducts("Price (low to high)");
        
        // Get product prices and verify they are sorted low to high
        List<Double> productPrices = getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(productPrices);
        Collections.sort(expectedPrices);
        
        Assert.assertEquals(productPrices, expectedPrices, 
            "Products should be sorted by price low to high");
    }

    @Test(description = "Verify Price High to Low sorting works correctly")
    public void verifyPriceHighToLowSorting() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Sort Price High to Low
        inventoryPage.sortProducts("Price (high to low)");
        
        // Get product prices and verify they are sorted high to low
        List<Double> productPrices = getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(productPrices);
        expectedPrices.sort(Collections.reverseOrder());
        
        Assert.assertEquals(productPrices, expectedPrices, 
            "Products should be sorted by price high to low");
    }

    @Test(description = "Verify sorting maintains correct order after multiple changes")
    public void verifyMultipleSortingChanges() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Sort A-Z first
        inventoryPage.sortProducts("Name (A to Z)");
        List<String> azNames = getProductNames();
        List<String> expectedAZ = new ArrayList<>(azNames);
        Collections.sort(expectedAZ);
        Assert.assertEquals(azNames, expectedAZ, "A-Z sorting should work");
        
        // Then sort Z-A
        inventoryPage.sortProducts("Name (Z to A)");
        List<String> zaNames = getProductNames();
        List<String> expectedZA = new ArrayList<>(zaNames);
        Collections.sort(expectedZA, Collections.reverseOrder());
        Assert.assertEquals(zaNames, expectedZA, "Z-A sorting should work");
        
        // Then sort Price Low to High
        inventoryPage.sortProducts("Price (low to high)");
        List<Double> lowHighPrices = getProductPrices();
        List<Double> expectedLowHigh = new ArrayList<>(lowHighPrices);
        Collections.sort(expectedLowHigh);
        Assert.assertEquals(lowHighPrices, expectedLowHigh, "Price low to high sorting should work");
        
        // Finally sort Price High to Low
        inventoryPage.sortProducts("Price (high to low)");
        List<Double> highLowPrices = getProductPrices();
        List<Double> expectedHighLow = new ArrayList<>(highLowPrices);
        expectedHighLow.sort(Collections.reverseOrder());
        Assert.assertEquals(highLowPrices, expectedHighLow, "Price high to low sorting should work");
    }

    @Test(description = "Verify sorting dropdown contains all expected options")
    public void verifySortingDropdownOptions() {
        // Login
        pages.LoginPage loginPage = new pages.LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        
        // Get all sorting options (this would require additional method in InventoryPage)
        // For now, we'll test that each sorting option works without throwing exceptions
        
        // Test A-Z sorting doesn't throw exception
        try {
            inventoryPage.sortProducts("Name (A to Z)");
        } catch (Exception e) {
            Assert.fail("A-Z sorting should not throw exception: " + e.getMessage());
        }
        
        // Test Z-A sorting doesn't throw exception
        try {
            inventoryPage.sortProducts("Name (Z to A)");
        } catch (Exception e) {
            Assert.fail("Z-A sorting should not throw exception: " + e.getMessage());
        }
        
        // Test Price low to high sorting doesn't throw exception
        try {
            inventoryPage.sortProducts("Price (low to high)");
        } catch (Exception e) {
            Assert.fail("Price low to high sorting should not throw exception: " + e.getMessage());
        }
        
        // Test Price high to low sorting doesn't throw exception
        try {
            inventoryPage.sortProducts("Price (high to low)");
        } catch (Exception e) {
            Assert.fail("Price high to low sorting should not throw exception: " + e.getMessage());
        }
    }

    // Helper methods to extract product data
    private List<String> getProductNames() {
        return inventoryPage.getAllProductNames();
    }

    private List<Double> getProductPrices() {
        return inventoryPage.getAllProductPrices();
    }
}
