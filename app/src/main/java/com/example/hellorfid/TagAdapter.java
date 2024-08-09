package com.example.hellorfid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private Context context;
    private List<Tag> tagList;

    public TagAdapter(Context context, List<Tag> tagList) {
        this.context = context;
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tagNumber.setText(tag.getTagNumber());
        holder.lotNumber.setText(tag.getLotNumber());

        // Set color to red if condition met (e.g., tag number starts with "A")
        if (tag.getTagNumber().startsWith("A")) {
            holder.tagNumber.setTextColor(context.getResources().getColor(R.color.red));
            holder.lotNumber.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.deleteIcon.setOnClickListener(view -> {
            // Remove item from list
            tagList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tagList.size());
        });

        holder.nextIcon.setOnClickListener(view -> {
            // Handle next icon click
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {

        TextView tagNumber;
        TextView lotNumber;
        ImageView deleteIcon;
        ImageView nextIcon;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNumber = itemView.findViewById(R.id.tagNumber1);
            lotNumber = itemView.findViewById(R.id.lotNumber1);
            deleteIcon = itemView.findViewById(R.id.deleteIcon1);
            nextIcon = itemView.findViewById(R.id.nextIcon1);
        }
    }
}
