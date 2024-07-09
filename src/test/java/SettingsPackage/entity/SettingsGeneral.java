package SettingsPackage.entity;

import java.util.Random;

import static Helper.Adverts.*;
import static Helper.Settings.*;
import static Helper.Settings.generateRandomNumber;

public class SettingsGeneral {
    String siteName;
    String city;
    String country;
    String defaultLanguage;
    String domain;
    String email;
    String phone;
    Boolean showCommentatorName;
    String timezone;
    String zipCode;

    public SettingsGeneral()  {
    }

    public void fillSettingsGeneralInfoWithRandomDataForAPI() throws Exception {
        this.siteName = generateCompanyUrl(generateName(4, COMPANY_WORDS));
        this.domain = generateCompanyUrl(generateName(2, COMPANY_WORDS));
        this.defaultLanguage = "eng";
        this.timezone = generateUTC();
        this.country = generateName(1, COUNTRY_LETTERS);
        this.city = generateName(1, CITIES_WORDS);
        this.zipCode = generateRandomNumber(10).toString();
        this.phone = generateRandomNumber(10).toString();
        this.email = generateEmail(generateName(2, COMPANY_WORDS));
        this.showCommentatorName = new Random().nextBoolean();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getShowCommentatorName() {
        return showCommentatorName;
    }

    public void setShowCommentatorName(Boolean showCommentatorName) {
        this.showCommentatorName = showCommentatorName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}