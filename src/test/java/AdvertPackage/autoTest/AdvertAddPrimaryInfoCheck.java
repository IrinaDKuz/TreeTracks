package AdvertPackage.autoTest;

import AdvertPackage.entity.Advert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static AdvertPackage.autoCreate.AdvertBrowserBuilder.*;
import static AdvertPackage.autoCreate.AdvertBrowserBuilder.buildBrowserAdvertFilter;
import static Helper.Auth.*;
import static Helper.Auth.PASSWORD_ADMIN;

/***
 Тест создает новую сущность и проверяет все алерты
 */

public class AdvertAddPrimaryInfoCheck {
    @Test
    public static void test() throws Exception {
        WebDriver driver = getDriver();
        auth(driver, EMAIL_ADMIN, PASSWORD_ADMIN);
        Advert advert = buildAdvert(driver);
    }

    public static Advert buildAdvert(WebDriver driver) throws Exception {
        Advert advert = null;
        for (int i = 0; i < 5 ; i++) {
            advert = new Advert();
            addNewBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver);
            buildBrowserAdvertContact(advert.getAdvertContact(), driver);
            buildBrowserAdvertRequisites(advert.getAdvertRequisites(), driver);
            buildBrowserAdvertPostback(advert.getAdvertPostback(), driver);
            Thread.sleep(5000);
            buildBrowserAdvertNotes(advert.getAdvertNotes(), driver);
            buildBrowserAdvertFilter(advert, driver);
        }
        return advert;

    }

}