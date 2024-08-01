package API.Offer.OfferMain;

import OfferMainPackage.entity.OfferMain;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - просмотр(get),
 TODO 0 DONE + https://api.admin.3tracks.link/offer/28/add-affiliate

 проверка
 для Offer Main
 */

public class OfferMainPageAPI_NEED_TO_DO {
    static int offerId;

    @Test
    public static void test() throws Exception {
        offerId = 31;
                //Integer.parseInt(getRandomValueFromBD("id", "offer"));
        System.out.println(offerId);
        offerMainGet();
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