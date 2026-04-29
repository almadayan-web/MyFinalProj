package com.alma.myfinalproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.alma.myfinalproj.services.DatabaseService;

public class Login extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = "LoginActivity";
    public static boolean isAdmin = false;
    Button btnSubmit, btnGoBack;
    EditText etEmail, etPassword;
    SharedPreferences sharedpreferences;
    private DatabaseService databaseService;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        databaseService = DatabaseService.getInstance();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        email = sharedpreferences.getString("email", "");
        password = sharedpreferences.getString("password", "");
        etEmail.setText(email);
        etPassword.setText(password);
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == btnSubmit.getId()) {
            Log.d(TAG, "onClick: Login button clicked");

            /// get the email and password entered by the user
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            /// log the email and password
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);

            Log.d(TAG, "onClick: Validating input...");
            /// Validate input


            Log.d(TAG, "onClick: Logging in user...");

            /// Login user
            loginUser(email, password);
        } else if (v.getId() == btnGoBack.getId()) {
            /// Navigate to Register Activity
            Intent registerIntent = new Intent(Login.this, Landing.class);
            startActivity(registerIntent);
        }
    }


    private void loginUser(String email, String password) {
        databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
            /// Callback method called when the operation is completed
            /// @param// Email  & password is logged in
            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User logged in: " + uid.toString());
                /// save the user data to shared preferences
                // SharedPreferencesUtil.saveUser(LoginActivity.this, user);
                /// Redirect to main activity and clear back stack to prevent user from going back to login screen


                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("email", email);

                editor.putString("password", password);

                editor.commit();

                if (email.contains("almaDayan@gmail.com") && password.contains("123456")) {

                    isAdmin = true;

                    Intent mainIntent = new Intent(Login.this, AdminActivity.class);
                    /// Clear the back stack (clear history) and start the MainActivity
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();

                } else {

                    Intent mainIntent = new Intent(Login.this, MainActivity.class);
                    /// Clear the back stack (clear history) and start the MainActivity
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                }
            }


            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                /// Show error message to user
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
                /// Sign out the user if failed to retrieve user data
                /// This is to prevent the user from being logged in again

            }
        });
    }
}