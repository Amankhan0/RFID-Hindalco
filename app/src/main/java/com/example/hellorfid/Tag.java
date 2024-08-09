package com.example.hellorfid;

public class Tag {
    private String tagNumber;
    private String lotNumber;

    public Tag(String tagNumber, String lotNumber) {
        this.tagNumber = tagNumber;
        this.lotNumber = lotNumber;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
}
