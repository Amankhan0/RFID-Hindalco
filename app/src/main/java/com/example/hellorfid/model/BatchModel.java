package com.example.hellorfid.model;

import java.util.Date;

public class BatchModel {
    private String id;
    private String batchName;
    private String batchNumber;
    private String status;
    private String movementStatus;
    private int totalInventory;
    private Date createdAt;
    private Date updatedAt;
    private String productName;


    private ProductModel product;

    // Constructor
    public BatchModel(String id, String batchName, String batchNumber, String status, String movementStatus,
                      int totalInventory, Date createdAt, Date updatedAt, ProductModel product) {
        this.id = id;
        this.batchName = batchName;
        this.batchNumber = batchNumber;
        this.status = status;
        this.movementStatus = movementStatus;
        this.totalInventory = totalInventory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
    }

    public BatchModel(String id, String buildingName, String buildingNo, String productName) {
        this.id = id;
        this.batchName = buildingName;
        this.batchNumber = buildingNo;
        this.productName = productName;
    }

    // Getters
    public String getId() { return id; }
    public String getBatchName() { return batchName; }
    public String getBatchNumber() { return batchNumber; }
    public String getStatus() { return status; }
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
