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

public class about extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        btnGoBack=findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==btnGoBack.getId()){
            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }
    }
}