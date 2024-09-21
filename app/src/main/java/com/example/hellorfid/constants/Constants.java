package com.example.hellorfid.constants;


public class Constants {

    //public final static String url = "http://137.184.74.218/";
//    public final static String url = "http://192.168.0.117:9090/";
//    public final static String url = "http://192.168.0.117:9090/";


    public final static String url = "https://api.hindalco.headsupcorporation.com/";

    public final static String addBulkTags = "iot/api/addBulkTags";
    public final static String updateBatch = "iot/api/updateBatch";
    public final static String searchBuilding = "plant/api/searchBuilding";
    public final static String updateOrder = "order/api/updateOrder";
    public final static String updateBulkTags = "iot/api/updateBulkTags";
    public final static String searchRfidTag = "iot/api/searchRfidTag";

    public final static String searchOrders = "order/api/searchOrder";
    public final static String login = "auth/login";



    public static String EXIT_ACTION = "EXIT";
    public static String EXIT_MOVEMENT_STATUS = "IN_TRANSIT";


    public static String CONVEYOR_ACTION = "CONVEYOR";
    public static String CONVEYOR_MOVEMENT_STATUS = "IN_TRANSIT";


    public static String ENTRY_ACTION = "ENTRY";
    public static String ENTRY_MOVEMENT_STATUS = "IN_BUILDING";

    public static String BAGGING_AUTO_PROGRESS = "BAGGING_IN_PROGRESS";
    public static String BAGGING_AUTO_ACTION = "BAGGING_AUTO";
    public static String BAGGING_MOVEMENT_STATUS = "IN_BUILDING";
    public static String BAGGING_COMPLETE = "BAGGING_COMPLETE";


    public static String ERROR_IN_WRONG_BUILDING = "IN_WRONG_BUILDING";
    public static String ERROR_DISPATCH_BULDING_NOT_FOUND = "DISPATCH_BULDING_NOT_FOUND";
    public static String ERROR_DISPATCHED_FROM_WORNG_BUILDING = "DISPATCHED_FROM_WORNG_BUILDING";
    public static String ERROR_DIFFRENT_BATCH_BAGGING = "TAG_SCANNED_FOR_A_DIFFERENT_OR_NON_BAGGING_BATCH";
    public static String ERROR_DIFFRENT_BUILDING_BAGGING = "TAG_SCANNED_FOR_A_DIFFERENT_BUILDING_BAGGING_BATCH";
    public static String ERROR_WRONG_MATERIAL_DISPATCHED = "ERROR_WRONG_MATERIAL_DISPATCHED";


    public static Boolean UPDATE_BULDING_ID = true;
    public static Boolean NOT_UPDATE_BULDING_ID = false;

    public static String ORDER_INITIATED= "ORDER_INITIATED";
    public static String ORDER_PICKED= "ORDER_PICKED";
    public static String ORDER_PICKING= "ORDER_PICKING";
    public static String ORDER_RECEIVING= "ORDER_RECEIVING";
    public static String ORDER_RECEIVED= "ORDER_RECEIVED";
    public static String ORDER_READY_TO_DISPATCH= "ORDER_READY_TO_DISPATCH";
    public static String ORDER_DISPATCHED= "ORDER_DISPATCHED";
    public static String ORDER_LOADING_IN_VEHICLE= "ORDER_LOADING_IN_VEHICLE";
    public static String ORDER_DELIVERED= "ORDER_DELIVERED";
    public static String ORDER_DISPATCHING_IN_PROGRESS= "ORDER_DISPATCHING_IN_PROGRESS";
    public static String ORDER_LOADING= "ORDER_LOADING";



    public static String inventoryRfidTagCol= "inventoryRfidTag";




}
