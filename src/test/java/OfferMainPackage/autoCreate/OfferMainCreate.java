package OfferMainPackage.autoCreate;

import AdminPackage.entity.Admin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import static Helper.Auth.*;

/***
 Тест создает сущность, заполняет табы
 - AdvertCreate
 - Primary Info, Contact
 */

public class OfferMainCreate {

    @Test
    public static void test() throws Exception {
        FirefoxDriver driver = (FirefoxDriver) getDriver();
        //auth(driver);
        Admin admin = buildAdmin(driver);
    }

    public static Admin buildAdmin(WebDriver driver) throws Exception {
        Admin admin = new Admin();
       // addNewBrowserAdminGeneral(admin.getAdminGeneral(), driver);
       // buildBrowserAdvertContact(advert.getAdvertContact(), driver);
        return admin;
    }
}
