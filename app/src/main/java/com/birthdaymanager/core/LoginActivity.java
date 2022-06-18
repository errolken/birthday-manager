package com.birthdaymanager.core;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.birthdaymanager.util.DBUtils;
import com.birthdaymanager.util.PasswordTransformer;
import com.birthdaymanager.util.ValidationUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRouteRegister;
    EditText loginEmail, loginPassword;
    DBUtils db;
    ValidationUtils valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DBUtils(this);
        valid = new ValidationUtils();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginPassword.setTransformationMethod(new PasswordTransformer());

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnRouteRegister = findViewById(R.id.btn_route_register);
        btnRouteRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnLogin)) {
            String loginEmailTxt = loginEmail.getText().toString();
            String loginPasswordTxt = loginPassword.getText().toString();
            try {
                if(!valid.validateEmail(loginEmailTxt)) {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!valid.validatePassword(loginPasswordTxt)) {
                    Toast.makeText(this, "Enter password of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor userData = db.getUserWithEmail(loginEmailTxt);
                if(userData.getCount() == 0) {
                    Toast.makeText(this, "No user found with that email", Toast.LENGTH_SHORT).show();
                }
                while(userData.moveToNext()) {
                    String dbUID = userData.getString(userData.getColumnIndexOrThrow("uid"));
                    String dbPassword = userData.getString(userData.getColumnIndexOrThrow("password"));

                    if(dbPassword.equals(loginPasswordTxt)) {
                        Intent it = new Intent(this, HomeActivity.class);
                        it.putExtra("uid", dbUID);
                        startActivity(it);
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.equals(btnRouteRegister)) {
            Intent it = new Intent(this, RegisterActivity.class);
            startActivity(it);
        }
    }
}