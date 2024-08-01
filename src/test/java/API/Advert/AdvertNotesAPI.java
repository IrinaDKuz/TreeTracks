package API.Advert;

import AdvertPackage.entity.AdvertNotes;
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

import java.util.ArrayList;

import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - get, add, edit, проверка, delete
 во вкладке Адверт - "Notes"
 TODO: 100% DONE
 */

public class AdvertNotesAPI {
    static int advertNotesId;
    static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBD("id", "advert"));
        Allure.step("Получаем заметки у рандомного Адверта " + advertId);
        notesGet(true);
        Allure.step("Добавляем заметки Адверту");
        notesAdd();
        Allure.step("Редактируем заметку " + advertNotesId);
        AdvertNotes advertNotes = notesEdit();
        Allure.step(CHECK);
        notesAssert(advertNotes);
        Allure.step(DELETE + advertNotesId);
        deleteMethod("advert", advertId + "/notes/" + advertNotesId + "/delete");
    }

    private static JsonObject initializeJsonAdvertNotes(AdvertNotes advertNotes) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", advertNotes.getType().toLowerCase());
        if (advertNotes.getType().equalsIgnoreCase("event")) {
            jsonObject.addProperty("location", advertNotes.getLocationId());
        }
        jsonObject.addProperty("text", advertNotes.getText());
        return jsonObject;
    }

    public static void notesAdd() throws Exception {
        AdvertNotes advertNotes = new AdvertNotes();
        advertNotes.fillAdvertNotesWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertNotes(advertNotes), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/notes/add");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        advertNotesId = jsonResponse.getJSONObject("data").getInt("notesId");
    }

    public static AdvertNotes notesEdit() throws Exception {
        AdvertNotes advertNotesEdit = new AdvertNotes();
        advertNotesEdit.fillAdvertNotesWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertNotes(advertNotesEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/notes/" + advertNotesId + "/edit");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertNotesEdit;
    }

    public static ArrayList<AdvertNotes> notesGet(Boolean isShow) {
        ArrayList<AdvertNotes> notesList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/notes");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertNotes advertNotes = new AdvertNotes();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertNotes.setNotesId(dataObject.getInt("id"));

            JSONObject admin = dataObject.getJSONObject("admin");
            advertNotes.setAdminTitle(admin.getString("label"));
            advertNotes.setAdminId(admin.getInt("value"));


            if (dataObject.get("location") instanceof JSONObject locationObject) {
                advertNotes.setLocationId(String.valueOf(locationObject.getInt("value")));
                advertNotes.setLocation(locationObject.getString("label"));
            }

            advertNotes.setText(dataObject.getString("text"));
            advertNotes.setType(dataObject.getString("type"));
            advertNotes.setCreatedAt(dataObject.getString("createdAt"));
            notesList.add(advertNotes);
        }
        return notesList;
    }

    public static void notesAssert(AdvertNotes advertNotesEdit) {
        ArrayList<AdvertNotes> AdvertNotesList = notesGet(true);
        for (AdvertNotes advertNotes : AdvertNotesList) {
            if (advertNotes.getNotesId() == advertNotesId) {
                System.out.println(advertNotes.getNotesId());
                Assert.assertEquals(advertNotes.getType(), advertNotesEdit.getType().toLowerCase());
                Assert.assertEquals(advertNotes.getText(), advertNotesEdit.getText());
                Assert.assertEquals(advertNotes.getLocationId(), advertNotesEdit.getLocationId());
                Assert.assertEquals(advertNotes.getLocation(), advertNotesEdit.getLocation());
            }
        }
    }
}
