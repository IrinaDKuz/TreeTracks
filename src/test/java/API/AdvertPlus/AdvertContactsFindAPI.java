package API.AdvertPlus;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static API.Advert.AdvertFilterAPI.getUrlWithParameters;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;
import static io.restassured.RestAssured.given;

/***
 Тест ищет контакты совпадающие по полям:
 person
 email
 messengerId + messengerValue
 excludeId
 TODO: тест нестабильный и иногда падает
 */

public class AdvertContactsFindAPI {

    static Map<String, String> headers = Map.of(
            "Authorization", authKeyAdmin,
            "Accept", "application/json",
            "Content-Type", "application/json"
    );

    @Test
    public static void test() throws Exception {
        Allure.step("1) Проверки каждого поля отдельно");
        positiveSeparateTest();
        Allure.step("2) Проверки полей в совокупности по одному Адверту");
        positiveCombineTest();
        Allure.step("3) Проверки полей в совокупности данные из полей у разных Адвертов");
        negativeCombineTest();
        Allure.step("4) Проверки exclude");
        excludeTest();
    }

    public static void positiveSeparateTest() throws Exception {
        separateTest("person");
        separateTest("email");
        separateMessengerTest();
    }

    public static void positiveCombineTest() throws Exception {
        String messengerId = getRandomValueFromBD("id", "advert_contact_messenger");
        String messengerTypeId = getValueFromBDWhere("messenger_id", "advert_contact_messenger",
                "id", messengerId);
        String messengerValue = getValueFromBDWhere("value", "advert_contact_messenger",
                "id", messengerId);
        String contactId = getValueFromBDWhere("contact_id", "advert_contact_messenger",
                "id", messengerId);
        String person = getRandomValueFromBDWhere("person", "advert_contact",
                "id", contactId);
        String email = getRandomValueFromBDWhere("email", "advert_contact",
                "id", contactId);

        combineTest(person, email, messengerTypeId, messengerValue);
    }

    public static void negativeCombineTest() throws Exception {
        String messengerTypeId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
        String messengerValue = getRandomValueFromBD("value", "advert_contact_messenger");
        String person = getRandomValueFromBD("person", "advert_contact");
        String email = getRandomValueFromBD("email", "advert_contact");
        combineTest(person, email, messengerTypeId, messengerValue);

        messengerTypeId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
        messengerValue = getRandomValueFromBDWhere("value", "advert_contact_messenger",
                "messenger_id", messengerTypeId);
        person = getRandomValueFromBD("person", "advert_contact");
        email = getRandomValueFromBD("email", "advert_contact");
        combineTest(person, email, messengerTypeId, messengerValue);
    }

    public static void excludeTest() throws Exception {
        separateWithExcludeTest("person");
        separateWithExcludeTest("email");
        separateMessengerTest();
    }

    public static void combineTest(String person, String email, String messengerTypeId, String messengerValue) throws Exception {
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
        System.out.println("               messengerId, значение: " + messengerTypeId);
        System.out.println("               messengerValue, значение: " + messengerValue);

        Allure.step("Ищем по совокупности полей: person = " + person +
                ", email = " + email + ", messengerId = " + messengerTypeId +
                ", messengerValue = " + messengerValue);

        List<Integer> personArray = findContact(Map.of("person", person, "email", email,
                "messengerId", messengerTypeId, "messengerValue", messengerValue));
        List<Integer> personArrayFromBDInt = personArrayFromBD.stream()
                .map(Integer::parseInt)
                .toList();

        Set<Integer> uniquePersonArrayFromBDInt = new LinkedHashSet<>(personArrayFromBDInt);
        List<Integer> personArrayFromBDIntList = new ArrayList<>(uniquePersonArrayFromBDInt);
        Collections.sort(personArray);
        Collections.sort(personArrayFromBDIntList);
        Assert.assertEquals(personArray, personArrayFromBDIntList);
    }

    private static void separateTest(String field) throws Exception {
        String fieldValue = getRandomValueFromBD(field, "advert_contact");
        System.out.println("Ищем по полю: " + field + ", значение: " + fieldValue);
        Allure.step("Ищем по полю: " + field + ", значение: " + fieldValue);
        List<Integer> personArray = findContact(Map.of(field, fieldValue));
        List<Integer> personArrayFromBD = getArrayFromBDWhere("advert_id", "advert_contact",
                field, fieldValue)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Collections.sort(personArray);
        Collections.sort(personArrayFromBD);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + ", id Адвертов из БД: " + personArrayFromBD);
        Assert.assertEquals(personArray, personArrayFromBD);
    }

    private static void separateWithExcludeTest(String field) throws Exception {
        String fieldValue = getFrequentValueFromBD(field, "advert_contact");
        System.out.println("Ищем по полю: " + field + ", значение: " + fieldValue);
        Allure.step("Ищем по полю: " + field + ", значение: " + fieldValue);
        List<String> ids = getArrayFromBDWhere("advert_id", "advert_contact", field, fieldValue);
        // Исключаем все
        System.out.println(ids);

        List<Integer> personArray = findContactExclude(Map.of(field, fieldValue, "excludeId[]", ids));
        Assert.assertEquals(personArray, Collections.emptyList());

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
        Collections.sort(personArray);
        Collections.sort(personArrayFromBDWithoutId);
        System.out.println(personArray);
        System.out.println(personArrayFromBDWithoutId);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDWithoutId);
        Assert.assertEquals(personArray, personArrayFromBDWithoutId);
    }

    private static void separateMessengerTest() throws Exception {
        String messengerId = getRandomValueFromBD("messenger_id", "advert_contact_messenger");
        String messengerValue = getRandomValueFromBDWhere("value", "advert_contact_messenger",
                "messenger_id", messengerId);

        System.out.println("Ищем по полям: messenger_id значение: " + messengerId);
        System.out.println("Ищем по полям: messenger_value значение: " + messengerValue);
        Allure.step("Ищем по полям: messenger_id = " + messengerId +
                " и messenger_value = " + messengerValue);


        List<Integer> personArray = findContact(Map.of("messengerId", messengerId, "messengerValue", messengerValue));
        List<String> contactArrayFromBD = getArrayFromBDWhereAnd("contact_id", "advert_contact_messenger",
                Map.of("messenger_id", messengerId, "value", messengerValue));

        List<String> personArrayFromBD = getArrayFromBDWhere("advert_id", "advert_contact",
                "id", contactArrayFromBD);
        List<Integer> personArrayFromBDInt = new ArrayList<>(personArrayFromBD.stream()
                .map(Integer::parseInt)
                .toList());
        Collections.sort(personArray);
        Collections.sort(personArrayFromBDInt);
        System.out.println(personArray);
        System.out.println(personArrayFromBDInt);
        Allure.step("id Адвертов из ответа на запрос : " + personArray + " id Адвертов из БД: " + personArrayFromBDInt);
        Assert.assertEquals(personArray, personArrayFromBDInt);
    }

    public static List<Integer> findContact(Map<String, String> params) {
        RequestSpecification request = given()
                .contentType(ContentType.URLENC);
        headers.forEach(request::header);
        params.forEach(request::param);

        Response response = request.when()
                .get("https://api.admin.3tracks.link/advert/contact/find");

        String responseBody = response.getBody().asString();
        Allure.step("Ответ на get: " + responseBody);
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
                .header("Authorization", authKeyAdmin)
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