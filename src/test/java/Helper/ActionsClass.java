package Helper;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionsClass {

    private static final String ALLOWED_CHARACTERS_WITH_SPACES = "абвгдежзийклмнопрстуфхцчшщъыьэюя" + "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "01234567890" + "@$!%*?&#/|\\~<>^{}[]():;" + "                         ";


    public static int getRandomInt() {
        return (int) (Math.random() * 100) + 1;
    }

    public static int getRandom5Int() {
        return (int) (Math.random() * 4) + 1;
    }

    public static double getRandomDouble() {
        return Math.round(Math.random() * 1000.0) / 100.0;
    }

    public static WebElement waitAndReturnElement(By by, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isElementPresent(WebDriver driver, By locator, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static WebElement waitForElement(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitAndClick(By by, WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
        } catch (Exception e) {
            Thread.sleep(3000);
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(3000);
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
        }
    }

    public static void scrollAndClick(By by, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public static void imgWaitAndClick(By by, WebDriver driver) {
        scrollRight(driver, 1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public static void sendKeys(By by, String text, WebDriver driver) throws InterruptedException {
        WebElement sendKeysElement = driver.findElement(by);
        sendKeysElement.sendKeys(text);
    }

    public static void waitAndSendKeys(By by, String text, WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement sendKeysElement = driver.findElement(by);
        wait.until(ExpectedConditions.visibilityOf(sendKeysElement));
        sendKeysElement.sendKeys(text);
    }

    public static void waitAndSendKeys(WebElement element, String text, WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }

    public static void waitClearAndSendKeys(By by, String text, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement sendKeysElement = driver.findElement(by);
        wait.until(ExpectedConditions.visibilityOf(sendKeysElement));
        sendKeysElement.clear();
        sendKeysElement.sendKeys(text);
    }

    public static void waitClearAndSendKeys(WebElement element, String text, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }


    public static void clearAndSendKeys(By by, String text, WebDriver driver) {
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }

    public static String getExpressionInBrackets(String inputString) {
        // Используем регулярное выражение для поиска кода языка в скобках
        Pattern pattern = Pattern.compile("\\((\\w+)\\)");
        Matcher matcher = pattern.matcher(inputString);
        String expression = "";
        // Если найдено совпадение, извлекаем код языка
        if (matcher.find()) expression = matcher.group(1).toLowerCase();
        return expression;
    }

    public static void alertWindow(WebDriver driver, Boolean accept) throws InterruptedException {
        Alert alert = driver.switchTo().alert();
        System.out.println("Текст в alert окне: " + alert.getText());
        Thread.sleep(1000);
        // Отклонить/Подтвердить
        if (accept) {
            alert.accept();
            Thread.sleep(5000);
            try {
                alert.accept();
            } catch (Exception ignored) {
            }
        } else {
            alert.dismiss();
        }
    }

    public static void checkSystemMassage(WebDriver driver, String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(text(), '" + expectedMessage + "')]"))));
    }

    public static String getElementTextFromPart(String tag, String partialText, WebDriver driver) {
        String xpathExpression = tag + "[contains(text(), '" + partialText + "')]";
        return driver.findElement(By.xpath(xpathExpression)).getText();
    }

    public static String getCurrentDate() {
        Date currentDate = new Date();
        // Создаем объект SimpleDateFormat для форматирования даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        // Преобразуем текущую дату в строку с выбранным форматом
        return dateFormat.format(currentDate);
    }

    public static String getCurrentDateAndTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd HH:mm:ss:SSS");
        return dateFormat.format(currentDate);
    }

    public static String getCurrentDateAndTimeSimp() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd HH:mm");
        return dateFormat.format(currentDate);
    }

    public static String getCurrentTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mmss");
        return dateFormat.format(currentDate);
    }

    public static String getFutureOrPastDate(int daysCount) {
        LocalDate newDate = LocalDate.now().plusDays(daysCount);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return newDate.format(formatter);
    }

    public static String getFutureOrPastDateTimeSQL(int daysCount) {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(daysCount);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return newDateTime.format(formatter);
    }

    public static String getFutureOrPastDateTimeSQL(String dateString, int daysCount) {
        String[] parts = dateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime dateTime = date.atStartOfDay();

        LocalDateTime newDateTime = dateTime.plusDays(daysCount);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return newDateTime.format(formatter);
    }


    public static boolean checkInArray(List<String> list1, List<String> list2) {
        for (String val : list1) {
            if (list2.contains(val)) {
                return true;
            }
        }
        return false;
    }


    public static void scrollRight(WebDriver driver, int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(" + pixels + ", 0);");
    }

    public static void scrollDown(WebDriver driver, int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + pixels + ");");
    }

    public static void save(WebDriver driver) {
        scrollDown(driver, 1000);
        driver.findElement(Buttons.SAVE).click();
    }

    public static void create(WebDriver driver) {
        scrollDown(driver, 1000);
        driver.findElement(Buttons.CREATE).click();
    }

    public static void selectDropDownValueByVisibleText(By byProperty, String value, WebDriver driver) {
        Select dropdown = new Select(driver.findElement(byProperty));
        dropdown.selectByVisibleText(value);
    }


    public static void selectDropDownValueByVisibleText(WebElement element, String value) {
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(value);
    }

    public static void selectDropDownValueByIndex(By byProperty, int value, WebDriver driver) {
        Select dropdown = new Select(driver.findElement(byProperty));
        dropdown.selectByIndex(value);
    }

    public static void selectDropDownValueRandom(By byProperty, int bound, WebDriver driver) {
        Select dropdown = new Select(driver.findElement(byProperty));
        dropdown.selectByIndex(new Random().nextInt(bound) + 1);
    }


    public static void selectDropDownValueByValue(By byProperty, String value, WebDriver driver) {
        Select dropdown = new Select(driver.findElement(byProperty));
        dropdown.selectByValue(value);
    }

    public static void selectValueByValue(String label, String value, WebDriver driver) {
        System.out.println(value);
        WebElement dropdown = driver.findElement(By.xpath("//label[contains(text(), '" + label + "')]/parent::div//div[@class=' css-b62m3t-container']"));
        dropdown.click();
        WebElement option = driver.findElement(By.xpath("//div[text()='" + value + "']"));
        option.click();
    }

    public static void selectAutocompleteInput(String label, String value, WebDriver driver) throws InterruptedException {
        By by = By.xpath("//label[contains(text(), '" + label + "')]/parent::div//input");
        waitClearAndSendKeys(by, value, driver);
        Thread.sleep(500);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(by)).moveByOffset(0, 40).click().build().perform();
    }

    public static void selectAutocompleteInput(String label, String[] values, WebDriver driver) throws InterruptedException {
        By by = By.xpath("//label[contains(text(), '" + label + "')]/parent::div//input");
        By crossBy = By.xpath("//label[contains(text(), '" + label + "')]/parent::div//div[@class=' css-v7duua']");
         while (isElementPresent(driver, crossBy, Duration.ofSeconds(3))) {
            waitAndClick(crossBy, driver);
        }

        for (String value : values) {
            waitAndSendKeys(by, value, driver);
            Thread.sleep(500);
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(by)).moveByOffset(0, 40).click().build().perform();
        }
    }

    public static void sendKeysByLabel(String label, String value, WebDriver driver) {
        By by = By.xpath("//label[contains(text(), '" + label + "')]/parent::div//input");
        waitClearAndSendKeys(by, value, driver);
    }

    public static void sendKeysToTextAreaByLabel(String label, String value, WebDriver driver) {
        By by = By.xpath("//label[contains(text(), '" + label + "')]/parent::div//textarea");
        waitClearAndSendKeys(by, value, driver);
    }


    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS_WITH_SPACES.length());
            randomString.append(ALLOWED_CHARACTERS_WITH_SPACES.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
