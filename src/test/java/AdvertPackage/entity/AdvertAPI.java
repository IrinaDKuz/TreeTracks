package AdvertPackage.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static Helper.Adverts.*;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class AdvertAPI {

    static int advertId = 1010;
    static String rowName = "offers";

    static JsonObject jsonAdvertPrimaryInfo = new JsonObject();

    private static JsonObject initializeJsonAdvertPIBody() throws Exception {
        // Добавляем свойства в объект JsonObject
        jsonAdvertPrimaryInfo.addProperty("tab", "primary");

        JsonObject advertObject = new JsonObject();
        advertObject.addProperty("company", generateName(3, COMPANY_WORDS));

        JsonArray allowedIpArray = new JsonArray();
        allowedIpArray.add(generateIPAddress());
        allowedIpArray.add(generateIPAddress());
        advertObject.add("allowedIp", allowedIpArray);

        JsonArray allowedSubAccountArray = new JsonArray();
        allowedSubAccountArray.add("sub8");
        allowedSubAccountArray.add("sub5");
        allowedSubAccountArray.add("sub2");
        advertObject.add("allowedSubAccount", allowedSubAccountArray);

        JsonArray disallowedSubAccountArray = new JsonArray();
        disallowedSubAccountArray.add("sub1");
        disallowedSubAccountArray.add("sub3");
        disallowedSubAccountArray.add("sub4");
        advertObject.add("disallowedSubAccount", disallowedSubAccountArray);

        advertObject.addProperty("forbidChangePostbackStatus", true);
        advertObject.addProperty("managerId", getRandomValueFromBD("id", "admin"));

        advertObject.addProperty("accountManager", "");
        advertObject.addProperty("salesManager", getRandomValueFromBD("id", "admin"));

        JsonArray categoriesArray = new JsonArray();
        categoriesArray.add(getRandomValueFromBDWhere("id", "category", "lang", "'general'"));
        advertObject.add("categories", categoriesArray);

        advertObject.addProperty("siteUrl", generateCompanyUrl(generateName(3, COMPANY_WORDS)));

        JsonArray geoArray = new JsonArray();
        geoArray.add("al");
        geoArray.add("am");
        geoArray.add("at");
        advertObject.add("geo", geoArray);

        // advertObject.addProperty("modelType", getRandomValue(MODEL_TYPES_MAP));
        advertObject.addProperty("modelType", "cpa");

        // advertObject.addProperty("status", getRandomValue(STATUS_MAP));
        advertObject.addProperty("status", "active");


        JsonArray tagArray = new JsonArray();
        tagArray.add(generateName(1, COMPANY_WORDS));
        tagArray.add(generateName(1, COMPANY_WORDS));
        advertObject.add("tag", tagArray);

        advertObject.addProperty("companyLegalname", generateName(4, COMPANY_WORDS));
        advertObject.addProperty("note", generateName(10, COMPANY_WORDS));
        advertObject.addProperty("userRequestSourceId", getRandomValueFromBD("id", "user_request_source"));
        advertObject.addProperty("userRequestSourceValue", generateName(2, COMPANY_WORDS));

        jsonAdvertPrimaryInfo.add("advert", advertObject);
        return jsonAdvertPrimaryInfo;
    }


    @Test
    public static void advertEdit() throws Exception {
        for (int i = 1; i < 5; i++) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(initializeJsonAdvertPIBody(), JsonObject.class);

            Response response;
            response = RestAssured.given()
                    .contentType(ContentType.URLENC)
                    .header("Authorization", "Bearer d8ed15517b05a53d53339b4d5e1f0abf")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .post("https://api.admin.3tracks.link/advert/" + advertId + "/edit");

            // Получаем и выводим ответ
            String responseBody = response.getBody().asString();
            System.out.println("Ответ: " + responseBody);
            advertId ++;
        }
    }
}