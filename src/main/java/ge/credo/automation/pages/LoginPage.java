package ge.credo.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class
LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput =
            By.xpath("//input[@id='username']");

    private final By passwordInput =
            By.xpath("//input[@id='password']");

    private final By rememberMeCheckbox =
            By.xpath("//input[@id='rememberMe']");

    private final By loginButton =
            By.xpath("//button[@type='submit' and @aria-label='sign in']");

    private final By forgotPasswordLink =
            By.xpath("//a[@aria-label='lost credentials']");

    private final By showPasswordButton =
            By.xpath("//button[@aria-label='Show password']");

    private final By authenticationRejectionMessage =
            By.xpath("//*[normalize-space()='მონაცემები არასწორია']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterUsername(String username) {
        var input = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        input.clear();
        input.sendKeys(username);
    }

    public void enterPassword(String password) {
        var input = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        input.clear();
        input.sendKeys(password);
    }

    public String getUsernameValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput))
                .getDomProperty("value");
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        wait.ignoring(ElementClickInterceptedException.class).until(webDriver -> {
            webDriver.findElement(loginButton).click();
            return true;
        });
    }

    public boolean waitForUsernameValidationError() {
        return waitForInvalidState(usernameInput);
    }

    public boolean hasUsernameValidationError() {
        return hasInvalidState(usernameInput);
    }

    public boolean waitForPasswordValidationError() {
        return waitForInvalidState(
                passwordInput);
    }

    public boolean waitForAuthenticationRejection() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(authenticationRejectionMessage))
                .isDisplayed();
    }

    public boolean isLoginFormVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).isDisplayed()
                && wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).isDisplayed();
    }

    private boolean waitForInvalidState(By locator) {
        return wait.until(webDriver -> hasInvalidState(locator));
    }

    private boolean hasInvalidState(By locator) {
        String classAttribute = driver.findElement(locator).getDomAttribute("class");
        return classAttribute != null && classAttribute.contains("invalid");
    }
}
