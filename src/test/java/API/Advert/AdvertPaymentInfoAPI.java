package API.Advert;

import AdvertPackage.entity.AdvertPaymentInfo;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, add, edit, проверка, delete
 - edit minPayout
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

        AdvertRequisites advertRequisites = requisiteAdd();
        advertPaymentId = Integer.parseInt(getLastValueFromBDWhere("id", "advert_payment",
                "advert_id", String.valueOf(advertId)));

        advertRequisites.setRequisitesId(advertPaymentId);

        Allure.step(CHECK);
        requisitesAssert(advertRequisites);


        Allure.step("Редактируем последний метод оплаты у текущего Адверта" + advertPaymentId);
        AdvertRequisites advertRequisitesEdit = requisiteEdit();
        Allure.step("Редактируем minPayout у текущего Адверта" + advertPaymentId);
        BigDecimal minPayment = minPaymentEdit();

        Allure.step(CHECK);
        requisitesAssert(advertRequisitesEdit);
        paymentAssert(minPayment);

        Allure.step(DELETE + advertPaymentId);
        deleteMethod("advert", advertId + "/payment-info/" + advertPaymentId);
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

    private static AdvertRequisites requisiteAdd() throws Exception {
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

        String responseBody = response.getBody().asString();
        System.out.println(ADD_RESPONSE + responseBody);
        Allure.step(ADD_RESPONSE + responseBody);

    return advertRequisites;
    }

    public static BigDecimal minPaymentEdit() throws Exception {
        AdvertPaymentInfo advertPaymentInfo = new AdvertPaymentInfo();
        advertPaymentInfo.fillAdvertPaymentInfoWithRandomData();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("minPayout", advertPaymentInfo.getMinPayout());

        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info");

        String responseBody = response.getBody().asString();
        System.out.println(EDIT_RESPONSE + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertPaymentInfo.getMinPayout();
    }


    public static AdvertRequisites requisiteEdit() throws Exception {
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

        String responseBody = response.getBody().asString();
        System.out.println(EDIT_RESPONSE + responseBody);
        Allure.step(EDIT_RESPONSE + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertRequisitesEdit;
    }

    public static AdvertPaymentInfo paymentGet(Boolean isShow) {
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
        JSONObject dataObject = jsonObject.getJSONObject("data");

        AdvertPaymentInfo advertPaymentInfo = new AdvertPaymentInfo();
        advertPaymentInfo.setMinPayout(dataObject.isNull("minPayout") ? null : dataObject.getBigDecimal("minPayout"));

        JSONArray dataArray = dataObject.getJSONArray("payments");
        for (int i = 0; i < dataArray.length(); i++) {
            AdvertRequisites advertRequisites = new AdvertRequisites();
            JSONObject requisitesObject = dataArray.getJSONObject(i);

            advertRequisites.setRequisitesId(requisitesObject.getInt("id"));
            advertRequisites.setCurrency(requisitesObject.getString("currency"));
            advertRequisites.setPaymentSystemId(requisitesObject.getInt("payment"));

            JSONObject requisites = requisitesObject.getJSONObject("requisites");
            Map<String, String> requisitesMap = new HashMap<>();

            for (String key : requisites.keySet()) {
                requisitesMap.put(key, requisites.optString(key, ""));
            }
            advertRequisites.setRequisites(requisitesMap);
            advertPaymentInfo.addAdvertRequisitesList(advertRequisites);
        }
        return advertPaymentInfo;
    }

    public static void requisitesAssert(AdvertRequisites advertRequisitesEdit) {
        AdvertPaymentInfo advertPaymentInfo = paymentGet(true);
        List<AdvertRequisites> advertRequisitesList = advertPaymentInfo.getAdvertRequisitesList();

        boolean isAssert = false;
        for (AdvertRequisites advertRequisite : advertRequisitesList) {
            if (advertRequisite.getRequisitesId() == advertPaymentId) {
                Assert.assertEquals(advertRequisite.getPaymentSystemId(), advertRequisitesEdit.getPaymentSystemId());
                Assert.assertEquals(advertRequisite.getCurrency(), advertRequisitesEdit.getCurrency());
             //   Assert.assertEquals(advertRequisite.getDefault(), advertRequisitesEdit.getDefault());
                Assert.assertEquals(advertRequisite.getRequisites(), advertRequisitesEdit.getRequisites());
                isAssert = true;
            }
        }
        Assert.assertTrue(isAssert);
    }

    public static void paymentAssert(BigDecimal minPaymentEdit) {
        AdvertPaymentInfo advertPaymentInfo = paymentGet(true);
        Assert.assertEquals(minPaymentEdit, advertPaymentInfo.getMinPayout());
    }
}