package TaskPackage.entity;

public class FeedBackTask extends Task {

    public FeedBackTask() throws Exception {
        super();
        this.type = "feedback";
        this.name = "feedback";
    }

    public FeedBackTask(Integer taskId) throws Exception {
        super(taskId);
        this.name = "feedback";
    }

    public FeedBackTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.type = "feedback";
        this.name = "feedback";
    }
}
