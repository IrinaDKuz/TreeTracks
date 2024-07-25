package OfferMainPackage.entity;

import OfferDraftPackage.entity.OfferBasicInfo;

public class OfferMain {
    public OfferBasicInfo offerBasicInfo;
    int id;


    public OfferMain() {
        this.offerBasicInfo = new OfferBasicInfo();
    }

    public OfferBasicInfo getOfferGeneral() {
        return offerBasicInfo;
    }

    public void setOfferGeneral(OfferBasicInfo offerBasicInfo) {
        this.offerBasicInfo = offerBasicInfo;
    }

}