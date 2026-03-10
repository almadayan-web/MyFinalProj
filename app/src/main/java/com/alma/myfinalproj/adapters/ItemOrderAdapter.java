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
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.model.ItemCart;
import com.alma.myfinalproj.utils.ImageUtil;

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
        View view = LayoutInflater.from(context).inflate(R.layout.oneitemorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemCart item_cart = items.get(position);
        holder.bind(item_cart);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name, product_price, product_amount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_imageItemCart);
            product_name = itemView.findViewById(R.id.product_nameItemCart);
            product_price = itemView.findViewById(R.id.product_priceItemCart);
            product_amount = itemView.findViewById(R.id.product_AmountItemCart);
        }

        public void bind(final ItemCart item_cart) {
            Item item = item_cart.getItem();
            int amount = item_cart.getAmount();



                product_image.setImageBitmap(ImageUtil.convertFromivIPic(item.getPic()));
                product_name.setText(item.getName());
                product_price.setText(item.getPrice() + "₪");
                product_amount.setText(String.valueOf(amount));









            }

        }
    }

