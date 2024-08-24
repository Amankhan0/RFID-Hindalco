package com.example.hellorfid.model;

import java.util.Date;
import java.util.List;

public class DispatchModel {
    private String id;
    private String buildingName;
    private String buildingNo;
    private String unitName;
    private String unitId;
    private boolean addEmptyBag;
    private String status;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
    private List<String> zoneIds;
    private List<String> buildingIds;
    private List<String> locationIds;
    private List<String> readerIds;
    private List<String> tagIds;

    // Constructor
    public DispatchModel(String id, String buildingName, String buildingNo, String unitName, String unitId,
                         boolean addEmptyBag, String status, String createdBy, String updatedBy,
                         Date createdAt, Date updatedAt, List<String> zoneIds, List<String> buildingIds,
                         List<String> locationIds, List<String> readerIds, List<String> tagIds) {
        this.id = id;
        this.buildingName = buildingName;
        this.buildingNo = buildingNo;
        this.unitName = unitName;
        this.unitId = unitId;
        this.addEmptyBag = addEmptyBag;
        this.status = status;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.zoneIds = zoneIds;
        this.buildingIds = buildingIds;
        this.locationIds = locationIds;
        this.readerIds = readerIds;
        this.tagIds = tagIds;
    }

    public DispatchModel(String id, String buildingName, String buildingNo) {
        this.id = id;
        this.buildingName = buildingName;
        this.buildingNo = buildingNo;
        this.unitName = unitName;

    }

    // Getters
    public String getId() { return id; }
    public String getBuildingName() { return buildingName; }
    public String getBuildingNo() { return buildingNo; }
    public String getUnitName() { return unitName; }
    public String getUnitId() { return unitId; }
    public boolean isAddEmptyBag() { return addEmptyBag; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public List<String> getZoneIds() { return zoneIds; }
    public List<String> getBuildingIds() { return buildingIds; }
    public List<String> getLocationIds() { return locationIds; }
    public List<String> getReaderIds() { return readerIds; }
    public List<String> getTagIds() { return tagIds; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public void setUnitId(String unitId) { this.unitId = unitId; }
    public void setAddEmptyBag(boolean addEmptyBag) { this.addEmptyBag = addEmptyBag; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setZoneIds(List<String> zoneIds) { this.zoneIds = zoneIds; }
    public void setBuildingIds(List<String> buildingIds) { this.buildingIds = buildingIds; }
    public void setLocationIds(List<String> locationIds) { this.locationIds = locationIds; }
    public void setReaderIds(List<String> readerIds) { this.readerIds = readerIds; }
    public void setTagIds(List<String> tagIds) { this.tagIds = tagIds; }
}