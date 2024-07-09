package SettingsPackage.entity;

import Helper.GeoAndLang;

import java.util.Random;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.*;
import static Helper.Settings.*;

public class SettingsEmail {

    int emailId;
    String type;
    String protocol;
    String server;
    int port;
    String user;
    String password;

    public SettingsEmail()  {
    }

    public void fillSettingsEmailWithRandomDataForAPI() {
        this.type = getRandomKey(EMAIL_TYPE);
        this.protocol = getRandomKey(EMAIL_PROTOCOL);
        this.server = generateCompanyUrl(generateName(2, COMPANY_WORDS));
        this.port = generateRandomNumber(4);
        this.user = generateName(2, CONTACT_WORDS);
        this.password = generateName(4, CONTACT_WORDS);
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}