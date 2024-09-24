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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_product_acording_to_orders);



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
            Gson gson = new Gson();
            commanModel = gson.fromJson(commanModelJson, CommanModel.class);
        }


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

    private View createProductCard(final int index, final JSONObject productData) throws JSONException {
        View cardView = LayoutInflater.from(this).inflate(R.layout.order_acording_product_list, productContainer, false);

        JSONObject productId = productData.getJSONObject("productId");
        final int quantity = productData.getInt("quantity");
        final String productName = productId.getString("productName");

        TextView productNameTextView = cardView.findViewById(R.id.productNameTextView);
        TextView quantityTextView = cardView.findViewById(R.id.quantityTextView);

        productNameTextView.setText("Product Name: " + productName);
        quantityTextView.setText("Quantity: " + quantity);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                productIndex = index;

                System.out.println("Product Index: " + index);
                System.out.println("Product Name: " + productName);
                System.out.println("Quantity: " + quantity);
                Intent intent = new Intent(LoadProductAcordingToOrdersActivity.this, MainActivity.class);
                intent.putExtra("totalInventory", quantity);
                startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
            }
        });

        return cardView;
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
                if (resultCode == RESULT_OK && data != null) {
                    String result = data.getStringExtra("result_key");

                    JSONObject jsonObject = new JSONObject(orderData);
                    JSONArray productIds = jsonObject.getJSONArray("productIds");

                    if (result != null) {
                        String finalJson = Helper.commanParser(result, false, commanModel,this,productIds);
                        System.out.println("finalJson----->>>" + finalJson);

                        JSONObject res = Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, finalJson);
                        System.out.println("final json received --- " + res);
                        if (res.getInt("status") == 200) {

                            try {
                                if (productIndex >= 0 && productIndex < productIds.length()) {
                                    JSONObject product = productIds.getJSONObject(productIndex);

                                    // Extract the _id from the nested productId object
                                    String productId = product.getJSONObject("productId").getString("_id");

                                    // Create a new JSONObject with the desired structure
                                    JSONObject updatedProduct = new JSONObject();
                                    updatedProduct.put("productId", productId);
                                    updatedProduct.put("quantity", product.getInt("quantity"));
                                    updatedProduct.put("status", "ORDER_PICKED");

                                    // Replace the old product object with the updated one
                                    productIds.put(productIndex, updatedProduct);

                                    // Update the productIds array in the main object
                                    jsonObject.put("productIds", productIds);

                                    System.out.println("jsonObject: " + jsonObject);


                                    JSONArray updateProductIds = jsonObject.getJSONArray("productIds");

                                    // Track whether all products are picked
                                    boolean allPicked = true;

                                    // Loop through the productIds array to check their status
                                    for (int i = 0; i < updateProductIds.length(); i++) {
                                        JSONObject newproduct = productIds.getJSONObject(i);

                                        // Check if the status of the product is "ORDER_PICKED"
                                        if (!"ORDER_PICKED".equals(newproduct.getString("status"))) {
                                            allPicked = false; // If any product is not picked, set flag to false
                                            break; // No need to continue checking, exit the loop
                                        }
                                    }

                                    // If all products are picked, update the orderStatus
                                    if (allPicked) {
                                        System.out.println("call ---- call");
                                        jsonObject.put("orderStatus", "ORDER_PICKED");
                                    }

                                    System.out.println("jsonObject" + jsonObject);

                                    // Convert the updated JSONObject back to a string
                                    apiCallBackWithToken.Api(Constants.updateOrder, jsonObject, new ApiCallBackWithToken.ApiCallback() {
                                        @Override
                                        public JSONObject onSuccess(JSONObject responseJson) {

                                            System.out.println("responseJson------" + responseJson);
                                            return responseJson;
                                        }
                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.e("TAG", "API call failed", e);
                                            runOnUiThread(() -> Toast.makeText(LoadProductAcordingToOrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show());
                                        }
                                    });
                                } else {
                                    System.out.println("Invalid productIndex: " + productIndex);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("Error parsing or updating JSON: " + e.getMessage());
                            }
                            System.out.println("res ---- " + orderData);
                            System.out.println("res ---- " + productIndex);
                        }
                        Toast.makeText(this, "Order processing completed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("OrderActivity", "Received null result from MainActivity");
                        Toast.makeText(this, "Error: Received null result from MainActivity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("OrderActivity", "JSONException in onActivityResult", e);
            Toast.makeText(this, "Error processing result: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Log.e("OrderActivity", "InterruptedException in onActivityResult", e);
            Toast.makeText(this, "Operation interrupted: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OrderActivity", "Unexpected error in onActivityResult", e);
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}