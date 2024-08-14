package AdvertPackage.entity;

import java.util.*;
import java.util.stream.Collectors;

import static SQL.AdvertSQL.*;

public class AdvertContactDouble {
    String id;
    String advert1;
    String advert2;
    String contact_id1;
    String contact_id2;
    String field;
    String messenger_id1;
    String messenger_id2;
    String created_at;
    String updated_at;

    public AdvertContactDouble(String field,
                               String advert1, String advert2,
                               String contact_id1, String contact_id2,
                               String messenger_id1, String messenger_id2
                               ) {
        this.advert1 = advert1;
        this.advert2 = advert2;
        this.contact_id1 = contact_id1;
        this.contact_id2 = contact_id2;
        this.field = field;
        this.messenger_id1 = messenger_id1;
        this.messenger_id2 = messenger_id2;
    }


    public AdvertContactDouble() {
    }

    public void fillAdvertContactDoubleFromBD(String id) throws Exception {
        this.id = id;
        this.advert1 = getValueFromAdvertDoubleContactBD("advert1", id);
        this.advert2 = getValueFromAdvertDoubleContactBD("advert2", id);
        this.contact_id1 = getValueFromAdvertDoubleContactBD("contact_id1", id);
        this.contact_id2 = getValueFromAdvertDoubleContactBD("contact_id2", id);
        this.field = getValueFromAdvertDoubleContactBD("field", id);
        this.messenger_id1 = getValueFromAdvertDoubleContactBD("messenger_id1", id);
        this.messenger_id2 = getValueFromAdvertDoubleContactBD("messenger_id2", id);
        this.created_at = getValueFromAdvertDoubleContactBD("created_at", id);
        this.updated_at = getValueFromAdvertDoubleContactBD("updated_at", id);
    }

    private String getValueFromAdvertDoubleContactBD(String value, String id) throws Exception {
        return getValueFromBDWhere(value, "advert_contact_double", "id", id);
    }

    public static List<AdvertContactDouble> fillAdvertContactsDoubleFromBD() throws Exception {
        List<String> stringIds = getArrayFromBD("id", "advert_contact_double");
        // Преобразуем строки в Integer и сортируем
        List<Integer> ids = stringIds.stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());

        Collections.sort(ids);
        List<AdvertContactDouble> list = new ArrayList<>();
        for (int id : ids) {
            AdvertContactDouble advertContactDouble = new AdvertContactDouble();
            advertContactDouble.fillAdvertContactDoubleFromBD(String.valueOf(id));
            list.add(advertContactDouble);
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvert1() {
        return advert1;
    }

    public void setAdvert1(String advert1) {
        this.advert1 = advert1;
    }

    public String getAdvert2() {
        return advert2;
    }

    public void setAdvert2(String advert2) {
        this.advert2 = advert2;
    }

    public String getContact_id1() {
        return contact_id1;
    }

    public void setContact_id1(String contact_id1) {
        this.contact_id1 = contact_id1;
    }

    public String getContact_id2() {
        return contact_id2;
    }

    public void setContact_id2(String contact_id2) {
        this.contact_id2 = contact_id2;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessenger_id1() {
        return messenger_id1;
    }

    public void setMessenger_id1(String messenger_id1) {
        this.messenger_id1 = messenger_id1;
    }

    public String getMessenger_id2() {
        return messenger_id2;
    }

    public void setMessenger_id2(String messenger_id2) {
        this.messenger_id2 = messenger_id2;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
