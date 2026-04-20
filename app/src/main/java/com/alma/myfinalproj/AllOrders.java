package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    List<Order> orderList=new ArrayList<>();
    Button btnGoBack;



    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_orders);

        databaseService = DatabaseService.getInstance();

        rvAllOrders = findViewById(R.id.rvAllOrders);
        rvAllOrders.setLayoutManager(new GridLayoutManager(this, 2));
        btnGoBack=findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);

        orderAdapter = new OrderAdapter(this, orderList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onOrderClick(Order order) {
                // Handle user click
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




    @Override
    public void onClick(View view) {
        if (view.getId()==btnGoBack.getId()){
            Intent go = new Intent(this, AdminActivity.class);
            startActivity(go);
        }
    }


    private void fetchOrdersFromFirebase() {
        databaseService.getAllOrders(new DatabaseService.DatabaseCallback<List<Order>>() {
            @Override
            public void onCompleted(List<Order> orders) {
                if (orders != null) {


                    orderList.addAll(orders);

                    orderAdapter.setOrders(orderList);
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("TAG", "Failed to load orders", e);
                Toast.makeText(AllOrders.this, "שגיאה בטעינת נתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}