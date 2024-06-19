package AdvertPackage.entity;

import static Helper.Adverts.*;
import static SQL.AdvertSQL.*;

public class AdvertNotes {

    int id;
    int advert_id;
    int admin_id;

    String type;
    String location = null;
    String text;

    public AdvertNotes() throws Exception {
        this.type = (String) getRandomValue(NOTES_TYPES_MAP);
        if (this.type.equals("Conference")) {
            this.location = getRandomValueFromBD("name", "conference");
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
}
