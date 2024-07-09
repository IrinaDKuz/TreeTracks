package API.Settings.System;

import SettingsPackage.entity.SettingsGeneral;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - add/edit и get
 во вкладке Settings - "General"
 */


public class SettingsGeneralAPI {

    @Test
    public static void test() throws Exception {
        generalGet();
        SettingsGeneral settingsGeneral = generalAddEdit();
        generalAssert(settingsGeneral);
    }

    private static JsonObject initializeJsonSettingGeneralBody(SettingsGeneral settingsGeneral) {
        JsonObject mainObject = new JsonObject();

        JsonObject settingGeneral = new JsonObject();
        settingGeneral.addProperty("siteName", settingsGeneral.getSiteName());
        settingGeneral.addProperty("domain", settingsGeneral.getDomain());
        settingGeneral.addProperty("defaultLanguage", settingsGeneral.getDefaultLanguage());
        settingGeneral.addProperty("timezone", settingsGeneral.getTimezone());
        settingGeneral.addProperty("country", settingsGeneral.getCountry());
        settingGeneral.addProperty("city", settingsGeneral.getCity());
        settingGeneral.addProperty("zipCode", settingsGeneral.getZipCode());
        settingGeneral.addProperty("phone", settingsGeneral.getPhone());
        settingGeneral.addProperty("email", settingsGeneral.getEmail());
        settingGeneral.addProperty("showCommentatorName", settingsGeneral.getShowCommentatorName());

        mainObject.add("setting_general", settingGeneral);
        return mainObject;
    }

    public static SettingsGeneral generalAddEdit() throws Exception {
        SettingsGeneral settingsGeneral = new SettingsGeneral();
        settingsGeneral.fillSettingsGeneralInfoWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingGeneralBody(settingsGeneral), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/general");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return settingsGeneral;
    }

    public static SettingsGeneral generalGet() {
        SettingsGeneral settingsGeneral = new SettingsGeneral();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/general");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataObject = jsonObject.getJSONObject("data");

        settingsGeneral.setSiteName(dataObject.getString("siteName"));
        settingsGeneral.setDomain(dataObject.getString("domain"));
        settingsGeneral.setDefaultLanguage(dataObject.getString("defaultLanguage"));
        settingsGeneral.setTimezone(dataObject.getString("timezone"));
        settingsGeneral.setCountry(dataObject.getString("country"));
        settingsGeneral.setCity(dataObject.getString("city"));
        settingsGeneral.setZipCode(dataObject.getString("zipCode"));
        settingsGeneral.setPhone(dataObject.getString("phone"));
        settingsGeneral.setEmail(dataObject.getString("email"));
        settingsGeneral.setShowCommentatorName(dataObject.getBoolean("showCommentatorName"));
        return settingsGeneral;
    }

    public static void generalAssert(SettingsGeneral settingsGeneralEdit) {
        // ниже упрощенный, но работающий метод возможно его нужно будет поменять и расписать все поля
        SettingsGeneral settingsGeneral = generalGet();
        Assert.assertTrue(EqualsBuilder.reflectionEquals(settingsGeneral, settingsGeneralEdit));
    }
}