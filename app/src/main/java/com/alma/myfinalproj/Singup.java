package com.alma.myfinalproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alma.myfinalproj.model.User;
import com.alma.myfinalproj.services.DatabaseService;

public class Singup extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack, btnSubmit;
    EditText etFname, etLname, etPhone, etEmail, etPassword;
    private static final String TAG = "RegisterActivity";
    private DatabaseService databaseService;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        databaseService=DatabaseService.getInstance();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnGoBack=findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        etFname=findViewById(R.id.etFname);
        etLname=findViewById(R.id.etLname);
        etPhone=findViewById(R.id.etPhone);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnSubmit.getId()) {
            Log.d(TAG, "onClick: Register button clicked");

            /// get the input from the user
             email = etEmail.getText().toString();
             password = etPassword.getText().toString();
            String fName = etFname.getText().toString();
            String lName = etLname.getText().toString();
            String phone = etPhone.getText().toString();



            Log.d(TAG, "onClick: Registering user...");

            /// Register user
            registerUser(fName, lName, phone, email, password);

        }

        if(v.getId() == btnGoBack.getId()){

            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }

    }






    /// Register the user
    private void registerUser(String fname, String lname, String phone, String email, String password) {
        Log.d(TAG, "registerUser: Registering user...");


        /// create a new user object
        User user = new User("kklkl", fname, lname, phone,email, password);

        Log.d(TAG, "registerUser"+ user.toString());


        createUserInDatabase(user);

    }



private void createUserInDatabase(User user) {


    SharedPreferences.Editor editor = sharedpreferences.edit();

    editor.putString("email", email);

    editor.putString("password", password);

    editor.commit();
    databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<String>() {
        @Override
        public void onCompleted(String uid) {
            Log.d(TAG, "createUserInDatabase: User created successfully");
            /// save the user to shared preferences
            user.setId(uid);

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("email", email);

            editor.putString("password", password);

            editor.commit();

            Log.d(TAG, "createUserInDatabase: Redirecting to MainActivity");
            /// Redirect to MainActivity and clear back stack to prevent user from going back to register screen
            Intent mainIntent = new Intent(Singup.this, MainActivity.class);
            /// clear the back stack (clear history) and start the MainActivity
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        }

        @Override
        public void onFailed(Exception e) {
            Log.e(TAG, "createUserInDatabase: Failed to create user", e);
            /// show error message to user
            Toast.makeText(Singup.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            /// sign out the user if failed to register


        }
    });
}
}