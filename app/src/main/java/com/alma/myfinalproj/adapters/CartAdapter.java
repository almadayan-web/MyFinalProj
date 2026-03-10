package com.alma.myfinalproj.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.R;
import com.alma.myfinalproj.model.Cart;
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.model.ItemCart;
import com.alma.myfinalproj.utils.ImageUtil;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private Cart cart;

    public CartAdapter(Context context, Cart cart) {
        this.context = context;
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cart != null && cart.getItems() != null && position < cart.getItems().size()) {
            ItemCart item_cart = cart.getItems().get(position);
            holder.bind(item_cart);
        }
    }

    @Override
    public int getItemCount() {
        return (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name, product_price, product_amount, product_size;
        ImageButton ibPlus, ibMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_amount = itemView.findViewById(R.id.product_amount);

        }

        public void bind(final ItemCart item_cart) {
            Item product = item_cart.getItem();
            int amount = item_cart.getAmount();

            if (amount > 0) {


                product_image.setImageBitmap(ImageUtil.convertFromivIPic(product.getPic()));
                product_name.setText(product.getName());
                product_price.setText(product.getPrice() + "₪");
                product_amount.setText(String.valueOf(amount));
                //   product_size.setText(product.getSize());


            }

        }
    }
}
