package com.example.hellorfid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.OrderModel;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private OnOrderClickListener onOrderClickListener;
    private ApiCallBackWithToken apiCallBackWithToken;

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

        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                try {
//                    Helper.commanHitApi(apiCallBackWithToken, Constants.addBulkTags, );
                    onOrderClickListener.onOrderClick(order);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, orderDateTimeTextView, saleTypeTextView, orderTypeTextView,
                orderStatusTextView, qtyTextView, batchIDTextView, movementStatusTextView;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
//            orderDateTimeTextView = itemView.findViewById(R.id.orderDateTimeTextView);
            saleTypeTextView = itemView.findViewById(R.id.saleTypeTextView);
            orderTypeTextView = itemView.findViewById(R.id.orderTypeTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            qtyTextView = itemView.findViewById(R.id.qtyTextView);
            batchIDTextView = itemView.findViewById(R.id.batchIDTextView);
            movementStatusTextView = itemView.findViewById(R.id.movementStatusTextView);
        }

        void bind(OrderModel order) {
            idTextView.setText("Order ID: " + order.getId());

//            String formattedDate = "N/A";
//            Object orderDateTime = order.getOrderDateTime();
//            if (orderDateTime != null) {
//                if (orderDateTime instanceof Date) {
//                    formattedDate = dateFormat.format((Date) orderDateTime);
//                } else if (orderDateTime instanceof Long) {
//                    formattedDate = dateFormat.format(new Date((Long) orderDateTime));
//                } else if (orderDateTime instanceof String) {
//                    try {
//                        Date parsedDate = dateFormat.parse((String) orderDateTime);
//                        formattedDate = dateFormat.format(parsedDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                        formattedDate = (String) orderDateTime; // Use the string as-is if parsing fails
//                    }
//                }
//            }


            saleTypeTextView.setText("Sale Type: " + order.getSaleType());
            orderTypeTextView.setText("Order Type: " + order.getOrderType());
            orderStatusTextView.setText("Status: " + order.getOrderStatus());
//            qtyTextView.setText("Quantity: " + order.getQty());
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