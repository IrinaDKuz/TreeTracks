package OfferMainPackage.entity;

import java.util.*;
import java.util.stream.Collectors;

import static API.Helper.LANG;
import static Helper.ActionsClass.getCurrentDateAndTime;
import static Helper.ActionsClass.getFutureOrPastDate;
import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Offers.*;
import static SQL.AdvertSQL.*;

public class OfferMainGeneral {
    Integer advertId;
    List<Integer> categoriesId;
    List<String> categoriesName;
    List<Integer> tagId;
    List<String> tagName;
    List<Integer> trafficSourceId;
    List<String> trafficSourceName;
    String title;
    String status;
    String privacyLevel;
    Boolean statusNotice;
    OfferTemplate description;
    OfferTemplate kpi;
    OfferTemplate paidGoal;
    Boolean isTop;
    String notes;
    String payouts;
    String reconciliation;
    String releaseDate;
    String stopDate;
    Integer sendBeforeStoping;



    public static class OfferTemplate {


        public Map<String, String> templateMap = new HashMap<>();

        public OfferTemplate() {
            for (String lang : LANG) {
                templateMap.put(lang, generateName(20, OFFER_WORDS));
            }
        }

        public Map<String, String> getTemplateMap() {
            return templateMap;
        }

        public void setTemplateMap(Map<String, String> templateMap) {
            this.templateMap = templateMap;
        }
    }

    public OfferMainGeneral() {
    }

    public void fillOfferGeneralWithRandomDataForAPI() throws Exception {
        this.advertId = Integer.valueOf(getRandomValueFromBD("id", "advert"));
        this.title = generateName(3, OFFER_WORDS);
        this.status = getRandomKey(OFFER_STATUS_MAP);
        this.privacyLevel = getRandomKey(OFFER_PRIVACY_LEVEL);
        this.statusNotice = new Random().nextBoolean();
        this.description = new OfferTemplate();
        this.kpi = new OfferTemplate();
        this.paidGoal = new OfferTemplate();
        this.isTop = new Random().nextBoolean();
        this.notes = generateName(10, OFFER_WORDS);
        this.payouts = generateName(5, OFFER_WORDS);
        this.reconciliation = generateName(2, OFFER_WORDS);
        this.releaseDate = getCurrentDateAndTime();
        this.stopDate = getFutureOrPastDate(6);
        this.sendBeforeStoping = 21;
        this.trafficSourceId = getSomeValuesFromBDWhere("id", "traffic_source",
                "lang", "general", 3).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.tagId = getSomeValuesFromBD("id", "tag", 3).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.categoriesId = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", 3).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

    }


    public Integer getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Integer advertId) {
        this.advertId = advertId;
    }

    public List<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Integer> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public List<String> getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(List<String> categoriesName) {
        this.categoriesName = categoriesName;
    }

    public List<Integer> getTagId() {
        return tagId;
    }

    public void setTagId(List<Integer> tagId) {
        this.tagId = tagId;
    }

    public List<String> getTagName() {
        return tagName;
    }

    public void setTagName(List<String> tagName) {
        this.tagName = tagName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(String privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Boolean getStatusNotice() {
        return statusNotice;
    }

    public void setStatusNotice(Boolean statusNotice) {
        this.statusNotice = statusNotice;
    }

    public OfferTemplate getDescription() {
        return description;
    }

    public void setDescription(OfferTemplate description) {
        this.description = description;
    }

    public OfferTemplate getKpi() {
        return kpi;
    }

    public void setKpi(OfferTemplate kpi) {
        this.kpi = kpi;
    }

    public OfferTemplate getPaidGoal() {
        return paidGoal;
    }

    public void setPaidGoal(OfferTemplate paidGoal) {
        this.paidGoal = paidGoal;
    }

    public Boolean isTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPayouts() {
        return payouts;
    }

    public void setPayouts(String payouts) {
        this.payouts = payouts;
    }

    public String getReconciliation() {
        return reconciliation;
    }

    public void setReconciliation(String reconciliation) {
        this.reconciliation = reconciliation;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public Integer getSendBeforeStoping() {
        return sendBeforeStoping;
    }

    public void setSendBeforeStoping(Integer sendBeforeStoping) {
        this.sendBeforeStoping = sendBeforeStoping;
    }

    public List<Integer> getTrafficSourceId() {
        return trafficSourceId;
    }

    public void setTrafficSourceId(List<Integer> trafficSourceId) {
        this.trafficSourceId = trafficSourceId;
    }
}