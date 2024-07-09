package API.Advert;

import AdvertPackage.entity.AdvertPostback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import java.util.stream.StreamSupport;
import static Helper.Auth.authKeyAdmin;
import static Helper.Auth.authKeyTest;
import static SQL.AdvertSQL.getRandomValueFromBDWhereMore;
import static java.util.Objects.isNull;

/***
 Тест проверяет работу API методов
 - get, add/edit, проверка
 во вкладке Адверт - "Postback"
 */

public class AdvertPostbackAPI {
    static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "1000"));
        postbackGet();
        AdvertPostback advertPostback = postbackEdit();
        postbackAssert(advertPostback);
    }

    private static JsonObject initializeJsonAdvertPostbackBody(AdvertPostback advertPostback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("securePostbackCode", advertPostback.getSecurePostbackCode());

        List<String> allowedIpList = advertPostback.getAllowedIp();
        JsonArray allowedIPArray = new JsonArray();
        allowedIpList.forEach(allowedIPArray::add);
        jsonObject.add("allowedIp", allowedIPArray);

        List<String> allowedSubAccountList = advertPostback.getAllowedSubAccount();
        JsonArray allowedSubAccountArray = new JsonArray();
        allowedSubAccountList.forEach(allowedSubAccountArray::add);
        jsonObject.add("allowedSubAccount", allowedSubAccountArray);

        List<String> disAllowedSubAccountList = advertPostback.getDisallowedSubAccount();
        JsonArray disAllowedSubAccountArray = new JsonArray();
        disAllowedSubAccountList.forEach(disAllowedSubAccountArray::add);
        jsonObject.add("forbiddenSubAccount", disAllowedSubAccountArray);

        jsonObject.addProperty("forbidChangePostbackStatus",
                isNull(advertPostback.getForbidChangePostbackStatus()) ? false : advertPostback.getForbidChangePostbackStatus());
        return jsonObject;
    }

    public static AdvertPostback postbackEdit() {
        AdvertPostback advertPostback = new AdvertPostback();
        advertPostback.fillAdvertPostbackWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPostbackBody(advertPostback), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/postback/edit");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertPostback;
    }

    public static AdvertPostback postbackGet() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/postback");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");

        AdvertPostback advertPostback = new AdvertPostback();
        advertPostback.setSecurePostbackCode(data.isNull("securePostbackCode") ? null : data.getString("securePostbackCode"));
        advertPostback.setForbidChangePostbackStatus(data.isNull("forbidChangePostbackStatus") ? false : data.getBoolean("forbidChangePostbackStatus"));

        if (data.get("allowedIp") instanceof JSONArray) {
            JSONArray jsonArray = data.getJSONArray("allowedIp");
            List<String> listArray = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPostback.setAllowedIp(listArray);
        } else advertPostback.setAllowedIp(null);


        if (data.get("allowedSubAccount") instanceof JSONArray) {
            JSONArray jsonArray = data.getJSONArray("allowedSubAccount");
            List<String> listArray = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPostback.setAllowedSubAccount(listArray);
        } else advertPostback.setAllowedSubAccount(null);

        if (data.get("forbiddenSubAccount") instanceof JSONArray) {
            JSONArray jsonArray = data.getJSONArray("forbiddenSubAccount");
            List<String> listArray = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPostback.setDisallowedSubAccount(listArray);
        } else advertPostback.setDisallowedSubAccount(null);

        return advertPostback;
    }

    public static void postbackAssert(AdvertPostback advertPostbackEdit) {
        AdvertPostback advertPostback = postbackGet();
        Assert.assertEquals(advertPostback.getSecurePostbackCode(), advertPostbackEdit.getSecurePostbackCode());
        Assert.assertEquals(advertPostback.getForbidChangePostbackStatus(), advertPostbackEdit.getForbidChangePostbackStatus());
        Assert.assertEquals(advertPostback.getAllowedIp(), advertPostbackEdit.getAllowedIp());
        Assert.assertEquals(advertPostback.getAllowedSubAccount(), advertPostbackEdit.getAllowedSubAccount());
        Assert.assertEquals(advertPostback.getDisallowedSubAccount(), advertPostbackEdit.getDisallowedSubAccount());
    }
}
