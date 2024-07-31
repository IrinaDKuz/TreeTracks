package API.AdvertPlus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static Helper.AllureHelper.DATA;
import static Helper.Auth.authKeyAdmin;

public class AdvertBulkAPI {

    static int advertId = 1010;
    static String rowName = "offers";

    static JsonObject json = new JsonObject();

    // Вот это все обернуть в еще один мапа - параметр - массив значений
    private static JsonObject initializeJsonBulkAdvertBody(Map<String, List<String>> arrayPropertyValueMap,
                                                           Map<String, String> stringPropertyValueMap) {

        JsonObject jsonMethod = new JsonObject();
        if (!arrayPropertyValueMap.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : arrayPropertyValueMap.entrySet()) {
                JsonArray jsonArray = new JsonArray();
                for (String value : entry.getValue()) {
                    jsonArray.add(value);
                }
                jsonMethod.add(entry.getKey(), jsonArray);
            }
        }

        // только статус не массив, его отдельно будем передавать
        if (!stringPropertyValueMap.isEmpty()) {
            for (Map.Entry<String, String> entry : stringPropertyValueMap.entrySet()) {
                jsonMethod.addProperty(entry.getKey(), entry.getValue());
            }
        }
        return jsonMethod;
    }

    public static void advertBulkChange(Map<String, List<String>> arrayPropertyValueMap,
                                   Map<String, String> stringPropertyValueMap) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonBulkAdvertBody(arrayPropertyValueMap,
                stringPropertyValueMap), JsonObject.class);

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/bulk-edit");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Allure.step("Ответ: " + responseBody);

    }
}