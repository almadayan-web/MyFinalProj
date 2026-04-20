package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.alma.myfinalproj.model.Cart;
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.model.ItemCart;
import com.alma.myfinalproj.services.DatabaseService;
import com.alma.myfinalproj.utils.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;

public class ItemProfile extends AppCompatActivity implements View.OnClickListener {

    Button btnItemClose, btnAddToCart;
    Item currentItem = null;
    Intent takeit;

    TextView tvItemType, tvItemName, tvItemSize, tvItemPrice, tvItemDetails;
    ImageView ivItemPic;
    private String itemId = "";

    DatabaseService databaseService;
    Cart userCart = null;
    String uid;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_profile);

        initViews();

        takeit = getIntent();
        itemId = takeit.getStringExtra("itemId");

        databaseService = DatabaseService.getInstance();
        uid = mAuth.getUid();

        if (!itemId.isEmpty()) {
            databaseService.getItem(itemId, new DatabaseService.DatabaseCallback<Item>() {
                @Override
                public void onCompleted(Item item) {
                    currentItem = item;
                    if (currentItem != null) {
                        tvItemName.setText(currentItem.getName());
                        tvItemType.setText(currentItem.getType());
                        tvItemSize.setText(currentItem.getSize());
                        tvItemPrice.setText(currentItem.getPrice() + "");
                        tvItemDetails.setText(currentItem.getDetails());
                        ivItemPic.setImageBitmap(ImageUtil.convertFromivIPic(currentItem.getPic()));
                    }
                }

                @Override
                public void onFailed(Exception e) {}
            });
        }
    }

    private void initViews() {
        tvItemName = findViewById(R.id.tvItemName);
        tvItemType = findViewById(R.id.tvItemType);
        tvItemSize = findViewById(R.id.tvItemSize);
        tvItemPrice = findViewById(R.id.tvItemPrice);
        tvItemDetails = findViewById(R.id.tvItemDetails);
        ivItemPic = findViewById(R.id.ivItemPic);
        btnItemClose = findViewById(R.id.btnItemClose);
        btnItemClose.setOnClickListener(this);
        btnAddToCart = findViewById(R.id.btnItemAddToCart);
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnItemClose.getId()) {
            finish();
        }

        if (v == btnAddToCart) {
            // שינוי כאן - בלי לשמור את כל האובייקט Item
            ItemCart newItemCart = new ItemCart(
                    currentItem.getId(),
                    currentItem.getName(),
                    currentItem.getPrice(),
                    1
            );

            databaseService.getCart(uid, new DatabaseService.DatabaseCallback<Cart>() {
                @Override
                public void onCompleted(Cart cart) {
                    if (cart == null)
                        cart = new Cart();
                    userCart = cart;
                    userCart.addItemToCart(newItemCart);

                    databaseService.updateCart(userCart, uid, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {
                            Log.d("Update cart", userCart.getTotal() + "");
                            Toast.makeText(ItemProfile.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                            Intent go = new Intent(ItemProfile.this, CartActivity.class);
                            startActivity(go);
                        }

                        @Override
                        public void onFailed(Exception e) {}
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    userCart = new Cart();
                    userCart.addItemToCart(newItemCart);
                }
            });
        }
    }
}