package com.example.hellorfid;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
// AddBagActivity.java
public class AddBagActivity extends AppCompatActivity implements ApiCallBackWithToken.ApiCallback {

    private static final String TAG = "AddBagActivity";
    private static final Logger log = Logger.getLogger(AddBagActivity.class);
    private Spinner buildingSpinner;
    private EditText lotNumberEditText;
    private EditText batchNumberEditText;
    private EditText bagQuantityEditText;
    private Button submitButton;

    private AutoCompleteTextView productAutoCompleteTextView;
    private List<String> productList = new ArrayList<>();
    private SessionManager sessionManager;
    private ApiCallBackWithToken apiCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bag);

        // Initialize views
        buildingSpinner = findViewById(R.id.buildingspinner);
        lotNumberEditText = findViewById(R.id.lotnum);
        batchNumberEditText = findViewById(R.id.batchnum);
        bagQuantityEditText = findViewById(R.id.numofbag);
        submitButton = findViewById(R.id.addbad_btn);
        sessionManager = new SessionManager(this);

        ImageView allScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBagActivity.this, HandheldTerminalActivity.class);
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

                    System.out.println("token------" + token);

                    apiCallBack = new ApiCallBackWithToken();
                    String url = "helper/api/searchProduct"; // Replace with your actual URL

                    System.out.println("json---" + json);

                    apiCallBack.Api(url, json, AddBagActivity.this, token); // Updated method call
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
    public void onSuccess(JSONObject responseJson) {
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
                Toast.makeText(AddBagActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(AddBagActivity.this, "Request Successful", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onFailure(Exception e) {
        // Log the error
        Log.e(TAG, "Request failed. Error: " + e.getMessage(), e);

        // Handle failure response
        runOnUiThread(() -> {
            Toast.makeText(AddBagActivity.this, "Request Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void submitForm() {
        String selectedBuilding = buildingSpinner.getSelectedItem().toString();
        String lotNumber = lotNumberEditText.getText().toString();
        String batchNumber = batchNumberEditText.getText().toString();
        String bagQuantity = bagQuantityEditText.getText().toString();

        // Validate inputs
        if (selectedBuilding.isEmpty() || lotNumber.isEmpty() ||
                batchNumber.isEmpty() || bagQuantity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Debugging values
        Log.d(TAG, "Selected Building: " + selectedBuilding);
        Log.d(TAG, "Lot Number: " + lotNumber);
        Log.d(TAG, "Batch Number: " + batchNumber);
        Log.d(TAG, "Bag Quantity: " + bagQuantity);

        // TODO: Process the form data (e.g., save to database, send to server, etc.)

        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();
    }
}
