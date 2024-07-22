package API.Settings.Content;

import SettingsPackage.entity.ContentGoal;
import SettingsPackage.entity.ContentMessenger;
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
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete и проверка
 во вкладке Settings - "Goal"
 */


public class ContentGoalAPI {
    public static int settingGoalId;

    @Test
    public static void test() throws Exception {
        goalAddEdit("add");
        settingGoalId = Integer.parseInt(getLastValueFromBD("id", "goal"));
        System.out.println(settingGoalId);
        ContentGoal contentGoal = goalAddEdit(settingGoalId + "/edit");
        goalAssert(contentGoal);
        deleteMethod("setting/goal", settingGoalId + "/remove");
    }

    private static JsonObject initializeJsonSettingGoalBody(ContentGoal contentGoal) {
        JsonObject goalObject = new JsonObject();
        JsonObject titleObject = new JsonObject();
        titleObject.addProperty("title", contentGoal.getGoalTitle());
        goalObject.add("goal", titleObject);
        return goalObject;
    }

    public static ContentGoal goalAddEdit(String method) {
        ContentGoal contentGoal = new ContentGoal();
        contentGoal.fillContentGoalWithRandomData();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonSettingGoalBody(contentGoal), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/setting/goal/" + method);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ " + method + " : " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return contentGoal;
    }

    public static ArrayList<ContentGoal> goalGet() {
        ArrayList<ContentGoal> contentCategories = new ArrayList<>();
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/setting/goal");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            ContentGoal contentGoal = new ContentGoal();
            JSONObject jsonGoal = dataArray.getJSONObject(i);
            contentGoal.setGoalTitle(jsonGoal.getString(("title")));
            contentGoal.setGoalId(jsonGoal.getInt(("id")));
            contentCategories.add(contentGoal);
        }
        return contentCategories;
    }

    public static void goalAssert(ContentGoal contentGoalEdit) {
        List<ContentGoal> contentGoals = goalGet();
        boolean isGoalFound = false;

        for (ContentGoal contentGoal : contentGoals) {
            if (contentGoal.getGoalTitle().equals(contentGoalEdit.getGoalTitle())) {
                isGoalFound = true;
                Assert.assertEquals(contentGoal.getGoalTitle(), contentGoalEdit.getGoalTitle());
            }
        }
        if (!isGoalFound)
            Assert.fail();
    }
}