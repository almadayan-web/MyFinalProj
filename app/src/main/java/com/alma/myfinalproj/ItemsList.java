package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alma.myfinalproj.adapters.ItemAdapter;
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ItemsList extends AppCompatActivity {

    private static final String TAG = "UsersListActivity";
    DatabaseService databaseService;
    private ItemAdapter itemAdapter;
    private TextView tvItemCount;
    private List<Item> itemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_list);

        databaseService = DatabaseService.getInstance();
        RecyclerView rvAllItems = findViewById(R.id.rv_item_list);
        tvItemCount = findViewById(R.id.tv_item_count);


        rvAllItems.setLayoutManager(new GridLayoutManager(this, 2));

        itemAdapter = new ItemAdapter(itemsList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                // Handle user click
                Log.d("TAG", "item clicked: " + item);
                Intent intent = new Intent(ItemsList.this, EditItem.class);
                intent.putExtra("itemId", item.getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Item item) {

            }
        });

        rvAllItems.setAdapter(itemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseService.getInstance().getItemList(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {

                itemsList.addAll(items);

                itemAdapter.notifyDataSetChanged();

                tvItemCount.setText(itemsList.size() + "");
            }


            @Override
            public void onFailed(Exception e) {

            }
        });
    }


}