package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static Helper.ActionsClass.waitAndReturnElement;

public class Auth {
    public static String authKeyTest = "Bearer ac9894e101da82b7175f02a315399145";
    public static String authKeyAdmin = "Bearer d8ed15517b05a53d53339b4d5e1f0abf";

    @Test
    public static void auth(WebDriver driver) throws InterruptedException {
        driver.get("https://admin.3tracks.link/");

//       waitAndReturnElement(By.xpath("//input[@type='email']"), driver)
//                .sendKeys("admin@3tracks.online");
//       driver.findElement(By.xpath("//input[@type='password']")).sendKeys("password");

        waitAndReturnElement(By.xpath("//input[@type='email']"), driver)
                .sendKeys("petrpetrovpp2023@gmail.com");

         driver.findElement(By.xpath("//input[@type='password']")).sendKeys("petrPETRtest");
       // driver.findElement(By.xpath("//input[@type='password']")).sendKeys("TEST123");

        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }

    public static ChromeDriver getDriver() {
        return Driver.addDriver();
    }
}
