package com.alma.myfinalproj.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.R;
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.utils.ImageUtil;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final List<Item> itemList;
    private final OnItemClickListener onClickListener;
    public ItemAdapter(@Nullable final OnItemClickListener onClickListener) {
        itemList = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    public ItemAdapter(@Nullable final List<Item> itemList, @Nullable final OnItemClickListener onClickListener) {
        this.itemList = itemList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (item == null) return;

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice() + "");


        holder.ivItemPic.setImageBitmap(ImageUtil.convertFromivIPic(item.getPic()));


        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onItemClick(item);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onLongItemClick(item);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Item> items) {
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    public void updateItem(Item item) {
        int index = itemList.indexOf(item);
        if (index == -1) return;
        itemList.set(index, item);
        notifyItemChanged(index);
    }

    public void removeItem(Item item) {
        int index = itemList.indexOf(item);
        if (index == -1) return;
        itemList.remove(index);
        notifyItemRemoved(index);
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);

        void onLongItemClick(Item item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Chip chipRole;

        ImageView ivItemPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);


            ivItemPic = itemView.findViewById(R.id.ivItemPic);
        }
    }
}