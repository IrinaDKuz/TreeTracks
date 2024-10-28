package TaskPackage.entity;

import java.util.ArrayList;

import static API.Helper.getValueFromJsonByKey;
import static Helper.Adverts.generateName;
import static Helper.Tasks.TASK_WORDS;
import static SQL.AdvertSQL.getValueFromBDWhere;

public class TestConversionTask extends Task{

    String title;

    public TestConversionTask() throws Exception {
        super();
        this.type = "test_conversion";
        this.name = "test-conversion";
        this.title = generateName(5, TASK_WORDS);
        this.taskWatchers = new ArrayList<>();
    }

    public TestConversionTask(Integer taskId) throws Exception {
        super(taskId);
        this.title = getValueFromJsonByKey(getValueFromBDWhere("data", "task",
                "id", String.valueOf(taskId)), "url");
        this.type = "test_conversion";
        this.name = "test-conversion";
    }

    public TestConversionTask(Integer taskId, Integer requesterId) throws Exception {
        super(taskId,  requesterId);
        this.type = "test_conversion";
        this.name = "test-conversion";
        this.title = generateName(5, TASK_WORDS);
        this.taskWatchers = new ArrayList<>();

    }

    public TestConversionTask(Boolean isEmpty) throws Exception {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
