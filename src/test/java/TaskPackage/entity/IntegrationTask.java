package TaskPackage.entity;

import AdvertPackage.entity.AdvertPrimaryInfo;
import OfferDraftPackage.entity.OfferBasicInfo;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static API.Advert.AdvertAccessAPI.accessAdd;
import static API.Advert.AdvertPrimaryInfoAPI.primaryInfoAddEdit;
import static API.Helper.getValueFromJsonByKey;
import static API.Offer.OfferMain.OfferAccessAPI.offerAccessAdd;
import static API.Offer.OfferMain.OfferMainBasicInfoAPI.basicInfoEdit;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;
import static SQL.OfferSQL.getRandomOffer;

public class IntegrationTask extends Task {

    Integer accessId;
    Integer platformType;

    String file;
    String filePath;


    public IntegrationTask() throws Exception {
        super();
        this.type = "integration";
        this.name = "integration";
    }

    public IntegrationTask(Boolean isEmpty) throws Exception {
        super(isEmpty);
    }

    public IntegrationTask(Integer taskId) throws Exception {
        super(taskId);
        this.name = "integration";
        this.accessId = Integer.valueOf(getValueFromJsonByKey(getValueFromBDWhere("data", "task",
                "id", String.valueOf(taskId)), "accessId"));

        this.platformType = Integer.valueOf(getValueFromJsonByKey(getValueFromBDWhere("data", "task",
                "id", String.valueOf(taskId)), "platformType"));
        this.filePath = Paths.get(System.getProperty("user.dir"), "6_8_MB_test-document.pdf").toString();
    }

    public IntegrationTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.type = "integration";
        this.name = "integration";

        // Отредактируем рандомного Адверта, установив, setSalesManagerId = adminId
        AdvertPrimaryInfo advertPrimaryInfo = new AdvertPrimaryInfo();
        advertPrimaryInfo.fillAdvertPrimaryInfoWithRandomData();
        advertPrimaryInfo.setSalesManagerId(String.valueOf(requesterId));
        Integer advertId = getRandomAdvert();
        advertPrimaryInfo.setAdvertId(advertId);
        primaryInfoAddEdit(true, advertPrimaryInfo);

        this.advertId = advertId;

        // теперь нужно отредактировать рандомный Оффер, присоединив туда нашего Адверта
        Integer offerId = getRandomOffer();

        OfferBasicInfo offerBasicInfoEdit = new OfferBasicInfo();
        offerBasicInfoEdit.fillOfferBasicInfoWithRandomDataForAPI();
        offerBasicInfoEdit.setAdvertId(advertId);
        offerBasicInfoEdit.setOfferId(offerId);
        basicInfoEdit(offerBasicInfoEdit);

        this.offerId = offerId;

        // Добавляем  Access
        offerAccessAdd(offerId);

        this.accessId = Integer.valueOf(getLastValueFromBDWhere("advert_access_id", "advert_access_offer",
                "offer_id", String.valueOf(this.offerId), "advert_access_id") );

        this.platformType = Integer.valueOf(getRandomValueFromBD("id", "platform"));
        this.filePath = Paths.get(System.getProperty("user.dir"), "6_8_MB_test-document.pdf").toString();
    }

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
