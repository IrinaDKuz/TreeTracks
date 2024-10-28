package API.Offer.OfferMain;

import OfferMainPackage.entity.OfferAccess;
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

import java.util.ArrayList;

import static Helper.AllureHelper.*;
import static Helper.Auth.KEY;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, add, проверка, edit, проверка, delete
 во вкладке Offer - "Access"
 */

public class OfferAccessAPI {
    public static int offerAccessId;
    public static int offerId;

    @Test
    public static void test() throws Exception {
        offerId = Integer.parseInt(getRandomValueFromBD("id", "offer"));
        System.out.println("Получаем Access у рандомного Оффера " + offerId);
        Allure.step("Получаем Access у рандомного Оффера " + offerId);
        accessGet(true);

        Allure.step("Добавляем Access");
        OfferAccess newOfferAccess = offerAccessAdd(offerId);

        Allure.step("Выполняем проверки");
        contactsAssert(newOfferAccess);

        Allure.step("Редактируем Access");
        OfferAccess offerAccessEdit = accessEdit();
        Allure.step("Выполняем проверки");
        contactsAssert(offerAccessEdit);
        Allure.step("Удаление не предусмотрено");
    }

    public static OfferAccess offerAccessAdd(Integer offerId) throws Exception {
        OfferAccess offerAccess = new OfferAccess();
        offerAccess.fillOfferAccessWithRandomData(String.valueOf(offerId));
        contactsAddPost(String.valueOf(offerId), offerAccess);
        return offerAccess;
    }

    public static int contactsAddPost(String id, OfferAccess offerAccess) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferAccess(offerAccess), JsonObject.class);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/offer/" + id + "/access/new");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);
        Allure.step("Ответ на add: " + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");

        offerAccessId = Integer.parseInt(getLastValueFromBD("id", "advert_access"));
        return offerAccessId;
    }

    public static OfferAccess accessEdit() throws Exception {
        OfferAccess offerAccess = new OfferAccess();
        offerAccess.fillOfferAccessWithRandomData(String.valueOf(offerId));
        contactsEditPost(String.valueOf(offerId), String.valueOf(offerAccessId), offerAccess);
        return offerAccess;
    }

    public static void contactsEditPost(String offerId, String offerAccessId, OfferAccess offerAccess) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferAccess(offerAccess), JsonObject.class);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/offer/" + offerId + "/access/" + offerAccessId + "/edit");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step("Ответ на edit: " + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");
    }

    private static JsonObject initializeJsonOfferAccess(OfferAccess offerAccess) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description", offerAccess.getDescription());
        jsonObject.addProperty("email", offerAccess.getEmail());
        jsonObject.addProperty("login", offerAccess.getLogin());
        jsonObject.addProperty("password", offerAccess.getPassword());
        jsonObject.addProperty("type", offerAccess.getType());
        jsonObject.addProperty("url", offerAccess.getUrl());

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        return jsonObject;
    }

    public static ArrayList<OfferAccess> accessGet(Boolean isShow) {
        ArrayList<OfferAccess> accessArrayList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer/" + offerId + "/access");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            OfferAccess offerAccess = new OfferAccess();
            JSONObject dataObject = dataArray.getJSONObject(i);
            offerAccess.setAccessId(dataObject.getInt("id"));
            offerAccess.setUrl(dataObject.getString("url"));
            offerAccess.setType(dataObject.getString("type"));
            offerAccess.setLogin(dataObject.getString("login"));
            offerAccess.setPassword(dataObject.getString("password"));
            offerAccess.setEmail(String.valueOf(dataObject.isNull("email") ? null : dataObject.getString("email")));
            offerAccess.setDescription(String.valueOf(dataObject.isNull("description") ? null : dataObject.getString("description")));

            accessArrayList.add(offerAccess);
        }
        return accessArrayList;
    }

    public static void contactsAssert(OfferAccess offerAccessNew) {
        ArrayList<OfferAccess> offerAccessArrayList = accessGet(true);
        Boolean isAssert = false;
        for (OfferAccess offerAccess : offerAccessArrayList) {
            if (offerAccess.getAccessId() == offerAccessId) {
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertEquals(offerAccess.getUrl(), offerAccessNew.getUrl());
                softAssert.assertEquals(offerAccess.getType(), offerAccessNew.getType());
                softAssert.assertEquals(offerAccess.getLogin(), offerAccessNew.getLogin());
                softAssert.assertEquals(offerAccess.getPassword(), offerAccessNew.getPassword());
                softAssert.assertEquals(offerAccess.getEmail(), offerAccessNew.getEmail());
                softAssert.assertEquals(offerAccess.getDescription(), offerAccessNew.getDescription());
                softAssert.assertAll();
                isAssert = true;
            }
        }
        Assert.assertTrue(isAssert);
    }
}