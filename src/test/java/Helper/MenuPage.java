package Helper;

import Helper.ActionsClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Helper.ActionsClass.waitAndClick;
import static Helper.Path.getAncestor;

public class MenuPage {

    public static final By xpath_h1_Offer = By.xpath("//section//a[text() = 'Offer']");
    public static final By class_formGroup = By.className("form-group");
    public static final By menu_xpath_h1_Offer = By.xpath("//section//a[text() = 'Offer']");

    public static final String DASHBOARD = "Dashboard";
    public static final String STATISTICS = "Statistics";
    public static final String OFFERS = "Offers";
    public static final String ADVERTISERS = "Advertisers";
    public static final String AFFILIATES = "Заказы";
    public static final String ADMINS = "DayPack";
    public static final String TICKETS = "LeadAction";
    public static final String BILLING = "Affiliate payment type";
    public static final String TASKS = "Модели оплаты";
    public static final String NEWS = "Модели оплаты";
    public static final String SETTINGS = "Модели оплаты";
    public static final String CONVERSION = "Модели оплаты";

    public static String getMenuItemPath(String menuString) {
        return "(//p[contains(text(), '" + menuString + "')])[1]";
    }

    public static void menuItemClick(String menuString, WebDriver driver ) throws InterruptedException {
        waitAndClick(By.xpath("//section//a[text() = '" + menuString + "']"), driver);
    }

    public static void openMenuItem(String menuItemName, WebDriver driver) throws InterruptedException {
        boolean isOpen = driver
                .findElement(By.xpath(getAncestor(getMenuItemPath(menuItemName), "li")))
                .getAttribute("class")
                .contains("menu-open");
        if (!isOpen) {
            waitAndClick(By.xpath(getMenuItemPath(menuItemName)), driver);
        }
    }


}

