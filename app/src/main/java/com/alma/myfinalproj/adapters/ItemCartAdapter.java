package com.alma.myfinalproj.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.R;
import com.alma.myfinalproj.model.ItemCart;

import java.util.List;

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ViewHolder> {
    private Context context;
    private List<ItemCart> items;

    public ItemCartAdapter(Context context, List<ItemCart> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCart item_cart = items.get(position);

        holder.itemCart_image.setImageResource(R.drawable.almalogopic); // לוגו קבוע במקום תמונה כבדה
        holder.itemCart_name.setText(item_cart.getItemName());
        holder.itemCart_price.setText(item_cart.getItemPrice() + "₪");
        holder.itemCart_amount.setText(String.valueOf(item_cart.getAmount()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemCart_image;
        TextView itemCart_name, itemCart_price, itemCart_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCart_image = itemView.findViewById(R.id.itemCart_imageItemOrder);
            itemCart_name = itemView.findViewById(R.id.itemCart_nameItemOrder);
            itemCart_price = itemView.findViewById(R.id.itemCart_priceItemOrder);
            itemCart_amount = itemView.findViewById(R.id.itemCart_AmountItemOrder);
        }
    }
}