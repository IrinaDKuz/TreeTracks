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
import static SQL.AdvertSQL.*;

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
        clearAndSendKeysByLabel(NAME, advertPrimaryInfo.getCompany(), driver);
        clearAndSendKeysByLabel(COMPANY_LEGAL_NAME, advertPrimaryInfo.getCompanyLegalName(), driver);
        clearAndSendKeysByLabel(SITE_URL, advertPrimaryInfo.getSiteUrl(), driver);

        selectAutocompleteInput(MANAGER, advertPrimaryInfo.getManagerName(), driver);
        selectAutocompleteInput(ACCOUNT_MANAGER, advertPrimaryInfo.getAccountManagerName(), driver);
        selectAutocompleteInput(SALES_MANAGER, advertPrimaryInfo.getSalesManagerName(), driver);
        selectAutocompleteInput(GEO, advertPrimaryInfo.getGeo(), driver);
        selectAutocompleteInput(PRICING_MODEL, advertPrimaryInfo.getPricingModel(), driver);
        selectAutocompleteInput(CATEGORIES, advertPrimaryInfo.getCategoriesName(), driver);
        selectAutocompleteInput(TAGS, advertPrimaryInfo.getTagName(), driver);
        selectAutocompleteInput(USER_REQUEST_SOURCE, advertPrimaryInfo.getUserRequestSourceName(), driver);
        clearAndSendKeysByLabel(USER_REQUEST_SOURCE_VALUE, advertPrimaryInfo.getUserRequestSourceValue(), driver);
        sendKeysToTextAreaByLabel(NOTES, advertPrimaryInfo.getNote(), driver);
        // TODO Ждать пару секунд сообщение об ошибке
    }

    public static void addBrowserAdvertContact(ArrayList<AdvertContact> advertContacts, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", CONTACTS), driver);
        Thread.sleep(3000);
        for (AdvertContact advertContact : advertContacts) {
            waitAndClick(contain("button", "Add Contact Person"), driver);
            fillBrowserAdvertContactInfo(advertContact, false, driver);
            Thread.sleep(3000);
        }
    }

    public static void editBrowserAdvertContact(AdvertContact advertContact, WebDriver driver) throws Exception {
        menuItemClick(ADVERTISERS, driver);
        int lastNumberWithContacts = Integer.parseInt(getLastValueFromBDWhereAdvertExist(
                "advert_id", "advert_contact"));
        String email = getRandomValueFromBDWhere("email", "advert_contact",
                "advert_id", String.valueOf(lastNumberWithContacts));
        waitAndClick(By.xpath("//a[text()='" + lastNumberWithContacts + "']/ancestor::tr//button"), driver);
        waitAndClick(contain("a", CONTACTS), driver);
        Thread.sleep(3000);
        waitAndClick(forPencilButton(email), driver);
        fillBrowserAdvertContactInfo(advertContact, true, driver);
        Thread.sleep(3000);

        // Удаляем отредактированную сущность
        waitAndClick(forBasketButton(advertContact.getEmail()), driver);
        acceptModalDialog(driver);
    }

    public static void fillBrowserAdvertContactInfo(AdvertContact advertContact, Boolean isEdit, WebDriver driver) throws InterruptedException {
        clearAndSendKeysByLabel("Contact person", advertContact.getPerson(), driver);
        clearAndSendKeysByLabel("Position", advertContact.getPosition(), driver);
        selectAutocompleteInput("Contact status", advertContact.getStatus(), driver);
        clearAndSendKeysByLabel("Email", advertContact.getEmail(), driver);
        int i = 1;
        int j = 1;

        if (!isEdit)
            for (AdvertContact.Messenger advertMessenger : advertContact.getMessengers()) {
                waitAndClick(textIsEqual("button", "Add Messanger"), driver);
                selectAutocompleteInput("Messenger", advertMessenger.getMessengerTypeName(), i, driver);
                sendKeys(By.xpath("(//*[@id='value'])[" + j + "]"), advertMessenger.getMessengerValue(), driver);
                i = i + 3;
                j++;
            }
        else {
            System.out.println("Реализовать для messengers Edit");
        }

        waitAndClick(contain("button", "Save"), driver);
    }

    public static void addBrowserAdvertRequisites(ArrayList<AdvertRequisites> advertRequisites, WebDriver driver) throws InterruptedException {
        waitAndClick(contain("a", PAYMENT_INFO), driver);

        for (AdvertRequisites advertRequisite : advertRequisites) {
            waitAndClick(contain("button", "+ Add"), driver);
            selectAutocompleteInput("Payment method", advertRequisite.getPaymentSystemTitle(), 1, driver);
            selectAutocompleteInput("Currency", advertRequisite.getCurrency(), 1, driver);

            for (Map.Entry<String, String> entry : advertRequisite.getRequisites().entrySet()) {
                sendKeysByLabel(entry.getKey(), entry.getValue(), driver);
            }
            waitAndClick(contain("button", "Save"), driver);
        }
    }

    public static void editBrowserAdvertRequisites(AdvertRequisites advertRequisite, WebDriver driver) throws Exception {
        menuItemClick(ADVERTISERS, driver);
        int lastNumberWithContacts = Integer.parseInt(getLastValueFromBD("advert_id", "advert_payment"));
        String paymentSystemId = getRandomValueFromBDWhere("payment_system_id", "advert_payment",
                "advert_id", String.valueOf(lastNumberWithContacts));

        String paymentSystemName = getValueFromBDWhere("title", "payment_system",
                "id", String.valueOf(paymentSystemId));

        waitAndClick(By.xpath("//a[text()='" + lastNumberWithContacts + "']/ancestor::tr//button"), driver);
        waitAndClick(contain("a", PAYMENT_INFO), driver);
        Thread.sleep(3000);

        waitAndClick(forEditButton(paymentSystemName), driver);
        selectAutocompleteInput("Payment method", advertRequisite.getPaymentSystemTitle(), driver);
        selectAutocompleteInput("Currency", advertRequisite.getCurrency(), driver);
        for (Map.Entry<String, String> entry : advertRequisite.getRequisites().entrySet()) {
            sendKeysByLabel(entry.getKey(), entry.getValue(), driver);
        }
        waitAndClick(contain("button", "Save"), driver);
        Thread.sleep(3000);

        // Удаляем отредактированную сущность
        waitAndClick(forDeleteButton(advertRequisite.getPaymentSystemTitle()), driver);
        acceptModalDialog(driver);
    }

    public static void addBrowserAdvertPostback(AdvertPostback advertPostback, WebDriver driver) throws InterruptedException {
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

    public static void editBrowserAdvertNotes(AdvertNotes advertNote, WebDriver driver) throws Exception {
        menuItemClick(ADVERTISERS, driver);
        int lastNumberWithNotes = Integer.parseInt(getLastValueFromBD("advert_id", "advert_notes"));
        String notesText = getValueFromBDWhere("text", "advert_notes",
                "advert_id", String.valueOf(lastNumberWithNotes));

        waitAndClick(penOnAdvertId(lastNumberWithNotes), driver);
        waitAndClick(contain("a", NOTES), driver);
        Thread.sleep(3000);

        waitAndClick(forPencilButtonNotes(notesText), driver);
        selectAutocompleteInput("Type", advertNote.getType(), driver);
        if (advertNote.getLocation() != null) {
            selectAutocompleteInput("Location", advertNote.getLocation(), driver);
        }
        sendKeysToTextAreaByLabel("Text", advertNote.getText(), driver);
        Thread.sleep(3000);
        waitAndClick(contain("button", "Save"), driver);
        // Удаляем отредактированную сущность
        waitAndClick(forBasketButtonNotes(advertNote.getText()), driver);
        acceptModalDialog(driver);
    }

public static void buildBrowserAdvertFilter(Advert advert, WebDriver driver) throws InterruptedException {
    menuItemClick(ADVERTISERS, driver);
    showAdvertPrimaryInfoInformation(advert.getAdvertPrimaryInfo());
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

