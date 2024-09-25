package AdvertPackage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static Helper.ActionsClass.getRandomDouble82;

public class AdvertPaymentInfo {
    BigDecimal minPayout;
    List<AdvertRequisites> advertRequisitesList = new ArrayList<>();

    public AdvertPaymentInfo() {
    }

    public void fillAdvertPaymentInfoWithRandomData() throws Exception {
        this.minPayout = BigDecimal.valueOf(getRandomDouble82());
    }

    public BigDecimal getMinPayout() {
        return minPayout;
    }

    public void setMinPayout(BigDecimal minPayout) {
        this.minPayout = minPayout;
    }

    public List<AdvertRequisites> getAdvertRequisitesList() {
        return advertRequisitesList;
    }

    public void setAdvertRequisitesList(List<AdvertRequisites> advertRequisitesList) {
        this.advertRequisitesList = advertRequisitesList;
    }

    public void addAdvertRequisitesList(AdvertRequisites advertRequisites) {
        this.advertRequisitesList.add(advertRequisites);
    }
}