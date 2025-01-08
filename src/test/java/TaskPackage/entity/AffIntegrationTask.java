package TaskPackage.entity;

public class AffIntegrationTask extends Task {

    public AffIntegrationTask() throws Exception {
        super();
        this.type = "affiliate_integration";
        this.name = "affiliate-integration";
    }

    public AffIntegrationTask(Integer taskId) throws Exception {
        super(taskId);
        this.type = "affiliate_integration";
        this.name = "affiliate-integration";
    }

    public AffIntegrationTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.type = "affiliate_integration";
        this.name = "affiliate-integration";
    }
}
