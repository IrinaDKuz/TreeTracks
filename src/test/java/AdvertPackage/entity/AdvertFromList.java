package AdvertPackage.entity;

import java.util.*;

public class AdvertFromList {

    Integer advertId;
    String name;
    String legalName;
    String status;
    Integer offerCount;
    List<String> pricingModel;
    List<String> geo;
    List<String> geoAbb = new ArrayList<>();
    List<Integer> categoriesId;
    List<String> categoriesName = new ArrayList<>();
    List<Integer> tagId;
    List<String> tagName = new ArrayList<>();
    Set<Integer> paymentTypeIds;
    String note;

    public Integer getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Integer advertId) {
        this.advertId = advertId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(Integer offerCount) {
        this.offerCount = offerCount;
    }

    public List<String> getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(List<String> pricingModel) {
        this.pricingModel = pricingModel;
    }

    public List<String> getGeo() {
        return geo;
    }

    public void setGeo(List<String> geo) {
        this.geo = geo;
    }

    public List<String> getGeoAbb() {
        return geoAbb;
    }

    public void setGeoAbb(List<String> geoAbb) {
        this.geoAbb = geoAbb;
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

    public Set<Integer> getPaymentTypeIds() {
        return paymentTypeIds;
    }

    public void setPaymentTypeIds(Set<Integer> paymentTypeIds) {
        this.paymentTypeIds = paymentTypeIds;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
