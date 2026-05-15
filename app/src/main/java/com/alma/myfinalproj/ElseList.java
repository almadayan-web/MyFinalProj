package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageButton;

import com.alma.myfinalproj.adapters.ItemAdapter;
import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ElseList extends BaseActivity {  // ← שינוי

    RecyclerView rvItemList;
    ItemAdapter itemAdapter;
    List<Item> itemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_else_list);

        // ← הוספת כפתור התפריט
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        rvItemList = findViewById(R.id.rv_item_list);
        rvItemList.setLayoutManager(new GridLayoutManager(this, 2));

        itemAdapter = new ItemAdapter(itemsList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Log.d("TAG", "item clicked: " + item);
                Intent intent = new Intent(ElseList.this, ItemProfile.class);
                intent.putExtra("itemId", item.getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(Item item) {}
        });
        itemAdapter.setItemList(itemsList);
        rvItemList.setAdapter(itemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseService.getInstance().getItemList(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                if (items != null) {
                    itemsList.clear();  // ← מונע כפילויות
                    for (int i = 0; i < items.size(); i++) {
                        if (!items.get(i).getType().contains("עוגה")) {
                            itemsList.add(items.get(i));
                        }
                    }
                    Log.d("list", itemsList.toString());
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(Exception e) {}
        });
    }
}