package AdminPackage.entity;

import static Helper.Adverts.*;
import static Helper.Adverts.generateName;
import static SQL.AdvertSQL.getRandomValueFromBD;

public class AdminGeneral {
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
    String[] allowedIp;
    String[] allowedSubAccount;
    String[] disallowedSubAccount;
    String userRequestSourceId;
    String userRequestSourceValue;
    String note;
    Boolean forbidChangePostbackStatus;

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

    public AdminGeneral() throws Exception {
        this.status = getRandomValue(STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.modelType = getRandomValue(MODEL_TYPES_MAP);
        this.managerId = getRandomValueFromBD("email", "admin");
        this.salesManager = getRandomValueFromBD("email", "admin");
        this.accountManager = getRandomValueFromBD("email", "admin");

        //TODO: из-за непонятных стран пока не понятно как реализовывать
        this.geo = new String[]{"Albania", "Armenia", "Austria"};

        // TODO надо брать рандомно из БД из-за языков пока не понятно как реализовывать
        this.categories = new String[]{"Forex"};

        this.tag = new String[]{generateName(1, COMPANY_WORDS), generateName(1, COMPANY_WORDS)};
        this.allowedIp = new String[]{ generateIPAddress(), generateIPAddress()};
        this.disallowedSubAccount = new String[]{"Sub 1", "Sub 3", "Sub 4"};
        this.allowedSubAccount = new String[]{"Sub 8", "Sub 5", "Sub 2"};
        this.userRequestSourceId = getRandomValueFromBD("name", "user_request_source");
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
        this.forbidChangePostbackStatus = true;
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

    public String[] getAllowedIp() {
        return allowedIp;
    }

    public void setAllowedIp(String[] allowedIp) {
        this.allowedIp = allowedIp;
    }

    public String[] getAllowedSubAccount() {
        return allowedSubAccount;
    }

    public void setAllowedSubAccount(String[] allowedSubAccount) {
        this.allowedSubAccount = allowedSubAccount;
    }

    public String[] getDisallowedSubAccount() {
        return disallowedSubAccount;
    }

    public void setDisallowedSubAccount(String[] disallowedSubAccount) {
        this.disallowedSubAccount = disallowedSubAccount;
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

    public Boolean getForbidChangePostbackStatus() {
        return forbidChangePostbackStatus;
    }

    public void setForbidChangePostbackStatus(Boolean forbidChangePostbackStatus) {
        this.forbidChangePostbackStatus = forbidChangePostbackStatus;
    }


}
