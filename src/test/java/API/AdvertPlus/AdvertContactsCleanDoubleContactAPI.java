package API.AdvertPlus;

import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static API.Advert.AdvertContactsAPI.contactsDelete;
import static SQL.AdvertSQL.getArrayFromBD;
import static SQL.AdvertSQL.getValueFromBDWhere;

/***
 Тест удаляет все кантакты, которые есть в таблице advert_contact_double
 */

public class AdvertContactsCleanDoubleContactAPI {
    public static int advertContactId;
    public static int advertId;

    @Test
    public static void test() {
        try {
            List<String> ids = getArrayFromBD("advert1", "advert_contact_double");
            for (String id : ids) {
                try {
                    advertId = Integer.parseInt(id);
                    advertContactId = Integer.parseInt(getValueFromBDWhere("id", "advert_contact",
                            "advert_id", String.valueOf(advertId)));
                    System.out.println(advertId);
                    System.out.println(advertContactId);
                    contactsDelete();
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}