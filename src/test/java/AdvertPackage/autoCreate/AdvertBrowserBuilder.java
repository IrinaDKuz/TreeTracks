package AdvertPackage.autoCreate;

import AdvertPackage.entity.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Helper.ActionsClass.*;
import static Helper.Adverts.STATUS_MAP;
import static Helper.Adverts.getRandomValue;
import static Helper.MenuPage.*;
import static Helper.Path.*;

public class AdvertBrowserBuilder {
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


    public static void addNewBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);
        waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        fillBrowserAdvertPrimaryInfo(advertPrimaryInfo, driver);
        waitAndClick(contain("button", "Create"), driver);
    }

    public static void editBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber + "]"), driver);
        fillBrowserAdvertPrimaryInfo(advertPrimaryInfo, driver);
        waitAndClick(contain("button", "Save"), driver);
    }

    public static void fillBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, ChromeDriver driver) throws InterruptedException {
        selectAutocompleteInput(STATUS, advertPrimaryInfo.getStatus(), driver);
        selectAutocompleteInput(MODEL_TYPE, advertPrimaryInfo.getModelType(), driver);
        selectAutocompleteInput(MANAGER, advertPrimaryInfo.getManagerId(), driver);
        selectAutocompleteInput("Sales Manager", advertPrimaryInfo.getSalesManager(), driver);
        selectAutocompleteInput("Account Manager", advertPrimaryInfo.getAccountManager(), driver);
        selectAutocompleteInput("GEO", advertPrimaryInfo.getGeo(), driver);
        selectAutocompleteInput("Categories", advertPrimaryInfo.getCategories(), driver);
        selectAutocompleteInput("Tags", advertPrimaryInfo.getTag(), driver);
        selectAutocompleteInput(USER_REQUEST_SOURCE, advertPrimaryInfo.getUserRequestSourceId(), driver);

        sendKeysByLabel(COMPANY, advertPrimaryInfo.getCompany(), driver);
        sendKeysByLabel(COMPANY_LEGAL_NAME, advertPrimaryInfo.getCompanyLegalName(), driver);
        sendKeysByLabel(SITE_URL, advertPrimaryInfo.getSiteUrl(), driver);
        sendKeysByLabel(USER_REQUEST_SOURCE_VALUE, advertPrimaryInfo.getUserRequestSourceValue(), driver);
        sendKeysToTextAreaByLabel(NOTE, advertPrimaryInfo.getNote(), driver);

        // TODO Ждать пару секунд сообщение об ошибке

    }

    public static void buildBrowserAdvertContact(ArrayList<AdvertContact> advertContacts, ChromeDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Contacts"), driver);
        Thread.sleep(3000);

        for (AdvertContact advertContact : advertContacts) {
            //TODO переделать для списка
            waitAndClick(contain("button", "Add Contact Person"), driver);
            fillBrowserAdvertContactInfo("", "Disable", advertContact.getEmail(), driver);
            // Проверяем ошибку
            Thread.sleep(3000);

            driver.navigate().refresh();
            waitAndClick(contain("button", "Add Contact Person"), driver);
            fillBrowserAdvertContactInfo(advertContact.getPerson(), "Disable", "", driver);
            Thread.sleep(3000);
            // Проверяем ошибку
            driver.navigate().refresh();
            waitAndClick(contain("button", "Add Contact Person"), driver);
            fillBrowserAdvertContactInfo(advertContact.getPerson(), advertContact.getStatus(), advertContact.getEmail(), driver);
            // Проверяем, что ошибки нет, успешное добавление
            // Проверяем запись в БД
            Thread.sleep(3000);
            // редактируем созданную сущность
            waitAndClick(forEditDeleteButtons("div", advertContact.getPerson(), "Edit"), driver);
            fillBrowserAdvertContactInfo(advertContact.getPerson() + "edit",
                    getRandomValue(STATUS_MAP).toString(), advertContact.getEmail() + "edit", driver);
            Thread.sleep(3000);
            // Удаляем созданную сущность
            waitAndClick(forEditDeleteButtons("div", advertContact.getPerson(), "Delete"), driver);
            // Сообщение с подтверждением
        }
    }

    public static void fillBrowserAdvertContactInfo(String contactPersonValue,
                                                    String contactStatusValue,
                                                    String contactEmailValue,
                                                    WebDriver driver) throws InterruptedException {
        sendKeysByLabel("Contact person", contactPersonValue, driver);
        //selectAutocompleteInput("Contact status", contactStatusValue, driver);
        sendKeysByLabel("Email", contactEmailValue, driver);
        waitAndClick(contain("button", "Save Contact"), driver);
    }

    public static void buildBrowserAdvertRequisites(ArrayList<AdvertRequisites> advertRequisites, ChromeDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Requisites"), driver);
        waitAndClick(contain("button", "+ Add New Requisite"), driver);

        for (AdvertRequisites advertRequisite : advertRequisites) {
            selectAutocompleteInput("Payment method", advertRequisite.getPaymentSystemTitle(), driver);
            selectAutocompleteInput("Currency", advertRequisite.getCurrency(), driver);

            for (Map.Entry<Object, String> entry : advertRequisite.getRequisites().entrySet()) {
                sendKeysByLabel((String) entry.getKey(), entry.getValue(), driver);
            }
            waitAndClick(contain("button", "Save"), driver);
        }
    }

    public static void buildBrowserAdvertPostback(AdvertPostback advertPostback, ChromeDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Postbacks"), driver);

        selectAutocompleteInput("Allowed IPs", advertPostback.getAllowedIp(), driver);
        selectAutocompleteInput("Allowed sub account", advertPostback.getAllowedSubAccount(), driver);
        selectAutocompleteInput("Disallowed sub account", advertPostback.getDisallowedSubAccount(), driver);

        if (advertPostback.getForbidChangePostbackStatus().equals(true)) {
            WebElement checkbox = driver.findElement(By.id("forbidChangePostbackStatus"));
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public static void buildBrowserAdvertNotes(ArrayList<AdvertNotes> advertNotes, ChromeDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Notes"), driver);
        waitAndClick(contain("button", "Add new note"), driver);

        for (AdvertNotes advertNote : advertNotes) {
            selectAutocompleteInput("Type", advertNote.getType(), driver);
            if (advertNote.getLocation() != null) {
                selectAutocompleteInput("Location", advertNote.getLocation(), driver);
            }
            sendKeysToTextAreaByLabel("Text", advertNote.getText(), driver);
        }
    }

    public static void buildBrowserAdvertFilter(Advert advert, ChromeDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);

        selectAutocompleteInputByText("Sales Manager", advert.getAdvertPrimaryInfo().getSalesManager(), driver);
        selectAutocompleteInputByText("Account Manager", advert.getAdvertPrimaryInfo().getAccountManager(), driver);
        enterTextByPlaceholder("Site Url", advert.getAdvertPrimaryInfo().getSiteUrl(), driver);
        selectAutocompleteInputByText("Geo", advert.getAdvertPrimaryInfo().getGeo().getFirst(), driver);
        selectAutocompleteInputByText("Categories", advert.getAdvertPrimaryInfo().getCategories().getFirst(), driver);
        selectAutocompleteInputByText(MODEL_TYPE, advert.getAdvertPrimaryInfo().getModelType(), driver);
        selectAutocompleteInputByText(STATUS, advert.getAdvertPrimaryInfo().getStatus(), driver);
        enterTextByPlaceholder(NOTE, advert.getAdvertPrimaryInfo().getNote(), driver);
        enterTextByPlaceholder(COMPANY_LEGAL_NAME, advert.getAdvertPrimaryInfo().getCompanyLegalName(), driver);
        selectAutocompleteInputByText(MANAGER, advert.getAdvertPrimaryInfo().getManagerId(), driver);
        enterTextByPlaceholder("Contact", advert.getAdvertContact().getFirst().getEmail(), driver);
        selectAutocompleteInputByText(USER_REQUEST_SOURCE, advert.getAdvertPrimaryInfo().getUserRequestSourceId(), driver);
        enterTextByPlaceholder(USER_REQUEST_SOURCE_VALUE, advert.getAdvertPrimaryInfo().getUserRequestSourceValue(), driver);
        selectAutocompleteInputByText(TAG, advert.getAdvertPrimaryInfo().getTag().getFirst(), driver);
        enterTextByPlaceholder("Person", advert.getAdvertContact().getFirst().getPerson(), driver);
        selectAutocompleteInputByText("Requsite Type", advert.getAdvertRequisites().getFirst().getPaymentSystemTitle(), driver);
        enterTextByPlaceholder("Requsite Value", getRandomValue(advert.getAdvertRequisites().getFirst().getRequisites()).toString(), driver);
        waitAndClick(contain("button", "Show results"), driver);
    }
}

