package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManagerBag;

public class AddBatchActivity extends AppCompatActivity implements ApiCallBackWithToken.ApiCallback {

    private static final String TAG = "AddBagActivity";
    private static final Logger log = Logger.getLogger(AddBatchActivity.class);
    private Spinner buildingSpinner;
    private EditText lotNumberEditText;
    private EditText batchNumberEditText;
    private EditText bagQuantityEditText;
    private Button submitButton;

    private AutoCompleteTextView productAutoCompleteTextView;
    private List<String> productList = new ArrayList<>();


    private ApiCallBackWithToken apiCallBack;

    private SessionManager sessionManager;  // For token
    private SessionManagerBag sessionManagerBag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bag);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);  // For token
        sessionManagerBag = new SessionManagerBag(this);

        // Initialize views
        buildingSpinner = findViewById(R.id.buildingspinner);
        lotNumberEditText = findViewById(R.id.lotnum);
        batchNumberEditText = findViewById(R.id.batchnum);
        bagQuantityEditText = findViewById(R.id.numofbag);
        submitButton = findViewById(R.id.addbad_btn);

        ImageView allScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBatchActivity.this, HandheldTerminalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Set up building spinner
        ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter.createFromResource(this,
                R.array.addbagbuilding, android.R.layout.simple_spinner_item);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        // Initialize product AutoCompleteTextView
        productAutoCompleteTextView = findViewById(R.id.product_autocomplete);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, productList);
        productAutoCompleteTextView.setAdapter(productAdapter);

        productAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String token = sessionManager.getToken();
                System.out.println("token--4rsf-sdf" + token);
                if (s.length() >= 1) { // Start searching when there's at least 1 character
                    JSONObject json = new JSONObject();
                    try {
                        json.put("page", 1);
                        json.put("limit", 10);
                        JSONObject searchObj = new JSONObject();
                        searchObj.put("productName", s.toString());
                        json.put("search", searchObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    apiCallBack = new ApiCallBackWithToken(AddBatchActivity.this);
                    String url = "helper/api/searchProduct";

                    apiCallBack.Api(url, json, AddBatchActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        // Set up submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    @Override
    public JSONObject onSuccess(JSONObject responseJson) {
        String result = responseJson.toString();

        // Log the successful response
        Log.d(TAG, "Request successful. Response: " + result);

        System.out.println("responseJson-----"+responseJson);

        // Handle successful response
        runOnUiThread(() -> {
            try {
                // Parse the response JSON
                JSONArray contentArray = responseJson.getJSONArray("content");

                // Clear the existing product list
                productList.clear();

                System.out.println("contentArray---" + contentArray);

                // Populate the product list with new suggestions
                for (int i = 0; i < contentArray.length(); i++) {
                    JSONObject product = contentArray.getJSONObject(i);
                    String productName = product.getString("productName");
                    productList.add(productName);
                    System.out.println("productName---" + productName);
                }

                // Update the adapter with the new product list
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, productList);
                productAutoCompleteTextView.setAdapter(adapter);

                // Show the dropdown
                productAutoCompleteTextView.showDropDown();

                Log.d(TAG, "Adapter updated with new data");

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AddBatchActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(AddBatchActivity.this, "Request Successful", Toast.LENGTH_SHORT).show();
        });
        return responseJson;
    }

    @Override
    public void onFailure(Exception e) {
        // Log the error
        Log.e(TAG, "Request failed. Error: " + e.getMessage(), e);

        // Handle failure response
        runOnUiThread(() -> {
            Toast.makeText(AddBatchActivity.this, "Request Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void submitForm() {
        String selectedBuilding = buildingSpinner.getSelectedItem().toString();
        String lotNumber = lotNumberEditText.getText().toString();
        String batchNumber = batchNumberEditText.getText().toString();
        String bagQuantity = bagQuantityEditText.getText().toString();
        String searchProduct = productAutoCompleteTextView.getText().toString();

        // Validate inputs
        if (selectedBuilding.isEmpty() || lotNumber.isEmpty() ||
                batchNumber.isEmpty() || bagQuantity.isEmpty() || searchProduct.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

            // Store values using SessionManager
        sessionManagerBag.clearSession();
        sessionManagerBag.setProductName(searchProduct);
        sessionManagerBag.setBuilding(selectedBuilding);
        sessionManagerBag.setLotNumber(lotNumber);
        sessionManagerBag.setBatchNumber(batchNumber);
        sessionManagerBag.setBagQuantity(bagQuantity);

        // Debugging values
        Log.d(TAG, "Search Product: " + searchProduct);
        Log.d(TAG, "Selected Building: " + selectedBuilding);
        Log.d(TAG, "Lot Number: " + lotNumber);
        Log.d(TAG, "Batch Number: " + batchNumber);
        Log.d(TAG, "Bag Quantity: " + bagQuantity);



        // Move to ScanPickingActivity
        Intent intent = new Intent(AddBatchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();

    }
}


