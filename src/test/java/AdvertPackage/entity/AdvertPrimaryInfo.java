package AdvertPackage.entity;

import static Helper.Adverts.*;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.GEO_ARRAY;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class AdvertPrimaryInfo {
    String status;
    String company;
    String companyLegalName;
    String siteUrl;
    String modelType;
    String managerId;
    String salesManager;
    String accountManager;
    String[] geo;
    String[] categories;
    String[] tag;
    String userRequestSourceId;
    String userRequestSourceValue;
    String note;

    String address1;
    String address2;
    String city;
    String country;
    String createdAt;
    String currency;
    int id;
    String offerCount;
    String paymentMethod;
    String registrationNumber;
    String s2s;
    String secure_postback_code;
    String updatedAt;
    String zip_code;

    public AdvertPrimaryInfo() throws Exception {
        this.status = (String) getRandomValue(STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.modelType = getRandomValue(MODEL_TYPES_MAP).toString();
        this.managerId = getRandomValueFromBD("email", "admin");
        this.salesManager = getRandomValueFromBD("email", "admin");
        this.accountManager = getRandomValueFromBD("email", "admin");

        //TODO: из-за непонятных стран пока не понятно как реализовывать
        this.geo = new String[]{generateName(1, GEO_ARRAY),
                generateName(1, GEO_ARRAY), generateName(1, GEO_ARRAY)};

        // TODO надо брать рандомно из БД из-за языков пока не понятно как реализовывать
        this.categories = new String[]{getRandomValueFromBDWhere("title", "category", "lang", "'general'")};

        this.tag = new String[]{generateName(1, COMPANY_WORDS), generateName(1, COMPANY_WORDS)};
        this.userRequestSourceId = getRandomValueFromBD("name", "user_request_source");
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyLegalName() {
        return companyLegalName;
    }

    public void setCompanyLegalName(String companyLegalName) {
        this.companyLegalName = companyLegalName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getSalesManager() {
        return salesManager;
    }

    public void setSalesManager(String salesManager) {
        this.salesManager = salesManager;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public String[] getGeo() {
        return geo;
    }

    public void setGeo(String[] geo) {
        this.geo = geo;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getUserRequestSourceId() {
        return userRequestSourceId;
    }

    public void setUserRequestSourceId(String userRequestSourceId) {
        this.userRequestSourceId = userRequestSourceId;
    }

    public String getUserRequestSourceValue() {
        return userRequestSourceValue;
    }

    public void setUserRequestSourceValue(String userRequestSourceValue) {
        this.userRequestSourceValue = userRequestSourceValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
