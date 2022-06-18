package com.birthdaymanager.core;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends AppCompatActivity {
    private static final int DISPLAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(LoadingActivity.this,
                        RegisterActivity.class);

                LoadingActivity.this.startActivity(intent);
                LoadingActivity.this.finish();
            }
        }, DISPLAY_TIME);
    }
}