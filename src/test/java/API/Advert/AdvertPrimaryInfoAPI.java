package API.Advert;

import AdvertPackage.entity.AdvertPrimaryInfo;
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

import java.util.*;
import java.util.stream.StreamSupport;

import static API.Helper.deleteMethod;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBDWhereMore;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete, проверка
 во вкладке Адверт - "Primary Info"
 */

public class AdvertPrimaryInfoAPI {
    static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "1000"));
        primaryInfoGet();
        primaryInfoAddEdit(false);
        AdvertPrimaryInfo advertPrimaryInfo = primaryInfoAddEdit(true);
        primaryInfoAssert(advertPrimaryInfo);
        deleteMethod("advert", String.valueOf(advertId));
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
        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
        advertPrimaryInfo.fillAdvertPrimaryInfoWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPrimaryInfo(advertPrimaryInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

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
        System.out.println("Ответ на add/edit: " + responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return advertPrimaryInfo;
    }

    public static AdvertPrimaryInfo primaryInfoGet() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");

        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
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

    public static void primaryInfoAssert(AdvertPrimaryInfo advertPrimaryInfoEdit) {
        AdvertPrimaryInfo advertPrimaryInfo = primaryInfoGet();
        Assert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfoEdit.getStatus());
        Assert.assertEquals(advertPrimaryInfo.getCompany(), advertPrimaryInfoEdit.getCompany());
        Assert.assertEquals(advertPrimaryInfo.getCompanyLegalName(), advertPrimaryInfoEdit.getCompanyLegalName());
        Assert.assertEquals(advertPrimaryInfo.getSiteUrl(), advertPrimaryInfoEdit.getSiteUrl());
        Assert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfoEdit.getManagerId());
        Assert.assertEquals(advertPrimaryInfo.getSalesManagerId(), advertPrimaryInfoEdit.getSalesManagerId());
        Assert.assertEquals(advertPrimaryInfo.getAccountManagerId(), advertPrimaryInfoEdit.getAccountManagerId());
        System.out.println(advertPrimaryInfo.getGeoAbb());
        System.out.println(advertPrimaryInfoEdit.getGeoAbb());
        Assert.assertEquals(advertPrimaryInfo.getGeoAbb(), advertPrimaryInfoEdit.getGeoAbb());
        List<Integer> tags = advertPrimaryInfo.getTagId();
        List<Integer> tagsEdit = advertPrimaryInfoEdit.getTagId();
        Collections.sort(tags);
        Collections.sort(tagsEdit);
        Assert.assertEquals(tags, tagsEdit);

        Assert.assertEquals(advertPrimaryInfo.getPricingModel(), advertPrimaryInfoEdit.getPricingModel());
        Set<Integer> categoriesId = advertPrimaryInfo.getCategoriesId();
        Set<Integer> categoriesIdEdit = advertPrimaryInfoEdit.getCategoriesId();
        Assert.assertEquals(categoriesId, categoriesIdEdit);
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceId(), advertPrimaryInfoEdit.getUserRequestSourceId());
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceValue(), advertPrimaryInfoEdit.getUserRequestSourceValue());
        Assert.assertEquals(advertPrimaryInfo.getNote(), advertPrimaryInfoEdit.getNote());
    }
}