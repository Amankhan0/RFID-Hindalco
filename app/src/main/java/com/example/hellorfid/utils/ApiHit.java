package com.example.hellorfid.utils;

import android.content.Context;
import android.util.Log;

import com.example.hellorfid.dump.ApiCallBackWithToken;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiHit {
    private static ApiCallBackWithToken apiCallBackWithToken;

    public interface ApiHitCallback {
        void onResult(JSONObject responseJson);
        void onError(Exception e);
    }

    public static void hitApiAndLogResult(Context context, String url, ApiHitCallback callback) {
        try {
            apiCallBackWithToken = new ApiCallBackWithToken(context);

            JSONObject requestBody = new JSONObject();
            requestBody.put("page", "1");
            requestBody.put("limit", "5");
            requestBody.put("search", new JSONObject());

            System.out.println("requestBody" + requestBody);
            String apiEndpoint = url;

            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    Log.d("TAG", "API call successful. Response: " + responseJson.toString());
                    callback.onResult(responseJson);
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("TAG", "API call failed--------", e);
                    callback.onError(e);
                }
            });

        } catch (JSONException e) {
            Log.e("TAG", "Error creating JSON request body", e);
            callback.onError(e);
        }
    }
}
