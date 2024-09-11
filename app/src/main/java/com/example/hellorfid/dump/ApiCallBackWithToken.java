package com.example.hellorfid.dump;

import android.content.Context;
import android.util.Log;

import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.session.SessionManager;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCallBackWithToken {

    private static final Logger log = Logger.getLogger(ApiCallBackWithToken.class);

    private static final String TAG = "ApiCallBackWithToken";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private SessionManager sessionManager;

    public ApiCallBackWithToken(Context context) {
        this.sessionManager = new SessionManager(context);
    }

    public void Api(String url, Object jsonData, ApiCallback callback) {
        // Retrieve the token from SessionManager
        String token = sessionManager.getToken();

        // Log token
        System.out.println("Retrieved token: " + token);

        String jsonString;
        if (jsonData instanceof JSONObject) {
            jsonString = ((JSONObject) jsonData).toString();
        } else if (jsonData instanceof JSONArray) {
            jsonString = ((JSONArray) jsonData).toString();
        } else {
            throw new IllegalArgumentException("Input must be either JSONObject or JSONArray");
        }

        RequestBody body = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(sessionManager.getIpAddress())
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Log.d(TAG, "Sending request to URL: " + url + " with body: " + jsonString);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage(), e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        System.out.println("responseBody---"+response);

                        String responseBody = response.body().string();
                        JSONObject responseJson = new JSONObject(responseBody);
                        Log.d(TAG, "Request successful. Response: " + responseBody);
                        callback.onSuccess(responseJson);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage(), e);
                        callback.onFailure(e);
                    }
                } else {
                    String errorMessage = "Request failed with status code: " + response.code() + " and message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new ApiException(response.code(), errorMessage));
                }
            }
        });
    }

    public interface ApiCallback {
        JSONObject onSuccess(JSONObject responseJson);
        void onFailure(Exception e);
    }

    public static class ApiException extends Exception {
        private int statusCode;

        public ApiException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}