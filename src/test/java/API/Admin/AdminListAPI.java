package API.Admin;

import AdminPackage.entity.Admin;
import AdminPackage.entity.AdminGeneral;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getArrayFromBDWhere;

/***
 Тест проверяет работу API методов Админов
 - getList,
 TODO: lastLoginDt, lastLoginIp
 */


public class AdminListAPI {

    @Test
    public static void test() throws Exception {
        adminListGet();
    }

    public static void adminListGet() throws Exception {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/admin/");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray adminArray = data.getJSONArray("admin");
        for (int i = 0; i < adminArray.length(); i++) {
            Admin admin = new Admin();
            AdminGeneral adminGeneral = admin.getAdminGeneral();
            JSONObject dataObject = adminArray.getJSONObject(i);
            adminGeneral.setStatus(dataObject.getString("status"));

            if (!dataObject.isNull("role")){
                JSONObject role = dataObject.getJSONObject("role");
                adminGeneral.setRoleId(String.valueOf(role.getInt("value")));
            }
            adminGeneral.setEmail(dataObject.isNull("email") ? null : dataObject.getString("email"));
            adminGeneral.setFirstName(dataObject.isNull("firstName") ? null : dataObject.getString("firstName"));
            adminGeneral.setLastName(dataObject.isNull("secondName") ? null : dataObject.getString("secondName"));
            adminGeneral.setPhone(dataObject.isNull("phone") ? null : dataObject.getString("phone"));
            adminGeneral.setSkype(dataObject.isNull("skype") ? null : dataObject.getString("skype"));
            adminGeneral.setTelegram(dataObject.isNull("telegram") ? null : dataObject.getString("telegram"));
            adminGeneral.setWorkingHours(dataObject.isNull("workingHours") ? null : dataObject.getString("workingHours"));
            admin.setId(dataObject.getInt("id"));
        }
    }
}
