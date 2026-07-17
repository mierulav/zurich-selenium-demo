# Zurich QA Automation Demo — Selenium + Java + TestNG

# Target app:
https://www.saucedemo.com/

# Run all tests (desktop + mobile) with ExtentReports HTML output
- mvn clean test

# Run specific test
- mvn clean test -DsuiteXmlFile=src/test/resources/web-test.xmss=l

# Reports:
- ExtentReports HTML → `test-output/ExtentReport_<timestamp>.html`

# DB Validations:
- Uses dockerized MySQL DB (no external DB server needed)
- To spin up MySQL DB, run `docker-compose up -d` in the project root (requires Docker installed)
- Need credentials which is stores in .env (if needed please contact amirul saddam)

# Email Report:
- mailtrap.io -> Need credentials which is stores in .env (if needed please contact amirul saddam)

## Architecture in a glance
base/BasePage.java → abstract WebDriver lifecycle (thread-safe via ThreadLocal)
utilities/DriverFactory.java → thread-safe browser creation
utilities/LocatorFactory.java → returns the correct locator-strategy implements per Platform
locators/*Locators.java → interfaces (contracts)
locators/desktop|mobile/* → concrete locator strategies
pages/Common.java → abstract, shared element operations (click/type/wait/screenshot)
pages/* -> operations for each page (login, products, cart, checkout, etc)
utilities/DBUtils.java → JDBC/mySQL - SQL scripting for expected-value data validation
utilities/CsvDataProvider.java → TestNG @DataProvider reading from CSV
listeners/* -> ExtentReports + retry-on-failure per @Test + screenshot-on-failure
listeners/TestListener.java → ExtentReports + screenshot-on-failure
testcases/* -> TestNG @Test classes for each page/feature
