package API.Advert;

import AdvertPackage.entity.AdvertRequisites;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;

/***
 Тест проверяет работу API методов
 - get, add, edit, проверка, delete
 во вкладке Адверт - "Payment Info"
 */

public class AdvertPaymentInfoAPI {
    public static int advertPaymentId;
    public static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBD("id", "advert"));
        Allure.step("Получаем методы оплаты у рандомного Адверта " + advertId);
        paymentGet(true);
        Allure.step("Добавляем метод оплаты и заполняем его поля");
        paymentAdd();

        advertPaymentId = Integer.parseInt(getRandomValueFromBDWhere("id", "advert_payment",
                "advert_id", String.valueOf(advertId)));
        Allure.step("Редактируем любой метод оплаты у текущего Адверта" + advertPaymentId );
        AdvertRequisites advertRequisites = paymentEdit();
        Allure.step(CHECK);
        paymentAssert(advertRequisites);
        Allure.step(DELETE + advertPaymentId);
        deleteMethod("advert",advertId + "/payment-info/" + advertPaymentId);
        assertDelete(String.valueOf(advertPaymentId), "advert_payment");
    }

    private static JsonObject initializeJsonAdvertPayment(AdvertRequisites advertRequisites) {
        JsonObject jsonObject = new JsonObject();
        JsonObject requisitesObject = new JsonObject();

        for (Object key : advertRequisites.getRequisites().keySet()) {
            requisitesObject.addProperty((String) key, advertRequisites.getRequisites().get(key));
        }

        JsonObject advertPaymentInfoObject = new JsonObject();
        advertPaymentInfoObject.addProperty("currency", advertRequisites.getCurrency());
        advertPaymentInfoObject.add("requisites", requisitesObject);
        advertPaymentInfoObject.addProperty("paymentSystemId", advertRequisites.getPaymentSystemId());
        advertPaymentInfoObject.addProperty("isDefault", advertRequisites.getDefault());

        jsonObject.add("advert_payment_info", advertPaymentInfoObject);
        return jsonObject;
    }

    private static void paymentAdd() throws Exception {
        AdvertRequisites advertRequisites = new AdvertRequisites();
        advertRequisites.fillAdvertRequisitesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPayment(advertRequisites), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info/add");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println(ADD_RESPONSE + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        //  advertPaymentId = jsonResponse.getJSONObject("data").getInt("advertContact");
    }

    public static AdvertRequisites paymentEdit() throws Exception {
        AdvertRequisites advertRequisitesEdit = new AdvertRequisites();
        advertRequisitesEdit.fillAdvertRequisitesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPayment(advertRequisitesEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info/" + advertPaymentId + "/edit");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println(EDIT_RESPONSE + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertRequisitesEdit;
    }

    public static ArrayList<AdvertRequisites> paymentGet(Boolean isShow) {
        ArrayList<AdvertRequisites> paymentList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertRequisites advertRequisites = new AdvertRequisites();
            JSONObject dataObject = dataArray.getJSONObject(i);

            advertRequisites.setRequisitesId(dataObject.getInt("id"));
            advertRequisites.setCurrency(dataObject.getString("currency"));
            advertRequisites.setPaymentSystemId(dataObject.getInt("payment"));

            JSONObject requisites = dataObject.getJSONObject("requisites");
            Map<String, String> requisitesMap = new HashMap<>();

            for (String key : requisites.keySet()) {
                requisitesMap.put(key, requisites.optString(key, ""));
            }
            advertRequisites.setRequisites(requisitesMap);
        }
        return paymentList;
    }

    public static void paymentAssert(AdvertRequisites advertRequisiteEdit) {
        ArrayList<AdvertRequisites> advertRequisitesList = paymentGet(true);
        for (AdvertRequisites advertRequisite : advertRequisitesList) {
            if (advertRequisite.getRequisitesId() == advertPaymentId) {
                Assert.assertEquals(advertRequisite.getPaymentSystemId(), advertRequisiteEdit.getPaymentSystemId());
                Assert.assertEquals(advertRequisite.getCurrency(), advertRequisiteEdit.getCurrency());
                Assert.assertEquals(advertRequisite.getDefault(), advertRequisiteEdit.getDefault());
                Assert.assertEquals(advertRequisite.getRequisites(), advertRequisiteEdit.getRequisites());
            }
        }
    }
}