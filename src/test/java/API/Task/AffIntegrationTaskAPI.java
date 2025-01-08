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

import static API.Helper.URL;
import static API.Helper.getValueFromJson;
import static API.Task.FeedBackTaskAPI.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO: сейчас нет сравнений урлов и тайтлов нужно что-то придуать
 */

public class AffIntegrationTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем Aff Integration Task");
        Task affIntegrationTask = new AffIntegrationTask(taskId, userId);
        taskAddEdit(false, affIntegrationTask);
        taskId = affIntegrationTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(affIntegrationTask, taskGet(true, taskId));

        Allure.step("Получаем Aff Integration Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем Aff Integration Task id=" + taskId);
        Task affIntegrationTaskEdit = new AffIntegrationTask(taskId, userId);
        taskAddEdit(true, affIntegrationTaskEdit);
        Allure.step(CHECK);
        taskAssert(affIntegrationTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete Aff Integration Task id=" + taskId);
        // deleteMethod("task", String.valueOf(taskId));
        // assertSoftDelete(String.valueOf(taskId), "task");
    }
}