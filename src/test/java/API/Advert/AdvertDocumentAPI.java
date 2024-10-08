package API.Advert;

import AdvertPackage.entity.AdvertDocument;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;

import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов
 - get, upload(add), проверка, edit, проверка, download, delete
 во вкладке Адверт - "Document"
 */
// TODO: 100% DONE

public class AdvertDocumentAPI {
    public static String advertDocumentHash;
    public static String advertDocumentId;
    public static int advertId;

    @Test
    public static void test() throws Exception {
        authApi(103);
        advertId = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "1000"));
        Allure.step("Получаем Документы у рандомного Адверта " + advertId);
        documentsGet(true);

        Allure.step("Добавляем Документ");
        AdvertDocument advertDocumentNew = documentsAdd();
        Allure.step(CHECK);
        documentsAssert(advertDocumentNew);

        Allure.step("Редактируем Документ");
        AdvertDocument advertDocument = documentsEdit();
        Allure.step(CHECK);
        documentsAssert(advertDocument);

        Allure.step("Скачиваем Документ");
        documentsDownLoad();

        Allure.step("Удаляем Документ " + advertDocumentHash);
        deleteMethod("advert",advertId + "/document/" + advertDocumentHash);
        assertDelete(String.valueOf(advertDocumentId), "advert_document");
    }

    private static AdvertDocument documentsAdd() throws Exception {
        AdvertDocument advertDocument = new AdvertDocument();
        advertDocument.fillAdvertDocumentWithRandomDataForAPI();
        documentsAddPost(advertDocument);
        return advertDocument;
    }

    public static String documentsAddPost(AdvertDocument advertDocument) throws Exception {

        System.out.println(DATA + " path: " + advertDocument.getFilePath() + " description: " + advertDocument.getDescription());
        Allure.step(DATA + " path: " + advertDocument.getFilePath() + " description: " + advertDocument.getDescription());

        Response response = RestAssured.given()
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .multiPart("file", new java.io.File(advertDocument.getFilePath()))
                .multiPart("description", advertDocument.getDescription())
                .when()
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/document/upload")
                .then()
                .extract()
                .response();

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на upload: " + responseBody);
        Allure.step("Ответ на upload: " + responseBody);

        Assert.assertTrue(responseBody.contains("{\"success\":true}"));
        advertDocumentId = getLastValueFromBD("id", "advert_document");
        advertDocumentHash = getLastValueFromBDWhere("hash", "advert_document",
                "id", advertDocumentId);
        return advertDocumentHash;
    }

    public static AdvertDocument documentsEdit() throws Exception {
        AdvertDocument advertDocumentEdit = new AdvertDocument();
        advertDocumentEdit.fillAdvertDocumentWithRandomDataForAPI();
        documentsEditPost(String.valueOf(advertId), String.valueOf(advertDocumentHash), advertDocumentEdit);
        return advertDocumentEdit;
    }

    public static void documentsEditPost(String advertId, String advertDocumentHash, AdvertDocument advertDocument) throws Exception {
        System.out.println(DATA + " description: " + advertDocument.getDescription());
        Allure.step(DATA + " description: " + advertDocument.getDescription());

        Response response = RestAssured.given()
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .multiPart("description", advertDocument.getDescription())
                .when()
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/document/"
                        + advertDocumentHash + "/edit")
                .then()
                .extract()
                .response();

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на upload: " + responseBody);
        Allure.step("Ответ на upload: " + responseBody);

        Assert.assertTrue(responseBody.contains("{\"success\":true}"));
    }


    public static ArrayList<AdvertDocument> documentsGet(Boolean isShow) {
        ArrayList<AdvertDocument> documentsList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/document");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray dataArray = data.getJSONArray("documents");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertDocument advertDocument = new AdvertDocument();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertDocument.setOriginal_name(dataObject.getString("originalName"));
            advertDocument.setDescription(dataObject.isNull("description") ? null : dataObject.getString("description"));
            advertDocument.setHash(dataObject.getString("hash"));
            advertDocument.setExt(dataObject.getString("ext"));
            advertDocument.setUploader_id(String.valueOf(dataObject.getJSONObject("uploader").getInt("value")));
            documentsList.add(advertDocument);
        }
        return documentsList;
    }


    private static void documentsDownLoad() {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/document/"
                        + advertDocumentHash + "/download");

        response.then().assertThat().statusCode(200);
    }


    public static void documentsAssert(AdvertDocument advertDocumentEdit) {
        ArrayList<AdvertDocument> advertDocumentsList = documentsGet(true);
        Boolean isAssert = false;
        for (AdvertDocument advertDocument : advertDocumentsList) {
            if (advertDocument.getHash().equals(advertDocumentHash)) {
                SoftAssert softAssert = new SoftAssert();
                System.out.println(advertDocument.getHash());
                softAssert.assertEquals(advertDocument.getDescription(), advertDocumentEdit.getDescription());
                softAssert.assertEquals(advertDocument.getOriginal_name(), advertDocumentEdit.getOriginal_name());
                softAssert.assertEquals(advertDocument.getExt(), advertDocumentEdit.getExt());
                softAssert.assertEquals(advertDocument.getUploader_id(), advertDocumentEdit.getUploader_id());
                softAssert.assertAll();
                isAssert = true;
            }
        }
        Assert.assertTrue(isAssert);
    }
}