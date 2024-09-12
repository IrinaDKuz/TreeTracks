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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
 Тест проверяет работу API методов Админов
 - filters,
 TODO: 0% DONE
 */

public class ContentFilterOffer {


    @Test
    public static void testCombinations() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        List<AdminContentFilterForTesting> filterIncludeList = new ArrayList<>();
        List<AdminContentFilterForTesting> filterExcludeList = new ArrayList<>();

        List<String> filterValue = sortToString(getSomeValuesFromBD("id", "offer", new Random().nextInt(10) + 1));
        List<Integer> expectedIds = sortToInteger(filterValue);
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(true, "idInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getSomeValuesFromBD("id", "offer", new Random().nextInt(10) + 1);
        expectedIds = sortToInteger(filterValue);
        filter = new AdminContentFilterForTesting(false, "idExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", filterValue));
        filter = new AdminContentFilterForTesting(true, "advertInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", filterValue));
        filter = new AdminContentFilterForTesting(false, "advertExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        List<String> advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("sales_manager", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(true, "salesManagerInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("sales_manager", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(false, "salesManagerExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("account_manager", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(true, "accountManagerInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("account_manager", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(false, "accountManagerExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("user_request_source", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(true, "userRequestSourceInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        advert_id = getSomeValuesFromBD("advert_id", "offer", new Random().nextInt(10) + 1);
        filterValue = getArrayFromBDWhere("user_request_source", "advert", "id", advert_id);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "advert_id", advert_id));
        filter = new AdminContentFilterForTesting(false, "userRequestSourceExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getSomeValuesFromBD("id", "tag", new Random().nextInt(3) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("offer_id", "offer_tag", "tag_id", filterValue));
        filter = new AdminContentFilterForTesting(true, "tagInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getSomeValuesFromBD("id", "tag", new Random().nextInt(3) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("offer_id", "offer_tag", "tag_id", filterValue));
        filter = new AdminContentFilterForTesting(false, "tagExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getSomeValuesFromBDWhere("id", "category", "lang", "general", new Random().nextInt(5) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("offer_id", "offer_category", "category_id", filterValue));
        filter = new AdminContentFilterForTesting(true, "categoryInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getSomeValuesFromBDWhere("id", "category", "lang", "general", new Random().nextInt(5) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("offer_id", "offer_category", "category_id", filterValue));
        filter = new AdminContentFilterForTesting(false, "categoryExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getRandomKeys(OFFER_PRIVACY_LEVEL, new Random().nextInt(2) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "offer", "privacy_level", filterValue));
        filter = new AdminContentFilterForTesting(true, "privacyLevelInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getRandomKeys(OFFER_PRIVACY_LEVEL, new Random().nextInt(2) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "offer", "privacy_level", filterValue));
        filter = new AdminContentFilterForTesting(false, "privacyLevelExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getRandomKeys(OFFER_STATUS_MAP, new Random().nextInt(2) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "status", filterValue));
        filter = new AdminContentFilterForTesting(true, "statusInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);

        filterValue = getRandomKeys(OFFER_STATUS_MAP, new Random().nextInt(2) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhere("id", "offer", "status", filterValue));
        filter = new AdminContentFilterForTesting(false, "statusExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);


        filterValue = getRandomKeys(GEO_MAP, new Random().nextInt(10) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "offer", "country", filterValue));
        filter = new AdminContentFilterForTesting(true, "geoInclude", filterValue, expectedIds);
        filterIncludeList.add(filter);


        filterValue = getRandomKeys(GEO_MAP, new Random().nextInt(10) + 1);
        expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "offer", "country", filterValue));
        filter = new AdminContentFilterForTesting(false, "geoExclude", filterValue, expectedIds);
        filterExcludeList.add(filter);

        // 1) Тестирование по отдельности include
        System.out.println(" ");
        System.out.println("1) Тестирование по отдельности include");
        for (AdminContentFilterForTesting filler1 : filterIncludeList) {
            if (filler1.getInclude())
                testFieldCombination(List.of(filler1), softAssert);
        }

  /*      // 2) Тестирование по отдельности include - exclude
        for (int i = 0; i < filterIncludeList.size(); i++) {
            AdminContentFilterForTesting filler1 = filterIncludeList.get(i);
            String name1 = filler1.getFilterName().replace("Include", "").replace("Exclude", "").trim();

            for (int j = i + 1; j < filterIncludeList.size(); j++) {
                AdminContentFilterForTesting filler2 = filterIncludeList.get(j);
                String name2 = filler2.getFilterName().replace("Include", "").replace("Exclude", "").trim();

                if (name1.equals(name2)) {
                    testFieldCombination(List.of(filler1, filler2), softAssert);
                }
            }
        }*/

        // 3) Тестирование конфигураций include + include все
        System.out.println(" ");
        System.out.println("3) Тестирование конфигураций include + include все");
        testFieldCombination(filterIncludeList, softAssert);


        // 4) Тестирование конфигураций по всем include - exclude
        System.out.println(" ");
        System.out.println("4) Тестирование конфигураций по всем include - exclude");
        for (int i = 0; i < filterIncludeList.size(); i++) {
            int n = new Random().nextInt(filterExcludeList.size());
            testFieldCombination(List.of(filterIncludeList.get(i), filterExcludeList.get(n)), softAssert);
        }

        // 5) Тестирование конфигураций include - по всем exclude
        System.out.println(" ");
        System.out.println("5) Тестирование конфигураций include - по всем exclude");
        for (int i = 0; i < filterExcludeList.size(); i++) {
            int n = new Random().nextInt(filterIncludeList.size());
            testFieldCombination(List.of(filterExcludeList.get(i), filterIncludeList.get(n)), softAssert);
        }


        // 6) Тестирование работы self и null


/*        // 2) Тестирование всех комбинаций полей
        List<Map<String, List<?>>> combinations = getCombinations(allFields);
        for (Map<String, List<?>> combination : combinations) {
            testFieldCombination(combination, softAssert);
        }*/

        softAssert.assertAll();
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

            attempts++; // увеличиваем количество попыток
        }

        if (!success) {
            System.out.println("Метод не удалось выполнить после " + maxAttempts + " попыток.");
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

        // Thread.sleep(60000);
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
        JSONArray offersArray = data.getJSONArray("offer");

        for (int i = 0; i < count; i++) {
            JSONObject offerObject = offersArray.getJSONObject(i);

            if (!offerObject.isNull("permission"))
                offersFromList.add(offerObject.getInt("id"));
        }
        return offersFromList;
    }
}





