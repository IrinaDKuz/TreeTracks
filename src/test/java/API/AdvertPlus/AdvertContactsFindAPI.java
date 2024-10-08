package API.AdvertPlus;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static API.Advert.AdvertFilterAPI.getUrlWithParameters;
import static Helper.AllureHelper.*;
import static Helper.Auth.*;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест ищет контакты совпадающие по полям:
 person
 email
 messengers [messengerId, value]
 excludeId
 TODO: messengers + тест нестабильный и иногда падает


 NB! Тест из-за особенностей мапы не умеет искать такие значения:
 Ищем по полям: messengers[][messengerId] = 108 и messengers[][value] = Bougainvillea0815093027381
 Ищем по полям: messengers[][messengerId] = 103 и messengers[][value] = Campanula0813170123490
 Ищем по полям: messengers[][messengerId] = 103 и messengers[][value] = Echinacea0813181935575
 и перезаписывает значения с одинаковым id, такие кейсы нужно прогонять вручную


 Нужно ли будет искать пустые email?
 */

public class AdvertContactsFindAPI {

    static Map<String, String> headers = Map.of(
            "Authorization", KEY,
            "Accept", "application/json",
            "Content-Type", "application/json"
    );

    @Test
    public static void test() throws Exception {
        authApi(103);
        SoftAssert softAssert = new SoftAssert();
        Allure.step("1) Проверки каждого поля отдельно");
        System.out.println("1) Проверки каждого поля отдельно");
        positiveSeparateTest(softAssert);
        Allure.step("2) Проверки полей в совокупности по одному Адверту");
        System.out.println("2) Проверки полей в совокупности по одному Адверту");
        positiveCombineTest(softAssert);
        Allure.step("3) Проверки полей в совокупности данные из полей у разных Адвертов");
        System.out.println("3) Проверки полей в совокупности данные из полей у разных Адвертов");
        negativeCombineTest(softAssert);
        Allure.step("4) Проверки exclude");
        System.out.println("4) Проверки exclude");
        excludeTest(softAssert);
        softAssert.assertAll();
    }

    public static void positiveSeparateTest(SoftAssert softAssert) throws Exception {
        separateTest("person", softAssert);
        separateTest("email", softAssert);
        separateMessengerTest(softAssert);
    }

    public static void positiveCombineTest(SoftAssert softAssert) throws Exception {
        String messengerId = getRandomValueFromBD("id", "advert_contact_messenger");
        System.out.println("messengerId = " + messengerId);
        String messengerTypeId = getValueFromBDWhere("messenger_id", "advert_contact_messenger",
                "id", messengerId);
        String messengerValue = getValueFromBDWhere("value", "advert_contact_messenger",
                "id", messengerId);
        String contactId = getValueFromBDWhere("contact_id", "advert_contact_messenger",
                "id", messengerId);
        System.out.println("contactId = " + contactId);
        String person = getRandomValueFromBDWhere("person", "advert_contact",
                "id", contactId);
        String email = getRandomValueFromBDWhere("email", "advert_contact",
                "id", contactId);

        combineTest(person, email, messengerTypeId, messengerValue, softAssert);
    }

    public static void negativeCombineTest(SoftAssert softAssert) throws Exception {
        Allure.step("3.1) messengerTypeId и messengerValue из разных контактов и несовпадают");
        System.out.println("3.1) messengerTypeId и messengerValue из разных контактов и несовпадают");
        String messengerTypeId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
        String messengerValue = getRandomValueFromBD("value", "advert_contact_messenger");
        String person = getRandomValueFromBD("person", "advert_contact");
        String email = getRandomValueFromBD("email", "advert_contact");
        combineTest(person, email, messengerTypeId, messengerValue, softAssert);

        Allure.step("3.2) messengerTypeId и messengerValue из одного мессенджера и совпадают");
        System.out.println("3.2) messengerTypeId и messengerValue из одного мессенджера и совпадают");

        messengerTypeId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
        messengerValue = getRandomValueFromBDWhere("value", "advert_contact_messenger",
                "messenger_id", messengerTypeId);
        person = getRandomValueFromBD("person", "advert_contact");
        email = getRandomValueFromBD("email", "advert_contact");
        combineTest(person, email, messengerTypeId, messengerValue, softAssert);
    }

    public static void excludeTest(SoftAssert softAssert) throws Exception {
        separateWithExcludeTest("person", softAssert);
        separateWithExcludeTest("email", softAssert);
        separateMessengerTest(softAssert);
    }

    public static void combineTest(String person, String email, String messengerTypeId, String messengerValue,
                                   SoftAssert softAssert) throws Exception {
        List<String> contactArrayFromBD = getArrayFromBDWhereAnd("contact_id", "advert_contact_messenger",
                Map.of("messenger_id", messengerTypeId, "value", messengerValue));
        List<String> personArrayFromBD = new ArrayList<>();
        if (contactArrayFromBD.isEmpty())
            personArrayFromBD.addAll(getArrayFromBDWhereOr("advert_id", "advert_contact",
                    Map.of("person", person, "email", email)));
        else {
            for (String contactFromBD : contactArrayFromBD)
                personArrayFromBD.addAll(getArrayFromBDWhereOr("advert_id", "advert_contact",
                        Map.of("person", person, "email", email, "id", contactFromBD)));
        }

        System.out.println("Ищем по полям: person, значение: " + person);
        System.out.println("               email, значение: " + email);
        System.out.println("               messengers[0][messengerId], значение: " + messengerTypeId);
        System.out.println("               messengers[0][value], значение: " + messengerValue);

        Allure.step("Ищем по совокупности полей: person = " + person +
                ", email = " + email + ", messengers[0][messengerId] = " + messengerTypeId +
                ", messengers[0][value] = " + messengerValue);

        List<Integer> personArray = findContact(Map.of("person", person, "email", email,
                "messengers[0][messengerId]", messengerTypeId, "messengers[0][value]", messengerValue));
        List<Integer> personArrayFromBDInt = personArrayFromBD.stream()
                .map(Integer::parseInt)
                .toList();

        Set<Integer> uniquePersonArrayFromBDInt = new LinkedHashSet<>(personArrayFromBDInt);
        List<Integer> personArrayFromBDIntList = new ArrayList<>(uniquePersonArrayFromBDInt);

        if (!personArray.isEmpty() && !personArrayFromBDIntList.isEmpty()) {
            Collections.sort(personArray);
            Collections.sort(personArrayFromBDIntList);
        }
        System.out.println("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDIntList);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDIntList);
        softAssert.assertEquals(personArray, personArrayFromBDIntList);
    }

    private static void separateTest(String field, SoftAssert softAssert) throws Exception {
        String fieldValue = getFrequentValueFromBDNotNull(field, "advert_contact");
        System.out.println("Ищем по полю: " + field + ", значение: " + fieldValue);
        Allure.step("Ищем по полю: " + field + ", значение: " + fieldValue);
        List<Integer> personArray = findContact(Map.of(field, fieldValue));
        List<Integer> personArrayFromBD = getArrayFromBDWhere("advert_id", "advert_contact",
                field, fieldValue)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (!personArray.isEmpty() && !personArrayFromBD.isEmpty()) {
            Collections.sort(personArray);
            Collections.sort(personArrayFromBD);
        }
        System.out.println("id Адвертов из ответа на запрос : " + personArray + ", id Адвертов из БД: " + personArrayFromBD);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + ", id Адвертов из БД: " + personArrayFromBD);
        softAssert.assertEquals(personArray, personArrayFromBD);
    }

    private static void separateWithExcludeTest(String field, SoftAssert softAssert) throws Exception {
        String fieldValue = getFrequentValueFromBDNotNull(field, "advert_contact");
        System.out.println("Ищем по полю: " + field + ", значение: " + fieldValue);
        Allure.step("Ищем по полю: " + field + ", значение: " + fieldValue);
        List<String> ids = getArrayFromBDWhere("advert_id", "advert_contact", field, fieldValue);
        // Исключаем все
        System.out.println(ids);

        List<Integer> personArray = findContactExclude(Map.of(field, fieldValue, "excludeId[]", ids));

        System.out.println("Проверяем, что пустой массив : " + personArray);
        Allure.step("Проверяем, что пустой массив : " + personArray);
        softAssert.assertEquals(personArray, Collections.emptyList());

        // Исключаем несколько рандомно
        Random random = new Random();
        List<String> idsToExclude = ids.stream()
                .skip(random.nextInt(ids.size()))
                .limit(random.nextInt(ids.size()))
                .toList();

        Allure.step("Исключаем: " + idsToExclude);

        personArray = findContactExclude(Map.of(field, fieldValue, "excludeId[]", idsToExclude));
        List<Integer> personArrayFromBD = getArrayFromBDWhere("advert_id", "advert_contact",
                field, fieldValue)
                .stream()
                .map(Integer::parseInt)
                .toList();

        Allure.step("Весь список: " + personArrayFromBD);
        Allure.step("Исключаем: " + idsToExclude);

        List<Integer> personArrayFromBDWithoutId = new ArrayList<>(personArrayFromBD);
        personArrayFromBDWithoutId.removeAll(idsToExclude.stream().map(Integer::parseInt).toList());
        if (!personArray.isEmpty() && !personArrayFromBDWithoutId.isEmpty()) {
            Collections.sort(personArray);
            Collections.sort(personArrayFromBDWithoutId);
        }
        System.out.println("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDWithoutId);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDWithoutId);
        softAssert.assertEquals(personArray, personArrayFromBDWithoutId);
    }

    private static void separateMessengerTest(SoftAssert softAssert) throws Exception {
        int count = new Random().nextInt(5) + 1;
        Map<String, String> messengerMap = new HashMap<>();
        for (int i = 0; i < count; i++) {

            String messengerId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
            String messengerValue = getRandomValueFromBDWhere("value", "advert_contact_messenger",
                    "messenger_id", messengerId);

            System.out.println("Ищем по полям: messengers[0][messengerId] = " + messengerId +
                    " и messengers[0][value] = " + messengerValue);

            Allure.step("Ищем по полям: messengers[0][messengerId] = " + messengerId +
                    " и messengers[0][value] = " + messengerValue);

            messengerMap.put(messengerId, messengerValue);
        }

        List<String> contactArrayFromBD = getContactArrayFromBD(messengerMap);
        List<Integer> personArrayFromRequest = getPersonArrayFromRequest(messengerMap);

        List<String> personArrayFromBD = getArrayFromBDWhere("advert_id", "advert_contact",
                "id", contactArrayFromBD);
        List<Integer> personArrayFromBDInt = new ArrayList<>(personArrayFromBD.stream()
                .map(Integer::parseInt)
                .toList());

        if (!personArrayFromRequest.isEmpty() && !personArrayFromBDInt.isEmpty()) {
            Collections.sort(personArrayFromRequest);
            Collections.sort(personArrayFromBDInt);
        }
        System.out.println("id Адвертов из ответа на запрос : " + personArrayFromRequest + " id Адвертов из БД: " + personArrayFromBDInt);
        Allure.step("id Адвертов из ответа на запрос : " + personArrayFromRequest + " id Адвертов из БД: " + personArrayFromBDInt);
        softAssert.assertEquals(personArrayFromRequest, personArrayFromBDInt);
    }


    private static List<String> getContactArrayFromBD(Map<String, String> messengerMap) throws Exception {
        List<String> contactArrayFromBD = new ArrayList<>();
        for (Map.Entry<String, String> entry : messengerMap.entrySet()) {
            contactArrayFromBD.addAll(getArrayFromBDWhereAnd(
                    "contact_id",
                    "advert_contact_messenger",
                    Map.of("messenger_id", entry.getKey(), "value", entry.getValue())
            ));
        }
        return contactArrayFromBD;
    }

    private static List<Integer> getPersonArrayFromRequest(Map<String, String> messengerMap) throws Exception {
        Map<String, String> messengerMapForFind = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, String> entry : messengerMap.entrySet()) {
            System.out.println("messengers[" + String.valueOf(i) + "][messengerId]");
            messengerMapForFind.put("messengers[" + String.valueOf(i) + "][messengerId]", entry.getKey());
            messengerMapForFind.put("messengers[" + String.valueOf(i) + "][value]", entry.getValue());
            i++;
        }
        return findContact(messengerMapForFind);
    }

    public static List<Integer> findContact(Map<String, String> params) {
        RequestSpecification request = given()
                .contentType(ContentType.URLENC);
        headers.forEach(request::header);
        System.out.println(params);
        params.forEach(request::param);

        Response response = request.when()
                .get("https://api.admin.3tracks.link/advert/contact/find");

        String responseBody = response.getBody().asString();
        Allure.step("Ответ на get: " + responseBody);
        System.out.println("Ответ на get: " + responseBody);
        attachJson(responseBody, GET_RESPONSE);

        JsonPath jsonPath = new JsonPath(responseBody);
        if (jsonPath.get("data.advert.value") != null) {
            return jsonPath.getList("data.advert.value");
        } else {
            return List.of();
        }
    }

    public static List<Integer> findContactExclude(Map<String, Object> params) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get(getUrlWithParameters("https://api.admin.3tracks.link/advert/contact/find?", params));

        String responseBody = response.getBody().asString();
        System.out.println("Ответ на get: " + responseBody);
        Allure.step("Ответ на get: " + responseBody);

        JsonPath jsonPath = new JsonPath(responseBody);
        if (jsonPath.get("data.advert.value") != null) {
            return jsonPath.getList("data.advert.value");
        } else {
            return List.of();
        }

    }
}