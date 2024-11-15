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
import com.example.hellorfid.constants.StoryHandler;
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
        if (sessionManager.getOrderData() == null ) {
            showNoDataMessage();
            return;
        }

        JSONObject orderJson = new JSONObject(sessionManager.getOrderData().toString());
        JSONArray productIds = orderJson.getJSONArray("productIds");
        if (productIds.length() == 0) {
            showNoDataMessage();
            return;
        }
        System.out.println("productIds ==="+productIds);
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
        String productID = productId.getString("_id");
// String grade = productData.getString("grade");

        String grade = productId.getString("grade");

        String productStatus = productId.optString("status");
        if ("ORDER_PICKED".equals(productStatus)) {
            cardView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            cardView.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        TextView productNameTextView = cardView.findViewById(R.id.productNameTextView);
        TextView quantityTextView = cardView.findViewById(R.id.quantityTextView);
        TextView statusTextView = cardView.findViewById(R.id.statusTextView);
        TextView productIdTextView = cardView.findViewById(R.id.productIdTextView);
TextView gradeTextView = cardView.findViewById(R.id.gradeTextView);

        productNameTextView.setText("Product Name: " + productName);
        quantityTextView.setText("Quantity: " + quantity);
        statusTextView.setText("Status: " + productStatus);
        productIdTextView.setText("Product ID: " + productID);
        gradeTextView.setText("Grade: " + grade);



        if(!"ORDER_PICKED".equals(productStatus)){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("orderData ==="+orderData);
                    System.out.println("index ===="+index);
                    productIndex = index;

                    if(orderData != null && index != -1){

                    }

                    System.out.println();

                    try {
                        sessionManager.setProductData(sessionManager.getOrderData().getJSONArray("productIds").get(index).toString());

                        StoryHandler.orderStory(sessionManager,LoadProductAcordingToOrdersActivity.this,"NA",Constants.INBOUND_ORDER);


                    } catch (JSONException e) {
                        System.out.println("in catch-->>>>");
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return cardView;
    }
}