package API.Offer.OfferMain;

import OfferDraftPackage.entity.OfferGeneral;
import OfferMainPackage.entity.OfferMain;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static API.Helper.deleteMethod;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - просмотр(get), delete,
 TODO https://api.admin.3tracks.link/offer/28/add-affiliate

 проверка
 для Offer Main
 */

public class OfferMainAPI {
    static int offerId;

    @Test
    public static void test() throws Exception {
        offerId = 31;
                //Integer.parseInt(getRandomValueFromBD("id", "offer"));
        System.out.println(offerId);
        offerMainGet();
        deleteMethod("offer-draft", String.valueOf(offerId));
    }

    public static OfferMain offerMainGet() {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer/" + offerId);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("offer");

        OfferMain offerMain = new OfferMain();
        offerMain.getOfferGeneral().setStatus(data.getString("status"));
        offerMain.getOfferGeneral().setTitle(data.isNull("email") ? null : data.getString("title"));
        offerMain.getOfferGeneral().setStatusNotice(data.isNull("statusNotice") ? null : data.getBoolean("statusNotice"));

        // JSONArray role = data.getJSONArray("kpi");
        //offerGeneral.setRoleId(String.valueOf(role.isNull("value") ? null : role.getInt("value")));
        return offerMain;
    }
}