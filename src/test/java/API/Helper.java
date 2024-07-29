package API;

import OfferDraftPackage.entity.OfferBasicInfo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Helper.Auth.authKeyAdmin;

public class Helper {



    public static final String[] LANG = {"eng", "rus"};

    public static void deleteMethod(String url, String id) {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/" + url + "/" + id );

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на delete: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
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
        }
        else return null;
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
}
