package ge.credo.automation.tests;

import ge.credo.automation.base.BaseTest;
import ge.credo.automation.components.Language;
import ge.credo.automation.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Negative authentication")
public class NegativeAuthenticationTest extends BaseTest {
    private static final String LOGIN_PAGE_URL = "https://mycredo.ge/landing/main/authorization";
    private static final String INCORRECT_CREDENTIALS_SCENARIO =
            "incorrect username and incorrect password";
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void createPageObjects() {
        loginPage = new LoginPage(driver);
    }

    @DataProvider(name = "negativeLoginCases")
    public Object[][] negativeLoginCases() {
        return new Object[][]{
                {Language.GEORGIAN, "Georgian username with empty password", Language.GEORGIAN.getUsername(), ""},
                {Language.MEGRELIAN, "Megrelian username with empty password", Language.MEGRELIAN.getUsername(), ""},
                {Language.SVAN, "Svan username with empty password", Language.SVAN.getUsername(), ""},
                {null, "both username and password empty", "", ""},
                {null, "username empty with non-empty password", "", RandomStringUtils.secure().nextAlphanumeric(12)},
                {null, "whitespace-only username with empty password", "   ", ""},
                {null, INCORRECT_CREDENTIALS_SCENARIO, RandomStringUtils.secure().nextAlphanumeric(16),
                        RandomStringUtils.secure().nextAlphanumeric(16)}
        };
    }

    @Test(dataProvider = "negativeLoginCases")
    public void rejectsClientSideInvalidCredentials(
            Language language,
            String scenarioName,
            String username,
            String password) {
        SoftAssert softly = new SoftAssert();
        loginPage.enterUsername(username);
        if (language != null) {
            softly.assertEquals(loginPage.getUsernameValue(), username,
                    "Username input should contain the supplied " + language.getDisplayName() + " value");
        }
        submitPasswordAndLogin(scenarioName, password);

        if (scenarioName.equals(INCORRECT_CREDENTIALS_SCENARIO)) {
            softly.assertTrue(loginPage.waitForAuthenticationRejection(),
                    "Incorrect credentials should show the verified authentication rejection");
            softly.assertTrue(loginPage.isLoginFormVisible(),
                    "Authentication rejection should leave the login form available to an unauthenticated user");
        } else {
            softly.assertEquals(driver.getCurrentUrl(), LOGIN_PAGE_URL,
                    "Client-side validation should keep the user on the login page");
        }

        if (username.isEmpty()) {
            softly.assertTrue(loginPage.waitForUsernameValidationError(),
                    "Username should have client-side validation for: " + scenarioName);
        } else if (username.isBlank()) {
            softly.assertEquals(loginPage.getUsernameValue(), username,
                    "The UI should preserve the whitespace-only username value");
            softly.assertFalse(loginPage.hasUsernameValidationError(),
                    "Current MyCredo validation treats a whitespace-only username as non-empty");
        }
        if (password.isEmpty()) {
            softly.assertTrue(loginPage.waitForPasswordValidationError(),
                    "Password should have client-side validation for: " + scenarioName);
        }
        softly.assertAll();
    }

    @Step("Enter password and submit negative authentication scenario: {scenarioName}")
    public void submitPasswordAndLogin(String scenarioName, String password) {
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }
}
