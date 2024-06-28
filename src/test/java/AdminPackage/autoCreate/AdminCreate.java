package AdminPackage.autoCreate;

import AdminPackage.entity.Admin;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static Helper.Auth.*;

/***
 Тест создает сущность, заполняет табы
 - AdvertCreate
 - Primary Info, Contact
 */

public class AdminCreate {

    @Test
    public static void test() throws Exception {
        ChromeDriver driver = getDriver();
        //auth(driver);
        Admin admin = buildAdmin(driver);
    }

    public static Admin buildAdmin(ChromeDriver driver) throws Exception {
        Admin admin = new Admin();

       // addNewBrowserAdminGeneral(admin.getAdminGeneral(), driver);
       // buildBrowserAdvertContact(advert.getAdvertContact(), driver);
        return admin;
    }
}
