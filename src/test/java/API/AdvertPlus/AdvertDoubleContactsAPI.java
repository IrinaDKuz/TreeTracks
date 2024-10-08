package API.AdvertPlus;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertContactDouble;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static API.Advert.AdvertContactsAPI.contactsAddPost;
import static API.Advert.AdvertContactsAPI.contactsEditPost;
import static API.Helper.deleteMethod;
import static AdvertPackage.entity.AdvertContactDouble.fillAdvertContactsDoubleFromBD;
import static Helper.Auth.authApi;
import static SQL.AdvertSQL.*;
import static SQL.DatabaseTest.sqlQueryList;

/***
 Тест ищет контакты совпадающие по полям:
 person
 email
 messengerId + messengerValue
 excludeId
 TODO: тест нестабильный и иногда падает
 */

public class AdvertDoubleContactsAPI {

    @Test
    public static void testAddEmail() throws Exception {
        authApi(103);

        System.out.println("******* Добавляем email *******");
        addEditNewContact("email", false);
    }

    @Test(dependsOnMethods = "testAddEmail", alwaysRun = true)
    public static void testEditEmail() throws Exception {
        System.out.println("******* Редактируем email *******");
        addEditNewContact("email", true);
    }

    @Test(dependsOnMethods = "testEditEmail", alwaysRun = true)
    public static void testAddPerson() throws Exception {
        System.out.println("******* Добавляем person *******");
        addEditNewContact("person", false);
    }

    @Test(dependsOnMethods = "testAddPerson", alwaysRun = true)
    public static void testEditPerson() throws Exception {
        System.out.println("******* Редактируем person *******");
        addEditNewContact("person", true);
    }

    @Test(dependsOnMethods = "testEditPerson", alwaysRun = true)
    public static void testAddMessengers() throws Exception {
        System.out.println("******* Добавляем messenger *******");
        addEditNewContactMessengers(false);
    }

    @Test(dependsOnMethods = "testAddMessengers", alwaysRun = true)
    public static void testEditMessengers() throws Exception {
        System.out.println("******* Редактируем messenger *******");
        addEditNewContactMessengers(true);
    }

    @Test(dependsOnMethods = "testEditMessengers", alwaysRun = true)
    public static void testEditContact1() throws Exception {
        System.out.println("******* Редактируем контакт1 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id1", "advert1", true);
    }

    @Test(dependsOnMethods = "testEditContact1", alwaysRun = true)
    public static void testEditContact2() throws Exception {
        System.out.println("******* Редактируем контакт2 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id2", "advert2", true);
    }

    @Test(dependsOnMethods = "testEditContact2", alwaysRun = true)
    public static void testDeleteContact1() throws Exception {
        System.out.println("******* Удаляем контакт1 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id1", "advert1", false);
    }

    @Test(dependsOnMethods = "testDeleteContact1", alwaysRun = true)
    public static void testDeleteContact2() throws Exception {
        System.out.println("******* Удаляем контакт2 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id2", "advert2", false);
    }


    @Test(dependsOnMethods = "testDeleteContact2", alwaysRun = true)
    public static void testEditContact1WithMessengers() throws Exception {
        System.out.println("******* Редактируем контакт1 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id1", "contact_id1", "advert1", true);
    }

    @Test(dependsOnMethods = "testEditContact1WithMessengers", alwaysRun = true)
    public static void testEditContact2WithMessengers() throws Exception {
        System.out.println("******* Редактируем контакт2 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id2", "contact_id2", "advert2", true);
    }

    @Test(dependsOnMethods = "testEditContact2WithMessengers", alwaysRun = true)
    public static void testDeleteContact1WithMessengers() throws Exception {
        System.out.println("******* Удаляем контакт1 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id1", "contact_id1", "advert1", false);
    }

    @Test(dependsOnMethods = "testDeleteContact1WithMessengers", alwaysRun = true)
    public static void testDeleteContact2WithMessengers() throws Exception {
        System.out.println("******* Удаляем контакт2 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id2", "contact_id2", "advert2", false);
    }


    private static void addEditNewContact(String parameterName, Boolean isEdit) throws Exception {
        // Записываем список объектов из базы
        List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
        String value = getRandomValueFromBDWhereNotNull(parameterName, "advert_contact", parameterName);

        System.out.println("Было всего записей: " + advertContactDoublesFromBD.size());
        Allure.step("Было всего записей: " + advertContactDoublesFromBD.size());
        System.out.println("ParameterName = " + parameterName);
        Allure.step("ParameterName = " + parameterName);
        System.out.println("ParameterValue = " + value);
        Allure.step("ParameterValue = " + value);


        List<String> contactIds = getArrayFromBDWhereOrderBy("id", "advert_contact",
                parameterName, value, "id");

        Map<String, String> contactVSAdvertMap = new LinkedHashMap<>();
        for (String contactId : contactIds) {
            contactVSAdvertMap.put(contactId, getValueFromBDWhere("advert_id", "advert_contact",
                    "id", contactId));
        }

        List<String> exceptValuesList = new ArrayList<>(contactVSAdvertMap.values());

        String randomAdvert2 = null;
        String randomAdvert3 = null;

        AdvertContact advertContact = new AdvertContact();
        advertContact.fillAdvertContactWithRandomUniqueData();

        if (parameterName.equals("person"))
            advertContact.setPerson(value);
        if (parameterName.equals("email"))
            advertContact.setEmail(value);

        String duplicatorContactId;
        String duplicatorAdvertId1;
        if (isEdit) {
            duplicatorAdvertId1 = getRandomValueFromBDExcept("advert_id", "advert_contact",
                    exceptValuesList);
            duplicatorContactId = getRandomValueFromBDWhere("id", "advert_contact",
                    "advert_id", duplicatorAdvertId1);
            editContact(duplicatorAdvertId1, duplicatorContactId, advertContact);
        } else {
            duplicatorAdvertId1 = getRandomValueFromBDExcept("id", "advert",
                    exceptValuesList);
            duplicatorContactId = String.valueOf(
                    addContact(duplicatorAdvertId1, advertContact));
        }

        for (String contactId : contactIds) {

            System.out.println("Новая строка: " + duplicatorAdvertId1 + " " +
                    contactVSAdvertMap.get(contactId) + " " +
                    duplicatorContactId + " " +
                    contactId + " " + parameterName);

            Allure.step("Новая строка: " + duplicatorAdvertId1 + " " +
                    contactVSAdvertMap.get(contactId) + " " +
                    duplicatorContactId + " " +
                    contactId + " " + parameterName);

            AdvertContactDouble advertContactDouble =
                    new AdvertContactDouble(parameterName, duplicatorAdvertId1, contactVSAdvertMap.get(contactId),
                            String.valueOf(duplicatorContactId), contactId, "null", "null");
            advertContactDoublesFromBD.add(advertContactDouble);
        }
        System.out.println("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());
        Allure.step("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());

        List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
        System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());
        Allure.step("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());

        assertContactDoubleBD(advertContactDoublesFromBD, advertContactDoublesFromBDAfter);
    }

    private static void addEditNewContactMessengers(Boolean isEdit) {
        // Записываем список объектов из базы
        try {
            List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
            String messengerId = getRandomValueFromBD("id", "advert_contact_messenger");
            String value = getRandomValueFromBDWhere("value", "advert_contact_messenger",
                    "id", messengerId);

            String messengerType = getRandomValueFromBDWhere("messenger_id", "advert_contact_messenger",
                    "id", messengerId);


            System.out.println("Было всего записей: " + advertContactDoublesFromBD.size());
            Allure.step("Было всего записей: " + advertContactDoublesFromBD.size());
            System.out.println("ParameterType = " + messengerType);
            Allure.step("ParameterType = " + messengerType);
            System.out.println("ParameterValue = " + value);
            Allure.step("ParameterValue = " + value);
            System.out.println("MessengerId = " + messengerId);
            Allure.step("MessengerId = " + messengerId);


            List<String> contactIds = getArrayFromBDWhereAnd("contact_id", "advert_contact_messenger",
                    Map.of("value", value, "messenger_id", messengerType));

            System.out.println("contactIds = " + contactIds);
            Allure.step("contactIds = " + contactIds);

            Map<String, String> contactVSAdvertMap = new LinkedHashMap<>();
            try {
                for (String contactId : contactIds) {
                    contactVSAdvertMap.put(contactId, getValueFromBDWhere("advert_id", "advert_contact",
                            "id", contactId));
                }
            } catch (NoSuchElementException e) {
                System.err.println("Заново. Это происходит, если в таблице есть мессенджер который принадлежит удаленному Адверту" +
                        "Нужно удалить мессенджер" + messengerId + " или контакт из " + contactIds);
                System.err.println(e);
            }

            List<String> exceptValuesList = new ArrayList<>(contactVSAdvertMap.values());

            AdvertContact advertContact = new AdvertContact();
            advertContact.fillAdvertContactWithRandomUniqueData();

            Random random = new Random();
            int randomIndex = random.nextInt(advertContact.getMessengers().size());
            advertContact.getMessengers().get(randomIndex).setMessengerValue(value);
            advertContact.getMessengers().get(randomIndex).setMessengerTypeId(messengerType);

            String duplicatorContactId;
            String duplicatorAdvertId1;
            if (isEdit) {
                duplicatorAdvertId1 = getRandomValueFromBDExcept("advert_id", "advert_contact",
                        exceptValuesList);
                duplicatorContactId = getRandomValueFromBDWhere("id", "advert_contact",
                        "advert_id", duplicatorAdvertId1);
                editContact(duplicatorAdvertId1, duplicatorContactId, advertContact);

            } else {
                duplicatorAdvertId1 = getRandomValueFromBDExcept("id", "advert",
                        exceptValuesList);
                duplicatorContactId = String.valueOf(addContact(duplicatorAdvertId1, advertContact));
            }

            String doubleMessengerId = getArrayFromBDWhereAnd("id", "advert_contact_messenger",
                    Map.of("value", value, "messenger_id", messengerType, "contact_id", duplicatorContactId))
                    .getFirst();

            for (String contactId : contactIds) {
                System.out.println("Новая строка: " + duplicatorAdvertId1 + " " +
                        contactVSAdvertMap.get(contactId) + " " +
                        duplicatorContactId + " " +
                        contactId + " messenger");

                Allure.step("Новая строка: " + duplicatorAdvertId1 + " " +
                        contactVSAdvertMap.get(contactId) + " " +
                        duplicatorContactId + " " +
                        contactId + " messenger");

                AdvertContactDouble advertContactDouble =
                        new AdvertContactDouble("messenger", duplicatorAdvertId1, contactVSAdvertMap.get(contactId),
                                duplicatorContactId, contactId, doubleMessengerId, messengerId);
                advertContactDoublesFromBD.add(advertContactDouble);
            }
            System.out.println("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());
            Allure.step("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());

            List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
            System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());
            Allure.step("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());


            assertContactDoubleBD(advertContactDoublesFromBD, advertContactDoublesFromBDAfter);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void deleteEditContactFromBD(String messengerParameterName,
                                                String contactParameterName,
                                                String advertFieldName,
                                                Boolean isEdit) {
        try {
            // Записываем список объектов из базы
            List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
            String id;
            if (messengerParameterName != null) {
                id = getRandomValueFromBDWhereNotNull("id", "advert_contact_double",
                        messengerParameterName);
                String messengerIdFromBD = getValueFromBDWhere(contactParameterName, "advert_contact_double",
                        "id", id);
                System.out.println("ContactId = " + messengerIdFromBD);
                Allure.step("ContactId = " + messengerIdFromBD);


            } else {
                id = getRandomValueFromBDWhereNotNull("id", "advert_contact_double",
                        contactParameterName);
            }
            String contactIdFromBD = getValueFromBDWhere(contactParameterName, "advert_contact_double",
                    "id", id);
            String advertIdFromBD = getValueFromBDWhere(advertFieldName, "advert_contact_double",
                    contactParameterName, contactIdFromBD);

            System.out.println("Было всего записей: " + advertContactDoublesFromBD.size());
            System.out.println("AdvertID = " + advertIdFromBD);
            System.out.println("ContactId = " + contactIdFromBD);

            Allure.step("Было всего записей: " + advertContactDoublesFromBD.size());
            Allure.step("AdvertID = " + advertIdFromBD);
            Allure.step("ContactId = " + contactIdFromBD);

            // Эти ids будем удалять из таблицы
            List<String> ids = getIdsWhereContactIsOrderById(contactIdFromBD);
            System.out.println("Удалили/изменили записей: " + ids.size());

            advertContactDoublesFromBD.removeIf(advertContactDouble -> ids.contains(advertContactDouble.getId()));
            // И редактируем/удаляем сам контакт
            if (isEdit) {
                AdvertContact advertContact = new AdvertContact();
                advertContact.fillAdvertContactWithRandomUniqueData();
                editContact(advertIdFromBD, contactIdFromBD, advertContact);
            } else {
                deleteContact(advertIdFromBD, contactIdFromBD);
            }

            List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
            System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());
            System.out.println("Стало записей  в локальном массиве: " + advertContactDoublesFromBD.size());

            Allure.step("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());
            Allure.step("Стало записей  в локальном массиве: " + advertContactDoublesFromBD.size());


            assertContactDoubleBD(advertContactDoublesFromBD, advertContactDoublesFromBDAfter);
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    private static void assertContactDoubleBD(List<AdvertContactDouble> advertContactDoublesFromBD,
                                              List<AdvertContactDouble> advertContactDoublesFromBDAfter) {
        SoftAssert softAssert = new SoftAssert();
        if (advertContactDoublesFromBD.size() == advertContactDoublesFromBDAfter.size()) {
            try {
                for (int i = 0; i < advertContactDoublesFromBD.size(); i++) {
                    AdvertContactDouble acd1 = advertContactDoublesFromBD.get(i);
                    AdvertContactDouble acd2 = advertContactDoublesFromBDAfter.get(i);
                    softAssert.assertEquals(acd1.getAdvert1(), acd2.getAdvert1());
                    softAssert.assertEquals(acd1.getAdvert2(), acd2.getAdvert2());
                    softAssert.assertEquals(acd1.getContact_id1(), acd2.getContact_id1());
                    softAssert.assertEquals(acd1.getContact_id2(), acd2.getContact_id2());
                    softAssert.assertEquals(acd1.getField(), acd2.getField());
                    softAssert.assertEquals(acd1.getMessenger_id1(), acd2.getMessenger_id1());
                    softAssert.assertEquals(acd1.getMessenger_id2(), acd2.getMessenger_id2());
                }
                softAssert.assertAll();
            } catch (AssertionError a) {
                System.err.println(a);
            }
        } else {
            for (int i = 0; i < Math.min(advertContactDoublesFromBD.size(), advertContactDoublesFromBDAfter.size()); i++) {
                DiffResult diff = new ReflectionDiffBuilder(advertContactDoublesFromBD.get(i)
                        , advertContactDoublesFromBDAfter.get(i), null)
                        .build();

                for (Object diffEntry : diff.getDiffs()) {
                    System.err.println(diffEntry.toString());
                   Allure.step("Различия строк: " + diffEntry);

                }
            }
            // Вывод элементов, которые есть в первом списке, но отсутствуют во втором
            for (int i = advertContactDoublesFromBD.size(); i < advertContactDoublesFromBDAfter.size(); i++) {
                System.err.println("Запись в БД, отсутствующая в локальном массиве: " + advertContactDoublesFromBDAfter.get(i));
                Allure.step("Запись в БД, отсутствующая в локальном массиве: " +
                        "Адверт1: " + advertContactDoublesFromBDAfter.get(i).getAdvert1() +
                        "Адверт2: " + advertContactDoublesFromBDAfter.get(i).getAdvert2() +
                        "Контакт1: " + advertContactDoublesFromBDAfter.get(i).getContact_id1() +
                        "Контакт2: " + advertContactDoublesFromBDAfter.get(i).getContact_id2());
            }

            // Вывод элементов, которые есть во втором списке, но отсутствуют в первом
            for (int i = advertContactDoublesFromBDAfter.size(); i < advertContactDoublesFromBD.size(); i++) {
                System.err.println("Запись в локальном массиве, отсутствующая в БД: " + advertContactDoublesFromBD.get(i).getAdvert1());
               Allure.step("Запись в локальном массиве, отсутствующая в БД: " +
                       "Адверт1: " + advertContactDoublesFromBD.get(i).getAdvert1() +
                       "Адверт2: " + advertContactDoublesFromBD.get(i).getAdvert2() +
                       "Контакт1: " + advertContactDoublesFromBD.get(i).getContact_id1() +
                       "Контакт2: " + advertContactDoublesFromBD.get(i).getContact_id2());
            }
            Assert.fail();
        }

        System.out.println("New test____________________________");
    }


    private static int addContact(String advertId, AdvertContact advertContact) {
        return contactsAddPost(advertId, advertContact);
    }

    private static void editContact(String advertId, String duplicatorContactId, AdvertContact advertContactEdit) throws
            Exception {
        contactsEditPost(advertId, duplicatorContactId, advertContactEdit);
    }

    private static void deleteContact(String advertId, String advertContactId) {
        deleteMethod("advert", advertId + "/contact/" + advertContactId);
    }

    public static List<String> getIdsWhereContactIsOrderById(String contactId) throws Exception {
        String sqlRequest = "SELECT id FROM advert_contact_double " +
                "WHERE contact_id1 = " + contactId + " OR contact_id2 = " + contactId +
                " ;";
        System.out.println(sqlRequest);
        return sqlQueryList(sqlRequest, "id");
    }
}