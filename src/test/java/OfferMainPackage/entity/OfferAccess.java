package OfferMainPackage.entity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Settings.generateRandomNumber;
import static SQL.AdvertSQL.getArrayFromBDWhere;

public class OfferAccess {
    String offerId;
    int accessId;
    String url;
    String type;
    String login;
    String password;
    String email;
    String description;

    public OfferAccess() {
    }

    public void fillOfferAccessWithRandomData(String offerId) throws Exception {
        this.offerId = offerId;
        this.url = generateCompanyUrl(generateName(3, LOGIN_WORDS));
        this.type = getRandomKey(ADVERT_ACCESS_TYPE);
        this.login = generateName(3, LOGIN_WORDS);
        this.password = generateName(2, LOGIN_WORDS) + generateRandomNumber(5);
        this.email = generateEmail(this.login);
        this.description = generateName(10, LOGIN_WORDS);
    }

    public String getAdvertId() {
        return offerId;
    }

    public void setAdvertId(String advertId) {
        this.offerId = advertId;
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
