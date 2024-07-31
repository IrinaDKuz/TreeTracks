package API.Advert;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static AdvertPackage.entity.AdvertPrimaryInfo.getArrayFromBDString;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Адвертов
 - getList, filters,
 */

// TODO: реализовать requisites, contact(messengers),

public class AdvertFilterAPI {

    public final static Map<String, String> generalAdvertFields = new HashMap<>() {{
        put("sales_manager", "salesManager[]");
        put("account_manager", "accountManager[]");
        put("site_url", "siteUrl");
        put("geo", "geo[]");
        put("pricing_model", "pricingModel[]");
        put("status", "status[]");
        put("note", "note");
        put("manager_id", "managerId[]");
        put("company_legalname", "companyLegalname");
        put("user_request_source_value", "userRequestSourceValue");
        put("user_request_source_id", "userRequestSource[]");
    }};
    public final static Map<String, String> tagAdvertFields = new HashMap<>() {{
        put("advert_tag_id", "tag[]");
    }};
    public final static Map<String, String> categoryAdvertFields = new HashMap<>() {{
        put("category_id", "categories[]");
    }};
    public final static Map<String, String> paymentAdvertFields = new HashMap<>() {{
        put("payment_system_id", "paymentType[]");
        // put("requisites", "paymentDetails[]"); // пока вручную
    }};
    public final static Map<String, String> contactAdvertFields = new HashMap<>() {{
        put("person", "person");
        put("email", "contact"); // добавить сюда messenger
    }};
    public final static Map<String, String> messengerAdvertFields = new HashMap<>() {{
        put("value", "contact");
    }};

    @Test
    public static void test() throws Exception {
        Allure.description("Проверка работы фильтров");
        for (Map.Entry<String, String> entry : generalAdvertFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "advert", entry.getKey());
            filterAdverts(entry, value);
        }
        for (Map.Entry<String, String> entry : tagAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_tag_relation");
            System.out.println(value);
            filterAdverts(entry, value, "advert_tag_relation", "advert_id");
        }
        for (Map.Entry<String, String> entry : categoryAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_category");
            System.out.println(value);
            filterAdverts(entry, value, "advert_category", "advert_id");
        }
        for (Map.Entry<String, String> entry : paymentAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_payment");
            System.out.println(value);
            filterAdverts(entry, value, "advert_payment", "advert_id");
        }
        for (Map.Entry<String, String> entry : contactAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_contact");
            System.out.println(value);
            filterAdverts(entry, value, "advert_contact", "advert_id");
        }
        for (Map.Entry<String, String> entry : messengerAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_contact_messenger");
            System.out.println(value);
            // пока только вручную
            // filterAdverts(entry, value, "advert_contact", "advert_id");
        }
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, String tableName, String idRowName) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere(idRowName, tableName, entry.getKey(), valueString));
        filterAdvertsPost(entry.getValue(), valueString, ids);
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString) throws Exception {
        Object value;
        Set<String> ids = new TreeSet<>(); // Set для хранения уникальных значений
        if (entry.getKey().equals("geo") || entry.getKey().equals("pricing_model")) {
            List<String> valueList = getArrayFromBDString(valueString);
            for (Object val : valueList) {
                ids.addAll(getArrayFromBDWhereIsLike("id", "advert", entry.getKey(), val.toString()));
            }
            value = valueList;
        } else {
            value = valueString;
            ids.addAll(getArrayFromBDWhere("id", "advert", entry.getKey(), (String) value));
        }
        filterAdvertsPost(entry.getValue(), value, ids);
    }

    private static void filterAdvertsPost(String paramName, Object paramValue, Set<String> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);

        System.out.println("paramName = " + paramName);
        System.out.println("paramValue = " + paramValue);
        System.out.println("advertIds = " + ids);
        Allure.step("Поверка " + paramName + "=" + paramValue);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(getUrlWithParameters("https://api.admin.3tracks.link/advert?", params));

        String responseBody = response.getBody().asString();
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
        ids = removeDeletedAdverts(ids);
        System.out.println(filterIdList);
        System.out.println(ids);
        Allure.step("AdvertId из фильтра: " + filterIdList);
        Allure.step("AdvertId из базы: " + ids);
        Assert.assertEquals(filterIdList, ids);
    }

    private static Set<String> removeDeletedAdverts(Set<String> ids) {
        ids.removeIf(id -> !isInDatabase("id", id, "advert"));
        return ids;
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
        return url;
    }
}