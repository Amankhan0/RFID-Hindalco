package com.example.hellorfid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hellorfid.R;
import com.example.hellorfid.model.OrderModel;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private OnOrderClickListener onOrderClickListener;

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
//            idTextView = itemView.findViewById(R.id.idTextView);
//            orderDateTimeTextView = itemView.findViewById(R.id.orderDateTimeTextView);
            saleTypeTextView = itemView.findViewById(R.id.saletype);
            orderTypeTextView = itemView.findViewById(R.id.ordertype);
//            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
//            qtyTextView = itemView.findViewById(R.id.qtyTextView);
//            batchIDTextView = itemView.findViewById(R.id.batchIDTextView);
//            movementStatusTextView = itemView.findViewById(R.id.movementStatusTextView);
        }

        void bind(OrderModel order) {
            if (idTextView != null) idTextView.setText("ID: " + order.getId());
            if (orderDateTimeTextView != null) orderDateTimeTextView.setText("Order Date: " + (order.getOrderDateTime() != null ? dateFormat.format(order.getOrderDateTime()) : "N/A"));
            if (saleTypeTextView != null) saleTypeTextView.setText("Sale Type: " + order.getSaleType());
            if (orderTypeTextView != null) orderTypeTextView.setText("Order Type: " + order.getOrderType());
            if (orderStatusTextView != null) orderStatusTextView.setText("Order Status: " + order.getOrderStatus());
            if (qtyTextView != null) qtyTextView.setText("Quantity: " + order.getQty());
            if (batchIDTextView != null) batchIDTextView.setText("Batch ID: " + order.getBatchID());
            if (movementStatusTextView != null) movementStatusTextView.setText("Movement Status: " + order.getMovementStatus());
        }
    }

    public void updateOrders(List<OrderModel> newOrders) {
        this.orderList.clear();
        this.orderList.addAll(newOrders);
        notifyDataSetChanged();
    }
}