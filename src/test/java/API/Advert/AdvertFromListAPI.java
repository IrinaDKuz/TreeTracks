package API.Advert;

import AdvertPackage.entity.Advert;
import AdvertPackage.entity.AdvertFromList;
import AdvertPackage.entity.AdvertPrimaryInfo;
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

import java.util.*;
import java.util.stream.StreamSupport;

import static API.Helper.*;
import static Helper.AllureHelper.*;
import static Helper.Auth.authKeyAdmin;

/***
 Тест проверяет работу API методов
 - get list, проверка
 во вкладке список Адвертов
 // TODO: 100% Done
 */

public class AdvertFromListAPI {
    static int count;

    @Test
    public static void test() throws Exception {
        count = new Random().nextInt(31) + 20;

        Allure.step("Получаем список из " + count + " Адвертов");
        System.out.println("Получаем список из " + count + " Адвертов");

        Allure.step(CHECK);
        advertListAssert();
    }

    public static List<AdvertFromList> advertListGet(Boolean isShow) {

        List<AdvertFromList> advertsList = new ArrayList<>();

        Response response;
        response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", authKeyAdmin)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .get(URL + "/advert?page=1&limit=" + count + "/");

        String responseBody = response.getBody().asString();
        if (isShow) {
            System.out.println(GET_RESPONSE + responseBody);
            Allure.step(GET_RESPONSE + responseBody);
            attachJson(responseBody, GET_RESPONSE);
        }

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray advertsArray = data.getJSONArray("adverts");

        Assert.assertEquals(advertsArray.length(), count);

        for (int i = 0; i < count; i++) {
            AdvertFromList advertFromList = new AdvertFromList();
            JSONObject advertObject = advertsArray.getJSONObject(i);
            advertFromList.setAdvertId(advertObject.getInt("id"));
            advertFromList.setName(advertObject.getString("name"));
            advertFromList.setLegalName(advertObject.isNull("legalName") ? null : advertObject.getString("legalName") );
            advertFromList.setStatus(advertObject.getString("status"));
            advertFromList.setOfferCount(advertObject.getInt("offerCount"));
            advertFromList.setNote(advertObject.isNull("note") ? null : advertObject.getString("note"));

            advertFromList.setTagId(getArrayFromJson(advertObject, "tag"));
            advertFromList.setCategoriesId(getArrayFromJson(advertObject, "category"));
            advertFromList.setPricingModel(getStringArrayFromJson(advertObject, "pricingModel"));
            advertFromList.setPricingModel(getStringArrayFromJson(advertObject, "paymentType"));
            advertFromList.setGeoAbb(getStringArrayFromJson(advertObject, "geo"));
            advertsList.add(advertFromList);
        }
        return advertsList;
    }

    public static void advertListAssert() {
        List<AdvertFromList> advertsList = advertListGet(true);
        for (AdvertFromList advert : advertsList) {
            advert.getAdvertId();

/*            Allure.step("Сравнение отправленных значений в полях с полученными из get");
            Assert.assertEquals(advertPrimaryInfo.getStatus(), advertPrimaryInfoGet.getStatus());
            Assert.assertEquals(advertPrimaryInfo.getCompany(), advertPrimaryInfoGet.getCompany());
            Assert.assertEquals(advertPrimaryInfo.getCompanyLegalName(), advertPrimaryInfoGet.getCompanyLegalName());
            Assert.assertEquals(advertPrimaryInfo.getSiteUrl(), advertPrimaryInfoGet.getSiteUrl());
            Assert.assertEquals(advertPrimaryInfo.getManagerId(), advertPrimaryInfoGet.getManagerId());
            Assert.assertEquals(advertPrimaryInfo.getSalesManagerId(), advertPrimaryInfoGet.getSalesManagerId());
            Assert.assertEquals(advertPrimaryInfo.getAccountManagerId(), advertPrimaryInfoGet.getAccountManagerId());
            Assert.assertEquals(advertPrimaryInfo.getGeoAbb(), advertPrimaryInfoGet.getGeoAbb());
            List<Integer> tags = advertPrimaryInfo.getTagId();
            List<Integer> tagsEdit = advertPrimaryInfoGet.getTagId();
            Collections.sort(tags);
            Collections.sort(tagsEdit);
            Assert.assertEquals(tags, tagsEdit);

            Assert.assertEquals(advertPrimaryInfo.getPricingModel(), advertPrimaryInfoGet.getPricingModel());
            Set<Integer> categoriesId = advertPrimaryInfo.getCategoriesId();
            Set<Integer> categoriesIdEdit = advertPrimaryInfoGet.getCategoriesId();
            Assert.assertEquals(categoriesId, categoriesIdEdit);
            Assert.assertEquals(advertPrimaryInfo.getUserRequestSource(), advertPrimaryInfoGet.getUserRequestSource());
            Assert.assertEquals(advertPrimaryInfo.getNote(), advertPrimaryInfoGet.getNote());

            if (advertPrimaryInfo.getAdvertId() != null) {
                Allure.step("Сравнение значений offer count со значениями из базы");
                advertPrimaryInfo.fillAdvertPrimaryInfoWithOffersData(advertPrimaryInfo.getAdvertId().toString());
                Assert.assertEquals(advertPrimaryInfo.getActiveOffersCount(), advertPrimaryInfoGet.getActiveOffersCount());
                Assert.assertEquals(advertPrimaryInfo.getInactiveOffersCount(), advertPrimaryInfoGet.getInactiveOffersCount());
                Assert.assertEquals(advertPrimaryInfo.getTotalOffersCount(), advertPrimaryInfoGet.getTotalOffersCount());
                Assert.assertEquals(advertPrimaryInfo.getDraftOffersCount(), advertPrimaryInfoGet.getDraftOffersCount());
            }*/
        }
    }
}