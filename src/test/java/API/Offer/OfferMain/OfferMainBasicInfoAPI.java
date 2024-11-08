package API.Offer.OfferMain;

import OfferDraftPackage.entity.OfferBasicInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static API.Helper.*;
import static API.Offer.OfferDraft.OfferDraftBasicInfoAPI.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.KEY;
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - get, edit, проверка
 для вкладки General Offer Main
 //TODO: 90% Done (осталось logo)
 */

public class OfferMainBasicInfoAPI {
    static int offerMainId;

    @Test
    public static void test() throws Exception {
        offerMainId = Integer.parseInt(getRandomValueFromBD("id", "offer"));
        Allure.step("Выбираем рандомный Offer Main id=" + offerMainId);

        Allure.step("Получаем Basic Info Offer Main id=" + offerMainId);
        basicInfoGet(true);

        Allure.step("Редактируем Basic Info Offer Main id=" + offerMainId);
        OfferBasicInfo offerBasicInfoEdit = new OfferBasicInfo();
        offerBasicInfoEdit.fillOfferBasicInfoWithRandomDataForAPI();
        offerBasicInfoEdit.setOfferId(offerMainId);
        basicInfoEdit(offerBasicInfoEdit);

        Allure.step(CHECK);
        basicInfoAssert(basicInfoGet(false), offerBasicInfoEdit);
    }

    public static OfferBasicInfo basicInfoEdit(OfferBasicInfo offerBasicInfo) {

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferBasicInfo(offerBasicInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = "https://api.admin.3tracks.link/offer/" + offerBasicInfo.getOfferId() + "/basic-info";
        System.out.println(path);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return offerBasicInfo;
    }

    public static OfferBasicInfo basicInfoGet(Boolean isShow) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer/" + offerMainId + "/basic-info");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println("Ответ на get: " + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject offer = data.getJSONObject("offer");

        OfferBasicInfo offerBasicInfo = new OfferBasicInfo();
        offerBasicInfo.setOfferId(offer.getInt("id"));
        offerBasicInfo.setTitle(offer.getString("title")); // НЕ МОЖЕТ БЫТЬ NULL
        offerBasicInfo.setStatus(offer.getString("status"));
        offerBasicInfo.setPrivacyLevel(offer.getString("privacyLevel"));
        offerBasicInfo.setNotes(offer.isNull("notes") ? null : offer.getString("notes"));
        offerBasicInfo.setReconciliation(offer.isNull("reconciliation") ? null : offer.getString("reconciliation"));
        offerBasicInfo.setPayouts(offer.isNull("payouts") ? null : offer.getString("payouts"));
        offerBasicInfo.setReleaseDate(offer.isNull("releaseDate") ? null : offer.getString("releaseDate"));
        offerBasicInfo.setStopDate(offer.isNull("stopDate") ? null : offer.getString("stopDate"));
        offerBasicInfo.setSendBeforeStopping(offer.isNull("sendBeforeStopping") ? null : offer.getInt("sendBeforeStopping"));
        offerBasicInfo.setTop(offer.isNull("isTop") ? null : offer.getBoolean("isTop"));
        offerBasicInfo.setStatusNotice(offer.isNull("statusNotice") ? null : offer.getBoolean("statusNotice"));
        offerBasicInfo.setPreviewUrl(offer.isNull("previewUrl") ? null : offer.getString("previewUrl"));  // НЕ МОЖЕТ БЫТЬ NULL

        offerBasicInfo.setKpi(getTemplateFromJson(offer, "kpi"));
        offerBasicInfo.setPaidGoal(getTemplateFromJson(offer, "paidGoal"));
        offerBasicInfo.setDescription(getTemplateFromJson(offer, "description"));

        JSONObject advert = offer.getJSONObject("advert"); // // НЕ МОЖЕТ БЫТЬ NULL
        offerBasicInfo.setAdvertId(advert.getInt("value"));
        offerBasicInfo.setAdvertName(advert.getString("label"));

        offerBasicInfo.setTagId(getArrayFromJson(offer, "tag"));
        offerBasicInfo.setTrafficSourceId(getArrayFromJson(offer, "trafficSource"));
        offerBasicInfo.setCategoriesId(getArrayFromJson(offer, "category"));
        return offerBasicInfo;
    }
}