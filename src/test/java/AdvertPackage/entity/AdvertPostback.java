package AdvertPackage.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Helper.ActionsClass.getRandomBoolean;
import static Helper.Adverts.*;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class AdvertPostback {

    List<String> allowedIp;
    List<String> allowedSubAccount;
    List<String> disallowedSubAccount;
    Boolean forbidChangePostbackStatus;

    public AdvertPostback() {
        this.allowedIp =  new ArrayList<>(Arrays.asList(generateIPAddress(), generateIPAddress()));
        this.disallowedSubAccount = new ArrayList<>(Arrays.asList("Sub 1", "Sub 3", "Sub 4"));
        this.allowedSubAccount =  new ArrayList<>(Arrays.asList("Sub 8", "Sub 5", "Sub 2"));
        this.forbidChangePostbackStatus = getRandomBoolean();
    }

    public List<String> getAllowedIp() {
        return allowedIp;
    }

    public void setAllowedIp(List<String> allowedIp) {
        this.allowedIp = allowedIp;
    }

    public List<String> getAllowedSubAccount() {
        return allowedSubAccount;
    }

    public void setAllowedSubAccount(List<String> allowedSubAccount) {
        this.allowedSubAccount = allowedSubAccount;
    }

    public List<String> getDisallowedSubAccount() {
        return disallowedSubAccount;
    }

    public void setDisallowedSubAccount(List<String> disallowedSubAccount) {
        this.disallowedSubAccount = disallowedSubAccount;
    }
    public Boolean getForbidChangePostbackStatus() {
        return forbidChangePostbackStatus;
    }

    public void setForbidChangePostbackStatus(Boolean forbidChangePostbackStatus) {
        this.forbidChangePostbackStatus = forbidChangePostbackStatus;
    }

}
