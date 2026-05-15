package com.alma.myfinalproj.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.R;
import com.alma.myfinalproj.model.Cart;
import com.alma.myfinalproj.model.ItemCart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private Cart cart;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, Cart cart, OnCartChangedListener listener) {
        this.context = context;
        this.cart = cart;
        this.listener = listener;
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
            holder.bind(item_cart, position);
        }
    }

    @Override
    public int getItemCount() {
        return (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, product_price, product_amount;
        Button btnDelete, btnEditAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_amount = itemView.findViewById(R.id.product_amount);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEditAmount = itemView.findViewById(R.id.btnEditAmount);
        }

        public void bind(ItemCart item_cart, int position) {
            product_name.setText(item_cart.getItemName());
            product_price.setText(item_cart.getItemPrice() + "₪");
            product_amount.setText(String.valueOf(item_cart.getAmount()));

            // כפתור מחק
            btnDelete.setOnClickListener(v -> {
                cart.getItems().remove(position);
                notifyDataSetChanged();
                if (listener != null) listener.onCartChanged();
            });

            // כפתור ערוך כמות
            btnEditAmount.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("ערוך כמות");

                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_amount, null);
                builder.setView(dialogView);

                TextView tvMinus = dialogView.findViewById(R.id.tvMinus);
                TextView tvAmount = dialogView.findViewById(R.id.tvDialogAmount);
                TextView tvPlus = dialogView.findViewById(R.id.tvPlus);

                final int[] newAmount = {item_cart.getAmount()};
                tvAmount.setText(String.valueOf(newAmount[0]));

                tvMinus.setOnClickListener(v2 -> {
                    if (newAmount[0] > 1) {
                        newAmount[0]--;
                        tvAmount.setText(String.valueOf(newAmount[0]));
                    }
                });

                tvPlus.setOnClickListener(v2 -> {
                    newAmount[0]++;
                    tvAmount.setText(String.valueOf(newAmount[0]));
                });

                builder.setPositiveButton("אישור", (dialog, which) -> {
                    item_cart.setAmount(newAmount[0]);
                    notifyDataSetChanged();
                    if (listener != null) listener.onCartChanged();
                });

                builder.setNegativeButton("ביטול", null);
                builder.show();
            });
        }
    }
}