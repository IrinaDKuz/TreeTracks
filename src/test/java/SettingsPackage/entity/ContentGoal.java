package SettingsPackage.entity;

import static Helper.Adverts.generateName;
import static Helper.Settings.*;

public class ContentGoal {

    String goalTitle;
    Integer goalId;

    public ContentGoal() {
    }

    public void fillContentGoalWithRandomData() {
        this.goalTitle = generateName(2, GOALS_WORDS);
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }


}