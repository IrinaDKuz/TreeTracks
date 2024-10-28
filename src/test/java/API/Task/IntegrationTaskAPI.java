package API.Task;

import TaskPackage.entity.IntegrationTask;
import TaskPackage.entity.OfferApproveTask;
import TaskPackage.entity.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
 // TODO: проверка файла
 */

public class IntegrationTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем Integration Task");
        Task integrationTask = new IntegrationTask(taskId, userId);
        integrationTaskAddEdit(false, (IntegrationTask)integrationTask);
        taskId = integrationTask.getTaskId();
        Allure.step(CHECK);

        integrationTaskAssert(integrationTask, integrationTaskGet(true, taskId));

        Allure.step("Получаем Integration Task id=" + taskId);
        integrationTaskGet(true, taskId);

        Allure.step("Редактируем Integration Task id=" + taskId);
        Task integrationTaskEdit = new IntegrationTask(taskId, userId);
        integrationTaskAddEdit(true, (IntegrationTask)integrationTaskEdit);
        Allure.step(CHECK);
        integrationTaskAssert(integrationTaskEdit, integrationTaskGet(false, taskId));

        Allure.step("Выполняем soft delete Integration Task id=" + taskId);
        // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }

    public static IntegrationTask integrationTaskAddEdit(Boolean isEdit, IntegrationTask integrationTask) throws Exception {

        String path = isEdit ? URL + "/task/" + taskId + "/action/edit" :
                URL + "/task/integration/new";

        System.out.println(path);

        List<Integer> taskWatchers = integrationTask.getTaskWatchers();

        List<Integer> taskTags = integrationTask.getTaskTag();

        RequestSpecification request = RestAssured.given()
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .multiPart("file[]", new java.io.File(integrationTask.getFilePath()))
                .multiPart("status", integrationTask.getStatus())
                .multiPart("assigner", integrationTask.getAssigneeId())
                .multiPart("offerId", integrationTask.getOfferId())
                .multiPart("accessId", integrationTask.getAccessId())
                .multiPart("platformTypeId", integrationTask.getPlatformType())
                .multiPart("description", integrationTask.getNotes())
                .multiPart("dueDate", integrationTask.getDueDate())
                .log().all(); // Логирование запроса


        for (int i = 0; i < taskWatchers.size(); i++) {
            request.multiPart("watcher[" + i + "]", taskWatchers.get(i));
        }

        for (int i = 0; i < taskTags.size(); i++) {
            request.multiPart("tags[" + i + "]", taskTags.get(i));
        }

        System.out.println(request);

        Response response = request
                .when()
                .post(path)
                .then()
                .extract()
                .response();

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            integrationTask.setTaskId(jsonResponse.getJSONObject("data").getInt("id"));
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }

        integrationTask.setRequesterId(userId);

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return integrationTask;
    }

    public static IntegrationTask integrationTaskGet(Boolean isShow, Integer taskId) throws Exception {
        IntegrationTask integrationTaskGet = new IntegrationTask(true);

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
        integrationTaskGet.setType(info.getString("type"));
        integrationTaskGet.setStatus(info.getString("status"));

        integrationTaskGet.setOfferId(getValueFromJson(info, "offer"));
        integrationTaskGet.setAdvertId(getValueFromJson(info, "advert"));

        integrationTaskGet.setRequesterId(info.getInt("requester"));
        integrationTaskGet.setAssigneeId(info.getInt("assigner"));

        JSONObject platformType = info.getJSONObject("platformType");
        integrationTaskGet.setPlatformType(platformType.getInt("value"));

        JSONObject access = info.getJSONObject("access");
        integrationTaskGet.setAccessId(access.getInt("value"));

        integrationTaskGet.setNotes(info.isNull("description") ? null : info.getString("description"));

        integrationTaskGet.setDueDate(info.getString("dueDate"));

        if (info.get("watcher") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("watcher");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false)
                    .map(element -> Integer.parseInt(element.toString()))
                    .toList();
            integrationTaskGet.setTaskWatchers(listArray);

        } else integrationTaskGet.setTaskWatchers(null);


        if (info.get("tags") instanceof JSONArray) {
            JSONArray watcherArray = info.getJSONArray("tags");
            List<Integer> listArray = StreamSupport.stream(watcherArray.spliterator(), false)
                    .map(element -> Integer.parseInt(element.toString()))
                    .toList();
            integrationTaskGet.setTaskTag(listArray);

        } else integrationTaskGet.setTaskTag(null);

        return integrationTaskGet;
    }

    public static void integrationTaskAssert(Task task, IntegrationTask integrationTaskGet) {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        IntegrationTask integrationTask = (IntegrationTask) task;

        // Assert.assertEquals(integrationTask.getTaskId(), integrationTaskGet.getTaskId());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(integrationTask.getStatus(), integrationTaskGet.getStatus());
        softAssert.assertEquals(integrationTask.getType(), integrationTaskGet.getType());
        softAssert.assertEquals(integrationTask.getRequesterId(), integrationTaskGet.getRequesterId());
        softAssert.assertEquals(integrationTask.getAssigneeId(), integrationTaskGet.getAssigneeId());
        softAssert.assertEquals(integrationTask.getAdvertId(), integrationTaskGet.getAdvertId());
        softAssert.assertEquals(integrationTask.getOfferId(), integrationTaskGet.getOfferId());
        softAssert.assertEquals(integrationTask.getDueDate(), integrationTaskGet.getDueDate());
        softAssert.assertEquals(integrationTask.getNotes(), integrationTaskGet.getNotes());

        softAssert.assertEquals(integrationTask.getPlatformType(), integrationTaskGet.getPlatformType());
        softAssert.assertEquals(integrationTask.getAccessId(), integrationTaskGet.getAccessId());

        List<Integer> tags = integrationTask.getTaskTag();
        List<Integer> tagsEdit = integrationTaskGet.getTaskTag();
        softAssert.assertEquals(new HashSet<>(tags), new HashSet<>(tagsEdit), "tags do not match");

        List<Integer> watchers = integrationTask.getTaskWatchers();
        List<Integer> watchersEdit = integrationTaskGet.getTaskWatchers();
        softAssert.assertEquals(new HashSet<>(watchers), new HashSet<>(watchersEdit), "watchers do not match");

        softAssert.assertAll();
    }
}