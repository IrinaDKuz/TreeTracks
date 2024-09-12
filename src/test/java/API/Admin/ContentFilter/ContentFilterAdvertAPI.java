package API.Admin.ContentFilter;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertFromList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static API.Helper.*;
import static API.Helper.getStringArrayFromJson;
import static Helper.Adverts.ADVERT_STATUS_MAP;
import static Helper.Adverts.MODEL_TYPES_MAP;
import static Helper.AllureHelper.*;
import static Helper.AllureHelper.DATA;
import static Helper.Auth.authKeyAdmin;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест проверяет работу API методов Админов
 - filters,
 TODO: 0% DONE
 */

public class ContentFilterAdvertAPI {


    @Test
    public static void test() throws Exception {
        SoftAssert softAssert = new SoftAssert();
  /*      Allure.step("1) Проверки exclude полей отдельно");
        System.out.println("1) Проверки exclude полей отдельно");
        excludeSeparateTest(softAssert);*/

        Allure.step("1) Проверки include полей отдельно");
        System.out.println("1) Проверки include полей отдельно");
        includeSeparateTest(softAssert);


   /*     for (Map.Entry<String, String> entry : adminFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "admin", entry.getKey());
            List<String> ids = getArrayFromBDWhere("id", "admin", entry.getKey(), value);
            filterAdmins(entry.getValue(), value, ids, softAssert);
        }*/
        softAssert.assertAll();
    }


    public static void excludeSeparateTest(SoftAssert softAssert) throws Exception {
        List<Integer> idExclude = getSomeValuesFromBD("id", "advert",
                new Random().nextInt(100) + 1).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "idExclude", idExclude, idExclude, softAssert);

        List<Integer> managerIdExclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("id", "advert",
                "manager_id", managerIdExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "managerIdExclude", managerIdExclude, idExclude, softAssert);

        List<Integer> accountManagerExclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("id", "advert",
                "account_manager", accountManagerExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "accountManagerExclude", accountManagerExclude, idExclude, softAssert);

        List<Integer> salesManagerExclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("id", "advert",
                "sales_manager", salesManagerExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "salesManagerExclude", salesManagerExclude, idExclude, softAssert);

        List<Integer> userRequestSourceExclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("id", "advert",
                "user_request_source", userRequestSourceExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "userRequestSourceExclude", userRequestSourceExclude, idExclude, softAssert);

        List<Integer> tagExclude = getSomeValuesFromBD("id", "advert_tag",
                new Random().nextInt(3) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("advert_id", "advert_tag_relation",
                "advert_tag_id", tagExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "tagExclude", tagExclude, idExclude, softAssert);


        List<Integer> categoryExclude = getSomeValuesFromBDWhere("id", "category",
                "lang", "general",
                new Random().nextInt(3) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idExclude = getArrayFromBDWhereInteger("advert_id", "advert_category",
                "category_id", categoryExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(false, "categoryExclude", categoryExclude, idExclude, softAssert);


        getRandomKey(ADVERT_STATUS_MAP);


        List<String> pricingModelExclude = getRandomKeys(MODEL_TYPES_MAP, new Random().nextInt(2) + 1);

        idExclude = getArrayFromBDWhere("id", "advert",
                "pricing_model", pricingModelExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(false, "pricingModelExclude", pricingModelExclude, idExclude, softAssert);


        List<String> statusExclude = getRandomKeys(ADVERT_STATUS_MAP, new Random().nextInt(3) + 1);

        idExclude = getArrayFromBDWhere("id", "advert",
                "status", statusExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(false, "statusExclude", statusExclude, idExclude, softAssert);


        List<String> geoExclude = getRandomKeys(ADVERT_STATUS_MAP, new Random().nextInt(3) + 1);

        idExclude = getArrayFromBDWhere("id", "advert",
                "geo", geoExclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(false, "geoExclude", geoExclude, idExclude, softAssert);




/*        List<Integer> personArrayFromBD = getArrayFromBDWhere("id", "advert",
                field, fieldValue)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        separateTest("managerIdExclude", softAssert);
        separateTest("salesManagerExclude", softAssert);*/

        //  separateMessengerTest(softAssert);
    }


    public static void includeSeparateTest(SoftAssert softAssert) throws Exception {


        /////////////
        List<Integer> idInclude = getSomeValuesFromBD("id", "advert",
                new Random().nextInt(100) + 1).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(true, "idInclude", idInclude, idInclude, softAssert);
/////////////
        List<Integer> managerIdInclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idInclude = getArrayFromBDWhereInteger("id", "advert",
                "manager_id", managerIdInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(true, "managerIdInclude", managerIdInclude, idInclude, softAssert);
/////////////
        List<Integer> accountManagerInclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idInclude = getArrayFromBDWhereInteger("id", "advert",
                "account_manager", accountManagerInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(true, "accountManagerInclude", accountManagerInclude, idInclude, softAssert);
/////////////
        List<Integer> salesManagerInclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idInclude = getArrayFromBDWhereInteger("id", "advert",
                "sales_manager", salesManagerInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(true, "salesManagerInclude", salesManagerInclude, idInclude, softAssert);
/////////////
        List<Integer> userRequestSourceInclude = getSomeValuesFromBD("id", "admin",
                new Random().nextInt(10) + 1).stream()
                .map(Integer::valueOf)
                .toList();
/////////////
        idInclude = getArrayFromBDWhereInteger("id", "advert",
                "user_request_source", userRequestSourceInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTest(true, "userRequestSourceInclude", userRequestSourceInclude, idInclude, softAssert);
/////////////
        List<Integer> tagInclude = getSomeValuesFromBD("id", "advert_tag",
                new Random().nextInt(5) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idInclude = getArrayFromBDWhereInteger("advert_id", "advert_tag_relation",
                "advert_tag_id", tagInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        Set<Integer> set = new HashSet<>(idInclude);
        idInclude = new ArrayList<>(set);
/////////////
        separateTest(true, "tagInclude", tagInclude, idInclude, softAssert);


        List<Integer> categoryInclude = getSomeValuesFromBDWhere("id", "category",
                "lang", "general",
                new Random().nextInt(5) + 1).stream()
                .map(Integer::valueOf)
                .toList();

        idInclude = getArrayFromBDWhereInteger("advert_id", "advert_category",
                "category_id", categoryInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        Set<Integer> set2 = new HashSet<>(idInclude);
        idInclude = new ArrayList<>(set2);
/////////////
        separateTest(true, "categoryInclude", categoryInclude, idInclude, softAssert);


        List<String> pricingModelInclude = getRandomKeys(MODEL_TYPES_MAP, new Random().nextInt(2) + 1);

        idInclude = getArrayFromBDWhereLike("id", "advert",
                "pricing_model", pricingModelInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(true, "pricingModelInclude", pricingModelInclude, idInclude, softAssert);
/////////////
        List<String> statusInclude = getRandomKeys(ADVERT_STATUS_MAP, new Random().nextInt(2) + 1);

        idInclude = getArrayFromBDWhere("id", "advert",
                "status", statusInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(true, "statusInclude", statusInclude, idInclude, softAssert);

        List<String> geoInclude = getRandomKeys(GEO_MAP, new Random().nextInt(5) + 1);
        idInclude = getArrayFromBDWhereLike("id", "advert",
                "geo", geoInclude).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        separateTestString(true, "geoInclude", geoInclude, idInclude, softAssert);

/*        List<Integer> personArrayFromBD = getArrayFromBDWhere("id", "advert",
                field, fieldValue)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        separateTest("managerIdExclude", softAssert);
        separateTest("salesManagerExclude", softAssert);*/

        //  separateMessengerTest(softAssert);
    }


    private static void separateTest(Boolean isInclude, String field, List<Integer> list, List<Integer> idExcludeFromBD, SoftAssert softAssert) throws Exception {
        System.err.println("----- " + field + ", значение: " + list);
        Allure.step(field + ", значение: " + list);

        contentFilter(Map.of(field, list));
        List<Integer> personArray = advertListGet(isInclude, false);

        if (!personArray.isEmpty() && !idExcludeFromBD.isEmpty()) {
            Collections.sort(personArray);
            Collections.sort(idExcludeFromBD);
        }

        System.out.println("id Адвертов из ответа на запрос : " + personArray);
        System.out.println("id Адвертов из БД: " + idExcludeFromBD);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + ", id Адвертов из БД: " + idExcludeFromBD);
        softAssert.assertEquals(personArray, idExcludeFromBD);
    }


    private static void separateTestString(Boolean isInclude, String field, List<String> list, List<Integer> idExcludeFromBD, SoftAssert softAssert) throws Exception {
        System.err.println("----- " + field + ", значение: " + list);
        Allure.step(field + ", значение: " + list);

        contentFilterString(Map.of(field, list));
        List<Integer> personArray = advertListGet(isInclude, false);

        if (!personArray.isEmpty() && !idExcludeFromBD.isEmpty()) {
            Collections.sort(personArray);
            Collections.sort(idExcludeFromBD);
        }

        System.out.println("id Адвертов из ответа на запрос : " + personArray);
        System.out.println("id Адвертов из БД: " + idExcludeFromBD);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + ", id Адвертов из БД: " + idExcludeFromBD);
        softAssert.assertEquals(personArray, idExcludeFromBD);
    }

    public static void contentFilter(Map<String, List<Integer>> params) throws InterruptedException {
        JsonObject jsonObject = new JsonObject();

        params.forEach((key, value) -> {
            JsonArray jsonArray = new JsonArray();
            value.forEach(jsonArray::add);
            jsonObject.add(key, jsonArray);
        });

        String path = "https://api.admin.3tracks.link/admin/104/content-filter/advert";

        // Отправка POST запроса
        Response response = RestAssured.given()
                .contentType(ContentType.JSON) // Устанавливаем тип содержимого JSON
                .header("Authorization", "Bearer " + authKeyAdmin) // Добавляем заголовок авторизации
                .body(jsonObject.toString()) // Передаем тело запроса
                .post(path);

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("{\"success\":true"));


        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(" https://api.admin.3tracks.link/admin/104/content-filter");

        // Получаем и выводим ответ
        responseBody = response.getBody().asString();

        System.out.println(GET_RESPONSE + responseBody);
        Allure.step(GET_RESPONSE + responseBody);

        Thread.sleep(60000);
    }

    public static void contentFilterString(Map<String, List<String>> params) throws InterruptedException {
        JsonObject jsonObject = new JsonObject();

        params.forEach((key, value) -> {
            JsonArray jsonArray = new JsonArray();
            value.forEach(jsonArray::add);
            jsonObject.add(key, jsonArray);
        });

        String path = "https://api.admin.3tracks.link/admin/104/content-filter/advert";

        // Отправка POST запроса
        Response response = RestAssured.given()
                .contentType(ContentType.JSON) // Устанавливаем тип содержимого JSON
                .header("Authorization", "Bearer " + authKeyAdmin) // Добавляем заголовок авторизации
                .body(jsonObject.toString()) // Передаем тело запроса
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));

        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(" https://api.admin.3tracks.link/admin/104/content-filter");

        // Получаем и выводим ответ
        responseBody = response.getBody().asString();

        System.out.println(GET_RESPONSE + responseBody);
        Allure.step(GET_RESPONSE + responseBody);
        Thread.sleep(60000);

    }


    public static ArrayList<Integer> advertListGet(Boolean isInclude, Boolean isShow) throws SQLException {

        ArrayList<Integer> advertFromList = new ArrayList<>();
        Integer count = Integer.valueOf(getCountFromBD("advert"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(URL + "/advert?page=1&limit=" + count + "/");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray advertsArray = data.getJSONArray("adverts");

        for (int i = 0; i < count; i++) {
            JSONObject advertObject = advertsArray.getJSONObject(i);

            if (isInclude) {
                if (!advertObject.isNull("permission"))
                    advertFromList.add(advertObject.getInt("id"));
            } else {
                if (advertObject.isNull("permission"))
                    advertFromList.add(advertObject.getInt("id"));
            }
        }
        return advertFromList;
    }
}
