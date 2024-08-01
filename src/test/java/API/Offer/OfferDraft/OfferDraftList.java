package API.Offer.OfferDraft;

import OfferMainPackage.entity.OfferMain;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Helper.AllureHelper.GET_RESPONSE;
import static Helper.AllureHelper.attachJson;
import static Helper.Auth.authKeyAdmin;
import static java.util.Objects.isNull;

/***
 Тест проверяет работу API методов Draft Offer
 - getList,
 TODO: 30% DONE
 */

public class OfferDraftList {

    @Test
    public static void test() throws Exception {
        Allure.description("ТЕСТ ЗАВЕРШЕН НА 30% - Проверка работы метода get Draft Offer List");
        Allure.step("Получаем список Draft Offer");
        offersGet();
    }

    public static void offersGet() {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer-draft/");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);
        attachJson(responseBody, GET_RESPONSE);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray offers = data.getJSONArray("offers");

        for (int i = 0; i < offers.length(); i++) {
            OfferMain offerMain = new OfferMain();
            JSONObject dataObject = offers.getJSONObject(i);

            offerMain.getOfferGeneral().setStatus(dataObject.isNull("status") ? null : dataObject.getString("status"));
            offerMain.getOfferGeneral().setTitle(dataObject.isNull("title") ? null : dataObject.getString("title"));
            offerMain.getOfferGeneral().setPrivacyLevel(dataObject.isNull("privacyLevel") ? null : dataObject.getString("privacyLevel"));

            // offerGeneral.setTitle(data.isNull("email") ? null : data.getString("title"));
            //  offerGeneral.setStatusNotice(data.isNull("statusNotice") ? null : data.getBoolean("statusNotice"));
            // JSONArray role = data.getJSONArray("kpi");
            //offerGeneral.setRoleId(String.valueOf(role.isNull("value") ? null : role.getInt("value")));
        }
    }
}