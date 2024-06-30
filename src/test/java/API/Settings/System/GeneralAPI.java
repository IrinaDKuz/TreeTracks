package API.Settings.System;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static Helper.Adverts.*;
import static Helper.Auth.authKeyAdmin;
import static Helper.Settings.*;

public class GeneralAPI {

    static Map<String, Object> settingGeneralMap = new HashMap<>();

    @Test
    public static void test() throws InterruptedException {
        generalAddEdit();
        generalGet();
    }

    private static JsonObject initializeJsonSettingGeneralBody() {
        JsonObject mainObject = new JsonObject();
        JsonObject settingGeneral = new JsonObject();
        settingGeneralMap.put("siteName", generateCompanyUrl(generateName(4, COMPANY_WORDS)));
        settingGeneralMap.put("domain", generateCompanyUrl(generateName(2, COMPANY_WORDS)));
        settingGeneralMap.put("defaultLanguage", "eng");
        settingGeneralMap.put("timezone", generateUTC());
        settingGeneralMap.put("country", generateName(1, COUNTRY_LETTERS));
        settingGeneralMap.put("city", generateName(1, CITIES_WORDS));
        settingGeneralMap.put("zipCode", generateRandomNumber(10).toString());
        settingGeneralMap.put("phone", generateRandomNumber(10).toString());
        settingGeneralMap.put("email", generateEmail(generateName(2, COMPANY_WORDS)));
        settingGeneralMap.put("showCommentatorName", new Random().nextBoolean());

        for (Map.Entry<String, Object> entry : settingGeneralMap.entrySet()) {
           if (entry.getValue() instanceof Boolean) {
                settingGeneral.addProperty(entry.getKey(), (Boolean) entry.getValue());
                System.out.println(entry.getKey() + ":" + entry.getValue());
            } else {
                settingGeneral.addProperty(entry.getKey(), (String) entry.getValue());
            }
        }

        mainObject.add("setting_general", settingGeneral);
        return mainObject;
    }

    public static void generalAddEdit() {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingGeneralBody(), JsonObject.class);

        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/general");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }

    public static void generalGet() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/general");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataObject = jsonObject.getJSONObject("data");

        for (Map.Entry<String, Object> entry : settingGeneralMap.entrySet()) {
            System.out.println(entry.getValue() + ":" + dataObject.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), dataObject.get(entry.getKey()));
        }
    }
}