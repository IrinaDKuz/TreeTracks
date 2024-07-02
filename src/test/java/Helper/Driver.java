package Helper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class Driver {
    static final String HOST_URL = "http://localhost:4445/";


    public static WebDriver addDriver() {
        System.setProperty("webdriver.chrome.driver", "//Users/Irina/Desktop/Sourses/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ChromeDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + 0.6 + "'");

        return driver;
    }

    public static ChromeDriver addChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "//Users/Irina/Desktop/Sourses/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + 0.6 + "'");

        return driver;
    }

    public static FirefoxDriver addFireFoxDriver() {
        System.setProperty("webdriver.gecko.driver", "//Users/Irina/Desktop/Sourses/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + 0.6 + "'");
        return driver;
    }
}
