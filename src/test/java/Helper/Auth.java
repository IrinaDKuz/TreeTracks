package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static Helper.ActionsClass.waitAndReturnElement;
import static Helper.ActionsClass.waitAndSendKeys;

public class Auth {
    public static String authKeyTest = "Bearer ac9894e101da82b7175f02a315399145";
    public static String authKeyAdmin = "Bearer d8ed15517b05a53d53339b4d5e1f0abf";

    public static final String EMAIL_TEST = "petrpetrovpp2023@gmail.com";
    public static final String PASSWORD_TEST = "petrPETRtest"; //TEST123

    public static final String EMAIL_ADMIN = "admin@3tracks.online";
    public static final String PASSWORD_ADMIN = "password";


    @Test
    public static void auth(WebDriver driver) throws InterruptedException {
        driver.get("https://admin.3tracks.link/");
        waitAndSendKeys(By.xpath("//input[@type='email']"), EMAIL_ADMIN, driver);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(PASSWORD_ADMIN);
        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }

    public static void auth(WebDriver driver, String email, String password) throws InterruptedException {
        driver.get("https://admin.3tracks.link/");
        waitAndSendKeys(By.xpath("//input[@type='email']"), email, driver);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }

    public static ChromeDriver getDriver() {
        return Driver.addDriver();
    }
}
