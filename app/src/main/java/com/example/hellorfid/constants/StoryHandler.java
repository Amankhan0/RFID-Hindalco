package com.example.hellorfid.constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryHandler {

    public static JSONObject storyJson (String name,String caseName,JSONArray story) throws JSONException {
            JSONObject mainObject = new JSONObject();
            mainObject.put("name", name);
            mainObject.put("caseName", caseName);
            mainObject.put("story", story);
            return mainObject;
    }

    public static JSONObject storyJsonObj (String id,String actionName,boolean isExcuted,String actionType ,String data ,String scanQty ) throws JSONException {
        JSONObject action1 = new JSONObject();
        action1.put("id", id);
        action1.put("actionName", actionName);
        action1.put("isExcuted", isExcuted);
        action1.put("actionType", actionType);
        action1.put("data", data);
        action1.put("scanQty", scanQty);

        return action1;
    }


}
