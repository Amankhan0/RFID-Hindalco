
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
            try {
                selectedItemInfo.put("buildingIds", BuildingId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            System.out.println("<-----selectedItemInfo----->>>"+selectedItemInfo);
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
        if (endPoint.equals(Constants.searchDevice)) {
            // Special handling for weighing machine search

            searchObject.put("deviceType", "weighing_scale");  // Keep the backend type as is
            requestBody.put("search", searchObject);
        } else if (endPoint.equals(Constants.searchNozzle)) {
            requestBody.put("search", new JSONObject());
            endPoint = "device/api/searchDeviceProfile";
        } else {
            JSONObject buildingObject = new JSONObject();
            buildingObject.put("buildingIds", sessionManager.getBuildingId());
            requestBody.put("search", endPoint.equals(Constants.searchVehicle) ? new JSONObject() : buildingObject);
        }

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
                        System.out.println("deviceID---"+ deviceId);
                        // Check if this is an antenna
                        if (name.startsWith("antennas.")) {
                            System.out.println("Found antenna: " + name + " with value: " + value + " userEntry: " + userEntry);

                            // Create a new JSONObject for the item
                            JSONObject antennaItem = new JSONObject();
                            antennaItem.put("_id", device.getString("_id"));
                            antennaItem.put("deviceName", deviceName);
                            antennaItem.put("deviceId", deviceId);
                            antennaItem.put("value", value);
                            antennaItem.put("userEntry", userEntry);
                            antennaItem.put("antennaNumber", name);
                            antennaItem.put("deviceId", deviceId);

                            // Create display name: deviceName + value + userEntry
                            StringBuilder displayName = new StringBuilder(deviceName);

                            // Add value if it exists
                            if (!value.isEmpty()) {
                                displayName.append(" - ").append(value);
                            }

                            // Add userEntry if it exists
                            if (!userEntry.isEmpty()) {
                                displayName.append(" (").append(userEntry).append(")");
                            }

                            itemNames.add(displayName.toString());
                            itemData.add(antennaItem);
                            System.out.println("Added item: " + displayName);
                        }
                    }
                }
            }
        }



        // Log the final processed data
        System.out.println("\nTotal processed items: " + itemNames.size());
        for (int i = 0; i < itemNames.size(); i++) {
            System.out.println("Item " + (i + 1) + ": " + itemNames.get(i));
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

    private JSONObject generateRequestBody(String tagType, String itemInfo, String fieldName) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("rfidTag", tagId); // Use the tagId received from MainActivity
        json.put("tagInfo", new JSONObject(itemInfo).getString(fieldName));
        json.put("tagType", tagType);
        return json;
    }

    private void updateApiHit(String endPoint, JSONObject requestBody) {

        System.out.println("requestBody"+requestBody);

        apiCallBackWithToken.Api(endPoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                runOnUiThread(() -> {
                    System.out.println("come------"+responseJson);
                });
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Mapping", "API call failed", e);
                runOnUiThread(() -> Toast.makeText(Mapping.this, "Failed to load items", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                System.out.println("data-- -- -" + data);
                String result = data.getStringExtra("result_key");

                if (result != null && result.startsWith("[") && result.endsWith("]")) {
                    tagId = result.substring(1, result.length() - 1);
                    System.out.println("Extracted tagId: " + tagId);

                    // Now execute the API call with the received tagId
                    try {
                        JSONObject body = generateRequestBody(selectedEndpointInfo.tagType, selectedItemInfo.toString(), selectedEndpointInfo.fieldName);

                        System.out.println("selectedItemInfo.toString()"+selectedItemInfo.toString());
                        System.out.println("body" + body);
                        System.out.println("addTagJson"+Constants.addTagJson);
                        JSONObject newBody = new JSONObject(Constants.addTagJson);
                        System.out.println("jsonObject"+newBody);


                            newBody.put("createdBy",userId);
                        if(selectedEndpointInfo.tagType=="Vehicle"){
                            newBody.put("tagPlacement", selectedItemInfo.get("_id"));
                            newBody.put("rfidTag", body.get("rfidTag"));
                            newBody.put("tagInfo", body.get("tagInfo"));
                            newBody.put("tagType", body.get("tagType"));
                            newBody.put("status", "Active");
                        }else if(selectedEndpointInfo.tagType=="Zone"){

                            JSONArray buildingIds = selectedItemInfo.getJSONArray("buildingIds");
                            if (buildingIds.length() > 0) {
                                newBody.put("currentLocation", buildingIds.get(0));
                                newBody.put("buildingId", buildingIds.get(0));
                            } else {
                                System.out.println("---buildingIds--- No building IDs available");
                            }
                            newBody.put("operationStatus", "NA");
                            newBody.put("tagPlacement", selectedItemInfo.get("_id"));
                            newBody.put("rfidTag", body.get("rfidTag"));
                            newBody.put("tagInfo", body.get("tagInfo"));
                            newBody.put("tagType", body.get("tagType"));
                            newBody.put("status", "Active");
                        }
                        else if(selectedEndpointInfo.tagType=="Location"){
                            System.out.println("clickeddddd");
                            JSONArray buildingIds = selectedItemInfo.getJSONArray("buildingIds");
                            if (buildingIds.length() > 0) {
                                newBody.put("currentLocation", buildingIds.get(0));
                                newBody.put("buildingId", buildingIds.get(0));
                            } else {
                                System.out.println("---buildingIds--- No building IDs available");
                            }
                            newBody.put("tagPlacement", selectedItemInfo.get("_id"));
                            newBody.put("rfidTag", body.get("rfidTag"));
                            newBody.put("tagInfo", body.get("tagInfo"));
                            newBody.put("tagType", body.get("tagType"));
                            newBody.put("status", "Active");
                        } else if (selectedEndpointInfo.tagType == "Nozzle") {

                            JSONArray buildingIds = selectedItemInfo.getJSONArray("buildingIds");
                            if (buildingIds.length() > 0) {
                                newBody.put("currentLocation", buildingIds.get(0));
                                newBody.put("buildingId", buildingIds.get(0));
                            }
                            newBody.put("tagPlacement", selectedItemInfo.get("_id"));
                            newBody.put("rfidTag", body.get("rfidTag"));
                            newBody.put("tagInfo", body.get("tagInfo"));
                            newBody.put("tagType", body.get("tagType"));
                            newBody.put("status", "Active");

                            System.out.println("selectedEndpointInfo----"+ selectedEndpointInfo);
                        }else if (selectedEndpointInfo.tagType == "WeighingMachine") {
                            // Add handling for Weighing Machine
                            JSONArray buildingIds = selectedItemInfo.getJSONArray("buildingIds");
                            if (buildingIds.length() > 0) {
                                newBody.put("currentLocation", buildingIds.get(0));
                                newBody.put("buildingId", buildingIds.get(0));
                            }
                            newBody.put("tagPlacement", selectedItemInfo.get("_id"));
                            newBody.put("rfidTag", body.get("rfidTag"));
                            newBody.put("tagInfo", body.get("tagInfo"));
                            newBody.put("tagType", body.get("tagType"));
                            newBody.put("status", "Active");
                        }


                        System.out.println("updated newBody"+newBody);

                        apiCallBackWithToken.Api(Constants.addTag, newBody, new ApiCallBackWithToken.ApiCallback() {
                            @Override
                            public JSONObject onSuccess(JSONObject responseJson) {

                                try {
                                    // Check if status is 201
                                    int status = responseJson.getInt("status");
                                    if (status == 201) {
                                        // Extract the _id from responseJson
                                        String newTagId = responseJson.getJSONObject("data").getString("_id");

                                        // Create a new JSONObject for selectedItemInfo with only tagIds and _id
                                        JSONObject updatedSelectedItemInfo = new JSONObject();
                                        updatedSelectedItemInfo.put("tagIds", newTagId);
                                        updatedSelectedItemInfo.put("_id", selectedItemInfo.getString("_id"));

                                        System.out.println("Updated selectedItemInfo with new tagId: " + updatedSelectedItemInfo);
                                        // Run on UI thread to show success message

                                        System.out.println("selectedEndpointInfo.tagType"+selectedEndpointInfo.tagType);

                                        if (selectedEndpointInfo.tagType == "Vehicle") {
                                            updateApiHit(Constants.updateVehicle, updatedSelectedItemInfo);
                                        } else if (selectedEndpointInfo.tagType == "Zone") {
                                            updateApiHit(Constants.updateZone, updatedSelectedItemInfo);
                                        } else if (selectedEndpointInfo.tagType == "Location") {
                                            updateApiHit(Constants.updateLocation, updatedSelectedItemInfo);
                                        } else if (selectedEndpointInfo.tagType == "WeighingMachine") {
                                            updateApiHit(Constants.updateDevice, updatedSelectedItemInfo);
                                        }


                                        runOnUiThread(() -> {
                                            Toast.makeText(Mapping.this, "Tag added successfully", Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        // If status is not 201, show an error toast
                                        runOnUiThread(() -> {
                                            Toast.makeText(Mapping.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Mapping", "Failed to parse responseJson", e);

                                    // Show error toast in case of JSON parsing error
                                    runOnUiThread(() -> {
                                        Toast.makeText(Mapping.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    });
                                }
                                return responseJson;
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("Mapping", "API call failed", e);
                                runOnUiThread(() -> Toast.makeText(Mapping.this, "Failed to add tag", Toast.LENGTH_SHORT).show());
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("Mapping", "Error generating request body", e);
                        Toast.makeText(Mapping.this, "Error generating request body", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Invalid tag ID format");
                    Toast.makeText(Mapping.this, "Invalid tag ID format", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("result cancelled");
                Toast.makeText(Mapping.this, "Tag reading cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void processWeighingMachines(JSONArray content) throws JSONException {
        for (int i = 0; i < content.length(); i++) {
            JSONObject device = content.getJSONObject(i);
            String deviceName = device.optString("deviceName", "Unknown Device");
            String deviceUserIdentify = device.optString("deviceUserIdentify", "");
            String model = device.optString("model", "");

            StringBuilder displayName = new StringBuilder(deviceName);
            if (!deviceUserIdentify.isEmpty()) {
                displayName.append(" (").append(deviceUserIdentify).append(")");
            }
            if (!model.isEmpty()) {
                displayName.append(" - ").append(model);
            }

            itemNames.add(displayName.toString());
            itemData.add(device);
        }
    }
}