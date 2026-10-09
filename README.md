# Credo Business Internet Banking UI Automation

Java 17 Selenium automation for negative authentication checks on the MyCredo business internet banking login page. The project uses Maven, TestNG, WebDriverManager, Apache Commons Lang, and Allure.

## Technology stack

- Java 17
- Selenium WebDriver and ChromeDriver
- TestNG
- Maven Surefire
- Bonigarcia WebDriverManager
- Apache Commons Lang `RandomStringUtils`
- Allure TestNG reporting

## Negative authentication coverage

`NegativeAuthenticationTest` has one TestNG `@DataProvider` and one parameterized `@Test`. Each DataProvider row receives fresh browser setup and cleanup through TestNG's `@BeforeMethod` and `@AfterMethod`.

The suite covers seven scenarios:

1. Georgian username `ნინი` with an empty password.
2. Megrelian username `მადიშა` with an empty password.
3. Svan username `თარაშ` with an empty password.
4. Both username and password empty.
5. Empty username with a randomly generated non-empty password.
6. Whitespace-only username with an empty password.
7. Randomly generated incorrect username and password.

Georgian, Megrelian, and Svan describe the **username test data**. They do not switch the MyCredo interface language and the tests do not use the UI language dropdown.

The incorrect-credentials case verifies the live, observed rejection message `მონაცემები არასწორია` and checks that the login form remains visible, rather than assuming that the URL does not change.

## QA observation: whitespace username

For a username containing only spaces and an empty password, the currently observed UI preserves the spaces and does not mark the username field with its existing `invalid` state. The password is marked invalid. This is a recorded application behavior, not an automatically confirmed product bug; its acceptability depends on the product's validation requirements.

## Project structure

```text
src/main/java/ge/credo/automation/
  components/
    Language.java                 username-data categories and supplied values
    LanguageSelector.java         reusable language-menu component
  pages/
    LoginPage.java                login locators, actions, and explicit waits
src/test/java/ge/credo/automation/
  base/
    BaseTest.java                 Chrome setup and safe browser cleanup
  tests/
    NegativeAuthenticationTest.java
src/test/resources/
  allure.properties               Allure result-directory configuration
pom.xml                           dependencies, Java 17, Compiler and Surefire
testng.xml                        explicitly registers NegativeAuthenticationTest
```

## Design notes

- **Page Object Model:** `LoginPage` owns login locators and browser interactions; tests express scenarios and assertions.
- **BaseTest:** starts Chrome through WebDriverManager, opens the login page, and always calls `driver.quit()` after each test invocation.
- **Language:** stores the three manually supplied username-data categories and values.
- **LanguageSelector:** remains a reusable component for the application's language menu, but is deliberately not used by these username-data tests.
- **Assertions and data:** tests use `SoftAssert` followed by `assertAll()`. `RandomStringUtils.secure().nextAlphanumeric()` creates the non-empty and incorrect credential values.
- **Synchronization and reporting:** the framework uses explicit `WebDriverWait` conditions only—no implicit waits or `Thread.sleep()`—and uses Allure `@Feature` and `@Step` annotations.

## Prerequisites

- Java 17 or later
- Google Chrome
- Maven 3.9+ or IntelliJ IDEA's bundled Maven
- Network access to MyCredo and Maven Central

## Run tests

From a terminal:

```bash
mvn clean test
```

In IntelliJ IDEA:

1. Open the Maven tool window.
2. Expand **Lifecycle**.
3. Run **clean**, then **test**.

Alternatively, right-click `NegativeAuthenticationTest.java` and choose **Run**. `testng.xml` is used by Maven Surefire and explicitly includes the final test class.

## Allure reports

The Allure adapter writes result files to `target/allure-results`.

With the Allure command-line tool installed, open a local report with:

```bash
allure serve target/allure-results
```

## Latest verified execution

`mvn clean test` completed with:

- **7 passed**
- **0 failed**
- **0 skipped**
