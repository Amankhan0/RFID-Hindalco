package com.example.hellorfid.model;

import java.util.Date;

public class BatchModel {
    private String id;
    private String batchName;
    private String batchNumber;
    private String lotNumber;
    private String status;
    private String movementStatus;
    private int totalInventory;
    private Date createdAt;
    private Date updatedAt;
    private String productName;
    private String pid;
    private boolean isTagAdded;



    private ProductModel product;




    public BatchModel(String id, String batchName, String batchNumber, String productName,String pid,String status,String movementStatus,int totalInventory,boolean isTagAdded) {
        this.id = id;
        this.batchName = batchName;
        this.batchNumber = batchNumber;
        this.pid = pid;
        this.status = status;
        this.movementStatus = movementStatus;
        this.productName = productName;
        this.totalInventory = totalInventory;
        this.isTagAdded = isTagAdded;
    }

    public boolean isTagAdded() {
        return isTagAdded;
    }

    public void setTagAdded(boolean tagAdded) {
        isTagAdded = tagAdded;
    }

    // Getters
    public String getPid() { return pid; }

    public String getId() { return id; }
    public String getBatchName() { return batchName; }
    public String getBatchNumber() { return batchNumber; }
    public String getStatus() { return status; }
    public String getLotNumber() { return lotNumber; }
    public String getMovementStatus() { return movementStatus; }
    public int getTotalInventory() { return totalInventory; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public String getProductName() { return productName; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setBatchName(String batchName) { this.batchName = batchName; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setMovementStatus(String movementStatus) { this.movementStatus = movementStatus; }
    public void setTotalInventory(int totalInventory) { this.totalInventory = totalInventory; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setProduct(ProductModel product) { this.product = product; }
}
