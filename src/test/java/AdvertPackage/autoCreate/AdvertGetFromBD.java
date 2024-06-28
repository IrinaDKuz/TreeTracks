package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import org.testng.annotations.Test;

/***
  Берем сущность из БД
 */

public class AdvertGetFromBD {

    @Test
    public static void test() throws Exception {
        Advert advert = buildAdvert();
    }

    public static Advert buildAdvert() throws Exception {
        Advert advert = new Advert(1032);
        return advert;
    }
}
