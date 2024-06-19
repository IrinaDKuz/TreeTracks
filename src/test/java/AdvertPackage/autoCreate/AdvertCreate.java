package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static AdvertPackage.autoCreate.AdvertBuilder.*;
import static Helper.Auth.*;

/***
 Тест создает сущность, заполняет табы
 - AdvertCreate
 - Primary Info, Contact
 */

public class AdvertCreate {

    @Test
    public static void test() throws Exception {
        ChromeDriver driver = getDriver();
        auth(driver);
        Advert advert = buildAdvert(driver);
    }

    public static Advert buildAdvert(ChromeDriver driver) throws Exception {
        Advert advert = null;
        for (int i = 0; i < 5 ; i++) {
            advert = new Advert();
            editBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver);
            //buildBrowserAdvertContact(advert.getAdvertContact(), driver);
            //buildBrowserAdvertRequisites(advert.getAdvertRequisites(), driver);
            buildBrowserAdvertPostback(advert.getAdvertPostback(), driver);
            Thread.sleep(5000);
            // buildBrowserAdvertNotes(advert.getAdvertNotes(), driver);
            // buildBrowserAdvertFilter(advert, driver);
        }
            return advert;
        
    }
}
