package API.Task;

import TaskPackage.entity.FeedBackTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static API.Helper.*;
import static API.Task.FeedBackTaskAPI.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.KEY;
import static Helper.Auth.authApi;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO:
 */

public class ConditionsReviewTaskAPI {
    static int taskId;
    static Integer userId = 55;

    @Test
    public static void test() throws Exception {
        authApi(userId);

        Allure.step("Добавляем Conditions Review Task");
        FeedBackTask feedBackTask = feedBackTaskAddEdit(false);
        taskId = feedBackTask.getTaskId();
        Allure.step(CHECK);

        feedBackTaskAssert(feedBackTask, feedBackTaskGet(true, taskId));

        Allure.step("Получаем Conditions Review Task id=" + taskId);
        feedBackTaskGet(true, taskId);


        Allure.step("Редактируем Conditions Review Task id=" + taskId);
        FeedBackTask feedBackTaskEdit = feedBackTaskAddEdit(true);
        Allure.step(CHECK);
        feedBackTaskAssert(feedBackTaskEdit, feedBackTaskGet(false, taskId));

        Allure.step("Выполняем soft delete Conditions Review Task id=" + taskId);
       // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }


    public static FeedBackTask feedBackTaskAddEdit(Boolean isEdit) throws Exception {
        FeedBackTask feedBackTask = new FeedBackTask();
        feedBackTask.fillConditionsReviewTaskWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonFeedBackTaskInfo(feedBackTask), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? URL + "/task/" + taskId + "/action/edit" :
                URL + "/task/conditions-review/new";

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
            feedBackTask.setTaskId(jsonResponse.getJSONObject("data").getInt("id"));
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }

        feedBackTask.setRequesterId(userId);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return feedBackTask;
    }
}