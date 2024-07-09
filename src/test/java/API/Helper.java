package API;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import static Helper.Auth.authKeyAdmin;

public class Helper {



    public static final String[] LANG = {"eng", "rus"};

    public static void deleteMethod(String url, String id) {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/" + url + "/" + id );

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на delete: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }
}
