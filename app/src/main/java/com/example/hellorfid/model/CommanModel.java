package com.example.hellorfid.model;

public class CommanModel {

    private String orderId;
    private String orderStatus;
    private String currentLocation;
    private String dispatchFrom;
    private String dispatchTo;
    private String dispatchFromType;
    private String dispatchToType;
    private String billTo;
    private String pickBy;
    private String readerId;
    private int qty;
    private String batchID;
    private String movementStatus;
    private String status;
    private boolean isError;

    // Getter and Setter for orderId
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    // Getters and Setters
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDispatchFrom() {
        return dispatchFrom;
    }

    public void setDispatchFrom(String dispatchFrom) {
        this.dispatchFrom = dispatchFrom;
    }

    public String getDispatchTo() {
        return dispatchTo;
    }

    public void setDispatchTo(String dispatchTo) {
        this.dispatchTo = dispatchTo;
    }

    public String getDispatchFromType() {
        return dispatchFromType;
    }

    public void setDispatchFromType(String dispatchFromType) {
        this.dispatchFromType = dispatchFromType;
    }

    public String getDispatchToType() {
        return dispatchToType;
    }

    public void setDispatchToType(String dispatchToType) {
        this.dispatchToType = dispatchToType;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getPickBy() {
        return pickBy;
    }

    public void setPickBy(String pickBy) {
        this.pickBy = pickBy;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    public String getMovementStatus() {
        return movementStatus;
    }

    public void setMovementStatus(String movementStatus) {
        this.movementStatus = movementStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }
}
