package API.Admin;

import AdminPackage.entity.AdminGeneral;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - get, add/edit, delete проверка
 во вкладке Админ - "General"
 TODO: 100% DONE
 */

public class AdminGeneralAPI {
    static int adminId;

    @Test
    public static void test() throws Exception {
        Allure.step("Добавляем Админа");
        AdminGeneral adminGeneralNew = generalAddEdit(false);
        Allure.step(CHECK);
        generalAssert(adminGeneralNew);

        Allure.step("Получаем General Info Админа id= " + adminId);
        generalGet(true);

        Allure.step("Редактируем General Info Админа id= " + adminId);
        AdminGeneral adminGeneralEdit = generalAddEdit(true);
        Allure.step("Получаем General Info Админа id= " + adminId);
        generalGet(true);
        Allure.step(CHECK);
        generalAssert(adminGeneralEdit);
        Allure.step(DELETE + adminId);
        deleteMethod("admin", String.valueOf(adminId));
        assertDelete(String.valueOf(adminId), "admin");
    }

    private static JsonObject initializeJsonAdminGeneral(AdminGeneral adminGeneral, boolean isEdit) {
        JsonObject jsonObject = new JsonObject();
        JsonObject adminObject = new JsonObject();
        adminObject.addProperty("status", adminGeneral.getStatus().toLowerCase());
        adminObject.addProperty("roleId", Integer.parseInt(adminGeneral.getRoleId()));
        adminObject.addProperty("email", adminGeneral.getEmail());
        if (!isEdit)
            adminObject.addProperty("plainPassword", adminGeneral.getPassword());
        adminObject.addProperty("firstName", adminGeneral.getFirstName());
        adminObject.addProperty("secondName", adminGeneral.getLastName());
        adminObject.addProperty("phone", adminGeneral.getPhone().toLowerCase());
        adminObject.addProperty("skype", adminGeneral.getSkype());
        adminObject.addProperty("telegram", adminGeneral.getTelegram());
        adminObject.addProperty("workingHours", adminGeneral.getWorkingHours());

        if (isEdit)
            jsonObject.add("admin_edit", adminObject);
        else jsonObject.add("admin", adminObject);

        return jsonObject;
    }

    public static AdminGeneral generalAddEdit(Boolean isEdit) throws Exception {
        AdminGeneral adminGeneral = new AdminGeneral();
        adminGeneral.fillAdminGeneralWithRandomDataForAPI();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdminGeneral(adminGeneral, isEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step(DATA + jsonObject.toString().replace("],", "],\n"));
        attachJson(String.valueOf(jsonObject), DATA);

        String path = isEdit ? "https://api.admin.3tracks.link/admin/" + adminId + "/edit" :
                "https://api.admin.3tracks.link/admin/new";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post(path);

        String responseBody = response.getBody().asString();

        if (!isEdit) {
            System.out.println(ADD_RESPONSE + responseBody);
            Allure.step(ADD_RESPONSE + responseBody);
            JSONObject jsonResponse = new JSONObject(responseBody);
            adminId = jsonResponse.getJSONObject("data").getInt("id");
        } else {
            System.out.println(EDIT_RESPONSE + responseBody);
            Allure.step(EDIT_RESPONSE + responseBody);
        }
        Assert.assertTrue(responseBody.contains("{\"success\":true"));
        return adminGeneral;
    }

    public static AdminGeneral generalGet(Boolean isShow) {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/admin/" + adminId);

        String responseBody = response.getBody().asString();

        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

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
        AdminGeneral adminGeneral = generalGet(false);
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