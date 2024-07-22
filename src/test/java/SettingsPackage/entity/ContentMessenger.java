package SettingsPackage.entity;
import static Helper.Adverts.generateName;
import static Helper.Settings.MESSENGERS_WORDS;

public class ContentMessenger {

    MessengerLang engMessengerLang;
    MessengerLang rusMessengerLang;
    MessengerLang generalMessengerLang;

    public static class MessengerLang {
        Integer messengerId;
        String messengerLang;
        String messengerTitle;
        Integer messengerParentId;

        public MessengerLang(String lang, String title) {
            this.messengerLang = lang;
            this.messengerTitle = lang + "_" + title;
        }

        public MessengerLang() {
        }

        public String getMessengerLang() {
            return messengerLang;
        }

        public void setMessengerLang(String messengerLang) {
            this.messengerLang = messengerLang;
        }

        public Integer getMessengerId() {
            return messengerId;
        }

        public void setMessengerId(Integer messengerId) {
            this.messengerId = messengerId;
        }

        public String getMessengerTitle() {
            return messengerTitle;
        }

        public void setMessengerTitle(String messengerTitle) {
            this.messengerTitle = messengerTitle;
        }

        public Integer getMessengerParentId() {
            return messengerParentId;
        }

        public void setMessengerParentId(Integer messengerParentId) {
            this.messengerParentId = messengerParentId;
        }
    }

    public ContentMessenger() {
    }

    public void fillContentMessengerWithRandomData() {
        String title = generateName(1, MESSENGERS_WORDS);
        this.engMessengerLang = new MessengerLang("eng", title);
        this.rusMessengerLang = new MessengerLang("rus", title);
        this.generalMessengerLang = new MessengerLang("general", title);
    }

    public MessengerLang getEngMessengerLang() {
        return engMessengerLang;
    }

    public void setEngMessengerLang(MessengerLang engMessengerLang) {
        this.engMessengerLang = engMessengerLang;
    }

    public MessengerLang getRusMessengerLang() {
        return rusMessengerLang;
    }

    public void setRusMessengerLang(MessengerLang rusMessengerLang) {
        this.rusMessengerLang = rusMessengerLang;
    }

    public MessengerLang getGeneralMessengerLang() {
        return generalMessengerLang;
    }

    public void setGeneralMessengerLang(MessengerLang generalMessengerLang) {
        this.generalMessengerLang = generalMessengerLang;
    }
}