
package API.Admin.ContentFilter;

import AdminPackage.entity.AdminContentFilterForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.sql.SQLException;
import java.util.*;

import static API.Admin.ContentFilter.ContentFilterAdvert.getRandomFilter;
import static API.Helper.*;
import static Helper.AllureHelper.GET_RESPONSE;
import static Helper.Auth.authKeyAdmin;
import static Helper.GeoAndLang.GEO_MAP;
import static Helper.GeoAndLang.getRandomKeys;
import static Helper.Offers.OFFER_PRIVACY_LEVEL;
import static Helper.Offers.OFFER_STATUS_MAP;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест проверяет работу Offer Content Filter
 TODO: 90% DONE
 */


public class ContentFilterOffer {

    static List<AdminContentFilterForTesting> filterIncludeList = new ArrayList<>();
    static List<AdminContentFilterForTesting> filterExcludeList = new ArrayList<>();

    @Test
    public static void testPrepareData() throws Exception {
        System.out.println(" ");
        System.out.println("0) Заполнение массива данных для дальнейших проверок");
        prepareData();
    }

    @Test(dependsOnMethods = "testPrepareData", alwaysRun = true)
    public void testSeparateInclude() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        //1) Тестирование по отдельности include
        System.out.println(" ");
        System.out.println("1) Тестирование по отдельности include");
        for (AdminContentFilterForTesting filler1 : filterIncludeList) {
            if (filler1.getInclude())
                testFieldCombination(List.of(filler1), softAssert);
        }
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "testPrepareData", alwaysRun = true)
    public void testSeveral() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 2) Тестирование конфигураций include + exclude несколько
        System.out.println(" ");
        System.out.println("2) Несколько include - несколько exclude");
        List<AdminContentFilterForTesting> list = getRandomFilter(filterIncludeList, filterIncludeList.size() - 1);
        list.addAll(getRandomFilter(filterExcludeList, filterExcludeList.size() - 1));
        testFieldCombination(list, softAssert);
        softAssert.assertAll();

    }

    @Test(dependsOnMethods = "testSeveral", alwaysRun = true)
    public void testAllInclude() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 3) Тестирование конфигураций include все
        System.out.println(" ");
        System.out.println("3) Тестирование конфигураций include все");
        testFieldCombination(filterIncludeList, softAssert);
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "testAllInclude", alwaysRun = true)
    public void test1() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 4) Тестирование конфигураций по всем include - exclude
        System.out.println(" ");
        System.out.println("4) Тестирование конфигураций по всем include - exclude");
        for (int i = 0; i < filterIncludeList.size(); i++) {
            int n = new Random().nextInt(filterExcludeList.size());
            testFieldCombination(List.of(filterIncludeList.get(i), filterExcludeList.get(n)), softAssert);
        }
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "test1", alwaysRun = true)
    public void test2() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 5) Тестирование конфигураций include - по всем exclude
        System.out.println(" ");
        System.out.println("5) Тестирование конфигураций include - по всем exclude");
        for (int i = 0; i < filterExcludeList.size(); i++) {
            int n = new Random().nextInt(filterIncludeList.size());
            testFieldCombination(List.of(filterExcludeList.get(i), filterIncludeList.get(n)), softAssert);
        }
        softAssert.assertAll();
    }

    public static void prepareData() throws Exception {
        filterIncludeList.add(contentFilterOffers(true, "idInclude"));
        filterExcludeList.add(contentFilterOffers(false, "idExclude"));

        filterIncludeList.add(contentFilterOffersAdverts(true, "advertInclude"));
        filterExcludeList.add(contentFilterOffersAdverts(false, "advertExclude"));

        filterIncludeList.add(contentFilterOffersAdmins(true, "sales_manager", "salesManagerInclude"));
        filterExcludeList.add(contentFilterOffersAdmins(false, "sales_manager", "salesManagerExclude"));

        filterIncludeList.add(contentFilterOffersAdmins(true, "account_manager", "accountManagerInclude"));
        filterExcludeList.add(contentFilterOffersAdmins(false, "account_manager", "accountManagerExclude"));

       /* filterIncludeList.add(contentFilterOffersAdmins(true, "user_request_source", "userRequestSourceInclude"));
        filterExcludeList.add(contentFilterOffersAdmins(false, "user_request_source", "userRequestSourceExclude"));
*/
        filterIncludeList.add(contentFilterOther(true, "tag_id", "tag", "offer_tag", "tagInclude"));
        filterExcludeList.add(contentFilterOther(false, "tag_id", "tag", "offer_tag", "tagExclude"));

        filterIncludeList.add(contentFilterOther(true, "category_id", "category", "offer_category", "categoryInclude"));
        filterExcludeList.add(contentFilterOther(false, "category_id", "category", "offer_category", "categoryExclude"));

        filterIncludeList.add(contentFilterOfferInfo(true, OFFER_PRIVACY_LEVEL, 2, "privacy_level", "privacyLevelInclude"));
        filterExcludeList.add(contentFilterOfferInfo(false, OFFER_PRIVACY_LEVEL, 2, "privacy_level", "privacyLevelExclude"));

        filterIncludeList.add(contentFilterOfferInfo(true, OFFER_STATUS_MAP, 2, "status", "statusInclude"));
        filterExcludeList.add(contentFilterOfferInfo(false, OFFER_STATUS_MAP, 2, "status", "statusExclude"));

        filterIncludeList.add(contentFilterOfferInfo(true, GEO_MAP, 50, "country", "geoInclude"));
        filterExcludeList.add(contentFilterOfferInfo(false, GEO_MAP, 50, "country", "geoExclude"));
    }

    public static AdminContentFilterForTesting contentFilterOffers(boolean isInclude, String filterName) throws Exception {
        List<String> filterValue = sortToString(getSomeValuesFromBD("id", "offer", new Random().nextInt(10) + 1));
        List<Integer> expectedIds = sortToInteger(filterValue);
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterOffersAdverts(boolean isInclude, String filterName) throws Exception {
        List<String> filterValue = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        List<Integer> expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", filterValue));
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterOffersAdmins(boolean isInclude, String bdName, String filterName) throws Exception {
        // Выберем несколько Адвертов, у которых есть офферы
        List<String> advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(5) + 5);
        // Заберем у них значения админов
        List<String> filterValue = getArrayFromBDWhere(bdName, "advert", "id", advert_id);
        // Снова заберем Адвертов, у которых такие значения
        advert_id = getArrayFromBDWhere("id", "advert", bdName, filterValue);

        if (filterValue.contains("null")) {
            filterValue.removeIf(value -> value.equals("null")); // Удаляем все "null"
            filterValue.add("null"); // Добавляем один "null"
        }

        List<Integer> expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }


    public static AdminContentFilterForTesting contentFilterOfferInfo(boolean isInclude, Map<String, String> map, int bound,
                                                                      String whereName, String filterName) throws Exception {

        List<String> filterValue = getRandomKeys(map, new Random().nextInt(bound) + 1);
        List<Integer> expectedIds;
        if (filterName.contains("geo")) {
            expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "offer", whereName, filterValue));
        } else
            expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", whereName, filterValue));

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }


    public static AdminContentFilterForTesting contentFilterOther(boolean isInclude, String bdName,
                                                                  String tableName, String relationTableName, String filterName) throws Exception {
        List<String> filterValue = new ArrayList<>();
        if (tableName.equals("tag"))
            filterValue = getSomeValuesFromBD("id", tableName, new Random().nextInt(3) + 1);
        if (tableName.equals("category"))
            filterValue = getSomeValuesFromBDWhere("id", tableName, "lang", "general", new Random().nextInt(5) + 1);

        List<Integer> expectedIds = sortToInteger(getArrayFromBDWhere("offer_id", relationTableName, bdName, filterValue));
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);


        return filter;
    }

    public static void testFieldCombination(List<AdminContentFilterForTesting> contentFilters, SoftAssert softAssert) throws Exception {
        Allure.step("Тестирование полей: ");
        System.err.println("Тестирование полей: ");
        for (AdminContentFilterForTesting filter : contentFilters) {
            Allure.step(filter.getFilterName() + " " + filter.getFilterValue());
            System.err.println(filter.getFilterName());
            System.out.println(filter.getFilterValue());

        }
        // Вызов метода для установки значений фильтра
        contentFilter(contentFilters);

        int maxAttempts = 3; // максимальное количество попыток
        int attempts = 0;
        boolean success = false;

        while (attempts < maxAttempts && !success) {
            try {
                Thread.sleep(30000);

                // Получение ids Адвертов в null или не null permissions
                List<Integer> actualIds = new ArrayList<>();
                List<Integer> expectedIds = new ArrayList<>();

                // Сложим/вычтем expectedIds
                List<Integer> toAdd = new ArrayList<>();
                List<Integer> toRemove = new ArrayList<>();

                List<Integer> specialToAdd = new ArrayList<>();
                List<Integer> specialToRemove = new ArrayList<>();

                for (AdminContentFilterForTesting filter : contentFilters) {
                    List<Integer> filterIds = //filterExistAdvert
                            (filter.getExpectedIds());


                    if (!filter.getFilterName().equals("idInclude") && !filter.getFilterName().equals("idExclude")) {
                        if (filter.getInclude()) {
                            toAdd.addAll(filterIds); // Отложенные добавления
                        } else {
                            toRemove.addAll(filterIds); // Отложенные удаления
                        }
                    } else {
                        if (filter.getFilterName().equals("idInclude"))
                            specialToAdd.addAll(filterIds);
                        if (filter.getFilterName().equals("idExclude"))
                            specialToRemove.addAll(filterIds);
                    }

                    // После завершения цикла выполняем добавление и удаление
                    expectedIds.addAll(toAdd);
                    expectedIds.removeAll(toRemove);

                    expectedIds.addAll(specialToAdd);
                    expectedIds.removeAll(specialToRemove);
                }

                // Запросим actualIds
                actualIds.addAll(offersListGet());

                // Все отсортируем и удалим повторы
                List<Integer> expectedIdsFilter = new ArrayList<>();
                List<Integer> sortedActualIds = new ArrayList<>(actualIds);
                if (!actualIds.isEmpty())
                    Collections.sort(sortedActualIds);

                if (!expectedIds.isEmpty()) {
                    expectedIdsFilter = new ArrayList<>(expectedIds.stream().distinct().toList());
                    Collections.sort(expectedIdsFilter);
                }

                System.out.println("Из метода: " + sortedActualIds);
                System.out.println("Из БД: " + expectedIdsFilter);
                Assert.assertEquals(sortedActualIds, expectedIdsFilter);
                success = true;

                if (success)
                    System.out.println("Метод выполнен успешно.");


            } catch (AssertionError e) {
                System.err.println("Ошибка при выполнении метода: " + e.getMessage());
            }

            attempts++;
        }

        if (!success) {
            System.out.println("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            Allure.step("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            softAssert.fail();
        }
    }


    public static void contentFilter(List<AdminContentFilterForTesting> contentFilters) throws
            InterruptedException {
        JsonObject jsonObject = new JsonObject();

        for (AdminContentFilterForTesting filter : contentFilters) {
            JsonArray jsonArray = new JsonArray();
            filter.getFilterValue().forEach(jsonArray::add);  // Добавляем каждое значение в JsonArray
            jsonObject.add(filter.getFilterName(), jsonArray); // Добавляем ключ-значение в JsonObject
        }
        System.out.println(jsonObject);

        String path = "https://api.admin.3tracks.link/admin/104/content-filter/offer";

        // Отправка POST запроса
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", authKeyAdmin)
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));

        response = given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(" https://api.admin.3tracks.link/admin/104/content-filter");

        responseBody = response.getBody().asString();

        System.out.println(GET_RESPONSE + responseBody);
        Allure.step(GET_RESPONSE + responseBody);
    }


    public static List<Integer> offersListGet() throws SQLException {
        ArrayList<Integer> offersFromList = new ArrayList<>();

        int count = Integer.parseInt(getCountFromBD("offer"));

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", authKeyAdmin)
                .get(URL + "/offer?page=1&limit=" + count + "/");

        String responseBody = response.getBody().asString();
        Allure.step("Ответ на запрос: " + responseBody);
        System.out.println("Ответ на запрос: " + responseBody);


        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray offersArray = data.getJSONArray("offers");

        for (int i = 0; i < count; i++) {
            JSONObject offerObject = offersArray.getJSONObject(i);

            if (!offerObject.isNull("permission"))
                offersFromList.add(offerObject.getInt("id"));
        }
        return offersFromList;
    }
}


