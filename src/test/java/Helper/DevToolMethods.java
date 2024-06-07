package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Helper.ActionsClass.*;
import static Helper.Path.contain;

/***
 * https://chromedevtools.github.io/devtools-protocol/tot/Network/#method-getCookies
 */

public class DevToolMethods {
    static String brand = "Brand_Automation_Advert@mailto.plus";

    public interface TokenCallback {
        void onTokenExtracted(String token);
    }
}

   /* public static void writeCookieAndTokenInFile() throws InterruptedException, IOException {
        ChromeDriver driver = getDriver();
        auth(driver);

        waitAndClick(contain("p", LEADS_SALES), driver);
        waitAndClick(contain("p", LEAD_ACTION), driver);
        waitAndClick(Buttons.ADD_RU, driver);

        selectAutocompleteInput(By.xpath("//input[@data-id ='lead_action_new_form_leadId']"),
                "123", driver);

        selectAutocompleteInput(By.xpath("//input[@data-id ='lead_action_new_form_brandId']"),
                brand, driver);

        Select dropdown = new Select(driver.findElement(By.id("lead_action_new_form_paymentType")));
        dropdown.selectByVisibleText(paymentType);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatter);
        waitAndReturnElement(By.id("lead_action_new_form_actionDt"), driver).sendKeys(formattedDate);
        driver.findElement(By.id("lead_action_new_form_amount")).sendKeys(String.valueOf(getRandomDouble()));
        driver.findElement(By.id("lead_action_new_form_description")).sendKeys("Lead_action_new_form_Description");

        String url = "https://admin.apileads.tech/lead-action/new";
        DevTools devTools = driver.getDevTools();


        final String[] content = new String[1];
        getToken(driver, devTools, url, new TokenCallback() {
            @Override
            public void onTokenExtracted(String token) {
                content[0] = token;
            }
        });

        write("token.txt", content[0]);
        write("cookie.txt", getCookie(driver));
    }


    public static void write(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении из файла: " + e.getMessage());
        }
        return content.toString();
    }

    public static void getToken(WebDriver driver, DevTools devTools, String url, TokenCallback callback) {
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        CountDownLatch latch = new CountDownLatch(1);

        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request request = requestConsumer.getRequest();
            if (request.getUrl().contains(url)) {
                Pattern pattern = Pattern.compile("_token%5D=([^&]+)");
                Matcher matcher = pattern.matcher(request.getPostData().toString().replace("]", ""));
                if (matcher.find()) {
                    callback.onTokenExtracted(matcher.group(1));
                } else {
                    System.out.println("Token not found");
                    callback.onTokenExtracted(null);
                }
                latch.countDown();
            }
        });

        driver.findElement(By.xpath("//button[contains(text(), 'Добавить')]")).click();

        try {
            latch.await(); // Ожидание завершения асинхронной задачи
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getCookie(WebDriver driver) {
        String[] cookieParts = driver.manage().getCookies().toArray()[0].toString().split("; ");
        String phpSessidValue = null;
        for (String part : cookieParts) {
            if (part.startsWith("PHPSESSID=")) {
                phpSessidValue = part.substring("PHPSESSID=".length());
                break;
            }
        }
        return phpSessidValue;
    }
}
*/