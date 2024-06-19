package AdvertPackage.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static Helper.Adverts.*;
import static Helper.Auth.authKeyTest;
import static SQL.AdvertSQL.getRandomValueFromBD;

public class AdvertNotesAPI {

    static int advertId = 1035;

    static JsonObject jsonAdvertPrimaryInfo = new JsonObject();

    private static JsonObject initializeJsonAdvertPIBody() throws Exception {
        String type = getRandomValue(NOTES_TYPES_MAP).toString().toLowerCase();
        jsonAdvertPrimaryInfo.addProperty("type", type);  // call, conference, meeting
        if (type.equals("conference")){
            jsonAdvertPrimaryInfo.addProperty("location",
                    getRandomValueFromBD("id", "conference"));
        }
        jsonAdvertPrimaryInfo.addProperty("text", generateName(10, DESCRIPTION_WORDS));
        return jsonAdvertPrimaryInfo;
    }

    @Test
    public static void advertEdit() throws Exception {
        for (int i = 1; i < 105; i++) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPIBody(), JsonObject.class);

            Response response;
            response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", authKeyTest)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .post("https://api.admin.3tracks.link/advert/" + advertId + "/notes/add");
            // Получаем и выводим ответ
            String responseBody = response.getBody().asString();
            System.out.println("Ответ: " + responseBody);
        }
    }
}