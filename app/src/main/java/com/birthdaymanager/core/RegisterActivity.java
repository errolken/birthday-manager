package com.birthdaymanager.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.birthdaymanager.util.DBUtils;
import com.birthdaymanager.util.PasswordTransformer;
import com.birthdaymanager.util.SMSUtils;
import com.birthdaymanager.util.ValidationUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister, btnRouteLogin;
    EditText regName, regEmail, regPassword;
    DBUtils db;
    ValidationUtils valid;
    SMSUtils smsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DBUtils(this);
        valid = new ValidationUtils();
        smsUtils = new SMSUtils("You have successfully created an account with Birthday Manager. Please login to continue.");

        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regPassword.setTransformationMethod(new PasswordTransformer());

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        btnRouteLogin = findViewById(R.id.btn_route_login);
        btnRouteLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnRegister)) {
            String regNameTxt = regName.getText().toString();
            String regEmailTxt = regEmail.getText().toString();
            String regPasswordTxt = regPassword.getText().toString();

            try {
                if(!valid.validateName(regNameTxt)) {
                    Toast.makeText(this, "Enter name of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!valid.validateName(regEmailTxt)) {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!valid.validatePassword(regPasswordTxt)) {
                    Toast.makeText(this, "Enter password of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean isInserted = db.insertUser(regNameTxt, regEmailTxt, regPasswordTxt);
                if (isInserted) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS }, 100);
                    smsUtils.attemptSendMessage();
                    Toast.makeText(this, "Successfully registered. Please login", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, LoginActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            } catch(Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.equals(btnRouteLogin)) {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }
    }
}