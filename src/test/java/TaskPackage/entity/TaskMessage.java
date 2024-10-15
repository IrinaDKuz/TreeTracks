package TaskPackage.entity;

import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Tasks.*;
import static SQL.AdvertSQL.*;

public class TaskMessage {

    Integer taskId;
    Integer taskMessageId;
    MessageFile file;
    String text;

    String type;
    Integer authorId;
    Integer replyId;

    String date;

    Boolean isFeedback;
    Boolean isPin;
    Boolean isUpdatedConditions;



    public static class MessageFile {
        String hash;
        String ext;
        String name;

        public MessageFile() {

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public TaskMessage() {
    }

    public void fillTaskMessageWithRandomData() throws Exception {
        this.type = "message";
        /*  this.file = generateName(30, TASK_WORDS);*/
        this.text = generateName(30, TASK_WORDS);
        this.replyId = null;
    }

    public void fillTaskMessageWithRandomData(Integer taskId, Integer authorId) throws Exception {
        this.type = "message";
        /*  this.file = generateName(30, TASK_WORDS);*/
        this.text = generateName(30, TASK_WORDS);
        this.authorId = authorId;
        this.taskId = taskId;
        try {
            String replayedId = getRandomValueFromBDWhere("id", "task_message",
                    "task_id", String.valueOf(taskId));
            this.replyId = Integer.valueOf(replayedId);

        }
        catch (IllegalArgumentException e) {
            System.out.println(e);
            this.replyId = null;
        }
    }


    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskMessageId() {
        return taskMessageId;
    }

    public void setTaskMessageId(Integer taskMessageId) {
        this.taskMessageId = taskMessageId;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setFile(MessageFile file) {
        this.file = file;
    }

    public MessageFile getFile() {
        return file;
    }

    public Boolean getFeedback() {
        return isFeedback;
    }

    public void setFeedback(Boolean feedback) {
        isFeedback = feedback;
    }

    public Boolean getPin() {
        return isPin;
    }

    public void setPin(Boolean pin) {
        isPin = pin;
    }

    public Boolean getUpdatedConditions() {
        return isUpdatedConditions;
    }

    public void setUpdatedConditions(Boolean updatedConditions) {
        isUpdatedConditions = updatedConditions;
    }
}
