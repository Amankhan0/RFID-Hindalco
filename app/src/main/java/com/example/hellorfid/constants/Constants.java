package com.example.hellorfid.constants;


public class Constants {

    //public final static String url = "http://137.184.74.218/";
//    public final static String url = "http://192.168.0.117:9090/";
//    public final static String url = "http://192.168.0.117:9090/";


    public final static String url = "https://api.hindalco.headsupcorporation.com/";

    public final static String addBulkTags = "iot/api/addBulkTags";
    public final static String addTag = "iot/api/addTag";

    public final static String updateBatch = "iot/api/updateBatch";
    public final static String searchBuilding = "plant/api/searchBuilding";
    public final static String updateOrder = "order/api/changeOrderStatus";
    public final static String updateOrderComplete = "order/api/updateOrder";

    public final static String updateBulkTags = "iot/api/updateBulkTags";
    public final static String addBulkCycleCount = "iot/api/addBulkCycleCount";
    public final static String searchGeneral = "helper/api/searchGeneral";
    public static final String DISPATCHED = "DISPATCHED";
    public static final String RECHECK = "RECHECK";
    public static final String RECHECKED = "RECHECKED";
    public static final String RECHECKING = "RECHECKING";
    public static final String RECHECK_FAILED = "RECHECK_FAILED";

    public static final String ONE = "ONE";
    public static final String TWO = "TWO";
    public static final String ORDER_PICKED_PARTIALLY = "ORDER_PICKED_PARTIALLY";
    public static final Object ORDER_RECEIVED_PARTIALLY = "ORDER_RECEIVED_PARTIALLY";
    public static String searchRole = "user/api/searchRole";


//    public final static String searchRfidTag = "iot/api/searchRfidTag";
    public final static String searchRfidTag = "iot/api/searchTag";

    public final static String searchOrders = "order/api/searchOrder";
    public final static String login = "auth/login";

    public final static String searchVehicle = "helper/api/searchVehicle";
    public final static String updateVehicle = "helper/api/updateVehicle";



    public final static String searchZone = "plant/api/searchZone";
    public final static String updateZone = "plant/api/updateZone";



    public final static String searchLocation = "plant/api/searchLocation";
    public final static String updateLocation = "plant/api/updateLocation";
    public static final String ACTIVE = "ACTIVE";
    public static final String UPDATE ="UPDATE" ;
    public static final String MAPPING = "MAPPING";
    public static final String VEHICLE = "VEHICLE" ;
    public static final String ZONE = "ZONE";
    public static final String LOCATION = "LOCATION";
    public static final String REPLACE = "REPLACE";
    public static final String NEW_TAG = "NEW_TAG";
    public static final String UNHOLD ="UNHOLD" ;
    public static final String HOLD = "HOLD";
    public static final String INVENTORY = "INVENTORY";
    public static final String CYCLE_COUNT = "CYCLE_COUNT";
    public static final String MOVE = "MOVE";
    public static final String OPERATION_STATUS_CHANGE = "OPERATION_STATUS_CHANGE";
    public static final String INBOUND = "INBOUND";
    public static final String OUTBOUND = "OUTBOUND";


    public static String EXIT_ACTION = "EXIT";
    public static String EXIT_MOVEMENT_STATUS = "IN_TRANSIT";


    public static String CONVEYOR_ACTION = "CONVEYOR";
    public static String CONVEYOR_MOVEMENT_STATUS = "IN_TRANSIT";


    public static String ENTRY_ACTION = "ENTRY";
    public static String ENTRY_MOVEMENT_STATUS = "IN_BUILDING";
    public static String IN_BUILDING = "IN_BUILDING";
    public static String EMPTY = "EMPTY";

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


    public static String addTagJson = "{\"rfidTag\":\"NA\",\"readerId\":\"NA\",\"status\":\"NA\",\"movementStatus\":\"NA\",\"currentLocation\":\"NA\",\"operationStatus\":\"NA\",\"tagType\":\"NA\",\"tagInfo\":\"NA\",\"tagPlacement\":\"NA\",\"tagMovementInfo\":\"NA\",\"tagMovementTime\":\"NA\",\"tagLotNumber\":\"NA\",\"tagWeight\":-1,\"tagWeightUnit\":\"NA\",\"tagWeightInfo\":\"NA\",\"createdBy\":\"NA\",\"updatedBy\":\"NA\",\"batchId\":\"NA\",\"product_id\":\"NA\",\"dispatchTo\":\"NA\",\"batchNumber\":\"NA\",\"orderId\":\"NA\",\"buildingId\":\"NA\",\"weight\":-1}";



    public static String OUTBOUND_ORDER= "OUTBOUND";
    public static String INBOUND_ORDER= "INBOUND";
    public static final String HOLD_LOCATION= "HOLD_LOCATION";



}
