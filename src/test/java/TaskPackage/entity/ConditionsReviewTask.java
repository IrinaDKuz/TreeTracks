package TaskPackage.entity;

public class ConditionsReviewTask extends Task {

    public ConditionsReviewTask() throws Exception {
        super();
        this.type = "conditions_review";
        this.name = "conditions-review";
    }

    public ConditionsReviewTask(Integer taskId) throws Exception {
        super(taskId);
        this.type = "conditions_review";
        this.name = "conditions-review";
    }

    public ConditionsReviewTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,requesterId );
        this.type = "conditions_review";
        this.name = "conditions-review";
    }
}
