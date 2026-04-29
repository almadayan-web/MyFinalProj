package com.alma.myfinalproj;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.CartAdapter;
import com.alma.myfinalproj.model.Cart;
import com.alma.myfinalproj.model.ItemCart;
import com.alma.myfinalproj.model.Order;
import com.alma.myfinalproj.model.User;
import com.alma.myfinalproj.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBackCart;

    RecyclerView rcCart;

    Cart cart = null;
    Button btnBePayment, btnEditAmount, btnDelete;
    TextView tvprice;
    DatabaseService databaseService;
    String uid;
    User user = null;
    FirebaseAuth mAuth;
    EditText etDestDate;
    String destenationDate;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        btnGoBackCart = findViewById(R.id.btnGoBackCart);
        btnGoBackCart.setOnClickListener(this);
        //btnEditAmount= findViewById(R.id.btnEditAmount);
        //btnEditAmount.setOnClickListener(this);
        //btnDelete= findViewById(R.id.btnDelete);
        //btnDelete.setOnClickListener(this);
        btnBePayment = findViewById(R.id.btnOrderAndPay);
        btnBePayment.setOnClickListener(this);


        rcCart = findViewById(R.id.rvCart);
        tvprice = findViewById(R.id.tvTotal);

        etDestDate = findViewById(R.id.etDestDate);
        etDestDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CartActivity.this,
                    (DatePicker view, int y, int m, int d) -> {
                        etDestDate.setText(d + "/" + (m + 1) + "/" + y);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


        rcCart.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();


        databaseService = DatabaseService.getInstance();
        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                user = new User(object);
            }

            @Override
            public void onFailed(Exception e) {
                return;
            }
        });

        // אתחול סל ריק בתחילה כדי למנוע NullPointer
        // טען את הסל מהמסד
        databaseService.getCart(uid, new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart resultCart) {
                if (resultCart != null && resultCart.getItems() != null) {
                    cart = resultCart;

                    Toast.makeText(CartActivity.this, "" + cart.toString(), Toast.LENGTH_SHORT).show();
                } else cart = new Cart();

                cartAdapter = new CartAdapter(CartActivity.this, cart);
                rcCart.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
                tvprice.setText(cart.getTotalPrice() + "");

            }

            @Override
            public void onFailed(Exception e) {
                cart = new Cart();
                Log.e("error", e.getMessage());
                Toast.makeText(CartActivity.this, "שגיאה בטעינת הסל", Toast.LENGTH_SHORT).show();
            }
        });


        //adapter = new ItemsAdapter(cart.getItems());
        //rcCart.setAdapter(adapter);
    }

    public void beyomdPayment(View view) {
        Intent intent = new Intent(CartActivity.this, Payment.class);
        intent.putExtra("total", cart.getTotal());
        startActivity(intent);
    }


    private void processOrder() {
        if (cart == null || cart.getItems().isEmpty()) {
            Toast.makeText(this, "העגלה ריקה!", Toast.LENGTH_SHORT).show();
            return;
        }

        // המרת הפריטים לפריטים קלים (בלי תמונות)
        List<ItemCart> lightItems = new ArrayList<>();
        for (ItemCart originalItem : cart.getItems()) {
            ItemCart light = new ItemCart(
                    originalItem.getItemId(),
                    originalItem.getItemName(),
                    originalItem.getItemPrice(),
                    originalItem.getAmount()
            );
            lightItems.add(light);
        }

        String orderId = databaseService.generateOrderId();
        Order order = new Order(orderId, lightItems, cart.getTotal(), "new", user, System.currentTimeMillis(), destenationDate);
        order.setTimestamp(System.currentTimeMillis());

        databaseService.createNewOreder(order, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(CartActivity.this, "הזמנה נשמרה!", Toast.LENGTH_SHORT).show();
                double total = order.getTotalPrice();
                cart = new Cart();
                goUpdateCart(cart);
                Intent go = new Intent(CartActivity.this, Payment.class);
                go.putExtra("total", total);
                startActivity(go);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CartActivity.this, "שגיאה בשמירת ההזמנה", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void goUpdateCart(Cart cart) {
        databaseService.updateCart(cart, uid, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view == btnBePayment) {

            destenationDate = etDestDate.getText().toString();
            // בדיקות תקינות
            processOrder();
        } else if (view == btnGoBackCart) {

            Intent go = new Intent(CartActivity.this, MainActivity.class);
            startActivity(go);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}