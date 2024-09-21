package com.example.hellorfid.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hellorfid.constants.Constants;

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



    private static final String BatchData = "BatchData";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
    public String getReplaceSecondScan() {
        return sharedPreferences.getString(replaceScanSecond, null);
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
    public String getBatch() {
        return sharedPreferences.getString(BatchData, null);
    }

    // clear building
    public void clearBatch() {
        editor.remove(BatchData);
        editor.apply();
    }


    // Save payload
    public void setPayload(String payload) {
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
    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
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
