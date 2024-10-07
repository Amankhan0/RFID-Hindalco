package com.example.hellorfid.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.StoryHandler;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeneralStatusChangeActivity extends AppCompatActivity {
    private ListView listView;
    private SearchView searchView;
    private ApiCallBackWithToken apiCallBackWithToken;
    private List<String> statusOptions = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_master);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        apiCallBackWithToken = new ApiCallBackWithToken(this);
        SessionManager sessionManager = new SessionManager(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusOptions);
        listView.setAdapter(adapter);

        setupSearchView();
        fetchGeneralStatusOptions();

        listView.setOnItemClickListener((parent, view, position, id) -> {




            String selectedItem = statusOptions.get(position);
            System.out.println("Selected: "+ selectedItem);
            sessionManager.setOptionSelected(Constants.OPERATION_STATUS_CHANGE);

            StoryHandler.generalStatusChangeStory(sessionManager, this, "NA",selectedItem );
            Toast.makeText(GeneralStatusChangeActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void fetchGeneralStatusOptions() {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", 1);
            requestBody.put("limit", 100);

            JSONObject search = new JSONObject();
            search.put("fieldName", "general_status_change_spinner_HT");
            requestBody.put("search", search);

            apiCallBackWithToken.Api(Constants.searchGeneral, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    try {
                        JSONArray content = responseJson.getJSONArray("content");
                        statusOptions.clear();
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject item = content.getJSONObject(i);
                            String value = item.getString("value");
                            statusOptions.add(value);
                            Log.d("GeneralStatusChange", "Added option: " + value);
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        Log.e("GeneralStatusChange", "Error parsing JSON", e);
                    }
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("GeneralStatusChange", "API call failed", e);
                    runOnUiThread(() -> Toast.makeText(GeneralStatusChangeActivity.this, "Failed to load options", Toast.LENGTH_SHORT).show());
                }
            });
        } catch (JSONException e) {
            Log.e("GeneralStatusChange", "Error creating request body", e);
        }
    }
}