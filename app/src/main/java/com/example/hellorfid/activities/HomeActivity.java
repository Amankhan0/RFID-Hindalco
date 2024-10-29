package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.constants.StoryHandler;
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
import java.util.Objects;

import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home";
    private ApiCallBackWithToken apiCallBackWithToken;
    private ListView buildingList;
    private HomeAdapter buildingAdapter;
    private List<HomeModel> buildingModels;
    private ProgressBar progressBar;
    private TextView loadingText;
private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        buildingList = findViewById(R.id.buildingList);
        buildingModels = new ArrayList<>();
        buildingAdapter = new HomeAdapter(this, buildingModels);
        buildingList.setAdapter(buildingAdapter);
        sessionManager = new SessionManager(this);
        // Loader Views
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        SessionManager sessionManager = new SessionManager(this);
        try {
            StoryHandler.checkPendingOps(sessionManager,this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Helper.setUserDetails(sessionManager);

        if (sessionManager.getToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        showLoader();
        hitApiAndLogResult();

        ImageView profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showLoader() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
            loadingText.setText("Loading...");
            buildingList.setVisibility(View.GONE);
        });
    }

    private void hideLoader() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            buildingList.setVisibility(View.VISIBLE);
        });
    }

    private void hitApiAndLogResult() {
        try {
            JSONObject requestBody = new JSONObject();

            // Set page and limit (as integers, not strings)
            int page = 1;
            int limit = 10;
            requestBody.put("page", page);
            requestBody.put("limit", limit);

            JSONObject search = new JSONObject();
            String userId = sessionManager.getUserId();
            JSONArray userIdsArray = new JSONArray();
            userIdsArray.put(userId);
            search.put("userIds", new JSONObject().put("$in", userIdsArray));
            requestBody.put("search", search);

            System.out.println("User ID from SessionManger: " + userId);
            System.out.println("Search JSON: " + search);
            System.out.println("Request Body JSON: " + requestBody);

            String apiEndpoint = Constants.searchBuilding;



            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    runOnUiThread(() -> {
                        System.out.println("responseJson---"+responseJson);
                        parseAndDisplayBuildings(responseJson);
                        hideLoader();  // Hide the loader once data is loaded
                    });
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "API call failed", e);
                    runOnUiThread(() -> {
                        hideLoader();  // Hide the loader if the API fails
                        loadingText.setText("Failed to load buildings");
                    });
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON request body", e);
            runOnUiThread(this::hideLoader);
        }
    }

    private void parseAndDisplayBuildings(JSONObject responseJson) {
        try {
            System.out.println("responseJson---&&&&&"+responseJson);
            JSONArray arr = responseJson.getJSONArray("content");
            buildingModels.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject buildingJson = arr.getJSONObject(i);
                String id = buildingJson.getString("_id");
                String name = buildingJson.getString("buildingName");
                String address = buildingJson.getString("buildingNo");
//                System.out.println("buildingJson.getBoolean(\"canAddNewTags\")"+buildingJson.getBoolean("canAddNewTags"));
//
                System.out.println("addEmptyBag------>>>>>>"+buildingJson.get("addEmptyBag"));
                boolean canAddNewTags = false;

                if(buildingJson.get("addEmptyBag")!=null){
                    canAddNewTags = buildingJson.getBoolean("addEmptyBag");
                }


                buildingModels.add(new HomeModel(id, name, address,canAddNewTags));
            }
            runOnUiThread(() -> buildingAdapter.notifyDataSetChanged());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response", e);
        }
    }
}
