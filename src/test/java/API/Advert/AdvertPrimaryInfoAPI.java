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

import java.util.List;
import java.util.stream.StreamSupport;

import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBDWhereMore;

/***
 Тест проверяет работу API методов
 - get, add/edit, проверка
 во вкладке Адверт - "Primary Info"
 */

public class AdvertPrimaryInfoAPI {

    static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "1000"));
        primaryInfoGet();
        AdvertPrimaryInfo advertPrimaryInfo = primaryInfoEdit();
        primaryInfoAssert(advertPrimaryInfo);
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
        advertObject.addProperty("pricingModel", advertPrimaryInfo.getModelType().toLowerCase());
        advertObject.addProperty("companyLegalname", advertPrimaryInfo.getCompanyLegalName());
        advertObject.addProperty("note", advertPrimaryInfo.getNote());
        advertObject.addProperty("userRequestSourceId", Integer.parseInt(advertPrimaryInfo.getUserRequestSourceId()));
        advertObject.addProperty("userRequestSourceValue", advertPrimaryInfo.getUserRequestSourceValue());

        List<String> tagList = advertPrimaryInfo.getTag();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        advertObject.add("tag", tagArray);

        List<String> categoriesList = advertPrimaryInfo.getCategories();
        JsonArray categoriesArray = new JsonArray();
        categoriesList.forEach(categoriesArray::add);
        advertObject.add("categories", categoriesArray);

        List<String> geoList = advertPrimaryInfo.getGeo();
        JsonArray geoArray = new JsonArray();
        geoList.forEach(geoArray::add);
        advertObject.add("geo", geoArray);

        jsonObject.add("advert", advertObject);
        return jsonObject;
    }

    public static AdvertPrimaryInfo primaryInfoEdit() throws Exception {
        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
        advertPrimaryInfo.fillAdvertPrimaryInfoWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPrimaryInfo(advertPrimaryInfo), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/edit");
        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
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
        advertPrimaryInfo.setCompany(data.isNull("name") ? null :data.getString("name"));

        advertPrimaryInfo.setCompanyLegalName(data.isNull("companyLegalname") ? null :data.getString("companyLegalname"));
        // advertPrimaryInfo.setCompany(data.getString("registrationNumber"));
        advertPrimaryInfo.setSiteUrl(data.isNull("siteUrl") ? null : data.getString("siteUrl"));
        advertPrimaryInfo.setModelType(data.getString("pricingModel"));
        advertPrimaryInfo.setManagerId(String.valueOf(data.isNull("managerId") ? null : data.getInt("managerId")));
        advertPrimaryInfo.setSalesManagerId(String.valueOf(data.isNull("salesManager") ? null : data.getInt("salesManager")));
        advertPrimaryInfo.setAccountManagerId(String.valueOf(data.isNull("accountManager") ? null : data.getInt("accountManager")));
        advertPrimaryInfo.setUserRequestSourceId(String.valueOf(data.isNull("userRequestSourceId") ? null : data.getInt("userRequestSourceId")));
        advertPrimaryInfo.setUserRequestSourceValue(data.isNull("userRequestSourceValue") ? null : data.getString("userRequestSourceValue"));
        advertPrimaryInfo.setNote(data.isNull("note") ? null : data.getString("note"));

        if (data.get("geo") instanceof JSONArray) {
            JSONArray geoArray = data.getJSONArray("geo");
            List<String> listArray = StreamSupport.stream(geoArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPrimaryInfo.setGeo(listArray);
        } else advertPrimaryInfo.setGeo(null);

        if (data.get("categories") instanceof JSONArray) {
            JSONArray categoriesArray = data.getJSONArray("categories");
            List<String> listArray = StreamSupport.stream(categoriesArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPrimaryInfo.setCategories(listArray);
        } else advertPrimaryInfo.setCategories(null);

        if (data.get("tag") instanceof JSONArray) {
            JSONArray tagArray = data.getJSONArray("tag");
            List<String> listArray = StreamSupport.stream(tagArray.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            advertPrimaryInfo.setTag(listArray);
        } else advertPrimaryInfo.setTag(null);

        return advertPrimaryInfo;
    }

    public static void primaryInfoAssert(AdvertPrimaryInfo advertPrimaryInfoEdit) {
        AdvertPrimaryInfo advertPrimaryInfo = primaryInfoGet();
        Assert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfoEdit.getStatus());
        Assert.assertEquals(advertPrimaryInfo.getCompany(), advertPrimaryInfoEdit.getCompany());
        Assert.assertEquals(advertPrimaryInfo.getCompanyLegalName(), advertPrimaryInfoEdit.getCompanyLegalName());
        Assert.assertEquals(advertPrimaryInfo.getSiteUrl(), advertPrimaryInfoEdit.getSiteUrl());
        Assert.assertEquals(advertPrimaryInfo.getModelType(), advertPrimaryInfoEdit.getModelType().toLowerCase());
        Assert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfoEdit.getManagerId());
        Assert.assertEquals(advertPrimaryInfo.getSalesManagerId(), advertPrimaryInfoEdit.getSalesManagerId());
        Assert.assertEquals(advertPrimaryInfo.getAccountManagerId(), advertPrimaryInfoEdit.getAccountManagerId());
        Assert.assertEquals(advertPrimaryInfo.getGeo(), advertPrimaryInfoEdit.getGeo());
        Assert.assertEquals(advertPrimaryInfo.getTag(), advertPrimaryInfoEdit.getTag());
        Assert.assertEquals(advertPrimaryInfo.getCategories(), advertPrimaryInfoEdit.getCategories());
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceId(), advertPrimaryInfoEdit.getUserRequestSourceId());
        Assert.assertEquals(advertPrimaryInfo.getUserRequestSourceValue(), advertPrimaryInfoEdit.getUserRequestSourceValue());
        Assert.assertEquals(advertPrimaryInfo.getNote(), advertPrimaryInfoEdit.getNote());
    }
}