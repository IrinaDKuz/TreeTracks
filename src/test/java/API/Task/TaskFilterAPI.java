package API.Task;


import static org.hamcrest.Matchers.*;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Tasks
 - filters,
 */
// TODO: 50% - НЕТ ПРОВЕРОК ПО ДАТАМ


public class TaskFilterAPI {

    public final static Map<String, String> generalTaskFields = new HashMap<>() {{
        put("assigne_id", "assigne[]");
        put("requester_id", "requester[]");
        put("status", "status[]");
        put("type", "type[]");
        put("advert_id", "advert[]");
        put("offer_id", "offer[]");
        put("affiliate_id", "affiliate[]");
    }};

    public final static Map<String, String> watcherTaskFields = new HashMap<>() {{
        put("admin_id", "watcher[]");
    }};




    @Test
    public static void test() throws Exception {
        Allure.description("Проверка работы фильтров");
        SoftAssert softAssert = new SoftAssert();
        for (Map.Entry<String, String> entry : generalTaskFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "task", entry.getKey());
            filterAdverts(entry, value, softAssert);
        }
        for (Map.Entry<String, String> entry : watcherTaskFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "task_watcher");
            System.out.println(value);
            filterAdverts(entry, value, "task_watcher", "task_id", softAssert);
        }

        softAssert.assertAll();
    }


    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, String tableName, String idRowName, SoftAssert softAssert) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere(idRowName, tableName, entry.getKey(), valueString));
        List<String> filterIds = filterTasksGet(entry.getValue(), valueString);
        filterAssert(filterIds, ids, softAssert);
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, SoftAssert softAssert) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere("id", "task", entry.getKey(), valueString));
        List<String> filterIds = filterTasksGet(entry.getValue(), valueString);
        filterAssert(filterIds, ids, softAssert);
    }

    private static List<String> filterTasksGet(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);

        System.out.println("paramName = " + paramName);
        System.out.println("paramValue = " + paramValue);
        Allure.step("Поверка " + paramName + "=" + paramValue);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(getUrlWithParameters("https://api.admin.3tracks.link/task?", params));

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataArray = jsonObject.getJSONObject("data");
        JSONArray adverts = dataArray.getJSONArray("tasks");

        List<String> filterIdList = new ArrayList<>();
        for (int i = 0; i < adverts.length(); i++) {
            JSONObject dataObject = adverts.getJSONObject(i);
            filterIdList.add(String.valueOf(dataObject.getInt("id")));
        }
        Collections.sort(filterIdList);

        validateFields(response);
        return filterIdList;
    }

    private static void filterAssert(List<String> filterIdList, Set<String> ids, SoftAssert softAssert) throws Exception {
        Set<String> actualIds = removeDeletedTasks(ids);
        System.out.println("TasksIds = " + actualIds);
        System.out.println("filterTasksIds = " + filterIdList);
        Allure.step("AdvertId из фильтра: " + filterIdList);
        Allure.step("AdvertId из базы: " + actualIds);
        softAssert.assertEquals(filterIdList, actualIds);
    }

    private static Set<String> removeDeletedTasks(Set<String> ids) throws Exception {
        Set<String> toRemove = new HashSet<>();
        for (String id : ids) {
              if (!getValueFromBDWhere("deleted_at", "task", "id", id).equals("null")) {
                toRemove.add(id);
            }
        }

        ids.removeAll(toRemove);
        return ids;
    }

    // Метод для проверки наличия полей
    public static void validateFields(Response response) {
        response.then().body("success", notNullValue())
                .body("data", notNullValue())
                .body("data.tasks", notNullValue())
                .body("data.tasks[0]", hasKey("updatedAt")) // Поле может быть null, если оно ожидается в данных
                .body("data.tasks[0].dueDate", notNullValue())
                .body("data.tasks[0].status", notNullValue())
                .body("data.tasks[0].type", notNullValue())
                .body("data.tasks[0].assigneId", notNullValue())
                .body("data.tasks[0].requesterId", notNullValue())
                .body("data.tasks[0].id", notNullValue())
                .body("data.tasks[0].watcher", notNullValue())
                .body("data.tasks[0].tags", notNullValue())
                .body("data.tasks[0]", hasKey("affiliateId"))
                .body("data.tasks[0]", hasKey("advert")) // может быть null
                .body("data.tasks[0]", hasKey("offer"))  //  может быть null
                .body("pagination", notNullValue())
                .body("pagination.page", notNullValue())
                .body("pagination.limit", notNullValue())
                .body("pagination.totalCount", notNullValue());
    }



    public static String getUrlWithParameters(String url, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> listValue = (List<?>) value;
                for (Object item : listValue) {
                    url += key + "=" + item.toString() + "&";
                }
            } else {
                url += key + "=" + value.toString() + "&";
            }
        }
        url = url.substring(0, url.length() - 1);
        System.out.println(url);
        Allure.step("url: " + url);
        return url;
    }
}