package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseAdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Use super.setContentView to set the base layout containing the Drawer and FrameLayout
        super.setContentView(R.layout.activity_base_admin);
        drawerLayout = findViewById(R.id.nav_layoutA);
        NavigationView navigationView = findViewById(R.id.nav_viewA);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout contentFrame = findViewById(R.id.content_frameA);
        if (contentFrame != null) {
            contentFrame.removeAllViews();
            getLayoutInflater().inflate(layoutResID, contentFrame, true);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();



        if (id == R.id.nav_homeadmin) {
            startActivity(new Intent(this, AdminActivity.class));
        }
        else if (id == R.id.nav_signOutadmin) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Landing.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_additem) {
            startActivity(new Intent(this, AddItem.class));
        } else if (id == R.id.nav_listuser) {
            startActivity(new Intent(this, UsersListActivity.class));
        } else if (id == R.id.nav_listitem) {
            startActivity(new Intent(this, ItemsList.class));
        } else if (id == R.id.nav_showorders) {
            startActivity(new Intent(this, AllOrders.class));
        }

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}