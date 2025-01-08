package API.Advert;

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

import static API.Helper.getRandomValueFromJson;
import static AdvertPackage.entity.AdvertPrimaryInfo.getArrayFromBDString;
import static Helper.Auth.*;
import static Helper.GeoAndLang.getSomeRandomGeoKeys;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Адвертов
 - filters,
 // TODO: реализовать requisites,
 */


public class AdvertFilterAPI {
    static int userId;

    public final static Map<String, String> generalAdvertFields = new HashMap<>() {{
        put("id", "id");
        put("name", "name");
        put("sales_manager", "salesManager[]");
        put("account_manager", "accountManager[]");
        put("site_url", "siteUrl");
        put("geo", "geo[]");
        put("pricing_model", "pricingModel[]");
        put("status", "status[]");
        put("note", "note");
        put("company_legalname", "companyLegalname");
        put("manager_id", "managerId[]");
        put("user_request_source", "userRequestSource[]");
    }};

    public final static Map<String, String> tagAdvertFields = new HashMap<>() {{
        put("advert_tag_id", "tag[]");
    }};
    public final static Map<String, String> categoryAdvertFields = new HashMap<>() {{
        put("category_id", "categories[]");
    }};
    public final static Map<String, String> paymentAdvertFields = new HashMap<>() {{
        put("payment_system_id", "paymentType[]");
        put("requisites", "paymentDetails");
    }};
    public final static Map<String, String> contactAdvertFields = new HashMap<>() {{
        put("person", "person");
        put("email", "contact");
    }};
    public final static Map<String, String> messengerAdvertFields = new HashMap<>() {{
        put("id", "contact");
    }};

    @Test
    public static void test() throws Exception {
        userId = getRandomUserId();
        authApi(userId);
        Allure.description("Проверка работы фильтров");
        SoftAssert softAssert = new SoftAssert();

        for (Map.Entry<String, String> entry : generalAdvertFields.entrySet()) {
            String value;
            if (entry.getKey().contains("anager"))
                value = String.valueOf(getSomeValuesFromBD("id", "admin", new Random().nextInt(5) + 1));
            else if (entry.getKey().equals("geo"))
                value = String.valueOf(getSomeRandomGeoKeys(new Random().nextInt(5) + 1));
            else
                value = getRandomValueFromBDWhereNotNull(entry.getKey(), "advert", entry.getKey());
            filterAdverts(entry, value, softAssert);
            filterAdverts(entry, "null", softAssert);

        }
        for (Map.Entry<String, String> entry : tagAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_tag_relation");
            System.out.println(value);
            filterAdverts(entry, value, "advert_tag_relation", "advert_id", softAssert);
        }
        for (Map.Entry<String, String> entry : categoryAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_category");
            System.out.println(value);
            filterAdverts(entry, value, "advert_category", "advert_id", softAssert);
        }
        for (Map.Entry<String, String> entry : paymentAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_payment");
            if (entry.getKey().equals("requisites")) value = getRandomValueFromJson(value);
            System.out.println(value);
            filterAdverts(entry, value, "advert_payment", "advert_id", softAssert);
        }
        for (Map.Entry<String, String> entry : contactAdvertFields.entrySet()) {
            String value = getRandomValueFromBD(entry.getKey(), "advert_contact");
            System.out.println(value);
            filterAdverts(entry, value, "advert_contact", "advert_id", softAssert);
        }
        for (Map.Entry<String, String> entry : messengerAdvertFields.entrySet()) {
            String value = getRandomValueFromBD("value", "advert_contact_messenger");
            List<String> contactsID = getArrayFromBDWhere("contact_id", "advert_contact_messenger", "value", value);
            filterAdverts(entry, value, contactsID, "advert_id", softAssert);
        }
        softAssert.assertAll();
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, List<String> contactsID, String idRowName, SoftAssert softAssert) throws Exception {
        Set<String> ids = new TreeSet<>();
        ids.addAll(getArrayFromBDWhere(idRowName, "advert_contact", entry.getKey(), contactsID));
        List<String> filterIds = filterAdvertsPost(entry.getValue(), valueString);
        filterAssert(filterIds, ids, softAssert, entry.getValue());
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, String tableName, String idRowName, SoftAssert softAssert) throws Exception {
        Set<String> ids = new TreeSet<>();
        if (entry.getKey().equals("requisites")) {
            ids.addAll(getArrayFromBDWhereIsLike(idRowName, tableName, entry.getKey(), valueString));
        } else {
            ids.addAll(getArrayFromBDWhere(idRowName, tableName, entry.getKey(), valueString));
        }
        List<String> filterIds = filterAdvertsPost(entry.getValue(), valueString);
        filterAssert(filterIds, ids, softAssert, entry.getValue());
    }

    private static void filterAdverts(Map.Entry<String, String> entry, String valueString, SoftAssert softAssert) throws Exception {
        Object value;
        Set<String> ids = new TreeSet<>(); // Set для хранения уникальных значений
        if (!valueString.equals("null")) {
            if (entry.getKey().equals("geo") || entry.getKey().equals("pricing_model") || entry.getKey().contains("anager")) {
                List<String> valueList = getArrayFromBDString(valueString);
                for (Object val : valueList) {
                    System.out.println(entry.getKey() + " БД");
                    System.out.println(val.toString() + " БД value");
                    if (!entry.getKey().contains("anager"))
                        ids.addAll(getArrayFromBDWhereIsLike("id", "advert", entry.getKey(), val.toString()));
                    else
                        ids.addAll(getArrayFromBDWhere("id", "advert", entry.getKey(), val.toString()));

                }
                value = valueList;
            } else {
                value = valueString;
                ids.addAll(getArrayFromBDWhere("id", "advert", entry.getKey(), (String) value));
            }
        } else {
            value = "null";
            ids.addAll(getArrayFromBDWhereNull("id", "advert", entry.getKey()));
        }

        List<String> filterIds = filterAdvertsPost(entry.getValue(), value);
        filterAssert(filterIds, ids, softAssert, entry.getValue());
    }

    private static List<String> filterAdvertsPost(String paramName, Object paramValue) throws InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2000);
        params.put(paramName, paramValue);

        System.err.println("paramName = " + paramName);
        System.err.println("paramValue = " + paramValue);
        Allure.step("Поверка " + paramName + "=" + paramValue);
        Thread.sleep(5000);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(getUrlWithParameters("https://api.admin.3tracks.link/advert?", params));

        String responseBody = response.getBody().asString();
        // System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("adverts");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            Collections.sort(filterIdList);
            return filterIdList;
        } else {
            System.err.println("Параметр " + paramName + " нельзя null");
            return new ArrayList<>();
        }

    }

    private static void filterAssert(List<String> filterIdList, Set<String> ids, SoftAssert softAssert, String name) {
        removeDeletedAdverts(ids);
        System.out.println("advertIds из базы = " + ids);
        System.out.println("filterAdvertIds из фильтра = " + filterIdList);
        Allure.step("AdvertId из фильтра: " + filterIdList);
        Allure.step("AdvertId из базы: " + ids);
        softAssert.assertEquals(filterIdList, ids, "Проверка " + name + "не успешна");
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
        Allure.step("url: " + url);
        return url;
    }
}