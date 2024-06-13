package AdminPackage.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class AdminPermissionsAPI {

    static int adminId = 97;
    static String rowName = "offers";

    static String jsonAllTrueBody = "{\"permissions\":" +
            "{\"admin\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"admin_log\":{\"view\":true}," +
            "\"dashboard\":{\"view\":true}," +
            "\"offers\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"advertisers\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"advertisers_documents\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"advertisers_notes\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"affiliates\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"affiliates_moderation\":{\"view\":true,\"edit\":true,\"delete\":true}," +
            "\"affiliates_log\":{\"view\":true,\"export\":true}," +
            "\"statistics_custom\":{\"view\":true,\"create\":true,\"export\":true}," +
            "\"statistics_daily\":{\"view\":true,\"export\":true}," +
            "\"statistics_conversions\":{\"view\":true,\"edit\":true}," +
            "\"statistics_advertisers\":{\"view\":true,\"edit\":true,\"export\":true}," +
            "\"statistics_affiliates\":{\"view\":true,\"export\":true}," +
            "\"statistics_sales_managers\":{\"view\":true,\"export\":true}," +
            "\"statistics_affiliate_managers\":{\"view\":true,\"export\":true}," +
            "\"statistics_offers\":{\"view\":true,\"export\":true}," +
            "\"statistics_refferals\":{\"view\":true,\"export\":true}," +
            "\"statistics_partners\":{\"view\":true,\"export\":true}," +
            "\"statistics_countries\":{\"view\":true,\"export\":true}," +
            "\"statistics_cities\":{\"view\":true,\"export\":true}," +
            "\"statistics_os\":{\"view\":true,\"export\":true}," +
            "\"statistics_goals\":{\"view\":true,\"export\":true}," +
            "\"statistics_devices\":{\"view\":true,\"export\":true}," +
            "\"statistics_connection_type\":{\"view\":true,\"export\":true}," +
            "\"statistics_landing\":{\"view\":true,\"export\":true}," +
            "\"statistics_affiliate_postbacks\":{\"view\":true,\"export\":true}," +
            "\"statistics_server_postbacks\":{\"view\":true,\"export\":true}," +
            "\"statistics_retention_rate\":{\"view\":true,\"export\":true}," +
            "\"statistics_kpi\":{\"view\":true,\"export\":true}," +
            "\"statistics_comparison_report\":{\"view\":true,\"export\":true}," +
            "\"statistics_categories\":{\"view\":true,\"export\":true}," +
            "\"news\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"billing_advertisers\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"billing_partners\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"billing_compleed_invoice\":{\"create\":true,\"delete\":true}," +
            "\"tickets\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true,\"export\":true}," +
            "\"remove_tickets\":{\"delete\":true}," +
            "\"settings\":{\"view\":true,\"edit\":true,\"create\":true,\"delete\":true}," +
            "\"settings_tracking_domains\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"settings_tags\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"setting_user_request_source\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"gererate_shared_reports_link\":{\"create\":true}," +
            "\"generate_links_tickets\":{\"edit\":true}," +
            "\"tasks\":{\"view\":true,\"create\":true,\"edit\":true,\"delete\":true}," +
            "\"notice\":{\"view\":true,\"edit\":true,\"delete\":true}," +
            "\"content_filter\":{\"create\":true,\"edit\":true,\"view\":true,\"delete\":true}}}";


    @Test
    public static void postApiPermission() {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonAllTrueBody, JsonObject.class);

        JsonObject adminPermissions = jsonObject.getAsJsonObject("permissions")
                .getAsJsonObject(rowName);

        adminPermissions.addProperty("view", false);
        adminPermissions.addProperty("create", false);
        adminPermissions.addProperty("edit", false);
        adminPermissions.addProperty("delete", false);
        adminPermissions.addProperty("export", false);


        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", "Bearer d8ed15517b05a53d53339b4d5e1f0abf")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/admin/" + adminId + "/permission");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
    }
}