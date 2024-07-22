package API.Settings.Content;

import SettingsPackage.entity.ContentTag;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static API.Helper.deleteMethod;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getLastValueFromBD;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete и проверка
 во вкладке Settings - "Tag Advert"
 */

public class ContentAdvertTagAPI {
    public static int settingTagId;

    @Test
    public static void test() throws Exception {
        tagAddEdit("add");
        ContentTag contentTag = tagAddEdit(settingTagId + "/edit");
        tagAssert(contentTag);
        deleteMethod("setting/advert-tag", String.valueOf(settingTagId));
    }

    private static JsonObject initializeJsonSettingTagBody(ContentTag contentTag) {
        JsonObject titleObject = new JsonObject();
        titleObject.addProperty("name", contentTag.getTagTitle());
        return titleObject;
    }

    public static ContentTag tagAddEdit(String method) {
        ContentTag contentTag = new ContentTag();
        contentTag.fillContentTagWithRandomData();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingTagBody(contentTag), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/advert-tag/" + method);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ " + method + " : " + responseBody);

        if (method.equals("add")) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            settingTagId = jsonResponse.getJSONObject("data").getInt("advertTag");
        } else
            Assert.assertEquals(responseBody, "{\"success\":true}");
        return contentTag;
    }

    public static ArrayList<ContentTag> tagGet() {
        ArrayList<ContentTag> contentCategories = new ArrayList<>();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/advert-tag");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONArray dataArray = dataObject.getJSONArray("advertTag");

        for (int i = 0; i < dataArray.length(); i++) {
            ContentTag contentTag = new ContentTag();
            JSONObject jsonTag = dataArray.getJSONObject(i);
            contentTag.setTagTitle(jsonTag.getString(("name")));
            contentTag.setTagId(jsonTag.getInt(("id")));
            contentCategories.add(contentTag);
        }
        return contentCategories;
    }

    public static void tagAssert(ContentTag contentTagEdit) {
        List<ContentTag> contentTags = tagGet();
        boolean isTagFound = false;

        for (ContentTag contentTag : contentTags) {
            if (contentTag.getTagTitle().equals(contentTagEdit.getTagTitle())) {
                isTagFound = true;
                Assert.assertEquals(contentTag.getTagTitle(), contentTagEdit.getTagTitle());
            }
        }
        if (!isTagFound)
            Assert.fail();
    }
}