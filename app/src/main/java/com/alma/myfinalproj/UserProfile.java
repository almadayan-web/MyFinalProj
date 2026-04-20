package com.alma.myfinalproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.alma.myfinalproj.model.User;
import com.alma.myfinalproj.services.DatabaseService;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack;

    private static final String TAG = "UserProfileActivity";

    private EditText etUserFname, etUserLname, etUserPhone;
    private TextView tvUserEmail, tvUserPassword;
    private Button btnSubmit;
    String selectedUid;
    User selectedUser;
    boolean isCurrentUser = false;
    private DatabaseService databaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        databaseService = DatabaseService.getInstance();

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);

        selectedUid = getIntent().getStringExtra("USER_UID");
        String currentUid = FirebaseAuth.getInstance().getUid();
        isCurrentUser = currentUid != null && currentUid.equals(selectedUid);
        isCurrentUser = true;

        Log.d(TAG, "Selected user: " + selectedUid);

        // Initialize the EditText fields
        etUserFname = findViewById(R.id.etUserFname);
        etUserLname = findViewById(R.id.etUserLname);
        etUserPhone = findViewById(R.id.etUserPhone);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPassword = findViewById(R.id.tvUserPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        showUserProfile();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            updateUserProfile();
            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }
        if (v==btnGoBack) {
            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }
    }

    private void showUserProfile() {
        // Get the user data from database
        databaseService.getUser( new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                selectedUser = user;
                // Set the user data to the EditText fields
                etUserFname.setText(user.getFname());
                etUserLname.setText(user.getLname());
                etUserPhone.setText(user.getPhone());
                tvUserEmail.setText(user.getEmail());
                tvUserPassword.setText(user.getPassword());

                // Update display fields
                String displayName = user.getFname() + " " + user.getLname();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error getting user profile", e);
            }
        });

        // disable the EditText fields if the user is not the current user
        if (!isCurrentUser) {
            tvUserEmail.setEnabled(false);
            tvUserPassword.setEnabled(false);
        } else {
            tvUserEmail.setEnabled(true);
            tvUserPassword.setEnabled(true);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void updateUserProfile() {
        if (selectedUser == null) {
            Log.e(TAG, "User not found");
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get the updated user data from the EditText fields
        String firstName = etUserFname.getText().toString();
        String lastName = etUserLname.getText().toString();
        String phone = etUserPhone.getText().toString();
        String email = tvUserEmail.getText().toString();
        String password = tvUserPassword.getText().toString();


        // Update the user object
        selectedUser.setFname(firstName);
        selectedUser.setLname(lastName);
        selectedUser.setPhone(phone);
        selectedUser.setEmail(email);
        selectedUser.setPassword(password);

        updateUserInDatabase(selectedUser);
        // Update the user data in the authentication
        Log.d(TAG, "Updating user profile");
        Log.d(TAG, "Selected user UID: " + selectedUser.getId());
        Log.d(TAG, "Is current user: " + isCurrentUser);
        Log.d(TAG, "User email: " + selectedUser.getEmail());
        Log.d(TAG, "User password: " + selectedUser.getPassword());

    }

    private void updateUserInDatabase(User user) {
        Log.d(TAG, "Updating user in database: " + user.getId());
        databaseService.updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void result) {
                Log.d(TAG, "User profile updated successfully");
                Toast.makeText(UserProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                showUserProfile(); // Refresh the profile view
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error updating user profile", e);
                Toast.makeText(UserProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}