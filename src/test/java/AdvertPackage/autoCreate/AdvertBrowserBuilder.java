package AdvertPackage.autoCreate;

import AdvertPackage.entity.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Helper.ActionsClass.*;
import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomValue;
import static Helper.MenuPage.*;
import static Helper.Path.*;

public class AdvertBrowserBuilder {

    public static void addNewBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, WebDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);
        waitAndClick(contain("button", ADD_ADVERT_BUTTON), driver);
        fillBrowserAdvertPrimaryInfo(advertPrimaryInfo, driver);
        waitAndClick(contain("button", "Create"), driver);
    }

    public static void editBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, WebDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);
        Random random = new Random();
        int randomNumber = 5 + random.nextInt(46);
        waitAndClick(By.xpath("(//td//a//button)[" + randomNumber + "]"), driver);
        fillBrowserAdvertPrimaryInfo(advertPrimaryInfo, driver);
        waitAndClick(contain("button", "Save"), driver);
    }

    public static void fillBrowserAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo, WebDriver driver) throws InterruptedException {
        selectAutocompleteInput(STATUS, advertPrimaryInfo.getStatus(), driver);
        selectAutocompleteInput(PRICING_MODEL, advertPrimaryInfo.getPricingModel(), driver);
        selectAutocompleteInput(MANAGER, advertPrimaryInfo.getManagerName(), driver);
        selectAutocompleteInput("Sales Manager", advertPrimaryInfo.getSalesManagerName(), driver);
        selectAutocompleteInput("Account Manager", advertPrimaryInfo.getAccountManagerName(), driver);
        selectAutocompleteInput("Categories", advertPrimaryInfo.getCategoriesName(), driver);
        selectAutocompleteInput("Tags", advertPrimaryInfo.getTagName(), driver);
        selectAutocompleteInput(USER_REQUEST_SOURCE, advertPrimaryInfo.getUserRequestSourceName(), driver);

        sendKeysByLabel(NAME, advertPrimaryInfo.getCompany(), driver);
        sendKeysByLabel(COMPANY_LEGAL_NAME, advertPrimaryInfo.getCompanyLegalName(), driver);
        sendKeysByLabel(SITE_URL, advertPrimaryInfo.getSiteUrl(), driver);
        sendKeysByLabel(USER_REQUEST_SOURCE_VALUE, advertPrimaryInfo.getUserRequestSourceValue(), driver);
        sendKeysToTextAreaByLabel(NOTES, advertPrimaryInfo.getNote(), driver);
        selectAutocompleteInput("GEO", advertPrimaryInfo.getGeo(), driver);

        // TODO Ждать пару секунд сообщение об ошибке

    }

    public static void buildBrowserAdvertContact(ArrayList<AdvertContact> advertContacts, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Contacts"), driver);
        Thread.sleep(3000);

        for (AdvertContact advertContact : advertContacts) {
            //TODO переделать для списка
            waitAndClick(contain("button", "Add Contact Person"), driver);
            fillBrowserAdvertContactInfo(advertContact.getPerson(),
                    advertContact.getStatus(), advertContact.getEmail(),
                    advertContact.getPosition(), driver);
            Thread.sleep(3000);

            // редактируем созданную сущность
           /* waitAndClick(forEditDeleteButtons("div", advertContact.getPerson(), "Edit"), driver);
            fillBrowserAdvertContactInfo(advertContact.getPerson(),
                    advertContact.getStatus(), advertContact.getEmail(),
                    advertContact.getPosition(), driver);            Thread.sleep(3000);*/

            // Удаляем созданную сущность
           // waitAndClick(forEditDeleteButtons("div", advertContact.getPerson(), "Delete"), driver);
            // Сообщение с подтверждением
        }
    }

    public static void fillBrowserAdvertContactInfo(String contactPersonValue,
                                                    String contactStatusValue,
                                                    String contactEmailValue,
                                                    String contactPositionValue,
                                                    WebDriver driver) throws InterruptedException {
        sendKeysByLabel("Contact person", contactPersonValue, driver);
        sendKeysByLabel("Position", contactPositionValue, driver);

        selectAutocompleteInput("Contact status", contactStatusValue, driver);
        sendKeysByLabel("Email", contactEmailValue, driver);
        waitAndClick(contain("button", "Save"), driver);
    }

    public static void buildBrowserAdvertRequisites(ArrayList<AdvertRequisites> advertRequisites, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Payment info"), driver);
        waitAndClick(contain("button", "+ Add"), driver);

        for (AdvertRequisites advertRequisite : advertRequisites) {
            selectAutocompleteInput("Payment method", advertRequisite.getPaymentSystemTitle(), driver);
            selectAutocompleteInput("Currency", advertRequisite.getCurrency(), driver);

            for (Map.Entry<String, String> entry : advertRequisite.getRequisites().entrySet()) {
                sendKeysByLabel((String) entry.getKey(), entry.getValue(), driver);
            }
            waitAndClick(contain("button", "Save"), driver);
        }
    }

    public static void buildBrowserAdvertPostback(AdvertPostback advertPostback, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Postbacks"), driver);

        selectAutocompleteInput("Allowed IPs", advertPostback.getAllowedIp(), driver);
        selectAutocompleteInput("Allowed sub account", advertPostback.getAllowedSubAccount(), driver);
        selectAutocompleteInput("Forbidden sub account", advertPostback.getDisallowedSubAccount(), driver);

        if (advertPostback.getForbidChangePostbackStatus().equals(true)) {
            WebElement checkbox = driver.findElement(By.id("forbidChangePostbackStatus"));
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
        waitAndClick(contain("button", "Save"), driver);
    }

    public static void buildBrowserAdvertNotes(ArrayList<AdvertNotes> advertNotes, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", "Notes"), driver);

        for (AdvertNotes advertNote : advertNotes) {
            waitAndClick(contain("button", "Add new note"), driver);
            selectAutocompleteInput("Type", advertNote.getType(), driver);
            if (advertNote.getLocation() != null) {
                selectAutocompleteInput("Location", advertNote.getLocation(), driver);
            }
            sendKeysToTextAreaByLabel("Text", advertNote.getText(), driver);
            waitAndClick(contain("button", "Save"), driver);
        }
    }

    public static void buildBrowserAdvertFilter(Advert advert, WebDriver driver) throws InterruptedException {
        menuItemClick(ADVERTISERS, driver);

        selectAutocompleteInputByText("Sales Manager", advert.getAdvertPrimaryInfo().getSalesManagerName(), driver);
        selectAutocompleteInputByText("Account Manager", advert.getAdvertPrimaryInfo().getAccountManagerName(), driver);
        enterTextByPlaceholder("Site URL", advert.getAdvertPrimaryInfo().getSiteUrl(), driver);
        selectAutocompleteInputByText("Geo", advert.getAdvertPrimaryInfo().getGeo().getFirst(), driver);
        selectAutocompleteInputByText("Categories", advert.getAdvertPrimaryInfo().getCategoriesName().getFirst(), driver);
        selectAutocompleteInputByText(PRICING_MODEL, advert.getAdvertPrimaryInfo().getPricingModel().getFirst(), driver);
        selectAutocompleteInputByText(STATUS, advert.getAdvertPrimaryInfo().getStatus(), driver);
        enterTextByPlaceholder(NOTES, advert.getAdvertPrimaryInfo().getNote(), driver);
        enterTextByPlaceholder(COMPANY_LEGAL_NAME, advert.getAdvertPrimaryInfo().getCompanyLegalName(), driver);
        selectAutocompleteInputByText(MANAGER, advert.getAdvertPrimaryInfo().getManagerName(), driver);
        enterTextByPlaceholder("Contact", advert.getAdvertContact().getFirst().getEmail(), driver);
        selectAutocompleteInputByText(USER_REQUEST_SOURCE, advert.getAdvertPrimaryInfo().getUserRequestSourceName(), driver);
        enterTextByPlaceholder(USER_REQUEST_SOURCE_VALUE, advert.getAdvertPrimaryInfo().getUserRequestSourceValue(), driver);
        selectAutocompleteInputByText(TAG, advert.getAdvertPrimaryInfo().getTagName().getFirst(), driver);
        enterTextByPlaceholder("Person", advert.getAdvertContact().getFirst().getPerson(), driver);
        selectAutocompleteInputByText("Payment Type", advert.getAdvertRequisites().getFirst().getPaymentSystemTitle(), driver);
        enterTextByPlaceholder("Payment Value", getRandomValue(advert.getAdvertRequisites().getFirst().getRequisites()).toString(), driver);
        waitAndClick(contain("button", "Show results"), driver);
    }
}

