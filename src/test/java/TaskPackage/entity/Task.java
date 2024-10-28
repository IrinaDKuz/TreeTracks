package TaskPackage.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;

public class Task {

    Integer taskId;

    String status;
    String type;
    String name;

    List<Integer> taskTag;

    Integer requesterId;
    Integer assigneeId;
    List<Integer> taskWatchers;

    Integer advertId;
    Integer offerId;
    Integer affiliateId;

    String notes;
    String dueDate;

    public Task() throws Exception {
        this.status = getRandomKey(TASKS_STATUS_MAP);

        this.taskTag = getSomeValuesFromBD("id", "task_tag", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.requesterId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.assigneeId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        List<Integer> watchersList = new java.util.ArrayList<>(getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5)
                .stream().map(Integer::valueOf).toList());

        watchersList.remove(this.assigneeId);
        watchersList.remove(this.requesterId);

        this.taskWatchers = watchersList;
        this.offerId = Integer.valueOf(getRandomValueFromBD("id", "offer"));
        this.advertId = Integer.valueOf(getValueFromBDWhere("advert_id", "offer", "id", this.offerId.toString()));
        this.affiliateId = Integer.valueOf(getRandomValueFromBD("id", "affiliate"));

        this.notes = generateName(30, TASK_WORDS);
        this.dueDate = generateDueDatePlusNDays(new Random().nextInt(15) + 1);
    }


    public Task(Integer taskId) throws Exception {
        this.taskId = taskId;
        this.status = getValueFromBDWhere("status", "task", "id", String.valueOf(taskId));
        this.type = getValueFromBDWhere("type", "task", "id", String.valueOf(taskId));

        this.requesterId = Integer.parseInt(getValueFromBDWhere("requester_id", "task",
                "id", String.valueOf(taskId)));

        this.assigneeId = Integer.parseInt(getValueFromBDWhere("assigne_id", "task",
                "id", String.valueOf(taskId)));

        String offerIdString = getValueFromBDWhere("offer_id", "task", "id", String.valueOf(taskId));
        this.offerId = (offerIdString.equals("null")) ? null : Integer.parseInt(offerIdString);

        String advertIdString = getValueFromBDWhere("advert_id", "task", "id", String.valueOf(taskId));
        this.advertId = (advertIdString.equals("null")) ? null : Integer.parseInt(advertIdString);

        String affiliateIdString = getValueFromBDWhere("affiliate_id", "task", "id", String.valueOf(taskId));
        this.affiliateId = (affiliateIdString.equals("null")) ? null : Integer.parseInt(affiliateIdString);

        this.notes = getValueFromBDWhere("notes", "task",
                "id", String.valueOf(taskId));

        String dateTimeString = getValueFromBDWhere("due_date", "task",
                "id", String.valueOf(taskId));
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dueDate  = dateTime.format(dateFormatter);

        this.taskTag = getArrayFromBDWhere("tag_id", "task_tag_relation", "task_id", String.valueOf(taskId))
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.taskWatchers = getArrayFromBDWhere("admin_id", "task_watcher", "task_id", String.valueOf(taskId))
                .stream().map(Integer::valueOf).collect(Collectors.toList());
    }

    public Task(Boolean isEmpty) throws Exception {
    }

    public Task(Integer taskId, Integer requesterId) throws Exception {
        this.taskId = taskId;
        this.requesterId = requesterId;

        this.status = getRandomKey(TASKS_STATUS_MAP);

        this.taskTag = getSomeValuesFromBD("id", "task_tag", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.assigneeId = Integer.valueOf(getRandomValueFromBDWhereExcept("id", "admin", "status", "enabled",
                this.requesterId.toString()));

        List<Integer> watchersList = new java.util.ArrayList<>(getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5)
                .stream().map(Integer::valueOf).toList());

        watchersList.remove(this.assigneeId);
        watchersList.remove(this.requesterId);

        this.taskWatchers = watchersList;
        this.offerId = Integer.valueOf(getRandomValueFromBD("id", "offer"));
        this.advertId = Integer.valueOf(getValueFromBDWhere("advert_id", "offer", "id", this.offerId.toString()));
        this.affiliateId = Integer.valueOf(getRandomValueFromBD("id", "affiliate"));

        this.notes = "This is some information about task " + generateName(30, TASK_WORDS);
        this.dueDate = generateDueDatePlusNDays(new Random().nextInt(15) + 1);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
