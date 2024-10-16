package com.example.hellorfid.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hellorfid.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {

    private static final String PREF_NAME = "LoginSession";
    private static final String KEY_TOKEN = "token";
    private static final String buildingId = "buildingId";
    private static final String buildingIdTo = "buildingIdTo";
    private static final String KEY_PAYLOAD = "payload";
    private static final String KEY_ROLE = "role";

    private static final String replaceScanFirst = "replaceScanFirst";
    private static final String replaceScanSecond = "replaceScanSecond";
    private static final String IP_ADDRESS_KEY = "IP_ADDRESS_KEY";
    private static final String COMMANDATA = "COMMANDATA";
    private static final String ORDER_DATA = "ORDER_DATA";
    private static final String USER_ID = "USER_ID";
    private static final String OPTION_SELECTED = "OPTION_SELECTED";
    private static final String PRODUCT_DATA = "PRODUCT_DATA";
    private static final String LOCATION_DATA = "LOCATION_DATA";
    private static final String STORY = "STORY";
    private static final String CASE_EXCUTOR = "CASE_EXCUTOR";
    private static final String SCREEN_TYPE = "SCREEN_TYPE";

    private static final String CHECK_TAG_ON = "CHECK_TAG_ON";
    private static final String QTY = "QTY";
    private static final String SET_SCAN_COUNT = "SET_SCAN_COUNT";
    private static final String PENDING_OPS = "PENDING_OPS";
    private static final String IS_NEW_TAGS_ALLOWED_TO_BUILDING = "IS_NEW_TAGS_ALLOWED_TO_BUILDING";


    private static final String BatchData = "BatchData";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setScreenType(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(SCREEN_TYPE, token);
        editor.apply();
    }

    public void setNewTagsAllowed(boolean token) {
        System.out.println("Check Request 6 "+ token);
        editor.putBoolean(IS_NEW_TAGS_ALLOWED_TO_BUILDING, token);
        editor.apply();
    }

    // Get saved token
    public boolean getNewTagsAllowed() {
        return sharedPreferences.getBoolean(IS_NEW_TAGS_ALLOWED_TO_BUILDING, false);
    }


    public void setPendingOps(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(PENDING_OPS, token);
        editor.apply();
    }


    public void setCaseExcutor(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(CASE_EXCUTOR, token);
        editor.apply();
    }

    public void setStory(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(STORY, token);
        editor.apply();
    }

    public void setLocationData(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(LOCATION_DATA, token);
        editor.apply();
    }
    public void setProductData(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(PRODUCT_DATA, token);
        editor.apply();
    }

    public void setSetScanCount(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(SET_SCAN_COUNT, token);
        editor.apply();
    }

    public void setUserId(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(USER_ID, token);
        editor.apply();
    }

    public void setOptionSelected(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(OPTION_SELECTED, token);
        editor.apply();
    }

    public void setOrderData(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(ORDER_DATA, token);
        editor.apply();
    }

    public void setQty(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(QTY, token);
        editor.apply();
    }

    public void setCheckTagOn(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(CHECK_TAG_ON, token);
        editor.apply();
    }



    // Save token
    public void setCommandata(String token) {
        System.out.println("Check Request 6 "+ token);
        editor.putString(COMMANDATA, token);
        editor.apply();
    }

    // Save token
    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Save token
    public void setReplaceFirstScan(String tag) {
        editor.putString(replaceScanFirst, tag);
        editor.apply();
    }

    public void setReplaceSecondScan(String tag) {
        editor.putString(replaceScanSecond, tag);
        editor.apply();
    }

    // Get saved token
    public String getReplaceFirstScan() {
        return sharedPreferences.getString(replaceScanFirst, null);
    }

    // Get saved token
    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    // Get saved token
    public String getSetScanCount() {
        return sharedPreferences.getString(SET_SCAN_COUNT, null);
    }


    // Get saved token
    public String getCaseExcutor() {
        return sharedPreferences.getString(CASE_EXCUTOR, null);
    }


    // Get saved token
    public JSONObject getOrderData() throws JSONException {
        String fec = sharedPreferences.getString(ORDER_DATA, null);
        JSONObject jsonObject = new JSONObject(fec);
        return jsonObject;
    }

    // Get saved token
    public JSONObject getProductData() throws JSONException {
        String fec = sharedPreferences.getString(PRODUCT_DATA, null);
        JSONObject jsonObject = new JSONObject(fec);
        return jsonObject;
    }

    // Get saved token
    public String getReplaceSecondScan() {
        return sharedPreferences.getString(replaceScanSecond, null);
    }

    // Get saved token
    public String getScreenType() {
        return sharedPreferences.getString(SCREEN_TYPE, null);
    }

    // Get saved token
    public String getOptionSelected() {
        return sharedPreferences.getString(OPTION_SELECTED, null);
    }

    // clear building
    public void clearReplaceScan() {
        editor.remove(replaceScanFirst);
        editor.apply();
    }

    public void clearSecondReplaceScan() {
        editor.remove(replaceScanSecond);
        editor.apply();
    }
    // Get saved token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Save token
    public void setBuildingId(String id) {
        editor.putString(buildingId, id);
        editor.apply();
    }

    // Get saved token
    public String getBuildingId() {
        return sharedPreferences.getString(buildingId, null);
    }


    // Get saved token
    public String getPendingOps() {
        return sharedPreferences.getString(PENDING_OPS, null);
    }

    // clear building
    public void clearBuildingId() {
        editor.remove(buildingId);
        editor.apply();
    }

    public void setBuildingIdTo(String id) {
        editor.putString(buildingIdTo, id);
        editor.apply();
    }

    // Get saved token
    public String getBuildingIdTo() {
        return sharedPreferences.getString(buildingIdTo, null);
    }

    // Get saved token
    public String getCommandata() {
        return sharedPreferences.getString(COMMANDATA, null);
    }

    public String getCheckTagOn() {
        return sharedPreferences.getString(CHECK_TAG_ON, null);
    }

    public String getQty() {
        return sharedPreferences.getString(QTY, null);
    }

    // clear building
    public void clearBuildingIdTo() {
        editor.remove(buildingIdTo);
        editor.apply();
    }


    public void setBatch(String batch) {
        editor.putString(BatchData, batch);
        editor.apply();
    }

    // Get saved token
    public JSONArray getLocationData() throws JSONException {
        String fec = sharedPreferences.getString(LOCATION_DATA, null);
        JSONArray jsonObject = new JSONArray(fec);
        return jsonObject;
    }


    // Get saved token
    public String getBatch() {
        return sharedPreferences.getString(BatchData, null);
    }

    // clear building
    public void clearBatch() {
        editor.remove(BatchData);
        editor.apply();
    }

    public void clearPendingOps() {
        editor.remove(STORY);
        editor.remove(PENDING_OPS);
        editor.apply();
    }


    // Save payload
    public void setPayload(String payload) {
        System.out.println("saved------->>>>>>" + payload);
        editor.putString(KEY_PAYLOAD, payload);
        editor.apply();
    }

    // Get saved payload
    public String getPayload() {
        return sharedPreferences.getString(KEY_PAYLOAD, null);
    }

    // Save role
    public void setRole(String role) {
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    // Get saved role
    public String getRole(String value) throws JSONException {
        String data = sharedPreferences.getString(KEY_ROLE, null);
        if(data!=null){
            JSONObject jsonObject = new JSONObject(data);
            JSONArray content = jsonObject.getJSONArray("content");
            JSONArray permission = content.getJSONObject(0).getJSONArray("permission");
            System.out.println("permission---->"+permission);
            for(int i=0;i<permission.length();i++){
                if(permission.getJSONObject(i).get("value").equals("Handheld Manager")){
                    JSONArray child = permission.getJSONObject(i).getJSONArray("child");
                    for (int j=0;j<child.length();j++){
                        if(child.getJSONObject(j).get("value").equals(value)){
                            return child.getJSONObject(j).getJSONArray("permission").getJSONObject(0).getString("read");
                        }
                    }
                    System.out.println("permission---->"+permission.getJSONObject(i));

                }
            }


        }
        return null;
    }


    // Get saved role
    public String getStory() {
        return sharedPreferences.getString(STORY, null);
    }

    // Clear session
    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void setIpAddress(String ipAddress) {
        editor.putString(IP_ADDRESS_KEY, ipAddress);
        editor.apply();
    }

    public String getIpAddress() {

        if (sharedPreferences.getString(IP_ADDRESS_KEY, null) == null) {
            return Constants.url;
        }

        return sharedPreferences.getString(IP_ADDRESS_KEY, null);
    }
}
