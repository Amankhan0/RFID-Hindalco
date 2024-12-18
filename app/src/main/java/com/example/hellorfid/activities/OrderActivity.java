package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hellorfid.R;
import com.example.hellorfid.adapter.OrderAdapter;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.constants.StoryHandler;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;
import com.example.hellorfid.model.OrderModel;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.widget.ProgressBar;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    private static final Logger log = Logger.getLogger(OrderActivity.class);
    private SessionManager sessionManager;
    private ApiCallBackWithToken apiCallBackWithToken;
    private List<OrderModel> orderList;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private TextView loadingText;
    private Button btnPrevious, btnNext;
    private TextView tvPageNumber;

    CommanModel commanModel;

    private int currentPage = 1;
    private static final int PAGE_SIZE = 5;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OrderActivity.onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        sessionManager = new SessionManager(this);
        apiCallBackWithToken = new ApiCallBackWithToken(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);


        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList, this);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvPageNumber = findViewById(R.id.tvPageNumber);

        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, HandheldTerminalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnPrevious.setOnClickListener(v -> loadPreviousPage());
        btnNext.setOnClickListener(v -> loadNextPage());

        showLoader();
        hitApiAndLogResult();
    }

    private void loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            showLoader();
            hitApiAndLogResult();
        }
    }

    private void loadNextPage() {
        if (!isLastPage) {
            currentPage++;
            showLoader();
            hitApiAndLogResult();
        }
    }

    private void updatePaginationButtons() {
//        btnPrevious.setEnabled(currentPage > 1);
        btnPrevious.setVisibility(currentPage > 1 ? View.VISIBLE : View.GONE);
        btnNext.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
//        btnNext.setEnabled(!isLastPage);
        tvPageNumber.setText("Page " + currentPage);
    }

    private void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
        loadingText.setText("Loading...");
        recyclerView.setVisibility(View.GONE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        updatePaginationButtons();
    }

    private void refreshData() {
//        currentPage = 1; // Reset to the first page when refreshing
//        isLastPage = false;
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        hitApiAndLogResult();
    }


    @Override
    public void onOrderClick(OrderModel order) throws JSONException, InterruptedException {
        if(sessionManager.getOptionSelected().equals(Constants.RECHECK)){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", order.getId());
            jsonObject.put("updatedOrderId", order.getId());
            jsonObject.put("orderStatus", Constants.DISPATCHED);
            jsonObject.put("operationStatus", Constants.RECHECKING);

            JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,jsonObject);

            System.out.println("updated order------->" +res);
            if(res!=null && res.has("data")){
                sessionManager.setOrderData(res.getString("data").toString());
                StoryHandler.orderRecheck(sessionManager, OrderActivity.this, res.getString("data").toString(), Constants.RECHECK);
                Intent intent = new Intent(this, MultiActionActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(OrderActivity.this, "Error in updating order", Toast.LENGTH_SHORT).show();
            }

        }else {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", order.getId());
            jsonObject.put("updatedOrderId", order.getId());
//             jsonObject.put("orderStatus", sessionManager.getOptionSelected().equals(Constants.INBOUND)?Constants.ORDER_RECEIVING:Constants.ORDER_PICKING);
//             jsonObject.put("operationStatus",  sessionManager.getOptionSelected().equals(Constants.INBOUND)?Constants.ORDER_RECEIVING:Constants.ORDER_PICKING);

            jsonObject.put("orderStatus", order.getOrderStatus());
            jsonObject.put("operationStatus",  order.getOrderStatus());


            System.out.println("jsonObject +Order" + jsonObject);
            JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,jsonObject);

            System.out.println("updated order--->" + res);
            if (res.getInt("status") == 200) {
                sessionManager.setOrderData(res.getString("data").toString());
                    Intent intent = new Intent(this, LoadProductAcordingToOrdersActivity.class);
                    startActivity(intent);
            }
        }
    }
    private void hitApiAndLogResult() {
        try {
            JSONObject s = new JSONObject();
            if(sessionManager.getOptionSelected().equals(Constants.INBOUND)){
                s.put("currentLocation", sessionManager.getBuildingId());
                s.put("orderType",Constants.INBOUND);
            } else if(sessionManager.getOptionSelected().equals(Constants.OUTBOUND)){
                s.put("currentLocation", sessionManager.getBuildingId());
                s.put("orderType",Constants.OUTBOUND);
            }else {
                s.put("currentLocation", sessionManager.getBuildingId());
                s.put("orderStatus",Constants.DISPATCHED);
                s.put("orderType",Constants.OUTBOUND);


            }

            // Modified to use currentPage and PAGE_SIZE
            JSONObject requestBody = Helper.getSearchJson(currentPage, PAGE_SIZE, s);
            JSONObject res = Helper.commanHitApi(apiCallBackWithToken,Constants.searchOrders,requestBody);
            System.out.println("<<---res--->" + res);
            if (res == null ) {


                runOnUiThread(() -> {
                    runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
                    hideLoader();
                    if (currentPage == 1) {

                        btnNext.setVisibility(View.GONE);
                        tvPageNumber.setVisibility(View.GONE);

                        Toast.makeText(OrderActivity.this, "No order found", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            Boolean lastPage = res.optBoolean("last");

            if (lastPage) {
                isLastPage = true;
            }else {
                isLastPage = false;
            }

            parseAndDisplayOrder(res);
            runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
            hideLoader();



        } catch (JSONException e) {
            Log.e("TAG", "Error creating JSON request body", e);
            runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));

            runOnUiThread(this::hideLoader);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
                order.setOrderDateTime(orderJson.optString("orderDateTime"));
                order.setExpectedArrival(orderJson.optString("expectedArrival"));
                System.out.println("orderJson.optString(\"saleType\")" +orderJson.optString("saleType"));
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

                System.out.println("orderJson.optInt(\"qty\")---"+orderJson.optInt("qty"));
                System.out.println("orderJson.optInt(\"batch id\")---"+orderJson.optString("batchId"));


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

                            OrderModel.ProductOrder productOrder = new OrderModel.ProductOrder(product, productOrderJson.optInt("quantity"),productOrderJson.optString("status"));
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
                    Toast.makeText(OrderActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.e("TAG", "Error parsing JSON response", e);
            runOnUiThread(() -> Toast.makeText(OrderActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show());
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