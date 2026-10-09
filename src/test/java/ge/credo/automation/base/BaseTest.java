package ge.credo.automation.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://mycredo.ge/landing/main/authorization");
        } catch (RuntimeException exception) {
            if (driver != null) {
                driver.quit();
            }
            throw exception;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
