package API.Affiliate;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertPrimaryInfo;
import AffiliatePackage.entity.Affiliate;
import Helper.Affiliates;
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
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.StreamSupport;

import static API.Helper.*;
import static AdvertPackage.entity.AdvertContact.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.KEY;
import static Helper.Auth.authApi;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete, проверка
 во вкладке Affiliate - "Primary Info"
 //TODO: 0% Done
 */

public class AffiliatePrimaryInfoAPI {
    static Integer affiliateId;

    @Test
    public static void test() throws Exception {

        authApi(103);
        Allure.step("Добавляем Аффилейта");

        Affiliate affiliatePrimaryInfoAdd = new Affiliate();
        affiliatePrimaryInfoAdd.fillAffiliateWithRandomData();
        primaryInfoAddEdit(false, affiliatePrimaryInfoAdd);
        affiliateId = affiliatePrimaryInfoAdd.getId();
        Allure.step(CHECK);
        primaryInfoAssert(affiliatePrimaryInfoAdd, primaryInfoGet(true));

        Allure.step("Получаем Primary Info Аффилейта id=" + affiliateId);
        primaryInfoGet(true);

        Allure.step("Редактируем Primary Info Аффилейта id=" + affiliateId);
        Affiliate affiliatePrimaryInfoEdit = new Affiliate();
        affiliatePrimaryInfoEdit.fillAffiliateWithRandomData();
        affiliatePrimaryInfoEdit.setId(affiliatePrimaryInfoAdd.getId());

        primaryInfoAddEdit(true, affiliatePrimaryInfoEdit);
        Allure.step(CHECK);
        primaryInfoAssert(affiliatePrimaryInfoEdit, primaryInfoGet(true));

        Allure.step("Удаляем Аффилейта (чтобы не засорять базу) id=" + affiliateId);
       // deleteMethod("affiliate", String.valueOf(affiliateId));
       // assertDelete(String.valueOf(affiliateId), "advert");

       /* advertId = Integer.parseInt(getFrequentValueFromBDNotNull("advert_id", "offer"));
        Allure.step("Проверка для Адверта, к которому присоединено несколько офферов advertId=" + advertId);

        AdvertPrimaryInfo advertPrimaryInfoEdit2 = primaryInfoAddEdit(true, advertId);
        primaryInfoAssert(advertPrimaryInfoEdit2, primaryInfoGet(true));*/
    }

    private static JsonObject initializeJsonAdvertPrimaryInfo(Affiliate affiliate) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", affiliate.getEmail());
        jsonObject.addProperty("plainPassword", affiliate.getPlainPassword());
        jsonObject.addProperty("managerId", affiliate.getManagerId());
        jsonObject.addProperty("status", affiliate.getStatus());
        jsonObject.addProperty("referralPercent", affiliate.getReferralPercent());
        jsonObject.addProperty("address1", affiliate.getAddress1());
        jsonObject.addProperty("address2", affiliate.getAddress2());
        jsonObject.addProperty("city", affiliate.getCity());
        jsonObject.addProperty("country", affiliate.getCountry());
        jsonObject.addProperty("zipCode", affiliate.getZipCode());
        jsonObject.addProperty("refererDesc", affiliate.getRefererDesc());
        jsonObject.addProperty("note", affiliate.getNote());
        jsonObject.addProperty("hideConversionPercent", affiliate.getHideConversionPercent());


        List<String> allowedSubAccountList = affiliate.getAllowedSubAccount();
        JsonArray allowedSubAccount = new JsonArray();
        allowedSubAccountList.forEach(allowedSubAccount::add);
        jsonObject.add("allowedSubAccount", allowedSubAccount);

        List<String> disallowedSubAccountList = affiliate.getDisallowedSubAccount();
        JsonArray disallowedSubAccount = new JsonArray();
        disallowedSubAccountList.forEach(disallowedSubAccount::add);
        jsonObject.add("disallowedSubAccount", disallowedSubAccount);

        Set<Integer> tagList = affiliate.getTag();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        jsonObject.add("tag", tagArray);

        Set<Integer> categoriesList = affiliate.getCategory();
        JsonArray categoriesArray = new JsonArray();
        categoriesList.forEach(categoriesArray::add);
        jsonObject.add("category", categoriesArray);

        Set<Integer> trafficSourceList = affiliate.getTrafficSource();
        JsonArray trafficSourceArray = new JsonArray();
        trafficSourceList.forEach(trafficSourceArray::add);
        jsonObject.add("trafficSource", trafficSourceArray);

        Set<Integer> geoList = affiliate.getTrafficGeo();
        JsonArray geoArray = new JsonArray();
        geoList.forEach(geoArray::add);
        jsonObject.add("trafficGeo", geoArray);


        JsonArray messengersArray = new JsonArray();

        for (Messenger messenger : affiliate.getMessenger()) {
            JsonObject jsonMessenger = new JsonObject();
            if (messenger.getMessengerId() != null) {
                jsonMessenger.addProperty("id", messenger.getMessengerId());
            }
            jsonMessenger.addProperty("messengerId", messenger.getMessengerTypeId());
            jsonMessenger.addProperty("value", messenger.getMessengerValue());
            messengersArray.add(jsonMessenger);
        }
        jsonObject.add("messengers", messengersArray);

        return jsonObject;
    }

    public static Affiliate primaryInfoAddEdit(Boolean isEdit, Affiliate affiliatePrimaryInfo) {

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPrimaryInfo(affiliatePrimaryInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? URL + "/affiliate/" + affiliatePrimaryInfo.getId() + "/edit" :
                URL + "/affiliate/new";

        System.out.println(path);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            affiliatePrimaryInfo.setId(jsonResponse.getJSONObject("data").getInt("id"));
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
            affiliatePrimaryInfo.setId(affiliateId);
        }
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return affiliatePrimaryInfo;
    }

    public static Affiliate primaryInfoGet(Boolean isShow) {
        Affiliate affiliate = new Affiliate();
        affiliate.setId(affiliateId);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(URL + "/affiliate/" + affiliateId);

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        affiliate.setEmail(data.isNull("email") ? null : data.getString("email"));
        affiliate.setName(data.isNull("name") ? null : data.getString("name"));
        affiliate.setSource(data.isNull("source") ? null : data.getString("source"));
        affiliate.setTimeZone(data.isNull("timeZone") ? null : data.getString("timeZone"));
        affiliate.setManagerId(data.isNull("manager") ? null : data.getInt("manager"));
        affiliate.setStatus(data.isNull("status") ? null : data.getString("status"));
        affiliate.setReferralPercent(data.isNull("referralPercent") ? null : data.getInt("referralPercent"));
        affiliate.setAddress1(data.isNull("address1") ? null : data.getString("address1"));
        affiliate.setAddress2(data.isNull("address2") ? null : data.getString("address2"));
        affiliate.setCity(data.isNull("city") ? null : data.getString("city"));
        affiliate.setCountry(data.isNull("country") ? null : data.getString("country"));
        affiliate.setZipCode(data.isNull("zipCode") ? null : data.getString("zipCode"));
        affiliate.setRefererDesc(data.isNull("refererDesc") ? null : data.getString("refererDesc"));
        affiliate.setNote(data.isNull("note") ? null : data.getString("note"));

        affiliate.setAllowedSubAccount(getStringArrayFromJson(data,"allowedSubAccount"));
        affiliate.setDisallowedSubAccount(getStringArrayFromJson(data,"disallowedSubAccount"));
        Set<Integer> categorySet = new HashSet<>(getArrayFromJson(data, "categories"));
        affiliate.setCategory(categorySet);

        Set<Integer> tagsSet = new HashSet<>(getArrayFromJson(data, "tags"));
        affiliate.setTag(tagsSet);

        Set<Integer> trafficsSourceSet = new HashSet<>(getArrayFromJson(data, "trafficsSource"));
        affiliate.setTrafficSource(trafficsSourceSet);

        if (data.get("trafficsGeo") instanceof JSONArray) {
            JSONArray trafficsGeoArray = data.getJSONArray("trafficsGeo");
            Set<Integer> trafficsGeoIdSet = new HashSet<>();
            for (int i = 0; i < trafficsGeoArray.length(); i++) {
                JSONObject trafficGeoObject = trafficsGeoArray.getJSONObject(i);
                int id = trafficGeoObject.getInt("id");
                trafficsGeoIdSet.add(id);
            }
            affiliate.setTrafficGeo(trafficsGeoIdSet);
        } else {
            affiliate.setTrafficGeo(null);
        }

        if (data.get("messengers") instanceof JSONArray) {
            JSONArray messengerArray = data.getJSONArray("messengers");
            List<Messenger> messengerList = new ArrayList<>();

            for (int i = 0; i < messengerArray.length(); i++) {
                JSONObject messengerObject = messengerArray.getJSONObject(i);
                Messenger messenger = new Messenger();
                messenger.setMessengerId(messengerObject.optString("messengerId"));
                messenger.setMessengerValue(messengerObject.optString("value"));
                messengerList.add(messenger);
            }
            affiliate.setMessenger(messengerList);
        } else {
            affiliate.setMessenger(null);
        }

        return affiliate;
    }

    public static void primaryInfoAssert(Affiliate affiliate, Affiliate affiliateGet) {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(affiliate.getEmail(), affiliateGet.getEmail(), "Email");
        softAssert.assertEquals(affiliate.getName(), affiliateGet.getName(), "Name");
        softAssert.assertEquals(affiliate.getSource(), affiliateGet.getSource(), "Source");
        softAssert.assertEquals(affiliate.getTimeZone(), affiliateGet.getTimeZone(), "TimeZone");
        softAssert.assertEquals(affiliate.getManagerId(), affiliateGet.getManagerId(), "ManagerId");
        softAssert.assertEquals(affiliate.getStatus(), affiliateGet.getStatus(), "Status");
        softAssert.assertEquals(affiliate.getReferralPercent(), affiliateGet.getReferralPercent(), "ReferralPercent");
        softAssert.assertEquals(affiliate.getAddress1(), affiliateGet.getAddress1(), "Address1");
        softAssert.assertEquals(affiliate.getAddress2(), affiliateGet.getAddress2(), "Address2");
        softAssert.assertEquals(affiliate.getCity(), affiliateGet.getCity(), "City");
        softAssert.assertEquals(affiliate.getCountry(), affiliateGet.getCountry(), "Country");
        softAssert.assertEquals(affiliate.getZipCode(), affiliateGet.getZipCode(), "ZipCode");
        softAssert.assertEquals(affiliate.getRefererDesc(), affiliateGet.getRefererDesc(), "RefererDesc");
        softAssert.assertEquals(affiliate.getNote(), affiliateGet.getNote(), "Note");

        softAssert.assertEquals(affiliate.getTag(), affiliateGet.getTag(), "Tag");
        softAssert.assertEquals(affiliate.getCategory(), affiliateGet.getCategory(), "Category");
        softAssert.assertEquals(affiliate.getTrafficSource(), affiliateGet.getTrafficSource(), "TrafficSource");

        softAssert.assertEquals(affiliate.getAllowedSubAccount(), affiliateGet.getAllowedSubAccount(), "AllowedSubAccount");
        softAssert.assertEquals(affiliate.getDisallowedSubAccount(), affiliateGet.getDisallowedSubAccount(), "DisallowedSubAccount");
        softAssert.assertEquals(affiliate.getTrafficGeo(), affiliateGet.getTrafficGeo(), "TrafficGeo");

       for (Messenger messenger : affiliate.getMessenger()){
          String type =  messenger.getMessengerTypeId();
          String value = messenger.getMessengerValue();
           System.out.println(type + " - "+ value);
       }

        for (Messenger messenger : affiliateGet.getMessenger()){
            String type =  messenger.getMessengerTypeId();
            String value = messenger.getMessengerValue();
            System.out.println(type + " - "+ value);
        }

        // softAssert.assertEquals(affiliate.getMessenger(), affiliateGet.getMessenger(), "Messenger");

        softAssert.assertAll();
    }
}