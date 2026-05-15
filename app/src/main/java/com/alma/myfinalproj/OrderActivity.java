package com.alma.myfinalproj;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.CartAdapter;
import com.alma.myfinalproj.adapters.OrderItemsAdapter;
import com.alma.myfinalproj.model.Order;
import com.alma.myfinalproj.services.DatabaseService;

public class OrderActivity extends BaseAdminActivity {

    private TextView tvOrderId, tvOrderStatus, tvOrderDate, tvOrderDestDate,
            tvOrderTotal, tvOrderUserName, tvOrderUserPhone;
    private RecyclerView rvOrderItems;
    private DatabaseService databaseService;
    private String orderId;
    private Button btnChangeStatus;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        tvOrderId       = findViewById(R.id.tvOrderId);
        tvOrderStatus   = findViewById(R.id.tvOrderStatus);
        tvOrderDate     = findViewById(R.id.tvOrderDate);
        tvOrderDestDate = findViewById(R.id.tvOrderDestDate);
        tvOrderTotal    = findViewById(R.id.tvOrderTotal);
        tvOrderUserName = findViewById(R.id.tvOrderUserName);
        tvOrderUserPhone= findViewById(R.id.tvOrderUserPhone);
        rvOrderItems    = findViewById(R.id.rvOrderItems);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        orderId = getIntent().getStringExtra("orderId");
        databaseService = DatabaseService.getInstance();

        loadOrder();
    }

    private void loadOrder() {
        databaseService.getOrder(orderId, new DatabaseService.DatabaseCallback<Order>() {
            @Override
            public void onCompleted(Order order) {
                if (order == null) {
                    Toast.makeText(OrderActivity.this, "ההזמנה לא נמצאה", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                currentOrder = order;  // ← שמור את ההזמנה

                tvOrderId.setText("מספר הזמנה: " + order.getOrderId());
                tvOrderStatus.setText("סטטוס: " + order.getStatus());
                tvOrderDate.setText("תאריך הזמנה: " + order.getFormattedTimestamp());
                tvOrderDestDate.setText("תאריך אספקה: " + order.getDestinationDate());
                tvOrderTotal.setText("סה\"כ לתשלום: ₪" + order.getTotalPrice());

                if (order.getUser() != null) {
                    tvOrderUserName.setText("לקוח: " + order.getUser().getFname() + " " + order.getUser().getLname());
                    tvOrderUserPhone.setText("טלפון: " + order.getUser().getPhone());
                }

                OrderItemsAdapter adapter = new OrderItemsAdapter(order.getItems());
                rvOrderItems.setAdapter(adapter);

                // ← הגדרת כפתור שינוי סטטוס
                btnChangeStatus = findViewById(R.id.btnChangeStatus);
                btnChangeStatus.setOnClickListener(v -> showStatusDialog());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("OrderActivity", "Failed to load order", e);
                Toast.makeText(OrderActivity.this, "שגיאה בטעינת ההזמנה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStatusDialog() {
        String[] statuses = {"new", "Committed", "Done"};
        String[] statusesHebrew = {"חדשה", "מאושרת", "הושלמה"};

        new AlertDialog.Builder(this)
                .setTitle("שנה סטטוס הזמנה")
                .setItems(statusesHebrew, (dialog, which) -> {
                    String newStatus = statuses[which];
                    currentOrder.setStatus(newStatus);

                    databaseService.updateOrder(currentOrder, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {
                            tvOrderStatus.setText("סטטוס: " + newStatus);
                            Toast.makeText(OrderActivity.this, "הסטטוס עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(OrderActivity.this, "שגיאה בעדכון הסטטוס", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }
}