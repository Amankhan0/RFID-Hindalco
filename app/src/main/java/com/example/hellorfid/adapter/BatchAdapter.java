package com.example.hellorfid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hellorfid.R;
import com.example.hellorfid.activities.AddBatchFormActivity;
import com.example.hellorfid.activities.BatchActivity;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.BatchModel;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BatchAdapter extends BaseAdapter {


    private Context context;
    private List<BatchModel> batchModels;
    private LayoutInflater inflater;
    private SessionManager sessionManager;

    public BatchAdapter(Context context, List<BatchModel> batchModels) {
        this.context = context;
        this.batchModels = batchModels;
        this.inflater = LayoutInflater.from(context);
        this.sessionManager = new SessionManager(context);
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
                    JSONObject BatchJson = new JSONObject();

                    try {
                        BatchJson.put("batchId", batchModel.getId());
                        BatchJson.put("product_id", batchModel.getPid());
                        BatchJson.put("batchNumber", batchModel.getBatchNumber());
                        BatchJson.put("status", batchModel.getStatus());
                        BatchJson.put("movementStatus", batchModel.getMovementStatus());
                        BatchJson.put("totalInventory", batchModel.getTotalInventory());


                        sessionManager.clearBatch();
                        // Store BatchJson in SessionManager
                        sessionManager.setBatch(BatchJson.toString());


                        System.out.println("batch save -----"+sessionManager.getBatch());

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("totalInventory", batchModel.getTotalInventory());
                        intent.putExtra("apiUrl", Constants.addBulkTags);  // Add this line
                        context.startActivity(intent);

                        // Show a toast message to confirm the action
                        Toast.makeText(context, "Batch data stored in session", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error storing batch data", Toast.LENGTH_SHORT).show();
                    }

                    // If you still want to print the JSON object for debugging purposes:
                    System.out.println("BatchJson: " + BatchJson.toString());
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