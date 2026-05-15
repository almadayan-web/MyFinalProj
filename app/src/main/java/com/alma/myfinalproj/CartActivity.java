package com.alma.myfinalproj;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.CartAdapter;
import com.alma.myfinalproj.model.Cart;
import com.alma.myfinalproj.model.Order;
import com.alma.myfinalproj.model.User;
import com.alma.myfinalproj.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class CartActivity extends BaseActivity implements View.OnClickListener {  // ← שינוי

    Button btnGoBackCart;
    RecyclerView rcCart;
    Cart cart = null;
    Button btnBePayment;
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

        // ← הוספת כפתור התפריט
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        btnGoBackCart = findViewById(R.id.btnGoBackCart);
        btnGoBackCart.setOnClickListener(this);
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
            public void onFailed(Exception e) {}
        });

        databaseService.getCart(uid, new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart resultCart) {
                if (resultCart != null && resultCart.getItems() != null) {
                    cart = resultCart;
                } else {
                    cart = new Cart();
                }

                cartAdapter = new CartAdapter(CartActivity.this, cart, () -> {
                    tvprice.setText(cart.getTotalPrice() + "");
                    goUpdateCart(cart);
                });

                rcCart.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();

                if (cart != null && cart.getTotalPrice() != 0) {
                    tvprice.setText(cart.getTotalPrice() + "");
                }
            }

            @Override
            public void onFailed(Exception e) {
                cart = new Cart();
                Log.e("error", e.getMessage());
                Toast.makeText(CartActivity.this, "שגיאה בטעינת הסל", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processOrder() {
        if (cart == null || cart.getItems().isEmpty()) {
            Toast.makeText(this, "העגלה ריקה!", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = databaseService.generateOrderId();
        Order order = new Order(orderId, cart.getItems(), cart.getTotal(), "new", user, System.currentTimeMillis(), destenationDate);

        databaseService.createNewOreder(order, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                double total = order.getTotalPrice();
                cart = new Cart();
                goUpdateCart(cart);

                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("ההזמנה בוצעה בהצלחה! 🎉")
                        .setMessage("לתיאום ושאלות ניתן ליצור קשר:\n\n📞 0548185110")
                        .setPositiveButton("אישור", (dialog, which) -> {
                            Intent go = new Intent(CartActivity.this, Payment.class);
                            go.putExtra("total", total);
                            startActivity(go);
                        })
                        .show();
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
            public void onCompleted(Void object) {}

            @Override
            public void onFailed(Exception e) {}
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnBePayment) {
            destenationDate = etDestDate.getText().toString();
            processOrder();
        } else if (view == btnGoBackCart) {
            Intent go = new Intent(CartActivity.this, MainActivity.class);
            startActivity(go);
        }
    }
}