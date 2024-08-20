package API.AdvertPlus;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertContactDouble;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static API.Advert.AdvertContactsAPI.contactsAddPost;
import static API.Advert.AdvertContactsAPI.contactsEditPost;
import static API.Helper.deleteMethod;
import static AdvertPackage.entity.AdvertContactDouble.fillAdvertContactsDoubleFromBD;
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
    public static void test() throws Exception {

        System.out.println("******* Добавляем email *******");
        addEditNewContact("email", false);

        System.out.println("******* Редактируем email *******");
        addEditNewContact("email", true);

        System.out.println("******* Добавляем person *******");
        addEditNewContact("person", false);

        System.out.println("******* Редактируем person *******");
        addEditNewContact("person", true);

        System.out.println("******* Добавляем messenger *******");
        addEditNewContactMessengers(false);

        System.out.println("******* Добавляем messenger 2 *******");
        addEditNewContactMessengers(false);

        System.out.println("******* Редактируем messenger *******");
        addEditNewContactMessengers(true);

        System.out.println("******* Редактируем контакт1 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id1", "advert1", true);
        System.out.println("******* Редактируем контакт2 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id2", "advert2", true);
        System.out.println("******* Удаляем контакт1 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id1", "advert1", false);
        System.out.println("******* Удаляем контакт2 из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD(null, "contact_id2", "advert2", false);

        System.out.println("******* Редактируем контакт1 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id1", "contact_id1", "advert1", true);
        System.out.println("******* Редактируем контакт2 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id2", "contact_id2", "advert2", true);
        System.out.println("******* Удаляем контакт1 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id1", "contact_id1", "advert1", false);
        System.out.println("******* Удаляем контакт2 с мессенджером из базы и проверяем, что удалился из базы *******");
        deleteEditContactFromBD("messenger_id2", "contact_id2", "advert2", false);
    }

    private static void addEditNewContact(String parameterName, Boolean isEdit) throws Exception {
        // Записываем список объектов из базы
        List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
        String value = getRandomValueFromBDWhereNotNull(parameterName, "advert_contact", parameterName);

        System.out.println("Было всего записей: " + advertContactDoublesFromBD.size());
        System.out.println("ParameterName = " + parameterName);
        System.out.println("ParameterValue = " + value);

        List<String> contactIds = getArrayFromBDWhereOrderBy("id", "advert_contact",
                parameterName, value, "id");

        Map<String, String> contactVSAdvertMap = new LinkedHashMap<>();
        for (String contactId : contactIds) {
            contactVSAdvertMap.put(contactId, getValueFromBDWhere("advert_id", "advert_contact",
                    "id", contactId));
        }

        // берем Адверта1 у которого нет контактов
        // берем Адверта2 у которого есть 1 контакт
        // берем Адверта3 у которого есть больше 2х контактов

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

            AdvertContactDouble advertContactDouble =
                    new AdvertContactDouble(parameterName, duplicatorAdvertId1, contactVSAdvertMap.get(contactId),
                            String.valueOf(duplicatorContactId), contactId, "null", "null");
            advertContactDoublesFromBD.add(advertContactDouble);
        }
        System.out.println("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());

        List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
        System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());

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
            System.out.println("ParameterValue = " + value);
            System.out.println("ParameterType = " + messengerType);
            System.out.println("MessengerId = " + messengerId);

            List<String> contactIds = getArrayFromBDWhereAnd("contact_id", "advert_contact_messenger",
                    Map.of("value", value, "messenger_id", messengerType));

            System.out.println("contactIds = " + contactIds);

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

                AdvertContactDouble advertContactDouble =
                        new AdvertContactDouble("messenger", duplicatorAdvertId1, contactVSAdvertMap.get(contactId),
                                duplicatorContactId, contactId, doubleMessengerId, messengerId);
                advertContactDoublesFromBD.add(advertContactDouble);
            }
            System.out.println("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());

            List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
            System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());

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
            } catch (
                    AssertionError a) {
                System.err.println(a);
            }
        } else {
            for (int i = 0; i < Math.min(advertContactDoublesFromBD.size(), advertContactDoublesFromBDAfter.size()); i++) {
                DiffResult diff = new ReflectionDiffBuilder(advertContactDoublesFromBD.get(i)
                        , advertContactDoublesFromBDAfter.get(i), null)
                        .build();

                for (Object diffEntry : diff.getDiffs()) {
                    System.err.println(diffEntry.toString());
                }
            }
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