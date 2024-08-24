package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hellorfid.R;
import com.example.hellorfid.adapter.BatchAdapter;
import com.example.hellorfid.adapter.HomeAdapter;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.BatchModel;
import com.example.hellorfid.model.HomeModel;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchActivity extends AppCompatActivity {


    private ApiCallBackWithToken apiCallBackWithToken;
    private ListView batchList;
    private List<BatchModel> batchModels;
    private BatchAdapter batchAdapter;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_batch);

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        sessionManager = new SessionManager(this);

        batchList = findViewById(R.id.batchList);
        batchModels = new ArrayList<>();
        batchAdapter = new BatchAdapter(this, batchModels);
        batchList.setAdapter(batchAdapter);

        hitApiAndLogResult();

        Button addNewBatchButton  =  findViewById(R.id.addNewBatchButton);

        addNewBatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BatchActivity.this, AddBatchFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void hitApiAndLogResult() {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", "1");
            requestBody.put("limit", "5");

            JSONObject search = new JSONObject();

            search.put("buildingId", sessionManager.getBuildingId());
            requestBody.put("search", search);

            System.out.println("requestBody" + requestBody);
            String apiEndpoint = "iot/api/searchBatch";

            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    parseAndDisplayBatch(responseJson);
                    System.out.println("responseJson------"+responseJson);
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("TAG", "API call failed", e);
                }
            });

        } catch (JSONException e) {
            Log.e("TAG", "Error creating JSON request body", e);
        }
    }

    private void parseAndDisplayBatch(JSONObject responseJson) {
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(responseJson));
            JSONArray arr = jsonObject.getJSONArray("content");
            System.out.println("arr----"+arr);
            batchModels.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject buildingJson = arr.getJSONObject(i);
                String id = buildingJson.getString("id");
                String batchName = buildingJson.getString("batchName");
                String batchNumber = buildingJson.getString("batchNumber");
                JSONObject productId = buildingJson.getJSONObject("product_id");
                String status = buildingJson.getString("status");
                String movementStatus = buildingJson.getString("movementStatus");
                String pid = productId.getString("id");
                int totalInventory = buildingJson.getInt("totalInventory");
                String productName = productId.getString("productName");

                batchModels.add(new BatchModel(id, batchName, batchNumber,productName,pid,status,movementStatus,totalInventory));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    batchAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            Log.e("TAG", "Error parsing JSON response", e);
        }
    }

}