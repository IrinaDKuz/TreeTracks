package TaskPackage.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static API.Helper.getValueFromJsonByKey;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;

public class GeneralTask {

    Integer taskId;
    String title;

    String status;
    String type;
    List<Integer> taskTag;

    Integer requesterId;
    Integer assigneeId;
    List<Integer> taskWatchers;

    Integer advertId;
    Integer offerId;
    Integer affiliateId;

    String description;
    String dueDate;

    public GeneralTask() {
    }

    public GeneralTask(Integer taskId) throws Exception {
        this.title = getValueFromJsonByKey(getValueFromBDWhere("data", "task",
                "id", String.valueOf(taskId)), "title");
        this.status = getValueFromBDWhere("status", "task", "id", String.valueOf(taskId));
        this.type = getValueFromBDWhere("type", "task", "id", String.valueOf(taskId));

        this.requesterId = Integer.parseInt(getValueFromBDWhere("requester_id", "task",
                "id", String.valueOf(taskId)));

        this.assigneeId = Integer.parseInt(getValueFromBDWhere("assigne_id", "task",
                "id", String.valueOf(taskId)));


        String offerIdString = getValueFromBDWhere("offer_id", "task", "id", String.valueOf(taskId));
        this.offerId = offerIdString.equals("null") ? null : Integer.parseInt(offerIdString);

        String advertIdString = getValueFromBDWhere("advert_id", "task",
                "id", String.valueOf(taskId));
        this.advertId = advertIdString.equals("null") ? null : Integer.parseInt(advertIdString);

        String affiliateIdString = getValueFromBDWhere("affiliate_id", "task",
                "id", String.valueOf(taskId));
        this.affiliateId = affiliateIdString.equals("null") ? null : Integer.parseInt(affiliateIdString);

        this.description = getValueFromBDWhere("notes", "task",
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

    public void fillTaskWithRandomData() throws Exception {
        this.title = generateName(4, TASK_WORDS);
        this.status = getRandomKey(TASKS_STATUS_MAP);
        this.taskTag = getSomeValuesFromBD("id", "task_tag", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());

        this.requesterId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.assigneeId = Integer.valueOf(getRandomValueFromBDWhere("id", "admin", "status", "enabled"));
        this.taskWatchers = getSomeValuesFromBDWhere("id", "admin", "status", "enabled", 5)
                .stream().map(Integer::valueOf).collect(Collectors.toList());
        this.offerId = Integer.valueOf(getRandomValueFromBD("id", "offer"));
        this.advertId = Integer.valueOf(getValueFromBDWhere("advert_id", "offer", "id", this.offerId.toString()));
        this.affiliateId = Integer.valueOf(getRandomValueFromBD("id", "affiliate"));
        this.description = generateName(30, TASK_WORDS);
        this.dueDate = generateDueDate();
    }


    public void fillGeneralTaskWithRandomData() throws Exception {
        this.type = "general";
        fillTaskWithRandomData();
    }


    public void fillTestConversionTaskWithRandomData() throws Exception {
        this.type = "test_conversion";
        fillTaskWithRandomData();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String notes) {
        this.description = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

}
