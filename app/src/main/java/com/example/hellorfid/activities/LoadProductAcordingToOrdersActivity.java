package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.utils.CommanModelHolder;
import com.example.hellorfid.utils.JwtDecoder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadProductAcordingToOrdersActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;

    private String orderData;
    private LinearLayout productContainer;
    private TextView noDataTextView;
    private ApiCallBackWithToken apiCallBackWithToken;
    private CommanModel commanModel;
    private int productIndex;
    private SessionManager sessionManager;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_product_acording_to_orders);

        sessionManager = new SessionManager(this);

        JSONObject decodedToken = null;
        try {
            decodedToken = JwtDecoder.decoded(sessionManager.getToken());
            userId = decodedToken.getString("userId");  // Assuming 'sub' claim contains the user ID
            System.out.println("userId"+userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("decodedToken"+decodedToken);

        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, OrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        orderData = getIntent().getStringExtra("orderData");
        String commanModelJson = getIntent().getStringExtra("commonModal");


        if (commanModelJson != null) {
            System.out.println("commanModelJson --- "+commanModelJson);
            Gson gson = new Gson();
            commanModel = gson.fromJson(commanModelJson, CommanModel.class);
            CommanModelHolder.getInstance().setCommanModel(commanModel);
        }

        System.out.println("orderData---"+orderData);

        productContainer = findViewById(R.id.productContainer);
        noDataTextView = findViewById(R.id.noDataTextView);
        apiCallBackWithToken = new ApiCallBackWithToken(this);

        try {
            populateProducts();
        } catch (JSONException e) {
            e.printStackTrace();
            showNoDataMessage();
        }
    }

    private void populateProducts() throws JSONException {
        if (orderData == null || orderData.isEmpty()) {
            showNoDataMessage();
            return;
        }

        JSONObject orderJson = new JSONObject(orderData);
        JSONArray productIds = orderJson.getJSONArray("productIds");

        if (productIds.length() == 0) {
            showNoDataMessage();
            return;
        }

        for (int i = 0; i < productIds.length(); i++) {
            JSONObject productData = productIds.getJSONObject(i);
            View productCard = createProductCard(i, productData);
            productContainer.addView(productCard);
        }
    }

    private void showNoDataMessage() {
        productContainer.setVisibility(View.GONE);
        noDataTextView.setVisibility(View.VISIBLE);
    }

//    private View createProductCard(final int index, final JSONObject productData) throws JSONException {
//        View cardView = LayoutInflater.from(this).inflate(R.layout.order_acording_product_list, productContainer, false);
//
//        JSONObject productId = productData.getJSONObject("productId");
//        final int quantity = productData.getInt("quantity");
//        final String productName = productId.getString("productName");
//
//        TextView productNameTextView = cardView.findViewById(R.id.productNameTextView);
//        TextView quantityTextView = cardView.findViewById(R.id.quantityTextView);
//
//        productNameTextView.setText("Product Name: " + productName);
//        quantityTextView.setText("Quantity: " + quantity);
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                productIndex = index;
//
//                System.out.println("Product Index: " + index);
//                System.out.println("Product Name: " + productName);
//                System.out.println("Quantity: " + quantity);
//                Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, MainActivity.class);
//                intent.putExtra("totalInventory", quantity);
//                startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
//            }
//        });
//
//        return cardView;
//    }

    private View createProductCard(final int index, final JSONObject productData) throws JSONException {
        View cardView = LayoutInflater.from(this).inflate(R.layout.order_acording_product_list, productContainer, false);

        JSONObject productId = productData.getJSONObject("productId");
        final int quantity = productData.getInt("quantity");
        final String productName = productId.getString("productName");
//        final String status = productId.getString("status");

        // Check product status and update the background color if it's "ORDER_PICKED"
        String productStatus = productData.optString("status", "ORDER_INITIATED"); // Default to "ORDER_INITIATED" if status is missing
        if ("ORDER_PICKED".equals(productStatus)) {
            cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Set background to green
        } else {
            cardView.setBackgroundColor(getResources().getColor(android.R.color.white)); // Default background to white
        }

        TextView productNameTextView = cardView.findViewById(R.id.productNameTextView);
        TextView quantityTextView = cardView.findViewById(R.id.quantityTextView);
        TextView statusTextView = cardView.findViewById(R.id.status);

        productNameTextView.setText("Product Name: " + productName);
        quantityTextView.setText("Quantity: " + quantity);
        statusTextView.setText("Status: " + productStatus);

        if(!"ORDER_PICKED".equals(productStatus)){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("orderData ==="+orderData);
                    System.out.println("index ===="+index);
                    productIndex = index;

                    if(orderData != null && index != -1){
                        Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, WhichProductLocationOrZone.class);
                        intent.putExtra("orderData", orderData);
                        intent.putExtra("selectedProdutIndex", String.valueOf(index) );
                        intent.putExtra("quantity", String.valueOf(quantity));
                        startActivity(intent);
                    }


//                    productIndex = index;
//                    Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, MainActivity.class);
//                    intent.putExtra("totalInventory", quantity);
//                    startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
                }
            });
        }
        return cardView;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
//            if (resultCode == RESULT_OK && data != null) {
//                String result = data.getStringExtra("result_key");
//
//                try {
//                    JSONObject jsonObject = new JSONObject(orderData);
//
//                    System.out.println("jsonObject" + jsonObject);
//
//                    JSONArray productIds = jsonObject.getJSONArray("productIds");
//
//                    if (result != null) {
//                        String finalJson = Helper.commanParser(result, false, commanModel, this, productIds);
//                        System.out.println("finalJson----->>>" + finalJson);
//
//                        JSONObject res = Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, finalJson);
//                        System.out.println("final json received --- " + res);
//                        if (res.getInt("status") == 200) {
//
//                            if (jsonObject.has("productIds")) {
//                                JSONArray productArr = jsonObject.getJSONArray("productIds");
//                                JSONArray newProductIds = new JSONArray();
//
//                                for (int i = 0; i < productArr.length(); i++) {
//                                    JSONObject productObject = productArr.getJSONObject(i);
//                                    System.out.println("productObject" + productObject);
//                                    // Get the productId object directly
//                                    JSONObject innerProductObject = productObject.getJSONObject("productId");
//
//                                    // Extract the fields you need
//                                    String id = innerProductObject.getString("_id");
//                                    int quantity = productObject.getInt("quantity");
//                                    String status = productObject.getString("status");
//
//                                    // Create a new JSON object with the extracted fields
//                                    JSONObject newProductObject = new JSONObject();
//                                    newProductObject.put("productId", id);
//                                    newProductObject.put("quantity", quantity);
//                                    newProductObject.put("status", productIndex == i ? "ORDER_PICKED" : status);
//
//                                    // Add the new product object to your array
//                                    newProductIds.put(newProductObject);
//                                }
//
//                                jsonObject.put("productIds", newProductIds);
//
//                                boolean allPicked = true;
//                                for (int i = 0; i < newProductIds.length(); i++) {
//                                    System.out.println("newProductIds" + newProductIds);
//
//
//                                    JSONObject productObject = newProductIds.getJSONObject(i);
//
//                                    System.out.println("productObject === " + productObject);
//
//                                    String status = productObject.getString("status");
//
//                                    if (!"ORDER_PICKED".equals(status)) {
//                                        allPicked = false; // If any product is not picked, set flag to false
//                                        break; // No need to continue checking, exit the loop
//                                    }
//                                }
//
//                                if (jsonObject.has("vehicleIds")) {
//                                    JSONArray vehicleIds = jsonObject.getJSONArray("vehicleIds");
//                                    JSONArray newVehicleIds = new JSONArray();
//
//                                    for (int i = 0; i < vehicleIds.length(); i++) {
//                                        JSONObject vehicleObject = vehicleIds.getJSONObject(i);
//                                        String vehicleIdString = vehicleObject.getString("vehicleId");
//                                        JSONObject innerVehicleObject = new JSONObject(vehicleIdString);
//                                        String id = innerVehicleObject.getString("_id");
//
//                                        JSONObject newVehicleObject = new JSONObject();
//                                        newVehicleObject.put("vehicleId", id);
//                                        newVehicleIds.put(newVehicleObject);
//                                    }
//                                    jsonObject.put("vehicleIds", newVehicleIds);
//                                }
//
//                                if (allPicked) {
//                                    System.out.println("call ---- call");
//                                    jsonObject.put("orderStatus", "ORDER_PICKED");
//                                }
//
//                                System.out.println("jsonObject veupdate ---- " + jsonObject);
//
//
//                                apiCallBackWithToken.Api(Constants.updateOrder, jsonObject, new ApiCallBackWithToken.ApiCallback() {
//                                    @Override
//                                    public JSONObject onSuccess(JSONObject responseJson) {
//                                        System.out.println("responseJson------" + responseJson);
//                                        Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, HandheldTerminalActivity.class);
//                                        startActivity(intent);
//                                        return responseJson;
//                                    }
//
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        Log.e("TAG", "API call failed", e);
//                                        runOnUiThread(() -> Toast.makeText(LoadProductAcordingToOrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show());
//                                    }
//                                });
//
//                            }
//                        }
//                        Toast.makeText(this, "Order processing completed", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Log.e("OrderActivity", "Received null result from MainActivity");
//                        Toast.makeText(this, "Error: Received null result from MainActivity", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.println("Error parsing or updating JSON: " + e.getMessage());
//                    Toast.makeText(this, "Error processing order data", Toast.LENGTH_SHORT).show();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
}