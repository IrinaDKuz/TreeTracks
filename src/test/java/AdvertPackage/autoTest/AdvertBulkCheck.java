package AdvertPackage.autoTest;

import AdvertPackage.entity.Advert;
import AdvertPackage.entity.AdvertPrimaryInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static API.Advert.AdvertAPI.advertBulkChange;
import static Helper.Adverts.*;
import static SQL.AdvertSQL.*;

/***
 Берем несколько рандомных сущностей из БД
 меняем параметры при помощи bulk API
 Проверяем новые сущности из БД и те, которые поменяли
 */

public class AdvertBulkCheck {
    public static int count = 6;

    @Test
    public static void test() throws Exception {
        for (int i = 0; i < count; i++) {

            List<Advert> advertsBeforeChangesFromBD = new ArrayList<>();
            List<Integer> advertsId = new ArrayList<>();
            for (int j = 0; j < new Random().nextInt(10); j++) {
                //  int id = Integer.parseInt(getRandomValueFromBD("id", "advert"));
                int id = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "900"));
                advertsId.add(id);
                Advert advert = new Advert(id);
                showAdvertData(advert);
                advertsBeforeChangesFromBD.add(advert);
            }

            List<Advert> advertsAfterChanges = bulkAdvertRandomChange(advertsBeforeChangesFromBD);

            List<Advert> advertsAfterChangesFromBD = new ArrayList<>();
            for (Integer advertId : advertsId) {
                Advert advert = new Advert(advertId);
                showAdvertData(advert);
                advertsAfterChangesFromBD.add(new Advert(advertId));
            }
            checkAdvertsChanges(advertsAfterChanges, advertsAfterChangesFromBD);
        }
    }

    private static void showAdvertData(Advert advert) {
        System.out.println("_____________________________");
        System.out.println("id:" + advert.getId());
        System.out.println("Status:" + advert.getAdvertPrimaryInfo().getStatus());
        System.out.println("Manager:" + advert.getAdvertPrimaryInfo().getManagerId());
        System.out.println("SalesManager:" + advert.getAdvertPrimaryInfo().getSalesManager());
        System.out.println("AccountManager:" + advert.getAdvertPrimaryInfo().getAccountManager());
        System.out.println("Tags:" + advert.getAdvertPrimaryInfo().getTag());
        System.out.println("Category:" + advert.getAdvertPrimaryInfo().getCategories());
        System.out.println("_____________________________");
    }

    private static void checkAdvertsChanges(List<Advert> advertsAfterChanges, List<Advert> advertsAfterChangesFromBD) {
        for (int i = 0; i < advertsAfterChanges.size(); i++) {
            AdvertPrimaryInfo advertPrimaryInfo = advertsAfterChanges.get(i).getAdvertPrimaryInfo();
            AdvertPrimaryInfo advertPrimaryInfo2 = advertsAfterChangesFromBD.get(i).getAdvertPrimaryInfo();

            Assert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfo2.getStatus());
            Assert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfo2.getManagerId());
            Assert.assertEquals(advertPrimaryInfo.getAccountManager(), advertPrimaryInfo2.getAccountManager());
            Assert.assertEquals(advertPrimaryInfo.getSalesManager(), advertPrimaryInfo2.getSalesManager());
            Collections.sort(advertPrimaryInfo.getTag());
            Collections.sort(advertPrimaryInfo2.getTag());
            System.out.println(advertPrimaryInfo.getTag());
            System.out.println(advertPrimaryInfo2.getTag());
            Assert.assertEquals(advertPrimaryInfo.getTag(), advertPrimaryInfo2.getTag());
            Collections.sort(advertPrimaryInfo.getCategories());
            Collections.sort(advertPrimaryInfo2.getCategories());
            System.out.println("!getCategories " + advertPrimaryInfo.getCategories());
            System.out.println("!getCategories " + advertPrimaryInfo2.getCategories());
            Assert.assertEquals(advertPrimaryInfo.getCategories(), advertPrimaryInfo2.getCategories());
        }
    }

    public static List<Advert> bulkAdvertRandomChange(List<Advert> adverts) throws Exception {
        Map<String, List<String>> mapOfArrays = new HashMap<>();

        // как будет работать если захотим добавить тег, который уже есть у одной из сущностей
        List<String> tagAdd = generateNameList(3, COMPANY_WORDS);
        List<String> tagRemove = generateNameList(3, COMPANY_WORDS);
        List<String> categoryAdd = generateCategoryList(3);
        List<String> categoryRemove = generateCategoryList(3);

        mapOfArrays.put("tagAdd", tagAdd);
        mapOfArrays.put("tagRemove", tagRemove);
        mapOfArrays.put("categoryAdd", categoryAdd);
        mapOfArrays.put("categoryRemove", categoryRemove);

        List<String> ids = new ArrayList<>();
        adverts.forEach(advert -> ids.add(String.valueOf(advert.getId())));
        mapOfArrays.put("ids", ids);

        Map<String, String> mapOfStrings = new HashMap<>();
        mapOfStrings.put("status", getRandomValue(STATUS_MAP).toString().toLowerCase().replace(" ", "_"));
        mapOfStrings.put("manager", getRandomValueFromBD("id", "admin"));
        mapOfStrings.put("accountManager", getRandomValueFromBD("id", "admin"));
        mapOfStrings.put("salesManager", getRandomValueFromBD("id", "admin"));

        advertBulkChange(mapOfArrays, mapOfStrings);

        adverts.forEach(advert -> {
            advert.getAdvertPrimaryInfo().addTag(tagAdd);
            advert.getAdvertPrimaryInfo().deleteTag(tagRemove);
            advert.getAdvertPrimaryInfo().setStatus(mapOfStrings.get("status"));
            advert.getAdvertPrimaryInfo().setManagerId(mapOfStrings.get("manager"));
            advert.getAdvertPrimaryInfo().setAccountManager(mapOfStrings.get("accountManager"));
            advert.getAdvertPrimaryInfo().setSalesManager(mapOfStrings.get("salesManager"));
            advert.getAdvertPrimaryInfo().addCategories(categoryAdd);
            advert.getAdvertPrimaryInfo().deleteCategories(categoryRemove);
        });

        return adverts;
    }
}



