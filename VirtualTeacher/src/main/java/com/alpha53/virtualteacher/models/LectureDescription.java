package com.alpha53.virtualteacher.models;

public class LectureDescription {
    private int descriptionId;
    private String description;

    public LectureDescription() {
    }

    public LectureDescription(int descriptionId, String description) {
        this.descriptionId = descriptionId;
        this.description = description;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
