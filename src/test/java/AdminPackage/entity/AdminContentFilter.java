package AdminPackage.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static Helper.Admin.ADMIN_STATUS_MAP;
import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.GeoAndLang.getRandomValue;
import static SQL.AdvertSQL.*;

public class AdminContentFilter {


    List<Integer> idInclude = new ArrayList<>();
    List<Integer> idExclude = new ArrayList<>();

    List<Integer> managerIdInclude;
    List<Integer> managerIdExclude;

    List<Integer> salesManagerInclude;
    List<Integer> salesManagerExclude;

    List<Integer> accountManagerInclude;
    List<Integer> accountManagerExclude;

    List<Integer> userRequestSourceInclude;
    List<Integer> userRequestSourceExclude;

    List<Integer> tagInclude;
    List<Integer> tagExclude;

    List<Integer> categoryInclude;
    List<Integer> categoryExclude;

    List<String> pricingModelInclude;
    List<String> pricingModelExclude;

    List<String> statusInclude;
    List<String> statusExclude;

    List<String> geoInclude;
    List<String> geoExclude;



    public AdminContentFilter() {
    }

    public void fillAdminGeneralWithRandomDataForAPI() throws Exception {
        this.idInclude = getSomeValuesFromBD("id", "advert", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        this.idExclude = getSomeValuesFromBD("id", "advert", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.managerIdInclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        this.managerIdExclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.accountManagerInclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        this.accountManagerExclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.salesManagerInclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        this.salesManagerExclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.userRequestSourceInclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        this.userRequestSourceExclude = getSomeValuesFromBD("id", "admin", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.categoryInclude = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.categoryExclude = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.tagInclude = getSomeValuesFromBD("id", "advert_tag",
                 new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        this.tagExclude = getSomeValuesFromBD("id", "advert_tag",
                new Random().nextInt(2)).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        getRandomKey(ADMIN_STATUS_MAP);

    }


    public List<Integer> getIdInclude() {
        return idInclude;
    }

    public void setIdInclude(List<Integer> idInclude) {
        this.idInclude = idInclude;
    }

    public List<Integer> getIdExclude() {
        return idExclude;
    }

    public void setIdExclude(List<Integer> idExclude) {
        this.idExclude = idExclude;
    }

    public List<Integer> getManagerIdInclude() {
        return managerIdInclude;
    }

    public void setManagerIdInclude(List<Integer> managerIdInclude) {
        this.managerIdInclude = managerIdInclude;
    }

    public List<Integer> getManagerIdExclude() {
        return managerIdExclude;
    }

    public void setManagerIdExclude(List<Integer> managerIdExclude) {
        this.managerIdExclude = managerIdExclude;
    }

    public List<Integer> getSalesManagerInclude() {
        return salesManagerInclude;
    }

    public void setSalesManagerInclude(List<Integer> salesManagerInclude) {
        this.salesManagerInclude = salesManagerInclude;
    }

    public List<Integer> getSalesManagerExclude() {
        return salesManagerExclude;
    }

    public void setSalesManagerExclude(List<Integer> salesManagerExclude) {
        this.salesManagerExclude = salesManagerExclude;
    }

    public List<Integer> getAccountManagerInclude() {
        return accountManagerInclude;
    }

    public void setAccountManagerInclude(List<Integer> accountManagerInclude) {
        this.accountManagerInclude = accountManagerInclude;
    }

    public List<Integer> getAccountManagerExclude() {
        return accountManagerExclude;
    }

    public void setAccountManagerExclude(List<Integer> accountManagerExclude) {
        this.accountManagerExclude = accountManagerExclude;
    }

    public List<String> getPricingModelInclude() {
        return pricingModelInclude;
    }

    public void setPricingModelInclude(List<String> pricingModelInclude) {
        this.pricingModelInclude = pricingModelInclude;
    }

    public List<String> getPricingModelExclude() {
        return pricingModelExclude;
    }

    public void setPricingModelExclude(List<String> pricingModelExclude) {
        this.pricingModelExclude = pricingModelExclude;
    }

    public List<String> getStatusInclude() {
        return statusInclude;
    }

    public void setStatusInclude(List<String> statusInclude) {
        this.statusInclude = statusInclude;
    }

    public List<String> getStatusExclude() {
        return statusExclude;
    }

    public void setStatusExclude(List<String> statusExclude) {
        this.statusExclude = statusExclude;
    }

    public List<Integer> getUserRequestSourceInclude() {
        return userRequestSourceInclude;
    }

    public void setUserRequestSourceInclude(List<Integer> userRequestSourceInclude) {
        this.userRequestSourceInclude = userRequestSourceInclude;
    }

    public List<Integer> getUserRequestSourceExclude() {
        return userRequestSourceExclude;
    }

    public void setUserRequestSourceExclude(List<Integer> userRequestSourceExclude) {
        this.userRequestSourceExclude = userRequestSourceExclude;
    }

    public List<Integer> getTagInclude() {
        return tagInclude;
    }

    public void setTagInclude(List<Integer> tagInclude) {
        this.tagInclude = tagInclude;
    }

    public List<Integer> getTagExclude() {
        return tagExclude;
    }

    public void setTagExclude(List<Integer> tagExclude) {
        this.tagExclude = tagExclude;
    }

    public List<String> getGeoInclude() {
        return geoInclude;
    }

    public void setGeoInclude(List<String> geoInclude) {
        this.geoInclude = geoInclude;
    }

    public List<String> getGeoExclude() {
        return geoExclude;
    }

    public void setGeoExclude(List<String> geoExclude) {
        this.geoExclude = geoExclude;
    }

    public List<Integer> getCategoryInclude() {
        return categoryInclude;
    }

    public void setCategoryInclude(List<Integer> categoryInclude) {
        this.categoryInclude = categoryInclude;
    }

    public List<Integer> getCategoryExclude() {
        return categoryExclude;
    }

    public void setCategoryExclude(List<Integer> categoryExclude) {
        this.categoryExclude = categoryExclude;
    }


}