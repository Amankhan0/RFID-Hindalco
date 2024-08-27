package com.example.hellorfid.constants;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.hellorfid.activities.OrderActivity;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Helper {

    public static String commanParser(String data, Boolean obj, CommanModel commanModel) throws JSONException {
        if (!obj) {
            // Get the JSONArray from the data
            JSONArray jsonArray = tag(data);
            JSONArray finalArray = new JSONArray() ;
            // Loop through the JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                String rfidTag = jsonArray.getString(i);
                try {
                    // Create a new JSONObject
                    JSONObject jsonObject = new JSONObject();
                    // Add key-value pairs to the JSONObject
                    jsonObject.put("batchId", commanModel.getBatchID());
                    jsonObject.put("rfidTag", rfidTag.toLowerCase());
                    jsonObject.put("orderId", commanModel.getOrderId());
                    jsonObject.put("currentLocation", commanModel.getCurrentLocation());
                    jsonObject.put("dispatchTo", commanModel.getDispatchTo());
                    jsonObject.put("readerId", null);
                    jsonObject.put("status", commanModel.getStatus());
                    jsonObject.put("movementStatus", commanModel.getMovementStatus());
                    jsonObject.put("errorMsg", JSONObject.NULL);
                    jsonObject.put("isError", false);
                    // Convert the JSONObject to a string and print it
                    finalArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            return finalArray.toString();

        }

        return null;
    }


    public static JSONObject commanUpdate(ApiCallBackWithToken apiCallBackWithToken, String URL,
                                          String whereKey,String wherevalue,String andKey, String andValue) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(whereKey, wherevalue);
                    jsonObject.put(andKey, andValue);
                    return internalHit(apiCallBackWithToken,URL,jsonObject);
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }

        return null;
    }



    static JSONArray tag(String data) throws JSONException {
        return new JSONArray(data);
    }


    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, String requestBody) throws JSONException, InterruptedException {
        JSONArray jsonArray = new JSONArray(requestBody);

        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};

        apiCallBackWithToken.Api(URL, jsonArray, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }


    private static JSONObject internalHit(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }


    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody,Boolean obj) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }




}
