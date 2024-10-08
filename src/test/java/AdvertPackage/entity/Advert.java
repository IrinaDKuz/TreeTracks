package AdvertPackage.entity;

import java.util.ArrayList;
import java.util.Random;

public class Advert {
    int id;
    AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
    ArrayList<AdvertContact> advertContact = new ArrayList<>();
    ArrayList<AdvertRequisites> advertRequisites = new ArrayList<>();
    AdvertPostback advertPostback;
    ArrayList<AdvertNotes> advertNotes = new ArrayList<>();

    public Advert() throws Exception {
        this.advertPrimaryInfo = new AdvertPrimaryInfo();

        for (int i = 0; i <= new Random().nextInt(5) + 1; i++)
            this.advertContact.add(new AdvertContact());

        for (int i = 0; i <= new Random().nextInt(5) + 1; i++)
            this.advertRequisites.add(new AdvertRequisites());

        this.advertPostback = new AdvertPostback();
        for (int i = 0; i <= new Random().nextInt(5) + 1; i++)

            this.advertNotes.add(new AdvertNotes());
    }

    public Advert(int id) throws Exception {
        this.id = id;
        this.advertPrimaryInfo.fillAdvertPrimaryInfoFromBD(id);
      //  AdvertContact advertContact = new AdvertContact();
       // advertContact.fillAdvertContactFromBD();
        //this.advertRequisites.add(new AdvertRequisites(id));
        //this.advertPostback = new AdvertPostback(id);
        //this.advertNotes.add(new AdvertNotes(id));
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

    public ArrayList<AdvertRequisites> getAdvertRequisites() {
        return advertRequisites;
    }

    public void setAdvertRequisites(ArrayList<AdvertRequisites> advertRequisites) {
        this.advertRequisites = advertRequisites;
    }

    public AdvertPostback getAdvertPostback() {
        return advertPostback;
    }

    public void setAdvertPostback(AdvertPostback advertPostback) {
        this.advertPostback = advertPostback;
    }

    public ArrayList<AdvertNotes> getAdvertNotes() {
        return advertNotes;
    }

    public void setAdvertNotes(ArrayList<AdvertNotes> advertNotes) {
        this.advertNotes = advertNotes;
    }
}