package AdminPackage.autoCreate;

import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertPrimaryInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Random;

import static Helper.MenuPage.*;
import static Helper.ActionsClass.*;
import static Helper.Path.contain;

public class AdminBuilder {
    public static final String ADD_ADVERT_BUTTON = "+ Add new Advertiser";

    public static void buildBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);

        // TODO ниже пока Pандом Pандомыч
        // waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber +"]"), driver);

        sendKeysByLabel("Company", advertPrimaryInfo.getCompany(), driver);
        sendKeysByLabel("Company Legalname", advertPrimaryInfo.getCompanyLegalName(), driver);
        sendKeysByLabel("Site URL", advertPrimaryInfo.getSiteUrl(), driver);

        selectAutocompleteInput("Status", advertPrimaryInfo.getStatus(), driver);
        selectAutocompleteInput("Model Type", advertPrimaryInfo.getModelType(), driver);
        selectAutocompleteInput("Manager", advertPrimaryInfo.getManagerId(), driver);
        selectAutocompleteInput("Sales Manager", advertPrimaryInfo.getSalesManager(), driver);
        selectAutocompleteInput("Account Manager", advertPrimaryInfo.getAccountManager(), driver);
        selectAutocompleteInput("GEO", advertPrimaryInfo.getGeo(), driver);
        selectAutocompleteInput("Categories", advertPrimaryInfo.getCategories(), driver);
        selectAutocompleteInput("Tags", advertPrimaryInfo.getTag(), driver);
        selectAutocompleteInput("Allowed IPs", advertPrimaryInfo.getAllowedIp(), driver);
        selectAutocompleteInput("Allowed sub account", advertPrimaryInfo.getAllowedSubAccount(), driver);
        selectAutocompleteInput("Disallowed sub account", advertPrimaryInfo.getDisallowedSubAccount(), driver);
        selectAutocompleteInput("User Request Source", advertPrimaryInfo.getUserRequestSourceId(), driver);

        sendKeysByLabel("User Request Source Value", advertPrimaryInfo.getUserRequestSourceValue(), driver);
        sendKeysToTextAreaByLabel("Note", advertPrimaryInfo.getNote(), driver);

        WebElement checkbox = driver.findElement(By.id("forbidChangePostbackStatus"));
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        waitAndClick(contain("button", "Save"), driver);
    }

    public static void buildBrowserAdvertContact(ArrayList<AdvertContact> advertContacts, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);
        // TODO ниже пока Pандом Pандомыч
        // waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber +"]"), driver);

        waitAndClick(contain("a", "Contacts"), driver);

        for (AdvertContact advertContact : advertContacts) {
            waitAndClick(contain("button", "Add Contact Person"), driver);
            sendKeysByLabel("Contact person", advertContact.getPerson(), driver);
            selectAutocompleteInput("Contact status", advertContact.getStatus(), driver);
            sendKeysByLabel("Email", advertContact.getEmail(), driver);
            waitAndClick(contain("button", "Save Contacts"), driver);
        }
    }
}

