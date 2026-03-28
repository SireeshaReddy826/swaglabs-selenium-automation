package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void log(String message) {
        String timestamp = dateFormat.format(new Date());
        System.out.println("[" + timestamp + "] " + message);
    }
    
    public static void logInfo(String message) {
        log("INFO: " + message);
    }
    
    public static void logError(String message) {
        log("ERROR: " + message);
    }
    
    public static void logWarning(String message) {
        log("WARNING: " + message);
    }
    
    public static void logDebug(String message) {
        log("DEBUG: " + message);
    }
    
    public static void logTestStart(String testName) {
        logInfo("=== TEST START: " + testName + " ===");
    }
    
    public static void logTestEnd(String testName) {
        logInfo("=== TEST END: " + testName + " ===");
    }
    
    public static void logTestPass(String testName) {
        logInfo("✓ TEST PASS: " + testName);
    }
    
    public static void logTestFail(String testName, String reason) {
        logError("✗ TEST FAIL: " + testName + " - " + reason);
    }
    
    public static void logStep(String stepDescription) {
        logInfo("STEP: " + stepDescription);
    }
    
    public static void logAction(String action) {
        logInfo("ACTION: " + action);
    }
    
    public static void logVerification(String verification) {
        logInfo("VERIFY: " + verification);
    }
}
