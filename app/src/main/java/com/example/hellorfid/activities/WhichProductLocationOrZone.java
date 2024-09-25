package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.utils.CommanModelHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WhichProductLocationOrZone extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;

    private String orderData;
    private String productIndex;
    private ApiCallBackWithToken apiCallBackWithToken;
    private Spinner spinnerZones;
    private Spinner spinnerLocations;
    private TextView locationLabel;
    private JSONArray contentArray;
    private Button btnNext;
    private String quantity;
    private CommanModel commanModel;
    private boolean isZoneSelected = false;
    private boolean isLocationSelected = false;
    private SessionManager sessionManager;
    private String selectedZoneId;
    private String selectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_which_product_location_or_zone);

        spinnerZones = findViewById(R.id.spinnerZones);
        spinnerLocations = findViewById(R.id.spinnerLocations);
        locationLabel = findViewById(R.id.locationLabel);
        btnNext = findViewById(R.id.btnNext);
        apiCallBackWithToken = new ApiCallBackWithToken(this);
        sessionManager = new SessionManager(this);
        // Initially hide the location spinner and its label
        spinnerLocations.setVisibility(View.GONE);
        locationLabel.setVisibility(View.GONE);

        orderData = getIntent().getStringExtra("orderData");
        productIndex = getIntent().getStringExtra("selectedProdutIndex");
        quantity = getIntent().getStringExtra("quantity");

        commanModel = CommanModelHolder.getInstance().getCommanModel();




        System.out.println("orderData --- >>>>" + orderData);
        System.out.println("productIndex --- >>>>" + productIndex);

        // Set up the Next button
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(v -> {
            int quantityInt = Integer.parseInt(quantity);
            Intent intent = new Intent(WhichProductLocationOrZone.this, MainActivity.class);
            intent.putExtra("totalInventory", quantityInt);
            startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);

            System.out.println("orderData >>---"+orderData);
            System.out.println("productIndex >>---"+productIndex);

            Toast.makeText(this, "Next button clicked", Toast.LENGTH_SHORT).show();
        });

        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WhichProductLocationOrZone.this, HandheldTerminalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        if (orderData == null || productIndex == null) {
            Toast.makeText(this, "Order data or product index missing", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(orderData);
            JSONArray productArr = jsonObject.getJSONArray("productIds");
            int index = Integer.parseInt(productIndex);
            JSONObject productObject = productArr.getJSONObject(index);
            JSONObject innerProductObject = productObject.getJSONObject("productId");

            String id = innerProductObject.getString("_id");

            hitApi(Constants.searchZone);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse order data", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateSpinnerWithZones(JSONArray contentArray) {
        try {
            this.contentArray = contentArray;
            List<String> zoneList = new ArrayList<>();
            zoneList.add("Choose Zone");

            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject zoneObject = contentArray.getJSONObject(i);
                String zoneValue = zoneObject.getString("value");
                zoneList.add(zoneValue);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, zoneList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerZones.setAdapter(adapter);

            spinnerZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (position == 0) {
                        System.out.println("No zone selected.");
                        isZoneSelected = false;
                        spinnerLocations.setVisibility(View.GONE);
                        locationLabel.setVisibility(View.GONE);
                    } else {
                        isZoneSelected = true;
                        try {
                            JSONObject selectedZoneObject = contentArray.getJSONObject(position - 1);
                            System.out.println("Selected zone object: " + selectedZoneObject.toString());

                            selectedZoneId = selectedZoneObject.getString("_id");

                            showLocationLoading();
                            hitSearchLocationApi(selectedZoneId);

                            spinnerLocations.setVisibility(View.VISIBLE);
                            locationLabel.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("Error getting zone object: " + e.getMessage());
                        }
                    }
                    updateNextButtonState();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    isZoneSelected = false;
                    updateNextButtonState();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load zones", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLocationLoading() {
        List<String> loadingList = new ArrayList<>();
        loadingList.add("Loading locations...");
        ArrayAdapter<String> loadingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loadingList);
        loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(loadingAdapter);
        spinnerLocations.setEnabled(false);
    }

    private void hitApi(String endPoint) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("page", "1");
        requestBody.put("limit", "20");
        requestBody.put("search", new JSONObject());

        apiCallBackWithToken.Api(endPoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                runOnUiThread(() -> {
                    System.out.println("responseJson---" + responseJson);
                    try {
                        JSONArray contentArray = responseJson.getJSONArray("content");
                        populateSpinnerWithZones(contentArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Mapping", "API call failed", e);
                runOnUiThread(() -> Toast.makeText(WhichProductLocationOrZone.this, "Failed to load items", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void hitSearchLocationApi(String selectedZoneId) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", 1);
            requestBody.put("limit", 5);
            JSONObject search = new JSONObject();
            JSONArray zoneIds = new JSONArray();
            zoneIds.put(selectedZoneId);
            search.put("zoneIds", zoneIds);
            requestBody.put("search", search);

            apiCallBackWithToken.Api(Constants.searchLocation, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    runOnUiThread(() -> {
                        System.out.println("Search Location API Response: " + responseJson.toString());
                        try {
                            populateLocationsSpinner(responseJson.getJSONArray("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("Error parsing location response: " + e.getMessage());
                        }
                    });
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("Mapping", "Search Location API call failed", e);
                    runOnUiThread(() -> {
                        Toast.makeText(WhichProductLocationOrZone.this, "Failed to search locations", Toast.LENGTH_SHORT).show();
                        // Reset location spinner on failure
                        List<String> errorList = new ArrayList<>();
                        errorList.add("Error loading locations");
                        ArrayAdapter<String> errorAdapter = new ArrayAdapter<>(WhichProductLocationOrZone.this, android.R.layout.simple_spinner_item, errorList);
                        errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLocations.setAdapter(errorAdapter);
                        spinnerLocations.setEnabled(true);
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error creating request body for search location: " + e.getMessage());
        }
    }

    private void populateLocationsSpinner(JSONArray locationsArray) {
        try {
            List<String> locationList = new ArrayList<>();
            List<JSONObject> locationObjects = new ArrayList<>();
            locationList.add("Choose Location");
            locationObjects.add(null);  // Add a null object for the "Choose Location" option

            for (int i = 0; i < locationsArray.length(); i++) {
                JSONObject locationObject = locationsArray.getJSONObject(i);
                String locationValue = locationObject.getString("value");
                locationList.add(locationValue);
                locationObjects.add(locationObject);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLocations.setAdapter(adapter);
            spinnerLocations.setEnabled(true);

            spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (position == 0) {
                        System.out.println("No location selected.");
                        isLocationSelected = false;
                    } else {
                        isLocationSelected = true;
//                        selectedLocation = locationList.get(position);

                        JSONObject selectedObject = locationObjects.get(position);


                        try {
                            selectedLocation = selectedObject.getString("_id");
                            System.out.println("selectedLocation"+selectedLocation);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // Handle the selected location as needed
                    }
                    updateNextButtonState();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    isLocationSelected = false;
                    updateNextButtonState();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error populating locations spinner: " + e.getMessage());
        }
    }

    private void updateNextButtonState() {
        btnNext.setEnabled(isZoneSelected && isLocationSelected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                String result = data.getStringExtra("result_key");

                try {
                    JSONObject jsonObject = new JSONObject(orderData);

                    System.out.println("jsonObject" + jsonObject);

                    JSONArray productIds = jsonObject.getJSONArray("productIds");

                    if (result != null) {
                        String finalJson = Helper.commanParser(result, false, commanModel, this, productIds);
                        System.out.println("finalJson----->>>" + finalJson);

                        JSONObject res = Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, finalJson);
                        System.out.println("final json received --- " + res);
                        if (res.getInt("status") == 200) {

                            if (jsonObject.has("productIds")) {
                                JSONArray productArr = jsonObject.getJSONArray("productIds");
                                JSONArray newProductIds = new JSONArray();

                                for (int i = 0; i < productArr.length(); i++) {
                                    JSONObject productObject = productArr.getJSONObject(i);
                                    System.out.println("productObject" + productObject);
                                    // Get the productId object directly
                                    JSONObject innerProductObject = productObject.getJSONObject("productId");

                                    // Extract the fields you need
                                    String id = innerProductObject.getString("_id");
                                    int quantity = productObject.getInt("quantity");
                                    String status = productObject.getString("status");

                                    // Create a new JSON object with the extracted fields
                                    JSONObject newProductObject = new JSONObject();
                                    newProductObject.put("productId", id);
                                    newProductObject.put("quantity", quantity);
                                    int index = Integer.parseInt(productIndex);
                                    newProductObject.put("status", index == i ? "ORDER_PICKED" : status);
                                    newProductObject.put("zoneIds", selectedZoneId);
                                    newProductObject.put("locationIds", selectedLocation);
                                    newProductObject.put("buildingIds", sessionManager.getBuildingId());
                                    // Add the new product object to your array
                                    newProductIds.put(newProductObject);
                                }

                                jsonObject.put("productIds", newProductIds);

                                boolean allPicked = true;
                                for (int i = 0; i < newProductIds.length(); i++) {
                                    System.out.println("newProductIds" + newProductIds);


                                    JSONObject productObject = newProductIds.getJSONObject(i);

                                    System.out.println("productObject === " + productObject);

                                    String status = productObject.getString("status");

                                    if (!"ORDER_PICKED".equals(status)) {
                                        allPicked = false; // If any product is not picked, set flag to false
                                        break; // No need to continue checking, exit the loop
                                    }
                                }

                                if (jsonObject.has("vehicleIds")) {
                                    JSONArray vehicleIds = jsonObject.getJSONArray("vehicleIds");
                                    JSONArray newVehicleIds = new JSONArray();

                                    for (int i = 0; i < vehicleIds.length(); i++) {
                                        JSONObject vehicleObject = vehicleIds.getJSONObject(i);
                                        String vehicleIdString = vehicleObject.getString("vehicleId");
                                        JSONObject innerVehicleObject = new JSONObject(vehicleIdString);
                                        String id = innerVehicleObject.getString("_id");

                                        JSONObject newVehicleObject = new JSONObject();
                                        newVehicleObject.put("vehicleId", id);
                                        newVehicleIds.put(newVehicleObject);
                                    }
                                    jsonObject.put("vehicleIds", newVehicleIds);
                                }

                                if (allPicked) {
                                    System.out.println("call ---- call");
                                    jsonObject.put("orderStatus", "ORDER_PICKED");
                                }



                                System.out.println("jsonObject veupdate ---- " + jsonObject);


                                apiCallBackWithToken.Api(Constants.updateOrder, jsonObject, new ApiCallBackWithToken.ApiCallback() {
                                    @Override
                                    public JSONObject onSuccess(JSONObject responseJson) {
                                        System.out.println("responseJson------" + responseJson);
                                        Intent intent = new Intent(WhichProductLocationOrZone.this, HandheldTerminalActivity.class);
                                        startActivity(intent);
                                        return responseJson;
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.e("TAG", "API call failed", e);
                                        runOnUiThread(() -> Toast.makeText(WhichProductLocationOrZone.this, "Failed to load orders", Toast.LENGTH_SHORT).show());
                                    }
                                });

                            }
                        }
                        Toast.makeText(this, "Order processing completed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("OrderActivity", "Received null result from MainActivity");
                        Toast.makeText(this, "Error: Received null result from MainActivity", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error parsing or updating JSON: " + e.getMessage());
                    Toast.makeText(this, "Error processing order data", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}