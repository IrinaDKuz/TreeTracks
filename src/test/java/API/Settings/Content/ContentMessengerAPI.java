package API.Settings.Content;

import SettingsPackage.entity.ContentMessenger;
import SettingsPackage.entity.ContentMessenger.MessengerLang;
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
 во вкладке Settings - "Messenger"
 */


public class ContentMessengerAPI {
    public static int settingMessengerId;

    @Test
    public static void test() throws Exception {
        messengerAddEdit("add");
        settingMessengerId = Integer.parseInt(getLastValueFromBDWhere("id", "messenger_type",
                "lang", "general"));
        ContentMessenger contentMessenger = messengerAddEdit(settingMessengerId + "/edit");
        messengerAssert(contentMessenger);
        deleteMethod("setting/messenger", settingMessengerId + "/remove");
    }

    private static JsonObject initializeJsonSettingMessengerBody(ContentMessenger contentMessenger) {
        JsonObject mainObject = new JsonObject();
        JsonObject mainObject2 = new JsonObject();

        JsonObject jsonCategories = new JsonObject();

        JsonObject jsonCategoriesEng = new JsonObject();
        jsonCategoriesEng.addProperty("title", contentMessenger.getEngMessengerLang().getMessengerTitle());
        jsonCategories.add("eng", jsonCategoriesEng);

        JsonObject jsonCategoriesRus = new JsonObject();
        jsonCategoriesRus.addProperty("title", contentMessenger.getRusMessengerLang().getMessengerTitle());
        jsonCategories.add("rus", jsonCategoriesRus);

        JsonObject jsonCategoriesGeneral = new JsonObject();
        jsonCategoriesGeneral.addProperty("title", contentMessenger.getGeneralMessengerLang().getMessengerTitle());
        jsonCategories.add("general", jsonCategoriesGeneral);

        mainObject2.add("messengerType", jsonCategories);
        mainObject.add("messenger_type_lang", mainObject2);

        return mainObject;
    }

    public static ContentMessenger messengerAddEdit(String method) {
        ContentMessenger contentMessenger = new ContentMessenger();
        contentMessenger.fillContentMessengerWithRandomData();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingMessengerBody(contentMessenger), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/messenger/" + method);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ " + method + " : " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return contentMessenger;
    }

    public static ArrayList<ContentMessenger> messengerGet() {
        ArrayList<ContentMessenger> contentCategories = new ArrayList<>();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/messenger");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            ContentMessenger contentMessenger = new ContentMessenger();
            JSONObject dataObject = dataArray.getJSONObject(i);

            JSONObject LangMessengerObjectEng = dataObject.getJSONObject("eng");
            MessengerLang messengerLangEng = new MessengerLang();
            JSONObject LangMessengerObjectRus = dataObject.getJSONObject("rus");
            MessengerLang messengerLangRus = new MessengerLang();
            JSONObject LangMessengerObjectGen = dataObject.getJSONObject("general");
            MessengerLang messengerLangGen = new MessengerLang();
            setCategoriesLang(messengerLangEng, LangMessengerObjectEng);
            setCategoriesLang(messengerLangRus, LangMessengerObjectRus);
            setCategoriesLang(messengerLangGen, LangMessengerObjectGen);

            contentMessenger.setEngMessengerLang(messengerLangEng);
            contentMessenger.setRusMessengerLang(messengerLangRus);
            contentMessenger.setGeneralMessengerLang(messengerLangGen);

            contentCategories.add(contentMessenger);
        }
        return contentCategories;
    }

    private static void setCategoriesLang(MessengerLang messengerLang, JSONObject dataObject) {
        messengerLang.setMessengerTitle(dataObject.getString(("title")));
        messengerLang.setMessengerLang(dataObject.getString(("lang")));
        messengerLang.setMessengerId(dataObject.isNull("id") ? null : dataObject.getInt(("id")));
        messengerLang.setMessengerParentId(dataObject.isNull("parentId") ? null : dataObject.getInt(("parentId")));
    }

    public static void messengerAssert(ContentMessenger contentMessengerEdit) {
        List<ContentMessenger> contentCategories = messengerGet();
        boolean isGoalFound = false;

        for (ContentMessenger contentMessenger : contentCategories) {
            if (Objects.equals(contentMessenger.getGeneralMessengerLang().getMessengerTitle(),
                    contentMessengerEdit.getGeneralMessengerLang().getMessengerTitle())) {
                MessengerLang gen = contentMessenger.getGeneralMessengerLang();
                MessengerLang genEdit = contentMessengerEdit.getGeneralMessengerLang();
                Assert.assertEquals(gen.getMessengerTitle(), genEdit.getMessengerTitle());

                MessengerLang eng = contentMessenger.getEngMessengerLang();
                MessengerLang engEdit = contentMessengerEdit.getEngMessengerLang();
                Assert.assertEquals(eng.getMessengerTitle(), engEdit.getMessengerTitle());

                MessengerLang rus = contentMessenger.getRusMessengerLang();
                MessengerLang rusEdit = contentMessengerEdit.getRusMessengerLang();
                Assert.assertEquals(rus.getMessengerTitle(), rusEdit.getMessengerTitle());
                isGoalFound = true;

            }
        }
        if (!isGoalFound)
            Assert.fail();
    }
}