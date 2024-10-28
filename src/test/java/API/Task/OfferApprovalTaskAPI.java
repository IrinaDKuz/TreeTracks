package API.Task;

import TaskPackage.entity.OfferApproveTask;
import TaskPackage.entity.Task;
import io.qameta.allure.Allure;
import org.testng.annotations.Test;

import static API.Helper.assertSoftDelete;
import static API.Task.FeedBackTaskAPI.*;
import static Helper.AllureHelper.CHECK;
import static Helper.Auth.authApi;
import static Helper.Auth.getRandomUserId;

/***
 Тест проверяет работу API методов
 - get, add/edit, soft delete, проверка
 во вкладке Task - "Primary Info"
 //TODO:
 */

public class OfferApprovalTaskAPI {
    static Integer taskId;
    static Integer userId;

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        Allure.step("Добавляем Offer approval Task");
        Task offerApproveTask = new OfferApproveTask(taskId, userId);
        taskAddEdit(false, offerApproveTask);
        taskId = offerApproveTask.getTaskId();
        Allure.step(CHECK);
        taskAssert(offerApproveTask, taskGet(true, taskId));

        Allure.step("Получаем Offer approval Task id=" + taskId);
        taskGet(true, taskId);

        Allure.step("Редактируем Offer approval Task id=" + taskId);
        Task offerApproveTaskEdit = new OfferApproveTask(taskId, userId);
        taskAddEdit(true, offerApproveTaskEdit);
        taskId = offerApproveTaskEdit.getTaskId();
        Allure.step(CHECK);
        taskAssert(offerApproveTaskEdit, taskGet(true, taskId));

        Allure.step("Выполняем soft delete Offer approval Task id=" + + taskId);
        // deleteMethod("task", String.valueOf(taskId));
        assertSoftDelete(String.valueOf(taskId), "task");
    }
}