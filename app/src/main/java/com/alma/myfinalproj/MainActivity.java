package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.core.view.GravityCompat;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Button btnCakes, btnElse, btnUserProfile, btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activitymain2);

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btnCakes = findViewById(R.id.btnCakes);
        btnCakes.setOnClickListener(this);
        btnElse = findViewById(R.id.btnElse);
        btnElse.setOnClickListener(this);
        btnUserProfile = findViewById(R.id.btnUserProfile);
        btnUserProfile.setOnClickListener(this);
        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnCakes.getId()) {
            Intent go = new Intent(this, CakesList.class);
            startActivity(go);
        }
        if (v.getId() == btnElse.getId()) {
            Intent go = new Intent(this, ElseList.class);
            startActivity(go);
        }
        if (v.getId() == btnUserProfile.getId()) {
            Intent go = new Intent(this, UserProfile.class);
            startActivity(go);
        }
        if (v.getId() == btnOrder.getId()) {
            Intent go = new Intent(this, CartActivity.class);
            startActivity(go);
        }
    }
}