package TaskPackage.entity;

import java.util.*;
import java.util.stream.Collectors;

import static Helper.Adverts.*;
import static Helper.GeoAndLang.getKeyFromValue;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.GeoAndLang.*;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;

public class FeedBackTask {

    Integer taskId;

    String status;
    String type;
    List<Integer> taskTag;

    Integer requesterId;
    Integer assigneeId;
    List<Integer> taskWatchers;

    Integer advertId;
    Integer offerId;
    Integer affiliateId;

    String notes;
    String dueDate;

    public FeedBackTask() {
    }

    public void fillFeedBackTaskWithRandomData() throws Exception {
        this.status = getRandomKey(TASKS_STATUS_MAP);
        this.type = "feedback";

        this.taskTag = getSomeValuesFromBD("id", "task_tag", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.requesterId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.assigneeId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.taskWatchers = getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.offerId = Integer.valueOf(getRandomValueFromBD("id", "offer"));
        this.advertId = Integer.valueOf(getValueFromBDWhere("advert_id", "offer", "id", this.offerId.toString()));
        this.affiliateId = Integer.valueOf(getRandomValueFromBD("id", "affiliate"));

        this.notes = generateName(30, TASK_WORDS);
        this.dueDate = generateDueDate();
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(List<Integer> taskTag) {
        this.taskTag = taskTag;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public List<Integer> getTaskWatchers() {
        return taskWatchers;
    }

    public void setTaskWatchers(List<Integer> taskWatchers) {
        this.taskWatchers = taskWatchers;
    }

    public Integer getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Integer advertId) {
        this.advertId = advertId;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public Integer getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(Integer affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
