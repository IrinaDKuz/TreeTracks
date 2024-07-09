package API.OfferDraft;

import AdminPackage.entity.AdminGeneral;
import OfferDraftPackage.entity.OfferDraft;
import OfferDraftPackage.entity.OfferGeneral;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static API.Helper.deleteMethod;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete проверка
 для Offer Draft
 */

public class OfferDraftAPI {
    static int offerId;

    @Test
    public static void test() throws Exception {

        generalAddEdit(false);
        generalGet();
        OfferGeneral offerGeneral = generalAddEdit(true);
        generalAssert(adminGeneral);
        // тут будут методы для вкладок

        deleteMethod("admin", String.valueOf(offerId));
    }

    private static JsonObject initializeJsonOfferGeneral(OfferGeneral offerGeneral, boolean isEdit) {
        JsonObject jsonObject = new JsonObject();
        JsonObject offerObject = new JsonObject();
        offerObject.addProperty("title", offerGeneral.getTitle());
        offerObject.addProperty("status", offerGeneral.getStatus());
        offerObject.addProperty("statusNotice", offerGeneral.getStatusNotice());
        offerObject.addProperty("privacyLevel", offerGeneral.getPrivacyLevel());
        offerObject.addProperty("statusNotice", offerGeneral.getStatusNotice());

        offerObject.addProperty("statusNotice", offerGeneral.getStatusNotice());

        offerObject.addProperty("email", adminGeneral.getEmail());
        if (!isEdit)
            offerObject.addProperty("plainPassword", adminGeneral.getPassword());
        offerObject.addProperty("firstName", adminGeneral.getFirstName());
        offerObject.addProperty("secondName", adminGeneral.getLastName());
        offerObject.addProperty("phone", adminGeneral.getPhone().toLowerCase());
        offerObject.addProperty("skype", adminGeneral.getSkype());
        offerObject.addProperty("telegram", adminGeneral.getTelegram());
        offerObject.addProperty("workingHours", adminGeneral.getWorkingHours());

        offerObject.addProperty("tag", offerGeneral.getTagId().getFirst());


        jsonObject.add("offer", offerObject);
        jsonObject.addProperty("tab", "general");


        return jsonObject;
    }

    public static OfferGeneral generalAddEdit(Boolean isEdit) throws Exception {
        OfferGeneral offerGeneral = new OfferGeneral();
        offerGeneral.fillOfferGeneralWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonOfferGeneral(offerGeneral, isEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        String path = isEdit ? "https://api.admin.3tracks.link/offer/" + offerId + "/edit" :
                "https://api.admin.3tracks.link/offer/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add/edit: " + responseBody);
        Assert.assertTrue(responseBody.contains("{\"success\":true"));

        if (!isEdit) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            offerId = jsonResponse.getJSONObject("data").getInt("id");
        }
        return offerGeneral;
    }

    public static AdminGeneral generalGet() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/admin/" + offerId);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject admin = data.getJSONObject("admin");

        AdminGeneral adminGeneral = new AdminGeneral();
        adminGeneral.setStatus(admin.getString("status"));
        JSONObject role = admin.getJSONObject("role");
        adminGeneral.setRoleId(String.valueOf(role.isNull("value") ? null : role.getInt("value")));
        adminGeneral.setEmail(admin.isNull("email") ? null : admin.getString("email"));
        adminGeneral.setFirstName(admin.isNull("firstName") ? null : admin.getString("firstName"));
        adminGeneral.setLastName(admin.isNull("secondName") ? null : admin.getString("secondName"));
        adminGeneral.setPhone(admin.isNull("phone") ? null : admin.getString("phone"));
        adminGeneral.setSkype(admin.isNull("skype") ? null : admin.getString("skype"));
        adminGeneral.setTelegram(admin.isNull("telegram") ? null : admin.getString("telegram"));
        adminGeneral.setWorkingHours(admin.isNull("workingHours") ? null : admin.getString("workingHours"));
        return adminGeneral;
    }

    public static void generalAssert(AdminGeneral adminGeneralEdit) {
        AdminGeneral adminGeneral = generalGet();
        Assert.assertEquals(adminGeneral.getStatus(), adminGeneralEdit.getStatus());
        Assert.assertEquals(adminGeneral.getRoleId(), adminGeneralEdit.getRoleId());
        Assert.assertEquals(adminGeneral.getEmail(), adminGeneralEdit.getEmail());
        Assert.assertEquals(adminGeneral.getFirstName(), adminGeneralEdit.getFirstName());
        Assert.assertEquals(adminGeneral.getLastName(), adminGeneralEdit.getLastName());
        Assert.assertEquals(adminGeneral.getPhone(), adminGeneralEdit.getPhone());
        Assert.assertEquals(adminGeneral.getSkype(), adminGeneralEdit.getSkype());
        Assert.assertEquals(adminGeneral.getTelegram(), adminGeneralEdit.getTelegram());
        Assert.assertEquals(adminGeneral.getWorkingHours(), adminGeneralEdit.getWorkingHours());
    }
}