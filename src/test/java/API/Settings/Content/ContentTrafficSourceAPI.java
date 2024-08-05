package API.Settings.Content;

import SettingsPackage.entity.ContentTrafficSource;
import SettingsPackage.entity.ContentTrafficSource.*;
import com.google.gson.Gson;
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
import java.util.Objects;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getLastValueFromBDWhere;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete и проверка
 во вкладке Settings - "Traffic sources"
 */


public class ContentTrafficSourceAPI {
    public static int settingTrafficSourceId;

    @Test
    public static void test() throws Exception {
        Allure.step("Добавляем TrafficSource");

        trafficSourceAddEdit("add");
        settingTrafficSourceId = Integer.parseInt(getLastValueFromBDWhere("id", "traffic_source",
                "lang", "general"));
        Allure.step("Редактируем TrafficSource" + settingTrafficSourceId);
        ContentTrafficSource contentTrafficSource = trafficSourceAddEdit(settingTrafficSourceId + "/edit");
        Allure.step(CHECK);
        trafficSourceAssert(contentTrafficSource);

        deleteMethod("setting/traffic-source", settingTrafficSourceId + "/remove");
        assertDelete(String.valueOf(settingTrafficSourceId), "traffic_source");

    }

    private static JsonObject initializeJsonSettingTrafficSourceBody(ContentTrafficSource contentTrafficSource) {
        JsonObject mainObject = new JsonObject();
        JsonObject mainObject2 = new JsonObject();

        JsonObject jsonCategories = new JsonObject();

        JsonObject jsonCategoriesEng = new JsonObject();
        jsonCategoriesEng.addProperty("title", contentTrafficSource.getEngContentTrafficLang().getContentTrafficTitle());
        jsonCategories.add("eng", jsonCategoriesEng);

        JsonObject jsonCategoriesRus = new JsonObject();
        jsonCategoriesRus.addProperty("title", contentTrafficSource.getRusContentTrafficLang().getContentTrafficTitle());
        jsonCategories.add("rus", jsonCategoriesRus);

        JsonObject jsonCategoriesGeneral = new JsonObject();
        jsonCategoriesGeneral.addProperty("title", contentTrafficSource.getGeneralContentTrafficLang().getContentTrafficTitle());
        jsonCategories.add("general", jsonCategoriesGeneral);

        mainObject2.add("trafficSource", jsonCategories);
        mainObject.add("traffic_source_lang", mainObject2);

        return mainObject;
    }

    public static ContentTrafficSource trafficSourceAddEdit(String method) {
        ContentTrafficSource contentTrafficSource = new ContentTrafficSource();
        contentTrafficSource.fillContentContentTrafficWithRandomData();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingTrafficSourceBody(contentTrafficSource), JsonObject.class);
        System.out.println(DATA + jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/traffic-source/" + method);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ " + method + " : " + responseBody);
        Allure.step("Ответ " + method + " : " + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");
        return contentTrafficSource;
    }

    public static ArrayList<ContentTrafficSource> trafficSourceGet() {
        ArrayList<ContentTrafficSource> contentCategories = new ArrayList<>();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/traffic-source");

        String responseBody = response.getBody().asString();
        System.out.println(GET_RESPONSE + responseBody);
        Allure.step(GET_RESPONSE + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            ContentTrafficSource contentTrafficSource = new ContentTrafficSource();
            JSONObject dataObject = dataArray.getJSONObject(i);

            JSONObject LangTrafficSourceObjectEng = null;
            ContentTrafficLang trafficSourceLangEng = null;
            if (dataObject.has("eng")) {
                LangTrafficSourceObjectEng = dataObject.getJSONObject("eng");
                trafficSourceLangEng = new ContentTrafficLang();
            }

            JSONObject LangTrafficSourceObjectRus = null;
            ContentTrafficLang trafficSourceLangRus = null;
            if (dataObject.has("rus")) {
                LangTrafficSourceObjectRus = dataObject.getJSONObject("rus");
                trafficSourceLangRus = new ContentTrafficLang();
            }

            JSONObject LangTrafficSourceObjectGen = dataObject.getJSONObject("general");
            ContentTrafficLang trafficSourceLangGen = new ContentTrafficLang();

            setContentTrafficLang(trafficSourceLangEng, LangTrafficSourceObjectEng);
            setContentTrafficLang(trafficSourceLangRus, LangTrafficSourceObjectRus);
            setContentTrafficLang(trafficSourceLangGen, LangTrafficSourceObjectGen);

            contentTrafficSource.setEngContentTrafficLang(trafficSourceLangEng);
            contentTrafficSource.setRusContentTrafficLang(trafficSourceLangRus);
            contentTrafficSource.setGeneralContentTrafficLang(trafficSourceLangGen);

            contentCategories.add(contentTrafficSource);
        }
        return contentCategories;
    }

    private static void setContentTrafficLang(ContentTrafficLang trafficSourceLang, JSONObject dataObject) {
        if (dataObject != null) {
            trafficSourceLang.setContentTrafficTitle(dataObject.isNull("title") ? null : dataObject.getString(("title")));
            trafficSourceLang.setContentTrafficLang(dataObject.isNull("lang") ? null : dataObject.getString(("lang")));
            trafficSourceLang.setContentTrafficId(dataObject.isNull("id") ? null : dataObject.getInt("id"));
            trafficSourceLang.setContentTrafficParentId(dataObject.isNull("parentId") ? null : dataObject.getInt(("parentId")));
        }
    }

    public static void trafficSourceAssert(ContentTrafficSource contentTrafficSourceEdit) {
        List<ContentTrafficSource> contentCategories = trafficSourceGet();
        boolean isTrafficSourceFound = false;

        for (ContentTrafficSource contentTrafficSource : contentCategories) {
            if (Objects.equals(contentTrafficSource.getGeneralContentTrafficLang().getContentTrafficTitle(),
                    contentTrafficSourceEdit.getGeneralContentTrafficLang().getContentTrafficTitle())) {
                ContentTrafficLang gen = contentTrafficSource.getGeneralContentTrafficLang();
                ContentTrafficLang genEdit = contentTrafficSourceEdit.getGeneralContentTrafficLang();
                Assert.assertEquals(gen.getContentTrafficTitle(), genEdit.getContentTrafficTitle());

                ContentTrafficLang eng = contentTrafficSource.getEngContentTrafficLang();
                ContentTrafficLang engEdit = contentTrafficSourceEdit.getEngContentTrafficLang();
                Assert.assertEquals(eng.getContentTrafficTitle(), engEdit.getContentTrafficTitle());

                ContentTrafficLang rus = contentTrafficSource.getRusContentTrafficLang();
                ContentTrafficLang rusEdit = contentTrafficSourceEdit.getRusContentTrafficLang();
                Assert.assertEquals(rus.getContentTrafficTitle(), rusEdit.getContentTrafficTitle());
                isTrafficSourceFound = true;
            }
        }
        Assert.assertTrue(isTrafficSourceFound);
    }
}