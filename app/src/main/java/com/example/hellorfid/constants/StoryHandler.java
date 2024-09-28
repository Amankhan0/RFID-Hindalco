package com.example.hellorfid.constants;

import android.content.Context;
import android.content.Intent;

import com.example.hellorfid.activities.ActionActivity;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.activities.LoadProductAcordingToOrdersActivity;
import com.example.hellorfid.activities.MultiActionActivity;
import com.example.hellorfid.activities.PendingOpsActivity;
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

    public static JSONObject storyJsonObj (String id,String actionName,String caseName,boolean isExcuted,String actionType ,String data,String supportData ,String scanQty ) throws JSONException {
        JSONObject action1 = new JSONObject();
        action1.put("id", id);
        action1.put("actionName", actionName);
        action1.put("caseName", caseName);
        action1.put("isExcuted", isExcuted);
        action1.put("actionType", actionType);
        action1.put("supportData", supportData);
        action1.put("data", data);
        action1.put("scanQty", scanQty);

        return action1;
    }


    public static String mappingVehicle(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        System.out.println("mappingVehicle  "+type);
        JSONArray jsonArray=  new JSONArray();
        JSONObject story1 = StoryHandler.storyJsonObj("1",type,type,false,"SCAN","NO_DATA",supportData,"1");
//      JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,false,"SCAN","NO_DATA");
        JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","NA","0");
        jsonArray.put(story1);
        jsonArray.put(story3);
        JSONObject finalarr = StoryHandler.storyJson("Start Scanner"+" For "+type,type,jsonArray);
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
        JSONObject story1 = StoryHandler.storyJsonObj("1","REPLACE " + "FROM",type,false,"SCAN","NO_DATA",supportData,"1");
        JSONObject story2 = StoryHandler.storyJsonObj("2","REPLACE " + "TO",Constants.NEW_TAG,false,"SCAN","NO_DATA",supportData,"1");
        JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,type,false,"UPDATE","NO_DATA","NA","0");
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

    public static String holdInventory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONArray inventoryCaseArr=  new JSONArray();

            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1");
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
            jsonArray.put(story1);
            jsonArray.put(story2);

            JSONObject story3 = StoryHandler.storyJsonObj("1",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","","1000");
            JSONObject story4 = StoryHandler.storyJsonObj("2",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
            inventoryCaseArr.put(story3);
            inventoryCaseArr.put(story4);

            JSONObject finalarr = StoryHandler.storyJson(type +" "+ Constants.LOCATION,Constants.LOCATION,jsonArray);
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
            JSONArray jsonArray=  new JSONArray();
            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1");
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA",sessionManager.getProductData().toString(),sessionManager.getProductData().getString("quantity"));
            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
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
            throw new RuntimeException(e);
        }
        return "";
    }


    public static void cycleCountStory(SessionManager sessionManager, Context context,String supportData,String type) throws JSONException {
        try {
            JSONArray jsonArray=  new JSONArray();
            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1");
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","NA","1000");
            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
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
            JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1");
            JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,Constants.INVENTORY,false,"SCAN","NO_DATA","NA","1000");
            JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
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






}
