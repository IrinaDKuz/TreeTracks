package API.Advert;

import AdvertPackage.entity.AdvertNotes;
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
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - get, add, edit, проверка, delete
 во вкладке Адверт - "Notes"
 */

public class AdvertNotesAPI {
    public static int advertNotesId;
    public static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBD("id", "advert"));
        notesGet();
        notesAdd();
        AdvertNotes advertNotes = notesEdit();
        notesAssert(advertNotes);
        notesDelete();
    }

    private static JsonObject initializeJsonAdvertNotes(AdvertNotes advertNotes) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", advertNotes.getType().toLowerCase());
        if (advertNotes.getType().equalsIgnoreCase("conference")) {
            jsonObject.addProperty("location", advertNotes.getLocation());
        }
        jsonObject.addProperty("text", advertNotes.getText());
        return jsonObject;
    }

    public static void notesAdd() throws Exception {
        AdvertNotes advertNotes = new AdvertNotes();
        advertNotes.fillAdvertNotesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertNotes(advertNotes), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/notes/add");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        advertNotesId = jsonResponse.getJSONObject("data").getInt("notesId");
    }

    public static AdvertNotes notesEdit() throws Exception {
        AdvertNotes advertNotesEdit = new AdvertNotes();
        advertNotesEdit.fillAdvertNotesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertNotes(advertNotesEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

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

        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertNotesEdit;
    }

    public static ArrayList<AdvertNotes> notesGet() {
        ArrayList<AdvertNotes> notesList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/notes");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertNotes advertNotes = new AdvertNotes();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertNotes.setNotesId(dataObject.getInt("id"));

            JSONObject admin = dataObject.getJSONObject("admin");
            advertNotes.setAdminTitle(admin.getString("title"));

            advertNotes.setLocation(dataObject.isNull("location") ? null : dataObject.getString("location"));
            advertNotes.setText(dataObject.getString("text"));
            advertNotes.setType(dataObject.getString("type"));
            advertNotes.setCreatedAt(dataObject.getString("createdAt"));
            notesList.add(advertNotes);
        }
        return notesList;
    }

    public static void notesAssert(AdvertNotes advertNotesEdit) {
        ArrayList<AdvertNotes> AdvertNotesList = notesGet();
        for (AdvertNotes advertNotes : AdvertNotesList) {
            if (advertNotes.getNotesId() == advertNotesId) {
                System.out.println(advertNotes.getNotesId());
                Assert.assertEquals(advertNotes.getType(), advertNotesEdit.getType().toLowerCase());
                Assert.assertEquals(advertNotes.getText(), advertNotesEdit.getText());
                Assert.assertEquals(advertNotes.getLocation(), advertNotesEdit.getLocation());
            }
        }
    }

    public static void notesDelete() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/advert/" + advertId + "/notes/" + advertNotesId + "/delete");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }
}
