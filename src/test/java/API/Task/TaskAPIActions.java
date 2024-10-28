
package API.Task;

import TaskPackage.entity.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static API.Helper.*;

import static API.Task.FeedBackTaskAPI.taskAssert;
import static API.Task.FeedBackTaskAPI.taskGet;
import static Helper.Adverts.generateName;
import static Helper.Auth.*;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;


/***
 Тест проверяет работу API методов
 для actions (copy, inProgress, requestAdditionalInfo, postpone)
 во вкладке Task
 //TODO: остальные actions и message
 */


public class TaskAPIActions {
    static Integer taskId;
    static String taskType;
    static String reason;
    static String note;
    static String newDate;


    // static String taskType = "conditions_review";
    // static String taskType = "general";
    // static String taskType = "url_request";


    @Test
    @Parameters({"taskTypeParameter"})
    public static void copyAction(String taskTypeParameter) throws Exception {

        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        authApi(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereNull("id", "task", taskType, "deleted_at"));

        // Доступно для всех. Доступен во всех статусах
        Allure.step("Копируем таску Task id=" + taskId);
        copyTask();

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        note = task.getNotes();

        task.setStatus("draft");
        task.setRequesterId(userId);
        task.setDueDate(generateDueDatePlusNDays(3));

        Integer actualTaskId = Integer.valueOf(getLastValueFromBD("id", "task"));
        System.out.println(actualTaskId);
        Allure.step("id новой таски: " + actualTaskId);

        Task newTaskGet = taskGet(true, actualTaskId);
        newTaskGet.setTaskId(actualTaskId);
        taskAssert(task, newTaskGet);

        messageAssert(newTaskGet);

    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void inProgressAction(String taskTypeParameter) throws Exception {
        // если статус таски InProgress(+), Resolved(+) ожидаем ошибку
        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        // Доступно только для Assignee (Assignee(+) и рандомный (+)). Доступен во всех статусах кроме InProgress, Resolved
        // Проверяем только для Assignee
        authApi(userId);

        Allure.step("Меняем статус на InProgress у Task id=" + taskId);
        inProgress(task.getStatus());
        task.setStatus("progress");

        taskAssert(task, taskGet(false, taskId));

        messageAssert(task);
    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void requestAdditionalInfoAction(String taskTypeParameter) throws Exception {
        // рандом если статус Additional info required (+), Resolved(+) ожидаем ошибку
        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        // Доступно только для Assignee. Доступен во всех статусах кроме  Additional info required, Resolved
        authApi(userId);

        Allure.step("Меняем статус на Additional info required у Task id=" + taskId);
        requestAdditionalInfo(task.getStatus());
        task.setStatus("additional_info_required");

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);
    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void postponeAction(String taskTypeParameter) throws Exception {

        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        // Доступно для Assignee и Requester. Доступен во всех статусах кроме Postponed и Resolved

        authApi(userId);
        Allure.step("Меняем статус на Postpone у Task id=" + taskId);
        newDate = generateDueDatePlusNDays(new Random().nextInt(70));
        postpone(task.getStatus(), newDate);

        task.setStatus("postponed");
        task.setDueDate(newDate);

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);

        // reason - maxLength: 500 (+) null (+)
        // dueDate - некоррeктный формат (+) null (+)
        // feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)

        // Доступно для Assignee и Requester. Доступен во всех статусах кроме Postponed и Resolved

        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "requester_id", String.valueOf(userId)));

        System.out.println(taskId);

        task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        authApi(userId);
        Allure.step("Меняем статус на Postpone у Task id=" + taskId);
        newDate = generateDueDatePlusNDays(new Random().nextInt(70));

        postpone(task.getStatus(), newDate);

        task.setStatus("postponed");
        task.setDueDate(newDate);

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);
    }


    @Test
    @Parameters({"taskTypeParameter"})
    public static void reviewAction(String taskTypeParameter) throws Exception {

        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);


        // Доступно только для Assignee. Доступен во всех статусах кроме ??? Review, Resolved
        authApi(userId);

        Allure.step("Меняем статус на Review у Task id=" + taskId);
        review(task.getStatus());
        task.setStatus("review");

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);

    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void completeAction(String taskTypeParameter) throws Exception {
        // рандом если статус Resolved(+) ожидаем ошибку

        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);

        // Доступно только для Requester. Доступен во всех статусах кроме Resolved
        // Requester(+) и рандомный(+)
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "requester_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        authApi(userId);
        Allure.step("Меняем статус на Resolved у Task id=" + taskId);

        resolve(task.getStatus());
        task.setStatus("resolved");

        taskAssert(task, taskGet(false, taskId));
    }


    @Test
    @Parameters({"taskTypeParameter"})
    public static void cancelAction(String taskTypeParameter) throws Exception {

        taskType = taskTypeParameter;
        Integer userId = getRandomUserId();
        System.out.println(userId);
        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);

        Task task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        // Доступно для Assignee и Requester. Доступен во всех статусах кроме Postponed и Resolved

        authApi(userId);
        Allure.step("Меняем статус на Cancelled у Task id=" + taskId);
        cancel(task.getStatus());

        task.setStatus("cancelled");

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);

        // reason - maxLength: 500 (+) null (+)
        // Доступно для Assignee и Requester. Доступен во всех статусах кроме Postponed и Resolved

        taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskType,
                "requester_id", String.valueOf(userId)));

        System.out.println(taskId);

        task = null;
        if (taskType.equals("general"))
            task = new GeneralTask(taskId);
        if (taskType.equals("test_conversion"))
            task = new TestConversionTask(taskId);
        if (taskType.equals("feedback"))
            task = new FeedBackTask(taskId);
        if (taskType.equals("conditions_review"))
            task = new ConditionsReviewTask(taskId);
        if (taskType.equals("url_request"))
            task = new UrlRequestTask(taskId);
        if (taskType.equals("integration"))
            task = new IntegrationTask(taskId);
        if (taskType.equals("offer_approval"))
            task = new OfferApproveTask(taskId);

        authApi(userId);
        Allure.step("Меняем статус на Cancelled у Task id=" + taskId);

        cancel(task.getStatus());

        task.setStatus("cancelled");

        taskAssert(task, taskGet(false, taskId));
        messageAssert(task);

    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void updateAction(String taskTypeParameter) throws Exception {
        if (taskTypeParameter.equals("feedback")) {
            Integer userId = getRandomUserId();
            taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskTypeParameter,
                    "assigne_id", String.valueOf(userId)));
            System.out.println(taskId);
            Task feedBackTaskEdited = new FeedBackTask(taskId);

            authApi(userId);

            String notes = "Update notes " + generateName(30, TASK_WORDS);
            // String notes = generateName(30, TASK_WORDS);
            // List<Integer> watchers = new ArrayList<>();
            List<Integer> watchers = getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5).stream().map(Integer::valueOf).collect(Collectors.toList());
            // List<Integer> tags = new ArrayList<>();
            List<Integer> tags = getSomeValuesFromBD("id", "task_tag", 5).stream().map(Integer::valueOf).collect(Collectors.toList());

            Allure.step("Меняем статус на Conditions updated у Task id=" + taskId);
            feedBackRequestUpdate(feedBackTaskEdited.getStatus(), notes, watchers, tags);
            // feedBackTaskEdited.setStatus("conditions_updated");

            feedBackTaskEdited.setNotes(notes);
            feedBackTaskEdited.setTaskWatchers(watchers);
            feedBackTaskEdited.setTaskTag(tags);

            Task feedBackTaskCopyActual = taskGet(true, taskId);
            feedBackTaskCopyActual.setTaskId(taskId);
            taskAssert(feedBackTaskEdited, feedBackTaskCopyActual);
            messageAssert(feedBackTaskCopyActual);

            // notes - maxLength: 1000 (+) "" (+)
            // dueDate - некоррeктный формат (+) null (+)
            // feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
            // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)
        }
    }

    @Test
    @Parameters({"taskTypeParameter"})
    public static void declineAction(String taskTypeParameter) throws Exception {
        if (taskTypeParameter.equals("offer_upproval")) {
            Integer userId = getRandomUserId();
            System.out.println(userId);
            taskId = Integer.parseInt(getRandomTaskFromBDWhereAndNotSoftDelete("id", "task", taskTypeParameter,
                    "assigne_id", String.valueOf(userId)));

            System.out.println(taskId);

            Task task = new OfferApproveTask(taskId);

            // Доступно для Assignee

            authApi(userId);
            Allure.step("Меняем статус на Review у Task id=" + taskId);
            decline(task.getStatus());

            task.setStatus("review");

            taskAssert(task, taskGet(false, taskId));
            messageAssert(task);
        }
    }


    public static void copyTask() {
        String path = URL + "/task/" + taskId + "/action/copy";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void inProgress(String taskStatus) {
        String path = URL + "/task/" + taskId + "/action/in_progress";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (taskStatus.equals("resolved") || taskStatus.equals("progress")) {
            System.out.println("Текущий статус '" + taskStatus + "' изменить нельзя");
            Allure.step("Текущий статус '" + taskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void requestAdditionalInfo(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/request_additional_info";
        JsonObject jsonObject = new JsonObject();
   /*     jsonObject.addProperty("text", "[2000] In a world increasingly driven by technology, " +
                "the importance of digital literacy cannot be overstated. From communication to commerce, education " +
                "to entertainment, nearly every aspect of our lives is touched by digital advancements. Being digitally " +
                "literate means more than just knowing how to use a computer or smartphone; it involves understanding how" +
                " technology works, how to evaluate online information critically, and how to engage safely and responsibly" +
                " in digital spaces. In today's job market, digitIn a world increasingly driven by technology, the importance " +
                "of digital literacy cannot be overstated. From communication to commerce, education to entertainment, nearly " +
                "every aspect of our lives is touched by digital advancements. Being digitally literate means more than just " +
                "knowing how to use a computer or smartphone; it involves understanding how technology works, how to evaluate" +
                " online information critically, and how to engage safely and responsibly in digital spaces. In today's job " +
                "market, digitIn a world increasingly driven by technology, the importance of digital literacy cannot be " +
                "overstated. From communication to commerce, education to entertainment, nearly every aspect of our lives " +
                "is touched by digital advancements. Being digitally literate means more than just knowing how to use a computer" +
                " or smartphone; it involves understanding how technology works, how to evaluate online information critically, " +
                "and how to engage safely and responsibly in digital spaces. In today's job market, digitIn a world increasingly" +
                " driven by technology, the importance of digital literacy cannot be overstated. From communication to commerce," +
                " education to entertainment, nearly every aspect of our lives is touched by digital advancements." +
                " Being digitally literate means more than just knowing how to use a computer or smartphone; it involves" +
                " understanding how technology works, how to evaluate online information critically, and how to engage safely " +
                "use in our new better feature(!)"); */

        reason = "I need more information about " + generateName(30, TASK_WORDS);
        jsonObject.addProperty("text", reason);
        //  {"success":false,"error":[{"name":"text","msg":"This value is too long. It should have 2000 characters or less."}]}

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("additional_info_required")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void review(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/review";
        JsonObject jsonObject = new JsonObject();
        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("review")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));

    }

    public static void postpone(String feedBackTaskStatus, String newDueDate) {
        String path = URL + "/task/" + taskId + "/action/postpone";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dueDate", newDueDate);
        reason = "The reason for the delay is the need for additional time to ensure quality" +
                " and accuracy." + generateName(20, TASK_WORDS);

        jsonObject.addProperty("reason", reason);
        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("postponed")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void cancel(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/cancel";
        JsonObject jsonObject = new JsonObject();

        reason = "The reason for cancellation is a shift in priorities, making it unnecessary to proceed" +
                " with this task at this time." + generateName(50, TASK_WORDS);

        jsonObject.addProperty("reason", reason);
        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("cancelled")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void resolve(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/complete";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void decline(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/decline";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void feedBackRequestUpdate(String feedBackTaskStatus, String notes, List<Integer> watchers, List<Integer> tags) {
        String path = URL + "/task/" + taskId + "/action/update_condition";
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("notes", notes);

        JsonArray tagsArray = new JsonArray();
        for (Integer i : tags)
            tagsArray.add(i);

        JsonArray watchersArray = new JsonArray();
        for (Integer i : watchers)
            watchersArray.add(i);

        jsonObject.add("tags", tagsArray);
        jsonObject.add("watcher", watchersArray);


        System.out.println(path);
        System.out.println(jsonObject);

        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not supported this task status\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }


    public static void messageAssert(Task task) throws Exception {
        Allure.step("Проверка созданного в чате сообщения");
        SoftAssert softAssert = new SoftAssert();

        String lastTaskMessageText = getLastValueFromBDWhere("text", "task_message",
                "task_id", String.valueOf(task.getTaskId()));
        String lastTaskMessageType = getLastValueFromBDWhere("type", "task_message",
                "task_id", String.valueOf(task.getTaskId()));

        if (task.getStatus().equals("draft")) {
            softAssert.assertEquals(lastTaskMessageText, note);
            softAssert.assertEquals(lastTaskMessageType, "notice");
            System.out.println("Проверка созданного в чате сообщения " + note);
        }

        if (task.getStatus().equals("progress")) {
            softAssert.assertTrue(lastTaskMessageText.contains("Status: Changed to progress by"));
            softAssert.assertEquals(lastTaskMessageType, "info");
            System.out.println("Проверка созданного в чате сообщения \"Status: Changed to progress by\"");
        }

        if (task.getStatus().equals("additional_info_required")) {
            softAssert.assertEquals(lastTaskMessageText, reason);
            softAssert.assertEquals(lastTaskMessageType, "notice");
            System.out.println("Проверка созданного в чате сообщения " + reason);
        }

        if (task.getStatus().equals("postponed")) {
            softAssert.assertTrue(lastTaskMessageText.contains("New due date set: " + newDate + " by "));
            softAssert.assertTrue(lastTaskMessageText.contains("Reason: " + reason));

            softAssert.assertEquals(lastTaskMessageType, "warning");
            System.out.println("Проверка созданного в чате сообщения \"New due date set: " + newDate + " by ..." + reason);
        }

        if (task.getStatus().equals("review")) {
            softAssert.assertTrue(lastTaskMessageText.contains("The task is waiting for "));
            softAssert.assertTrue(lastTaskMessageText.contains("review"));

            softAssert.assertEquals(lastTaskMessageType, "warning");
            System.out.println("Проверка созданного в чате сообщения \"The task is waiting for ... review");
        }

        if (task.getStatus().equals("cancelled")) {
            softAssert.assertTrue(lastTaskMessageText.contains("The task has been cancelled by"));
            softAssert.assertTrue(lastTaskMessageText.contains(reason));
            System.out.println(lastTaskMessageText);
            softAssert.assertEquals(lastTaskMessageType, "info");
            System.out.println("Проверка созданного в чате сообщения \"The task has been cancelled by ... Reason: " + reason);
        }

        if (task.getStatus().equals("conditions_updated")) {
            softAssert.assertTrue(lastTaskMessageText.contains("The task has been cancelled by"));
            softAssert.assertTrue(lastTaskMessageText.contains(reason));
            System.out.println(lastTaskMessageText);
            softAssert.assertEquals(lastTaskMessageType, "info");
            System.out.println("Проверка созданного в чате сообщения \"The task has been cancelled by ... Reason: " + reason);
        }

        softAssert.assertAll();
    }
}

