package com.example.hellorfid.constants;

import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CaseExecutorHandler {

    public static JSONObject holdUsingLocationTag (String story, ApiCallBackWithToken apiCallBackWithToken) throws JSONException, InterruptedException {
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
            return res1;
    }


    public static JSONObject unHoldUsingLocationTag (String story, ApiCallBackWithToken apiCallBackWithToken) throws JSONException, InterruptedException {
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
        return res1;
    }


    public static JSONObject outboundOrder (String story, ApiCallBackWithToken apiCallBackWithToken, SessionManager sessionManager) throws JSONException, InterruptedException {
        JSONArray jsonArray = new JSONArray(story);
        JSONObject locationObjH = jsonArray.getJSONObject(0);
        JSONObject inventoryObjH = jsonArray.getJSONObject(1);
        String locationData = locationObjH.getString("data");
        String inventoryData = inventoryObjH.getString("data");
        JSONArray arrLocData = new JSONArray(locationData);
        JSONObject location = arrLocData.getJSONObject(0);

        JSONArray resData = new JSONArray(inventoryData);
        
        for (int i = 0; i < resData.length(); i++) {

            resData.getJSONObject(i).put("tagType",Constants.INVENTORY);
            resData.getJSONObject(i).put("currentLocation",sessionManager.getBuildingId());
            resData.getJSONObject(i).put("locationIds",location.getString("locationIds"));
            resData.getJSONObject(i).put("zoneIds",location.getString("zoneIds"));
            resData.getJSONObject(i).put("buildingId",sessionManager.getBuildingId());
            resData.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData.getJSONObject(i).put("dispatchFrom",sessionManager.getOrderData().getString("dispatchFrom"));
            resData.getJSONObject(i).put("orderStatus",sessionManager.getOptionSelected().equals(Constants.OUTBOUND_ORDER)?Constants.ORDER_PICKED:Constants.ORDER_RECEIVED);
            resData.getJSONObject(i).put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            resData.getJSONObject(i).put("movementStatus",Constants.IN_BUILDING);
            resData.getJSONObject(i).put("status",Constants.EMPTY);
            resData.getJSONObject(i).put("batchNumber",sessionManager.getOrderData().has("batchNumber")?sessionManager.getOrderData().getString("batchNumber"):"NA");
            resData.getJSONObject(i).put("product_id",sessionManager.getProductData().getJSONObject("productId").getString("_id"));
            resData.getJSONObject(i).put("orderId",sessionManager.getOrderData().getString("_id"));
            resData.getJSONObject(i).put("createdBy",sessionManager.getUserId());
            resData.getJSONObject(i).put("updatedBy",sessionManager.getUserId());
            
            resData.getJSONObject(i).put("opreationStatus",Constants.ACTIVE);
        }

        System.out.println("resData "+resData.toString());
        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,resData);
        return res1;
    }



    public static JSONObject vehicleMap (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("story------>>>>>>> "+story);

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

    public static JSONObject zoneCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("story------>>>>>>> "+story);

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
            JSONObject upadate = Helper.commanHitApi(apiCallBackWithToken,Constants.updateVehicle,supportDataObj);
            System.out.println("RESSS tag supportDataObj------>>>>>>> "+supportDataObj);
            if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
        }
        return new JSONObject();
    }


    public static JSONObject locationCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("story------>>>>>>> "+story);

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
        tagData.put("locationIds",supportDataObj.getString("_id"));
        tagData.put("zoneIds",supportDataObj.getJSONArray("zoneIds").get(0));
        tagData.put("buildingIds",supportDataObj.getJSONArray("buildingIds").get(0));
        tagData.put("tagType",Constants.LOCATION);
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
            JSONObject upadate = Helper.commanHitApi(apiCallBackWithToken,Constants.updateVehicle,supportDataObj);
            System.out.println("RESSS tag supportDataObj------>>>>>>> "+supportDataObj);
            if(res1.getInt("status") == 200) {
                System.out.println("RESSS tag updated------>>>>>>> "+upadate);
                sessionManager.clearPendingOps();
            }
        }
        return new JSONObject();
    }


    public static JSONObject replcaeCase (String story, ApiCallBackWithToken apiCallBackWithToken,SessionManager sessionManager) throws JSONException, InterruptedException {

        System.out.println("story------>>>>>>> "+story);

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

        System.out.println("story------>>>>>>> "+story);

//
        JSONArray jsonArray = new JSONArray(story);
        JSONObject inventoryObj = jsonArray.getJSONObject(0);
        String fromData = inventoryObj.getString("data");
        JSONArray inventoryArr = new JSONArray(fromData);

        for (int i = 0; i < inventoryArr.length(); i++) {
            inventoryArr.getJSONObject(i).put("opreationStatus",status);
            System.out.println("RESSS inventoryArr------>>>>>>> "+inventoryArr.get(i));
        }

        JSONObject res1 = Helper.commanHitApi(apiCallBackWithToken,Constants.updateBulkTags,inventoryArr);
        if(res1.getInt("status") == 200) {
            System.out.println("RESSS tag updated------>>>>>>> "+res1);
            sessionManager.clearPendingOps();
        }

//        JSONObject from = fromArr.getJSONObject(0);
//
//
//        JSONObject toObj = jsonArray.getJSONObject(1);
//        String toData = toObj.getString("data");
//        JSONArray toArr = new JSONArray(toData);
//        JSONObject to = toArr.getJSONObject(0);
//
//        from.put("rfidTag",to.getString("rfidTag"));
//        System.out.println("RESSS from------>>>>>>> "+from);
//        System.out.println("RESSS to------>>>>>>> "+to);
//


        return new JSONObject();
    }



}
