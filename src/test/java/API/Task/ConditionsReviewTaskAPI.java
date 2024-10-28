package API.Task;

import TaskPackage.entity.ConditionsReviewTask;
import TaskPackage.entity.FeedBackTask;
import TaskPackage.entity.Task;
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

public class ConditionsReviewTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем Conditions Review Task");
        Task conditionsReviewTask = new ConditionsReviewTask(taskId, userId);
        taskAddEdit(false, conditionsReviewTask);
        taskId = conditionsReviewTask.getTaskId();
        Allure.step(CHECK);

        taskAssert(conditionsReviewTask, taskGet(true, taskId));

        Allure.step("Получаем Conditions Review Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем Conditions Review Task id=" + taskId);
        Task conditionsReviewTaskEdit = new ConditionsReviewTask(taskId, userId);
        taskAddEdit(true, conditionsReviewTaskEdit);
        Allure.step(CHECK);
        taskAssert(conditionsReviewTaskEdit, taskGet(false, taskId));

        Allure.step("Выполняем soft delete Conditions Review Task id=" + taskId);
       // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }
}