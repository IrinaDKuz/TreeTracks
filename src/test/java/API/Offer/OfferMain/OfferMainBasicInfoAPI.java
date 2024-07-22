package API.Offer.OfferMain;

import OfferDraftPackage.entity.OfferGeneral;
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
import java.util.Map;

import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - get, edit, проверка
 для вкладки General Offer Main
 */

//TODO доделать get, edit, delete

public class OfferMainBasicInfoAPI {
    static int offerId;

    @Test
    public static void test() throws Exception {
        offerId = 37;
                //Integer.parseInt(getRandomValueFromBD("id", "offer"));
        System.out.println(offerId);
        generalGet();
        OfferGeneral offerGeneral = generalEdit();
        generalAssert(offerGeneral);
    }

    private static JsonObject initializeJsonOfferGeneral(OfferGeneral offerGeneral) {
        JsonObject jsonObject = new JsonObject();
        JsonObject offerObject = new JsonObject();
        offerObject.addProperty("title", offerGeneral.getTitle());
        offerObject.addProperty("status", offerGeneral.getStatus());
        offerObject.addProperty("advertId", offerGeneral.getAdvertId());
        offerObject.addProperty("statusNotice", offerGeneral.getStatusNotice());
        offerObject.addProperty("privacyLevel", offerGeneral.getPrivacyLevel());
        offerObject.addProperty("isTop", offerGeneral.isTop());
        offerObject.addProperty("releaseDate", offerGeneral.getReleaseDate());
        offerObject.addProperty("stopDate", offerGeneral.getStopDate());
        offerObject.addProperty("sendBeforeStoping", offerGeneral.getSendBeforeStoping());
        offerObject.addProperty("notes", offerGeneral.getNotes());
        offerObject.addProperty("reconciliation", offerGeneral.getReconciliation());
        offerObject.addProperty("payouts", offerGeneral.getPayouts());

        List<Integer> tagList = offerGeneral.getTagId();
        JsonArray tagArray = new JsonArray();
       // tagList.forEach(tagArray::add);
        offerObject.add("tag", tagArray);

        List<Integer> categoryList = offerGeneral.getCategoriesId();
        JsonArray categoryArray = new JsonArray();
       // categoryList.forEach(categoryArray::add);
        offerObject.add("category", categoryArray);

        List<Integer> trafficSourceList = offerGeneral.getTrafficSourceId();
        JsonArray trafficSourceArray = new JsonArray();
       // trafficSourceList.forEach(trafficSourceArray::add);
       // trafficSourceList.add(2)
        offerObject.add("trafficSource", trafficSourceArray);

        JsonArray descriptionArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerGeneral.getDescription().getTemplateMap().entrySet()) {
            JsonObject descriptionObject = new JsonObject();
            descriptionObject.addProperty("lang", entry.getKey());
            descriptionObject.addProperty("text", entry.getValue());
            descriptionArray.add(descriptionObject);
        }

        JsonArray kpiArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerGeneral.getKpi().getTemplateMap().entrySet()) {
            JsonObject kpiObject = new JsonObject();
            kpiObject.addProperty("lang", entry.getKey());
            kpiObject.addProperty("text", entry.getValue());
            kpiArray.add(kpiObject);
        }

        JsonArray paidGoalArray = new JsonArray();
        for (Map.Entry<String, String> entry : offerGeneral.getPaidGoal().getTemplateMap().entrySet()) {
            JsonObject paidGoalObject = new JsonObject();
            paidGoalObject.addProperty("lang", entry.getKey());
            paidGoalObject.addProperty("text", entry.getValue());
            paidGoalArray.add(paidGoalObject);
        }

        offerObject.add("kpi", kpiArray);
        offerObject.add("description", descriptionArray);
        offerObject.add("paidGoal", paidGoalArray);

        jsonObject.add("offer", offerObject);
        jsonObject.addProperty("tab", "general");

        return jsonObject;
    }

    public static OfferGeneral generalEdit() throws Exception {
        OfferGeneral offerGeneral = new OfferGeneral();
        offerGeneral.fillOfferGeneralWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferGeneral(offerGeneral), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        String path = "https://api.admin.3tracks.link/offer/" + offerId + "/edit";

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
        return offerGeneral;
    }

    public static OfferGeneral generalGet() {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer/" + offerId + "/basic-info");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject offer = data.getJSONObject("offer");

        OfferGeneral offerGeneral = new OfferGeneral();
        offerGeneral.setOfferId(offer.getInt("id"));
        offerGeneral.setTitle(offer.getString("title"));
        offerGeneral.setStatus(offer.getString("status"));
        offerGeneral.setPrivacyLevel(offer.getString("privacyLevel"));
        offerGeneral.setNotes(offer.isNull("notes") ? null : data.getString("notes"));
        offerGeneral.setReconciliation(offer.isNull("reconciliation") ? null : data.getString("reconciliation"));
        offerGeneral.setPayouts(offer.isNull("payouts") ? null : data.getString("payouts"));
        offerGeneral.setReleaseDate(offer.isNull("releaseDate") ? null : data.getString("releaseDate"));
        offerGeneral.setStopDate(offer.isNull("stopDate") ? null : data.getString("stopDate"));
        offerGeneral.setSendBeforeStoping(offer.isNull("sendBeforeStopping") ? null : data.getInt("sendBeforeStopping"));

        offerGeneral.setTop(offer.isNull("statusNotice") ? null : data.getBoolean("statusNotice"));
        offerGeneral.setStatusNotice(offer.isNull("statusNotice") ? null : data.getBoolean("statusNotice"));

        JSONArray kpiArray = jsonObject.optJSONArray("kpi");
        if (kpiArray != null) {
            for (int i = 0; i < kpiArray.length(); i++) {
                JSONObject kpiItem = kpiArray.getJSONObject(i);
                offerGeneral.setKpi();

                String lang = kpiItem.optString("lang", "unknown");
                String text = kpiItem.optString("text", "no text");
                System.out.println("Lang: " + lang);
                System.out.println("Text: " + text);
            }




        JSONArray kpiArray = offer.getJSONArray("kpi");
        //offerGeneral.setRoleId(String.valueOf(role.isNull("value") ? null : role.getInt("value")));



        return offerGeneral;
    }

    public static void generalAssert(OfferGeneral offerGeneralEdit) {
        OfferGeneral offerGeneral = generalGet();
        Assert.assertEquals(offerGeneral.getStatus(), offerGeneralEdit.getStatus());
    }
}