package API.Advert;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertContact.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.getRandomValueFromBD;

/***
 Тест проверяет работу API методов
 - get, add, edit, проверка, delete
 во вкладке Адверт - "Contact"
 */

public class AdvertContactsAPI {
    public static int advertContactId;
    public static int advertId;

    @Test
    public static void test() throws Exception {
        advertId = Integer.parseInt(getRandomValueFromBD("id", "advert"));
        contactsGet();
        contactsAdd();
        AdvertContact advertContact = contactsEdit();
       // contactsAssert(advertContact);
        contactsDelete();
    }

    private static void contactsAdd() throws Exception {
        AdvertContact advertContact = new AdvertContact();
        advertContact.fillAdvertContactWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertContacts(advertContact), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/contact/add");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        advertContactId = jsonResponse.getJSONObject("data").getInt("advertContact");
    }


    public static AdvertContact contactsEdit() throws Exception {
        AdvertContact advertContactEdit = new AdvertContact();
        advertContactEdit.fillAdvertContactWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertContacts(advertContactEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/contact/" + advertContactId + "/edit");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertContactEdit;
    }


    private static JsonObject initializeJsonAdvertContacts(AdvertContact advertContact) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", advertContact.getStatus().toLowerCase());
        jsonObject.addProperty("person", advertContact.getPerson());
        jsonObject.addProperty("email", advertContact.getEmail());

        JsonArray messengersArray = new JsonArray();

        for (Messenger messenger : advertContact.getMessengers()) {
            JsonObject jsonMessenger = new JsonObject();
            jsonMessenger.addProperty("messengerId", messenger.getMessengerId());
            jsonMessenger.addProperty("value", messenger.getMessengerValue());
            messengersArray.add(jsonMessenger);
        }

        // Добавляем массив messengers в главный JSON-объект
        jsonObject.add("messengers", messengersArray);

        return jsonObject;
    }

    public static ArrayList<AdvertContact> contactsGet() {
        ArrayList<AdvertContact> contactsList = new ArrayList<>();
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get("https://api.admin.3tracks.link/advert/" + advertId + "/contact");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertContact advertContact = new AdvertContact();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertContact.setContactID(dataObject.getInt("id"));
            advertContact.setPerson(dataObject.getString("person"));
            advertContact.setStatus(dataObject.getString("status"));
            advertContact.setEmail(dataObject.getString("email"));

            JSONArray messengersArray = dataObject.getJSONArray("messengers");
            ArrayList<Messenger> messengers = new ArrayList<>();
            for (int j = 0; j < messengersArray.length(); j++) {
                JSONObject messengerObject = messengersArray.getJSONObject(j);
                Messenger messenger = new Messenger();
                messenger.setMessengerId(String.valueOf(messengerObject.getInt("messengerId")));
                messenger.setMessengerValue(messengerObject.getString("value"));
                messengers.add(messenger);
            }
            advertContact.setMessengers(messengers);
            contactsList.add(advertContact);
        }
        return contactsList;
    }

    public static void contactsAssert(AdvertContact advertContactEdit) {
        ArrayList<AdvertContact> advertContactsList = contactsGet();
        for (AdvertContact advertContact : advertContactsList) {
            if (advertContact.getContactID() == advertContactId) {
                System.out.println(advertContact.getContactID());
                Assert.assertEquals(advertContact.getPerson(), advertContactEdit.getPerson());
                Assert.assertEquals(advertContact.getStatus(), advertContactEdit.getStatus());
                Assert.assertEquals(advertContact.getEmail(), advertContactEdit.getEmail());
                System.out.println(advertContact.getMessengers());
                System.out.println(advertContactEdit.getMessengers());

                Assert.assertEquals(advertContact.getMessengers(), advertContactEdit.getMessengers());
            }
        }
    }


    public static void contactsDelete() {
        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .delete("https://api.admin.3tracks.link/advert/" + advertId + "/contact/" + advertContactId);

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ: " + responseBody);
        Assert.assertEquals(responseBody, "{\"success\":true}");
    }
}