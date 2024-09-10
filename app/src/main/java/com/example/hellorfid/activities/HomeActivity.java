package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.adapter.HomeAdapter;
import com.example.hellorfid.model.HomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home";
    private ApiCallBackWithToken apiCallBackWithToken;
    private ListView buildingList;
    private HomeAdapter buildingAdapter;
    private List<HomeModel> buildingModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        buildingList = findViewById(R.id.buildingList);
        buildingModels = new ArrayList<>();
        buildingAdapter = new HomeAdapter(this, buildingModels);
        buildingList.setAdapter(buildingAdapter);

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.getToken() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        // Call the API
        hitApiAndLogResult();
        ImageView ProfileButton = findViewById(R.id.profile);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });
    }

    private void hitApiAndLogResult() {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", "1");
            requestBody.put("limit", "5");
            requestBody.put("search", new JSONObject());

            System.out.println("requestBody" + requestBody);
            String apiEndpoint = "plant/api/searchBuilding";

            System.out.println("requestBody---"+requestBody);

            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    Log.d(TAG, "API call successful. Response: " + responseJson.toString());
                    parseAndDisplayBuildings(responseJson);
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "API call failed", e);
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body", e);
        }
    }

    private void parseAndDisplayBuildings(JSONObject responseJson) {
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(responseJson));
            JSONArray arr = jsonObject.getJSONArray("content");
            buildingModels.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject buildingJson = arr.getJSONObject(i);
                String id = buildingJson.getString("_id");
                String name = buildingJson.getString("buildingName");
                String address = buildingJson.getString("buildingNo");
                buildingModels.add(new HomeModel(id, name, address));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buildingAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response", e);
        }
    }
}