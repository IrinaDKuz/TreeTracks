package API.Offer.OfferDraft;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static API.Advert.AdvertFilterAPI.getUrlWithParameters;
import static API.Offer.OfferMain.OfferMainFilter.filterAssert;
import static API.Offer.OfferMain.OfferMainFilter.filterOffersPost;
import static Helper.AllureHelper.GET_RESPONSE;
import static Helper.AllureHelper.attachJson;
import static Helper.Auth.authKeyAdmin;
import static SQL.AdvertSQL.*;

/***
 Тест проверяет работу API методов Draft Offer
 - filter,
 TODO: 0% BUG ничего не работает
 */

public class OfferDraftFilter {

    public final static Map<String, String> offersFields = new HashMap<>() {
        {
            put("id", "ids[]");
            put("title", "title");
            put("advert_id", "advert[]");
            put("status", "status[]");
            put("country", "country[]");
            put("categories", "category[]");
            put("traffic_source", "trafficSources[]");
            put("privacy_level", "privacyLevel[]");
            put("tags", "tags[]");
        }
    };

    @Test
    public static void test() throws Exception {
        Allure.description("Проверка работы метода фильтрациии Offer Draft !!! НЕ РАБОТАЕТ BACK");
        filterOffersTest();
    }

    private static void filterOffersTest() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        for (Map.Entry<String, String> entry : offersFields.entrySet()) {
            String value = getRandomValueFromBDWhereNotNull(entry.getKey(), "offer_draft", entry.getKey());
            filterOffers(entry, value, softAssert);
        }
        softAssert.assertAll();
    }

    private static void filterOffers(Map.Entry<String, String> entry, String valueString, SoftAssert softAssert) throws Exception {
        Set<String> ids = new TreeSet<>(getArrayFromBDWhere("id", "offer_draft", entry.getKey(), valueString));
        List<String> filterIds = filterOffersPost(entry.getValue(), valueString, true);
        filterAssert(filterIds, ids, softAssert, true);
    }
}