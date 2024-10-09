package API.Task;

import TaskPackage.entity.FeedBackTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static API.Helper.*;
import static API.Task.FeedBackTaskAPI.feedBackTaskAssert;
import static API.Task.FeedBackTaskAPI.feedBackTaskGet;
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

public class FeedBackTaskAPIActions {
    static int taskId;

    @Test
    public static void copyAction() throws Exception {
        Integer userId = getRandomUserId();
        System.out.println(userId);
        authApi(userId);
        taskId = Integer.parseInt(getRandomValueFromBDWhereNull("id", "task", "deleted_at"));

        // Доступно для всех. Доступен во всех статусах
        Allure.step("Копируем таску Task id=" + taskId);
        copyTask();
        FeedBackTask feedBackTaskCopy = new FeedBackTask(taskId);
        feedBackTaskCopy.setStatus("draft");
        feedBackTaskCopy.setRequesterId(userId);
        feedBackTaskCopy.setDueDate(generateDueDatePlusNDays(3));

        Integer actualTaskId = Integer.valueOf(getLastValueFromBD("id", "task"));
        System.out.println(actualTaskId);
        Allure.step("id новой таски: " + actualTaskId);

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, actualTaskId);
        feedBackTaskAssert(feedBackTaskCopy, feedBackTaskCopyActual);
    }

    @Test //(dependsOnMethods = "copyAction", alwaysRun = true)
    public static void inProgressAction() throws Exception {
        // если статус таски InProgress(+), Resolved(+) ожидаем ошибку
        Integer userId = 103;
        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);
        FeedBackTask feedBackTaskEdited = new FeedBackTask(taskId);

        // Доступно только для Assignee (Assignee(+) и рандомный (+)). Доступен во всех статусах кроме InProgress, Resolved
        // Проверяем только для Assignee
        authApi(userId);

        Allure.step("Меняем статус на InProgress у Task id=" + taskId);
        feedBackInProgress(feedBackTaskEdited.getStatus());
        feedBackTaskEdited.setStatus("progress");

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)
    }

    @Test //(dependsOnMethods = "inProgressAction", alwaysRun = true)
    public static void requestAdditionalInfoAction() throws Exception {
        // рандом если статус Additional info required (+), Resolved(+) ожидаем ошибку
        Integer userId = 103;
        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "assigne_id", String.valueOf(userId)));

        System.out.println(taskId);
        FeedBackTask feedBackTaskEdited = new FeedBackTask(taskId);
        // Доступно только для Assignee. Доступен во всех статусах кроме  Additional info required, Resolved
        authApi(userId);

        Allure.step("Меняем статус на Additional info required у Task id=" + taskId);
        feedBackRequestAdditionalInfo(feedBackTaskEdited.getStatus());
        feedBackTaskEdited.setStatus("additional_info_required");

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);

        // text 2000 (+)
        //feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)
    }

    @Test //(dependsOnMethods = "requestAdditionalInfoAction", alwaysRun = true)
    public static void postponeAction() throws Exception {
        // рандом если статус Postpone(+), Resolved(+) ожидаем ошибку
        Integer userId = 103;
        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "assigne_id", String.valueOf(userId)));
        System.out.println(taskId);
        FeedBackTask feedBackTaskEdited = new FeedBackTask(taskId);
        // Доступно только для Assignee и Requester. Доступен во всех статусах кроме Postponed, Resolved
        // Assignee(+) Requester (+) и рандомный(+)
        authApi(userId);

        Allure.step("Меняем статус на Postpone у Task id=" + taskId);
        String newDueDate = generateDueDatePlusNDays(new Random().nextInt(70));
        System.out.println(newDueDate);
        feedBackRequestPostpone(feedBackTaskEdited.getStatus(), newDueDate);
        feedBackTaskEdited.setStatus("postponed");
        feedBackTaskEdited.setDueDate(newDueDate);

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // reason - maxLength: 500 (+) null (+)
        // dueDate - некоррeктный формат (+) null (+)
        // feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)

        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "requester_id", String.valueOf(userId)));
        System.out.println(taskId);
        feedBackTaskEdited = new FeedBackTask(taskId);
        Allure.step("Меняем статус на Postpone у Task id=" + taskId);
        newDueDate = generateDueDatePlusNDays(new Random().nextInt(70));
        System.out.println(newDueDate);
        feedBackRequestPostpone(feedBackTaskEdited.getStatus(), newDueDate);
        feedBackTaskEdited.setStatus("postponed");
        feedBackTaskEdited.setDueDate(newDueDate);

        feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);
    }

    @Test //(dependsOnMethods = "postponeAction", alwaysRun = true)
    public static void completeAction() throws Exception {
        // рандом если статус Resolved(+) ожидаем ошибку
        Integer userId = 103;
        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "requester_id", String.valueOf(userId)));
        System.out.println(taskId);
        FeedBackTask feedBackTaskEdited = new FeedBackTask(taskId);
        // Доступно только для Requester. Доступен во всех статусах кроме Resolved
        // Requester(+) и рандомный(+)
        authApi(userId);

        Allure.step("Меняем статус на Resolved у Task id=" + taskId);
        feedBackRequestResolve(feedBackTaskEdited.getStatus());
        feedBackTaskEdited.setStatus("resolved");

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);
    }

    @Test //(dependsOnMethods = "requestAdditionalInfoAction", alwaysRun = true)
    public static void updateAction() throws Exception {
        Integer userId = 55;
        taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task", "assigne_id", String.valueOf(userId)));
        System.out.println(taskId);
        FeedBackTask feedBackTaskEdited = new FeedBackTask(taskId);

        authApi(userId);

        String notes = "[1000] In a world increasingly driven by technology, the importance of digital literacy cannot be overstated. From communication to commerce, education to entertainment, nearly every aspect of our lives is touched by digital advancements. Being digitally literate means more than just knowing how to use a computer or smartphone; it involves understanding how technology works, how to evaluate online information critically, and how to engage safely and responsibly in digital spaces. In today's job market, digitIn a world increasingly driven by technology, the importance of digital literacy cannot be overstated. From communication to commerce, education to entertainment, nearly every aspect of our lives is touched by digital advancements. Being digitally literate means more than just knowing how to use a computer or smartphone; it involves understanding how technology works, how to evaluate online information critically, and how to engage safely and responsibly in digital spaces. In today'";
        // String notes = generateName(30, TASK_WORDS);
        // List<Integer> watchers = new ArrayList<>();
        List<Integer> watchers = getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5).stream().map(Integer::valueOf).collect(Collectors.toList());
        // List<Integer> tags = new ArrayList<>();
        List<Integer> tags = getSomeValuesFromBD("id", "task_tag", 5).stream().map(Integer::valueOf).collect(Collectors.toList());

        Allure.step("Меняем статус на Conditions updated у Task id=" + taskId);
        feedBackRequestUpdate(feedBackTaskEdited.getStatus(), notes, watchers, tags);
        feedBackTaskEdited.setStatus("conditions_updated");

        feedBackTaskEdited.setNotes(notes);
        feedBackTaskEdited.setTaskWatchers(watchers);
        feedBackTaskEdited.setTaskTag(tags);

        FeedBackTask feedBackTaskCopyActual = feedBackTaskGet(true, taskId);
        feedBackTaskAssert(feedBackTaskEdited, feedBackTaskCopyActual);

        // notes - maxLength: 1000 (+) "" (+)
        // dueDate - некоррeктный формат (+) null (+)
        // feedBackMassageAssert(feedBackTaskEdited, feedBackTaskCopyActual);
        // Status: Changed to progress by Варвара -Анастасия Александровна Петрова-Васечкина Скоробейникова (+)
    }


    public static void copyTask() {
        String path = URL + "/task/" + taskId + "/action/copy";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void feedBackInProgress(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/in_progress";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("progress")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not available\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void feedBackRequestAdditionalInfo(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/request_additional_info";
        JsonObject jsonObject = new JsonObject();
       /* jsonObject.addProperty("text", "[2000] In a world increasingly driven by technology, " +
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
                "use in our new better feature(!)");*/

        jsonObject.addProperty("text", generateName(30, TASK_WORDS));
        //  {"success":false,"error":[{"name":"text","msg":"This value is too long. It should have 2000 characters or less."}]}

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("additional_info_required")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not available\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void feedBackRequestPostpone(String feedBackTaskStatus, String newDueDate) {
        String path = URL + "/task/" + taskId + "/action/postpone";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dueDate", newDueDate);
        jsonObject.addProperty("reason", "[500] In a world increasingly driven by technology, the importance of digital literacy cannot be overstated. From communication to commerce, education to entertainment, nearly every aspect of our lives is touched by digital advancements. Being digitally literate means more than just knowing how to use a computer or smartphone; it involves understanding how technology works, how to evaluate online information critically, and how to engage safely and responsibly in digital spaces. In today's job m");
        // generateName(5, TASK_WORDS_REASON)

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").body(jsonObject.toString()).post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved") || feedBackTaskStatus.equals("postponed")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not available\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void feedBackRequestResolve(String feedBackTaskStatus) {
        String path = URL + "/task/" + taskId + "/action/complete";

        System.out.println(path);
        Response response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", KEY).header("Accept", "application/json").header("Content-Type", "application/json").post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        if (feedBackTaskStatus.equals("resolved")) {
            System.out.println("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Allure.step("Текущий статус '" + feedBackTaskStatus + "' изменить нельзя ");
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not available\",\"name\":\"logic_exception\"}]}");
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
            Assert.assertEquals(responseBody, "{\"success\":false,\"error\":[{\"msg\":\"Action not available\",\"name\":\"logic_exception\"}]}");
        } else Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void actionAssertMessage(FeedBackTask feedBackTask, FeedBackTask feedBackTaskGet) {
        Allure.step("Сравнение отправленных значений в полях с полученными из get");
        // Assert.assertEquals(feedBackTask.getTaskId(), feedBackTaskGet.getTaskId());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(feedBackTask.getStatus(), feedBackTaskGet.getStatus());
        softAssert.assertEquals(feedBackTask.getType(), feedBackTaskGet.getType());
        System.out.println(feedBackTaskGet.getRequesterId());
        softAssert.assertTrue(feedBackTaskGet.getRequesterId() == 103);
        softAssert.assertEquals(feedBackTask.getAssigneeId(), feedBackTaskGet.getAssigneeId());
        softAssert.assertEquals(feedBackTask.getAdvertId(), feedBackTaskGet.getAdvertId());
        softAssert.assertEquals(feedBackTask.getOfferId(), feedBackTaskGet.getOfferId());
        softAssert.assertEquals(feedBackTask.getAffiliateId(), feedBackTaskGet.getAffiliateId());
        // softAssert.assertEquals(feedBackTask.getNotes(), feedBackTaskGet.getNotes());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime actualDate = LocalDateTime.parse(feedBackTask.getDueDate(), formatter);
        LocalDateTime expectedDate = LocalDateTime.parse(feedBackTaskGet.getDueDate(), formatter);

        softAssert.assertEquals(actualDate.toString(), expectedDate.toString());

        List<Integer> tags = feedBackTask.getTaskTag();
        List<Integer> tagsEdit = feedBackTaskGet.getTaskTag();
        softAssert.assertEquals(new HashSet<>(tags), new HashSet<>(tagsEdit), "tags do not match");

        List<Integer> watchers = feedBackTask.getTaskWatchers();
        List<Integer> watchersEdit = feedBackTaskGet.getTaskWatchers();
        softAssert.assertEquals(new HashSet<>(watchers), new HashSet<>(watchersEdit), "watchers do not match");

        softAssert.assertAll();
    }
}