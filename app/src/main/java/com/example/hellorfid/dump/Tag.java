package com.example.hellorfid.dump;

public class Tag {
    private String tagNumber;
    private String lotNumber;
    private boolean isOverLimit;

    public Tag(String tagNumber, String lotNumber, boolean isOverLimit) {
        this.tagNumber = tagNumber;
        this.lotNumber = lotNumber;
        this.isOverLimit = isOverLimit;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public boolean isOverLimit() {
        return isOverLimit;
    }
}

