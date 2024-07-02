package AdminPackage.autoCreate;

import AdminPackage.entity.Admin;
import AdminPackage.entity.AdminPermissions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static AdminPackage.autoCreate.AdminPermissionCheckAdvert.tabNotHavePermissionCheck;
import static AdminPackage.autoCreate.AdminPermissionCheckAdvert.tabOpenCheck;
import static AdminPackage.entity.AdminPermissionsAPI.initializeJsonAdminPermissions;
import static Helper.ActionsClass.isElementPresent;
import static Helper.ActionsClass.waitAndClick;
import static Helper.Auth.*;
import static Helper.Buttons.DELETE;
import static Helper.Buttons.EDIT;
import static Helper.MenuPage.ADVERTISERS;
import static Helper.MenuPage.menuItemClick;
import static Helper.Path.contain;

/***
 Тест проверяет доступы
 */

public class AdminPermissionCheck {

    @Test
    public static void test() throws Exception {
        WebDriver driver = getDriver();
        auth(driver, EMAIL_ADMIN, PASSWORD_ADMIN);
        Admin admin = new Admin();

        checkAdminPermissions(driver, admin);
        // initializeJsonAdminPermissions(admin.getAdminPermissions());
        // changeAdminPermission();
    }


    public static void checkAdminPermissions(WebDriver driver, Admin admin) throws Exception {
        //
        // TODO вынести в отдельный метод
        menuItemClick(ADVERTISERS, driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber + "]"), driver);

        checkTabPermission(driver, "Contacts", admin.getAdminPermissions().getAdvertContactPermission().getView());
        if (admin.getAdminPermissions().getAdvertContactPermission().getView()) {
            checkCreateEditDeletePermission(driver, "Contacts", "Add Contact Person",
                    admin.getAdminPermissions().getAdvertContactPermission());
        }

        checkTabPermission(driver, "Requisites", admin.getAdminPermissions().getAdvertRequisitesPermission().getView());

        checkTabPermission(driver, "Postback", admin.getAdminPermissions().getAdvertPostbackPermission().getView());
        // checkTabPermission(driver, "Document", admin.getAdminPermissions().getAdvertDocumentPermission().getView());
        checkTabPermission(driver, "Note", admin.getAdminPermissions().getAdvertNotePermission().getView());
        if (admin.getAdminPermissions().getAdvertNotePermission().getView()) {
            //checkCreateEditDeletePermissionNote(driver, "Note", " Add new note",
              //      admin.getAdminPermissions().getAdvertNotePermission());
        }


        // TODO if есть view

      //  checkCreateEditDeletePermission(driver, "Note", " Add new note", admin.getAdminPermissions().getAdvertNotePermission().getCreate());
       // checkCreatePermission(driver, "Contact", "Contact", admin.getAdminPermissions().getAdvertContactPermission().getCreate());
      //  checkCreateEditDeletePermission(driver, "Requisites", "+ Add New Requisite", admin.getAdminPermissions().getAdvertRequisitesPermission().getCreate());

    }

    private static void checkTabPermission(WebDriver driver, String tabName, boolean hasPermission) {
        if (hasPermission) {
            tabOpenCheck(driver, tabName);
            // TODO: добавить проверку всех элементов на вкладке
            // isElementPresent(driver, By.xpath("//label[contains(text(), 'GEO')]/parent::div//input"));
        } else {
            tabNotHavePermissionCheck(driver, tabName);
        }
    }

    private static void checkCreateEditDeletePermission(WebDriver driver, String tabName, String buttonName, AdminPermissions.Access access) {
        if (access.getCreate()) {
            driver.findElement(contain("a", tabName)).click();
            Assert.assertTrue(isElementPresent(driver, contain("button", buttonName)), "Кнопки " + buttonName + " нет");
        } else {
            driver.findElement(contain("a", tabName)).click();
            Assert.assertFalse(isElementPresent(driver, contain("button", buttonName)),  "Кнопка " + buttonName + " есть");
        }
        if (access.getEdit()) {
            driver.findElement(contain("a", tabName)).click();
            Assert.assertTrue(isElementPresent(driver, contain("button", EDIT)), "Кнопки " + EDIT + " нет");
        } else {
            driver.findElement(contain("a", tabName)).click();
            Assert.assertFalse(isElementPresent(driver, contain("button", EDIT)),  "Кнопка " + EDIT + " есть");
        }

        if (access.getDelete()) {
            // if () есть сущность
            driver.findElement(contain("a", tabName)).click();
            Assert.assertTrue(isElementPresent(driver, contain("button", DELETE)), "Кнопка " + DELETE + " нет");
        } else {
            driver.findElement(contain("a", DELETE)).click();
            Assert.assertFalse(isElementPresent(driver, contain("button", DELETE)),  "Кнопка " + DELETE + " есть");
        }


    }
}

