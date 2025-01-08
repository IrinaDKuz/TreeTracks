package API.Admin.ContentFilter;

import AdminPackage.entity.AdminContentFilterForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static API.Helper.*;
import static Helper.AllureHelper.DELETE;
import static Helper.AllureHelper.GET_RESPONSE;
import static Helper.Auth.*;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест проверяет работу Task Content Filter
 Если пользователь является Assignee\Requester\Watcher в таске - то он видит таску в любом случае
 Если пользователь НЕ является Assignee\Requester\Watcher в таске - то он видит таску только в случае
 если он есть в фильтре include и его нет в фильтре exclude

 TODO: 0% DONE
 */

public class ContentFilterTaskMany {

    static List<AdminContentFilterForTesting> filterIncludeList = new ArrayList<>();
    static List<AdminContentFilterForTesting> filterExcludeList = new ArrayList<>();
    static Integer userId;



    @Test
    public static void test() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " !!!!!!!!");
            testPrepareData();
            testSeparateInclude();
            testSeveral();
            testAllInclude();
            test1();
            test2();
            testDeleteData();
        }
    }

    public static void testPrepareData() throws Exception {
        userId = getRandomUserId();
        authApi(userId);
        // System.out.println(" ");
        // System.out.println("0) Заполнение массива данных для дальнейших проверок");
        prepareData();
    }


    public static void testSeparateInclude() throws Exception {
        // System.out.println(" ");
        // System.out.println("1) Тестирование по отдельности include");
        for (AdminContentFilterForTesting filler1 : filterIncludeList) {
            if (filler1.getInclude())
                testFieldCombination(List.of(filler1));
        }
    }

    public static void testSeveral() throws Exception {
        // System.out.println(" ");
        // System.out.println("2) Несколько include - несколько exclude");
        List<AdminContentFilterForTesting> list = getRandomFilter(filterIncludeList, filterExcludeList.size() - 1);
        list.addAll(getRandomFilter(filterExcludeList, filterExcludeList.size() - 1));
        testFieldCombination(list);
    }

    public static void testAllInclude() throws Exception {
        // System.out.println(" ");
        // System.out.println("3) Тестирование конфигураций include + include все");
        testFieldCombination(filterIncludeList);
    }

    public static void test1() throws Exception {
        // System.out.println(" ");
        // System.out.println("4) Тестирование конфигураций по всем include - exclude");
        for (int i = 0; i < filterIncludeList.size(); i++) {
            int n = new Random().nextInt(filterExcludeList.size());

            testFieldCombination(List.of(filterIncludeList.get(i), filterExcludeList.get(n)));
        }
    }

    public static void test2() throws Exception {
        // 5) Тестирование конфигураций include - по всем exclude
        // System.out.println(" ");
        // System.out.println("5) Тестирование конфигураций include - по всем exclude");
        for (int i = 0; i < filterExcludeList.size(); i++) {
            int n = new Random().nextInt(filterIncludeList.size());
            testFieldCombination(List.of(filterExcludeList.get(i), filterIncludeList.get(n)));
        }
    }

    public static void testDeleteData() throws Exception {
        Allure.step(DELETE + " content-filter/task ");
        String id = getValueFromBDWhere("id", "content_filter",
                Map.of("type", "task", "admin_id", userId.toString()));
        deleteMethod("admin/" + userId + "/content-filter", "task");
        assertDelete(id, "content_filter");
        filterExcludeList = new ArrayList<>();
        filterIncludeList = new ArrayList<>();
    }


    public static void prepareData() throws Exception {

        filterIncludeList.add(contentFilterAdmins(true, "assigne_id", "assigneeInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "assigne_id", "assigneeExclude"));

        filterIncludeList.add(contentFilterAdmins(true, "requester_id", "requesterInclude"));
        filterExcludeList.add(contentFilterAdmins(false, "requester_id", "requesterExclude"));


        List<String> filterValue = getSomeValuesFromBD("type", "task", new Random().nextInt(3) + 1);

        filterIncludeList.add(contentFilterOther(true, filterValue, "id", "task",
                "type", "taskTypeInclude"));

        filterValue = getSomeValuesFromBD("type", "task", new Random().nextInt(3) + 2);
        filterExcludeList.add(contentFilterOther(false, filterValue, "id", "task",
                "type", "taskTypeExclude"));

        filterValue = getSomeValuesFromBD("id", "admin", new Random().nextInt(3) + 2);
        filterIncludeList.add(contentFilterOther(true, filterValue, "task_id", "task_watcher",
                "admin_id", "watcherInclude"));

        filterValue = getSomeValuesFromBD("id", "admin", new Random().nextInt(3) + 1);
        filterExcludeList.add(contentFilterOther(false, filterValue, "task_id", "task_watcher",
                "admin_id", "watcherExclude"));
    }

    public static AdminContentFilterForTesting contentFilterAdmins(boolean isInclude, String bdName, String filterName) throws Exception {
        List<String> filterValue = getSomeValuesFromBD("id", "admin", new Random().nextInt(4) + 1);


        List<String> uniqueValues = filterValue.stream()
                .distinct()
                .collect(Collectors.toList());

        if (!isInclude){
            // Удалить значения, которые в том же фильтре, но include
            uniqueValues.removeAll(filterIncludeList.getLast().getFilterValue());
        }

        List<Integer> expectedIds = new ArrayList<>(sortToInteger(getArrayFromBDWhere("id", "task", bdName, uniqueValues)));

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, uniqueValues, expectedIds);
        return filter;
    }

    public static AdminContentFilterForTesting contentFilterOther(boolean isInclude, List<String> filterValue, String bdName,
                                                                  String tableName, String whereName, String filterName) throws Exception {

        List<String> uniqueValues = filterValue.stream()
                .distinct()
                .collect(Collectors.toList());

        if (!isInclude){
            // Удалить значения, которые в том же фильтре, но include
            System.out.println("значения которые удалим " + filterIncludeList.getLast().getFilterValue());
            System.out.println("значения из которых удвляем " + uniqueValues);

            uniqueValues.removeAll(filterIncludeList.getLast().getFilterValue());
        }

        // // System.out.println("uniqueValues - " + uniqueValues);
        List<Integer> expectedIds = sortToInteger(getArrayFromBDWhere(bdName, tableName, whereName, uniqueValues));

       // System.out.println("isInclude - " + isInclude);
       // System.out.println("expectedIds - " + expectedIds);

        AdminContentFilterForTesting filter = new AdminContentFilterForTesting(isInclude, filterName, uniqueValues, expectedIds);
        return filter;
    }

    public static void testFieldCombination(List<AdminContentFilterForTesting> contentFilters) throws Exception {
        Allure.step("Тестирование полей: ");
        System.err.println("Тестирование полей: ");
        for (AdminContentFilterForTesting filter : contentFilters) {
            Allure.step(filter.getFilterName() + " " + filter.getFilterValue());
            System.err.println(filter.getFilterName());
            System.out.println(filter.getFilterValue());
        }
        // Вызов метода для установки значений фильтра
        contentFilter(contentFilters);

        int maxAttempts = 3; // максимальное количество попыток
        int attempts = 0;
        boolean success = false;

        while (attempts < maxAttempts && !success) {
            try {
                List<Integer> actualIds = new ArrayList<>();
                List<Integer> expectedIds = new ArrayList<>();

                List<Integer> toAdd = new ArrayList<>();
                List<Integer> toRemove = new ArrayList<>();

                for (AdminContentFilterForTesting filter : contentFilters) {
                    List<Integer> filterIds = filter.getExpectedIds().stream().distinct().toList();

                    if (filter.getFilterName().contains("Include"))
                        toAdd.addAll(filterIds);
                    if (filter.getFilterName().contains("Exclude"))
                        toRemove.addAll(filterIds);
                }

                // После завершения цикла выполняем добавление и удаление

                expectedIds.addAll(toAdd);
                Collections.sort(expectedIds);
                // System.out.println("Add filters !!!!!!!" + expectedIds);
                expectedIds.removeAll(toRemove);
                Collections.sort(expectedIds);
                // System.out.println("Delete filters !!!!!!!" + expectedIds);

                // Таски где пользователь Assignee\Requester\Watcher
                List<Integer> specialToAdd = new ArrayList<>();
                specialToAdd.addAll(
                        getArrayFromBDWhere("id", "task", "assigne_id", userId.toString())
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));

                Collections.sort(expectedIds);
                // System.out.println("assigne !!!!!!!" + expectedIds);




                specialToAdd.addAll(
                        getArrayFromBDWhere("id", "task", "requester_id", userId.toString())
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));

                Collections.sort(expectedIds);
                // System.out.println("requester_id !!!!!!!" + expectedIds);


                specialToAdd.addAll(
                        getArrayFromBDWhere("task_id", "task_watcher", "admin_id", userId.toString())
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));

                Collections.sort(expectedIds);
                // System.out.println("task_watcher !!!!!!!" + expectedIds);


                expectedIds.addAll(specialToAdd);

                // Удаляем те, которые были удалены
                expectedIds.removeAll(
                        getArrayFromBDWhereNotNull("id", "task", "deleted_at")
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));


                Collections.sort(expectedIds);
                // System.out.println("deleted_at !!!!!!!" + expectedIds);

                // Удаляем те, которые со статусом Cancelled и Resolved
                expectedIds.removeAll(
                        getArrayFromBDWhere("id", "task", "status", "cancelled")
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));


                Collections.sort(expectedIds);
                // System.out.println("cancelled !!!!!!!" + expectedIds);


                expectedIds.removeAll(
                        getArrayFromBDWhere("id", "task", "status", "resolved")
                                .stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()));

                Collections.sort(expectedIds);
                // System.out.println("resolved !!!!!!!" + expectedIds);

                // Запросим actualIds
                actualIds.addAll(taskListGet());

                // Все отсортируем и удалим повторы
                List<Integer> expectedIdsFilter = new ArrayList<>();
                List<Integer> sortedActualIds = new ArrayList<>(actualIds);

                if (!actualIds.isEmpty())
                    Collections.sort(sortedActualIds);

                if (!expectedIds.isEmpty()) {
                    expectedIdsFilter = new ArrayList<>(expectedIds.stream().distinct().toList());
                    Collections.sort(expectedIdsFilter);
                }

                 System.out.println("Из метода: " + sortedActualIds);
                 System.out.println("Из БД: " + expectedIdsFilter);

                List<Integer> differences = new ArrayList<>();

                List<Integer> finalExpectedIdsFilter = expectedIdsFilter;
                differences.addAll(sortedActualIds.stream()
                        .filter(element -> !finalExpectedIdsFilter.contains(element))
                        .collect(Collectors.toList()));

                differences.addAll(expectedIdsFilter.stream()
                        .filter(element -> !sortedActualIds.contains(element))
                        .collect(Collectors.toList()));

                System.out.println(differences);

                Allure.step("Из метода: " + sortedActualIds);
                Allure.step("Из БД: " + expectedIdsFilter);

                Assert.assertEquals(sortedActualIds, expectedIdsFilter);
                success = true;

                if (success) {
                    // System.out.println("Метод выполнен успешно.");
                    Allure.step("Метод выполнен успешно.");
                }


            } catch (AssertionError e) {
                System.err.println("Ошибка при выполнении метода: " + e.getMessage());
                Allure.step("Ошибка при выполнении метода: " + e.getMessage());
            }

            attempts++;
        }

        if (!success) {
            System.err.println("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            Allure.step("Метод не удалось выполнить после " + maxAttempts + " попыток.");
            Assert.fail();
        }


    }

    public static void contentFilter(List<AdminContentFilterForTesting> contentFilters) {
        JsonObject jsonObject = new JsonObject();

        for (AdminContentFilterForTesting filter : contentFilters) {
            JsonArray jsonArray = new JsonArray();
            filter.getFilterValue().forEach(jsonArray::add);  // Добавляем каждое значение в JsonArray
            jsonObject.add(filter.getFilterName(), jsonArray); // Добавляем ключ-значение в JsonObject
        }
        System.out.println(jsonObject);

        String path = "https://api.admin.3tracks.link/admin/" + userId + "/content-filter/task";

        // Отправка POST запроса
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", KEY)
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));

        path = "https://api.admin.3tracks.link/admin/" + userId + "/content-filter";
        response = given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(path);

        responseBody = response.getBody().asString();
        // System.out.println(GET_RESPONSE + responseBody);

    }


    public static List<Integer> taskListGet() throws SQLException {
        ArrayList<Integer> taskFromList = new ArrayList<>();

        int count = Integer.parseInt(getCountFromBD("task"));

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", KEY)
                .get(URL + "/task?page=1&limit=" + count + "/");

        String responseBody = response.getBody().asString();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray taskArray = data.getJSONArray("tasks");
        for (int i = 0; i < taskArray.length(); i++) {
            JSONObject taskObject = taskArray.getJSONObject(i);
            int id = taskObject.getInt("id");
            taskFromList.add(id);
        }

        return taskFromList;
    }


    public static List<AdminContentFilterForTesting> getRandomFilter(List<AdminContentFilterForTesting> list, int count) {
        List<AdminContentFilterForTesting> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList);
        return shuffledList.subList(0, new Random().nextInt(count) + 1);
    }


    public static void deleteData(String filterQuantity) {
        Response response = given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete(" https://api.admin.3tracks.link/admin/" + userId + "/content-filter/" + filterQuantity);

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }
}





