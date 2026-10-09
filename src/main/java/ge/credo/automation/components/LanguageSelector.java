package ge.credo.automation.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LanguageSelector {
    private final WebDriverWait wait;

    private final By languageMenuButton =
            By.xpath("//button[.//app-icon[@svgicon='language']]");

    public LanguageSelector(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectByVisibleText(String languageLabel) {
        wait.until(ExpectedConditions.elementToBeClickable(languageMenuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(languageOption(languageLabel))).click();
    }

    private By languageOption(String languageLabel) {
        return By.xpath("//*[normalize-space()=" + xpathLiteral(languageLabel) + "]");
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }

        return "\"" + value + "\"";
    }
}
