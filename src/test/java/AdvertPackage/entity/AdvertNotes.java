package AdvertPackage.entity;

import Helper.GeoAndLang;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;

public class AdvertNotes {

    int notesId;
    int advert_id;
    int admin_id;

    String type;
    String location;
    String locationId;

    String text;
    String adminTitle;
    String createdAt;

    public AdvertNotes() {
    }

    public void fillAdvertNotesWithRandomData() throws Exception {
        this.type =  getRandomValue(NOTES_TYPES_MAP);
        if (this.type.equals("Event")) {
            this.location = getRandomValueFromBD("name", "event");
        }
        this.text = generateName(20, DESCRIPTION_WORDS);
    }

    public void fillAdvertNotesWithRandomDataForAPI() throws Exception {
        this.type = getRandomKey(NOTES_TYPES_MAP);
        if (this.type.equals("event")) {
            this.location = getRandomValueFromBD("name", "event");
            this.locationId = getValueFromBDWhere("id", "event",
                    "name",   this.location  );

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
