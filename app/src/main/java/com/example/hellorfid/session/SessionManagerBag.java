package com.example.hellorfid.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerBag {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "AddBagPrefs";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_BUILDING = "building";
    private static final String KEY_LOT_NUMBER = "lot_number";
    private static final String KEY_BATCH_NUMBER = "batch_number";
    private static final String KEY_BAG_QUANTITY = "bag_quantity";
    private static final String KEY_TOKEN = "token";

    public SessionManagerBag(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void setProductName(String productName) {
        editor.putString(KEY_PRODUCT_NAME, productName);
        editor.apply();
    }

    public String getProductName() {
        return sharedPreferences.getString(KEY_PRODUCT_NAME, null);
    }

    public void setBuilding(String building) {
        editor.putString(KEY_BUILDING, building);
        editor.apply();
    }

    public String getBuilding() {
        return sharedPreferences.getString(KEY_BUILDING, null);
    }

    public void setLotNumber(String lotNumber) {
        editor.putString(KEY_LOT_NUMBER, lotNumber);
        editor.apply();
    }

    public String getLotNumber() {
        return sharedPreferences.getString(KEY_LOT_NUMBER, null);
    }

    public void setBatchNumber(String batchNumber) {
        editor.putString(KEY_BATCH_NUMBER, batchNumber);
        editor.apply();
    }

    public String getBatchNumber() {
        return sharedPreferences.getString(KEY_BATCH_NUMBER, null);
    }

    public void setBagQuantity(String bagQuantity) {
        editor.putString(KEY_BAG_QUANTITY, bagQuantity);
        editor.apply();
    }

    public String getBagQuantity() {
        return sharedPreferences.getString(KEY_BAG_QUANTITY, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}