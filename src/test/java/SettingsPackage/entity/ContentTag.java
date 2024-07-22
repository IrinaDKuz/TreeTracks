package SettingsPackage.entity;

import static Helper.Adverts.generateName;
import static Helper.Settings.GOALS_WORDS;

public class ContentTag {
    String tagTitle;
    Integer tagId;

    public ContentTag() {
    }

    public void fillContentTagWithRandomData() {
        this.tagTitle = generateName(2, GOALS_WORDS);
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}