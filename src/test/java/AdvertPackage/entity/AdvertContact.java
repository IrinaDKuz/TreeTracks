package AdvertPackage.entity;

import java.util.ArrayList;
import java.util.Random;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomValue;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;
import static SQL.AdvertSQL.getValueFromBDWhere;

public class AdvertContact {
    int contactID;
    String person;
    String status;
    String email;
    String created_at;
    String updated_at;

    ArrayList<Messenger> messengers;

    public static class Messenger {
        String messengerId;
        String messengerName;
        String messengerValue;

        public Messenger() {
        }

        public void generateMessenger() throws Exception {
            this.messengerId = getRandomValueFromBDWhere("id", "messenger_type", "lang", "'general'");
            this.messengerName = getValueFromBDWhere("title", "messenger_type", "id", this.messengerId);
            this.messengerValue = this.messengerName + " " + generateName(1, CONTACT_WORDS);
        }

        public String getMessengerId() {
            return messengerId;
        }

        public void setMessengerId(String messengerId) {
            this.messengerId = messengerId;
        }

        public String getMessengerValue() {
            return messengerValue;
        }

        public void setMessengerValue(String messengerValue) {
            this.messengerValue = messengerValue;
        }
    }

    public AdvertContact() {
    }

    public void fillAdvertContactWithRandomData() throws Exception {
        this.person = generateName(2, CONTACT_WORDS);
        this.status = getRandomValue(PERSON_STATUS_MAP).toLowerCase();
        this.email = generateEmail(this.person);
        ArrayList<Messenger> messengers = new ArrayList<>();
        for (int i = 0; i <= new Random().nextInt(5) + 1; i++) {
            Messenger messenger = new Messenger();
            messenger.generateMessenger();
            messengers.add(messenger);
        }
        this.messengers = messengers;
    }


    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public ArrayList<Messenger> getMessengers() {
        return messengers;
    }

    public void setMessengers(ArrayList<Messenger> messengers) {
        this.messengers = messengers;
    }
}