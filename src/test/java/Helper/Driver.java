package Helper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Driver {
    static  final String HOST_URL = "http://localhost:4445/";


    public static ChromeDriver addDriver() {
        System.setProperty("webdriver.chrome.driver", "//Users/Irina/Desktop/Sourses/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
       // RemoteWebDriver driver = new RemoteWebDriver(new URL(HOST_URL), options);
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + 0.6 + "'");

        return driver;
    }
}
