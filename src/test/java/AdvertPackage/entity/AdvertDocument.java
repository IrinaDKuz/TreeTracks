package AdvertPackage.entity;

import java.nio.file.Paths;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.GeoAndLang.getRandomValue;
import static SQL.AdvertSQL.getRandomValueFromBD;
import static SQL.AdvertSQL.getValueFromBDWhere;

public class AdvertDocument {
    int documentId;
    String original_name;
    String description;
    String hash;
    String ext;
    String advert_id;
    String uploader_id;
    String filePath;

    public AdvertDocument() {
    }

    public void fillAdvertDocumentWithRandomDataForAPI() {
        this.original_name = "6_8_MB_test-document.pdf";
        this.description = generateName(20, FLOWER_WORDS);
        this.ext = "pdf";
        this.uploader_id = "1";
        this.filePath = Paths.get(System.getProperty("user.dir"), "6_8_MB_test-document.pdf").toString();
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(String advert_id) {
        this.advert_id = advert_id;
    }

    public String getUploader_id() {
        return uploader_id;
    }

    public void setUploader_id(String uploader_id) {
        this.uploader_id = uploader_id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
