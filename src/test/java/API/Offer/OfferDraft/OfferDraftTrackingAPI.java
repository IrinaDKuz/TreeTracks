package API.Offer.OfferDraft;

import AdvertPackage.entity.AdvertContact;
import OfferDraftPackage.entity.OfferBasicInfo;
import OfferDraftPackage.entity.OfferTracking;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static API.Helper.getArrayFromJson;
import static API.Helper.getTemplateFromJson;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - add, проверка, get, edit, проверка, delete
 для вкладки Tracking Offer Draft
 */

//TODO: 0% Done (осталось logo)

public class OfferDraftTrackingAPI {
    static int offerDraftId;
    static int landingPageId;

    @Test
    public static void test() throws Exception {
        offerDraftId = Integer.parseInt(getRandomValueFromBD("id", "offer_draft"));
        System.out.println(offerDraftId);
        trackingGetShow();
        OfferTracking offerTrackingEdit = trackingAddEdit();
        basicInfoAssert(trackingGet(true), offerTrackingEdit);
        //deleteMethod("offer-draft", String.valueOf(offerDraftId));
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
        System.out.println("___ Подготовленные данные:___");
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        System.out.println("___ _____________________ ___");

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
        if (isShow)
            System.out.println("Ответ на get: " + responseBody);

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

    public static void trackingGetShow() {
        trackingGet(true);
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
        System.out.println("___ Корректная запись полей проверена V ___");
    }
}