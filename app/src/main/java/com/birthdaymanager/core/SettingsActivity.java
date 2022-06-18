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

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSave, btnLogout;
    EditText settingName, settingEmail, settingPassword;
    ValidationUtils valid;
    DBUtils db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DBUtils(this);
        valid = new ValidationUtils();

        settingName = findViewById(R.id.setting_name);
        settingEmail = findViewById(R.id.setting_email);
        settingPassword = findViewById(R.id.setting_password);
        settingPassword.setTransformationMethod(new PasswordTransformer());

        Bundle extras = getIntent().getExtras();
        Cursor userData = db.getUserWithUID(extras.getString("uid"));
        if(userData.getCount() == 0) {
            Toast.makeText(this, "No user found with that email", Toast.LENGTH_SHORT).show();
        }
        while(userData.moveToNext()) {
            settingName.setText(userData.getString(userData.getColumnIndexOrThrow("name")));
            settingEmail.setText(userData.getString(userData.getColumnIndexOrThrow("email")));
            settingPassword.setText(userData.getString(userData.getColumnIndexOrThrow("password")));
        }

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnSave)) {
            String settingNameTxt = settingName.getText().toString();
            String settingEmailTxt = settingEmail.getText().toString();
            String settingPasswordTxt = settingPassword.getText().toString();
            try {
                if (!valid.validateName(settingNameTxt)) {
                    Toast.makeText(this, "Enter name of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!valid.validateName(settingEmailTxt)) {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!valid.validatePassword(settingPasswordTxt)) {
                    Toast.makeText(this, "Enter password of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle extras = getIntent().getExtras();
                String uid = extras.getString("uid");
                Boolean isUpdated = db.updateUser(uid, settingNameTxt, settingEmailTxt, settingPasswordTxt);
                if (isUpdated) {
                    Toast.makeText(this, "Account successfully updated", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, HomeActivity.class);
                    it.putExtra("uid", uid);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.equals(btnLogout)) {
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }
    }
}