package AdvertPackage.entity;

import java.util.*;

import static Helper.Adverts.*;
import static SQL.AdvertSQL.*;

public class AdvertRequisites {

    String currency;
    int paymentSystemId;
    String paymentSystemTitle;
    Map<Object, String> requisites = new HashMap<>();

    public AdvertRequisites() throws Exception {
        this.paymentSystemId = Integer.parseInt(getRandomValueFromBD("id", "payment_system"));
        this.paymentSystemTitle = getValueFromBDWhere("title", "payment_system",
                "id", String.valueOf(this.paymentSystemId));
        this.currency = getRandomCurrencyFromBDPaymentSystem(this.paymentSystemId);
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

    public Map<Object, String> getRequisites() {
        return requisites;
    }

    public void setRequisites(Map<Object, String> requisites) {
        this.requisites = requisites;
    }

}
