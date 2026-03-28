# End-to-End UI Test Automation Framework - SwagLabs

A comprehensive test automation framework built with Selenium WebDriver for end-to-end UI testing of the SwagLabs e-commerce platform.

## 🚀 Features

- **End-to-End Test Coverage** - Complete user journey testing from login to checkout
- **Page Object Model (POM)** - Clean, maintainable, and reusable page classes
- **Data-Driven Testing** - Multiple test datasets using TestNG @DataProvider
- **Professional Reporting** - ExtentReports with HTML dashboards and screenshots
- **Explicit Waits** - Reliable element interaction with WebDriverWait
- **Failure Screenshots** - Automatic capture on test failures

## 🛠️ Technical Stack

- **Java** - Programming language
- **Selenium WebDriver 4.27.0** - Browser automation
- **TestNG 7.9.0** - Test framework and assertions
- **ExtentReports 5.1.1** - Professional HTML reporting
- **Maven** - Build management and dependencies

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.6 or higher
- Chrome Browser

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SireeshaReddy826/swaglabs-selenium-automation.git
   cd swaglabs-selenium-automation
   ```

2. **Run tests**
   ```bash
   mvn test
   ```

### Configuration

**Run tests with different environments:**
```bash
# Default (SwagLabs demo)
mvn test

# Custom environment
mvn test -DbaseUrl=https://staging.saucedemo.com/
```

## 🧪 Test Coverage

### End-to-End Workflows
- ✅ User login and authentication
- ✅ Product browsing and filtering
- ✅ Shopping cart management
- ✅ Multi-step checkout process
- ✅ Payment confirmation
- ✅ Order completion verification

### Functional Testing
- ✅ Product sorting (A-Z, Z-A, Price Low-High, Price High-Low)
- ✅ Price calculation validation (subtotal, tax, total)
- ✅ Negative test scenarios (invalid login, error handling)
- ✅ Data-driven testing with multiple user datasets

## 📊 Reporting

After test execution, HTML reports are generated in:
```
test-output/ExtentReport.html
```

## 🎯 Key Highlights

### Professional Practices
- **Clean Code Principles** - Maintainable and readable code
- **Design Patterns** - Page Object Model for scalability
- **Error Handling** - Comprehensive exception management
- **Test Isolation** - Independent test execution
- **Resource Management** - Proper driver cleanup

---

**Built with ❤️ for professional test automation demonstration**
