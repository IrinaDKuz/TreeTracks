package API.Advert;

import AdvertPackage.entity.AdvertNotes;
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
 Тест проверяет работу API методов Адвертов
 - getList, filters,
 */

// TODO: реализовать geo, Categories, Payment Type, Contact,
//  User Request Source, User Request Source Value, Tag, Person

public class AdvertListAPI {

    public final static Map<String, String> generalAdvertFields = new HashMap<>() {{
        put("manager_id", "managerId[]");
        put("sales_manager", "salesManager[]");
        put("account_manager", "accountManager[]");
        put("site_url", "siteUrl");
        //put("geo", "geo[]"); // гео пока вручную
        put("pricing_model", "pricingModel[]");
        put("note", "note");
        put("company_legalname", "companyLegalname");
        put("status", "status[]");
    }};


    @Test
    public static void test() throws Exception {

        for (Map.Entry<String, String> entry : generalAdvertFields.entrySet()) {
            String id = getRandomValueFromBDWhereNotNull("id", "advert", entry.getKey());
            String value = getValueFromBDWhere(entry.getKey(), "advert", "id", id);
            List<String> ids = getArrayFromBDWhere("id", "advert", entry.getKey(), value);
            filterAdverts(entry.getValue(), value, ids);
        }
    }

    private static void filterAdverts(String paramName, String paramValue, List<String> ids) {
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
                .get("https://api.admin.3tracks.link/advert");

        String responseBody = response.getBody().asString();
      //  System.out.println("Ответ на get: " + responseBody);

        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject dataArray = jsonObject.getJSONObject("data");
        JSONArray adverts = dataArray.getJSONArray("adverts");

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
