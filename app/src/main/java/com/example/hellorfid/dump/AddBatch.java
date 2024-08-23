package com.example.hellorfid.dump;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddBatch extends AppCompatActivity {

    private static final String TAG = "AddBatch";
    private TableLayout tableLayout;
    private ApiCallBackWithToken apiCallBackWithToken;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch);

        tableLayout = findViewById(R.id.tableLayout);
        apiCallBackWithToken = new ApiCallBackWithToken(AddBatch.this);
        sessionManager = new SessionManager(this); // Initialize sessionManager here

        // Call the API
        callSearchBatchApi();
    }

    private void callSearchBatchApi() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("page", 1);
            requestBody.put("limit", 10);
            requestBody.put("search", new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(AddBatch.this, "Error creating request body", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(AddBatch.this, "No token available. Please login first.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiCallBackWithToken.Api("iot/api/searchBatch", requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                runOnUiThread(() -> {
                    try {
                        // Log the full response
                        if (responseJson != null) {
                            Log.d(TAG, "API Response: " + responseJson.toString(2));
                        } else {
                            Log.e(TAG, "API Response is null");
                        }

                        // Parse and display the table
                        JSONArray contentArray = responseJson.optJSONArray("content");
                        if (contentArray != null) {
                            displayDataInTable(contentArray);
                        } else {
                            Log.e(TAG, "Content array is null");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AddBatch.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                });
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(AddBatch.this, "API call failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void displayDataInTable(JSONArray contentArray) {
        // Clear existing rows
        tableLayout.removeAllViews();

        // Add header row
        TableRow headerRow = new TableRow(this);
        addTableCell(headerRow, "Batch ID");
        addTableCell(headerRow, "Batch Name");
        tableLayout.addView(headerRow);

        // Add data rows
        for (int i = 0; i < contentArray.length(); i++) {
            try {
                JSONObject item = contentArray.getJSONObject(i);
                String batchId = item.optString("id", "N/A"); // Use optString with default value
                String batchName = item.optString("batchName", "N/A"); // Use optString with default value

                TableRow row = new TableRow(this);
                addTableCell(row, batchId);
                addTableCell(row, batchName);
                tableLayout.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addTableCell(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(10, 10, 10, 10);
        row.addView(textView);
    }
}
