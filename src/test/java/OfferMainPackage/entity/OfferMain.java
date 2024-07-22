package OfferMainPackage.entity;

import OfferDraftPackage.entity.OfferGeneral;

public class OfferMain {
    public OfferGeneral offerGeneral;
    int id;


    public OfferMain() {
        this.offerGeneral = new OfferGeneral();
    }

    public OfferGeneral getOfferGeneral() {
        return offerGeneral;
    }

    public void setOfferGeneral(OfferGeneral offerGeneral) {
        this.offerGeneral = offerGeneral;
    }

}