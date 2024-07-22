package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static AdvertPackage.autoCreate.AdvertBrowserBuilder.*;
import static Helper.Auth.*;

/***
 Тест рандомно выбирает Адверта у которого есть сущность,
 и редактирует ее
 - Primary Info,
 - Contact +
 - Requisites,
 TODO: Postback, Messenger,
 - Notes,
 */

public class AdvertEdit {

    @Test
    public static void test() throws Exception {
        // FirefoxDriver driver = (FirefoxDriver) getFireFoxDriver();
        ChromeDriver driver = (ChromeDriver) getChromeDriver();
        auth(driver, EMAIL_TEST, PASSWORD_TEST);
        Advert advert = new Advert();
        advert.getAdvertPrimaryInfo().fillAdvertPrimaryInfoWithRandomData();
        advert.getAdvertContact().getFirst().fillAdvertContactWithRandomData();
        advert.getAdvertRequisites().getFirst().fillAdvertRequisitesWithRandomData();
        advert.getAdvertPostback().fillAdvertPostbackWithRandomData();
        advert.getAdvertNotes().getFirst().fillAdvertNotesWithRandomData();
        editAdvert(advert, driver);
    }

    public static void editAdvert(Advert advertEdit, WebDriver driver) throws Exception {
        editBrowserAdvertPrimaryInfo(advertEdit.getAdvertPrimaryInfo(), driver);
        editBrowserAdvertContact(advertEdit.getAdvertContact().getFirst(), driver);
        editBrowserAdvertRequisites(advertEdit.getAdvertRequisites().getFirst(), driver);
        //editBrowserAdvertPostback(advertEdit.getAdvertPostback(), driver);
        editBrowserAdvertNotes(advertEdit.getAdvertNotes().getFirst(), driver);
    }
}
