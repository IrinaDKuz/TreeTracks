package AdvertPackage.entity;

import java.util.ArrayList;

public class Advert {
    int id;
    AdvertPrimaryInfo advertPrimaryInfo;
    ArrayList<AdvertContact> advertContact = new ArrayList<>();



    public Advert() throws Exception {
        this.advertPrimaryInfo = new AdvertPrimaryInfo();
        this.advertContact.add(new AdvertContact());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdvertPrimaryInfo getAdvertPrimaryInfo() {
        return advertPrimaryInfo;
    }

    public void setAdvertPrimaryInfo(AdvertPrimaryInfo advertPrimaryInfo) {
        this.advertPrimaryInfo = advertPrimaryInfo;
    }

    public ArrayList<AdvertContact> getAdvertContact() {
        return advertContact;
    }

    public void setAdvertContact(ArrayList<AdvertContact> advertContact) {
        this.advertContact = advertContact;
    }


}