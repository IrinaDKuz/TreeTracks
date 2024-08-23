package API.Advert;

import AdvertPackage.entity.AdvertAccess;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

import java.util.ArrayList;
import java.util.List;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, add, проверка, edit, проверка, delete
 во вкладке Адверт - "Access"
 */

public class AdvertAccessAPI {
    public static int advertAccessId;
    public static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBDWhereNotNull("advert_id", "offer",
                "advert_id"));
        System.out.println("Получаем Access у рандомного Адверта, у которого есть Оффер " + advertId);
        Allure.step("Получаем Access у рандомного Адверта, у которого есть Оффер " + advertId);
        accessGet(true);

        Allure.step("Добавляем Access");
        AdvertAccess newAdvertAccess = accessAdd();
        Allure.step("Добавляем второй Access");
        newAdvertAccess = accessAdd();

        Allure.step("Выполняем проверки");
        contactsAssert(newAdvertAccess);

        Allure.step("Редактируем Access");
        AdvertAccess advertAccessEdit = accessEdit();
        Allure.step("Выполняем проверки");
        contactsAssert(advertAccessEdit);
        Allure.step("Удаляем Access " + advertAccessId);
        deleteMethod("advert",advertId + "/access/" + advertAccessId);
        assertDelete(String.valueOf(advertAccessId), "advert_access");
    }

    private static AdvertAccess accessAdd() throws Exception {
        AdvertAccess advertAccess = new AdvertAccess();
        advertAccess.fillAdvertAccessWithRandomData(String.valueOf(advertId));
        contactsAddPost(String.valueOf(advertId), advertAccess);
        return advertAccess;
    }

    public static int contactsAddPost(String id, AdvertAccess advertAccess) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertAccess(advertAccess), JsonObject.class);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + id + "/access/add");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);
        Allure.step("Ответ на add: " + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        advertAccessId = Integer.parseInt(getLastValueFromBD("id", "advert_access"));
        return advertAccessId;
    }

    public static AdvertAccess accessEdit() throws Exception {
        AdvertAccess advertAccess = new AdvertAccess();
        advertAccess.fillAdvertAccessWithRandomData(String.valueOf(advertId));
        contactsEditPost(String.valueOf(advertId), String.valueOf(advertAccessId), advertAccess);
        return advertAccess;
    }

    public static void contactsEditPost(String advertId, String advertAccessId, AdvertAccess advertAccess) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertAccess(advertAccess), JsonObject.class);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/access/" + advertAccessId + "/edit");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step("Ответ на edit: " + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");
    }

    private static JsonObject initializeJsonAdvertAccess(AdvertAccess advertAccess) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description", advertAccess.getDescription());
        jsonObject.addProperty("email", advertAccess.getEmail());
        jsonObject.addProperty("login", advertAccess.getLogin());
        jsonObject.addProperty("password", advertAccess.getPassword());
        jsonObject.addProperty("type", advertAccess.getType());
        jsonObject.addProperty("url", advertAccess.getUrl());

        List<String> offerIds = advertAccess.getOfferIds();
        JsonArray offersArray = new JsonArray();

        for (String offerId : offerIds) {
            offersArray.add(offerId);
        }
        jsonObject.add("offer", offersArray);

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        return jsonObject;
    }

    public static ArrayList<AdvertAccess> accessGet(Boolean isShow) {
        ArrayList<AdvertAccess> accessArrayList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/access");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertAccess advertAccess = new AdvertAccess();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertAccess.setAccessId(dataObject.getInt("id"));
            advertAccess.setUrl(dataObject.getString("url"));
            advertAccess.setType(dataObject.getString("type"));
            advertAccess.setLogin(dataObject.getString("login"));
            advertAccess.setPassword(dataObject.getString("password"));
            advertAccess.setEmail(String.valueOf(dataObject.isNull("email") ? null : dataObject.getString("email")));
            advertAccess.setDescription(String.valueOf(dataObject.isNull("description") ? null : dataObject.getString("description")));

            JSONArray offersArray = dataObject.getJSONArray("offer");
            List<String> offersIds = new ArrayList<>();
            for (int j = 0; j < offersArray.length(); j++) {
                JSONObject offerInfo = offersArray.getJSONObject(j);
                String label = offerInfo.getString("label");
                offersIds.add(String.valueOf(offerInfo.getInt("value")));
            }
            advertAccess.setOfferIds(offersIds);
            accessArrayList.add(advertAccess);
        }

        return accessArrayList;
    }

    public static void contactsAssert(AdvertAccess advertAccessNew) {
        ArrayList<AdvertAccess> advertAccessArrayList = accessGet(true);
        Boolean isAssert = false;
        for (AdvertAccess advertAccess : advertAccessArrayList) {
            if (advertAccess.getAccessId() == advertAccessId) {
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(advertAccess.getUrl(), advertAccessNew.getUrl());
                softAssert.assertEquals(advertAccess.getType(), advertAccessNew.getType());
                softAssert.assertEquals(advertAccess.getLogin(), advertAccessNew.getLogin());
                softAssert.assertEquals(advertAccess.getPassword(), advertAccessNew.getPassword());
                softAssert.assertEquals(advertAccess.getEmail(), advertAccessNew.getEmail());
                softAssert.assertEquals(advertAccess.getDescription(), advertAccessNew.getDescription());
                softAssert.assertEquals(advertAccess.getOfferIds(), advertAccessNew.getOfferIds());
                softAssert.assertAll();
                isAssert = true;
            }
        }
        Assert.assertTrue(isAssert);
    }
}