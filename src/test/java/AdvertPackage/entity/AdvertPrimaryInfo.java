package AdvertPackage.entity;

import java.util.*;
import java.util.stream.Collectors;

import static Helper.Adverts.*;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;

public class AdvertPrimaryInfo {

    Integer advertId;
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

    Integer activeOffersCount;
    Integer inactiveOffersCount;
    Integer totalOffersCount;
    Integer draftOffersCount;

    List<String> pricingModel;
    List<String> geo;
    List<String> geoAbb = new ArrayList<>();

    Set<Integer> categoriesId;
    List<String> categoriesName = new ArrayList<>();

    List<Integer> tagId;
    List<String> tagName = new ArrayList<>();

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

    public void fillAdvertPrimaryInfoFromBD(int id) throws Exception {
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
        this.categoriesId = getArrayFromBDWhere("category_id", "advert_category", "advert_id", String.valueOf(id))
                .stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());

        this.tagId = getArrayFromBDWhere("advert_tag_id", "advert_tag_relation",
                "advert_id", String.valueOf(id)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.userRequestSourceId = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_id", id);
        this.userRequestSourceValue = getValueFromAdvertPrimaryInfoBDWhere("user_request_source_value", id);
        this.note = getValueFromAdvertPrimaryInfoBDWhere("note", id);
    }

    public void fillAdvertPrimaryInfoWithOffersData(String advertID) throws Exception {
        this.activeOffersCount = getCountFromBDWhereAndWhere("offer",
                "status", "active", "=",
                "advert_id", advertID, "=");
        System.out.println("activeOffersCount : " + this.activeOffersCount);

        this.inactiveOffersCount = getCountFromBDWhereAndWhere("offer",
                "status", "active", "!=",
                "advert_id", advertID, "=");
        System.out.println("inactiveOffersCount : " + this.inactiveOffersCount);

        this.totalOffersCount = getCountFromBDWhere("offer", "advert_id", advertID);
        System.out.println("totalOffersCount : " + this.totalOffersCount);

        this.draftOffersCount = getCountFromBDWhere("offer_draft", "advert_id", advertID);
        System.out.println("draftOffersCount : " + this.draftOffersCount);
    }

    public void fillAdvertPrimaryInfoWithRandomData() throws Exception {
        this.status = getRandomKey(ADVERT_STATUS_MAP);
        this.company = generateName(3, COMPANY_WORDS);
        this.companyLegalName = generateName(4, COMPANY_WORDS);
        this.siteUrl = generateCompanyUrl(this.company);
        this.pricingModel = Arrays.asList(getRandomKey(MODEL_TYPES_MAP));

        this.managerId = getRandomValueFromBDWhere("id", "admin", "status", "enabled");
        String managerName = getRandomValueFromBDWhere("first_name", "admin", "id", managerId);
        this.managerName = managerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "id", managerId);
        this.accountManagerId = getRandomValueFromBDWhere("id", "admin", "status", "enabled");
        String accountManagerName = getRandomValueFromBDWhere("first_name", "admin", "id", accountManagerId);
        this.accountManagerName = accountManagerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "id", accountManagerId);

        this.salesManagerId = getRandomValueFromBDWhere("id", "admin", "status", "enabled");
        String salesManagerName = getRandomValueFromBDWhere("first_name", "admin", "id", salesManagerId);
        this.salesManagerName = salesManagerName + " " +
                getValueFromBDWhere("second_name", "admin",
                        "id", salesManagerId);


        // TODO: НУжно связать id с именами

        this.geo = new ArrayList<>(Arrays.asList(
                getGeoRandomValue(),
                getGeoRandomValue(),
                getGeoRandomValue()));

        for (String geo : this.geo)
            this.geoAbb.add(getKeyFromValue(geo, GEO_MAP));

        this.categoriesId = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", 2).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());

        for (int categoryID : this.categoriesId)
            this.categoriesName.add(getValueFromBDWhere("title", "category",
                    "id", String.valueOf(categoryID)));

        this.tagId = getSomeValuesFromBD("id", "advert_tag", 3)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        for (int tagId : this.tagId)
            this.tagName.add(getValueFromBDWhere("name", "advert_tag",
                    "id", String.valueOf(tagId)));

        this.userRequestSourceId = getRandomValueFromBD("id", "user_request_source");
        this.userRequestSourceName = getRandomValueFromBDWhere("name", "user_request_source",
                "id", this.userRequestSourceId);
        this.userRequestSourceValue = generateName(2, COMPANY_WORDS);
        this.note = generateName(10, COMPANY_WORDS);
    }

    public static List<String> getArrayFromBDString(String stringFromBD) {
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

    public Set<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(Set<Integer> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public void addCategories(Set<Integer> listCategories) {
        if (categoriesId.isEmpty())
            categoriesId = listCategories;
        else {
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

    public void deleteCategories(Set<Integer> listCategories) {
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
                    tagId.add(Integer.valueOf(tagToAdd));
                }
            }
        }
    }


    public void deleteTagId(Set<Integer> tags) {
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

    public List<String> getGeoAbb() {
        return geoAbb;
    }

    public void setGeoAbb(List<String> geoAbb) {
        this.geoAbb = geoAbb;
    }

    public Integer getActiveOffersCount() {
        return activeOffersCount;
    }

    public void setActiveOffersCount(Integer activeOffersCount) {
        this.activeOffersCount = activeOffersCount;
    }

    public Integer getInactiveOffersCount() {
        return inactiveOffersCount;
    }

    public void setInactiveOffersCount(Integer inactiveOffersCount) {
        this.inactiveOffersCount = inactiveOffersCount;
    }

    public Integer getTotalOffersCount() {
        return totalOffersCount;
    }

    public void setTotalOffersCount(Integer totalOffersCount) {
        this.totalOffersCount = totalOffersCount;
    }

    public Integer getDraftOffersCount() {
        return draftOffersCount;
    }

    public void setDraftOffersCount(Integer draftOffersCount) {
        this.draftOffersCount = draftOffersCount;
    }

    public Integer getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Integer advertId) {
        this.advertId = advertId;
    }
}
