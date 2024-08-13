package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import static Helper.ActionsClass.waitAndReturnElement;
import static Helper.ActionsClass.waitAndSendKeys;

public class Auth {
    public static String authKeyTest = "Bearer ac9894e101da82b7175f02a315399145";
    public static String authKeyAdmin = "Bearer 2d427861d4de44f9016617334e802b41";
                                       //  "Bearer 5125483436dc828dc8608f50b26390cb";

    public static final String EMAIL_TEST = "petrpetrovpp2023@gmail.com";
    public static final String PASSWORD_TEST = "password"; //TEST123

    public static final String EMAIL_FULL_ACCESS = "petrpetrovpp2024@gmail.com";
    public static final String PASSWORD_FULL_ACCESS = "password";

    public static final String EMAIL_ADMIN = "admin@3tracks.online";
    public static final String PASSWORD_ADMIN = "password";


    public static final String DEV_NODE = "https://admin.3tracks.link/";
    public static final String PRE_STAGE_NODE = "http://newx.3tracks.online/";


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

