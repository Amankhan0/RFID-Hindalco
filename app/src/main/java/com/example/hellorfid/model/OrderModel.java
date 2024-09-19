package com.example.hellorfid.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderModel {

    private String _id;
    private Date orderDateTime;
    private Date expectedArrival;
    private String saleType;
    private String orderType;
    private String orderStatus;
    private String currentLocation;
    private String dispatchFrom;
    private String dispatchTo;
    private String dispatchFromType;
    private String dispatchToType;
    private String billTo;
    private List<VehicleId> vehicleIds;
    private List<ProductOrder> productIds;
    private String pickBy;
    private String readerId;
    private int qty;
    private String batchNumber;
    private String movementStatus;
    private String status;
    private boolean isError;
    private String errorMsg;
    private String createdBy;
    private String updatedBy;
    private long createdAt;
    private long updatedAt;
    private boolean isTagAdded;

    // Constructors
    public OrderModel() {
        // Default constructor
        this.vehicleIds = new ArrayList<>();
        this.productIds = new ArrayList<>();
    }

    public OrderModel(String _id, String batchName, String batchNumber, String productName, String pid,
                      String status, String movementStatus, int totalInventory, boolean isTagAdded) {
        this();  // Call default constructor to initialize lists
        this._id = _id;
        this.batchNumber = batchName;
        this.qty = totalInventory;
        this.status = status;
        this.movementStatus = movementStatus;
        this.isTagAdded = isTagAdded;

        // Create a new Product and ProductOrder
        Product product = new Product();
        product.setId(pid);
        product.setProductName(productName);

        ProductOrder productOrder = new ProductOrder(product, totalInventory);
        this.productIds.add(productOrder);
    }

    // Getters and setters
    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public Date getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(Date orderDateTime) { this.orderDateTime = orderDateTime; }

    public Date getExpectedArrival() { return expectedArrival; }
    public void setExpectedArrival(Date expectedArrival) { this.expectedArrival = expectedArrival; }

    public String getSaleType() { return saleType; }
    public void setSaleType(String saleType) { this.saleType = saleType; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getDispatchFrom() { return dispatchFrom; }
    public void setDispatchFrom(String dispatchFrom) { this.dispatchFrom = dispatchFrom; }

    public String getDispatchTo() { return dispatchTo; }
    public void setDispatchTo(String dispatchTo) { this.dispatchTo = dispatchTo; }

    public String getDispatchFromType() { return dispatchFromType; }
    public void setDispatchFromType(String dispatchFromType) { this.dispatchFromType = dispatchFromType; }

    public String getDispatchToType() { return dispatchToType; }
    public void setDispatchToType(String dispatchToType) { this.dispatchToType = dispatchToType; }

    public String getBillTo() { return billTo; }
    public void setBillTo(String billTo) { this.billTo = billTo; }

    public List<VehicleId> getVehicleIds() { return vehicleIds; }
    public void setVehicleIds(List<VehicleId> vehicleIds) { this.vehicleIds = vehicleIds; }

    public List<ProductOrder> getProductIds() { return productIds; }
    public void setProductIds(List<ProductOrder> productIds) { this.productIds = productIds; }

    public String getPickBy() { return pickBy; }
    public void setPickBy(String pickBy) { this.pickBy = pickBy; }

    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public String getMovementStatus() { return movementStatus; }
    public void setMovementStatus(String movementStatus) { this.movementStatus = movementStatus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isError() { return isError; }
    public void setError(boolean error) { isError = error; }

    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isTagAdded() { return isTagAdded; }
    public void setTagAdded(boolean tagAdded) { this.isTagAdded = tagAdded; }

    @Override
    public String toString() {
        return "OrderModel{" +
                "_id='" + _id + '\'' +
                ", orderDateTime=" + orderDateTime +
                ", expectedArrival=" + expectedArrival +
                ", saleType='" + saleType + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", dispatchFrom='" + dispatchFrom + '\'' +
                ", dispatchTo='" + dispatchTo + '\'' +
                ", dispatchFromType='" + dispatchFromType + '\'' +
                ", dispatchToType='" + dispatchToType + '\'' +
                ", billTo='" + billTo + '\'' +
                ", vehicleIds=" + vehicleIds +
                ", productIds=" + productIds +
                ", pickBy='" + pickBy + '\'' +
                ", readerId='" + readerId + '\'' +
                ", qty=" + qty +
                ", batchNumber='" + batchNumber + '\'' +
                ", movementStatus='" + movementStatus + '\'' +
                ", status='" + status + '\'' +
                ", isError=" + isError +
                ", errorMsg='" + errorMsg + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isTagAdded=" + isTagAdded +
                '}';
    }

    // Nested classes
    public static class VehicleId {
        private String vehicleId;

        public VehicleId() {}

        public VehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getVehicleId() { return vehicleId; }
        public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    }

    public static class ProductOrder {
        private Product productId;
        private int quantity;

        public ProductOrder() {}

        public ProductOrder(Product productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Product getProductId() { return productId; }
        public void setProductId(Product productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class Product {
        private String _id;
        private String productName;
        private String productCode;
        private int quantity;
        private String productDescription;
        private String productGroup;
        private double height;
        private double width;
        private double length;
        private double packedWeight;
        private double weight;
        private double buyingCost;
        private double sellingCost;
        private String grade;

        public Product() {}

        // Getters and setters for Product class
        public String getId() { return _id; }
        public void setId(String _id) { this._id = _id; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

        public String getProductGroup() { return productGroup; }
        public void setProductGroup(String productGroup) { this.productGroup = productGroup; }

        public double getHeight() { return height; }
        public void setHeight(double height) { this.height = height; }

        public double getWidth() { return width; }
        public void setWidth(double width) { this.width = width; }

        public double getLength() { return length; }
        public void setLength(double length) { this.length = length; }

        public double getPackedWeight() { return packedWeight; }
        public void setPackedWeight(double packedWeight) { this.packedWeight = packedWeight; }

        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }

        public double getBuyingCost() { return buyingCost; }
        public void setBuyingCost(double buyingCost) { this.buyingCost = buyingCost; }

        public double getSellingCost() { return sellingCost; }
        public void setSellingCost(double sellingCost) { this.sellingCost = sellingCost; }

        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
    }
}
