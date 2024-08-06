package API.Offer.OfferDraft;

import OfferDraftPackage.entity.OfferBasicInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static API.Helper.*;
import static Helper.AllureHelper.*;
import static Helper.AllureHelper.DATA;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - add, проверка, get, edit, проверка, delete
 для вкладки General Offer Draft
 //TODO: 90% Done (осталось logo)
 */

public class OfferDraftBasicInfoAPI {
    static int offerDraftId;

    @Test
    public static void test() throws Exception {
        Allure.step("Добавляем Оффер Драфт");
        OfferBasicInfo offerBasicInfoAdd = basicInfoAddEdit(false);
        Allure.step(CHECK);
        basicInfoAssert(basicInfoGet(false), offerBasicInfoAdd);

        Allure.step("Получаем Basic Info Оффер Драфта id=" + offerDraftId);
        basicInfoGet(true);

        Allure.step("Редактируем Basic Info Оффер Драфта id=" + offerDraftId);
        OfferBasicInfo offerBasicInfoEdit = basicInfoAddEdit(true);
        Allure.step(CHECK);
        basicInfoAssert(basicInfoGet(false), offerBasicInfoEdit);

       // deleteMethod("offer-draft", String.valueOf(offerDraftId));
      //  assertDelete(String.valueOf(offerDraftId), "offer_draft");
    }

    public static JsonObject initializeJsonOfferBasicInfo(OfferBasicInfo offerBasicInfo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", offerBasicInfo.getTitle());
        jsonObject.addProperty("status", offerBasicInfo.getStatus());
        jsonObject.addProperty("advert", offerBasicInfo.getAdvertId());
        jsonObject.addProperty("statusNotice", offerBasicInfo.getStatusNotice());
        jsonObject.addProperty("privacyLevel", offerBasicInfo.getPrivacyLevel());
        jsonObject.addProperty("isTop", offerBasicInfo.isTop());
        jsonObject.addProperty("releaseDate", offerBasicInfo.getReleaseDate());
        jsonObject.addProperty("stopDate", offerBasicInfo.getStopDate());
        jsonObject.addProperty("sendBeforeStopping", offerBasicInfo.getSendBeforeStopping());
        jsonObject.addProperty("notes", offerBasicInfo.getNotes());
        jsonObject.addProperty("reconciliation", offerBasicInfo.getReconciliation());
        jsonObject.addProperty("payouts", offerBasicInfo.getPayouts());
        jsonObject.addProperty("previewUrl", offerBasicInfo.getPreviewUrl());

        List<Integer> tagList = offerBasicInfo.getTagId();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        jsonObject.add("tag", tagArray);

        List<Integer> categoryList = offerBasicInfo.getCategoriesId();
        JsonArray categoryArray = new JsonArray();
        categoryList.forEach(categoryArray::add);
        jsonObject.add("category", categoryArray);

        List<Integer> trafficSourceList = offerBasicInfo.getTrafficSourceId();
        JsonArray trafficSourceArray = new JsonArray();
        trafficSourceList.forEach(trafficSourceArray::add);
        jsonObject.add("trafficSource", trafficSourceArray);

        JsonArray descriptionArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerBasicInfo.getDescription().getTemplateMap().entrySet()) {
            JsonObject descriptionObject = new JsonObject();
            descriptionObject.addProperty("lang", entry.getKey());
            descriptionObject.addProperty("text", entry.getValue());
            descriptionArray.add(descriptionObject);
        }

        JsonArray kpiArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerBasicInfo.getKpi().getTemplateMap().entrySet()) {
            JsonObject kpiObject = new JsonObject();
            kpiObject.addProperty("lang", entry.getKey());
            kpiObject.addProperty("text", entry.getValue());
            kpiArray.add(kpiObject);
        }

        JsonArray paidGoalArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerBasicInfo.getPaidGoal().getTemplateMap().entrySet()) {
            JsonObject paidGoalObject = new JsonObject();
            paidGoalObject.addProperty("lang", entry.getKey());
            paidGoalObject.addProperty("text", entry.getValue());
            paidGoalArray.add(paidGoalObject);
        }

        jsonObject.add("kpi", kpiArray);
        jsonObject.add("description", descriptionArray);
        jsonObject.add("paidGoal", paidGoalArray);
        return jsonObject;
    }

    public static OfferBasicInfo basicInfoAddEdit(Boolean isEdit) throws Exception {
        OfferBasicInfo offerBasicInfo = new OfferBasicInfo();
        offerBasicInfo.fillOfferBasicInfoWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferBasicInfo(offerBasicInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ?
                "https://api.admin.3tracks.link/offer-draft/" + offerDraftId + "/basic-info"
                : "https://api.admin.3tracks.link/offer-draft/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            offerDraftId = jsonResponse.getJSONObject("data").getInt("offerId");
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return offerBasicInfo;
    }

    public static OfferBasicInfo basicInfoGet(Boolean isShow) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer-draft/" + offerDraftId + "/basic-info");

        String responseBody = response.getBody().asString();
        if (isShow) {
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject offer = data.getJSONObject("offerDraft");

        OfferBasicInfo offerBasicInfo = new OfferBasicInfo();
        offerBasicInfo.setOfferId(offer.getInt("id"));
        offerBasicInfo.setTitle(offer.isNull("title") ? null : offer.getString("title"));
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

        if (!offer.isNull("advert")) {
            JSONObject advert = offer.getJSONObject("advert");
            offerBasicInfo.setAdvertId(advert.getInt("value"));
            offerBasicInfo.setAdvertName(advert.getString("label"));
        }

        offerBasicInfo.setTagId(getArrayFromJson(offer, "tag"));
        offerBasicInfo.setTrafficSourceId(getArrayFromJson(offer, "trafficSource"));
        offerBasicInfo.setCategoriesId(getArrayFromJson(offer, "category"));
        return offerBasicInfo;
    }

    public static void basicInfoAssert(OfferBasicInfo offerBasicInfo, OfferBasicInfo offerBasicInfoEdit) {
        Assert.assertEquals(offerBasicInfo.getStatus(), offerBasicInfoEdit.getStatus());
        Assert.assertEquals(offerBasicInfo.getTitle(), offerBasicInfoEdit.getTitle());
        Assert.assertEquals(offerBasicInfo.getAdvertId(), offerBasicInfoEdit.getAdvertId());
        Collections.sort(offerBasicInfo.getTrafficSourceId());
        Collections.sort(offerBasicInfoEdit.getTrafficSourceId());
        Assert.assertEquals(offerBasicInfo.getTrafficSourceId(), offerBasicInfoEdit.getTrafficSourceId());
        Assert.assertEquals(offerBasicInfo.getPrivacyLevel(), offerBasicInfoEdit.getPrivacyLevel());
        Assert.assertEquals(offerBasicInfo.isTop(), offerBasicInfoEdit.isTop());
        Assert.assertEquals(offerBasicInfo.getStatusNotice(), offerBasicInfoEdit.getStatusNotice());
        String releaseDate = offerBasicInfo.getReleaseDate().substring(0, offerBasicInfo.getReleaseDate().length() - 6);
        String releaseDateEdit = offerBasicInfoEdit.getReleaseDate().substring(0, offerBasicInfoEdit.getReleaseDate().length() - 5);
        Assert.assertEquals(releaseDate, releaseDateEdit);
        String stopDate = offerBasicInfo.getStopDate().substring(0, offerBasicInfo.getStopDate().length() - 6);
        String stopDateEdit = offerBasicInfoEdit.getStopDate().substring(0, offerBasicInfoEdit.getStopDate().length() - 5);
        Assert.assertEquals(stopDate, stopDateEdit);
        Assert.assertEquals(offerBasicInfo.getSendBeforeStopping(), offerBasicInfoEdit.getSendBeforeStopping());
        Assert.assertEquals(offerBasicInfo.getNotes(), offerBasicInfoEdit.getNotes());
        Assert.assertEquals(offerBasicInfo.getReconciliation(), offerBasicInfoEdit.getReconciliation());
        Assert.assertEquals(offerBasicInfo.getPayouts(), offerBasicInfoEdit.getPayouts());
        Assert.assertEquals(offerBasicInfo.getPreviewUrl(), offerBasicInfoEdit.getPreviewUrl());

        Collections.sort(offerBasicInfo.getTagId());
        Collections.sort(offerBasicInfoEdit.getTagId());
        Assert.assertEquals(offerBasicInfo.getTagId(), offerBasicInfoEdit.getTagId());

        Collections.sort(offerBasicInfo.getCategoriesId());
        Collections.sort(offerBasicInfoEdit.getCategoriesId());
        Assert.assertEquals(offerBasicInfo.getCategoriesId(), offerBasicInfoEdit.getCategoriesId());

        Assert.assertEquals(offerBasicInfo.getDescription().getTemplateMap().entrySet(),
                offerBasicInfoEdit.getDescription().getTemplateMap().entrySet());
        Assert.assertEquals(offerBasicInfo.getKpi().getTemplateMap().entrySet(),
                offerBasicInfoEdit.getKpi().getTemplateMap().entrySet());
        Assert.assertEquals(offerBasicInfo.getPaidGoal().getTemplateMap().entrySet(),
                offerBasicInfoEdit.getPaidGoal().getTemplateMap().entrySet());
    }
}