package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static Helper.ActionsClass.waitAndReturnElement;

public class Auth {

    @Test
    public static void auth(WebDriver driver) throws InterruptedException {
        driver.get("https://admin.3tracks.link/");

//       waitAndReturnElement(By.xpath("//input[@type='email']"), driver)
//                .sendKeys("admin@3tracks.online");
//       driver.findElement(By.xpath("//input[@type='password']")).sendKeys("password");

        waitAndReturnElement(By.xpath("//input[@type='email']"), driver)
                .sendKeys("petrpetrovpp2023@gmail.com");
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("petrPETRtest");

        driver.findElement(By.xpath("//button[contains(text(), 'Sign in')]")).click();
    }

    public static ChromeDriver getDriver() {
        return Driver.addDriver();
    }
}
