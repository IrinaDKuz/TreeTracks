package OtherTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

import static Helper.Adverts.MODEL_TYPES_MAP;
import static Helper.Auth.KEY;
import static Helper.Auth.authApiNewX;
import static Helper.GeoAndLang.getRandomKey;
import static OtherTest.FilterAPI.*;

/***
 Отправляем запросы из файла записываем id в новый файл для сравнения
 */


public class CheckFilterAPI {

    static int userId = 4;

    static String url = "https://api.newx.3tracks.online/";
    //  static String offer = "offer?";
    static String advertisers = "advert?page=1&limit=2000";
    //   static String affiliate = "affiliate?";
    //   static String admin = "admin/?";
    //   static String task = "task?";

    @Test
    public static void test() throws Exception {
        authApiNewX(userId);
        List<String> urls = fileRead("fileWithUrl");
        fileNew("fileWithId2");
        for (String url : urls) {
            if (url.contains("/advert?"))
                postAdvertsUrlWithFilter(url, "fileWithId2", true);
            if (url.contains("/offer?"))
                postOffersUrlWithFilter(url, "fileWithId2", true);
            if (url.contains("/affiliate?"))
                postAffiliatesUrlWithFilter(url, "fileWithId2", true);
            if (url.contains("/admin/?"))
                postAdminUrlWithFilter(url, "fileWithId2", true);
            if (url.contains("/task?"))
                postTaskUrlWithFilter(url, "fileWithId2", true);
        }
    }


    public static List<String> fileRead(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Пропускаем первую строку
                    continue;
                }
                lines.add(line); // Сохраняем все остальные строки
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}