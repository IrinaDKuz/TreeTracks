package API.Offer.OfferDraft;

import OfferDraftPackage.entity.OfferTracking;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.List;

import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - get, edit, проверка
 для вкладки Tracking Offer Draft
 //TODO: 100% Done
 */


public class OfferDraftTrackingAPI {
    static int offerDraftId;
    static int landingPageId;

    @Test
    public static void test() throws Exception {
        offerDraftId = 210; // Integer.parseInt(getRandomValueFromBD("id", "offer_draft"));
        System.out.println(offerDraftId);
        Allure.step("Получаем Tracking Info у рандомного Оффера " + offerDraftId);
        trackingGet(true);
        Allure.step("Редактируем Tracking Info");
        OfferTracking offerTrackingEdit = trackingAddEdit();
        Allure.step(CHECK);
        basicInfoAssert(trackingGet(false), offerTrackingEdit);
    }

    public static JsonObject initializeJsonOfferBasicInfo(OfferTracking offerTracking) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("trackingUrl", offerTracking.getTrackingUrl());
        jsonObject.addProperty("additionalMacro", offerTracking.getAdditionalMacro());
        jsonObject.addProperty("trafficbackUrl", offerTracking.getTrafficbackUrl());
        jsonObject.addProperty("trackingDomainUrl", offerTracking.getTrackingDomainUrlId());
        jsonObject.addProperty("redirectType", offerTracking.getRedirectType());
        jsonObject.addProperty("allowDeeplinks", offerTracking.getAllowDeeplinks());
        jsonObject.addProperty("allowImpressions", offerTracking.getAllowImpressions());

        JsonArray landingPagesArray = new JsonArray();
        for (OfferTracking.LandingPage landingPage : offerTracking.getLandingPages()) {
            JsonObject landingPageObject = new JsonObject();
            landingPageObject.addProperty("title", landingPage.getLandingPageTitle());
            landingPageObject.addProperty("trackingUrl", landingPage.getLandingPageTrackingUrl());
            landingPageObject.addProperty("previewUrl", landingPage.getLandingPagePreviewUrl());
            landingPageObject.addProperty("type", landingPage.getLandingPageType());
            landingPagesArray.add(landingPageObject);
        }
        jsonObject.add("landingPage", landingPagesArray);
        return jsonObject;
    }

    public static OfferTracking trackingAddEdit() throws Exception {
        OfferTracking offerTracking = new OfferTracking();
        offerTracking.fillOfferTrackingWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferBasicInfo(offerTracking), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = "https://api.admin.3tracks.link/offer-draft/" + offerDraftId + "/tracking";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return offerTracking;
    }

    public static OfferTracking trackingGet(Boolean isShow) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer-draft/" + offerDraftId + "/tracking");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println("Ответ на get: " + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject offer = data.getJSONObject("offerDraft");

        OfferTracking offerTracking = new OfferTracking();
        offerTracking.setOfferId(offer.getInt("id"));
        offerTracking.setTrackingUrl(offer.isNull("trackingUrl") ? null : offer.getString("trackingUrl"));
        offerTracking.setTrafficbackUrl(offer.isNull("trafficbackUrl") ? null : offer.getString("trafficbackUrl"));
        offerTracking.setTrackingDomainUrlId(offer.isNull("trackingDomainUrl") ? null : offer.getInt("trackingDomainUrl"));
        offerTracking.setAdditionalMacro(offer.isNull("additionalMacro") ? null : offer.getString("additionalMacro"));
        offerTracking.setRedirectType(offer.isNull("redirectType") ? null : offer.getString("redirectType"));
        offerTracking.setAllowDeeplinks(offer.isNull("allowDeeplinks") ? null : offer.getBoolean("allowDeeplinks"));
        offerTracking.setAllowImpressions(offer.isNull("allowImpressions") ? null : offer.getBoolean("allowImpressions"));
        List<OfferTracking.LandingPage> landingPages = new ArrayList<>();
        if (!offer.isNull("landingPage")) {
            JSONArray landingPagesArray = offer.getJSONArray("landingPage");
            for (int i = 0; i < landingPagesArray.length(); i++) {
                JSONObject landingPagesObject = landingPagesArray.getJSONObject(i);
                OfferTracking.LandingPage landingPage = new OfferTracking.LandingPage();
                landingPage.setLandingPageTitle(landingPagesObject.getString("title"));
                landingPage.setLandingPageTrackingUrl(landingPagesObject.getString("trackingUrl"));
                landingPage.setLandingPagePreviewUrl(landingPagesObject.getString("previewUrl"));
                landingPage.setLandingPageType(landingPagesObject.getString("type"));
                landingPage.setLandingPageUpdatedAt(landingPagesObject.isNull("updatedAt") ? null : landingPagesObject.getString("updatedAt"));
                landingPages.add(landingPage);
            }
            offerTracking.setLandingPages(landingPages);
        }
        return offerTracking;
    }

    public static void basicInfoAssert(OfferTracking offerTracking, OfferTracking offerTrackingEdit) {
        Assert.assertEquals(offerTracking.getTrackingUrl(), offerTrackingEdit.getTrackingUrl());
        Assert.assertEquals(offerTracking.getAdditionalMacro(), offerTrackingEdit.getAdditionalMacro());
        Assert.assertEquals(offerTracking.getTrafficbackUrl(), offerTrackingEdit.getTrafficbackUrl());
        Assert.assertEquals(offerTracking.getTrackingDomainUrlId(), offerTrackingEdit.getTrackingDomainUrlId());
        Assert.assertEquals(offerTracking.getRedirectType(), offerTrackingEdit.getRedirectType());
        Assert.assertEquals(offerTracking.getAllowDeeplinks(), offerTrackingEdit.getAllowDeeplinks());
        Assert.assertEquals(offerTracking.getAllowImpressions(), offerTrackingEdit.getAllowImpressions());
        for (int i = 0; i < offerTracking.getLandingPages().size(); i++) {
            OfferTracking.LandingPage lp = offerTracking.getLandingPages().get(i);
            OfferTracking.LandingPage lpEdit = offerTrackingEdit.getLandingPages().get(i);
            Assert.assertEquals(lp.getLandingPageTitle(), lpEdit.getLandingPageTitle());
            Assert.assertEquals(lp.getLandingPageTrackingUrl(), lpEdit.getLandingPageTrackingUrl());
            Assert.assertEquals(lp.getLandingPagePreviewUrl(), lpEdit.getLandingPagePreviewUrl());
            Assert.assertEquals(lp.getLandingPageType(), lpEdit.getLandingPageType());
            Assert.assertEquals(lp.getLandingPageTitle(), lpEdit.getLandingPageTitle());
        }
    }
}