package SettingsPackage.entity;

import static Helper.ActionsClass.getCurrentDate;
import static Helper.Adverts.generateName;
import static Helper.Settings.CATEGORIES_WORDS;

public class ContentTrafficSource {

    ContentTrafficLang engContentTrafficLang;
    ContentTrafficLang rusContentTrafficLang;
    ContentTrafficLang generalContentTrafficLang;

    public static class ContentTrafficLang {
        Integer contentTrafficId;
        String contentTrafficLang;
        String contentTrafficTitle;
        Integer contentTrafficParentId;

        public ContentTrafficLang(String lang, String title) {
            this.contentTrafficLang = lang;
            this.contentTrafficTitle = lang + "_" + title;
        }

        public ContentTrafficLang() {
        }


        public String getContentTrafficLang() {
            return contentTrafficLang;
        }

        public void setContentTrafficLang(String contentTrafficLang) {
            this.contentTrafficLang = contentTrafficLang;
        }

        public Integer getContentTrafficId() {
            return contentTrafficId;
        }

        public void setContentTrafficId(Integer contentTrafficId) {
            this.contentTrafficId = contentTrafficId;
        }

        public String getContentTrafficTitle() {
            return contentTrafficTitle;
        }

        public void setContentTrafficTitle(String contentTrafficTitle) {
            this.contentTrafficTitle = contentTrafficTitle;
        }

        public Integer getContentTrafficParentId() {
            return contentTrafficParentId;
        }

        public void setContentTrafficParentId(Integer contentTrafficParentId) {
            this.contentTrafficParentId = contentTrafficParentId;
        }

    }

    public ContentTrafficSource() {
    }

    public void fillContentContentTrafficWithRandomData() {
        String title = generateName(1, CATEGORIES_WORDS);
        this.engContentTrafficLang = new ContentTrafficLang("eng", title);
        this.rusContentTrafficLang = new ContentTrafficLang("rus", title);
        this.generalContentTrafficLang = new ContentTrafficLang("general", title);
    }


    public ContentTrafficLang getEngContentTrafficLang() {
        return engContentTrafficLang;
    }

    public void setEngContentTrafficLang(ContentTrafficLang engContentTrafficLang) {
        this.engContentTrafficLang = engContentTrafficLang;
    }

    public ContentTrafficLang getRusContentTrafficLang() {
        return rusContentTrafficLang;
    }

    public void setRusContentTrafficLang(ContentTrafficLang rusContentTrafficLang) {
        this.rusContentTrafficLang = rusContentTrafficLang;
    }

    public ContentTrafficLang getGeneralContentTrafficLang() {
        return generalContentTrafficLang;
    }

    public void setGeneralContentTrafficLang(ContentTrafficLang generalContentTrafficLang) {
        this.generalContentTrafficLang = generalContentTrafficLang;
    }
}