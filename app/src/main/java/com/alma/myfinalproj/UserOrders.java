package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.OrderAdapter;
import com.alma.myfinalproj.model.Order;
import com.alma.myfinalproj.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserOrders extends BaseActivity implements View.OnClickListener {

    RecyclerView rvAllOrders;
    OrderAdapter orderAdapter;
    List<Order> orderList = new ArrayList<>();
    Button btnGoBack;
    DatabaseService databaseService;
    String selectedSort = "without";
    String  uid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_orders);

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btnGoBack=findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
         mAuth = FirebaseAuth.getInstance();
         uid=mAuth.getCurrentUser().getUid();
        databaseService = DatabaseService.getInstance();
        rvAllOrders = findViewById(R.id.rvUserOrders);
        rvAllOrders.setLayoutManager(new GridLayoutManager(this, 1));
        orderAdapter = new OrderAdapter(this, orderList, new OrderAdapter.OnItemClickListener() {

            @Override
            public void onOrderClick(Order order) {
                Log.d("TAG", "order clicked: " + order);

            }

            @Override
            public void onLongOrderClick(Order order) {}
        });

        rvAllOrders.setAdapter(orderAdapter);
        fetchOrdersFromFirebase();
    }

    private void fetchOrdersFromFirebase() {
        databaseService.getUserOrders( uid,new DatabaseService.DatabaseCallback<List<Order>>() {
            @Override
            public void onCompleted(List<Order> orders) {
                if (orders != null) {
                    orderList.clear();
                    orderList.addAll(orders);
                    applyFiltersAndSorting();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("TAG", "Failed to load orders", e);
                Toast.makeText(UserOrders.this, "שגיאה בטעינת נתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFiltersAndSorting() {
        List<Order> filteredList = new ArrayList<>(orderList);

        switch (selectedSort) {
            case "date ftl":
                filteredList.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                break;
            case "date ltf":
                filteredList.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
                break;
            case "committed":
                List<Order> onlyCommitted = new ArrayList<>();
                for (Order o : orderList) {
                    if ("Committed".equalsIgnoreCase(o.getStatus())) {
                        onlyCommitted.add(o);
                    }
                }
                filteredList = onlyCommitted;
                break;
        }

        orderAdapter.setOrders(filteredList);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnGoBack.getId()) {
            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }
    }
}