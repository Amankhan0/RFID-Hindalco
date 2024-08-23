package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.hellorfid.R;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.session.SessionManager;

public class AddBatchFormActivity extends AppCompatActivity implements ApiCallBackWithToken.ApiCallback {

    private static final String TAG = "AddBatchFormActivity";
    private EditText lotNumberEditText;
    private EditText batchNumberEditText;
    private EditText bagQuantityEditText;
    private EditText batchNameEditText;
    private Button submitButton;
    private AutoCompleteTextView productAutoCompleteTextView;
    private List<String> productList = new ArrayList<>();
    private ApiCallBackWithToken apiCallBack;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_add_batch_form);

        sessionManager = new SessionManager(this);

        batchNameEditText = findViewById(R.id.batchName);
        lotNumberEditText = findViewById(R.id.lotnum);
        batchNumberEditText = findViewById(R.id.batchnum);
        bagQuantityEditText = findViewById(R.id.numofbag);
        submitButton = findViewById(R.id.addbad_btn);
        productAutoCompleteTextView = findViewById(R.id.product_autocomplete);

        ImageView allScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        allScreenBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddBatchFormActivity.this, HandheldTerminalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, productList);
        productAutoCompleteTextView.setAdapter(productAdapter);

        productAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    searchProduct(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submitButton.setOnClickListener(v -> {
            try {
                Log.d(TAG, "Submit button clicked");
                submitForm();
            } catch (Exception e) {
                Log.e(TAG, "Error in onClick", e);
            }
        });
    }

    private void searchProduct(String query) {
        String token = sessionManager.getToken();
        Log.d(TAG, "Searching product with token: " + token);
        JSONObject json = new JSONObject();
        try {
            json.put("page", 1);
            json.put("limit", 10);
            JSONObject searchObj = new JSONObject();
            searchObj.put("productName", query);
            json.put("search", searchObj);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON for product search", e);
            return;
        }
        apiCallBack = new ApiCallBackWithToken(this);
        String url = "helper/api/searchProduct";
        apiCallBack.Api(url, json, this);
    }

    private void submitForm() {
        Log.d(TAG, "submitForm method called");
        String batchName = batchNameEditText.getText().toString().trim();
        String productId = productAutoCompleteTextView.getText().toString().trim();
        String batchNumber = batchNumberEditText.getText().toString().trim();
        String totalInventory = bagQuantityEditText.getText().toString().trim();
        String buildingId = sessionManager.getBuildingId();
        String readerId = "handheldID";

        if (batchName.isEmpty() || productId.isEmpty() || batchNumber.isEmpty() ||
                totalInventory.isEmpty()) {
            Log.d(TAG, "Form validation failed: One or more fields are empty");
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();

            if (batchName.isEmpty()) batchNameEditText.setError("Batch Name is required");
            if (productId.isEmpty()) productAutoCompleteTextView.setError("Product is required");
            if (batchNumber.isEmpty()) batchNumberEditText.setError("Batch Number is required");
            if (totalInventory.isEmpty()) bagQuantityEditText.setError("Total Inventory is required");

            return;
        }

        Log.d(TAG, "Form data: " + batchName + ", " + productId + ", " + batchNumber + ", " + totalInventory + ", " + buildingId);

        JSONObject formData = new JSONObject();
        try {
            formData.put("batchName", batchName);
            formData.put("product_id", productId);
            formData.put("batchNumber", batchNumber);
            formData.put("status", "EMPTY");
            formData.put("movementStatus", "IN_TRANSIT");
            formData.put("totalInventory", Integer.parseInt(totalInventory));
            formData.put("buildingId", buildingId);
            formData.put("readerId", readerId);

            Log.d(TAG, "Form data JSON: " + formData.toString());

            // Call the new API endpoint
            String url = "iot/api/addBatch";
            apiCallBack = new ApiCallBackWithToken(this);
            apiCallBack.Api(url, formData, this);

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON from form data", e);
            Toast.makeText(this, "Error submitting form", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public JSONObject onSuccess(JSONObject responseJson) {
        Log.d(TAG, "API call successful. Response: " + responseJson.toString());
        runOnUiThread(() -> {
            try {
                // Check if the response is from the search product API
                if (responseJson.has("content")) {
                    JSONArray contentArray = responseJson.getJSONArray("content");
                    productList.clear();
                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject product = contentArray.getJSONObject(i);
                        String productNameid = product.getString("id");
                        productList.add(productNameid);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_dropdown_item_1line, productList);
                    productAutoCompleteTextView.setAdapter(adapter);
                    productAutoCompleteTextView.showDropDown();
                } else {
                    Intent intent = new Intent(AddBatchFormActivity.this, BatchActivity.class);
                    startActivity(intent);
                    // This is the response from the addBatch API
                    Toast.makeText(AddBatchFormActivity.this, "Batch added successfully", Toast.LENGTH_SHORT).show();
                    // Clear the form
                    clearForm();
                    // Optionally, navigate to another activity or refresh the current one
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing response", e);
                Toast.makeText(AddBatchFormActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
            }
        });
        return responseJson;
    }

    @Override
    public void onFailure(Exception e) {
        Log.e(TAG, "API call failed", e);
        runOnUiThread(() -> {
            Toast.makeText(AddBatchFormActivity.this, "Request Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void clearForm() {
        batchNameEditText.setText("");
        productAutoCompleteTextView.setText("");
        batchNumberEditText.setText("");
        bagQuantityEditText.setText("");
        lotNumberEditText.setText("");
    }
}