package com.example.hellorfid.dump;

public class Tag {
    private String tagNumber;
    private String lotNumber;
    private boolean isOverLimit;
    private String tagType;

    public Tag(String tagNumber, boolean isOverLimit,String tagType) {
        this.tagNumber = tagNumber;
        this.tagType = tagType;
        this.isOverLimit = isOverLimit;
    }

    public String getTagNumber() {
        return tagNumber;
    }
    public String getTagType() {
        return tagType;
    }


//    public String getLotNumber() {
//        return lotNumber;
//    }

    public boolean isOverLimit() {
        return isOverLimit;
    }
}

