package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import static AdvertPackage.autoCreate.AdvertBrowserBuilder.*;
import static Helper.Auth.*;

/***
 Тест создает сущность,
 рандомно заполняет табы
 - Primary Info,
 - Contact,


 */

public class AdvertCreate {

    @Test
    public static void test() throws Exception {
        // FirefoxDriver driver = (FirefoxDriver) getFireFoxDriver();
        ChromeDriver driver = (ChromeDriver) getChromeDriver();
        auth(driver, EMAIL_FULL_ACCESS, PASSWORD_FULL_ACCESS);

        Advert advert = new Advert();
       // Advert advertEdit = new Advert(999);

        advert.getAdvertPrimaryInfo().fillAdvertPrimaryInfoWithRandomDataForUI();
        advert.getAdvertContact().getFirst().fillAdvertContactWithRandomData();
        advert.getAdvertNotes().getFirst().fillAdvertNotesWithRandomData();
        advert.getAdvertRequisites().getFirst().fillAdvertRequisitesWithRandomData();
        advert.getAdvertPostback().fillAdvertPostbackWithRandomData();
        buildNewAdvert(advert, driver);

    }

    public static void buildNewAdvert(Advert advert, WebDriver driver) throws Exception {
        for (int i = 0; i < 10; i++) {
            addNewBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver);
            buildBrowserAdvertContact(advert.getAdvertContact(), driver);
            buildBrowserAdvertRequisites(advert.getAdvertRequisites(), driver);
            buildBrowserAdvertPostback(advert.getAdvertPostback(), driver);
            Thread.sleep(5000);
            buildBrowserAdvertNotes(advert.getAdvertNotes(), driver);
            buildBrowserAdvertFilter(advert, driver);
            Thread.sleep(50000);
        }
    }

    public static void buildEditAdvert(Advert advertEdit, WebDriver driver) throws Exception {
        editBrowserAdvertPrimaryInfo(advertEdit.getAdvertPrimaryInfo(), driver);
        buildBrowserAdvertContact(advertEdit.getAdvertContact(), driver);
        buildBrowserAdvertRequisites(advertEdit.getAdvertRequisites(), driver);
        buildBrowserAdvertPostback(advertEdit.getAdvertPostback(), driver);
        Thread.sleep(5000);
        buildBrowserAdvertNotes(advertEdit.getAdvertNotes(), driver);
        buildBrowserAdvertFilter(advertEdit, driver);
    }
}
