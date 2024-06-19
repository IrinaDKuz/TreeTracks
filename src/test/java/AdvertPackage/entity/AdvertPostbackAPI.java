package AdvertPackage.entity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static Helper.ActionsClass.generateRandomString;
import static Helper.ActionsClass.getRandomBoolean;
import static Helper.Adverts.*;
import static Helper.Adverts.generateIPAddress;
import static Helper.Auth.authKeyTest;
import static SQL.AdvertSQL.getRandomValueFromBD;

public class AdvertPostbackAPI {

    static int advertId = 996;
    static JsonObject jsonAdvertPrimaryInfo = new JsonObject();

    private static JsonObject initializeJsonAdvertPIBody() {
        jsonAdvertPrimaryInfo.addProperty("securePostbackCode", generateRandomString(10 ));

        JsonArray allowedIPArray = new JsonArray();
        allowedIPArray.add(generateIPAddress());
        jsonAdvertPrimaryInfo.add("allowedIp", allowedIPArray);

        JsonArray allowedSubAccountArray = new JsonArray();
        allowedSubAccountArray.add("sub1");
        allowedSubAccountArray.add("sub5");
        jsonAdvertPrimaryInfo.add("allowedSubAccount", allowedSubAccountArray);

        JsonArray disallowedSubAccountArray = new JsonArray();
        disallowedSubAccountArray.add("sub6");
        disallowedSubAccountArray.add("sub3");
        jsonAdvertPrimaryInfo.add("disallowedSubAccount", disallowedSubAccountArray);

        jsonAdvertPrimaryInfo.addProperty("forbidChangePostbackStatus", getRandomBoolean());
        return jsonAdvertPrimaryInfo;
    }

    @Test
    public static void advertEdit() {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPIBody(), JsonObject.class);
        System.out.println(jsonObject);
            Response response;
            response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", authKeyTest)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .post("https://api.admin.3tracks.link/advert/" + advertId + "/postback/edit");
            // Получаем и выводим ответ
            String responseBody = response.getBody().asString();
            System.out.println("Ответ: " + responseBody);
        }
    }