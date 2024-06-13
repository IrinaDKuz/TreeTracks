package AdvertPackage.autoCreate;

import AdvertPackage.entity.Advert;
import AdvertPackage.entity.AdvertContact;
import AdvertPackage.entity.AdvertPrimaryInfo;
import AdvertPackage.entity.AdvertRequisites;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Helper.ActionsClass.*;
import static Helper.MenuPage.*;
import static Helper.Path.contain;

public class AdvertBuilder {
    public static final String ADD_ADVERT_BUTTON = "+ Add new Advertiser";
    public static final String COMPANY = "Company";
    public static final String COMPANY_LEGAL_NAME = "Company Legalname";
    public static final String SITE_URL = "Site URL";
    public static final String STATUS = "Status";
    public static final String MODEL_TYPE = "Model Type";
    public static final String NOTE = "Note";
    public static final String MANAGER = "Manager";
    public static final String USER_REQUEST_SOURCE = "User Request Source";
    public static final String USER_REQUEST_SOURCE_VALUE = "User Request Source Value";
    public static final String TAG = "Tag";




    public static void buildBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);

        // TODO ниже пока Pандом Pандомыч
        // waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber + "]"), driver);

        sendKeysByLabel(COMPANY, advertPrimaryInfo.getCompany(), driver);
        sendKeysByLabel(COMPANY_LEGAL_NAME, advertPrimaryInfo.getCompanyLegalName(), driver);
        sendKeysByLabel(SITE_URL, advertPrimaryInfo.getSiteUrl(), driver);

        selectAutocompleteInput(STATUS, advertPrimaryInfo.getStatus(), driver);
        selectAutocompleteInput(MODEL_TYPE, advertPrimaryInfo.getModelType(), driver);
        selectAutocompleteInput(MANAGER, advertPrimaryInfo.getManagerId(), driver);
        selectAutocompleteInput("Sales Manager", advertPrimaryInfo.getSalesManager(), driver);
        selectAutocompleteInput("Account Manager", advertPrimaryInfo.getAccountManager(), driver);
        selectAutocompleteInput("GEO", advertPrimaryInfo.getGeo(), driver);
        selectAutocompleteInput("Categories", advertPrimaryInfo.getCategories(), driver);
        selectAutocompleteInput("Tags", advertPrimaryInfo.getTag(), driver);
        selectAutocompleteInput("Allowed IPs", advertPrimaryInfo.getAllowedIp(), driver);
        selectAutocompleteInput("Allowed sub account", advertPrimaryInfo.getAllowedSubAccount(), driver);
        selectAutocompleteInput("Disallowed sub account", advertPrimaryInfo.getDisallowedSubAccount(), driver);
        selectAutocompleteInput(USER_REQUEST_SOURCE, advertPrimaryInfo.getUserRequestSourceId(), driver);

        sendKeysByLabel(USER_REQUEST_SOURCE_VALUE, advertPrimaryInfo.getUserRequestSourceValue(), driver);
        sendKeysToTextAreaByLabel(NOTE, advertPrimaryInfo.getNote(), driver);

        WebElement checkbox = driver.findElement(By.id("forbidChangePostbackStatus"));
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        waitAndClick(contain("button", "Save"), driver);
    }

    public static void buildBrowserAdvertContact(ArrayList<AdvertContact> advertContacts, ChromeDriver driver) throws InterruptedException {
        // menuItemClick(ADVERTISERS, driver);
        // TODO ниже пока Pандом Pандомыч
        // waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        // Random random = new Random();
        // int randomNumber = 5 + random.nextInt(46);
        // waitAndClick(By.xpath("(//td//a//button)[" + randomNumber +"]"), driver);

        waitAndClick(contain("a", "Contacts"), driver);
        Thread.sleep(3000);
        if (!isElementPresent(driver, contain("button", "Delete Contact"))) {
            waitAndClick(contain("button", "Add Contact Person"), driver);
        }
        for (AdvertContact advertContact : advertContacts) {
            //TODO переделать для списка
            sendKeysByLabel("Contact person", advertContact.getPerson(), driver);
            selectAutocompleteInput("Contact status", advertContact.getStatus(), driver);
            sendKeysByLabel("Email", advertContact.getEmail(), driver);
            waitAndClick(contain("button", "Save Contacts"), driver);
        }
    }



    public static void buildBrowserAdvertRequisites(ArrayList<AdvertRequisites> advertRequisites, ChromeDriver driver) throws InterruptedException {
         menuItemClick(ADVERTISERS, driver);
        // TODO ниже пока Pандом Pандомыч
        // waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber +"]"), driver);

        waitAndClick(contain("a", "Requisites"), driver);
        waitAndClick(contain("button", "+ Add New Requisite"), driver);

        for (AdvertRequisites advertRequisite : advertRequisites) {
            selectAutocompleteInput("Payment method", advertRequisite.getPaymentSystemTitle(), driver);
            selectAutocompleteInput("Currency", advertRequisite.getCurrency(), driver);

            for (Map.Entry<String, String> entry : advertRequisite.getRequisites().entrySet()) {
                sendKeysByLabel((entry.getKey()), entry.getValue(), driver);
            }
            waitAndClick(contain("button", "Save"), driver);
        }
    }

    public static void buildBrowserAdvertFilter(Advert advert, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);

        // TODO ниже пока Pандом Pандомыч
        selectAutocompleteInputByText("Sales Manager", advert.getAdvertPrimaryInfo().getSalesManager(), driver);
        selectAutocompleteInputByText("Account Manager", advert.getAdvertPrimaryInfo().getAccountManager(), driver);

        enterTextByPlaceholder("Site Url", advert.getAdvertPrimaryInfo().getSiteUrl(), driver);

        selectAutocompleteInputByText("Geo", advert.getAdvertPrimaryInfo().getGeo()[0], driver);

        selectAutocompleteInputByText("Categories", advert.getAdvertPrimaryInfo().getCategories()[0], driver);

        selectAutocompleteInputByText(MODEL_TYPE, advert.getAdvertPrimaryInfo().getModelType(), driver);
        selectAutocompleteInputByText(STATUS, advert.getAdvertPrimaryInfo().getStatus(), driver);

        enterTextByPlaceholder(NOTE, advert.getAdvertPrimaryInfo().getNote(), driver);
        enterTextByPlaceholder(COMPANY_LEGAL_NAME, advert.getAdvertPrimaryInfo().getCompanyLegalName(), driver);

        selectAutocompleteInputByText(MANAGER, advert.getAdvertPrimaryInfo().getManagerId(), driver);
        //RT
        //RV
        enterTextByPlaceholder("Contact", advert.getAdvertContact().getFirst().getEmail(), driver);
        selectAutocompleteInputByText(USER_REQUEST_SOURCE, advert.getAdvertPrimaryInfo().getUserRequestSourceId(), driver);
        enterTextByPlaceholder(USER_REQUEST_SOURCE_VALUE, advert.getAdvertPrimaryInfo().getUserRequestSourceValue(), driver);
        selectAutocompleteInputByText(TAG, advert.getAdvertPrimaryInfo().getTag()[0], driver);
        enterTextByPlaceholder("Person", advert.getAdvertContact().getFirst().getPerson(), driver);
        waitAndClick(contain("button", "Show results"), driver);
    }
}

