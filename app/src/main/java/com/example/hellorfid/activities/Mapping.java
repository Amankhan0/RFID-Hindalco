
package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.StoryHandler;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.utils.JwtDecoder;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mapping extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    private static final Logger log = Logger.getLogger(Mapping.class);
    private Spinner spinnerOptions;
    private String tagId;
    private ApiCallBackWithToken apiCallBackWithToken;
    private ListView itemListView;
    private List<String> itemNames = new ArrayList<>();
    private List<JSONObject> itemData = new ArrayList<>();
    private String currentEndpoint;
    private JSONObject selectedItemInfo;
    private EndpointInfo selectedEndpointInfo;
    private SessionManager sessionManager;
    public String userId;
public String BuildingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapping);

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        spinnerOptions = findViewById(R.id.spinner_options);
        itemListView = findViewById(R.id.item_list_view);
        sessionManager = new SessionManager(this);

        BuildingId = sessionManager.getBuildingId();

        System.out.println("BuildingId----"+ BuildingId);
        JSONObject decodedToken = null;
        try {
            decodedToken = JwtDecoder.decoded(sessionManager.getToken());
            userId = decodedToken.getString("userId");  // Assuming 'sub' claim contains the user ID
            System.out.println("userId"+userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("decodedToken"+decodedToken);

        // Now you can access claims from the decoded token



        System.out.println("session === "+sessionManager.getToken());

        setupBackButton(allScreenBackBtn);
        setupSpinner();
        setupItemListView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupBackButton(ImageView allScreenBackBtn) {
        allScreenBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Mapping.this, HandheldTerminalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(adapter);

        int defaultSelection = adapter.getPosition("Choose an option");
        spinnerOptions.setSelection(defaultSelection);

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (!selectedOption.equals("Choose an option")) {
                    Toast.makeText(Mapping.this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
                    currentEndpoint = getEndpointForOption(selectedOption);
                    sessionManager.setCheckTagOn(selectedOption.toUpperCase());
                    if (currentEndpoint != null) {
                        try {
                            hitApi(currentEndpoint);
                        } catch (JSONException e) {
                            Log.e("Mapping", "Error hitting API", e);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private String getEndpointForOption(String option) {
        switch (option) {
            case "Vehicle":
                return Constants.searchVehicle;
            case "Zone":
                return Constants.searchZone;
            case "Location":
                return Constants.searchLocation;
            case "Weighing Machine":
                return Constants.searchDevice;
            case "Nozzle":
                return Constants.searchNozzle;
            // Add more cases for other options
            default:
                return null;
        }
    }

    private class EndpointInfo {
        String fieldName;
        String tagType;

        EndpointInfo(String fieldName, String tagType) {
            this.fieldName = fieldName;
            this.tagType = tagType;
        }
    }

    private EndpointInfo getEndpointInfo(String endpoint) {
        if (endpoint.equals(Constants.searchVehicle)) {
            return new EndpointInfo("vehicleNumber", "Vehicle");
        } else if (endpoint.equals(Constants.searchZone)) {
            return new EndpointInfo("value", "Zone");
        } else if (endpoint.equals(Constants.searchLocation)) {
            return new EndpointInfo("value", "Location");
        } else if (endpoint.equals(Constants.searchNozzle)) {
            return new EndpointInfo("nozzleNumber", "Nozzle");
        } else if (endpoint.equals(Constants.searchDevice)) {
            return new EndpointInfo("deviceName", "WeighingMachine");  // Updated tag type
        }
        return new EndpointInfo("unknown", "Unknown");
    }

    private void setupItemListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedItemInfo = itemData.get(position);
            System.out.println("Selected item data: " + selectedItemInfo.toString());


            System.out.println("---selectedOption----" + sessionManager.getOptionSelected());
            try {
                StoryHandler.mappingVehicle(sessionManager,this,selectedItemInfo.toString(),sessionManager.getOptionSelected(), "lbalblallbaallab");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


//            System.out.println("Selected item data: " + selectedItemInfo.toString());
//            selectedEndpointInfo = getEndpointInfo(currentEndpoint);
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("totalInventory", 1);
//            startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
        });
    }

    private void hitApi(String endPoint) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("page", "1");
        requestBody.put("limit", "100");

        JSONObject searchObject = new JSONObject();

        System.out.println("searchObject-->>>"+requestBody);
        if (endPoint.equals(Constants.searchDevice)) {
            // Special handling for weighing machine search

            searchObject.put("deviceType", "weighing_scale");  // Keep the backend type as is
            requestBody.put("search", searchObject);
        } else if (endPoint.equals(Constants.searchNozzle)) {

            JSONObject object = new JSONObject();
            object.put("placement", sessionManager.getBuildingId());
            object.put("capabilities.userEntry","BAGGING_MANUAL");
            requestBody.put("search", object);
            System.out.println("object--->>"+requestBody );

//            endPoint = "device/api/searchDeviceProfile";
        } else {
            JSONObject buildingObject = new JSONObject();
            buildingObject.put("buildingIds", sessionManager.getBuildingId());
            requestBody.put("search", endPoint.equals(Constants.searchVehicle) ? new JSONObject() : buildingObject);
        }

        System.out.println("sessionManager.getBuildingId()"+sessionManager.getBuildingId());
        System.out.println("requestBody ----  " + requestBody);
        System.out.println("endPoint ----  " + endPoint);

        apiCallBackWithToken.Api(endPoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("responseJson----" + responseJson);

                runOnUiThread(() -> {
                    try {
                        JSONArray content = responseJson.getJSONArray("content");
                        System.out.println("content----" + content);
                        itemNames.clear();
                        itemData.clear();

                        // Special handling for nozzle data
                        if (currentEndpoint.equals(Constants.searchNozzle)) {
                            processNozzleDevices(content);
                        } else {
                            // Regular processing for other endpoints
                            for (int i = 0; i < content.length(); i++) {
                                JSONObject item = content.getJSONObject(i);
                                String itemName = getItemName(item, currentEndpoint);
                                itemNames.add(itemName);
                                itemData.add(item);
                            }
                        }
                        updateItemList();
                    } catch (JSONException e) {
                        Log.e("Mapping", "Error parsing JSON", e);
                    }
                });
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("API call failed------" + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Mapping.this, "Failed to load items", Toast.LENGTH_SHORT).show());
            }
        });
    }
    private void processNozzleDevices(JSONArray content) throws JSONException {
        System.out.println("content--->>>" + content);
        for (int i = 0; i < content.length(); i++) {
            JSONObject device = content.getJSONObject(i);

            // Check if device has a deviceName
            String deviceName = device.optString("deviceName");
            if (deviceName != null && !deviceName.isEmpty()) {
                System.out.println("\nProcessing Device: " + deviceName);

                // Get capabilities array
                JSONArray capabilities = device.optJSONArray("capabilities");
                if (capabilities != null) {
                    System.out.println("Capabilities count: " + capabilities.length());

                    // Process each capability
                    for (int j = 0; j < capabilities.length(); j++) {
                        JSONObject capability = capabilities.getJSONObject(j);
                        String name = capability.optString("name", "");
                        String value = capability.optString("value", "");
                        String userEntry = capability.optString("userEntry", "");
                        String deviceId = device.getString("deviceId");

                        // Only process capabilities with userEntry = "BAGGING_MANUAL"
                        if ("BAGGING_MANUAL".equals(userEntry)) {
                            System.out.println("Found BAGGING_MANUAL antenna: " + name + " with value: " + value);

                            // Create a new JSONObject for the item
                            JSONObject antennaItem = new JSONObject();
                            antennaItem.put("_id", device.getString("_id"));
                            antennaItem.put("deviceName", deviceName);
                            antennaItem.put("deviceId", deviceId);
                            antennaItem.put("value", value);
                            antennaItem.put("userEntry", userEntry);
//                            antennaItem.put("antennaNumber", name);

                            System.out.println("antennaItem----"+antennaItem);

                            // Create display name: deviceName + value + userEntry
                            StringBuilder displayName = new StringBuilder(deviceName);

                            // Add value if it exists
                            if (!value.isEmpty()) {
                                displayName.append(" - ").append(value);
                            }

                            // Add userEntry if it exists
                            displayName.append(" (BAGGING_MANUAL)");

                            itemNames.add(displayName.toString());
                            itemData.add(antennaItem);
                            System.out.println("Added item: " + displayName);
                        }
                    }
                }
            }
        }
    }

    private String getItemName(JSONObject item, String endpoint) throws JSONException {
        EndpointInfo endpointInfo = getEndpointInfo(endpoint);
        if (endpoint.equals(Constants.searchNozzle)) {
            // Special handling for antenna names
            String deviceName = item.optString("deviceName", "Unknown Device");
            String value = item.optString("value", "");
            String userEntry = item.optString("userEntry", "");

            StringBuilder displayName = new StringBuilder(deviceName);
            if (!value.isEmpty()) {
                displayName.append(" - ").append(value);
            }
            if (!userEntry.isEmpty()) {
                displayName.append(" (").append(userEntry).append(")");
            }
            return displayName.toString();
        } else if (item.has(endpointInfo.fieldName)) {
            return item.getString(endpointInfo.fieldName);
        }
        return "Unknown Item";
    }

    private void updateItemList() {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) itemListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

//    private JSONObject generateRequestBody(String tagType, String itemInfo, String fieldName) throws JSONException {
//        JSONObject json = new JSONObject();
//        json.put("rfidTag", tagId); // Use the tagId received from MainActivity
//        json.put("tagInfo", new JSONObject(itemInfo).getString(fieldName));
//        json.put("tagType", tagType);
//        return json;
//    }

//    private void updateApiHit(String endPoint, JSONObject requestBody) {
//
//        System.out.println("requestBody"+requestBody);
//
//        apiCallBackWithToken.Api(endPoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
//            @Override
//            public JSONObject onSuccess(JSONObject responseJson) {
//                runOnUiThread(() -> {
//                    System.out.println("come------"+responseJson);
//                });
//                return responseJson;
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e("Mapping", "API call failed", e);
//                runOnUiThread(() -> Toast.makeText(Mapping.this, "Failed to load items", Toast.LENGTH_SHORT).show());
//            }
//        });
//    }

}