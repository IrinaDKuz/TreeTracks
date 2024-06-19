package AdvertPackage.entity;

import static Helper.ActionsClass.getRandomBoolean;
import static Helper.Adverts.*;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class AdvertPostback {

    String[] allowedIp;
    String[] allowedSubAccount;
    String[] disallowedSubAccount;
    Boolean forbidChangePostbackStatus;

    public AdvertPostback() {
        this.allowedIp = new String[]{ generateIPAddress(), generateIPAddress()};
        this.disallowedSubAccount = new String[]{"Sub 1", "Sub 3", "Sub 4"};
        this.allowedSubAccount = new String[]{"Sub 8", "Sub 5", "Sub 2"};
        this.forbidChangePostbackStatus = getRandomBoolean();
    }

    public String[] getAllowedIp() {
        return allowedIp;
    }

    public void setAllowedIp(String[] allowedIp) {
        this.allowedIp = allowedIp;
    }

    public String[] getAllowedSubAccount() {
        return allowedSubAccount;
    }

    public void setAllowedSubAccount(String[] allowedSubAccount) {
        this.allowedSubAccount = allowedSubAccount;
    }

    public String[] getDisallowedSubAccount() {
        return disallowedSubAccount;
    }

    public void setDisallowedSubAccount(String[] disallowedSubAccount) {
        this.disallowedSubAccount = disallowedSubAccount;
    }
    public Boolean getForbidChangePostbackStatus() {
        return forbidChangePostbackStatus;
    }

    public void setForbidChangePostbackStatus(Boolean forbidChangePostbackStatus) {
        this.forbidChangePostbackStatus = forbidChangePostbackStatus;
    }

}
