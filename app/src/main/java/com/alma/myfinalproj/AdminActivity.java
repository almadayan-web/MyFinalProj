package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        btnAddItem=findViewById(R.id.btnGoAddItem);
        btnAddItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnAddItem.getId()){

            Intent go = new Intent(this, AddItem.class);
            startActivity(go);
        }
    }
}