package API.Task;

import TaskPackage.entity.FeedBackTask;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static API.Helper.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO: 50% Done - статусы не настроены
 */

public class FeedBackTaskAPI {
    static int taskId;

    @Test
    public static void test() throws Exception {
        Allure.step("Добавляем FeedBack Task");
        FeedBackTask feedBackTask = feedBackTaskAddEdit(false);
        taskId = feedBackTask.getTaskId();
        Allure.step(CHECK);

        feedBackTaskAssert(feedBackTask, feedBackTaskGet(true));

        Allure.step("Получаем FeedBack Task id=" + taskId);
        feedBackTaskGet(true);

        Allure.step("Редактируем FeedBack Task id=" + taskId);
        FeedBackTask feedBackTaskEdit = feedBackTaskAddEdit(true);
        Allure.step(CHECK);
        feedBackTaskAssert(feedBackTaskEdit, feedBackTaskGet(false));

        Allure.step("Выполняем soft delete FeedBack Task id=" + taskId);
        deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }

    private static JsonObject initializeJsonFeedBackTaskInfo(FeedBackTask feedBackTask) {
       /* JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tab", "primary");
*/
        JsonObject taskObject = new JsonObject();
        taskObject.addProperty("status", feedBackTask.getStatus());
      //  taskObject.addProperty("type", feedBackTask.getType());
       // taskObject.addProperty("requesterId", feedBackTask.getRequesterId());
        taskObject.addProperty("assigner", feedBackTask.getAssigneeId());
     //   taskObject.addProperty("advertId", feedBackTask.getAdvertId());
        taskObject.addProperty("offerId", feedBackTask.getOfferId());
        taskObject.addProperty("affiliateId", feedBackTask.getAffiliateId());

        taskObject.addProperty("notes", feedBackTask.getNotes());
        taskObject.addProperty("dueDate", feedBackTask.getDueDate());

        List<Integer> taskWatchers = feedBackTask.getTaskWatchers();
        JsonArray taskWatchersArray = new JsonArray();
        taskWatchers.forEach(taskWatchersArray::add);
        taskObject.add("watcher", taskWatchersArray);

        List<Integer> tagList = feedBackTask.getTaskTag();
        JsonArray tagArray = new JsonArray();
        tagList.forEach(tagArray::add);
        taskObject.add("tags", tagArray);

        // jsonObject.add("advert", taskObject);
        return taskObject;
    }

    public static FeedBackTask feedBackTaskAddEdit(Boolean isEdit) throws Exception {
        FeedBackTask feedBackTask = new FeedBackTask();
        feedBackTask.fillFeedBackTaskWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonFeedBackTaskInfo(feedBackTask), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? URL + "/task/feedback/" + feedBackTask.getTaskId() + "/edit" :
                URL + "/task/feedback/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
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
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return feedBackTask;
    }

    public static FeedBackTask feedBackTaskGet(Boolean isShow) {
        FeedBackTask feedBackTaskGet = new FeedBackTask();

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
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

        feedBackTaskGet.setType(data.getString("type"));

        JSONObject info = data.getJSONObject("info");
        feedBackTaskGet.setStatus(info.getString("status"));
        feedBackTaskGet.setOfferId(info.isNull("offer") ? null : info.getInt("offer"));
        feedBackTaskGet.setAffiliateId(info.isNull("affiliate") ? null : info.getJSONObject("affiliate").getInt("value"));

        feedBackTaskGet.setRequesterId(info.getInt("requester"));
        feedBackTaskGet.setAssigneeId(info.getInt("assigner"));

       // feedBackTaskGet.setAdvertId(task.isNull("advert") ? null : task.getInt("advertId"));

        feedBackTaskGet.setNotes(info.isNull("notes") ? null : info.getString("notes"));
        feedBackTaskGet.setDueDate(info.getString("dueDate"));

        if (info.get("watcher") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("watcher");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false)
                    .map(element -> Integer.parseInt(element.toString()))
                    .toList();
            feedBackTaskGet.setTaskWatchers(listArray);

        } else feedBackTaskGet.setTaskWatchers(null);

        if (info.get("tags") instanceof JSONArray) {
            JSONArray tagArray = info.getJSONArray("tags");
            List<Integer> tagIdList = new ArrayList<>();
            for (int i = 0; i < tagArray.length(); i++) {
                JSONObject tagObject = tagArray.getJSONObject(i);
                int value = tagObject.getInt("value");
                tagIdList.add(value);
            }
            feedBackTaskGet.setTaskTag(tagIdList);
        } else {
            feedBackTaskGet.setTaskTag(null);
        }

        return feedBackTaskGet;
    }

    public static void feedBackTaskAssert(FeedBackTask feedBackTask, FeedBackTask feedBackTaskGet) {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        // Assert.assertEquals(feedBackTask.getTaskId(), feedBackTaskGet.getTaskId());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(feedBackTask.getStatus(), feedBackTaskGet.getStatus());
        softAssert.assertEquals(feedBackTask.getType(), feedBackTaskGet.getType());
        // softAssert.assertEquals(feedBackTask.getRequesterId(), feedBackTaskGet.getRequesterId());
        softAssert.assertEquals(feedBackTask.getAssigneeId(), feedBackTaskGet.getAssigneeId());
        softAssert.assertEquals(feedBackTask.getAdvertId(), feedBackTaskGet.getAdvertId());
        softAssert.assertEquals(feedBackTask.getOfferId(), feedBackTaskGet.getOfferId());
        softAssert.assertEquals(feedBackTask.getAffiliateId(), feedBackTaskGet.getAffiliateId());
        // softAssert.assertEquals(feedBackTask.getNotes(), feedBackTaskGet.getNotes());
        softAssert.assertEquals(feedBackTask.getDueDate(), feedBackTaskGet.getDueDate());

        List<Integer> tags = feedBackTask.getTaskTag();
        List<Integer> tagsEdit = feedBackTaskGet.getTaskTag();
        Collections.sort(tags);
        Collections.sort(tagsEdit);
        softAssert.assertEquals(tags, tagsEdit);

        List<Integer> watchers = feedBackTask.getTaskWatchers();
        List<Integer> watchersEdit = feedBackTaskGet.getTaskWatchers();
       // Collections.sort(watchers);
       // Collections.sort(watchersEdit);
        softAssert.assertEquals(watchers, watchersEdit);

        softAssert.assertAll();
    }
}