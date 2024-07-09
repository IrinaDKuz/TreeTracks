package API.Settings.System;

import SettingsPackage.entity.SettingsEmail;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - add/edit и get
 во вкладке Settings - "Email"
 */


public class SettingsEmailAPI {
    public static int settingEmailId;
    static String EMAIL_TYPE_ERROR = "This type is already in use. Please try another one.";

    @Test
    public static void test() throws Exception {
        emailGet();
        emailAddEdit("add");
        settingEmailId = Integer.parseInt(getRandomValueFromBD("id", "mail_server"));
        SettingsEmail settingsEmail = emailAddEdit(settingEmailId + "/edit");
        emailAssert(settingsEmail);
        emailDelete(String.valueOf(settingEmailId));
    }

    private static JsonObject initializeJsonSettingEmailBody(SettingsEmail settingsEmail) {
        JsonObject mainObject = new JsonObject();
        JsonObject settingEmail = new JsonObject();
        settingEmail.addProperty("type", settingsEmail.getType());
        settingEmail.addProperty("protocol", settingsEmail.getProtocol());
        settingEmail.addProperty("server", settingsEmail.getServer());
        settingEmail.addProperty("port", settingsEmail.getPort());
        settingEmail.addProperty("user", settingsEmail.getUser());
        settingEmail.addProperty("password", settingsEmail.getPassword());
        mainObject.add("mail_server", settingEmail);
        return mainObject;
    }

    public static SettingsEmail emailAddEdit(String method) throws Exception {
        SettingsEmail settingsEmail = new SettingsEmail();
        settingsEmail.fillSettingsEmailWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingEmailBody(settingsEmail), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        if (getArrayFromBD("type", "mail_server").contains(settingsEmail.getType())) {
            Response responseError = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", authKeyAdmin)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .post("https://api.admin.3tracks.link/setting/email/" + method);
            String responseErrorBody = responseError.getBody().asString();

            System.out.println("Ответ на add/edit (ошибка): " + responseErrorBody);
            JSONObject jsonResponseError = new JSONObject(responseErrorBody);
            JSONObject error = (JSONObject) jsonResponseError.getJSONArray("error").get(0);
            Assert.assertEquals(error.getString("msg"), EMAIL_TYPE_ERROR);

            emailDelete(getValueFromBDWhere("id", "mail_server",
                    "type", settingsEmail.getType()));
        }
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/email/" + method);
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add/edit: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return settingsEmail;
    }


    public static ArrayList<SettingsEmail> emailGet() {
        ArrayList<SettingsEmail> settingsEmails = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/email");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            SettingsEmail email = new SettingsEmail();
            JSONObject dataObject = dataArray.getJSONObject(i);
            email.setEmailId(dataObject.getInt("id"));
            email.setType(dataObject.isNull("type") ? null : dataObject.getString("type"));
            email.setProtocol(dataObject.getString("protocol"));
            email.setServer(dataObject.getString("server"));
            email.setPort(dataObject.getInt("port"));
            email.setUser(dataObject.getString("user"));
            email.setPassword(dataObject.getString("password"));
        }
        return settingsEmails;
    }


    public static void emailAssert(SettingsEmail settingsEmailEdit) {
        // ниже упрощенный, но работающий метод возможно его нужно будет поменять и расписать все поля
        ArrayList<SettingsEmail> settingsEmails = emailGet();
        for (SettingsEmail settingsEmail : settingsEmails) {
            if (settingsEmail.getEmailId() == settingEmailId){
                Assert.assertEquals(settingsEmail.getType(), settingsEmailEdit.getType());
                Assert.assertEquals(settingsEmail.getPassword(), settingsEmailEdit.getPassword());
                Assert.assertEquals(settingsEmail.getPort(), settingsEmailEdit.getPort());
                Assert.assertEquals(settingsEmail.getProtocol(), settingsEmailEdit.getProtocol());
                Assert.assertEquals(settingsEmail.getUser(), settingsEmailEdit.getUser());
                Assert.assertEquals(settingsEmail.getServer(), settingsEmailEdit.getServer());
            }
        }
    }

    public static void emailDelete(String id) {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/setting/email/" + id + "/remove");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на delete: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }
}