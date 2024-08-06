package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertNotes;
import AdvertPackage.entity.AdvertRequisites;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import static AdvertPackage.autoCreate.AdvertBrowserBuilder.*;
import static Helper.Adverts.showAdvertPrimaryInfoInformation;
import static Helper.Auth.*;

/***
 Тест создает сущность,
 рандомно заполняет табы
 - Primary Info,
 - Contact + Messengers,
 - Requisites,
 - Postback,
 - Notes,
 - Filter

 TODO: Проверки и следить edit/add
 */

public class AdvertCreate {

    @Test
    public static void test() throws Exception {
        FirefoxDriver driver = (FirefoxDriver) getFireFoxDriver();
       // ChromeDriver driver = (ChromeDriver) getChromeDriver();
        auth(driver, EMAIL_FULL_ACCESS, PASSWORD_FULL_ACCESS);
        Advert advert = new Advert();
        advert.getAdvertPrimaryInfo().fillAdvertPrimaryInfoWithRandomData();
        showAdvertPrimaryInfoInformation(advert.getAdvertPrimaryInfo());
        for (AdvertContact advertContact : advert.getAdvertContact())
            advertContact.fillAdvertContactWithRandomData();
        for (AdvertNotes advertNotes : advert.getAdvertNotes())
            advertNotes.fillAdvertNotesWithRandomData();
        for (AdvertRequisites advertRequisites : advert.getAdvertRequisites())
            advertRequisites.fillAdvertRequisitesWithRandomData();
        advert.getAdvertPostback().fillAdvertPostbackWithRandomData();
        buildNewAdvert(advert, driver);
    }

    public static void buildNewAdvert(Advert advert, WebDriver driver) throws Exception {
        // for (int i = 0; i < 10; i++) {
       // editBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver); // для тестов
        addNewBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver);
        addBrowserAdvertContact(advert.getAdvertContact(), driver);
        addBrowserAdvertRequisites(advert.getAdvertRequisites(), driver);
        addBrowserAdvertPostback(advert.getAdvertPostback(), driver);
        Thread.sleep(5000);
        buildBrowserAdvertNotes(advert.getAdvertNotes(), driver);
        buildBrowserAdvertFilter(advert, driver);
        Thread.sleep(50000);
        //  }
    }
}
