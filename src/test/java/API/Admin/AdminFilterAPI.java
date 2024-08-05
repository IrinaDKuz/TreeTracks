package API.Admin;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Админов
 - filters,
TODO: 90% DONE BUG не ищет по skype
 */

public class AdminFilterAPI {

    public final static Map<String, String> adminFields = new HashMap<>() {{
        put("status", "status");
        put("email", "email");
        put("first_name", "firstName");
        put("second_name", "secondName");
        put("phone", "messenger");
        put("telegram", "messenger");
        put("skype", "messenger");
    }};

    @Test
    public static void test() throws Exception {
        Allure.description("Проверка работы метода фильтрациии Админов");
        SoftAssert softAssert = new SoftAssert();
        for (Map.Entry<String, String> entry : adminFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "admin", entry.getKey());
            List<String> ids = getArrayFromBDWhere("id", "admin", entry.getKey(), value);
            filterAdmins(entry.getValue(), value, ids, softAssert);
        }
        softAssert.assertAll();
    }

    private static void filterAdmins(String paramName, String paramValue, List<String> ids, SoftAssert softAssert) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);

        System.out.println("paramName = " + paramName);
        System.out.println("paramValue = " + paramValue);
        System.out.println("advertIds = " + ids);
        Allure.step("Поверка " + paramName + "=" + paramValue);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .params(params)
                .get("https://api.admin.3tracks.link/admin");

        String responseBody = response.getBody().asString();
        attachJson(responseBody, GET_RESPONSE);
        softAssert.assertTrue(responseBody.contains("{\"success\":true"));

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataArray = jsonObject.getJSONObject("data");
        JSONArray admins = dataArray.getJSONArray("admin");

        List<String> filterIdList = new ArrayList<>();
        for (int i = 0; i < admins.length(); i++) {
            JSONObject dataObject = admins.getJSONObject(i);
            filterIdList.add(String.valueOf(dataObject.getInt("id")));
        }

        Collections.sort(filterIdList);
        Collections.sort(ids);
        Allure.step("AdminId из фильтра: " + filterIdList);
        Allure.step("AdminId из базы: " + ids);
        softAssert.assertEquals(filterIdList, ids);
    }
}
