package AdminPackage.autoCreate;

import AdminPackage.entity.Admin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static Helper.ActionsClass.isElementPresent;
import static Helper.ActionsClass.waitAndClick;
import static Helper.Auth.*;
import static Helper.MenuPage.ADVERTISERS;
import static Helper.MenuPage.menuItemClick;
import static Helper.Path.contain;

/***
 Тест проверяет доступы
 */

public class AdminPermissionCheckAdvert {

    public static void tabOpenCheck(WebDriver driver, String tabName) {
        // Проверяем, что вкладка есть
        Assert.assertTrue(isElementPresent(driver, contain("a", tabName)), "Вкладка " + tabName + " есть");
        driver.findElement(contain("a", tabName)).click();

        // TODO проверять элементы на странице
       /* isElementPresent(driver,
                By.xpath("//label[contains(text(), 'GEO')]/parent::div//input"));*/
    }

    public static void tabNotHavePermissionCheck(WebDriver driver, String tabName) {
        Assert.assertFalse(isElementPresent(driver, contain("a", tabName)), "Вкладки " + tabName + " нет");
        // TODO проверять конкретного адверта!!!
        driver.get("https://admin.3tracks.link/advertisers/" + 1037 + "/" + tabName.toLowerCase());
       // TODO переделать
        Assert.assertTrue(isElementPresent(driver, By.xpath("//img[@src='/Error404.svg']")),
                "Вкладка " + tabName + " есть");
    }
}
