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



}
