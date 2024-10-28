package TaskPackage.entity;

public class UrlRequestTask extends Task {

    public UrlRequestTask() throws Exception {
        super();
        this.type = "url_request";
        this.name = "url-request";
    }

    public UrlRequestTask(Integer taskId) throws Exception {
        super(taskId);
        this.type = "url_request";
        this.name = "url-request";
    }

    public UrlRequestTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.type = "url_request";
        this.name = "url-request";
    }
}
