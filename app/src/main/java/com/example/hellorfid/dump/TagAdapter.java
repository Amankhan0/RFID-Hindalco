package com.example.hellorfid.dump;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private Context context;
    private List<Tag> tagList;
    private OnTagDeleteListener onTagDeleteListener;

    public interface OnTagDeleteListener {
        void onTagDelete(String tagId);
    }

    public TagAdapter(Context context, List<Tag> tagList, OnTagDeleteListener listener) {
        this.context = context;
        this.tagList = tagList;
        this.onTagDeleteListener = listener;
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
        SessionManager sessionManager =new SessionManager(context);
        holder.tagNumber.setText(tag.getTagNumber().substring(tag.getTagNumber().length()-8));
        holder.lotNumber.setText(tag.getTagType());

        holder.scanningFor.setText(sessionManager.getCheckTagOn());
        if(sessionManager.getCheckTagOn().equals(tag.getTagType())){
            if (!tag.isOverLimit()) {
                holder.scanningFor.setBackgroundColor(Color.parseColor("#039e00"));
                holder.tagNumber.setTextColor(context.getResources().getColor(R.color.black));
                holder.lotNumber.setTextColor(context.getResources().getColor(R.color.black));
            }else {
                holder.scanningFor.setBackgroundColor(Color.parseColor("#9e0005"));
                holder.tagNumber.setTextColor(context.getResources().getColor(R.color.red));
                holder.lotNumber.setTextColor(context.getResources().getColor(R.color.red));
            }
        }else {
            holder.scanningFor.setBackgroundColor(Color.parseColor("#9e0005"));
            holder.tagNumber.setTextColor(context.getResources().getColor(R.color.red));
            holder.lotNumber.setTextColor(context.getResources().getColor(R.color.red));
        }



        holder.deleteIcon.setOnClickListener(view -> {
            String deletedTagId = tagList.get(position).getTagNumber();
            tagList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tagList.size());

            if (onTagDeleteListener != null) {
                onTagDeleteListener.onTagDelete(deletedTagId);
            }
        });

//        holder.nextIcon.setOnClickListener(view -> {
//            // Handle next icon click
//        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagNumber;
        TextView lotNumber;
        ImageView deleteIcon;
        TextView scanningFor;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNumber = itemView.findViewById(R.id.tagNumber1);
            lotNumber = itemView.findViewById(R.id.lotNumber1);
            deleteIcon = itemView.findViewById(R.id.deleteIcon1);
            scanningFor = itemView.findViewById(R.id.scanningFor);
        }
    }
}