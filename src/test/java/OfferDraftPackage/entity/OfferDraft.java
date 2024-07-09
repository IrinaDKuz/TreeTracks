package OfferDraftPackage.entity;

import AdminPackage.entity.AdminGeneral;
import AdminPackage.entity.AdminPermissions;

public class OfferDraft {
    public OfferGeneral offerGeneral;
    int id;


    public OfferDraft() {
        this.offerGeneral = new OfferGeneral();
    }

    public OfferGeneral getOfferGeneral() {
        return offerGeneral;
    }

    public void setOfferGeneral(OfferGeneral offerGeneral) {
        this.offerGeneral = offerGeneral;
    }

}