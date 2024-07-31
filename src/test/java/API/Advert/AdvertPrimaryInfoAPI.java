package API.Advert;

import AdvertPackage.entity.AdvertPrimaryInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.StreamSupport;

import static API.Helper.deleteMethod;
import static API.Helper.parseUnknownValueToInteger;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete, проверка
 во вкладке Адверт - "Primary Info"
 */

public class AdvertPrimaryInfoAPI {
    static int advertId;

    @Test
    public static void test() throws Exception {
        AdvertPrimaryInfo advertPrimaryInfoAdd = primaryInfoAddEdit(false);
        advertId = advertPrimaryInfoAdd.getAdvertId();
        primaryInfoAssert(advertPrimaryInfoAdd, primaryInfoGet(false));
        primaryInfoGet(true);

        AdvertPrimaryInfo advertPrimaryInfoEdit = primaryInfoAddEdit(true);
        primaryInfoAssert(advertPrimaryInfoEdit, primaryInfoGet(false));

        advertId = Integer.parseInt(getFrequentValueFromBD("advert_id", "offer"));
        Allure.step("Проверка для Адверта, к которому присоединено несколько офферов " + advertId);
        AdvertPrimaryInfo advertPrimaryInfoEdit2 = primaryInfoAddEdit(true);
        primaryInfoAssert(advertPrimaryInfoEdit2, primaryInfoGet(false));
    }

    private static JsonObject initializeJsonAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tab", "primary");

        JsonObject advertObject = new JsonObject();
        advertObject.addProperty("status", advertPrimaryInfo.getStatus().toLowerCase());
        advertObject.addProperty("name", advertPrimaryInfo.getCompany());
        advertObject.addProperty("managerId", Integer.parseInt(advertPrimaryInfo.getManagerId()));
        advertObject.addProperty("accountManager", Integer.parseInt(advertPrimaryInfo.getAccountManagerId()));
        advertObject.addProperty("salesManager", Integer.parseInt(advertPrimaryInfo.getSalesManagerId()));
        advertObject.addProperty("siteUrl", advertPrimaryInfo.getSiteUrl());
        advertObject.addProperty("companyLegalname", advertPrimaryInfo.getCompanyLegalName());
        advertObject.addProperty("note", advertPrimaryInfo.getNote());
        advertObject.addProperty("userRequestSourceId", Integer.parseInt(advertPrimaryInfo.getUserRequestSourceId()));
        advertObject.addProperty("userRequestSourceValue", advertPrimaryInfo.getUserRequestSourceValue());

        List<String> pricingModelList = advertPrimaryInfo.getPricingModel();
        JsonArray pricingModelArray = new JsonArray();
        pricingModelList.forEach(pricingModelArray::add);
        advertObject.add("pricingModel", pricingModelArray);

        List<Integer> tagList = advertPrimaryInfo.getTagId();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        advertObject.add("tag", tagArray);

        Set<Integer> categoriesList = advertPrimaryInfo.getCategoriesId();
        JsonArray categoriesArray = new JsonArray();
        categoriesList.forEach(categoriesArray::add);
        advertObject.add("categories", categoriesArray);

        List<String> geoList = advertPrimaryInfo.getGeoAbb();
        JsonArray geoArray = new JsonArray();
        geoList.forEach(geoArray::add);
        advertObject.add("geo", geoArray);

        jsonObject.add("advert", advertObject);
        return jsonObject;
    }

    public static AdvertPrimaryInfo primaryInfoAddEdit(Boolean isEdit) throws Exception {
        if (isEdit)
            Allure.step("Проверка редактирования Primary Info");
        else
            Allure.step("Проверка добавления нового Адверта и заполнения Primary Info");

        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
        advertPrimaryInfo.fillAdvertPrimaryInfoWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPrimaryInfo(advertPrimaryInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step("Данные для отправки: \n " + jsonObject.toString().replace("],", "],\n"));

        String path = isEdit ? "https://api.admin.3tracks.link/advert/" + advertId + "/edit" :
                "https://api.admin.3tracks.link/advert/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);
        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();

        if (isEdit) {
            Allure.step("Ответ на edit: " + responseBody);
            System.out.println("Ответ на edit: " + responseBody);
            Assert.assertTrue(responseBody.contains("{\"success\":true"));
            advertPrimaryInfo.setAdvertId(advertId);

        } else {
            Allure.step("Ответ на add: " + responseBody);
            System.out.println("Ответ на add: " + responseBody);
            Assert.assertTrue(responseBody.contains("{\"success\":true"));
            JSONObject jsonResponse = new JSONObject(responseBody);
            advertPrimaryInfo.setAdvertId(jsonResponse.getJSONObject("data").getInt("advertId"));
        }
        return advertPrimaryInfo;
    }

    public static AdvertPrimaryInfo primaryInfoGet(Boolean isShow) {
        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
        advertPrimaryInfo.setAdvertId(advertId);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println("Ответ на get: " + responseBody);
            Allure.step("Ответ на get: " + responseBody);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        advertPrimaryInfo.setStatus(data.getString("status"));
        advertPrimaryInfo.setCompany(data.isNull("name") ? null : data.getString("name"));
        advertPrimaryInfo.setCompanyLegalName(data.isNull("companyLegalname") ? null : data.getString("companyLegalname"));
        // advertPrimaryInfo.setCompany(data.getString("registrationNumber"));
        advertPrimaryInfo.setSiteUrl(data.isNull("siteUrl") ? null : data.getString("siteUrl"));
        advertPrimaryInfo.setManagerId(String.valueOf(data.isNull("managerId") ? null : data.getInt("managerId")));
        advertPrimaryInfo.setSalesManagerId(String.valueOf(data.isNull("salesManager") ? null : data.getInt("salesManager")));
        advertPrimaryInfo.setAccountManagerId(String.valueOf(data.isNull("accountManager") ? null : data.getInt("accountManager")));
        advertPrimaryInfo.setUserRequestSourceId(String.valueOf(data.isNull("userRequestSourceId") ? null : data.getInt("userRequestSourceId")));
        advertPrimaryInfo.setUserRequestSourceValue(data.isNull("userRequestSourceValue") ? null : data.getString("userRequestSourceValue"));
        advertPrimaryInfo.setNote(data.isNull("note") ? null : data.getString("note"));

        JSONObject offer = data.getJSONObject("offer");
        advertPrimaryInfo.setActiveOffersCount(parseUnknownValueToInteger(offer, "active"));
        advertPrimaryInfo.setInactiveOffersCount(parseUnknownValueToInteger(offer, "inactive"));
        advertPrimaryInfo.setTotalOffersCount(offer.getInt("total"));
        advertPrimaryInfo.setDraftOffersCount(offer.getInt("draft"));

        if (data.get("pricingModel") instanceof JSONArray) {
            JSONArray pricingModelArray = data.getJSONArray("pricingModel");
            List<String> listArray = StreamSupport.stream(pricingModelArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPrimaryInfo.setPricingModel(listArray);
        } else advertPrimaryInfo.setPricingModel(null);

        if (data.get("geo") instanceof JSONArray) {
            JSONArray geoArray = data.getJSONArray("geo");
            List<String> listArray = StreamSupport.stream(geoArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPrimaryInfo.setGeoAbb(listArray);
        } else advertPrimaryInfo.setGeoAbb(null);

        if (data.get("categories") instanceof JSONArray) {
            JSONArray categoriesArray = data.getJSONArray("categories");
            Set<Integer> categoriesIdList = new HashSet<>();
            for (int i = 0; i < categoriesArray.length(); i++) {
                int value = categoriesArray.getInt(i);
                categoriesIdList.add(value);
            }
            advertPrimaryInfo.setCategoriesId(categoriesIdList);
        } else advertPrimaryInfo.setCategoriesId(null);

        if (data.get("tag") instanceof JSONArray) {
            JSONArray tagArray = data.getJSONArray("tag");
            List<Integer> tagIdList = new ArrayList<>();
            for (int i = 0; i < tagArray.length(); i++) {
                int value = tagArray.getInt(i);
                tagIdList.add(value);
            }
            advertPrimaryInfo.setTagId(tagIdList);
        } else advertPrimaryInfo.setTagId(null);

        return advertPrimaryInfo;
    }

    public static void primaryInfoAssert(AdvertPrimaryInfo advertPrimaryInfo, AdvertPrimaryInfo advertPrimaryInfoGet) throws Exception {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        Assert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfoGet.getStatus());
        Assert.assertEquals(advertPrimaryInfo.getCompany(), advertPrimaryInfoGet.getCompany());
        Assert.assertEquals(advertPrimaryInfo.getCompanyLegalName(), advertPrimaryInfoGet.getCompanyLegalName());
        Assert.assertEquals(advertPrimaryInfo.getSiteUrl(), advertPrimaryInfoGet.getSiteUrl());
        Assert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfoGet.getManagerId());
        Assert.assertEquals(advertPrimaryInfo.getSalesManagerId(), advertPrimaryInfoGet.getSalesManagerId());
        Assert.assertEquals(advertPrimaryInfo.getAccountManagerId(), advertPrimaryInfoGet.getAccountManagerId());
        Assert.assertEquals(advertPrimaryInfo.getGeoAbb(), advertPrimaryInfoGet.getGeoAbb());
        List<Integer> tags = advertPrimaryInfo.getTagId();
        List<Integer> tagsEdit = advertPrimaryInfoGet.getTagId();
        Collections.sort(tags);
        Collections.sort(tagsEdit);
        Assert.assertEquals(tags, tagsEdit);

        Assert.assertEquals(advertPrimaryInfo.getPricingModel(), advertPrimaryInfoGet.getPricingModel());
        Set<Integer> categoriesId = advertPrimaryInfo.getCategoriesId();
        Set<Integer> categoriesIdEdit = advertPrimaryInfoGet.getCategoriesId();
        Assert.assertEquals(categoriesId, categoriesIdEdit);
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceId(), advertPrimaryInfoGet.getUserRequestSourceId());
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceValue(), advertPrimaryInfoGet.getUserRequestSourceValue());
        Assert.assertEquals(advertPrimaryInfo.getNote(), advertPrimaryInfoGet.getNote());

        if (advertPrimaryInfo.getAdvertId() != null) {
            Allure.step("Сравнение значений offer count со значениями из базы");
            advertPrimaryInfo.fillAdvertPrimaryInfoWithOffersData(advertPrimaryInfo.getAdvertId().toString());
            Assert.assertEquals(advertPrimaryInfo.getActiveOffersCount(), advertPrimaryInfoGet.getActiveOffersCount());
            Assert.assertEquals(advertPrimaryInfo.getInactiveOffersCount(), advertPrimaryInfoGet.getInactiveOffersCount());
            Assert.assertEquals(advertPrimaryInfo.getTotalOffersCount(), advertPrimaryInfoGet.getTotalOffersCount());
            Assert.assertEquals(advertPrimaryInfo.getDraftOffersCount(), advertPrimaryInfoGet.getDraftOffersCount());
        }
    }
}