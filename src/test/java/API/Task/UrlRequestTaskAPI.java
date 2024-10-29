package API.Task;

import TaskPackage.entity.FeedBackTask;
import TaskPackage.entity.Task;
import TaskPackage.entity.UrlRequestTask;
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
import static Helper.Auth.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO:
 */

public class UrlRequestTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем URL request Task");
        Task urlRequestTask = new UrlRequestTask(taskId, userId);
        taskAddEdit(false, urlRequestTask);
        taskId = urlRequestTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(urlRequestTask, taskGet(true, taskId));

        Allure.step("Получаем URL request Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем URL request Task id=" + taskId);
        Task urlRequestTaskEdit = new UrlRequestTask(taskId, userId);
        taskAddEdit(true, urlRequestTaskEdit);
        Allure.step(CHECK);
        taskAssert(urlRequestTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete URL request Task id=" + taskId);
     //   deleteMethod("task", String.valueOf(taskId));
      //  assertSoftDelete(String.valueOf(taskId), "task");
    }
}