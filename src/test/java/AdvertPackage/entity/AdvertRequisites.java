package AdvertPackage.entity;

import java.util.*;

import static Helper.Adverts.*;
import static SQL.AdvertSQL.*;

public class AdvertRequisites {

    int requisitesId;
    String currency;
    int paymentSystemId;
    String paymentSystemTitle;
    Map<String, String> requisites = new HashMap<>();
    Boolean isDefault;

    public AdvertRequisites() {
    }

    public void fillAdvertRequisitesWithRandomData() throws Exception {
        this.paymentSystemId = Integer.parseInt(getRandomValueFromBD("id", "payment_system"));
        this.paymentSystemTitle = getValueFromBDWhere("title", "payment_system",
                "id", String.valueOf(this.paymentSystemId));
        this.currency = getRandomCurrencyFromBDPaymentSystem(this.paymentSystemId);
        this.isDefault =new Random().nextBoolean();

        List<String> requisitesTitle = getRequisitesFromBDPaymentSystem(this.paymentSystemId);
        for (String title : requisitesTitle) {
            this.requisites.put(title, generateName(1, REQUISITES_WORDS) + new Random().nextInt(10000));
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPaymentSystemId() {
        return paymentSystemId;
    }

    public void setPaymentSystemId(int paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }

    public String getPaymentSystemTitle() {
        return paymentSystemTitle;
    }

    public void setPaymentSystemTitle(String paymentSystemTitle) {
        this.paymentSystemTitle = paymentSystemTitle;
    }

    public Map<String, String> getRequisites() {
        return requisites;
    }

    public void setRequisites(Map<String, String> requisites) {
        this.requisites = requisites;
    }

    public int getRequisitesId() {
        return requisitesId;
    }

    public void setRequisitesId(int requisitesId) {
        this.requisitesId = requisitesId;
    }


    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

}
