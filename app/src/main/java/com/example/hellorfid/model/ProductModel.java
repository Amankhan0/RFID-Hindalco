package com.example.hellorfid.model;

import java.util.Date;

public class ProductModel {
    private String productName;
    private String productCode;
    private String productDescription;
    private String productGroup;
    private String weight;
    private String height;
    private String width;
    private String length;
    private String packedWeight;
    private String buyingCost;
    private String sellingCost;
    private String grade;
    private boolean captureBatchNo;
    private boolean captureLotNo;
    private String status;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
    private String id;

    // Constructor
    public ProductModel(String productName, String productCode, String productDescription, String productGroup,
                        String weight, String height, String width, String length, String packedWeight,
                        String buyingCost, String sellingCost, String grade, boolean captureBatchNo,
                        boolean captureLotNo, String status, String createdBy, String updatedBy,
                        Date createdAt, Date updatedAt, String id) {
        this.productName = productName;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.productGroup = productGroup;
        this.weight = weight;
        this.height = height;
        this.width = width;
        this.length = length;
        this.packedWeight = packedWeight;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
        this.grade = grade;
        this.captureBatchNo = captureBatchNo;
        this.captureLotNo = captureLotNo;
        this.status = status;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = id;
    }

    // Getters
    public String getProductName() { return productName; }
    public String getProductCode() { return productCode; }
    public String getProductDescription() { return productDescription; }
    public String getProductGroup() { return productGroup; }
    public String getWeight() { return weight; }
    public String getHeight() { return height; }
    public String getWidth() { return width; }
    public String getLength() { return length; }
    public String getPackedWeight() { return packedWeight; }
    public String getBuyingCost() { return buyingCost; }
    public String getSellingCost() { return sellingCost; }
    public String getGrade() { return grade; }
    public boolean isCaptureBatchNo() { return captureBatchNo; }
    public boolean isCaptureLotNo() { return captureLotNo; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public String getId() { return id; }

    // Setters
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public void setProductGroup(String productGroup) { this.productGroup = productGroup; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setHeight(String height) { this.height = height; }
    public void setWidth(String width) { this.width = width; }
    public void setLength(String length) { this.length = length; }
    public void setPackedWeight(String packedWeight) { this.packedWeight = packedWeight; }
    public void setBuyingCost(String buyingCost) { this.buyingCost = buyingCost; }
    public void setSellingCost(String sellingCost) { this.sellingCost = sellingCost; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setCaptureBatchNo(boolean captureBatchNo) { this.captureBatchNo = captureBatchNo; }
    public void setCaptureLotNo(boolean captureLotNo) { this.captureLotNo = captureLotNo; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setId(String id) { this.id = id; }
}
