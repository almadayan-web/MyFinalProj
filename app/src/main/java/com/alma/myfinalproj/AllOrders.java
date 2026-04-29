package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.OrderAdapter;
import com.alma.myfinalproj.model.Order;
import com.alma.myfinalproj.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AllOrders extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvAllOrders;
    OrderAdapter orderAdapter;
    List<Order> orderList = new ArrayList<>();
    Button btnGoBack;
    DatabaseService databaseService;

    // חדש - למיון וסינון
    View btnShowOptionsOrder;
    LinearLayout optionsContainerOrder;
    TextView option1, option2, option3, option4;
    String selectedSort = "without";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_orders);

        databaseService = DatabaseService.getInstance();

        rvAllOrders = findViewById(R.id.rvAllOrders);
        rvAllOrders.setLayoutManager(new GridLayoutManager(this, 2));

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);

        // חדש - מיון וסינון
        btnShowOptionsOrder = findViewById(R.id.btnShowOptionsOrder);
        optionsContainerOrder = findViewById(R.id.optionsContainerOrder);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        btnShowOptionsOrder.setOnClickListener(this);
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        orderAdapter = new OrderAdapter(this, orderList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onOrderClick(Order order) {
                Log.d("TAG", "order clicked: " + order);
                Intent intent = new Intent(AllOrders.this, Order.class);
                intent.putExtra("orderId", order.getOrderId());
                startActivity(intent);
            }

            @Override
            public void onLongOrderClick(Order order) {
            }
        });

        rvAllOrders.setAdapter(orderAdapter);
        fetchOrdersFromFirebase();
    }

    private void fetchOrdersFromFirebase() {
        databaseService.getAllOrders(new DatabaseService.DatabaseCallback<List<Order>>() {
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
                Toast.makeText(AllOrders.this, "שגיאה בטעינת נתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFiltersAndSorting() {
        List<Order> filteredList = new ArrayList<>(orderList);

        switch (selectedSort) {
            case "date ftl":
                // מהחדש לישן
                filteredList.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                break;
            case "date ltf":
                // מהישן לחדש
                filteredList.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
                break;
            case "committed":
                // רק הזמנות שסטטוס שלהן Committed
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
        int id = view.getId();

        if (id == btnGoBack.getId()) {
            Intent go = new Intent(this, AdminActivity.class);
            startActivity(go);

        } else if (id == R.id.btnShowOptionsOrder) {
            int visibility = (optionsContainerOrder.getVisibility() == View.GONE)
                    ? View.VISIBLE : View.GONE;
            optionsContainerOrder.setVisibility(visibility);

        } else if (id == R.id.option1) {
            selectedSort = "without";
            applyFiltersAndSorting();
            optionsContainerOrder.setVisibility(View.GONE);

        } else if (id == R.id.option2) {
            selectedSort = "date ftl";
            applyFiltersAndSorting();
            optionsContainerOrder.setVisibility(View.GONE);

        } else if (id == R.id.option3) {
            selectedSort = "date ltf";
            applyFiltersAndSorting();
            optionsContainerOrder.setVisibility(View.GONE);

        } else if (id == R.id.option4) {
            selectedSort = "committed";
            applyFiltersAndSorting();
            optionsContainerOrder.setVisibility(View.GONE);
        }
    }
}