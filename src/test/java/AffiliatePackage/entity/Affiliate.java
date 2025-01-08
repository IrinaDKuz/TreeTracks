package AffiliatePackage.entity;

import AdvertPackage.entity.AdvertContact;

import java.util.*;
import java.util.stream.Collectors;

import static Helper.ActionsClass.generateRandomString;
import static Helper.Admin.generateEmail;
import static Helper.Adverts.*;
import static Helper.Affiliates.ADDRESS_TERMS;
import static Helper.Affiliates.AFFILIATE_STATUS_MAP;
import static Helper.GeoAndLang.*;
import static SQL.AdvertSQL.*;

public class Affiliate {
    int id;
    String email;
    String name;

    String source;
    String timeZone;

    String plainPassword;
    Integer managerId;
    String status;
    Integer referralPercent;
    String address1;
    String address2;
    String city;
    String country;
    String zipCode;
    String refererDesc;
    String note;
    List<String> allowedSubAccount;
    List<String> disallowedSubAccount;
    Boolean hideConversionPercent;
    List<AdvertContact.Messenger> messenger;
    Set<Integer> category;
    Set<Integer> tag;
    Set<Integer> trafficSource;
    Set<Integer> trafficGeo;

    public Affiliate() {
    }

    public void fillAffiliateWithRandomData() throws Exception {
        this.email = generateEmail("affiliate" + generateName(3, FLOWER_WORDS));
        this.name = this.email;
        this.plainPassword = generateRandomString(15);
        this.managerId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.status = getRandomKey(AFFILIATE_STATUS_MAP);
        this.address1 = "address1 " + generateName(10, ADDRESS_TERMS);
        this.address2 = "address2 " + generateName(10, ADDRESS_TERMS);
        this.city = getGeoRandomValue();
        this.country = getGeoRandomValue();
        this.zipCode = generateRandomString(32);
        this.refererDesc = generateName(10, FLOWER_WORDS);
        this.note = generateName(10, FLOWER_WORDS) + generateName(10, LOGIN_WORDS) ;
        this.disallowedSubAccount = new ArrayList<>(Arrays.asList("Sub 1", "Sub 3", "Sub 4"));
        this.allowedSubAccount = new ArrayList<>(Arrays.asList("Sub 8", "Sub 5", "Sub 2"));
        this.hideConversionPercent = new Random().nextBoolean();

        ArrayList<AdvertContact.Messenger> messengers = new ArrayList<>();
        for (int i = 0; i <= new Random().nextInt(3) + 1; i++) {
            AdvertContact.Messenger messenger = new AdvertContact.Messenger();
            messenger.generateMessenger();
            messengers.add(messenger);
        }
        this.messenger = messengers;

        this.category = getSomeValuesFromBDWhere("id", "category",
                "lang", "general", 3).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());

       /* this.tag = getSomeValuesFromBD("id", "affiliate_tag", 1)
                .stream().map(Integer::valueOf).collect(Collectors.toSet());*/


        this.tag = getSomeValuesFromBD("id", "affiliate_tag", 2)
                .stream().map(Integer::valueOf).collect(Collectors.toSet());



        this.trafficSource = getSomeValuesFromBDWhere("id", "traffic_source",
                "lang", "general", 3).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
        this.trafficGeo = getSomeValuesFromBD("id", "country", 3).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());;

    }

    public Affiliate(int id) throws Exception {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getReferralPercent() {
        return referralPercent;
    }

    public void setReferralPercent(Integer referralPercent) {
        this.referralPercent = referralPercent;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRefererDesc() {
        return refererDesc;
    }

    public void setRefererDesc(String refererDesc) {
        this.refererDesc = refererDesc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Boolean getHideConversionPercent() {
        return hideConversionPercent;
    }

    public void setHideConversionPercent(Boolean hideConversionPercent) {
        this.hideConversionPercent = hideConversionPercent;
    }

    public List<AdvertContact.Messenger> getMessenger() {
        return messenger;
    }

    public void setMessenger(List<AdvertContact.Messenger> messenger) {
        this.messenger = messenger;
    }

    public Set<Integer> getCategory() {
        return category;
    }

    public void setCategory(Set<Integer> category) {
        this.category = category;
    }

    public Set<Integer> getTag() {
        return tag;
    }

    public void setTag(Set<Integer> tag) {
        this.tag = tag;
    }

    public Set<Integer> getTrafficSource() {
        return trafficSource;
    }

    public void setTrafficSource(Set<Integer> trafficSource) {
        this.trafficSource = trafficSource;
    }

    public Set<Integer> getTrafficGeo() {
        return trafficGeo;
    }

    public void setTrafficGeo(Set<Integer> trafficGeo) {
        this.trafficGeo = trafficGeo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}