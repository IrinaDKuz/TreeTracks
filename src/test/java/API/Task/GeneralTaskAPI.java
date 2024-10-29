package API.Task;

import TaskPackage.entity.FeedBackTask;
import TaskPackage.entity.GeneralTask;
import TaskPackage.entity.Task;
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
import static API.Task.FeedBackTaskAPI.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO:
 */

public class GeneralTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем General Task");
        Task generalTask = new GeneralTask(taskId, userId);
        taskAddEdit(false, generalTask);
        taskId = generalTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(generalTask, taskGet(true, taskId));

        Allure.step("Получаем General Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем General Task id=" + taskId);
        Task generalTaskEdit = new GeneralTask(taskId, userId);
        taskAddEdit(true, generalTaskEdit);
        Allure.step(CHECK);
        taskAssert(generalTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete General Task id=" + taskId);
        // deleteMethod("task", String.valueOf(taskId));
        // assertSoftDelete(String.valueOf(taskId), "task");
    }
}