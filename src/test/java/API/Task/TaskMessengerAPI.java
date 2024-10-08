package API.Task;

import AdvertPackage.entity.AdvertNotes;
import AdvertPackage.entity.AdvertPaymentInfo;
import AdvertPackage.entity.AdvertRequisites;
import TaskPackage.entity.TaskMessage;
import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.List;

import static API.Helper.URL;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;
import static SQL.AdvertSQL.getRandomValueFromBDWhereNull;

/***
 Тест проверяет работу API методов
 - get, add, проверка
 во вкладке Task - "Message"
 TODO: taskMessageAssert
 */

public class TaskMessengerAPI {
    static int taskId;
    static int userId;
    static int taskMessageId;


    @Test
    public static void newMessageTest() throws Exception {
        userId = getRandomUserId();
        authApi(userId);

        taskId = Integer.parseInt(getRandomValueFromBDWhereNull("id", "task", "deleted_at"));

        Allure.step("Добавляем Task message isReply = false");
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.fillTaskMessageWithRandomData(taskId, userId);
        taskMessage.setReplyId(null);

        taskMessageAdd(taskMessage);
        taskMessage.setTaskId(taskId);
        taskMessage.setTaskMessageId(taskMessageId);

        Allure.step(CHECK);
        taskMessageAssert(taskMessage);

        Allure.step("Получаем FeedBack Task id=" + taskId);
        taskMessageGet(true);
    }

    @Test(dependsOnMethods = "newMessageTest", alwaysRun = true)
    public static void newReplyMessageTest() throws Exception {
        userId = getRandomUserId();
        authApi(userId);
        Allure.step("Добавляем Task message isReply = true");
        TaskMessage taskMessage2 = new TaskMessage();
        taskMessage2.fillTaskMessageWithRandomData(taskId, userId);

        taskMessageAdd(taskMessage2);
        taskMessage2.setTaskId(taskId);
        taskMessage2.setTaskMessageId(taskMessageId);

        Allure.step(CHECK);
        taskMessageAssert(taskMessage2);

        Allure.step("Получаем FeedBack Task id=" + taskId);
        taskMessageGet(true);
    }

    @Test(dependsOnMethods = "newReplyMessageTest", alwaysRun = true)
    public static void newPinMessageTest() throws Exception {
        userId = getRandomUserId();
        authApi(userId);
        Allure.step("Добавляем Task message isReply = false и pin");
        TaskMessage taskMessage3 = new TaskMessage();
        taskMessage3.fillTaskMessageWithRandomData(taskId, userId);
        taskMessage3.setReplyId(null);

        taskMessageAdd(taskMessage3);
        taskMessage3.setTaskId(taskId);
        taskMessage3.setTaskMessageId(taskMessageId);
        taskMessage3.setPin(true);
        taskMessagePin(taskMessage3);
        Allure.step(CHECK);
        taskMessageAssert(taskMessage3);
        Allure.step("Получаем FeedBack Task id=" + taskId);
        taskMessageGet(true);
    }

    @Test(dependsOnMethods = "newPinMessageTest", alwaysRun = true)
    public static void newFeedBackMessageTest() throws Exception {
        userId = getRandomUserId();
        authApi(userId);
        Allure.step("Добавляем Task message isReply = false и feedback");
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.fillTaskMessageWithRandomData(taskId, userId);
        taskMessage.setReplyId(null);

        taskMessageAdd(taskMessage);
        taskMessage.setTaskId(taskId);
        taskMessage.setTaskMessageId(taskMessageId);
        taskMessage.setFeedback(true);

        taskMessageFeedBack(taskMessage);
        Allure.step(CHECK);
        taskMessageAssert(taskMessage);
        Allure.step("Получаем FeedBack Task id=" + taskId);
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

    public static void taskMessageAdd(TaskMessage taskMessage) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonTaskMessageInfo(taskMessage), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = URL + "/task/" + taskId + "/add-message";
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
        JSONArray message = data.getJSONArray("message");

        for (int i = 0; i < message.length(); i++) {

            TaskMessage taskMessage = new TaskMessage();
            JSONObject messageObject = message.getJSONObject(i);

            taskMessage.setTaskMessageId(messageObject.getInt("id"));
            taskMessage.setType(messageObject.getString("type"));
            taskMessage.setAuthorId(messageObject.getInt("author"));
            taskMessage.setReplyId(messageObject.isNull("replyId") ? null : messageObject.getInt("replyId"));

            taskMessage.setDate(messageObject.getString("date"));
            taskMessage.setFeedback(messageObject.getBoolean("isFeedback"));
            taskMessage.setPin(messageObject.getBoolean("isPin"));
            taskMessage.setText(messageObject.isNull("text") ? null : messageObject.getString("text"));

            // taskMessage.setFile(messageObject.isNull("file")? null : messageObject.getString("text"));
            taskMessageList.add(taskMessage);
        }
        return taskMessageList;
    }

    public static void taskMessagePin(TaskMessage taskMessage) {
        // String path = URL + "/task/" + 12 + "/message/" + 166 + "/action/pin";

        String path = URL + "/task/" + taskMessage.getTaskId() + "/message/" + taskMessage.getTaskMessageId() + "/action/pin";
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

    public static void taskMessageFeedBack(TaskMessage taskMessage) {
        String path = URL + "/task/" + taskMessage.getTaskId() + "/message/" + taskMessage.getTaskMessageId() + "/action/feedback";
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
        List<TaskMessage> taskMessageList = taskMessageGet(false);
        Boolean isAssert = false;
        SoftAssert softAssert = new SoftAssert();
        for (TaskMessage taskMessageGet : taskMessageList) {
            if (taskMessageGet.getTaskMessageId() == taskMessageId) {
                softAssert.assertEquals(taskMessageGet.getType(), taskMessage.getType());
                softAssert.assertEquals(taskMessageGet.getAuthorId(), taskMessage.getAuthorId());
                softAssert.assertEquals(taskMessageGet.getReplyId(), taskMessage.getReplyId());
                softAssert.assertEquals(taskMessageGet.getText(), taskMessage.getText());
                isAssert = true;
            }
        }
        softAssert.assertAll();
        Assert.assertTrue(isAssert);
    }
}