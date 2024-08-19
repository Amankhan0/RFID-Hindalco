package com.example.hellorfid;

import android.util.Log;

import org.apache.log4j.Logger;
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
    public String baseUrl = "http://192.168.1.15:9090/";

    private static final String TAG = "ApiCallBackWithToken";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public void Api(String url, JSONObject loginJson, ApiCallback callback, String token) {

        RequestBody body = RequestBody.create(JSON, loginJson.toString());

        System.out.println("token-------sdfdfsg--dvgfd"+token);

        Request request = new Request.Builder()
                .url(baseUrl + url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Log.d(TAG, "Sending request to URL: " + url + " with body: " + loginJson.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("dsfig-sfgf-rgre"+ e.getMessage());
                Log.e(TAG, "Request failed: " + e.getMessage(), e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();

                        System.out.println("responseBody----"+responseBody);

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
