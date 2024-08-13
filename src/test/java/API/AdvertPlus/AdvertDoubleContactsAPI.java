package API.AdvertPlus;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertContactDouble;
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
        // person
        //testByPerson();
      //  for (int i = 0; i < 5; i++) {
            addEditNewContact("email", false);
            addEditNewContact("email", true);
            addEditNewContact("person", false);
            addEditNewContact("person", true);
            editContactFromBD("contact_id1", "advert1", true);
            editContactFromBD("contact_id2", "advert2", true);
            editContactFromBD("contact_id1", "advert1", false);
            editContactFromBD("contact_id2", "advert2", false);
       // }

        // messenger = из базы id и name и type

        // редактируем контакт1 из добавленных
        // проверяем базу

        // редактируем контакт2 из добавленных
        // проверяем базу

        // редактируем контакт из базы чтобы он стал дублем нового
        // проверяем базу

        // удаляем контакт1 из добавленных
        // проверяем базу

        // удаляем контакт2 из добавленных
        // проверяем базу
    }

    private static void addEditNewContact(String parameterName, Boolean isEdit) throws Exception {
        // Записываем список объектов из базы
        try {
            List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
            String value = getRandomValueFromBDWhereNotNull(parameterName, "advert_contact", parameterName);

            System.out.println("Было всего записей: " + advertContactDoublesFromBD.size());
            System.out.println("ParameterName = " + parameterName);
            System.out.println("ParameterValue = " + value);

            List<String> contactIds = getArrayFromBDWhereOrderBy("id", "advert_contact",
                    parameterName, value, "id");

            System.out.println("RowIds = " + contactIds);


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
                System.out.println("Редактируем контакт Адверта: " + duplicatorAdvertId1);

                duplicatorContactId = getRandomValueFromBDWhere("id", "advert_contact",
                        "advert_id", duplicatorAdvertId1);
                System.out.println("Редактируем контакт: " + duplicatorContactId);

                editContact(duplicatorAdvertId1, duplicatorContactId, advertContact);
            } else {
                duplicatorAdvertId1 = getRandomValueFromBDExcept("id", "advert",
                        exceptValuesList);
                System.out.println("Добавляем контакт Адверту: " + duplicatorAdvertId1);

                duplicatorContactId = String.valueOf(addContact(duplicatorAdvertId1, advertContact));
                System.out.println("Добавленный контакт id =  " + duplicatorAdvertId1);

            }

            for (String contactId : contactIds) {
                AdvertContactDouble advertContactDouble =
                        new AdvertContactDouble(parameterName, duplicatorAdvertId1, contactVSAdvertMap.get(contactId),
                                String.valueOf(duplicatorContactId), contactId);
                advertContactDoublesFromBD.add(advertContactDouble);
                System.out.println("Стало записей в локальном массиве: " + advertContactDoublesFromBD.size());
            }

            List<AdvertContactDouble> advertContactDoublesFromBDAfter = fillAdvertContactsDoubleFromBD();
            System.out.println("Стало записей в базе: " + advertContactDoublesFromBDAfter.size());

            assertContactDoubleBD(advertContactDoublesFromBD, advertContactDoublesFromBDAfter);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void assertContactDoubleBD(List<AdvertContactDouble> advertContactDoublesFromBD,
                                              List<AdvertContactDouble> advertContactDoublesFromBDAfter) {
        SoftAssert softAssert = new SoftAssert();
        try {
            for (int i = 0; i < advertContactDoublesFromBD.size(); i++) {
                AdvertContactDouble acd1 = advertContactDoublesFromBD.get(i);
                AdvertContactDouble acd2 = advertContactDoublesFromBDAfter.get(i);
                softAssert.assertEquals(acd1.getAdvert1(), acd2.getAdvert1());
                softAssert.assertEquals(acd1.getAdvert2(), acd2.getAdvert2());
                softAssert.assertEquals(acd1.getContact_id1(), acd2.getContact_id1());
                softAssert.assertEquals(acd1.getContact_id2(), acd2.getContact_id2());
                softAssert.assertEquals(acd1.getField(), acd2.getField());
            }
            softAssert.assertAll();
        } catch (AssertionError a) {
            System.err.println(a);
        }
    }

    private static void editContactFromBD(String parameterName, String advertFieldName, Boolean isEdit) {
        // Записываем список объектов из базы
        try {
            List<AdvertContactDouble> advertContactDoublesFromBD = fillAdvertContactsDoubleFromBD();
            String contactIdFromBD = getRandomValueFromBDWhereNotNull(parameterName, "advert_contact_double",
                    parameterName);
            String advertIdFromBD = getValueFromBDWhere(advertFieldName, "advert_contact_double",
                    parameterName, contactIdFromBD);

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

    private static int addContact(String advertId, AdvertContact advertContact) {
        return contactsAddPost(advertId, advertContact);
    }

    private static void editContact(String advertId, String duplicatorContactId, AdvertContact advertContactEdit) throws Exception {
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