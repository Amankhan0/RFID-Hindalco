package com.example.hellorfid.dump;

import android.content.Context;
import android.util.Log;

import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.session.SessionManager;

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

    private Context context;

    private SessionManager sessionManager;

    public ApiCallBack(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(context);
    }


    //    public String baseUrl = "http://192.168.0.114:9090/";
//public String baseUrl = "http://192.168.1.15:9090/";


public String baseUrl = "http://137.184.74.218/";
    private static final String TAG = "ApiCallBack";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

   public void login(String newUrl, JSONObject loginJson, ApiCallback callback) {


       String ipAddress = sessionManager.getIpAddress();
       if (ipAddress == null || ipAddress.isEmpty()) {
           Log.e(TAG, "IP address is null or empty");
           callback.onFailure(new IllegalStateException("IP address is not set"));
           return;
       }

       String fullUrl = ipAddress + newUrl;

       RequestBody body = RequestBody.create(JSON, loginJson.toString());

        Request request = new Request.Builder()
                .url(fullUrl)
                .post(body)
                .addHeader("Content-Type", "application/json" )
                .addHeader("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NSIsInJvbGVJZCI6eyJyb2xlTmFtZSI6IlN1cGVyIE1hc3RlciIsImFsbG93ZWRFbmRQb2ludHMiOlsiL3VzZXIvYXBpL3NlYXJjaFVzZXIiLCIvdXNlci9hcGkvYWRkVXNlciIsIi91c2VyL2FwaS91cGRhdGVVc2VyIiwiL3VzZXIvYXBpL2RlbGV0ZVVzZXIiLCIvdXNlci9hcGkvc2VhcmNoUm9sZSIsIi91c2VyL2FwaS9jaGFuZ2VQYXNzd29yZCIsIi91c2VyL2FwaS9hZGRSb2xlIiwiL3VzZXIvYXBpL3VwZGF0ZVJvbGUiLCIvdXNlci9hcGkvZGVsZXRlUm9sZSIsIi9vcmRlci9hcGkvYWRkSW5ib3VuZCIsIi9vcmRlci9hcGkvYWRkT3V0Ym91bmQiLCIvb3JkZXIvYXBpL3VwZGF0ZU91dGJvdW5kIiwiL29yZGVyL2FwaS9zZWFyY2hPdXRib3VuZCIsIi9vcmRlci9hcGkvZGVsZXRlT3V0Ym91bmQiLCIvb3JkZXIvYXBpL3VwZGF0ZUluYm91bmQiLCIvb3JkZXIvYXBpL3NlYXJjaEluYm91bmQiLCIvb3JkZXIvYXBpL2RlbGV0ZUluYm91bmQiLCIvb3JkZXIvYXBpL2FkZFdlYlJlY2VpdmluZyIsIi9vcmRlci9hcGkvdXBkYXRlV2ViUmVjZWl2aW5nIiwiL29yZGVyL2FwaS9zZWFyY2hXZWJSZWNlaXZpbmciLCIvb3JkZXIvYXBpL2RlbGV0ZVdlYlJlY2VpdmluZyIsIi9wbGFudC9hcGkvc2VhcmNoU2l0ZSIsIi9wbGFudC9hcGkvYWRkU2l0ZSIsIi9wbGFudC9hcGkvdXBkYXRlU2l0ZSIsIi9wbGFudC9hcGkvZGVsZXRlU2l0ZSIsIi9wbGFudC9hcGkvc2VhcmNoQnVpbGRpbmciLCIvcGxhbnQvYXBpL2FkZEJ1aWxkaW5nIiwiL3BsYW50L2FwaS91cGRhdGVCdWlsZGluZyIsIi9wbGFudC9hcGkvZGVsZXRlQnVpbGRpbmciLCIvcGxhbnQvYXBpL3NlYXJjaFJlYWRlciIsIi9wbGFudC9hcGkvYWRkUmVhZGVyIiwiL3BsYW50L2FwaS91cGRhdGVSZWFkZXIiLCIvcGxhbnQvYXBpL2RlbGV0ZVJlYWRlciIsIi9wbGFudC9hcGkvYWRkWm9uZSIsIi9wbGFudC9hcGkvdXBkYXRlWm9uZSIsIi9wbGFudC9hcGkvc2VhcmNoWm9uZSIsIi9wbGFudC9hcGkvZGVsZXRlWm9uZSIsIi9wbGFudC9hcGkvYWRkTG9jYXRpb24iLCIvcGxhbnQvYXBpL3VwZGF0ZUxvY2F0aW9uIiwiL3BsYW50L2FwaS9zZWFyY2hMb2NhdGlvbiIsIi9wbGFudC9hcGkvZGVsZXRlTG9jYXRpb24iLCIvcGxhbnQvYXBpL2FkZFRhZyIsIi9wbGFudC9hcGkvdXBkYXRlVGFnIiwiL3BsYW50L2FwaS9zZWFyY2hUYWciLCIvcGxhbnQvYXBpL2RlbGV0ZVRhZyIsIi9wbGFudC9hcGkvYWRkQnVpbGRpbmdUb1pvbmUiLCIvcGxhbnQvYXBpL3JlbW92ZUJ1aWxkaW5nRnJvbVpvbmUiLCIvcGxhbnQvYXBpL2FkZFpvbmVUb0xvY2F0aW9uIiwiL3BsYW50L2FwaS9yZW1vdmVab25lRnJvbUxvY2F0aW9uIiwiL2hlbHBlci9hcGkvc2VhcmNoUHJvZHVjdCIsIi9oZWxwZXIvYXBpL2FkZFByb2R1Y3QiLCIvaGVscGVyL2FwaS91cGRhdGVQcm9kdWN0IiwiL2hlbHBlci9hcGkvZGVsZXRlUHJvZHVjdCIsIi9oZWxwZXIvYXBpL3NlYXJjaEN1c3RvbWVyIiwiL2hlbHBlci9hcGkvYWRkQ3VzdG9tZXIiLCIvaGVscGVyL2FwaS91cGRhdGVDdXN0b21lciIsIi9oZWxwZXIvYXBpL2RlbGV0ZUN1c3RvbWVyIiwiL2hlbHBlci9hcGkvc2VhcmNoU3VwcGxpZXIiLCIvaGVscGVyL2FwaS9hZGRTdXBwbGllciIsIi9oZWxwZXIvYXBpL3VwZGF0ZVN1cHBsaWVyIiwiL2hlbHBlci9hcGkvZGVsZXRlU3VwcGxpZXIiLCIvaGVscGVyL2FwaS9zZWFyY2hHZW5lcmFsIiwiL2hlbHBlci9hcGkvYWRkR2VuZXJhbCIsIi9oZWxwZXIvYXBpL3VwZGF0ZUdlbmVyYWwiLCIvaGVscGVyL2FwaS9kZWxldGVHZW5lcmFsIiwiL2hlbHBlci9hcGkvYWRkVmVoaWNsZSIsIi9oZWxwZXIvYXBpL3VwZGF0ZVZlaGljbGUiLCIvaGVscGVyL2FwaS9zZWFyY2hWZWhpY2xlIiwiL2hlbHBlci9hcGkvZGVsZXRlVmVoaWNsZSIsIi9kZXZpY2UvYXBpL2FkZFJlYWRlciIsIi9kZXZpY2UvYXBpL3VwZGF0ZVJlYWRlciIsIi9kZXZpY2UvYXBpL3NlYXJjaFJlYWRlciIsIi9kZXZpY2UvYXBpL2RlbGV0ZVJlYWRlciIsIi9kZXZpY2UvYXBpL2xvZ2luUmVhZGVyIiwiL2RldmljZS9hcGkvYWRkTXF0dENvbmZpZyIsIi9kZXZpY2UvYXBpL3VwZGF0ZU1xdHRDb25maWciLCIvZGV2aWNlL2FwaS9zZWFyY2hNcXR0Q29uZmlnIiwiL2RldmljZS9hcGkvZGVsZXRlTXF0dENvbmZpZyIsIi9kZXZpY2UvYXBpL2FkZFdlaWdoaW5nU2NhbGUiLCIvZGV2aWNlL2FwaS91cGRhdGVXZWlnaGluZ1NjYWxlIiwiL2RldmljZS9hcGkvc2VhcmNoV2VpZ2hpbmdTY2FsZSIsIi9kZXZpY2UvYXBpL2RlbGV0ZVdlaWdoaW5nU2NhbGUiLCIvZGV2aWNlL2FwaS9yZWFkZXJTdGF0dXMiLCIvaW90L2FwaS9zZWFyY2hSZmlkVGFnIiwiL2lvdC9hcGkvYWRkVGFnIiwiL2lvdC9hcGkvYWRkQmF0Y2giLCIvaW90L2FwaS91cGRhdGVCYXRjaCIsIi9pb3QvYXBpL3NlYXJjaEJhdGNoIiwiL3BsYW50L2FwaS9tYXBwaW5nIl0sInBlcm1pc3Npb24iOlt7InZhbHVlIjoibWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dLCJjaGlsZCI6W3sidmFsdWUiOiJ1c2VyIG5hbWFnZW1lbnQgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoicm9sZXMgYW5kIHBlcm1pc3Npb24gbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoic2l0ZSBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJidWlsZGluZ3MgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoiUmVhZGVyIG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6InJlYWRlciByZXBsYWNlbWVudCBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJyZWFkZXIgaGVhbHRoIG1hc3RlciIsInBlcm1pc3Npb24iOlt7InJlYWQiOnRydWUsIndyaXRlIjp0cnVlLCJkZWxldGUiOnRydWV9XX0seyJ2YWx1ZSI6InByb2R1Y3QgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoiQ3VzdG9tZXIgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoic3VwcGxpZXIgbWFzdGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoiZ2VuZXJhbCBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJyZWFkZXIgYnVpbGRpbmcgbWFwcGluZyBtYXN0ZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19XX0seyJ2YWx1ZSI6ImluYm91bmQiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV0sImNoaWxkIjpbeyJ2YWx1ZSI6ImluYm91bmQgb3JkZXIiLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19LHsidmFsdWUiOiJ3ZWIgcmVjZXZpbmciLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6dHJ1ZSwiZGVsZXRlIjp0cnVlfV19XX0seyJ2YWx1ZSI6Im91dGJvdW5kIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOmZhbHNlLCJkZWxldGUiOmZhbHNlfV0sImNoaWxkIjpbeyJ2YWx1ZSI6Im91dGJvdW5kIG9yZGVyIiwicGVybWlzc2lvbiI6W3sicmVhZCI6dHJ1ZSwid3JpdGUiOnRydWUsImRlbGV0ZSI6dHJ1ZX1dfSx7InZhbHVlIjoid2ViIHBpY2tpbmciLCJwZXJtaXNzaW9uIjpbeyJyZWFkIjp0cnVlLCJ3cml0ZSI6ZmFsc2UsImRlbGV0ZSI6ZmFsc2V9XX1dfV19LCJleHAiOjE3MjQyNzA3OTMsInVzZXJJZCI6IjY2YmY5ZDU0NjUyNDE2NmVkYzIzZjQ5OSIsImlhdCI6MTcyNDI0MTk5M30.ycyXAFO7kyBPrI9mPe1b-2Y2MRz62TWodGqCiqOk1KmWHzwpm6iEQkMCx1lBT_3qMh2pMI8oNf8nYRt_kMJwmA")
                .build();

        System.out.println("request-----"+request.toString());

        Log.d(TAG, "Sending request to URL: " + newUrl + " with body: " + loginJson.toString());

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
