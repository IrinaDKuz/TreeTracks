package API.OfferDraft;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Админов
 - getList, filters,
 */

// TODO: реализовать Messengers

public class OfferDraftList {
    static List<String> contactMethods = Arrays.asList("phone", "telegram", "skype");

    public final static Map<String, String> adminFields = new HashMap<>() {{
        put("status", "status");
        put("email", "email");
        put("first_name", "firstName");
        put("second_name", "secondName");
       // put(contactMethods.get(new Random().nextInt(contactMethods.size())), "messengers");
    }};

    @Test
    public static void test() throws Exception {

        for (Map.Entry<String, String> entry : adminFields.entrySet()) {
            String id = getRandomValueFromBDWhereNotNull("id", "admin", entry.getKey());
            String value = getValueFromBDWhere(entry.getKey(), "admin", "id", id);
            List<String> ids = getArrayFromBDWhere("id", "admin", entry.getKey(), value);
            filterAdmins(entry.getValue(), value, ids);
        }
    }

    private static void filterAdmins(String paramName, String paramValue, List<String> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);
        System.out.println("paramName = " + paramName);
        System.out.println("paramValue = " + paramValue);
        System.out.println("advertIds = " + ids);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .params(params)
                .get("https://api.admin.3tracks.link/admin");

        String responseBody = response.getBody().asString();

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataArray = jsonObject.getJSONObject("data");
        JSONArray adverts = dataArray.getJSONArray("admin");

        List<String> filterIdList = new ArrayList<>();
        for (int i = 0; i < adverts.length(); i++) {
            JSONObject dataObject = adverts.getJSONObject(i);
            filterIdList.add(String.valueOf(dataObject.getInt("id")));
        }

        Collections.sort(filterIdList);
        Collections.sort(ids);
        Assertions.assertThat(filterIdList).isEqualTo(ids);
    }
}
