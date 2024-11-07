package com.example.hellorfid.constants;

import static java.lang.Integer.parseInt;

import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CaseExecutorHandler {

    public static JSONObject holdUsingLocationTag (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {
            JSONArray jsonArray = new JSONArray(story);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String data = jsonObject.getString("data");
            JSONArray arrData = new JSONArray(data);
            JSONObject dataObj = arrData.getJSONObject(0);
            JSONObject s = new JSONObject();
            s.put("locationIds",dataObj.getString("locationIds"));
            JSONObject res = Helper.commanHitApi(apiCallBackWithToken,Constants.searchRfidTag,Helper.getSearchJson(1,1000,s));
            JSONArray resData = res.getJSONArray("content");
            for (int i = 0; i < resData.length(); i++) {
                resData.getJSONObject(i).put("opreationStatus",Constants.HOLD);
            }
           JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,resData);
            if(res1.getInt("status") == 200) {
    //            System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
            return res1;
    }


    public static JSONObject unHoldUsingLocationTag (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {
        JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String data = jsonObject.getString("data");
        JSONArray arrData = new JSONArray(data);
        JSONObject dataObj = arrData.getJSONObject(0);
        JSONObject s = new JSONObject();
        s.put("locationIds",dataObj.getString("locationIds"));
        JSONObject res = Helper.commanHitApi(apiCallBackWithToken,Constants.searchRfidTag,Helper.getSearchJson(1,1000,s));
        JSONArray resData = res.getJSONArray("content");
        for (int i = 0; i < resData.length(); i++) {
            resData.getJSONObject(i).put("opreationStatus",Constants.ACTIVE);
        }
        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,resData);
        if(res1.getInt("status") == 200) {
            //            System.out.println("RESSS tag updated------>>>>>>> "+upadate);
            sessionManager.clearPendingOps();
        }
        return res1;
    }


    public static JSONObject order (String story, ApiCallBackWithToken apiCallBackWithToken, SessionManager sessionManager) throws JSONException, InterruptedException {
        JSONArray jsonArray = new JSONArray(story);
        JSONObject locationObjH = jsonArray.getJSONObject(0);
        JSONObject inventoryObjH = jsonArray.getJSONObject(1);
        String locationData = locationObjH.getString("data");
        String inventoryData = inventoryObjH.getString("data");
        JSONArray arrLocData = new JSONArray(locationData);
        JSONObject location = arrLocData.getJSONObject(0);

        JSONArray resData = new JSONArray(inventoryData);
        JSONArray resData1 = new JSONArray(inventoryData);
        int pCount = parseInt(sessionManager.getProductData().getString("quantity"));
        int actualCount = resData.length();
        String orderStatus =sessionManager.getOptionSelected().equals(Constants.OUTBOUND)?pCount==actualCount?Constants.ORDER_PICKED:Constants.ORDER_PICKED_PARTIALLY:
                pCount==actualCount?Constants.ORDER_RECEIVED:Constants.ORDER_RECEIVED_PARTIALLY;

        for (int i = 0; i < resData.length(); i++) {

            resData.getJSONObject(i).put("tagType",Constants.INVENTORY);
            resData.getJSONObject(i).put("currentLocation",sessionManager.getBuildingId());
            resData.getJSONObject(i).put("locationIds",location.getString("locationIds"));
            resData.getJSONObject(i).put("zoneIds",location.getString("zoneIds"));
            resData.getJSONObject(i).put("buildingId",sessionManager.getBuildingId());
            resData.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData.getJSONObject(i).put("dispatchFrom",sessionManager.getOrderData().getString("dispatchFrom"));
            resData.getJSONObject(i).put("opreationStatus",orderStatus);
            resData.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData.getJSONObject(i).put("movementStatus",Constants.IN_BUILDING);
            resData.getJSONObject(i).put("status",Constants.EMPTY);
            resData.getJSONObject(i).put("batchNumber",sessionManager.getOrderData().has("batchNumber")?sessionManager.getOrderData().getString("batchNumber"):"NA");
            resData.getJSONObject(i).put("product_id",sessionManager.getProductData().getJSONObject("productId").getString("_id"));
            resData.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData.getJSONObject(i).put("createdBy",sessionManager.getUserId());
            resData.getJSONObject(i).put("updatedBy",sessionManager.getUserId());

            resData1.getJSONObject(i).put("tagType",Constants.INVENTORY);
            resData1.getJSONObject(i).put("currentLocation",sessionManager.getBuildingId());
            resData1.getJSONObject(i).put("locationIds",location.getString("locationIds"));
            resData1.getJSONObject(i).put("zoneIds",location.getString("zoneIds"));
            resData1.getJSONObject(i).put("buildingId",sessionManager.getBuildingId());
            resData1.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData1.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData1.getJSONObject(i).put("dispatchFrom",sessionManager.getOrderData().getString("dispatchFrom"));
            resData1.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData1.getJSONObject(i).put("movementStatus",Constants.IN_BUILDING);
            resData1.getJSONObject(i).put("status",Constants.EMPTY);
            resData1.getJSONObject(i).put("batchNumber",sessionManager.getOrderData().has("batchNumber")?sessionManager.getOrderData().getString("batchNumber"):"NA");
            resData1.getJSONObject(i).put("product_id",sessionManager.getProductData().getJSONObject("productId").getString("_id"));
            resData1.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData1.getJSONObject(i).put("createdBy",sessionManager.getUserId());
            resData1.getJSONObject(i).put("updatedBy",sessionManager.getUserId());
            resData1.getJSONObject(i).put("opreationStatus",Constants.ACTIVE);
        }

        System.out.println("resData "+resData.toString());
        JSONObject orderData = sessionManager.getOrderData();

        orderData.put("orderStatus",orderStatus);


        System.out.println("order Data"+ orderData.toString());
        orderData.remove("productIds");
        orderData.remove("vehicleIds");

        JSONObject res2 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateOrderComplete,orderData);
        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,resData);
        System.out.println("oerder upadtede"+res2);
        if(res2!=null && res2.getInt("status") == 200) {
            if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+res1);
                JSONObject res3 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,resData1);
                sessionManager.clearPendingOps();
            }
        }
        return res2;
    }



    public static JSONObject vehicleMap (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("vehicleMap story------>>>>>>> "+story);

                JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dataS = jsonObject.getString("data");
        String supportDataS = jsonObject.getString("supportData");
        JSONArray dataObj = new JSONArray(dataS);
        JSONObject supportDataObj = new JSONObject(supportDataS);
        JSONObject tagData = dataObj.getJSONObject(0);
        tagData.put("currentLocation",supportDataObj.getString("_id"));
        tagData.put("tagType",Constants.VEHICLE);
        tagData.put("tagInfo",supportDataObj.getString("vehicleNumber"));
        tagData.put("opreationStatus",Constants.ACTIVE);
        tagData.put("status",Constants.ACTIVE);
        tagData.put("movementStatus",Constants.ACTIVE);
        tagData.put("createdBy",sessionManager.getUserId());
        tagData.put("updatedBy",sessionManager.getUserId());

        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,dataObj);

        if(res1.getInt("status") == 200){
            System.out.println("RESSS res------>>>>>>> "+res1);
            supportDataObj.put("tagIds",res1.getJSONArray("data").getJSONObject(0).getString("_id"));
            supportDataObj.remove("createdAt");
            supportDataObj.remove("updatedAt");
            supportDataObj.remove("siteIds");
            JSONObject upadate = Helper.commanHitApi(apiCallBackWithToken,Constants.updateVehicle,supportDataObj);
            System.out.println("RESSS tag supportDataObj------>>>>>>> "+supportDataObj);
            if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
        }
        return res1;
    }


    public static JSONObject nozzle (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("nozzle story------>>>>>>> nozzle"+story);

        JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dataS = jsonObject.getString("data");
        String supportDataS = jsonObject.getString("supportData");
        JSONArray dataObj = new JSONArray(dataS);
        JSONObject supportDataObj = new JSONObject(supportDataS);
        System.out.println("RESSS dataObj------>>>>>>> "+dataObj);
        System.out.println("RESSS supportDataObj------>>>>>>> Nozzle"+supportDataObj);

        JSONObject tagData = dataObj.getJSONObject(0);
        tagData.put("currentLocation",supportDataObj.getString("buildingIds"));
        tagData.put("currentLocation",supportDataObj.getString("_id"));
        tagData.put("buildingIds",supportDataObj.getString("buildingIds"));
        tagData.put("tagType",Constants.NOZZLE);
        tagData.put("readerId",supportDataObj.getString("deviceId"));
        tagData.put("tagPlacement", supportDataObj.getString("buildingIds"));
        tagData.put("tagInfo",supportDataObj.getString("value"));
        tagData.put("opreationStatus",Constants.ACTIVE);
        tagData.put("status",Constants.ACTIVE);
        tagData.put("createdBy",sessionManager.getUserId());
        tagData.put("updatedBy",sessionManager.getUserId());

        System.out.println("<<-----RESSS tagData------>>>>>>> "+dataObj);

//        System.out.println("inside the nozzle ---->>>>>");

        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,dataObj);

        return new JSONObject();
    }

    public static JSONObject WeighingMachine(String story, ApiCallBackWithToken apiCallBackWithToken, SessionManager sessionManager) throws JSONException, InterruptedException {
        System.out.println("weighing machine story------>>>>>>> " + story);

        // Parse the initial data
        JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        // Get data and supportData directly since they're already JSON strings in your input
        JSONArray dataObj = new JSONArray(jsonObject.getString("data"));
        JSONObject supportDataObj = new JSONObject(jsonObject.getString("supportData"));

        System.out.println("RESSS dataObj------>>>>>>> " + dataObj);
        System.out.println("RESSS supportDataObj------>>>>>>> WEIGHING MACHINE" + supportDataObj);

        // Get the first object from dataObj array
        JSONObject tagData = dataObj.getJSONObject(0);

        try {
            // Update tagData with values from supportDataObj
            // Note: We're using the actual field names from your supportDataObj
            tagData.put("currentLocation", supportDataObj.getString("buildingIds"));
            tagData.put("buildingId", supportDataObj.getString("buildingIds")); // Changed from buildingIds to match your data structure
            tagData.put("buildingIds", supportDataObj.getString("buildingIds"));
            tagData.put("tagType", "WEIGHING_SCALE"); // Assuming this is your Constants.WeigingScale value
            tagData.put("readerId", supportDataObj.getString("_id")); // Using _id since deviceId wasn't in the supportData
            tagData.put("tagPlacement", supportDataObj.getString("buildingIds"));

            // For tagInfo, you might want to use a specific field from supportDataObj
            // Currently there's no "value" field in supportDataObj, so you might want to use something else
            tagData.put("tagInfo", supportDataObj.getString("deviceName")); // Changed to deviceName as an example

            tagData.put("operationStatus", "ACTIVE"); // Changed from opreationStatus to operationStatus and using string instead of constant
            tagData.put("status", "ACTIVE");
            tagData.put("createdBy", sessionManager.getUserId());
            tagData.put("updatedBy", sessionManager.getUserId());

            // Optional: Add any additional fields that might be useful
            tagData.put("deviceType", supportDataObj.getString("deviceType"));
            tagData.put("macAddress", supportDataObj.getString("macAddress"));
            tagData.put("model", supportDataObj.getString("model"));

            System.out.println("<<-----RESSS updated tagData------>>>>>>> " + dataObj);

            JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, dataObj);

            return new JSONObject();

        } catch (JSONException e) {
            System.err.println("Error processing weighing machine data: " + e.getMessage());
            return new JSONObject().put("success", false).put("error", e.getMessage());
        }
    }




    public static JSONObject zoneCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("Zone story------>>>>>>> "+story);

       JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dataS = jsonObject.getString("data");
        String supportDataS = jsonObject.getString("supportData");
        JSONArray dataObj = new JSONArray(dataS);
        JSONObject supportDataObj = new JSONObject(supportDataS);
        System.out.println("RESSS dataObj------>>>>>>> "+dataObj);
        System.out.println("RESSS supportDataObj------>>>>>>> "+supportDataObj);

        JSONObject tagData = dataObj.getJSONObject(0);
        tagData.put("currentLocation",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("zoneIds",supportDataObj.getString("_id"));
        tagData.put("buildingIds",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("locationIds",supportDataObj.getJSONArray("locationIds").get(0));
        tagData.put("tagType",Constants.ZONE);
        tagData.put("tagPlacement",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("tagInfo",supportDataObj.getString("value"));
        tagData.put("opreationStatus",Constants.ACTIVE);
        tagData.put("status",Constants.ACTIVE);
        tagData.put("createdBy",sessionManager.getUserId());
        tagData.put("updatedBy",sessionManager.getUserId());

        System.out.println("RESSS tagData------>>>>>>> "+dataObj);


        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,dataObj);

        if(res1.getInt("status") == 200){
            System.out.println("RESSS res------>>>>>>> "+res1);
            supportDataObj.put("tagIds",res1.getJSONArray("data").getJSONObject(0).getString("_id"));
            supportDataObj.remove("createdAt");
            supportDataObj.remove("updatedAt");
            supportDataObj.remove("siteIds");
            JSONObject upadate = Helper.commanHitApi(apiCallBackWithToken,Constants.updateZone,supportDataObj);
            System.out.println("RESSS tag supportDataObj------>>>>>>> "+supportDataObj);
            if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
        }
        return new JSONObject();
    }


    public static JSONObject locationCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("locationCase story------>>>>>>> "+story);

        JSONArray jsonArray = new JSONArray(story);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String dataS = jsonObject.getString("data");
        String supportDataS = jsonObject.getString("supportData");
        JSONArray dataObj = new JSONArray(dataS);
        JSONObject supportDataObj = new JSONObject(supportDataS);
        System.out.println("RESSS dataObj------>>>>>>> "+dataObj);
        System.out.println("RESSS supportDataObj------>>>>>>> "+supportDataObj);

        JSONObject tagData = dataObj.getJSONObject(0);
        if(!supportDataObj.getString("tagIds").equals("null")){
            tagData.put("_id",supportDataObj.getString("tagIds"));
        }
        tagData.put("currentLocation",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("locationIds",supportDataObj.getString("_id"));
        tagData.put("zoneIds",supportDataObj.getString("zoneIds"));
        tagData.put("buildingIds",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("tagType",Constants.LOCATION);
        tagData.put("tagPlacement",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("tagInfo",supportDataObj.getString("value"));
        tagData.put("opreationStatus",Constants.ACTIVE);
        tagData.put("status",Constants.ACTIVE);
        tagData.put("createdBy",sessionManager.getUserId());
        tagData.put("updatedBy",sessionManager.getUserId());

        System.out.println("RESSS tagData------>>>>>>> "+tagData);


        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,dataObj);

        if(res1.getInt("status") == 200){
            System.out.println("RESSS res------>>>>>>> "+res1);
            supportDataObj.put("tagIds",res1.getJSONArray("data").getJSONObject(0).getString("_id"));
            supportDataObj.remove("createdAt");
            supportDataObj.remove("updatedAt");
            supportDataObj.remove("siteIds");
            supportDataObj.remove("usedBy");


            JSONObject upadate = Helper.commanHitApi(apiCallBackWithToken,Constants.updateLocation,supportDataObj);
            System.out.println("RESSS tag supportDataObj------>>>>>>> "+supportDataObj);
            if(upadate!=null && upadate.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
        }
        return new JSONObject();
    }


    public static JSONObject replcaeCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("replcae Casestory------>>>>>>> "+story);

        JSONArray jsonArray = new JSONArray(story);
        JSONObject fromObj = jsonArray.getJSONObject(0);
        String fromData = fromObj.getString("data");
        JSONArray fromArr = new JSONArray(fromData);
        JSONObject from = fromArr.getJSONObject(0);


        JSONObject toObj = jsonArray.getJSONObject(1);
        String toData = toObj.getString("data");
        JSONArray toArr = new JSONArray(toData);
        JSONObject to = toArr.getJSONObject(0);

        from.put("rfidTag",to.getString("rfidTag"));
        System.out.println("RESSS from------>>>>>>> "+from);
        System.out.println("RESSS to------>>>>>>> "+to);

       JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,fromArr);

        if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+res1);
                sessionManager.clearPendingOps();
            }

        return new JSONObject();
    }


    public static JSONObject inventoryStatusUpdate(String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager,String status) throws JSONException, InterruptedException {

        System.out.println("inventoryStatusUpdate story------>>>>>>> "+story);

//
        JSONArray jsonArray = new JSONArray(story);
        JSONObject inventoryObj = jsonArray.getJSONObject(0);
        String fromData = inventoryObj.getString("data");
        JSONArray inventoryArr = new JSONArray(fromData);

        for (int i = 0; i < inventoryArr.length(); i++) {
            inventoryArr.getJSONObject(i).put("opreationStatus",status);
            System.out.println("RESSS inventoryArr------>>>>>>> "+inventoryArr.get(i));
        }

        System.out.println("Final inventoryArr" + inventoryArr.toString());

        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,inventoryArr);
        if(res1.getInt("status") == 200) {
            System.out.println("RESSS tag updated------>>>>>>> "+res1);
            sessionManager.clearPendingOps();
        }
        return res1;
    }


    public static JSONObject cycleCount(String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager,String status) throws JSONException, InterruptedException {

        System.out.println("cycleCount story------>>>>>>> "+story);

        JSONArray jsonArray = new JSONArray(story);
        JSONObject locationObj = jsonArray.getJSONObject(0);
        String locationObjString = locationObj.getString("data");
        JSONArray locationArr = new JSONArray(locationObjString);
        JSONObject location = locationArr.getJSONObject(0);

        JSONObject inventoryObj = jsonArray.getJSONObject(1);
        String inventoryObjString = inventoryObj.getString("data");
        JSONArray inventoryArr = new JSONArray(inventoryObjString);

        for (int i = 0; i < inventoryArr.length(); i++) {
            inventoryArr.getJSONObject(i).put("cycleCountBy",Constants.LOCATION);
            inventoryArr.getJSONObject(i).put("cycleCountById",location.getString("locationIds"));
            inventoryArr.getJSONObject(i).put("locationIds",location.getString("locationIds"));

        }
        System.out.println("RESSS inventoryArr------>>>>>>> "+inventoryArr.toString());

        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkCycleCount,inventoryArr);
        System.out.println("res1---->>>" + res1);
        if(res1!=null && res1.getInt("status") == 200) {
            System.out.println("RESSS tag updated------>>>>>>> "+res1);
            sessionManager.clearPendingOps();
        }
        return new JSONObject();
    }


    public static JSONObject moveInventoryToLocation (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager,String status) throws JSONException, InterruptedException {
        System.out.println("RESSSS Inventory move---->"+story);
        JSONArray jsonArray = new JSONArray(story);
        JSONObject locationObj = jsonArray.getJSONObject(0);
        String locationObjString = locationObj.getString("data");
        JSONArray locationArr = new JSONArray(locationObjString);
        JSONObject location = locationArr.getJSONObject(0);

        JSONObject inventoryObj = jsonArray.getJSONObject(1);
        String inventoryObjString = inventoryObj.getString("data");
        JSONArray inventoryArr = new JSONArray(inventoryObjString);

        for (int i = 0; i < inventoryArr.length(); i++) {
            inventoryArr.getJSONObject(i).put("locationIds",location.getString("locationIds"));
            inventoryArr.getJSONObject(i).put("zoneIds",location.getString("zoneIds"));

        }

        System.out.println("RESSSS Inventory move location---->"+location);
        System.out.println("RESSSS Inventory move inventoryArr---->"+inventoryArr);
        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,inventoryArr);
        if(res1.getInt("status") == 200) {
            System.out.println("RESSS tag updated------>>>>>>> "+res1);
            sessionManager.clearPendingOps();
        }
        return new JSONObject();
    }


    public static JSONObject generalStatusChange (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager,String status) throws JSONException, InterruptedException {
        System.out.println("RESSSS Inventory move---->"+story);

//        JSONObject inventoryObj = jsonArray.getJSONObject(1);
//        String inventoryObjString = inventoryObj.getString("data");
//        JSONArray inventoryArr = new JSONArray(inventoryObjString);
//
//        for (int i = 0; i < inventoryArr.length(); i++) {
//            inventoryArr.getJSONObject(i).put("locationIds",location.getString("locationIds"));
//        }
//
//        System.out.println("RESSSS Inventory move location---->"+location);
//        System.out.println("RESSSS Inventory move inventoryArr---->"+inventoryArr);
////        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateLocation,inventoryArr);
////        if(res1.getInt("status") == 200) {
////            System.out.println("RESSS tag updated------>>>>>>> "+res1);
////            sessionManager.clearPendingOps();
////        }
        return new JSONObject();
    }

    public static JSONObject reCheckOrder(String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager,String status) throws JSONException, InterruptedException {

        System.out.println("reCheckOrder story------>>>>>>> "+story);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", sessionManager.getOrderData().getString("_id"));
        jsonObject.put("orderStatus", Constants.RECHECKED);
        jsonObject.put("operationStatus", Constants.RECHECKED);
        JSONObject res1 = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,jsonObject);
        if(res1!=null && res1.getInt("status") == 200) {
            System.out.println("RESSS tag updated------>>>>>>> "+res1);
            sessionManager.clearPendingOps();
        }
        return res1;
    }
}
