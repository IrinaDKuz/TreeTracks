package API.Offer.OfferMain;

import OfferMainPackage.entity.OfferMain;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static API.Advert.AdvertFilterAPI.getUrlWithParameters;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Main Offer
 - getList, filters,
 */

public class OfferMainList {

    public final static Map<String, String> offersFields = new HashMap<>() {{
        put("id", "ids[]");
        put("title", "title");
        put("status", "status[]");
        put("privacy_level", "privacyLevel[]");
    }};
    public final static Map<String, String> offersCategoryFields = new HashMap<>() {{
        put("category_id", "category[]");
    }};
    public final static Map<String, String> offersTrafficSourceFields = new HashMap<>() {{
        put("traffic_source_id", "trafficSources[]");
    }};
    public final static Map<String, String> offersTagFields = new HashMap<>() {{
        put("tag_id", "tags[]");
    }};

    @Test
    public static void test() throws Exception {
        filterOffersTest();
        offersTestGet();
    }

    private static void filterOffersTest() throws Exception {
        for (Map.Entry<String, String> entry : offersFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "offer", entry.getKey());
            filterOffers(entry, value);
        }
        for (Map.Entry<String, String> entry : offersCategoryFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "offer_category");
            filterOffers(entry, value, "offer_category", "offer_id");
        }
        for (Map.Entry<String, String> entry : offersTrafficSourceFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "offer_traffic_source");
            filterOffers(entry, value, "offer_traffic_source", "offer_id");
        }
        for (Map.Entry<String, String> entry : offersTagFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "offer_tag");
            filterOffers(entry, value, "offer_tag", "offer_id");
        }
    }

    private static void filterOffers(Map.Entry<String, String> entry, String valueString, String tableName, String idRowName) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere(idRowName, tableName, entry.getKey(), valueString));
        filterOffersPost(entry.getValue(), valueString, ids);
    }

    private static void filterOffers(Map.Entry<String, String> entry, String valueString) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere("id", "offer", entry.getKey(), valueString));
        filterOffersPost(entry.getValue(), valueString, ids);
    }

    private static void filterOffersPost(String paramName, Object paramValue, Set<String> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);

        System.out.println("paramName = " + paramName);
        System.out.println("paramValue = " + paramValue);
        System.out.println("offerIds = " + ids);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(getUrlWithParameters("https://api.admin.3tracks.link/offer?", params));

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataArray = jsonObject.getJSONObject("data");
        JSONArray adverts = dataArray.getJSONArray("offers");

        List<String> filterIdList = new ArrayList<>();
        for (int i = 0; i < adverts.length(); i++) {
            JSONObject dataObject = adverts.getJSONObject(i);
            filterIdList.add(String.valueOf(dataObject.getInt("id")));
        }
        Collections.sort(filterIdList);
        ids = removeDeletedAdverts(ids);
        System.out.println(filterIdList);
        System.out.println(ids);
        Assert.assertEquals(filterIdList, ids);
    }

    private static Set<String> removeDeletedAdverts(Set<String> ids) {
        ids.removeIf(id -> !isInDatabase("id", id, "offer"));
        return ids;
    }

    public static void offersTestGet() {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/offer/");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray offers = data.getJSONArray("offers");

        for (int i = 0; i < offers.length(); i++) {
            OfferMain offerMain = new OfferMain();
            JSONObject dataObject = offers.getJSONObject(i);

            offerMain.getOfferGeneral().setStatus(dataObject.getString("status"));
            offerMain.getOfferGeneral().setTitle(dataObject.getString("title"));
            offerMain.getOfferGeneral().setPrivacyLevel(dataObject.getString("privacyLevel"));
            System.out.println(offerMain.getOfferGeneral().getPrivacyLevel());

            // offerGeneral.setTitle(data.isNull("email") ? null : data.getString("title"));
            //  offerGeneral.setStatusNotice(data.isNull("statusNotice") ? null : data.getBoolean("statusNotice"));

            // JSONArray role = data.getJSONArray("kpi");
            //offerGeneral.setRoleId(String.valueOf(role.isNull("value") ? null : role.getInt("value")));
        }
    }

}