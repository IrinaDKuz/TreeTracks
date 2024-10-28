package API.Task;

import TaskPackage.entity.*;
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
import static Helper.AllureHelper.*;
import static Helper.Auth.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO: сейчас нет сравнений урлов и тайтлов нужно что-то придуать
 */

public class FeedBackTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем FeedBack Task");
        Task feedBackTask = new FeedBackTask(taskId, userId);
        taskAddEdit(false, feedBackTask);
        taskId = feedBackTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(feedBackTask, taskGet(true, taskId));

        Allure.step("Получаем FeedBack Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем FeedBack Task id=" + taskId);
        Task feedBackTaskEdit = new FeedBackTask(taskId, userId);
        taskAddEdit(true, feedBackTaskEdit);
        Allure.step(CHECK);
        taskAssert(feedBackTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete FeedBack Task id=" + taskId);
        // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }

    public static JsonObject initializeJsonTaskInfo(Task task) {
        JsonObject taskObject = new JsonObject();

        if (task instanceof TestConversionTask) {
            TestConversionTask testConversionTask = (TestConversionTask) task;
            taskObject.addProperty("url", testConversionTask.getTitle());
        }

        if (task instanceof GeneralTask) {
            GeneralTask generalTask = (GeneralTask) task;
            taskObject.addProperty("title", generalTask.getTitle());
        }

        taskObject.addProperty("status", task.getStatus());
        taskObject.addProperty("offerId", task.getOfferId());
        taskObject.addProperty("assigner", task.getAssigneeId());
        taskObject.addProperty("affiliateId", task.getAffiliateId());

        if (!(task.getType().equals("general") || task.getType().equals("integration")))
            taskObject.addProperty("notes", task.getNotes());
        else taskObject.addProperty("description", task.getNotes());

        taskObject.addProperty("dueDate", task.getDueDate());

        List<Integer> taskWatchers = task.getTaskWatchers();
        JsonArray taskWatchersArray = new JsonArray();
        taskWatchers.forEach(taskWatchersArray::add);
        taskObject.add("watcher", taskWatchersArray);

        List<Integer> tagList = task.getTaskTag();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        taskObject.add("tags", tagArray);
        return taskObject;
    }

    public static Task taskAddEdit(Boolean isEdit, Task task) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonTaskInfo(task), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? URL + "/task/" + task.getTaskId() + "/action/edit" : URL + "/task/" + task.getName() + "/new";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            task.setTaskId(jsonResponse.getJSONObject("data").getInt("id"));
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return task;
    }

    public static Task taskGet(Boolean isShow, Integer taskId) throws Exception {
        Task task;

        Response response;
        response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").get(URL + "/task/" + taskId);

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject info = data.getJSONObject("info");


        if (!(info.getString("type").equals("general") ||
                info.getString("type").equals("test_conversion")))
            task = new Task(true);

        else {
            task = new GeneralTask(true);
            if (info.getString("type").equals("general"))
                ((GeneralTask) task).setTitle(info.getString("title"));
            if (info.getString("type").equals("test_conversion"))
                ((GeneralTask) task).setTitle(info.getString("url"));
        }

        task.setType(info.getString("type"));
        task.setStatus(info.getString("status"));
        task.setOfferId(getValueFromJson(info, "offer"));
        task.setAffiliateId(getValueFromJson(info, "affiliate"));
        task.setAdvertId(getValueFromJson(info, "advert"));

        task.setRequesterId(info.getInt("requester"));
        task.setAssigneeId(info.getInt("assigner"));

        if (task.getType().equals("general") || task.getType().equals("integration"))
            task.setNotes(info.isNull("description") ? "null" : info.getString("description"));
        else
            task.setNotes(info.isNull("notes") ? "null" : info.getString("notes"));


        task.setDueDate(info.getString("dueDate"));

        if (info.get("watcher") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("watcher");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false).map(element -> Integer.parseInt(element.toString())).toList();
            task.setTaskWatchers(listArray);

        } else task.setTaskWatchers(null);

        if (info.get("tags") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("tags");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false).map(element -> Integer.parseInt(element.toString())).toList();
            task.setTaskTag(listArray);

        } else task.setTaskTag(null);

        return task;
    }

    public static void taskAssert(Task task, Task taskGet) {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        // Assert.assertEquals(feedBackTask.getTaskId(), feedBackTaskGet.getTaskId());
        SoftAssert softAssert = new SoftAssert();

        if (task instanceof GeneralTask generalTask && taskGet instanceof GeneralTask generalTaskGet) {
            System.out.println("Проверка General +");
            softAssert.assertEquals(generalTask.getTitle(), generalTaskGet.getTitle(), "Title do not match");
        }

        if (task instanceof TestConversionTask testConversionTask && taskGet instanceof TestConversionTask testConversionTaskGet) {
            System.out.println("Проверка TestConversionTask +");
            softAssert.assertEquals(testConversionTask.getTitle(), testConversionTaskGet.getTitle(), "Url do not match");
        }

        softAssert.assertEquals(task.getStatus(), taskGet.getStatus(), "Status do not match");
        softAssert.assertEquals(task.getType(), taskGet.getType(), "Type do not match");
        softAssert.assertEquals(task.getRequesterId(), taskGet.getRequesterId(), "RequesterId do not match");
        softAssert.assertEquals(task.getAssigneeId(), taskGet.getAssigneeId(), "AssigneeId do not match");
        softAssert.assertEquals(task.getAdvertId(), taskGet.getAdvertId(), "AdvertId do not match");
        softAssert.assertEquals(task.getOfferId(), taskGet.getOfferId(), "OfferId do not match");
        softAssert.assertEquals(task.getAffiliateId(), taskGet.getAffiliateId(), "AffiliateId do not match");
        softAssert.assertEquals(task.getDueDate(), taskGet.getDueDate(), "dueDate do not match");

        softAssert.assertEquals(task.getNotes(), taskGet.getNotes(), "notes do not match");

        List<Integer> tags = task.getTaskTag();
        List<Integer> tagsEdit = taskGet.getTaskTag();
        softAssert.assertEquals(new HashSet<>(tags), new HashSet<>(tagsEdit), "tags do not match");

        List<Integer> watchers = task.getTaskWatchers();
        List<Integer> watchersEdit = taskGet.getTaskWatchers();
        softAssert.assertEquals(new HashSet<>(watchers), new HashSet<>(watchersEdit), "watchers do not match");

        softAssert.assertAll();
    }
}