package AdvertPackage.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Helper.Adverts.*;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;

public class AdvertPrimaryInfo {
    String status;
    String company;
    String companyLegalName;
    String siteUrl;
    String modelType;
    String managerId;
    String manager;
    String salesManagerId;
    String salesManager;
    String accountManagerId;
    String accountManager;

    List<String> geo;
    List<String> categories;
    List<String> tag;
    String userRequestSourceId;
    String userRequestSourceName;
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

    public AdvertPrimaryInfo(){
    }

    public AdvertPrimaryInfo(int id) throws Exception {
        this.status = getValueFromAdvertPrimaryInfoBDWhere("status", id);
        this.company = getValueFromAdvertPrimaryInfoBDWhere("name", id);
        this.companyLegalName = getValueFromAdvertPrimaryInfoBDWhere("company_legalname", id);
        this.siteUrl = getValueFromAdvertPrimaryInfoBDWhere("site_url", id);
        this.modelType = getValueFromAdvertPrimaryInfoBDWhere("model_type", id);
        this.managerId = getValueFromAdvertPrimaryInfoBDWhere("manager_id", id);
        this.salesManagerId = getValueFromAdvertPrimaryInfoBDWhere("sales_manager", id);
        this.accountManagerId = getValueFromAdvertPrimaryInfoBDWhere("account_manager", id);
        this.geo = getArrayFromBDString(getValueFromAdvertPrimaryInfoBDWhere("geo", id));
        this.categories = getArrayFromBDWhere("category_id", "advert_category", "advert_id", String.valueOf(id));
        this.tag = getArrayFromBDString(getValueFromAdvertPrimaryInfoBDWhere("tag", id));
        this.userRequestSourceId = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_id", id);
        this.userRequestSourceValue = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_value", id);
        this.note = getValueFromAdvertPrimaryInfoBDWhere("note", id);
    }

    public void fillAdvertPrimaryInfoWithRandomDataForAPI() throws Exception {
        this.status = getRandomKey(STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.modelType = getRandomValue(MODEL_TYPES_MAP).toString();
        this.managerId = getRandomValueFromBD("id", "admin");
        this.salesManagerId = getRandomValueFromBD("id", "admin");
        this.accountManagerId = getRandomValueFromBD("id", "admin");
        this.geo = new ArrayList<>(Arrays.asList(
                getGeoRandomKey(),
                getGeoRandomKey(),
                getGeoRandomKey()));

        this.categories = new ArrayList<>(Arrays.asList(
                getRandomValueFromBDWhere("id", "category",
                        "lang", "'general'")));

        this.tag = new ArrayList<>(Arrays.asList(
                generateName(1, COMPANY_WORDS),
                generateName(1, COMPANY_WORDS)));

        this.userRequestSourceId = getRandomValueFromBD("id", "user_request_source");
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
    }

    public void fillAdvertPrimaryInfoWithRandomDataForUI() throws Exception {
        this.status = (String) getRandomValue(STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.modelType = getRandomValue(MODEL_TYPES_MAP).toString();
        this.manager = getRandomValueFromBD("name", "admin");
        this.salesManager = getRandomValueFromBD("name", "admin");
        this.accountManager = getRandomValueFromBD("name", "admin");

        this.geo = new ArrayList<>(Arrays.asList(
                generateName(1, GEO_ARRAY),
                generateName(1, GEO_ARRAY),
                generateName(1, GEO_ARRAY)));

        this.categories = new ArrayList<>(Arrays.asList(
                getRandomValueFromBDWhere("name", "category",
                        "lang", "'general'")));

        this.tag = new ArrayList<>(Arrays.asList(
                generateName(1, COMPANY_WORDS),
                generateName(1, COMPANY_WORDS)));

        this.userRequestSourceName = getRandomValueFromBD("name", "user_request_source");
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
    }






    private List<String> getArrayFromBDString(String stringFromBD) {
        String[] tagsArray = stringFromBD.replace("[", "").replace("]", "").replace("\"", "").split(",\\s*");
        return new ArrayList<>(Arrays.asList(tagsArray));
    }

    private String getValueFromAdvertPrimaryInfoBDWhere(String parameter, int id) throws Exception {
        return getValueFromBDWhere(parameter, "advert", "id", String.valueOf(id));
    }

    private List<String> getArrayFromAdvertPrimaryInfoBDWhere(String parameter, int id) throws Exception {
        return getArrayFromBDWhere(parameter, "advert", "id", String.valueOf(id));
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

    public String getSalesManagerId() {
        return salesManagerId;
    }

    public void setSalesManagerId(String salesManagerId) {
        this.salesManagerId = salesManagerId;
    }

    public String getAccountManagerId() {
        return accountManagerId;
    }

    public void setAccountManagerId(String accountManagerId) {
        this.accountManagerId = accountManagerId;
    }

    public List<String> getGeo() {
        return geo;
    }

    public void setGeo(List<String> geo) {
        this.geo = geo;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void addCategories(List<String> listCategories) {
        if (categories.isEmpty())
            categories = listCategories;
        else {
            for (String category : listCategories) {
                if (!categories.contains(category)) {
                    categories.add(category);
                }
            }
        }
    }

    public void deleteCategories(List<String> listCategories) {
        this.categories.removeAll(listCategories);
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public void addTag(List<String> tags) {
        tag.remove("null");
        if (tag.isEmpty()) {
            tag = tags;
        } else {
            for (String tagToAdd : tags) {
                if (!tag.contains(tagToAdd)) {
                    tag.add(tagToAdd);
                }
            }
        }
    }


    public void deleteTag(List<String> tags) {
        this.tag.removeAll(tags);
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
