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

import java.sql.SQLException;
import java.util.*;

import static API.Helper.*;
import static API.Helper.sortToInteger;
import static Helper.Adverts.ADVERT_STATUS_MAP;
import static Helper.Adverts.MODEL_TYPES_MAP;
import static Helper.AllureHelper.GET_RESPONSE;
import static Helper.Auth.authKeyAdmin;
import static Helper.GeoAndLang.GEO_MAP;
import static Helper.GeoAndLang.getRandomKeys;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест проверяет работу Advert Content Filter

 TODO: 90% DONE
 */

public class ContentFilterAdvert {

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
        //1) Тестирование по отдельности include
        System.out.println(" ");
        System.out.println("1) Тестирование по отдельности include");
        for (AdminContentFilterForTesting filler1 : filterIncludeList) {
            if (filler1.getInclude())
                testFieldCombination(List.of(filler1));
        }
    }

    @Test(dependsOnMethods = "testPrepareData", alwaysRun = true)
    public void testSeveral() throws Exception {
        // 2) Тестирование конфигураций include + exclude несколько
        System.out.println(" ");
        System.out.println("2) Несколько include - несколько exclude");
        List<AdminContentFilterForTesting> list = getRandomFilter(filterIncludeList, filterExcludeList.size() - 1);
        list.addAll(getRandomFilter(filterExcludeList, filterExcludeList.size() - 1));
        testFieldCombination(list);
    }

    @Test(dependsOnMethods = "testSeveral", alwaysRun = true)
    public void testAllInclude() throws Exception {
     // 3) Тестирование конфигураций include + include все
        System.out.println(" ");
        System.out.println("3) Тестирование конфигураций include + include все");
        testFieldCombination(filterIncludeList);
    }

    @Test(dependsOnMethods = "testAllInclude", alwaysRun = true)
    public void test1() throws Exception {
        // 4) Тестирование конфигураций по всем include - exclude
        System.out.println(" ");
        System.out.println("4) Тестирование конфигураций по всем include - exclude");
        for (int i = 0; i < filterIncludeList.size(); i++) {
            int n = new Random().nextInt(filterExcludeList.size());
            testFieldCombination(List.of(filterIncludeList.get(i), filterExcludeList.get(n)));
        }
    }

    @Test(dependsOnMethods = "test1", alwaysRun = true)
    public void test2() throws Exception {
        // 5) Тестирование конфигураций include - по всем exclude
        System.out.println(" ");
        System.out.println("5) Тестирование конфигураций include - по всем exclude");
        for (int i = 0; i < filterExcludeList.size(); i++) {
            int n = new Random().nextInt(filterIncludeList.size());
            testFieldCombination(List.of(filterExcludeList.get(i), filterIncludeList.get(n)));
        }
    }

    public static void prepareData() throws Exception {

        filterIncludeList.add(contentFilterAdverts(true, "idInclude"));
        filterExcludeList.add(contentFilterAdverts(false, "idExclude"));

        filterIncludeList.add(contentFilterAdmins(true, "manager_id", "managerIdInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "manager_id", "managerIdExclude"));

        filterIncludeList.add(contentFilterAdmins(true, "account_manager", "accountManagerInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "account_manager", "accountManagerExclude"));

        filterIncludeList.add(contentFilterAdmins(true, "sales_manager", "salesManagerInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "sales_manager", "salesManagerExclude"));

        filterIncludeList.add(contentFilterAdmins(true, "user_request_source", "userRequestSourceInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "user_request_source", "userRequestSourceExclude"));

        List<String> filterValue = getSomeValuesFromBD("id", "advert_tag", new Random().nextInt(3) + 3);
        filterIncludeList.add(contentFilterOther(true, filterValue, "advert_id", "advert_tag_relation",
                "advert_tag_id", "tagInclude"));

        filterValue = getSomeValuesFromBD("id", "advert_tag", new Random().nextInt(3) + 3);
        filterExcludeList.add(contentFilterOther(false, filterValue, "advert_id", "advert_tag_relation",
                "advert_tag_id", "tagExclude"));

        filterValue = getSomeValuesFromBDWhere("id", "category", "lang", "general", new Random().nextInt(5) + 3);
        filterIncludeList.add(contentFilterOther(true, filterValue, "advert_id", "advert_category",
                "category_id", "categoryInclude"));

        filterValue = getSomeValuesFromBDWhere("id", "category", "lang", "general", new Random().nextInt(5) + 3);
        filterExcludeList.add(contentFilterOther(false, filterValue, "advert_id", "advert_category",
                "category_id", "categoryExclude"));

        filterIncludeList.add(contentFilterAdvertInfo(true, MODEL_TYPES_MAP, 2, "pricing_model", "pricingModelInclude"));
        filterExcludeList.add(contentFilterAdvertInfo(false, MODEL_TYPES_MAP, 2, "pricing_model", "pricingModelExclude"));

        filterIncludeList.add(contentFilterAdvertInfo(true, ADVERT_STATUS_MAP, 2, "status", "statusInclude"));
        filterExcludeList.add(contentFilterAdvertInfo(false, ADVERT_STATUS_MAP, 2, "status", "statusExclude"));

        filterIncludeList.add(contentFilterAdvertInfo(true, GEO_MAP, 20, "geo", "geoInclude"));
        filterExcludeList.add(contentFilterAdvertInfo(false, GEO_MAP, 20, "geo", "geoExclude"));
    }


    public static AdminContentFilterForTesting contentFilterAdverts(boolean isInclude, String filterName) throws Exception {
        List<String> filterValue = sortToString(getSomeValuesFromBD("id", "advert", new Random().nextInt(100) + 3));
        List<Integer> expectedIds = sortToInteger(filterValue);
        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterAdmins(boolean isInclude, String bdName, String filterName) throws Exception {
        List<String> filterValue = getSomeValuesFromBD("id", "admin", new Random().nextInt(10) + 2);
        filterValue.add("null");
        filterValue.add("self");
        List<Integer> expectedIds = new ArrayList<>();

        expectedIds.addAll(sortToInteger(getArrayFromBDWhere("id", "advert", bdName, filterValue)));
        expectedIds.addAll(sortToInteger(getArrayFromBDWhereNull("id", "advert", bdName)));
        expectedIds.addAll(sortToInteger(getArrayFromBDWhere("id", "advert", bdName, "104")));

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterOther(boolean isInclude, List<String> filterValue, String bdName,
                                                                  String tableName, String whereName, String filterName) throws Exception {
        List<Integer> expectedIds = sortToInteger(getArrayFromBDWhere(bdName, tableName, whereName, filterValue));

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterAdvertInfo(boolean isInclude, Map<String, String> map, int bound,
                                                                       String whereName, String filterName) throws Exception {

        List<String> filterValue = getRandomKeys(map, new Random().nextInt(bound) + 2);
        List<Integer> expectedIds;
        if (filterName.contains("geo") || filterName.contains("pricingModel")) {
            expectedIds = sortToInteger(getArrayFromBDWhereLike("id", "advert", whereName, filterValue));
        } else
            expectedIds = sortToInteger(getArrayFromBDWhere("id", "advert", whereName, filterValue));

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, filterValue, expectedIds);
        return filter;
    }

    public static void testFieldCombination(List<AdminContentFilterForTesting> contentFilters) throws Exception {
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
                List<Integer> actualIds = new ArrayList<>();
                List<Integer> expectedIds = new ArrayList<>();

                // Сложим/вычтем expectedIds
                List<Integer> toAdd = new ArrayList<>();
                List<Integer> toRemove = new ArrayList<>();

                List<Integer> specialToAdd = new ArrayList<>();
                List<Integer> specialToRemove = new ArrayList<>();

                for (AdminContentFilterForTesting filter : contentFilters) {
                    List<Integer> filterIds = filterExistAdvert(filter.getExpectedIds());


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
                actualIds.addAll(advertListGet());

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
                Allure.step("Из метода: " + sortedActualIds);
                Allure.step("Из БД: " + expectedIdsFilter);

                Assert.assertEquals(sortedActualIds.add(99999), expectedIdsFilter);
                success = true;

                if (success) {
                    System.out.println("Метод выполнен успешно.");
                    Allure.step("Метод выполнен успешно.");
                }


            } catch (AssertionError e) {
                System.err.println("Ошибка при выполнении метода: " + e.getMessage());
                Allure.step("Ошибка при выполнении метода: " + e.getMessage());
            }

            attempts++;
        }

        if (!success) {
            System.out.println("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            Allure.step("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            Assert.fail();
        }


    }

    public static void contentFilter(List<AdminContentFilterForTesting> contentFilters) {
        JsonObject jsonObject = new JsonObject();

        for (AdminContentFilterForTesting filter : contentFilters) {
            JsonArray jsonArray = new JsonArray();
            filter.getFilterValue().forEach(jsonArray::add);  // Добавляем каждое значение в JsonArray
            jsonObject.add(filter.getFilterName(), jsonArray); // Добавляем ключ-значение в JsonObject
        }
        System.out.println(jsonObject);

        String path = "https://api.admin.3tracks.link/admin/104/content-filter/advert";

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

    }


    public static List<Integer> advertListGet() throws SQLException {
        ArrayList<Integer> advertFromList = new ArrayList<>();

        int count = Integer.parseInt(getCountFromBD("advert"));

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", authKeyAdmin)
                .get(URL + "/advert?page=1&limit=" + count + "/");

        String responseBody = response.getBody().asString();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray advertsArray = data.getJSONArray("adverts");

        for (int i = 0; i < count; i++) {
            JSONObject advertObject = advertsArray.getJSONObject(i);

            // if (isInclude) {
            if (!advertObject.isNull("permission"))
                advertFromList.add(advertObject.getInt("id"));
            /*} else {
                if (advertObject.isNull("permission"))
                    advertFromList.add(advertObject.getInt("id"));
            }*/
        }
        return advertFromList;
    }


    public static List<AdminContentFilterForTesting> getRandomFilter(List<AdminContentFilterForTesting> list, int count) {
        List<AdminContentFilterForTesting> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList);
        return shuffledList.subList(0, new Random().nextInt(count) + 1);
    }
}





