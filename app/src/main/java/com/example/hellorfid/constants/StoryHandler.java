package com.example.hellorfid.constants;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;

import com.example.hellorfid.activities.ActionActivity;
import com.example.hellorfid.activities.GeneralStatusChangeActivity;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.activities.LoadProductAcordingToOrdersActivity;
import com.example.hellorfid.activities.MultiActionActivity;
import com.example.hellorfid.activities.PendingOpsActivity;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryHandler {


    public static void checkPendingOps (SessionManager sessionManager, Context context) throws JSONException {
        if(sessionManager.getPendingOps() != null && !sessionManager.getPendingOps().isEmpty() ){
            System.out.println("ops pending");
            Intent intent = new Intent(context, PendingOpsActivity.class);
            context.startActivity(intent);
        }

    }

    public static JSONObject storyJson (String name,String caseName,JSONArray story ) throws JSONException {
            JSONObject mainObject = new JSONObject();
            mainObject.put("name", name);
            mainObject.put("caseName", caseName);
            mainObject.put("story", story);

        return mainObject;
    }

    public static JSONObject storyJsonObj (String id,String actionName,String caseName,boolean isExcuted,String actionType ,String data,String supportData ,String scanQty, String description,JSONArray lookingFor,JSONArray keyCheck ) throws JSONException {
        JSONObject action1 = new JSONObject();
        action1.put("id", id);
        action1.put("actionName", actionName);
        action1.put("caseName", caseName);
        action1.put("isExcuted", isExcuted);
        action1.put("actionType", actionType);
        action1.put("supportData", supportData);
        action1.put("data", data);
        action1.put("scanQty", scanQty);
        action1.put("description", description);
        action1.put("lookingFor", lookingFor);
        action1.put("keyCheck", keyCheck);

        return action1;
    }


    public static String mappingVehicle(SessionManager sessionManager, Context context,String supportData,String type, String desc) throws JSONException {
        System.out.println("mappingVehicle  "+type);
        JSONArray jsonArray=  new JSONArray();
        JSONObject story1 = StoryHandler.storyJsonObj("1",sessionManager.getCheckTagOn(),sessionManager.getCheckTagOn(),false,"SCAN","NO_DATA",supportData,"1", desc,new JSONArray(),new JSONArray());
//      JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,false,"SCAN","NO_DATA");
        JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","NA","0", "",new JSONArray(),new JSONArray());
        jsonArray.put(story1);
        jsonArray.put(story3);
        JSONObject finalarr = StoryHandler.storyJson("Start Scanner"+" For "+type,sessionManager.getCheckTagOn(),jsonArray);
        JSONArray jsonArray1=  new JSONArray();
        jsonArray1.put(finalarr);
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setStory(jsonArray1.toString());
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setCheckTagOn(type);
        Intent intent = new Intent(context, MultiActionActivity.class);
        context.startActivity(intent);

        return "Vehicle Mapped" + jsonArray1.toString();
    }

    public static String replace(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        System.out.println("mappingVehicle  "+type);
        JSONArray jsonArray=  new JSONArray();
        JSONObject story1 = StoryHandler.storyJsonObj("1","REPLACE " + "FROM",type,false,"SCAN","NO_DATA",supportData,"1", "desc",new JSONArray(),new JSONArray());
        JSONObject story2 = StoryHandler.storyJsonObj("2","REPLACE " + "TO",Constants.NEW_TAG,false,"SCAN","NO_DATA",supportData,"1", "desc",new JSONArray(),new JSONArray());
        JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,type,false,"UPDATE","NO_DATA","NA","0", "desc",new JSONArray(),new JSONArray());
        jsonArray.put(story1);
        jsonArray.put(story2);
        jsonArray.put(story3);
        JSONObject finalarr = StoryHandler.storyJson(Constants.REPLACE,Constants.REPLACE,jsonArray);
        JSONArray jsonArray1=  new JSONArray();
        jsonArray1.put(finalarr);
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setStory(jsonArray1.toString());
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setCheckTagOn(type);
        Intent intent = new Intent(context, MultiActionActivity.class);
        context.startActivity(intent);

        return "Vehicle Mapped" + jsonArray1.toString();
    }

    public static String WeighingScale(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        System.out.println("mappingVehicle  "+type);
        JSONArray jsonArray=  new JSONArray();
        JSONObject story1 = StoryHandler.storyJsonObj("1","Weighing Scale",Constants.WeighingScale,false,"SCAN","NO_DATA",supportData,"1", "desc",new JSONArray(),new JSONArray());
        JSONObject story2 = StoryHandler.storyJsonObj("2","Nozzle",Constants.NOZZLE,false,"SCAN","NO_DATA",supportData,"1", "desc",new JSONArray(),new JSONArray());
        JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,type,false,"UPDATE","NO_DATA","NA","0", "desc",new JSONArray(),new JSONArray());
        jsonArray.put(story1);
        jsonArray.put(story2);
        jsonArray.put(story3);
        JSONObject finalarr = StoryHandler.storyJson(Constants.WeighingScale,Constants.WeighingScale,jsonArray);
        JSONArray jsonArray1=  new JSONArray();
        jsonArray1.put(finalarr);
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setStory(jsonArray1.toString());
        System.out.println("finalarr.toString()"+jsonArray1.toString());
        sessionManager.setCheckTagOn(type);
        Intent intent = new Intent(context, MultiActionActivity.class);
        context.startActivity(intent);

        return "Vehicle Mapped" + jsonArray1.toString();
    }

    public static String holdInventory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONArray inventoryCaseArr=  new JSONArray();

            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1", "desc",new JSONArray().put(Constants.LOCATION),new JSONArray());
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0", "desc",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);

            JSONObject story3 = StoryHandler.storyJsonObj("1",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","","1000", "desc",new JSONArray().put(Constants.INVENTORY),new JSONArray());
            JSONObject story4 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0", "desc",new JSONArray(),new JSONArray());
            inventoryCaseArr.put(story3);
            inventoryCaseArr.put(story4);

            JSONObject finalarr = StoryHandler.storyJson(type +" "+ Constants.LOCATION,type,jsonArray);
            JSONObject finalarr1 = StoryHandler.storyJson(type +" "+ Constants.INVENTORY,type+"_"+Constants.INVENTORY,inventoryCaseArr);

            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);
            jsonArray1.put(finalarr1);

            sessionManager.setStory(jsonArray1.toString());

            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    public static String orderStory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONObject orderData = sessionManager.getOrderData();
            ApiCallBackWithToken apiCallBackWithToken = new ApiCallBackWithToken(context);
            JSONObject searchtag = new JSONObject();

            searchtag.put("orderId", orderData.getString("_id"));

            System.out.println("searchtag--->>>"+searchtag);
            JSONObject searchJson = Helper.getSearchJson(1, 5, searchtag);
            System.out.println("searchJson--->>>"+searchJson);


//           String Quantity = sessionManager.getProductData().getString("quantity");


//            JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.searchRfidTag,jsonObject);


            int ToTalQuantity = parseInt(sessionManager.getProductData().getString("quantity"));
            int finalQty = ToTalQuantity;
            System.out.println("orderData.getString(\"orderStatus\")"+orderData.getString("orderStatus"));



            if (orderData.getString("orderStatus").equals(Constants.ORDER_RECEIVED_PARTIALLY) || orderData.getString("orderStatus").equals(Constants.ORDER_PICKED_PARTIALLY)){
                JSONObject searchRfidTagData = Helper.commanHitApi(apiCallBackWithToken, Constants.searchRfidTag, searchJson);
                String Quantity = "";
                System.out.println("searchRfidTagData--->><><>>"+searchRfidTagData);
                if (searchRfidTagData != null){
                    ToTalQuantity =  (ToTalQuantity - searchRfidTagData.getInt("totalElements"));
                   JSONObject obj =  sessionManager.getProductData();
                   obj.put("quantity",ToTalQuantity);
                    sessionManager.setProductData(obj.toString());
                    System.out.println("inside searchRfidTagData--->><><>>"+searchRfidTagData);
                }
            }




            System.out.println("sessionManager.getOrderData()"+orderData);
            System.out.println("quamtity++++" + ToTalQuantity);

            JSONArray jsonArray=  new JSONArray();

            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA",sessionManager.getProductData().toString(),"1", "desc",new JSONArray().put(Constants.LOCATION),new JSONArray().put("tagType"));

            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA",
                    sessionManager.getProductData().toString()
                    ,String.valueOf(ToTalQuantity), "desc"
                    ,new JSONArray(),new JSONArray());

            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0","desc",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);
            jsonArray.put(story3);
            JSONObject finalarr = StoryHandler.storyJson("Start scanner for "+sessionManager.getOptionSelected()+" Order",sessionManager.getOptionSelected(),jsonArray);
            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            sessionManager.setStory(jsonArray1.toString());
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            System.out.println("in cathc---->><><");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static String orderRecheck(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA"
                    ,supportData,"1000", "desc",new JSONArray().put(Constants.INVENTORY).put(Constants.RECHECK),new JSONArray());
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0","desc",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);
            JSONObject finalarr = StoryHandler.storyJson("Start scanner for "+sessionManager.getOptionSelected()+" Order",sessionManager.getOptionSelected(),jsonArray);
            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            sessionManager.setStory(jsonArray1.toString());
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    public static void cycleCountStory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {

            JSONArray jsonArray=  new JSONArray();
            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1","desc",new JSONArray(),new JSONArray());
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","NA","1000", "desc",new JSONArray(),new JSONArray());
            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0", "desc",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);
            jsonArray.put(story3);
            JSONObject finalarr = StoryHandler.storyJson(Constants.CYCLE_COUNT, Constants.CYCLE_COUNT,jsonArray);
            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            sessionManager.setStory(jsonArray1.toString());
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public static void inventoryMoveStory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONObject story1 = StoryHandler.storyJsonObj("1","TO "+Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1", "desc",new JSONArray(),new JSONArray());
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","NA","1000", "desc",new JSONArray(),new JSONArray());
            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0", "desc",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);
            jsonArray.put(story3);
            JSONObject finalarr = StoryHandler.storyJson(Constants.MOVE+" "+Constants.INVENTORY, Constants.MOVE,jsonArray);
            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            sessionManager.setStory(jsonArray1.toString());
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public static void generalStatusChangeStory(SessionManager sessionManager, Context context,String supportData, String type){
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONObject story1= StoryHandler.storyJsonObj("1",Constants.INVENTORY,type,false,"SCAN","NO_DATA","NA","1000", "First step is to scan the inventory",new JSONArray(),new JSONArray());
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0", "click on the update button to update the status",new JSONArray(),new JSONArray());
            jsonArray.put(story1);
            jsonArray.put(story2);
            JSONObject finalarr = StoryHandler.storyJson(Constants.OPERATION_STATUS_CHANGE, Constants.OPERATION_STATUS_CHANGE,jsonArray);
            JSONArray jsonArray1=  new JSONArray();
            jsonArray1.put(finalarr);

            System.out.println("finalarr.toString()"+jsonArray1.toString());
            sessionManager.setStory(jsonArray1.toString());
            sessionManager.setCheckTagOn(type);
            System.out.println("finalarr.toString()"+jsonArray1.toString());
            Intent intent = new Intent(context, MultiActionActivity.class);
            context.startActivity(intent);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }







}
