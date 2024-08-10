package com.example.hellorfid;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCallBack {

    private static final String TAG = "ApiCallBack";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public void login(String url, JSONObject loginJson, ApiCallback callback) {
        RequestBody body = RequestBody.create(JSON, loginJson.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Log the request details
        Log.d(TAG, "Sending request to URL: " + url + " with body: " + loginJson.toString());

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
                        String responseBody = response.body().string();
                        JSONObject responseJson = new JSONObject(responseBody);

                        // Log the successful response
                        Log.d(TAG, "Request successful. Response: " + responseBody);

                        callback.onSuccess(responseJson);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage(), e);
                        callback.onFailure(e);
                    }
                } else {
                    String errorMessage = "Request failed with status code: " + response.code() + " and message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new IOException(errorMessage));
                }
            }
        });
    }

    public interface ApiCallback {
        void onSuccess(JSONObject responseJson);

        void onFailure(Exception e);
    }
}
