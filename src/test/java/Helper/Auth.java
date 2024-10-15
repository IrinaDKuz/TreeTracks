package Helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static Helper.ActionsClass.waitAndSendKeys;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Auth {
    public static String KEY;


    public static String authKeyTest = "Bearer ac9894e101da82b7175f02a315399145";
    public static String authKeyAdmin = "Bearer 4c5bf5862e686948c96f7f4b464139ba";
    public static String authKeyRoleAdmin = "Bearer e6945961d98e62d2d42e67faed83aba0";

    public static final String EMAIL_TEST = "petrpetrovpp2023@gmail.com";
    public static final String PASSWORD_TEST = "password";

    public static final String EMAIL_FULL_ACCESS = "petrpetrovpp2024@gmail.com";
    public static final String PASSWORD_FULL_ACCESS = "password";

    public static final String EMAIL_ADMIN = "admin@3tracks.online";
    public static final String PASSWORD_ADMIN = "password";


    public static final String DEV_NODE = "https://admin.3tracks.link/";
    public static final String PRE_STAGE_NODE = "http://newx.3tracks.online/";


    static Map<Integer, Map<String, String>> USERS = new HashMap<>() {
        {
            put(1, new HashMap<>() {{
                put("email", "admin@3tracks.online");
                put("password", "password");

            }});

            put(55, new HashMap<>() {{
                put("email", "Lenora45@gmail.com");
                put("password", "password");
            }});

            put(103, new HashMap<>() {{
                put("email", "petrpetrovpp2023@gmail.com");
                put("password", "password");

            }});
            put(104, new HashMap<>() {{
                put("email", "petrpetrovpp2024@gmail.com");
                put("password", "password");
            }});
        }};

    @Test
    public static void auth(WebDriver driver) throws InterruptedException {
        driver.get(DEV_NODE);
        waitAndSendKeys(By.xpath("//input[@type='email']"), EMAIL_ADMIN, driver);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(PASSWORD_ADMIN);
        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }

    public static void auth(WebDriver driver, String email, String password) throws InterruptedException {
        driver.get(DEV_NODE);
        waitAndSendKeys(By.xpath("//input[@type='email']"), email, driver);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }


    public static void authApi(String email, String password) {
        RestAssured.baseURI = "https://api.admin.3tracks.link";

        String requestBody = "{ \"username\": \"" + email + "\"," +
                " \"password\": \"" + password + "\" }";

        Response response = given()
                .body(requestBody)
                .post("/auth/login") // Адрес запроса
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        String key = response.path("data.key");
        KEY = "Bearer " + key;
    }


    public static void authApi(Integer user) {
        System.out.println(user);

        RestAssured.baseURI = "https://api.admin.3tracks.link";
        String email = USERS.get(user).get("email");
        String password = USERS.get(user).get("password");

        String requestBody = "{ \"username\": \"" + email + "\"," +
                " \"password\": \"" + password + "\" }";

        Response response = given()
                .body(requestBody)
                .post("/auth/login") // Адрес запроса
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        String key = response.path("data.key");
        KEY = "Bearer " + key;
    }


    public static int getRandomUserId() {
        Set<Integer> keys = USERS.keySet();
        Integer[] keyArray = keys.toArray(new Integer[0]);
        Random random = new Random();
        int randomIndex = random.nextInt(keyArray.length);
        return keyArray[randomIndex];
    }

    public static WebDriver getDriver() {
        return Driver.addDriver();
    }

    public static WebDriver getChromeDriver() {
        return Driver.addChromeDriver();
    }

    public static WebDriver getFireFoxDriver() {
        return Driver.addFireFoxDriver();
    }
}

