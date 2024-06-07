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
        Advert advert = new Advert();
        buildBrowserAdvertPrimaryInfo(advert.getAdvertPrimaryInfo(), driver);
        buildBrowserAdvertContact(advert.getAdvertContact(), driver);
        return advert;
    }
}
