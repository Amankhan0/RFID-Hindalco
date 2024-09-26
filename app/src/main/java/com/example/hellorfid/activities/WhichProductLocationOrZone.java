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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WhichProductLocationOrZone extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    private static final int REQUEST_CODE_SCAN_LOCATION = 1002;
    private static final int REQUEST_CODE_SCAN_PROCESS_INVENTORY = 1003;

    private String orderData;
    private String productIndex;
    private ApiCallBackWithToken apiCallBackWithToken;
    private Spinner spinnerZones;
    private Spinner spinnerLocations;
    private TextView locationLabel;
    private JSONArray contentArray;
    private Button btnNext;
    private Button btnScanLocation;
    private TextView tvLocationDetails;
    private String quantity;
    private CommanModel commanModel;
    private boolean isZoneSelected = false;
    private boolean isLocationSelected = false;
    private SessionManager sessionManager;
    private String selectedZoneId;
    private String selectedLocation;

    private String LocationIdAPI;
    private String ZoneIDAPI;
    private String BuildingIdsAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_which_product_location_or_zone);
        initializeViews();
        try {
            quantity = sessionManager.getProductData().getString("quantity");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setupListeners();
    }

    private void initializeViews() {
        spinnerZones = findViewById(R.id.spinnerZones);
        spinnerLocations = findViewById(R.id.spinnerLocations);
        locationLabel = findViewById(R.id.locationLabel);
        btnNext = findViewById(R.id.btnNext);
        btnScanLocation = findViewById(R.id.btnScanLocation);
        tvLocationDetails = findViewById(R.id.tvLocationDetails);
        apiCallBackWithToken = new ApiCallBackWithToken(this);
        sessionManager = new SessionManager(this);
    }

    private void setupListeners() {
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(v -> handleNextButtonClick());
        btnScanLocation.setOnClickListener(v -> openScanLocationActivity());
        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(v -> navigateBack());
    }


    private void handleNextButtonClick() {
        sessionManager.setSetScanCount(quantity.toString());
        sessionManager.setCheckTagOn(Constants.INVENTORY);
        Intent intent = new Intent(WhichProductLocationOrZone.this, MainActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_PROCESS_INVENTORY);
        Toast.makeText(this, "Next button clicked", Toast.LENGTH_SHORT).show();
    }

    private void openScanLocationActivity() {
        sessionManager.setCheckTagOn(Constants.LOCATION);
        sessionManager.setSetScanCount("1");
        Intent intent = new Intent(WhichProductLocationOrZone.this, MainActivity.class);
        intent.putExtra("mode", "scanLocation");
        intent.putExtra("totalInventory", 1);
        startActivityForResult(intent, REQUEST_CODE_SCAN_LOCATION);
    }

    private void navigateBack() {
        Intent intent = new Intent(WhichProductLocationOrZone.this, HandheldTerminalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_LOCATION) {
            System.out.println("Auto location called");
            sessionManager.setLocationData(data.getStringExtra("result_key").toString());
            btnNext.setEnabled(true);
        } else if (requestCode == REQUEST_CODE_SCAN_PROCESS_INVENTORY) {
            System.out.println("Auto location called");
            try {
                ProcessTags(resultCode, data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void ProcessTags(int resultCode, Intent data) throws JSONException, InterruptedException {
        String result = data.getStringExtra("result_key");
        System.out.println("TagING+DATA"+result);
        JSONArray jsonArray = new JSONArray(result);

        for(int i =0; i<jsonArray.length();i++){
//            System.out.println("sessionManager.getProductData()"+sessionManager.getProductData().toString());
            JSONObject item = jsonArray.getJSONObject(i);
            item.put("tagType",Constants.INVENTORY);
            item.put("currentLocation",sessionManager.getBuildingId());
            item.put("locationIds",sessionManager.getLocationData().getJSONObject(0).getString("locationIds"));
            item.put("zoneIds",sessionManager.getLocationData().getJSONObject(0).getString("zoneIds"));
            item.put("buildingId",sessionManager.getBuildingId());
            item.put("orderId",sessionManager.getOrderData().getString("_id"));
            item.put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            item.put("dispatchFrom",sessionManager.getOrderData().getString("dispatchFrom"));
            item.put("orderStatus",sessionManager.getOptionSelected().equals(Constants.OUTBOUND_ORDER)?Constants.ORDER_PICKED:Constants.ORDER_RECEIVED);
            item.put("dispatchTo",sessionManager.getOrderData().getString("dispatchTo"));
            item.put("movementStatus",Constants.IN_BUILDING);
            item.put("status",Constants.EMPTY);
            item.put("batchNumber",sessionManager.getOrderData().has("batchNumber")?sessionManager.getOrderData().getString("batchNumber"):"NA");
            item.put("product_id",sessionManager.getProductData().getJSONObject("productId").getString("_id"));
            item.put("orderId",sessionManager.getOrderData().getString("_id"));
            item.put("createdBy",sessionManager.getUserId());
            item.put("updatedBy",sessionManager.getUserId());
            System.out.println("item "+item.toString());
        }

        JSONObject res = Helper.commanHitApi(apiCallBackWithToken,Constants.addBulkTags,jsonArray);
        System.out.println("Tag added to system "+res);

    }





}