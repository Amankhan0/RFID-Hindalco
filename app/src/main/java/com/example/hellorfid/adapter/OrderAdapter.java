//package com.example.hellorfid.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.hellorfid.R;
//import com.example.hellorfid.constants.Constants;
//import com.example.hellorfid.constants.Helper;
//import com.example.hellorfid.dump.ApiCallBackWithToken;
//import com.example.hellorfid.model.OrderModel;
//
//import org.json.JSONException;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
//
//    private Context context;
//    private List<OrderModel> orderList;
//    private OnOrderClickListener onOrderClickListener;
//
//    public interface OnOrderClickListener {
//        void onOrderClick(OrderModel order) throws JSONException, InterruptedException;
//    }
//
//    public OrderAdapter(Context context, List<OrderModel> orderList, OnOrderClickListener listener) {
//        this.orderList = orderList;
//        this.context = context;
//        this.onOrderClickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
//        return new OrderViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
//        OrderModel order = orderList.get(position);
//        holder.bind(order);
//
//        holder.itemView.setOnClickListener(v -> {
//            if (onOrderClickListener != null) {
//                try {
//                    onOrderClickListener.onOrderClick(order);
//                } catch (JSONException | InterruptedException e) {
//                    System.out.println("order id--->><><<"+e);
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderList.size();
//    }
//
//    class OrderViewHolder extends RecyclerView.ViewHolder {
//        TextView idTextView, orderTypeTextView, saleTypeTextView,
//                orderStatusTextView, batchIDTextView, movementStatusTextView;
//
//        OrderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            idTextView = itemView.findViewById(R.id.idTextView);
//            orderTypeTextView = itemView.findViewById(R.id.orderTypeTextView);
//            saleTypeTextView = itemView.findViewById(R.id.saleTypeTextView);
//            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
//            batchIDTextView = itemView.findViewById(R.id.batchIDTextView);
//            movementStatusTextView = itemView.findViewById(R.id.movementStatusTextView);
//        }
//
//        void bind(OrderModel order) {
//            idTextView.setText("Order ID: " + order.getId());
//            orderTypeTextView.setText("Order Type: " + order.getOrderType());
//            saleTypeTextView.setText("Sale Type: " + order.getSaleType());
//            orderStatusTextView.setText("Status: " + order.getOrderStatus());
//            batchIDTextView.setText("Batch ID: " + order.getBatchNumber());
//            movementStatusTextView.setText("Movement Status: " + order.getMovementStatus());
//        }
//    }
//
//    public void updateOrders(List<OrderModel> newOrders) {
//        this.orderList.clear();
//        this.orderList.addAll(newOrders);
//        notifyDataSetChanged();
//    }
//}

package com.example.hellorfid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.model.OrderModel;
import org.json.JSONException;
import java.util.Arrays;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private OnOrderClickListener onOrderClickListener;

    private final List<String> nonClickableStatuses = Arrays.asList(
            Constants.ORDER_PICKED, Constants.ORDER_RECEIVED, Constants.RECHECK
    );

    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order) throws JSONException, InterruptedException;
    }

    public OrderAdapter(Context context, List<OrderModel> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.bind(order);

        boolean isNonClickable = nonClickableStatuses.contains(order.getOrderStatus());

        // Enhanced UI for non-clickable items
        if (isNonClickable) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5")); // Light grey background
            holder.itemView.setAlpha(0.8f); // Slight transparency
            // Apply grey text color to all TextViews
            holder.idTextView.setTextColor(Color.parseColor("#757575"));
            holder.orderTypeTextView.setTextColor(Color.parseColor("#757575"));
            holder.saleTypeTextView.setTextColor(Color.parseColor("#757575"));
            holder.orderStatusTextView.setTextColor(Color.parseColor("#757575"));
            holder.batchIDTextView.setTextColor(Color.parseColor("#757575"));
            holder.movementStatusTextView.setTextColor(Color.parseColor("#757575"));

            holder.itemView.setOnClickListener(v ->
                    Toast.makeText(context, "Not able to perform the operation", Toast.LENGTH_SHORT).show()
            );
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.itemView.setAlpha(1.0f);
            // Reset text colors for clickable items
            holder.idTextView.setTextColor(context.getResources().getColor(R.color.hindalcoRed));
            holder.orderTypeTextView.setTextColor(Color.BLACK);
            holder.saleTypeTextView.setTextColor(Color.BLACK);
            holder.orderStatusTextView.setTextColor(Color.BLACK);
            holder.batchIDTextView.setTextColor(Color.BLACK);
            holder.movementStatusTextView.setTextColor(Color.BLACK);

            holder.itemView.setOnClickListener(v -> {
                if (onOrderClickListener != null) {
                    try {
                        onOrderClickListener.onOrderClick(order);
                    } catch (JSONException | InterruptedException e) {
                        System.out.println("order id--->><><<"+e);
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, orderTypeTextView, saleTypeTextView,
                orderStatusTextView, batchIDTextView, movementStatusTextView;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            orderTypeTextView = itemView.findViewById(R.id.orderTypeTextView);
            saleTypeTextView = itemView.findViewById(R.id.saleTypeTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            batchIDTextView = itemView.findViewById(R.id.batchIDTextView);
            movementStatusTextView = itemView.findViewById(R.id.movementStatusTextView);
        }

        void bind(OrderModel order) {
            idTextView.setText("Order ID: " + order.getId());
            orderTypeTextView.setText("Order Type: " + order.getOrderType());
            saleTypeTextView.setText("Sale Type: " + order.getSaleType());
            orderStatusTextView.setText("Status: " + order.getOrderStatus());
            batchIDTextView.setText("Batch ID: " + order.getBatchNumber());
            movementStatusTextView.setText("Movement Status: " + order.getMovementStatus());
        }
    }

    public void updateOrders(List<OrderModel> newOrders) {
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }
}