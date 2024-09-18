
package API.Admin.ContentFilter;

import AdminPackage.entity.AdminContentFilterForTesting;
import io.qameta.allure.Allure;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static API.Admin.ContentFilter.ContentFilterAdvert.getRandomFilter;
import static API.Admin.ContentFilter.ContentFilterOffer.*;
import static API.Helper.assertDelete;
import static API.Helper.deleteMethod;
import static Helper.AllureHelper.DELETE;
import static SQL.AdvertSQL.getValueFromBDWhere;

/***
 Тест проверяет работу Offer Content Filter
 TODO: 90% DONE
 */


public class ContentFilterOfferRole {

    static List<AdminContentFilterForTesting> filterIncludeList = new ArrayList<>();
    static List<AdminContentFilterForTesting> filterExcludeList = new ArrayList<>();

    @Test
    public static void testPrepareData() throws Exception {
        System.out.println(" ");
        System.out.println("0) Заполнение массива данных для дальнейших проверок");
        filterIncludeList = prepareDataFilterIncludeOfferList();
        filterExcludeList = prepareDataFilterExcludeOfferList();
    }

    @Test(dependsOnMethods = "testPrepareData", alwaysRun = true)
    public void testSeparateInclude() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        //1) Тестирование по отдельности include
        System.out.println(" ");
        System.out.println("1) Тестирование по отдельности include");
        for (AdminContentFilterForTesting filler1 : filterIncludeList) {
            if (filler1.getInclude())
                testFieldCombination(true, List.of(filler1), softAssert);
        }
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "testPrepareData", alwaysRun = true)
    public void testSeveral() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 2) Тестирование конфигураций include + exclude несколько
        System.out.println(" ");
        System.out.println("2) Несколько include - несколько exclude");
        List<AdminContentFilterForTesting> list = getRandomFilter(filterIncludeList, filterIncludeList.size() - 1);
        list.addAll(getRandomFilter(filterExcludeList, filterExcludeList.size() - 1));
        testFieldCombination(true, list, softAssert);
        softAssert.assertAll();

    }

    @Test(dependsOnMethods = "testSeveral", alwaysRun = true)
    public void testAllInclude() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 3) Тестирование конфигураций include все
        System.out.println(" ");
        System.out.println("3) Тестирование конфигураций include все");
        testFieldCombination(true, filterIncludeList, softAssert);
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "testAllInclude", alwaysRun = true)
    public void test1() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 4) Тестирование конфигураций по всем include - exclude
        System.out.println(" ");
        System.out.println("4) Тестирование конфигураций по всем include - exclude");
        for (int i = 0; i < filterIncludeList.size(); i++) {
            int n = new Random().nextInt(filterExcludeList.size());
            testFieldCombination(true, List.of(filterIncludeList.get(i), filterExcludeList.get(n)), softAssert);
        }
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "test1", alwaysRun = true)
    public void test2() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        // 5) Тестирование конфигураций include - по всем exclude
        System.out.println(" ");
        System.out.println("5) Тестирование конфигураций include - по всем exclude");
        for (int i = 0; i < filterExcludeList.size(); i++) {
            int n = new Random().nextInt(filterIncludeList.size());
            testFieldCombination(true, List.of(filterExcludeList.get(i), filterIncludeList.get(n)), softAssert);
        }
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "test2", alwaysRun = true)
    public static void testDeleteData() throws Exception {
        Allure.step(DELETE + " content-filter/offer ");
        String id = getValueFromBDWhere("id", "content_filter_role",
                Map.of("type", "offer", "role_id", "1"));
        deleteMethod("role/1/content-filter", "offer");
        assertDelete(id, "content_filter_role");
    }
}


