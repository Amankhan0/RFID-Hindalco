package com.example.hellorfid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hellorfid.R;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.model.DispatchModel;
import com.example.hellorfid.session.SessionManager;

import java.util.List;

public class DispatchAdapter extends BaseAdapter {
    private Context context;
    private List<DispatchModel> buildingModels;
    private LayoutInflater inflater;
    private SessionManager sessionManager;

    public DispatchAdapter(Context context, List<DispatchModel> buildingModels) {
        this.context = context;
        this.buildingModels = buildingModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return buildingModels != null ? buildingModels.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return buildingModels != null ? buildingModels.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.sessionManager = new SessionManager(context);

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.building_list_item, parent, false);
            holder = new ViewHolder();
            holder.mainCard = convertView.findViewById(R.id.mainCard);
            holder.nameTextView = convertView.findViewById(R.id.buildingName);
            holder.addressTextView = convertView.findViewById(R.id.unitName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (buildingModels != null && position < buildingModels.size()) {
            DispatchModel buildingModel = buildingModels.get(position);
            holder.nameTextView.setText(buildingModel.getBuildingName());
            holder.addressTextView.setText(buildingModel.getBuildingNo());

            holder.mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("clicked " + buildingModel.getId());
                    sessionManager.setBuildingIdTo( buildingModel.getId());
                    sessionManager.clearBuildingIdTo();
                    if(sessionManager.getBuildingIdTo() == null || sessionManager.getBuildingIdTo().isEmpty()){
                        sessionManager.setBuildingIdTo(buildingModel.getId());
                        System.out.println("call-----Dispatch");
                    }
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        LinearLayout mainCard;
        TextView nameTextView;
        TextView addressTextView;
    }
}