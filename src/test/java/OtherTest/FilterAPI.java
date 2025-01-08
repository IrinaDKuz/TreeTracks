package OtherTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static Helper.Adverts.MODEL_TYPES_MAP;
import static Helper.Auth.KEY;
import static Helper.Auth.authApiNewX;
import static Helper.GeoAndLang.getRandomKey;

/***
 Записываем в файл все запросы
 без фильтров, с каждым из фильтров

 Отправляем запросы без фильтров
 Из ответов парсим id и записываем с список значения
 Берем ненулевые значения из списка и вставляем в фильтр
 если возьмем рандомные потом не воспроизведем, поэтому записываем значения так же в файл

 Записываем id в другой файл
 */

public class FilterAPI {

    static int userId = 4;

    static String offersUrl = "https://api.newx.3tracks.online/offer?page=1&limit=2000";
    static String advertisersUrl = "https://api.newx.3tracks.online/advert?page=1&limit=2000";
    static String affiliateUrl = "https://api.newx.3tracks.online/affiliate?page=1&limit=2000";
    static String adminsUrl = "https://api.newx.3tracks.online/admin/?page=1&limit=2000";
    static String tasksUrl = "https://api.newx.3tracks.online/task?page=1&limit=2000";


    // Названия всех 18 фильтров, которые хотим проверить + значение
    public final static Map<String, String> advertFilter = new HashMap<>() {{
        put("id", null);
        put("name", null);
        put("salesManager[]", null);
        put("accountManager[]", null);
        put("siteUrl", null);
        put("geo[]", null);
        put("categories[]", null);
        put("pricingModel[]", null);
        put("status[]", null);
        put("note", null);
        put("companyLegalname", null);
        put("managerId[]", null);
        put("paymentType[]", null);
        put("paymentDetails", null);
        put("contact", null);
        put("userRequestSource[]", null);
        put("tag[]", null);
        put("person", null);
    }};


    @Test
    public static void test() throws Exception {
        authApiNewX(userId);
        fileNew("fileWithUrl");
        fileNew("fileWithId");
        for (int i = 0; i < 10; i++) {
            postAdvertsUrl(advertisersUrl);
            postOffersUrl(offersUrl);
            postAffiliatesUrl(affiliateUrl);
            postAdminsUrl(adminsUrl);
            postTasksUrl(tasksUrl);
        }
    }

    private static void postAdvertsUrl(String url) {
        List<String> filterIdList = new ArrayList<>();

        fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("adverts");

            List<String> filterNameList = new ArrayList<>();
            List<String> filterManagerList = new ArrayList<>();
            List<String> filterAccountManagerList = new ArrayList<>();
            List<String> filterSalesManagerList = new ArrayList<>();
            List<String> filterLegalNameList = new ArrayList<>();
            List<String> filterStatusList = new ArrayList<>();
            List<String> filterNoteList = new ArrayList<>();
            List<String> filterUserRequestSourceList = new ArrayList<>();
            List<String> filterPaymentTypeList = new ArrayList<>();
            List<String> filterCategoryList = new ArrayList<>();
            List<String> filterGeoList = new ArrayList<>();
            List<String> filterTagList = new ArrayList<>();

            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));

                if (!dataObject.isNull("name"))
                    filterNameList.add(String.valueOf(dataObject.getString("name")));

                if (!dataObject.isNull("manager"))
                    filterManagerList.add(String.valueOf(dataObject.getInt("manager")));

                if (!dataObject.isNull("accountManager"))
                    filterAccountManagerList.add(String.valueOf(dataObject.getInt("accountManager")));

                if (!dataObject.isNull("salesManager"))
                    filterSalesManagerList.add(String.valueOf(dataObject.getInt("salesManager")));

                if (!dataObject.isNull("legalName"))
                    filterLegalNameList.add(String.valueOf(dataObject.getString("legalName")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));

                if (!dataObject.isNull("note"))
                    filterNoteList.add(String.valueOf(dataObject.getString("note")));

                if (!dataObject.isNull("userRequestSource"))
                    filterUserRequestSourceList.add(String.valueOf(dataObject.getInt("userRequestSource")));

                JSONArray paymentTypeArray = dataObject.getJSONArray("paymentType");
                if (!paymentTypeArray.isEmpty()) {
                    for (int j = 0; j < paymentTypeArray.length(); j++) {
                        filterPaymentTypeList.add(paymentTypeArray.getString(j));
                    }
                }

                JSONArray categoryArray = dataObject.getJSONArray("category");
                if (!categoryArray.isEmpty()) {
                    for (int j = 0; j < categoryArray.length(); j++) {
                        filterCategoryList.add(String.valueOf(categoryArray.getInt(j)));
                    }
                }

                JSONArray geoArray = dataObject.getJSONArray("geo");
                if (!geoArray.isEmpty()) {
                    for (int j = 0; j < geoArray.length(); j++) {
                        filterGeoList.add(geoArray.getString(j));
                    }
                }

                JSONArray tagArray = dataObject.getJSONArray("tag");
                if (!tagArray.isEmpty()) {
                    for (int j = 0; j < tagArray.length(); j++) {
                        filterTagList.add(String.valueOf(tagArray.getInt(j)));
                    }
                }
            }

            fileAdd("fileWithId", String.valueOf(filterIdList));
            postAdvertsUrlWithFilter(url + "&id=" + getRandomValue(filterIdList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&name=" + getRandomValue(filterNameList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&managerId[]=" + getRandomValue(filterManagerList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&accountManager[]=" + getRandomValue(filterAccountManagerList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&salesManager[]=" + getRandomValue(filterSalesManagerList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&companyLegalname=" + getRandomValue(filterLegalNameList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&status[]=" + getRandomValue(filterStatusList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&note=" + getRandomValue(filterNoteList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&userRequestSource[]=" + getRandomValue(filterUserRequestSourceList), "fileWithId", false);
            //  postAdvertsUrlWithFilter(url + "&paymentType[]=" + getRandomValue(filterPaymentTypeList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&pricingModel[]=" + getRandomKey(MODEL_TYPES_MAP), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&geo[]=" + getRandomValue(filterGeoList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&tag[]=" + getRandomValue(filterTagList), "fileWithId", false);
            postAdvertsUrlWithFilter(url + "&category[]=" + getRandomValue(filterCategoryList), "fileWithId", false);

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }

        // прогнать id по всем доп фильтрам "siteUrl", "paymentType[]", "paymentDetails", "contact", "person";
        postAdvertSiteUrl(filterIdList);
        postAdvertPaymentType(filterIdList);
        postAdvertPaymentDetails(filterIdList);
        postAdvertContact(filterIdList);
        postAdvertContactEmail(filterIdList);
        postAdvertContactPerson(filterIdList);

    }

    private static void postOffersUrl(String url) {
        List<String> filterIdList = new ArrayList<>();

        fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray offers = dataArray.getJSONArray("offers");

            List<String> filterTitleList = new ArrayList<>();
            List<String> filterStatusList = new ArrayList<>();
            List<String> filterPrivacyLevelList = new ArrayList<>();
            // List<String> filterGeoList = new ArrayList<>();
            List<String> filterCategoryList = new ArrayList<>();
            List<String> filterTagList = new ArrayList<>();
            List<String> filterAdvertList = new ArrayList<>();


            for (int i = 0; i < offers.length(); i++) {
                JSONObject dataObject = offers.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));

                if (!dataObject.isNull("title"))
                    filterTitleList.add(String.valueOf(dataObject.getString("title")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));

                if (!dataObject.isNull("privacyLevel"))
                    filterPrivacyLevelList.add(String.valueOf(dataObject.getString("privacyLevel")));

                JSONArray tagsArray = dataObject.getJSONArray("tags");
                if (!tagsArray.isEmpty()) {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        filterTagList.add(String.valueOf(tagsArray.getInt(j)));
                    }
                }

                JSONArray categoriesArray = dataObject.getJSONArray("categories");
                if (!tagsArray.isEmpty()) {
                    for (int j = 0; j < categoriesArray.length(); j++) {
                        filterCategoryList.add(String.valueOf(categoriesArray.getInt(j)));
                    }
                }

                JSONObject advertObject = dataObject.getJSONObject("advert");
                filterAdvertList.add(String.valueOf(advertObject.getInt("value")));
            }

            fileAdd("fileWithId", String.valueOf(filterIdList));
            postOffersUrlWithFilter(url + "&id=" + getRandomValue(filterIdList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&title=" + getRandomValue(filterTitleList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&advert[]=" + getRandomValue(filterAdvertList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&category[]=" + getRandomValue(filterCategoryList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&status[]=" + getRandomValue(filterStatusList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&privacyLevel=" + getRandomValue(filterPrivacyLevelList), "fileWithId", false);
            postOffersUrlWithFilter(url + "&tags[]=" + getRandomValue(filterTagList), "fileWithId", false);
        } else {
            System.err.println("Ошибка!!!!!!!!");
        }

        // прогнать id по всем доп фильтрам "trafficSources[]";
        postOffersTrafficSource(filterIdList);
    }

    private static void postAffiliatesUrl(String url) {
        List<String> filterIdList = new ArrayList<>();

        fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("affiliate");

            List<String> filterStatusList = new ArrayList<>();
            List<String> filterManagerList = new ArrayList<>();


            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));

                if (!dataObject.isNull("manager"))
                    filterManagerList.add(String.valueOf(dataObject.getInt("manager")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));
            }

            fileAdd("fileWithId", String.valueOf(filterIdList));
            postAffiliatesUrlWithFilter(url + "&id=" + getRandomValue(filterIdList), "fileWithId", false);
            postAffiliatesUrlWithFilter(url + "&manager[]=" + getRandomValue(filterManagerList), "fileWithId", false);
            postAffiliatesUrlWithFilter(url + "&status[]=" + getRandomValue(filterStatusList), "fileWithId", false);

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }

        // прогнать id по всем доп фильтрам "trafficSource[]", "categories[]"
        postAffiliateTrafficSource(filterIdList);
        postAffiliateCategories(filterIdList);
    }

    private static void postAdminsUrl(String url) {
        List<String> filterIdList = new ArrayList<>();

        fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("admin");

            List<String> filterStatusList = new ArrayList<>();
            List<String> filterEmailList = new ArrayList<>();
            List<String> filterFirstNameList = new ArrayList<>();
            List<String> filterSecondNameList = new ArrayList<>();
            List<String> filterMessengerList = new ArrayList<>();


            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));

                if (!dataObject.isNull("email"))
                    filterEmailList.add(String.valueOf(dataObject.getString("email")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));

                if (!dataObject.isNull("firstName"))
                    filterFirstNameList.add(String.valueOf(dataObject.getString("firstName")));

                if (!dataObject.isNull("secondName"))
                    filterSecondNameList.add(String.valueOf(dataObject.getString("secondName")));

                if (!dataObject.isNull("skype"))
                    filterMessengerList.add(String.valueOf(dataObject.getString("skype")));

                if (!dataObject.isNull("telegram"))
                    filterMessengerList.add(String.valueOf(dataObject.getString("telegram")));

                if (!dataObject.isNull("phone"))
                    filterMessengerList.add(String.valueOf(dataObject.getString("phone")));

            }

            fileAdd("fileWithId", String.valueOf(filterIdList));
            postAdminUrlWithFilter(url + "&status=" + getRandomValue(filterStatusList), "fileWithId", false);
            postAdminUrlWithFilter(url + "&email=" + getRandomValue(filterEmailList), "fileWithId", false);
            postAdminUrlWithFilter(url + "&firstName=" + getRandomValue(filterFirstNameList), "fileWithId", false);
            postAdminUrlWithFilter(url + "&secondName=" + getRandomValue(filterSecondNameList), "fileWithId", false);
            postAdminUrlWithFilter(url + "&messenger=" + getRandomValue(filterMessengerList), "fileWithId", false);

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    private static void postTasksUrl(String url) {
        List<String> filterIdList = new ArrayList<>();

        fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray tasks = dataArray.getJSONArray("tasks");

            List<String> filterAssigneeList = new ArrayList<>();
            List<String> filterRequesterList = new ArrayList<>();
            List<String> filterStatusList = new ArrayList<>();
            List<String> filterTypeList = new ArrayList<>();
            List<String> filterDueDateList = new ArrayList<>();

            List<String> filterWatcherArrayList = new ArrayList<>();
            List<String> filterAdvertList = new ArrayList<>();
            List<String> filterAffiliateList = new ArrayList<>();
            List<String> filterOfferList = new ArrayList<>();


            for (int i = 0; i < tasks.length(); i++) {
                JSONObject dataObject = tasks.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));

                if (!dataObject.isNull("assigner"))
                    filterAssigneeList.add(String.valueOf(dataObject.getInt("assigner")));

                if (!dataObject.isNull("requester"))
                    filterRequesterList.add(String.valueOf(dataObject.getInt("requester")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));

                if (!dataObject.isNull("type"))
                    filterTypeList.add(String.valueOf(dataObject.getString("type")));

                if (!dataObject.isNull("dueDate"))
                    filterDueDateList.add(String.valueOf(dataObject.getString("dueDate")));

                if (!dataObject.isNull("status"))
                    filterStatusList.add(String.valueOf(dataObject.getString("status")));

                JSONArray watcherArray = dataObject.getJSONArray("watcher");
                if (!watcherArray.isEmpty()) {
                    for (int j = 0; j < watcherArray.length(); j++) {
                        filterWatcherArrayList.add(String.valueOf(watcherArray.getInt(j)));
                    }
                }
                if (!dataObject.isNull("advert")) {
                    JSONObject advertObject = dataObject.getJSONObject("advert");
                    filterAdvertList.add(String.valueOf(advertObject.getInt("value")));
                }

                if (!dataObject.isNull("affiliate")) {
                    JSONObject affiliateObject = dataObject.getJSONObject("affiliate");
                    filterAffiliateList.add(String.valueOf(affiliateObject.getInt("value")));
                }

                if (!dataObject.isNull("offer")) {
                    JSONObject offerObject = dataObject.getJSONObject("offer");
                    filterOfferList.add(String.valueOf(offerObject.getInt("value")));
                }
            }

            fileAdd("fileWithId", String.valueOf(filterIdList));
            postTaskUrlWithFilter(url + "&assignee[]=" + getRandomValue(filterAssigneeList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&requester[]=" + getRandomValue(filterRequesterList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&watcher[]=" + getRandomValue(filterWatcherArrayList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&status[]=" + getRandomValue(filterStatusList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&type[]=" + getRandomValue(filterTypeList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&advert[]=" + getRandomValue(filterAdvertList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&offer[]=" + getRandomValue(filterOfferList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&affiliate[]=" + getRandomValue(filterAffiliateList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&dueDateFrom=" + getRandomValue(filterDueDateList), "fileWithId", false);
            postTaskUrlWithFilter(url + "&dueDateTo=" + getRandomValue(filterDueDateList), "fileWithId", false);

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postAdvertSiteUrl(List<String> filterIdList) {
        List<String> filterSiteUrlList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id;
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);

            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");
                if (!data.isNull("siteUrl")) {
                    String siteUrl = data.getString("siteUrl");
                    filterSiteUrlList.add(siteUrl);
                }

            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&siteUrl=" + getRandomValue(filterSiteUrlList), "fileWithId", false);
    }

    static void postAdvertPaymentDetails(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id + "/payment-info";
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");

                if (data.has("payments") && !data.getJSONArray("payments").isEmpty()) {
                    JSONArray payments = data.getJSONArray("payments");

                    for (int i = 0; i < payments.length(); i++) {
                        JSONObject dataObject = payments.getJSONObject(i);

                        if (dataObject.has("requisites") && dataObject.get("requisites") instanceof JSONObject) {
                            JSONObject requisitesObject = dataObject.getJSONObject("requisites");

                            // Итерация по ключам объекта requisites
                            Iterator<String> keys = requisitesObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = requisitesObject.getString(key);

                                // Добавляем значение в список
                                filterList.add(value);
                            }
                        }
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&paymentDetails=" + getRandomValue(filterList), "fileWithId", false);
    }

    static void postAdvertPaymentType(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id + "/payment-info";
            System.out.println(url);

            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");
                if (!data.getJSONArray("payments").isEmpty()) {
                    JSONArray payments = data.getJSONArray("payments");
                    for (int i = 0; i < payments.length(); i++) {
                        JSONObject dataObject = payments.getJSONObject(i);
                        filterList.add(String.valueOf(dataObject.getInt("payment")));
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&paymentType[]=" + getRandomValue(filterList), "fileWithId", false);
    }

    static void postAdvertContactPerson(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id + "/contact";
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                if (!jsonObject.getJSONArray("data").isEmpty()) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = data.getJSONObject(i);
                        filterList.add(String.valueOf(dataObject.getString("person")));
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&person=" + getRandomValue(filterList), "fileWithId", false);
    }

    static void postAdvertContact(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id + "/contact";
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {

                JSONObject jsonObject = new JSONObject(responseBody);

                if (!jsonObject.getJSONArray("data").isEmpty()) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = data.getJSONObject(i);

                        if (dataObject.has("messengers") && !dataObject.getJSONArray("messengers").isEmpty()) {
                            JSONArray messengers = dataObject.getJSONArray("messengers");

                            for (int j = 0; j < messengers.length(); j++) {
                                JSONObject messengersObject = messengers.getJSONObject(j);
                                filterList.add(messengersObject.getString("value"));
                            }
                        }
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&contact=" + getRandomValue(filterList), "fileWithId", false);
    }

    static void postAdvertContactEmail(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/advert/" + id + "/contact";
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);

            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                if (!jsonObject.getJSONArray("data").isEmpty()) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = data.getJSONObject(i);
                        if (!dataObject.isNull("email"))
                            filterList.add(dataObject.getString("email"));
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }
        postAdvertsUrlWithFilter(advertisersUrl + "&contact=" + getRandomValue(filterList), "fileWithId", false);
    }

    static void postAdvertsUrlWithFilter(String url, String idFileName, Boolean isCheck) {
        if (!isCheck)
            fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("adverts");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            fileAdd(idFileName, String.valueOf(filterIdList));

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postAffiliatesUrlWithFilter(String url, String idFileName, Boolean isCheck) {
        if (!isCheck)
            fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("affiliate");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            fileAdd(idFileName, String.valueOf(filterIdList));

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postAdminUrlWithFilter(String url, String idFileName, Boolean isCheck) {
        if (!isCheck)
            fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("admin");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            fileAdd(idFileName, String.valueOf(filterIdList));

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postTaskUrlWithFilter(String url, String idFileName, Boolean isCheck) {
        if (!isCheck)
            fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("tasks");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            fileAdd(idFileName, String.valueOf(filterIdList));

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postOffersUrlWithFilter(String url, String idFileName, Boolean isCheck) {
        if (!isCheck)
            fileAdd("fileWithUrl", url);

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(url);

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

        if (responseBody.contains("{\"success\":true")) {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataArray = jsonObject.getJSONObject("data");
            JSONArray adverts = dataArray.getJSONArray("offers");

            List<String> filterIdList = new ArrayList<>();
            for (int i = 0; i < adverts.length(); i++) {
                JSONObject dataObject = adverts.getJSONObject(i);
                filterIdList.add(String.valueOf(dataObject.getInt("id")));
            }
            fileAdd(idFileName, String.valueOf(filterIdList));

        } else {
            System.err.println("Ошибка!!!!!!!!");
        }
    }

    static void postOffersTrafficSource(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/offer/" + id;
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            JSONObject jsonObject = new JSONObject(responseBody);

            if (!jsonObject.isNull("offer")) {
                JSONObject offerObject = jsonObject.getJSONObject("offer");
                if (!offerObject.isNull("trafficSource")) {
                    JSONObject trafficSource = offerObject.getJSONObject("trafficSource");
                    if (!trafficSource.isNull("allow")) {
                        JSONArray allowArray = trafficSource.getJSONArray("allow");
                        for (int i = 0; i < allowArray.length(); i++) {
                            JSONObject allowObject = allowArray.getJSONObject(i);
                            if (!allowObject.isNull("id")) {
                                filterList.add(String.valueOf(allowObject.getInt("id")));
                            }
                        }
                    }
                }
            }
        }
        postOffersUrlWithFilter(offersUrl + "&trafficSources[]=" + getRandomValue(filterList),
                "fileWithId", false);
    }

    static void postAffiliateTrafficSource(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/affiliate/" + id;
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");

                if (!data.getJSONArray("trafficsSource").isEmpty()) {
                    JSONArray trafficsSourceArray = data.getJSONArray("trafficsSource");
                    for (int i = 0; i < trafficsSourceArray.length(); i++) {
                        filterList.add(String.valueOf(trafficsSourceArray.getInt(i)));
                    }
                }

            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }

        postAffiliatesUrlWithFilter(affiliateUrl + "&trafficSources[]=" + getRandomValue(filterList),
                "fileWithId", false);
    }

    static void postAffiliateCategories(List<String> filterIdList) {
        List<String> filterList = new ArrayList<>();
        for (String id : filterIdList) {
            String url = "https://api.newx.3tracks.online/affiliate/" + id;
            System.out.println(url);
            Response response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url);
            String responseBody = response.getBody().asString();
            System.out.println(responseBody);

            if (responseBody.contains("{\"success\":true")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");

                if (!data.getJSONArray("categories").isEmpty()) {
                    JSONArray trafficsSourceArray = data.getJSONArray("categories");
                    for (int i = 0; i < trafficsSourceArray.length(); i++) {
                        filterList.add(String.valueOf(trafficsSourceArray.getInt(i)));
                    }
                }
            } else {
                System.err.println("Ошибка!!!!!!!!");
            }
        }

        postAffiliatesUrlWithFilter(affiliateUrl + "&categories[]=" + getRandomValue(filterList),
                "fileWithId", false);
    }

    static void fileNew(String fileName) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            System.out.println("File created or cleared: " + fileName);
        } catch (IOException e) {
            System.err.println("An error occurred while creating or clearing the file: " + e.getMessage());
        }
    }

    private static void fileAdd(String fileName, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // true - режим добавления
            writer.newLine(); // Добавляем новую строку перед текстом
            writer.write(text);
            System.out.println("Text has been appended to the file: " + fileName);
        } catch (IOException e) {
            System.err.println("An error occurred while appending to the file: " + e.getMessage());
        }
    }

    public static String getRandomValue(List<String> list) {
        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("List is null or empty");
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}