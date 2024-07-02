package AdminPackage.entity;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomValue;

public class AdminContentFilter {
    String contactID;
    String person;
    String status;
    String email;
    String created_at;
    String updated_at;

    Messenger messenger;

    public class Messenger {
        // TODO Переделать
        String messengerId;
        String messengerValue;

        public Messenger() {
            this.messengerValue = getPerson();
        }
    }

    public AdminContentFilter() {
        this.person = generateName(2, CONTACT_WORDS);
        this.status = getRandomValue(PERSON_STATUS_MAP);
        this.email = generateEmail(this.person);
        this.messenger = new Messenger();
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
}