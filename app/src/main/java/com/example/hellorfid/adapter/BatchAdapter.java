package com.example.hellorfid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hellorfid.R;
import com.example.hellorfid.model.BatchModel;

import java.util.List;

public class BatchAdapter extends BaseAdapter {
    private Context context;
    private List<BatchModel> batchModels;
    private LayoutInflater inflater;

    public BatchAdapter(Context context, List<BatchModel> batchModels) {
        this.context = context;
        this.batchModels = batchModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return batchModels != null ? batchModels.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return batchModels != null ? batchModels.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.batch_list_item, parent, false);
            holder = new ViewHolder();
            holder.mainCard = convertView.findViewById(R.id.mainCard);
            holder.batchNameTextView = convertView.findViewById(R.id.batchName);
            holder.batchNumberTextView = convertView.findViewById(R.id.batchNumber);
            holder.productNameTextView = convertView.findViewById(R.id.productName);
            holder.statusTextView = convertView.findViewById(R.id.status);
            holder.addTagsButton = convertView.findViewById(R.id.addTagsButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (batchModels != null && position < batchModels.size()) {
            BatchModel batchModel = batchModels.get(position);
            holder.batchNameTextView.setText(batchModel.getBatchName());
            holder.batchNumberTextView.setText(batchModel.getBatchNumber());
            holder.productNameTextView.setText(batchModel.getProductName());
            holder.statusTextView.setText(batchModel.getStatus());

            holder.mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle click event
                    // For example, open a new activity with batch details
                }
            });

            holder.addTagsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("id----"+batchModel.getId());
                    System.out.println("id----"+batchModel.getPid());
                    System.out.println("id----"+batchModel.getBatchNumber());
                    System.out.println("id----"+batchModel.getStatus());
                    System.out.println("id----"+batchModel.getMovementStatus());

                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        LinearLayout mainCard;
        TextView batchNameTextView;
        TextView batchNumberTextView;
        TextView productNameTextView;
        TextView statusTextView;
        Button addTagsButton;
    }
}