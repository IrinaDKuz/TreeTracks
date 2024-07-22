package API.Settings.Content;

import SettingsPackage.entity.ContentCategory.*;
import SettingsPackage.entity.ContentCategory;
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
import java.util.Objects;

import static API.Helper.deleteMethod;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getLastValueFromBDWhere;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete и проверка
 во вкладке Settings - "Category"
 */


public class ContentCategoryAPI {
    public static int settingCategoryId;

    @Test
    public static void test() throws Exception {
        categoryAddEdit("add");
        settingCategoryId = Integer.parseInt(getLastValueFromBDWhere("id", "category",
                "lang", "general"));
        ContentCategory contentCategory = categoryAddEdit(settingCategoryId + "/edit");
        categoryAssert(contentCategory);
        deleteMethod("setting/category", settingCategoryId + "/remove");
    }

    private static JsonObject initializeJsonSettingCategoryBody(ContentCategory contentCategory) {
        JsonObject mainObject = new JsonObject();
        JsonObject mainObject2 = new JsonObject();

        JsonObject jsonCategories = new JsonObject();

        JsonObject jsonCategoriesEng = new JsonObject();
        jsonCategoriesEng.addProperty("title", contentCategory.getEngCategoryLang().getCategoryTitle());
        jsonCategories.add("eng", jsonCategoriesEng);

        JsonObject jsonCategoriesRus = new JsonObject();
        jsonCategoriesRus.addProperty("title", contentCategory.getRusCategoryLang().getCategoryTitle());
        jsonCategories.add("rus", jsonCategoriesRus);

        JsonObject jsonCategoriesGeneral = new JsonObject();
        jsonCategoriesGeneral.addProperty("title", contentCategory.getGeneralCategoryLang().getCategoryTitle());
        jsonCategories.add("general", jsonCategoriesGeneral);

        mainObject2.add("category", jsonCategories);
        mainObject.add("category_lang", mainObject2);

        return mainObject;
    }

    public static ContentCategory categoryAddEdit(String method) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.fillContentCategoryWithRandomData();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingCategoryBody(contentCategory), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/category/" + method);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ " + method + " : " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return contentCategory;
    }

    public static ArrayList<ContentCategory> categoryGet() {
        ArrayList<ContentCategory> contentCategories = new ArrayList<>();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/category");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            ContentCategory contentCategory = new ContentCategory();
            JSONObject dataObject = dataArray.getJSONObject(i);

            JSONObject LangCategoryObjectEng = dataObject.getJSONObject("eng");
            CategoryLang categoryLangEng = new CategoryLang();
            JSONObject LangCategoryObjectRus = dataObject.getJSONObject("rus");
            CategoryLang categoryLangRus = new CategoryLang();
            JSONObject LangCategoryObjectGen = dataObject.getJSONObject("general");
            CategoryLang categoryLangGen = new CategoryLang();
            setCategoriesLang(categoryLangEng, LangCategoryObjectEng);
            setCategoriesLang(categoryLangRus, LangCategoryObjectRus);
            setCategoriesLang(categoryLangGen, LangCategoryObjectGen);

            contentCategory.setEngCategoryLang(categoryLangEng);
            contentCategory.setRusCategoryLang(categoryLangRus);
            contentCategory.setGeneralCategoryLang(categoryLangGen);

            contentCategories.add(contentCategory);
        }
        return contentCategories;
    }

    private static void setCategoriesLang(CategoryLang categoryLang, JSONObject dataObject) {
        categoryLang.setCategoryTitle(dataObject.getString(("title")));
        categoryLang.setCategoryLang(dataObject.getString(("lang")));
        categoryLang.setCategoryId(dataObject.isNull("id") ? null : dataObject.getInt(("id")));
        categoryLang.setCategoryParentId(dataObject.isNull("parentId") ? null : dataObject.getInt(("parentId")));
    }

    public static void categoryAssert(ContentCategory contentCategoryEdit) {
        List<ContentCategory> contentCategories = categoryGet();
        for (ContentCategory contentCategory : contentCategories) {
            if (Objects.equals(contentCategory.getGeneralCategoryLang().getCategoryTitle(),
                    contentCategoryEdit.getGeneralCategoryLang().getCategoryTitle())) {
                CategoryLang gen = contentCategory.getGeneralCategoryLang();
                CategoryLang genEdit = contentCategoryEdit.getGeneralCategoryLang();
                Assert.assertEquals(gen.getCategoryTitle(), genEdit.getCategoryTitle());

                CategoryLang eng = contentCategory.getEngCategoryLang();
                CategoryLang engEdit = contentCategoryEdit.getEngCategoryLang();
                Assert.assertEquals(eng.getCategoryTitle(), engEdit.getCategoryTitle());

                CategoryLang rus = contentCategory.getRusCategoryLang();
                CategoryLang rusEdit = contentCategoryEdit.getRusCategoryLang();
                Assert.assertEquals(rus.getCategoryTitle(), rusEdit.getCategoryTitle());
            }
        }
    }
}