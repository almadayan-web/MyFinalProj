package com.alma.myfinalproj.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.R;
import com.alma.myfinalproj.model.ItemCart;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private List<ItemCart> items;

    public OrderItemsAdapter(List<ItemCart> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCart item = items.get(position);
        holder.tvName.setText(item.getItemName());
        holder.tvPrice.setText(item.getItemPrice() + "₪");
        holder.tvAmount.setText("כמות: " + item.getAmount());
        holder.tvSubtotal.setText("סה\"כ: " + (item.getItemPrice() * item.getAmount()) + "₪");
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvAmount, tvSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tvOrderItemName);
            tvPrice   = itemView.findViewById(R.id.tvOrderItemPrice);
            tvAmount  = itemView.findViewById(R.id.tvOrderItemAmount);
            tvSubtotal= itemView.findViewById(R.id.tvOrderItemSubtotal);
        }
    }
}