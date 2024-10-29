package API.Task;

import TaskPackage.entity.GeneralTask;
import TaskPackage.entity.Task;
import TaskPackage.entity.TestConversionTask;
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

import java.util.List;
import java.util.stream.StreamSupport;

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

public class TestConversionTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем Test conversion Task");
        Task testConversionTask = new TestConversionTask(taskId, userId);
        taskAddEdit(false, testConversionTask);
        taskId = testConversionTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(testConversionTask, taskGet(true, taskId));

        Allure.step("Получаем Test conversion Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем Test conversion Task id=" + taskId);
        Task testConversionTaskEdit = new TestConversionTask(taskId, userId);
        taskAddEdit(true, testConversionTaskEdit);
        Allure.step(CHECK);
        taskAssert(testConversionTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete Test conversion Task id=" + taskId);
       // deleteMethod("task", String.valueOf(taskId));
       // assertSoftDelete(String.valueOf(taskId), "task");
    }
}