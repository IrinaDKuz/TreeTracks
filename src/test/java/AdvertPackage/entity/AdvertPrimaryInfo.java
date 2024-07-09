package AdvertPackage.entity;

import java.util.*;
import java.util.stream.Collectors;

import static Helper.Adverts.*;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;

public class AdvertPrimaryInfo {
    String status;
    String company;
    String companyLegalName;
    String siteUrl;

    String managerId;
    String managerName;

    String salesManagerId;
    String salesManagerName;

    String accountManagerId;
    String accountManagerName;


    List<String> pricingModel;
    List<String> geo;


    List<Integer> categoriesId;
    List<String> categoriesName;


    List<Integer> tagId;
    List<String> tagName;


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

    public AdvertPrimaryInfo() {
    }

    public AdvertPrimaryInfo(int id) throws Exception {
        this.status = getValueFromAdvertPrimaryInfoBDWhere("status", id);
        this.company = getValueFromAdvertPrimaryInfoBDWhere("name", id);
        this.companyLegalName = getValueFromAdvertPrimaryInfoBDWhere("company_legalname", id);
        this.siteUrl = getValueFromAdvertPrimaryInfoBDWhere("site_url", id);
        this.pricingModel = Collections.singletonList(
                getValueFromAdvertPrimaryInfoBDWhere("pricing_model", id));
        this.managerId = getValueFromAdvertPrimaryInfoBDWhere("manager_id", id);
        this.salesManagerId = getValueFromAdvertPrimaryInfoBDWhere("sales_manager", id);
        this.accountManagerId = getValueFromAdvertPrimaryInfoBDWhere("account_manager", id);
        this.geo = getArrayFromBDString(getValueFromAdvertPrimaryInfoBDWhere("geo", id));
        this.categoriesName = getArrayFromBDWhere("category_id", "advert_category", "advert_id", String.valueOf(id));
        // ToDo: tag переделать на выбор из смежной таблицы
        this.tagName = getArrayFromBDString(getValueFromAdvertPrimaryInfoBDWhere("tag", id));
        this.userRequestSourceId = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_id", id);
        this.userRequestSourceValue = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_value", id);
        this.note = getValueFromAdvertPrimaryInfoBDWhere("note", id);
    }

    public void fillAdvertPrimaryInfoWithRandomDataForAPI() throws Exception {
        this.status = getRandomKey(ADVERT_STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.managerId = getRandomValueFromBD("id", "admin");
        this.salesManagerId = getRandomValueFromBD("id", "admin");
        this.accountManagerId = getRandomValueFromBD("id", "admin");
        this.geo = new ArrayList<>(Arrays.asList(
                getGeoRandomKey(),
                getGeoRandomKey(),
                getGeoRandomKey()));

        this.pricingModel = Arrays.asList(getRandomKey(MODEL_TYPES_MAP));
        this.categoriesId = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", 2)
                .stream().map(Integer::valueOf).collect(Collectors.toList());
        this.tagId = getSomeValuesFromBD("id", "advert_tag", 3)
                .stream().map(Integer::valueOf).collect(Collectors.toList());
        this.userRequestSourceId = getRandomValueFromBD("id", "user_request_source");
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
    }

    public void fillAdvertPrimaryInfoWithRandomDataForUI() throws Exception {
        this.status = getRandomValue(ADVERT_STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.pricingModel = Arrays.asList(getRandomValue(MODEL_TYPES_MAP));

        String managerName = getRandomValueFromBD("first_name", "admin");
        this.managerName = managerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "first_name", managerName);
        String salesManagerName = getRandomValueFromBD("first_name", "admin");
        this.salesManagerName = salesManagerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "first_name", salesManagerName);
        String accountManagerName = getRandomValueFromBD("first_name", "admin");
        this.accountManagerName = accountManagerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "first_name", accountManagerName);

        this.geo = new ArrayList<>(Arrays.asList(
                generateName(1, GEO_ARRAY),
                generateName(1, GEO_ARRAY),
                generateName(1, GEO_ARRAY)));

        this.categoriesName = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", 2);
        this.tagName = getSomeValuesFromBD("name", "advert_tag", 3);
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


    public List<String> getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(List<String> pricingModel) {
        this.pricingModel = pricingModel;
    }

    public List<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Integer> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public void addCategories(List<Integer> listCategories) {
        if (categoriesId.isEmpty())
            categoriesId = listCategories;
        else {
            // Удаляем категории, которые были удалены из базы

            Iterator<Integer> iterator = categoriesId.iterator();
            while (iterator.hasNext()) {
                Integer existCategory = iterator.next();
                if (!isInDatabaseWhere("id", existCategory.toString(), "category", "lang", "general")) {
                    iterator.remove(); // Безопасное удаление с помощью итератора
                }
            }
            for (Integer category : listCategories) {
                if (!categoriesId.contains(category)) {
                    categoriesId.add(category);
                }
            }
        }
    }

    public void deleteCategories(List<String> listCategories) {
        this.categoriesId.removeAll(listCategories);
    }

    public List<Integer> getTagId() {
        return tagId;
    }

    public void setTagId(List<Integer> tagId) {
        this.tagId = tagId;
    }

    public void addTagId(List<Integer> tags) {
        if (tagId.isEmpty()) {
            tagId = tags;
        } else {
            for (Integer tagToAdd : tags) {
                if (!tagId.contains(tagToAdd)) {
                    tagId.add(tagToAdd);
                }
            }
        }
    }


    public void deleteTagId(List<String> tags) {
        this.tagId.removeAll(tags);
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

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSalesManagerName() {
        return salesManagerName;
    }

    public void setSalesManagerName(String salesManagerName) {
        this.salesManagerName = salesManagerName;
    }

    public String getAccountManagerName() {
        return accountManagerName;
    }

    public void setAccountManagerName(String accountManagerName) {
        this.accountManagerName = accountManagerName;
    }

    public String getUserRequestSourceName() {
        return userRequestSourceName;
    }

    public void setUserRequestSourceName(String userRequestSourceName) {
        this.userRequestSourceName = userRequestSourceName;
    }

    public List<String> getTagName() {
        return tagName;
    }

    public void setTagName(List<String> tagName) {
        this.tagName = tagName;
    }


    public List<String> getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(List<String> categoriesName) {
        this.categoriesName = categoriesName;
    }


}
