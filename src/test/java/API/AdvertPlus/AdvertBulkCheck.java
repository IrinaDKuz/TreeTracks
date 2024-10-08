package API.AdvertPlus;

import AdvertPackage.entity.Advert;
import AdvertPackage.entity.AdvertPrimaryInfo;
import io.qameta.allure.Allure;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static API.AdvertPlus.AdvertBulkAPI.advertBulkChange;
import static Helper.ActionsClass.getSomeValuesFromArray;
import static Helper.Adverts.*;
import static Helper.AllureHelper.CHECK;
import static Helper.AllureHelper.DATA;
import static Helper.Auth.authApi;
import static Helper.GeoAndLang.getRandomValue;
import static SQL.AdvertSQL.*;
import static SQL.AdvertSQL.getSomeValuesFromBD;

/***
 Берем несколько рандомных сущностей из БД
 меняем параметры при помощи bulk API
 Проверяем новые сущности из БД и те, которые поменяли
 */

public class AdvertBulkCheck {
    public static int count = 3;

    @Test
    public static void test() throws Exception {
        authApi(103);

        for (int i = 0; i < count; i++) {
            List<Advert> advertsBeforeChangesFromBD = new ArrayList<>();
            List<Integer> advertsId = new ArrayList<>();
            for (int j = 0; j < new Random().nextInt(10); j++) {
                //  int id = Integer.parseInt(getRandomValueFromBD("id", "advert"));
                int id = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert",
                        "id", "900"));
                advertsId.add(id);
                Advert advert = new Advert(id);
                showAdvertData(advert);
                advertsBeforeChangesFromBD.add(advert);
            }
            Allure.step("id Адвертов, которых планируем менять: " + advertsId);
            List<Advert> advertsAfterChanges = bulkAdvertRandomChange(advertsBeforeChangesFromBD);

            List<Advert> advertsAfterChangesFromBD = new ArrayList<>();
            for (Integer advertId : advertsId) {
                Advert advert = new Advert(advertId);
                showAdvertData(advert);
                advertsAfterChangesFromBD.add(advert);
            }
            checkAdvertsChanges(advertsAfterChanges, advertsAfterChangesFromBD);
        }
    }

    private static void showAdvertData(Advert advert) {
        System.out.println("_____________________________");
        System.out.println("id:" + advert.getId());
        System.out.println("Status:" + advert.getAdvertPrimaryInfo().getStatus());
        System.out.println("Manager:" + advert.getAdvertPrimaryInfo().getManagerId());
        System.out.println("SalesManager:" + advert.getAdvertPrimaryInfo().getSalesManagerId());
        System.out.println("AccountManager:" + advert.getAdvertPrimaryInfo().getAccountManagerId());
        System.out.println("Tags:" + advert.getAdvertPrimaryInfo().getTagId());
        System.out.println("Category:" + advert.getAdvertPrimaryInfo().getCategoriesId());
        System.out.println("_____________________________");
    }

    private static void checkAdvertsChanges(List<Advert> advertsAfterChanges, List<Advert> advertsAfterChangesFromBD) {
        SoftAssert softAssert = new SoftAssert();
        Allure.step(CHECK);
        for (int i = 0; i < advertsAfterChanges.size(); i++) {
            AdvertPrimaryInfo advertPrimaryInfo = advertsAfterChanges.get(i).getAdvertPrimaryInfo();
            AdvertPrimaryInfo advertPrimaryInfo2 = advertsAfterChangesFromBD.get(i).getAdvertPrimaryInfo();
            softAssert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfo2.getStatus());
            softAssert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfo2.getManagerId());
            softAssert.assertEquals(advertPrimaryInfo.getAccountManagerId(), advertPrimaryInfo2.getAccountManagerId());
            softAssert.assertEquals(advertPrimaryInfo.getSalesManagerId(), advertPrimaryInfo2.getSalesManagerId());
            Collections.sort(advertPrimaryInfo.getTagId());
            Collections.sort(advertPrimaryInfo2.getTagId());
            softAssert.assertEquals(advertPrimaryInfo.getTagId(), advertPrimaryInfo2.getTagId());
            softAssert.assertEquals(advertPrimaryInfo.getCategoriesId(), advertPrimaryInfo2.getCategoriesId());
        }
        softAssert.assertAll();
    }

    public static List<Advert> bulkAdvertRandomChange(List<Advert> adverts) throws Exception {
        Map<String, List<String>> mapOfArrays = new HashMap<>();

        List<String> tag = getArrayFromBD("id", "advert_tag");
        List<String> tagAdd = getSomeValuesFromBD("id", "advert_tag", 2);
        tag.removeAll(tagAdd);
        List<String> tagRemove = getSomeValuesFromArray(tag, 2);

        List<String> category = getArrayFromBDWhere("id", "category",
                "lang", "general");
        List<String> categoryAdd = getSomeValuesFromBDWhere("id", "category", "lang",
                "general", 2);
        category.removeAll(categoryAdd);
        List<String> categoryRemove = getSomeValuesFromArray(category, 2);

        mapOfArrays.put("categoryRemove", categoryRemove);
        mapOfArrays.put("tagAdd", tagAdd);
        mapOfArrays.put("tagRemove", tagRemove);
        mapOfArrays.put("categoryAdd", categoryAdd);

        List<String> ids = new ArrayList<>();
        adverts.forEach(advert -> ids.add(String.valueOf(advert.getId())));
        mapOfArrays.put("ids", ids);

        Map<String, String> mapOfStrings = new HashMap<>();
        mapOfStrings.put("status", getRandomValue(ADVERT_STATUS_MAP).toLowerCase().replace(" ", "_"));
        mapOfStrings.put("manager", getRandomValueFromBD("id", "admin"));
        mapOfStrings.put("accountManager", getRandomValueFromBD("id", "admin"));
        mapOfStrings.put("salesManager", getRandomValueFromBD("id", "admin"));

        advertBulkChange(mapOfArrays, mapOfStrings);

        adverts.forEach(advert -> {
            advert.getAdvertPrimaryInfo().addTagId(tagAdd.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList()));
            advert.getAdvertPrimaryInfo().deleteTagId(tagRemove.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet()));
            advert.getAdvertPrimaryInfo().setStatus(mapOfStrings.get("status"));
            advert.getAdvertPrimaryInfo().setManagerId(mapOfStrings.get("manager"));
            advert.getAdvertPrimaryInfo().setAccountManagerId(mapOfStrings.get("accountManager"));
            advert.getAdvertPrimaryInfo().setSalesManagerId(mapOfStrings.get("salesManager"));
            advert.getAdvertPrimaryInfo().addCategories(categoryAdd.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet()));
            advert.getAdvertPrimaryInfo().deleteCategories(categoryRemove.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet()));
        });
        return adverts;
    }
}



