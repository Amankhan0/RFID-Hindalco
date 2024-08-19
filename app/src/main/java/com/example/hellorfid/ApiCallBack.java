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

    public String baseUrl = "http://192.168.1.15:9090/";

    private static final String TAG = "ApiCallBack";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();


    public void login(String url, JSONObject loginJson, ApiCallback callback) {

        RequestBody body = RequestBody.create(JSON, loginJson.toString());

//        System.out.println("in api call token" + token);

        Request request = new Request.Builder()
                .url(baseUrl+url)
                .post(body)
                .addHeader("Content-Type", "application/json" )
//                .addHeader("Authorization", "bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NSIsInJvbGVJZCI6eyJyb2xlTmFtZSI6IlN1cGVyIE1hc3RlciIsImFsbG93ZWRFbmRQb2ludHMiOlsiL3VzZXIvYXBpL3NlYXJjaFVzZXIiLCIvdXNlci9hcGkvYWRkVXNlciIsIi91c2VyL2FwaS91cGRhdGVVc2VyIiwiL3VzZXIvYXBpL2RlbGV0ZVVzZXIiLCIvdXNlci9hcGkvc2VhcmNoUm9sZSIsIi91c2VyL2FwaS9hZGRSb2xlIiwiL3VzZXIvYXBpL3VwZGF0ZVJvbGUiLCIvdXNlci9hcGkvZGVsZXRlUm9sZSIsIi9vcmRlci9hcGkvYWRkSW5ib3VuZCIsIi9vcmRlci9hcGkvdXBkYXRlSW5ib3VuZCIsIi9vcmRlci9hcGkvc2VhcmNoSW5ib3VuZCIsIi9vcmRlci9hcGkvZGVsZXRlSW5ib3VuZCIsIi9vcmRlci9hcGkvYWRkV2ViUmVjZWl2aW5nIiwiL29yZGVyL2FwaS91cGRhdGVXZWJSZWNlaXZpbmciLCIvb3JkZXIvYXBpL3NlYXJjaFdlYlJlY2VpdmluZyIsIi9vcmRlci9hcGkvZGVsZXRlV2ViUmVjZWl2aW5nIiwiL3BsYW50L2FwaS9zZWFyY2hTaXRlIiwiL3BsYW50L2FwaS9hZGRTaXRlIiwiL3BsYW50L2FwaS91cGRhdGVTaXRlIiwiL3BsYW50L2FwaS9kZWxldGVTaXRlIiwiL3BsYW50L2FwaS9zZWFyY2hCdWlsZGluZyIsIi9wbGFudC9hcGkvYWRkQnVpbGRpbmciLCIvcGxhbnQvYXBpL3VwZGF0ZUJ1aWxkaW5nIiwiL3BsYW50L2FwaS9kZWxldGVCdWlsZGluZyIsIi9wbGFudC9hcGkvc2VhcmNoUmVhZGVyIiwiL3BsYW50L2FwaS9hZGRSZWFkZXIiLCIvcGxhbnQvYXBpL3VwZGF0ZVJlYWRlciIsIi9wbGFudC9hcGkvZGVsZXRlUmVhZGVyIiwiL3BsYW50L2FwaS9hZGRab25lIiwiL3BsYW50L2FwaS91cGRhdGVab25lIiwiL3BsYW50L2FwaS9zZWFyY2hab25lIiwiL3BsYW50L2FwaS9kZWxldGVab25lIiwiL3BsYW50L2FwaS9hZGRMb2NhdGlvbiIsIi9wbGFudC9hcGkvdXBkYXRlTG9jYXRpb24iLCIvcGxhbnQvYXBpL3NlYXJjaExvY2F0aW9uIiwiL3BsYW50L2FwaS9kZWxldGVMb2NhdGlvbiIsIi9wbGFudC9hcGkvYWRkVGFnIiwiL3BsYW50L2FwaS91cGRhdGVUYWciLCIvcGxhbnQvYXBpL3NlYXJjaFRhZyIsIi9wbGFudC9hcGkvZGVsZXRlVGFnIiwiL2hlbHBlci9hcGkvc2VhcmNoUHJvZHVjdCIsIi9oZWxwZXIvYXBpL2FkZFByb2R1Y3QiLCIvaGVscGVyL2FwaS91cGRhdGVQcm9kdWN0IiwiL2hlbHBlci9hcGkvZGVsZXRlUHJvZHVjdCIsIi9oZWxwZXIvYXBpL3NlYXJjaEN1c3RvbWVyIiwiL2hlbHBlci9hcGkvYWRkQ3VzdG9tZXIiLCIvaGVscGVyL2FwaS91cGRhdGVDdXN0b21lciIsIi9oZWxwZXIvYXBpL2RlbGV0ZUN1c3RvbWVyIiwiL2hlbHBlci9hcGkvc2VhcmNoU3VwcGxpZXIiLCIvaGVscGVyL2FwaS9hZGRTdXBwbGllciIsIi9oZWxwZXIvYXBpL3VwZGF0ZVN1cHBsaWVyIiwiL2hlbHBlci9hcGkvZGVsZXRlU3VwcGxpZXIiLCIvaGVscGVyL2FwaS9zZWFyY2hHZW5lcmFsIiwiL2hlbHBlci9hcGkvYWRkR2VuZXJhbCIsIi9oZWxwZXIvYXBpL3VwZGF0ZUdlbmVyYWwiLCIvaGVscGVyL2FwaS9kZWxldGVHZW5lcmFsIiwiL2hlbHBlci9hcGkvYWRkVmVoaWNsZSIsIi9oZWxwZXIvYXBpL3VwZGF0ZVZlaGljbGUiLCIvaGVscGVyL2FwaS9zZWFyY2hWZWhpY2xlIiwiL2hlbHBlci9hcGkvZGVsZXRlVmVoaWNsZSIsIi9kZXZpY2UvYXBpL2FkZFJlYWRlciIsIi9kZXZpY2UvYXBpL3VwZGF0ZVJlYWRlciIsIi9kZXZpY2UvYXBpL3NlYXJjaFJlYWRlciIsIi9kZXZpY2UvYXBpL2RlbGV0ZVJlYWRlciIsIi9kZXZpY2UvYXBpL2xvZ2luUmVhZGVyIiwiL2RldmljZS9hcGkvYWRkTXF0dENvbmZpZyIsIi9kZXZpY2UvYXBpL3VwZGF0ZU1xdHRDb25maWciLCIvZGV2aWNlL2FwaS9zZWFyY2hNcXR0Q29uZmlnIiwiL2RldmljZS9hcGkvZGVsZXRlTXF0dENvbmZpZyIsIi9kZXZpY2UvYXBpL2FkZFdlaWdoaW5nU2NhbGUiLCIvZGV2aWNlL2FwaS91cGRhdGVXZWlnaGluZ1NjYWxlIiwiL2RldmljZS9hcGkvc2VhcmNoV2VpZ2hpbmdTY2FsZSIsIi9kZXZpY2UvYXBpL2RlbGV0ZVdlaWdoaW5nU2NhbGUiLCIvZGV2aWNlL2FwaS9yZWFkZXJTdGF0dXMiXSwicGVybWlzc2lvbiI6W3sidmFsdWUiOiJtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV0sImNoaWxkIjpbeyJ2YWx1ZSI6InVzZXIgbmFtYWdlbWVudCBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJyb2xlcyBhbmQgcGVybWlzc2lvbiBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJzaXRlIG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6ImJ1aWxkaW5ncyBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJSZWFkZXIgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoicmVhZGVyIHJlcGxhY2VtZW50IG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6InJlYWRlciBoZWFsdGggbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoicHJvZHVjdCBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJDdXN0b21lciBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJzdXBwbGllciBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJnZW5lcmFsIG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6InJlYWRlciBidWlsZGluZyBtYXBwaW5nIG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX1dfSx7InZhbHVlIjoiaW5ib3VuZCIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XSwiY2hpbGQiOlt7InZhbHVlIjoiaW5ib3VuZCBvcmRlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6IndlYiByZWNldmluZyIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX1dfSx7InZhbHVlIjoib3V0Ym91bmQiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6ZmFsc2UsImRlbGV0ZSI6ZmFsc2V9XSwiY2hpbGQiOlt7InZhbHVlIjoib3V0Ym91bmQgb3JkZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJ3ZWIgcGlja2luZyIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjpmYWxzZSwiZGVsZXRlIjpmYWxzZX1dfV19XSwic3RhdHVzIjpudWxsLCJjcmVhdGVkQnkiOm51bGwsInVwZGF0ZWRCeSI6bnVsbH0sImV4cCI6MTcyNDEwNDQwMiwidXNlcklkIjoiNjZiZjlkNTQ2NTI0MTY2ZWRjMjNmNDk5IiwiaWF0IjoxNzI0MDc1NjAyfQ._QNtgaeM-boTZ0wnf1_CrgY2OL5LAWWiBij0Iu4sg4PEzrOiyf9lnQwnDNd-JF6fVTFadc9bWRWUt3iS3NblLw")
                .build();

        System.out.println("request-----"+request.toString());
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
