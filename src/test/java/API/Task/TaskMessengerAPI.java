package API.Task;

import TaskPackage.entity.TaskMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static API.Helper.URL;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;
import static SQL.AdvertSQL.getRandomValueFromBDWhereAndNotSoftDelete;

/***
 Тест проверяет работу API методов
 - get, add, проверка
 во вкладке Task - "Message"
 TODO: taskMessageAssert
 */

public class TaskMessengerAPI {
    static int taskId;
    static int taskMessageId;
    static String taskType;

    // static String taskType = "conditions_review";
    // static String taskType = "feedback";

    @Test
    @Parameters({"taskTypeParameter"})
    public static void newMessageTest(String taskTypeParameter) throws Exception {
       taskType = taskTypeParameter;
       taskId = Integer.parseInt(getRandomValueFromBDWhereAndNotSoftDelete("id", "task",
                "type", taskType));
        newMessage(taskId, false);
    }

    @Test(dependsOnMethods = "newMessageTest", alwaysRun = true)
    public static void newReplyMessageTest() throws Exception {
        newMessage(taskId, true);
    }

    @Test(dependsOnMethods = "newReplyMessageTest", alwaysRun = true)
    public static void newPinMessageTest() throws Exception {
        newActionMessage(taskId, "pin");
    }

    @Test(dependsOnMethods = "newPinMessageTest", alwaysRun = true)
    @Parameters({"taskTypeParameter"})
    public static void newSpecialActionMessageTest(String taskTypeParameter) throws Exception {
        newActionMessage(taskId, taskTypeParameter);
    }

    @Test(dependsOnMethods = "newSpecialActionMessageTest", alwaysRun = true)
    @Parameters({"taskTypeParameter"})
    public static void newFileMessageTest() throws Exception {
        newFileMessage(taskId);
    }


    public static void newActionMessage(int taskId, String action) throws Exception {
        int userId = getRandomUserId();
        authApi(userId);
        Thread.sleep(2000);

        Allure.step("Добавляем Task message isReply = false и " + action);
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.fillTaskMessageWithRandomData(taskId, userId);
        taskMessage.setReplyId(null);

        taskMessageAdd(taskMessage);

        if (action.equals("pin")) {
            taskMessage.setPin(true);
            taskMessageAction(taskMessage, "pin");
        }
        if (action.equals("feedback")) {
            taskMessage.setFeedback(true);
            taskMessageAction(taskMessage, "feedback");
        }

        if (action.equals("conditions_review")) {
            taskMessage.setUpdatedConditions(true);
            taskMessageAction(taskMessage, "updated-conditions");
        }

        if (action.equals("url_request")) {
            taskMessage.setUrl(true);
            taskMessageAction(taskMessage, "url");
        }

        Allure.step(CHECK);
        taskMessageAssert(taskMessage);
        Allure.step("Получаем Task message id=" + taskId);
        taskMessageGet(true);
    }

    public static void newMessage(int taskId, Boolean isReply) throws Exception {
        int userId = getRandomUserId();
        authApi(userId);
        Thread.sleep(2000);

        Allure.step("Добавляем Task message isReply = " + isReply);
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.fillTaskMessageWithRandomData(taskId, userId);
        if (!isReply)
            taskMessage.setReplyId(null);

        taskMessageAssert(taskMessageAdd(taskMessage));

        Allure.step("Получаем  Task message id=" + taskId);
        taskMessageGet(true);
    }

    public static void newFileMessage(int taskId) throws Exception {
        int userId = getRandomUserId();
        authApi(userId);
        Thread.sleep(2000);

        Allure.step("Добавляем Task message с файлом");
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.fillTaskMessageWithRandomData(taskId, userId);
        taskMessage.fillTaskMessageWithFile();
        taskMessage.setReplyId(null);

        String path = URL + "/task/" + taskMessage.getTaskId() + "/add-message";
        System.out.println(path);

        RequestSpecification request = RestAssured.given()
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .multiPart("file", new java.io.File(taskMessage.getFilePath()))
                .multiPart("text", taskMessage.getText())
                .log().all();

        Response response = request
                .when()
                .post(path)
                .then()
                .extract()
                .response();

        String responseBody = response.getBody().asString();
        System.out.println(ADD_RESPONSE + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);
        JSONObject jsonResponse = new JSONObject(responseBody);
        taskMessageId = jsonResponse.getJSONObject("data").getInt("id");
        taskMessage.setTaskMessageId(taskMessageId);
        taskMessageAssert(taskMessage);

        Allure.step("Получаем Task Massage id=" + taskId);
        taskMessageGet(true);
    }

    private static JsonObject initializeJsonTaskMessageInfo(TaskMessage taskMessage) {
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("text", taskMessage.getText());
        //messageObject.addProperty("file", taskMessage.getFile().getName());
        if (taskMessage.getReplyId() != null) {
            messageObject.addProperty("replyId", taskMessage.getReplyId());
        }
        return messageObject;
    }

    public static TaskMessage taskMessageAdd(TaskMessage taskMessage) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonTaskMessageInfo(taskMessage), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = URL + "/task/" + taskMessage.getTaskId() + "/add-message";
        System.out.println(path);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();

        System.out.println(ADD_RESPONSE + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);
        JSONObject jsonResponse = new JSONObject(responseBody);

        taskMessageId = jsonResponse.getJSONObject("data").getInt("id");

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        taskMessage.setTaskMessageId(taskMessageId);
        return taskMessage;
    }

    public static List<TaskMessage> taskMessageGet(Boolean isShow) {
        List<TaskMessage> taskMessageList = new ArrayList<>();

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
        String taskType = info.getString("type");


        JSONArray message = data.getJSONArray("message");

        for (int i = 0; i < message.length(); i++) {

            TaskMessage taskMessage = new TaskMessage();
            JSONObject messageObject = message.getJSONObject(i);

            taskMessage.setTaskMessageId(messageObject.getInt("id"));
            taskMessage.setType(messageObject.getString("type"));
            taskMessage.setAuthorId(messageObject.getInt("author"));
            taskMessage.setReplyId(messageObject.isNull("replyId") ? null : messageObject.getInt("replyId"));

            taskMessage.setDate(messageObject.getString("date"));

            if (taskType.equals("feedback"))
                taskMessage.setFeedback(messageObject.getBoolean("isFeedback"));
            if (taskType.equals("conditions_review"))
                taskMessage.setUpdatedConditions(messageObject.getBoolean("isUpdatedConditions"));
            if (taskType.equals("url_request"))
                taskMessage.setUrl(messageObject.getBoolean("isUrl"));

            taskMessage.setPin(messageObject.getBoolean("isPin"));
            taskMessage.setText(messageObject.isNull("text") ? null : messageObject.getString("text"));
            taskMessage.setFileExist(!messageObject.isNull("file"));
            taskMessageList.add(taskMessage);
        }
        return taskMessageList;
    }

    public static void taskMessageAction(TaskMessage taskMessage, String action) {
        String path = URL + "/task/" + taskMessage.getTaskId() + "/message/" + taskMessage.getTaskMessageId() + "/action/" + action;
        System.out.println(path);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .post(path);

        String responseBody = response.getBody().asString();

        System.out.println(ADD_RESPONSE + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static void taskMessageAssert(TaskMessage taskMessage) {
        List<TaskMessage> taskMessageList = taskMessageGet(true);
        Boolean isAssert = false;
        SoftAssert softAssert = new SoftAssert();
        for (TaskMessage taskMessageGet : taskMessageList) {
            if (taskMessageGet.getTaskMessageId() == taskMessageId) {
                softAssert.assertEquals(taskMessageGet.getType(), taskMessage.getType(), "Type");
                softAssert.assertEquals(taskMessageGet.getAuthorId(), taskMessage.getAuthorId(), "AuthorId");
                softAssert.assertEquals(taskMessageGet.getReplyId(), taskMessage.getReplyId(), "ReplyId");
                softAssert.assertEquals(taskMessageGet.getText(), taskMessage.getText(), "Text");

                softAssert.assertEquals(taskMessageGet.getPin(), taskMessage.getPin(), "Pin");
                softAssert.assertEquals(taskMessageGet.getUpdatedConditions(), taskMessage.getUpdatedConditions(), "UpdatedConditions");
                softAssert.assertEquals(taskMessageGet.getFeedback(), taskMessage.getFeedback(), "Feedback");
                softAssert.assertEquals(taskMessageGet.getFileExist(), taskMessage.getFileExist(), "FileExist");
                isAssert = true;
            }
        }
        softAssert.assertAll();
        Assert.assertTrue(isAssert);
    }
}