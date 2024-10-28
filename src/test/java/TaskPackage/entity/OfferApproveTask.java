package TaskPackage.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Helper.Adverts.getExistManagerFromAdvert;
import static SQL.AdvertSQL.*;

public class OfferApproveTask extends Task {

    public OfferApproveTask() throws Exception {
        super();
        this.type = "offer_approval";
        this.name = "offer-approval";
    }

    public OfferApproveTask(Integer taskId) throws Exception {
        super(taskId);
        this.type = "offer_approval";
        this.name = "offer-approval";
    }

    public OfferApproveTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId, requesterId);
        this.type = "offer_approval";
        this.name = "offer-approval";
    }
}
