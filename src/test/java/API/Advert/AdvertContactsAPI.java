package API.Advert;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertContact.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.List;
import java.util.NoSuchElementException;

import static API.Helper.deleteMethod;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

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
        advertId = Integer.parseInt(getRandomValueFromBDWhereMore("id", "advert", "id", "1000"));
        Allure.step("Получаем контакты у рандомного Адверта " + advertId);
        contactsGet(true);
        Allure.step("Добавляем контакт и мессенджеры");
        contactsAdd();
        Allure.step("Редактируем контакт и мессенджеры");
        AdvertContact advertContact = contactsEdit();
        Allure.step("Выполняем проверки");
        contactsAssert(advertContact);
        Allure.step("Удаляем контакт " + advertContactId);
        deleteMethod("advert",advertId + "/contact/" + advertContactId);
    }

    private static void contactsAdd() throws Exception {
        AdvertContact advertContact = new AdvertContact();
        advertContact.fillAdvertContactWithRandomData();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertContacts(advertContact), JsonObject.class);
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
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/contact/add");

        // Получаем и выводим ответ
        String responseBody = response.getBody().asString();
        System.out.println("Ответ на add: " + responseBody);
        Allure.step("Ответ на add: " + responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        advertContactId = jsonResponse.getJSONObject("data").getInt("advertContact");
    }


    public static AdvertContact contactsEdit() throws Exception {
        AdvertContact advertContactEdit = new AdvertContact();
        advertContactEdit.fillAdvertContactWithRandomData();

        List<String> messengerIds = getArrayFromBDWhere("id", "advert_contact_messenger",
                "contact_id", String.valueOf(advertContactId));


        for (int i = 0; i < messengerIds.size(); i++) {
            try {
                advertContactEdit.getMessengers().get(i).setMessengerId(messengerIds.get(i));
            } catch (Exception e) {
                System.err.println(e);
                for (int j = 0; j < advertContactEdit.getMessengers().size(); j++) {
                    try {
                        advertContactEdit.getMessengers().get(j).setMessengerId(messengerIds.get(j));
                    } catch (Exception r) {
                        System.err.println(r);
                    }
                }
            }
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(initializeJsonAdvertContacts(advertContactEdit), JsonObject.class);
        System.out.println(jsonObject.toString().replace("],", "],\n"));
        Allure.step("Данные для отправки: \n " + jsonObject.toString().replace("],", "],\n"));

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .post("https://api.admin.3tracks.link/advert/" + advertId + "/contact/" + advertContactId + "/edit");

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на edit: " + responseBody);
        Allure.step("Ответ на edit: " + responseBody);

        Assert.assertEquals(responseBody, "{\"success\":true}");
        return advertContactEdit;
    }

    private static JsonObject initializeJsonAdvertContacts(AdvertContact advertContact) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", advertContact.getStatus().toLowerCase());
        jsonObject.addProperty("person", advertContact.getPerson());
        jsonObject.addProperty("email", advertContact.getEmail());
        jsonObject.addProperty("position", advertContact.getPosition());

        JsonArray messengersArray = new JsonArray();

        for (Messenger messenger : advertContact.getMessengers()) {
            JsonObject jsonMessenger = new JsonObject();
            if (messenger.getMessengerId() != null) {
                jsonMessenger.addProperty("id", messenger.getMessengerId());
            }
            jsonMessenger.addProperty("messengerId", messenger.getMessengerTypeId());
            jsonMessenger.addProperty("value", messenger.getMessengerValue());
            messengersArray.add(jsonMessenger);
        }
        jsonObject.add("messengers", messengersArray);
        return jsonObject;
    }

    public static ArrayList<AdvertContact> contactsGet(Boolean isShow) {
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
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            AdvertContact advertContact = new AdvertContact();
            JSONObject dataObject = dataArray.getJSONObject(i);
            advertContact.setContactID(dataObject.getInt("id"));
            advertContact.setPerson(dataObject.getString("person"));
            advertContact.setStatus(dataObject.getString("status"));
            advertContact.setEmail(dataObject.getString("email"));
            advertContact.setPosition(dataObject.isNull("position") ? null : dataObject.getString("position"));

            JSONArray messengersArray = dataObject.getJSONArray("messengers");
            ArrayList<Messenger> messengers = new ArrayList<>();
            for (int j = 0; j < messengersArray.length(); j++) {
                JSONObject messengerObject = messengersArray.getJSONObject(j);
                Messenger messenger = new Messenger();
                messenger.setMessengerId(String.valueOf(messengerObject.getInt("id")));
                messenger.setMessengerTypeId(String.valueOf(messengerObject.getInt("messengerId")));
                messenger.setMessengerValue(messengerObject.getString("value"));
                messengers.add(messenger);
            }
            advertContact.setMessengers(messengers);
            contactsList.add(advertContact);
        }
        return contactsList;
    }

    public static void contactsAssert(AdvertContact advertContactEdit) {
        ArrayList<AdvertContact> advertContactsList = contactsGet(true);
        for (AdvertContact advertContact : advertContactsList) {
            if (advertContact.getContactID() == advertContactId) {
                System.out.println(advertContact.getContactID());
                Assert.assertEquals(advertContact.getPerson(), advertContactEdit.getPerson());
                Assert.assertEquals(advertContact.getStatus(), advertContactEdit.getStatus());
                Assert.assertEquals(advertContact.getEmail(), advertContactEdit.getEmail());
                Assert.assertEquals(advertContact.getPosition(), advertContactEdit.getPosition());

                for (int i = 0; i < advertContactEdit.getMessengers().size(); i++) {
                    Messenger getMessenger = advertContact.getMessengers().get(i);
                    Messenger editMessenger = advertContactEdit.getMessengers().get(i);
                    Assert.assertEquals(getMessenger.getMessengerTypeId(), editMessenger.getMessengerTypeId());
                    Assert.assertEquals(getMessenger.getMessengerValue(), editMessenger.getMessengerValue());
                }
            }
        }
    }
}