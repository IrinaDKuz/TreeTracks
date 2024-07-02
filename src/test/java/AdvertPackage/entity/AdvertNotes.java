package AdvertPackage.entity;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomValue;
import static SQL.AdvertSQL.*;

public class AdvertNotes {

    int notesId;
    int advert_id;
    int admin_id;

    String type;
    String location;
    String text;
    String adminTitle;
    String createdAt;

    public AdvertNotes() {
    }

    public void fillAdvertNotesWithRandomData() throws Exception {
        this.type = (String) getRandomValue(NOTES_TYPES_MAP);
        if (this.type.equals("Conference")) {
            this.location = getRandomValueFromBD("name", "event");
        }
        this.text = generateName(20, DESCRIPTION_WORDS);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAdminTitle() {
        return adminTitle;
    }

    public void setAdminTitle(String adminTitle) {
        this.adminTitle = adminTitle;
    }
}
