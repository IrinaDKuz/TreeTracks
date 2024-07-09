package API.Advert;

import AdvertPackage.entity.AdvertRequisites;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        System.out.println(advertId);
        paymentGet();
        paymentAdd();
        advertPaymentId = Integer.parseInt(getRandomValueFromBDWhere("id", "advert_payment",
                "advert_id", String.valueOf(advertId)));
        AdvertRequisites advertRequisites = paymentEdit();
        paymentAssert(advertRequisites);
        paymentDelete();
    }

    private static JsonObject initializeJsonAdvertPayment(AdvertRequisites advertRequisites) {
        JsonObject jsonObject = new JsonObject();

        // Создаем вложенный объект requisites
        JsonObject requisitesObject = new JsonObject();

        for (Object key : advertRequisites.getRequisites().keySet()) {
            requisitesObject.addProperty((String) key, advertRequisites.getRequisites().get(key));
        }

        // Создаем объект advert_payment_info
        JsonObject advertPaymentInfoObject = new JsonObject();
        advertPaymentInfoObject.addProperty("currency", advertRequisites.getCurrency());
        advertPaymentInfoObject.add("requisites", requisitesObject);
        advertPaymentInfoObject.addProperty("paymentSystemId", advertRequisites.getPaymentSystemId());
        advertPaymentInfoObject.addProperty("isDefault", advertRequisites.getDefault());

        // Создаем основной объект jsonObject и добавляем в него advert_payment_info
        jsonObject.add("advert_payment_info", advertPaymentInfoObject);
        return jsonObject;
    }

    private static void paymentAdd() throws Exception {
        AdvertRequisites advertRequisites = new AdvertRequisites();
        advertRequisites.fillAdvertRequisitesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPayment(advertRequisites), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

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
        System.out.println("Ответ на add: " + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        //  advertPaymentId = jsonResponse.getJSONObject("data").getInt("advertContact");
    }

    public static AdvertRequisites paymentEdit() throws Exception {
        AdvertRequisites advertRequisitesEdit = new AdvertRequisites();
        advertRequisitesEdit.fillAdvertRequisitesWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPayment(advertRequisitesEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

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
        System.out.println("Ответ на edit: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertRequisitesEdit;
    }

    public static ArrayList<AdvertRequisites> paymentGet() {
        ArrayList<AdvertRequisites> paymentList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

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

    public static void paymentAssert(AdvertRequisites advertRequisiteEdit) throws Exception {
        ArrayList<AdvertRequisites> advertRequisitesList = paymentGet();
        for (AdvertRequisites advertRequisite : advertRequisitesList) {
            if (advertRequisite.getRequisitesId() == advertPaymentId) {
                Assert.assertEquals(advertRequisite.getPaymentSystemId(), advertRequisiteEdit.getPaymentSystemId());
                Assert.assertEquals(advertRequisite.getCurrency(), advertRequisiteEdit.getCurrency());
                Assert.assertEquals(advertRequisite.getDefault(), advertRequisiteEdit.getDefault());
                Assert.assertEquals(advertRequisite.getRequisites(), advertRequisiteEdit.getRequisites());
            }
        }
    }

    public static void paymentDelete() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/advert/" + advertId + "/payment-info/" + advertPaymentId);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на delete: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }
}