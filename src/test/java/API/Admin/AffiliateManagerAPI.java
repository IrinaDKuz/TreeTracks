package API.Admin;

import AdminPackage.entity.AffiliateManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete проверка
 во вкладке Админ - "General"
 TODO: 90% DONE
 */

public class AffiliateManagerAPI {
    static String affiliateManagerId = null;

    @Test
    public static void test() throws Exception {
        authApi(103);
        Allure.step("Получаем список AffiliateManager");
        affManagerGetList();

        Allure.step("Добавляем AffiliateManager");
        AffiliateManager affiliateManager = affManagerAddEdit(false);
        Allure.step(CHECK);
        affManagerAssert(affiliateManager);

        Allure.step("Редактируем AffiliateManager Info id= " + affiliateManagerId);
        AffiliateManager affManagerEdit = affManagerAddEdit(true);
        Allure.step(CHECK);
        affManagerAssert(affManagerEdit);

        Allure.step(DELETE + affiliateManagerId);
        deleteMethod("admin/affiliate-manager", String.valueOf(affiliateManagerId));
        assertDelete(String.valueOf(affiliateManagerId), "affiliate_manager");
    }

    private static JsonObject initializeJsonAffiliateManager(AffiliateManager affiliateManager) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("adminId", affiliateManager.getAdminId());
        jsonObject.addProperty("code", affiliateManager.getCode());
        jsonObject.addProperty("telegramId", affiliateManager.getTelegramId());
        jsonObject.addProperty("telegramUsername", affiliateManager.getTelegramUsername());
        jsonObject.addProperty("isAddToChat", affiliateManager.getAddToChat());
        jsonObject.addProperty("isChatAdmin", affiliateManager.getChatAdmin());

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        return jsonObject;
    }

    public static AffiliateManager affManagerAddEdit(Boolean isEdit) throws Exception {
        AffiliateManager affiliateManager = new AffiliateManager();
        affiliateManager.fillAffiliateManagerWithRandomDataForAPI(affiliateManagerId);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAffiliateManager(affiliateManager), JsonObject.class);


        String path = isEdit ? "https://api.admin.3tracks.link/admin/affiliate-manager/" + affiliateManagerId + "/edit" :
                "https://api.admin.3tracks.link/admin/affiliate-manager/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            affiliateManagerId = String.valueOf(jsonResponse.getJSONObject("data").getInt("id"));
            affiliateManager.setId(affiliateManagerId);
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return affiliateManager;
    }

    public static AffiliateManager affManagerGet(Boolean isShow) {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/admin/affiliate-manager/" + affiliateManagerId);

        String responseBody = response.getBody().asString();

        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject affiliateManagerJson = data.getJSONObject("affiliateManager");

        AffiliateManager affiliateManager = new AffiliateManager();
        affiliateManager.setId(String.valueOf(affiliateManagerJson.getInt("id")));
        affiliateManager.setAdminId(String.valueOf(affiliateManagerJson.getInt("adminId")));
        affiliateManager.setTelegramId(affiliateManagerJson.getString("telegramId"));
        affiliateManager.setTelegramUsername(affiliateManagerJson.getString("telegramUsername"));
        affiliateManager.setCode(affiliateManagerJson.getString("code"));
        affiliateManager.setAddToChat(affiliateManagerJson.getBoolean("isAddToChat"));
        affiliateManager.setChatAdmin(affiliateManagerJson.getBoolean("isChatAdmin"));

        return affiliateManager;
    }

    public static void affManagerGetList() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/admin/affiliate-manager/");

        String responseBody = response.getBody().asString();

        System.out.println(GET_RESPONSE + responseBody);
        Allure.step(GET_RESPONSE + responseBody);
        attachJson(responseBody, GET_RESPONSE);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray affiliateManagers = data.getJSONArray("managers");

        for (int i = 0; i < affiliateManagers.length(); i++) {
            JSONObject affiliateManagerJson = affiliateManagers.getJSONObject(i);
            AffiliateManager affiliateManager = new AffiliateManager();
            affiliateManager.setId(String.valueOf(affiliateManagerJson.getInt("id")));
            affiliateManager.setAdminId(String.valueOf(affiliateManagerJson.getInt("adminId")));
            affiliateManager.setTelegramId(affiliateManagerJson.getString("telegramId"));
            affiliateManager.setTelegramUsername(affiliateManagerJson.getString("telegramUsername"));
            affiliateManager.setCode(affiliateManagerJson.isNull("code") ? null : affiliateManagerJson.getString("code") );
            affiliateManager.setAddToChat(affiliateManagerJson.getBoolean("isAddToChat"));
            affiliateManager.setChatAdmin(affiliateManagerJson.getBoolean("isChatAdmin"));
        }
    }

    public static void affManagerAssert(AffiliateManager affiliateManagerEdit) {
        AffiliateManager affiliateManager = affManagerGet(true);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(affiliateManager.getId(), affiliateManagerEdit.getId());
        softAssert.assertEquals(affiliateManager.getAdminId(), affiliateManagerEdit.getAdminId());
        softAssert.assertEquals(affiliateManager.getTelegramId(), affiliateManagerEdit.getTelegramId());
        softAssert.assertEquals(affiliateManager.getTelegramUsername(), affiliateManagerEdit.getTelegramUsername());
        softAssert.assertEquals(affiliateManager.getCode(), affiliateManagerEdit.getCode());
        softAssert.assertEquals(affiliateManager.getAddToChat(), affiliateManagerEdit.getAddToChat());
        softAssert.assertEquals(affiliateManager.getChatAdmin(), affiliateManagerEdit.getChatAdmin());
        softAssert.assertAll();
    }
}