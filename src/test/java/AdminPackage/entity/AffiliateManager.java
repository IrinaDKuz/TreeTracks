package AdminPackage.entity;

import java.util.List;
import java.util.Random;
import static Helper.Adverts.LOGIN_WORDS;
import static Helper.Adverts.generateName;
import static Helper.Settings.generateRandomNumber;
import static SQL.AdvertSQL.*;

public class AffiliateManager {
    String id;
    String adminId;
    String telegramUsername;
    String telegramId;
    String code;
    Boolean isAddToChat;
    Boolean isChatAdmin;

    public void fillAffiliateManagerWithRandomDataForAPI(String id) throws Exception {
        this.id = id;
        List<String> existId = getArrayFromBD("admin_id", "affiliate_manager");
        this.adminId = getRandomValueFromBDExcept("id", "admin", existId);
        this.telegramUsername = generateName(1, LOGIN_WORDS) + "_" + generateRandomNumber(10);
        this.telegramId = String.valueOf(generateRandomNumber(10));
        this.code = String.valueOf(generateRandomNumber(5));
        this.isAddToChat = new Random().nextBoolean();
        this.isChatAdmin = new Random().nextBoolean();;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getAddToChat() {
        return isAddToChat;
    }

    public void setAddToChat(Boolean addToChat) {
        isAddToChat = addToChat;
    }

    public Boolean getChatAdmin() {
        return isChatAdmin;
    }

    public void setChatAdmin(Boolean chatAdmin) {
        isChatAdmin = chatAdmin;
    }



}