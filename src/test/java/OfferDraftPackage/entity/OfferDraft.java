package OfferDraftPackage.entity;

public class OfferDraft {
    public OfferBasicInfo offerBasicInfo;
    int id;


    public OfferDraft() {
        this.offerBasicInfo = new OfferBasicInfo();
    }

    public OfferBasicInfo getOfferGeneral() {
        return offerBasicInfo;
    }

    public void setOfferGeneral(OfferBasicInfo offerBasicInfo) {
        this.offerBasicInfo = offerBasicInfo;
    }

}