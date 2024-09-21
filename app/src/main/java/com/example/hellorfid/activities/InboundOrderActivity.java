//package com.example.hellorfid.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.hellorfid.R;
//import com.example.hellorfid.adapter.OrderAdapter;
//import com.example.hellorfid.constants.Constants;
//import com.example.hellorfid.constants.Helper;
//import com.example.hellorfid.dump.ApiCallBackWithToken;
//import com.example.hellorfid.model.CommanModel;
//import com.example.hellorfid.model.OrderModel;
//import com.example.hellorfid.reader.MainActivity;
//import com.example.hellorfid.session.SessionManager;
//import com.google.gson.JsonParser;
//
//import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class InboundOrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {
//
//    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
//    private static final Logger log = Logger.getLogger(InboundOrderActivity.class);
//    private SessionManager sessionManager;
//    private ApiCallBackWithToken apiCallBackWithToken;
//    private List<OrderModel> orderList;
//    private OrderAdapter orderAdapter;
//    private RecyclerView recyclerView;
//    CommanModel commanModel;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_order);
//
//        sessionManager = new SessionManager(this);
//        apiCallBackWithToken = new ApiCallBackWithToken(this);
//
//        orderList = new ArrayList<>();
//        orderAdapter = new OrderAdapter(this, orderList, this);
//
//        recyclerView = findViewById(R.id.recyclerViewOrders);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(orderAdapter);
//
//        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
//        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InboundOrderActivity.this, HandheldTerminalActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        hitApiAndLogResult();
//    }
//
//    @Override
//    public void onOrderClick(OrderModel order) throws JSONException, InterruptedException {
//        System.out.println("Order clicked: " + order.getId());
//        commanModel.setOrderStatus(Constants.ORDER_RECEIVING);
//        JSONObject res = Helper.commanUpdate(apiCallBackWithToken,Constants.updateOrder,
//                "_id",commanModel.getOrderId(),"orderStatus",Constants.ORDER_RECEIVING);
//
//        System.out.println("updated order--->" + res);
//
//        if(res.getInt("status")==200) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("totalInventory", order.getQty());
//            intent.putExtra("apiUrl", Constants.addBulkTags);
//            startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
//                if (resultCode == RESULT_OK && data != null) {
//                    String result = data.getStringExtra("result_key");
//                    if (result != null) {
//                        String finalJson = Helper.commanParser(result, false, commanModel);
//                        System.out.println("<<<-----finalJson----->>>" + finalJson);
//                        JSONObject res = Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, finalJson);
//                        System.out.println("final json recevied" + res);
//
//                        if (res == null) {
//                            System.out.println("call eles");
//                            JSONObject resultAgain = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,
//                                    "_id", commanModel.getOrderId(), "orderStatus", Constants.ORDER_INITIATED);
//                            System.out.println("resultAgain--" + resultAgain);
//                        } else {
//                            if (res.getInt("status") == 200) {
//                                JSONObject updatedOrderResult = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,
//                                        "_id", commanModel.getOrderId(), "orderStatus", Constants.ORDER_RECEIVED);
//                                System.out.println("updatedOrderResult order--->" + updatedOrderResult);
//                            }
//                        }
//
//                        hitApiAndLogResult(); // Refresh the order list
//                    } else {
//                        Log.e("InboundOrderActivity", "Received null result from MainActivity");
//                        Toast.makeText(this, "Error: Received null result from MainActivity", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (resultCode == RESULT_CANCELED) {
//                    System.out.println("result cancelled");
//                    JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,
//                            "_id", commanModel.getOrderId(), "orderStatus", Constants.ORDER_INITIATED);
//                    System.out.println("-----updated order--->" + res);
//                    Toast.makeText(this, "Operation cancelled", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (JSONException e) {
//            Log.e("InboundOrderActivity", "JSONException in onActivityResult", e);
//            Toast.makeText(this, "Error processing result: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        } catch (InterruptedException e) {
//            Log.e("InboundOrderActivity", "InterruptedException in onActivityResult", e);
//            Toast.makeText(this, "Operation interrupted: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e("InboundOrderActivity", "Unexpected error in onActivityResult", e);
//            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void hitApiAndLogResult() {
//        try {
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("page", "1");
//            requestBody.put("limit", "5");
//
//            JSONObject search = new JSONObject();
//            search.put("orderStatus", "ORDER_INITIATED");
//            search.put("orderType", "INBOUND");
//            requestBody.put("search", search);
//
//            System.out.println("requestBody" + requestBody);
//            String apiEndpoint = Constants.searchOrders;
//
//            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
//                @Override
//                public JSONObject onSuccess(JSONObject responseJson) {
//                    System.out.println("responseJson------"+responseJson);
//                    parseAndDisplayOrder(responseJson);
//                    return responseJson;
//                }
//                @Override
//                public void onFailure(Exception e) {
//                    Log.e("TAG", "API call failed", e);
//                    runOnUiThread(() -> Toast.makeText(InboundOrderActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show());
//                }
//            });
//
//        } catch (JSONException e) {
//            Log.e("TAG", "Error creating JSON request body", e);
//        }
//    }
//
//    private void parseAndDisplayOrder(JSONObject responseJson) {
//        try {
//            JSONArray content = responseJson.getJSONArray("content");
//            orderList.clear();
//            for (int i = 0; i < content.length(); i++) {
//                JSONObject orderJson = content.getJSONObject(i);
//                OrderModel order = new OrderModel();
//                commanModel = new CommanModel();
//
//                order.setId(orderJson.optString("_id"));
//                order.setOrderDateTime(parseDate(orderJson.optString("orderDateTime")));
//                order.setExpectedArrival(parseDate(orderJson.optString("expectedArrival")));
//                order.setSaleType(orderJson.optString("saleType"));
//                order.setOrderType(orderJson.optString("orderType"));
//                order.setOrderStatus(orderJson.optString("orderStatus"));
//                order.setCurrentLocation(orderJson.optString("currentLocation"));
//                order.setDispatchFrom(orderJson.optString("dispatchFrom"));
//                order.setDispatchTo(orderJson.optString("dispatchTo"));
//                order.setDispatchFromType(orderJson.optString("dispatchFromType"));
//                order.setDispatchToType(orderJson.optString("dispatchToType"));
//                order.setBillTo(orderJson.optString("billTo"));
//                order.setPickBy(orderJson.optString("pickBy"));
//                order.setReaderId(orderJson.optString("readerId"));
//                order.setQty(orderJson.optInt("qty"));
//                order.setBatchNumber(orderJson.optString("batchId"));
//                order.setMovementStatus(orderJson.optString("movementStatus"));
//                order.setStatus(orderJson.optString("status"));
//                order.setError(orderJson.optBoolean("isError"));
//                order.setErrorMsg(orderJson.optString("errorMsg"));
//                order.setCreatedBy(orderJson.optString("createdBy"));
//                order.setUpdatedBy(orderJson.optString("updatedBy"));
//                order.setCreatedAt(orderJson.optLong("createdAt"));
//                order.setUpdatedAt(orderJson.optLong("updatedAt"));
//                order.setTagAdded(orderJson.optBoolean("isTagAdded"));
//
//
//                commanModel.setOrderId(order.getId());
//                commanModel.setBatchID(order.getBatchNumber());
//                commanModel.setBillTo(order.getBillTo());
//                commanModel.setCurrentLocation(order.getCurrentLocation());
//                commanModel.setDispatchFrom(order.getDispatchFrom());
//                commanModel.setDispatchTo(order.getDispatchTo());
//                commanModel.setOrderStatus(order.getOrderStatus());
//                commanModel.setMovementStatus(order.getMovementStatus());
//
//                // Parse vehicle IDs
//                JSONArray vehicleIdsArray = orderJson.optJSONArray("vehicleIds");
//                if (vehicleIdsArray != null) {
//                    List<OrderModel.VehicleId> vehicleIds = new ArrayList<>();
//                    for (int j = 0; j < vehicleIdsArray.length(); j++) {
//                        JSONObject vehicleIdJson = vehicleIdsArray.getJSONObject(j);
//                        OrderModel.VehicleId vehicleId = new OrderModel.VehicleId(vehicleIdJson.optString("vehicleId"));
//                        vehicleIds.add(vehicleId);
//                    }
//                    order.setVehicleIds(vehicleIds);
//                }
//
//                // Parse product information
//                JSONArray productIdsArray = orderJson.optJSONArray("productIds");
//                if (productIdsArray != null) {
//                    List<OrderModel.ProductOrder> productOrders = new ArrayList<>();
//                    for (int j = 0; j < productIdsArray.length(); j++) {
//                        JSONObject productOrderJson = productIdsArray.getJSONObject(j);
//                        JSONObject productJson = productOrderJson.optJSONObject("productId");
//                        if (productJson != null) {
//                            OrderModel.Product product = new OrderModel.Product();
//                            product.setId(productJson.optString("id"));
//                            product.setProductName(productJson.optString("productName"));
//                            product.setProductCode(productJson.optString("productCode"));
//                            product.setQuantity(productJson.optInt("quantity"));
//                            product.setProductDescription(productJson.optString("productDescription"));
//                            product.setProductGroup(productJson.optString("productGroup"));
//                            product.setHeight(productJson.optDouble("height"));
//                            product.setWidth(productJson.optDouble("width"));
//                            product.setLength(productJson.optDouble("length"));
//                            product.setPackedWeight(productJson.optDouble("packedWeight"));
//                            product.setWeight(productJson.optDouble("weight"));
//                            product.setBuyingCost(productJson.optDouble("buyingCost"));
//                            product.setSellingCost(productJson.optDouble("sellingCost"));
//                            product.setGrade(productJson.optString("grade"));
//
//                            OrderModel.ProductOrder productOrder = new OrderModel.ProductOrder(product, productOrderJson.optInt("quantity"));
//                            productOrders.add(productOrder);
//                        }
//                    }
//                    order.setProductIds(productOrders);
//                }
//
//                orderList.add(order);
//            }
//
//            runOnUiThread(() -> {
//                orderAdapter.notifyDataSetChanged();
//                if (orderList.isEmpty()) {
//                    Toast.makeText(InboundOrderActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (JSONException e) {
//            Log.e("TAG", "Error parsing JSON response", e);
//            runOnUiThread(() -> Toast.makeText(InboundOrderActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show());
//        }
//    }
//
//    private Date parseDate(String dateString) {
//        if (dateString == null || dateString.isEmpty()) {
//            return null;
//        }
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
//            return format.parse(dateString);
//        } catch (ParseException e) {
//            Log.e("TAG", "Error parsing date: " + dateString, e);
//            return null;
//        }
//    }
//}

package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hellorfid.R;
import com.example.hellorfid.adapter.OrderAdapter;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;
import com.example.hellorfid.model.OrderModel;
import com.example.hellorfid.session.SessionManager;
import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InboundOrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    private static final Logger log = Logger.getLogger(InboundOrderActivity.class);
    private SessionManager sessionManager;
    private ApiCallBackWithToken apiCallBackWithToken;
    private List<OrderModel> orderList;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;
    CommanModel commanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        sessionManager = new SessionManager(this);
        apiCallBackWithToken = new ApiCallBackWithToken(this);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList, this);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboundOrderActivity.this, HandheldTerminalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        hitApiAndLogResult();
    }

    @Override
    public void onOrderClick(OrderModel order) throws JSONException, InterruptedException {
        System.out.println("order" + order);
        System.out.println("Order clicked: " + order.getId());
        commanModel.setOrderStatus(Constants.ORDER_RECEIVING);
        JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,
                "_id", commanModel.getOrderId(), "orderStatus", Constants.ORDER_RECEIVING);

        System.out.println("Inbound updated order--->" + res);

        if (res.getInt("status") == 200) {
            Gson gson = new Gson();
            Gson commonGson = new Gson();
            String orderJson = gson.toJson(order);
            String commanModelJson = commonGson.toJson(commanModel);
            Intent intent = new Intent(this, LoadProductAcordingToOrdersActivity.class);
            intent.putExtra("orderData", orderJson);
            intent.putExtra("commonModal", commanModelJson);
            startActivity(intent);
        }
    }

    private void hitApiAndLogResult() {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("page", "1");
            requestBody.put("limit", "20");

            JSONObject search = new JSONObject();
            JSONArray statusArray = new JSONArray(new String[]{"ORDER_INITIATED", "ORDER_RECEIVING"});
            search.put("orderStatus", statusArray);
            search.put("orderType", "INBOUND");
            requestBody.put("search", search);

            System.out.println("requestBody" + requestBody);
            String apiEndpoint = Constants.searchOrders;

            apiCallBackWithToken.Api(apiEndpoint, requestBody, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    System.out.println("responseJson------" + responseJson);
                    parseAndDisplayOrder(responseJson);
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("TAG", "API call failed", e);
                    runOnUiThread(() -> Toast.makeText(InboundOrderActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show());
                }
            });

        } catch (JSONException e) {
            Log.e("TAG", "Error creating JSON request body", e);
        }
    }

    private void parseAndDisplayOrder(JSONObject responseJson) {
        try {
            JSONArray content = responseJson.getJSONArray("content");
            orderList.clear();
            for (int i = 0; i < content.length(); i++) {
                JSONObject orderJson = content.getJSONObject(i);
                OrderModel order = new OrderModel();
                commanModel = new CommanModel();

                order.setId(orderJson.optString("_id"));
                order.setOrderDateTime(parseDate(orderJson.optString("orderDateTime")));
                order.setExpectedArrival(parseDate(orderJson.optString("expectedArrival")));
                order.setSaleType(orderJson.optString("saleType"));
                order.setOrderType(orderJson.optString("orderType"));
                order.setOrderStatus(orderJson.optString("orderStatus"));
                order.setCurrentLocation(orderJson.optString("currentLocation"));
                order.setDispatchFrom(orderJson.optString("dispatchFrom"));
                order.setDispatchTo(orderJson.optString("dispatchTo"));
                order.setDispatchFromType(orderJson.optString("dispatchFromType"));
                order.setDispatchToType(orderJson.optString("dispatchToType"));
                order.setBillTo(orderJson.optString("billTo"));
                order.setPickBy(orderJson.optString("pickBy"));
                order.setReaderId(orderJson.optString("readerId"));
                order.setQty(orderJson.optInt("qty"));
                order.setBatchNumber(orderJson.optString("batchNumber"));
                order.setMovementStatus(orderJson.optString("movementStatus"));
                order.setStatus(orderJson.optString("status"));
                order.setError(orderJson.optBoolean("isError"));
                order.setErrorMsg(orderJson.optString("errorMsg"));
                order.setCreatedBy(orderJson.optString("createdBy"));
                order.setUpdatedBy(orderJson.optString("updatedBy"));
                order.setCreatedAt(orderJson.optLong("createdAt"));
                order.setUpdatedAt(orderJson.optLong("updatedAt"));
                order.setTagAdded(orderJson.optBoolean("isTagAdded"));

                commanModel.setOrderId(order.getId());
                commanModel.setBatchID(order.getBatchNumber());
                commanModel.setBillTo(order.getBillTo());
                commanModel.setCurrentLocation(order.getCurrentLocation());
                commanModel.setDispatchFrom(order.getDispatchFrom());
                commanModel.setDispatchTo(order.getDispatchTo());
                commanModel.setOrderStatus(order.getOrderStatus());
                commanModel.setMovementStatus(order.getMovementStatus());

                // Parse vehicle IDs
                JSONArray vehicleIdsArray = orderJson.optJSONArray("vehicleIds");
                if (vehicleIdsArray != null) {
                    List<OrderModel.VehicleId> vehicleIds = new ArrayList<>();
                    for (int j = 0; j < vehicleIdsArray.length(); j++) {
                        JSONObject vehicleIdJson = vehicleIdsArray.getJSONObject(j);
                        OrderModel.VehicleId vehicleId = new OrderModel.VehicleId(vehicleIdJson.optString("vehicleId"));
                        vehicleIds.add(vehicleId);
                    }
                    order.setVehicleIds(vehicleIds);
                }

                // Parse product information
                JSONArray productIdsArray = orderJson.optJSONArray("productIds");
                if (productIdsArray != null) {
                    List<OrderModel.ProductOrder> productOrders = new ArrayList<>();
                    for (int j = 0; j < productIdsArray.length(); j++) {
                        JSONObject productOrderJson = productIdsArray.getJSONObject(j);
                        JSONObject productJson = productOrderJson.optJSONObject("productId");
                        if (productJson != null) {
                            OrderModel.Product product = new OrderModel.Product();
                            product.setId(productJson.optString("_id"));
                            product.setProductName(productJson.optString("productName"));
                            product.setProductCode(productJson.optString("productCode"));
                            product.setQuantity(productJson.optInt("quantity"));
                            product.setProductDescription(productJson.optString("productDescription"));
                            product.setProductGroup(productJson.optString("productGroup"));
                            product.setHeight(productJson.optDouble("height"));
                            product.setWidth(productJson.optDouble("width"));
                            product.setLength(productJson.optDouble("length"));
                            product.setPackedWeight(productJson.optDouble("packedWeight"));
                            product.setWeight(productJson.optDouble("weight"));
                            product.setBuyingCost(productJson.optDouble("buyingCost"));
                            product.setSellingCost(productJson.optDouble("sellingCost"));
                            product.setGrade(productJson.optString("grade"));

                            OrderModel.ProductOrder productOrder = new OrderModel.ProductOrder(product, productOrderJson.optInt("quantity"));
                            productOrders.add(productOrder);
                        }
                    }
                    order.setProductIds(productOrders);
                }

                orderList.add(order);
            }

            runOnUiThread(() -> {
                orderAdapter.notifyDataSetChanged();
                if (orderList.isEmpty()) {
                    Toast.makeText(InboundOrderActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.e("TAG", "Error parsing JSON response", e);
            runOnUiThread(() -> Toast.makeText(InboundOrderActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show());
        }
    }

    private Date parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e("TAG", "Error parsing date: " + dateString, e);
            return null;
        }
    }
}