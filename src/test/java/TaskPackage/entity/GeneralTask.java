package TaskPackage.entity;

import static API.Helper.getValueFromJsonByKey;
import static Helper.Adverts.generateName;
import static Helper.Tasks.TASK_WORDS;
import static SQL.AdvertSQL.getValueFromBDWhere;

public class GeneralTask extends Task{

    String title;

    public GeneralTask() throws Exception {
        super();
        this.title = generateName(5, TASK_WORDS);
        this.type = "general";
        this.name = "general";
    }

    public GeneralTask(Integer taskId) throws Exception {
        super(taskId);
        this.title = getValueFromJsonByKey(getValueFromBDWhere("data", "task",
                "id", String.valueOf(taskId)), "title");
        this.type = "general";
        this.name = "general";
    }

    public GeneralTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.title = generateName(5, TASK_WORDS);
        this.type = "general";
        this.name = "general";
    }

    public GeneralTask(Boolean isEmpty) throws Exception {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
