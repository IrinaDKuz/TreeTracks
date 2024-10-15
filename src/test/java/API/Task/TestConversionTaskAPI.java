package API.Task;

import TaskPackage.entity.GeneralTask;
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

import java.util.HashSet;
import java.util.List;
import java.util.stream.StreamSupport;

import static API.Helper.*;
import static API.Task.FeedBackTaskAPI.feedBackTaskGet;
import static API.Task.GeneralTaskAPI.generalTaskAssert;
import static Helper.Adverts.generateName;
import static Helper.AllureHelper.*;
import static Helper.Auth.KEY;
import static Helper.Auth.authApi;
import static Helper.Tasks.TASK_WORDS;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO:
 */

public class TestConversionTaskAPI {
    static int taskId;
    static Integer userId = 104;

    @Test
    public static void test() throws Exception {
        authApi(userId);

        Allure.step("Добавляем Test conversion Task");
        GeneralTask generalTask = generalTaskAddEdit(false);
        taskId = generalTask.getTaskId();
        Allure.step(CHECK);

        generalTaskAssert(generalTask, generalTaskGet(true, taskId));

        Allure.step("Получаем Test conversion Task id=" + taskId);
        feedBackTaskGet(true, taskId);


        Allure.step("Редактируем Test conversion Task id=" + taskId);
        GeneralTask generalTaskEdit = generalTaskAddEdit(true);
        Allure.step(CHECK);
        generalTaskAssert(generalTaskEdit, generalTaskGet(false, taskId));

        Allure.step("Выполняем soft delete Test conversion Task id=" + taskId);
       // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }


    public static GeneralTask generalTaskAddEdit(Boolean isEdit) throws Exception {
        GeneralTask generalTask = new GeneralTask();
        generalTask.fillTestConversionTaskWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonGeneralTaskInfo(generalTask), JsonObject.class);

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? URL + "/task/" + taskId + "/action/edit" :
                URL + "/task/test-conversion/new";

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
            generalTask.setTaskId(jsonResponse.getJSONObject("data").getInt("id"));
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }

        generalTask.setRequesterId(userId);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return generalTask;
    }


    public static JsonObject initializeJsonGeneralTaskInfo(GeneralTask generalTask) {
        JsonObject taskObject = new JsonObject();
        taskObject.addProperty("url", generalTask.getTitle());
        taskObject.addProperty("status", generalTask.getStatus());
        taskObject.addProperty("assigner", generalTask.getAssigneeId());
        taskObject.addProperty("offerId", generalTask.getOfferId());
        taskObject.addProperty("affiliateId", generalTask.getAffiliateId());

        taskObject.addProperty("notes", generalTask.getDescription());
        taskObject.addProperty("dueDate", generalTask.getDueDate());

        List<Integer> taskWatchers = generalTask.getTaskWatchers();
        JsonArray taskWatchersArray = new JsonArray();
        taskWatchers.forEach(taskWatchersArray::add);
        taskObject.add("watcher", taskWatchersArray);

        List<Integer> tagList = generalTask.getTaskTag();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        taskObject.add("tags", tagArray);
        return taskObject;
    }

    public static GeneralTask generalTaskGet(Boolean isShow, Integer taskId) {
        GeneralTask generalTask = new GeneralTask();

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(URL + "/task/" + taskId);

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject info = data.getJSONObject("info");
        generalTask.setType(info.getString("type"));
        generalTask.setStatus(info.getString("status"));
        generalTask.setTitle(info.getString("url"));

        generalTask.setOfferId(info.isNull("offer") ? null : getValueFromJson(info, "offer"));
        generalTask.setAffiliateId(info.isNull("affiliate") ? null : getValueFromJson(info, "affiliate"));
        generalTask.setAdvertId(info.isNull("advert") ? null : getValueFromJson(info, "advert"));

        generalTask.setRequesterId(info.getInt("requester"));
        generalTask.setAssigneeId(info.getInt("assigner"));

        generalTask.setDescription(info.getString("notes"));

        generalTask.setDueDate(info.getString("dueDate"));

        if (info.get("watcher") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("watcher");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false)
                    .map(element -> Integer.parseInt(element.toString()))
                    .toList();
            generalTask.setTaskWatchers(listArray);

        } else generalTask.setTaskWatchers(null);


        if (info.get("tags") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("tags");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false)
                    .map(element -> Integer.parseInt(element.toString()))
                    .toList();
            generalTask.setTaskTag(listArray);

        } else generalTask.setTaskTag(null);

        return generalTask;
    }
}