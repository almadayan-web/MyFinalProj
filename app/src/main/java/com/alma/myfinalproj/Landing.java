package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Landing extends AppCompatActivity implements View.OnClickListener {

    Button btnsu, btna, btnli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        btnsu = findViewById(R.id.btnsu);
        btnsu.setOnClickListener(this);
        btna = findViewById(R.id.btna);
        btna.setOnClickListener(this);
        btnli = findViewById(R.id.btnli);
        btnli.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == btnli.getId()) {
            Intent go = new Intent(this, login.class);
            startActivity(go);
        }
        if (view.getId() == btnsu.getId()) {
            Intent go = new Intent(this, Singup.class);
            startActivity(go);
        }
        if (view.getId() == btna.getId()) {
            Intent go = new Intent(this, about.class);
            startActivity(go);
        }
    }
}