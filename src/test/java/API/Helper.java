package API;

import OfferDraftPackage.entity.OfferBasicInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static Helper.AllureHelper.DELETE_RESPONSE;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.isInDatabase;

public class Helper {

    public static final String[] LANG = {"eng", "rus"};

   // public static final String URL = "https://api.newx.3tracks.online";
    public static final String URL = "https://api.admin.3tracks.link";


    public static void deleteMethod(String url, String id) {
        Response response;
        System.out.println("https://api.admin.3tracks.link/" + url + "/" + id);
        response = RestAssured.given().contentType(ContentType.URLENC).header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/" + url + "/" + id);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println(DELETE_RESPONSE + responseBody);
        Allure.step(DELETE_RESPONSE + responseBody);

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
    }

    public static List<Integer> getArrayFromJson(JSONObject data, String parameterName) {
        if (data.get(parameterName) instanceof JSONArray) {
            JSONArray array = data.getJSONArray(parameterName);
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                int value = array.getInt(i);
                list.add(value);
            }
            return list;
        } else return null;
    }

    public static List<String> getStringArrayFromJson(JSONObject data, String parameterName) {
        if (data.get(parameterName) instanceof JSONArray) {
            JSONArray array = data.getJSONArray(parameterName);
            List<String> listArray = StreamSupport.stream(array.spliterator(), false)
                    .map(Object::toString)
                    .toList();
            return listArray;
        } else return null;
    }

    public static OfferBasicInfo.OfferTemplate getTemplateFromJson(JSONObject data, String parameterName) {
        JSONArray array = data.getJSONArray(parameterName);
        OfferBasicInfo.OfferTemplate template = null;
        if (array != null) {
            Map<String, String> templateMap = null;
            templateMap = new HashMap<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                templateMap.put(jsonObject.getString("lang"), jsonObject.getString("text"));
            }
            template = new OfferBasicInfo.OfferTemplate();
            template.setTemplateMap(templateMap);
        }
        return template;
    }


    public static Integer parseUnknownValueToInteger(JSONObject jsonObject, String parameterName) {
        Object activeValue = jsonObject.get(parameterName);
        if (activeValue instanceof Integer) {
            return (Integer) activeValue;
        } else if (activeValue instanceof String) {
            return Integer.valueOf((String) activeValue);
        } else {
            throw new IllegalArgumentException("Unexpected type for " + parameterName);
        }
    }

    public static void assertDelete(String id, String tableName) {
        boolean isDelete = !isInDatabase("id", id, tableName);
        if (isDelete) {
            Allure.step("id=" + id + " не найден в БД " + tableName);
            System.out.println("id=" + id + " не найден в БД " + tableName);
        }
        else {
            Allure.step("id=" + id + " найден в БД " + tableName);
            System.out.println("id=" + id + " найден в БД " + tableName);
        }
        Assert.assertTrue(isDelete);
    }


    public static String getRandomValueFromJson(String jsonString) throws JsonProcessingException {
        // Создаем ObjectMapper для парсинга JSON-строки
        ObjectMapper mapper = new ObjectMapper();
        // Парсим JSON-строку в JsonNode
        JsonNode rootNode = mapper.readTree(jsonString);
        List<String> values = new ArrayList<>();
        rootNode.fields().forEachRemaining(entry -> values.add(entry.getValue().asText()));
        if (values.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(values.size());
        return values.get(randomIndex);
    }

    public static List<String> filterExistAdvertString(List<String> list) {
        //  удалили повторы
        List<Integer> listInt = list.stream()
                .map(Integer::valueOf).distinct().toList();

        // удалили тех, что нет в базе
        List<String> newList = new ArrayList<>();
        for (Integer advertId : listInt) {
            if (isInDatabase("id", String.valueOf(advertId), "advert")) {
                newList.add(String.valueOf(advertId));
            }
        }
        return newList;
    }


    public static List<Integer> filterExistAdvert(List<Integer> list) {
        //  удалили повторы
        List<Integer> listInt = list.stream().distinct().toList();

        // удалили тех, что нет в базе
        List<Integer> newList = new ArrayList<>();
        for (Integer advertId : listInt) {
            if (isInDatabase("id", String.valueOf(advertId), "advert")) {
                newList.add(advertId);
            }
        }
        return newList;
    }

    public static List<Integer> sortToInteger(List<String> list) {
        //  удалили повторы отсортировали
        return list.stream().map(Integer::valueOf).distinct().sorted().toList();
    }


    public static List<String> sortToString(List<String> list) {
        List<Integer> listInt = list.stream().map(Integer::valueOf).distinct().sorted().toList();
        return listInt.stream().map(String::valueOf).toList();
    }

}
