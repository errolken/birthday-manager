package com.birthdaymanager.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.birthdaymanager.adapter.RecyclerAdapter;
import com.birthdaymanager.model.CardModel;
import com.birthdaymanager.util.DBUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<CardModel> birthdayList;
    RecyclerAdapter adapter;
    TextView emptyView;
    DBUtils db;

    ImageButton btnSettings;
    ImageButton btnAdd;

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DBUtils(this);

        emptyView = findViewById(R.id.empty_text);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        initData();
        initRecyclerView();
        checkView();

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    private void checkView() {
        if(birthdayList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void initData() {
        birthdayList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String dbUID = extras.getString("uid");

//        db.insertBirthday("Godnon", "15 JUN", "12:00 AM", "1", dbUID);
//        db.insertBirthday("Errol", "02 OCT", "12:00 AM", "0", dbUID);
        Cursor birthdayData = db.getBirthdaysWithUID(dbUID);
        if(birthdayData.getCount() == 0) {
            return;
        }
        while(birthdayData.moveToNext()) {
            String bid = birthdayData.getString(birthdayData.getColumnIndexOrThrow("bid"));
            String homeDate = birthdayData.getString(birthdayData.getColumnIndexOrThrow("date"));
            String homeCardName = birthdayData.getString(birthdayData.getColumnIndexOrThrow("name"));

            String[] splitDate = homeDate.split(" ");
            String day = splitDate[0];
            String month = splitDate[1];

            String Stamp = new SimpleDateFormat("dd MMM yyyy").format(Calendar.getInstance().getTime());

            String testLeft = computeDaysLeft(homeDate, Stamp);
            String actualDaysLeft;
            if(Integer.parseInt(testLeft)<=0) {
                actualDaysLeft = String.valueOf(Math.abs(Integer.parseInt(testLeft)));
            } else {
                int days = Integer.parseInt(testLeft) - 365;
                actualDaysLeft = String.valueOf(Math.abs(days));
            }

            String daysLeft = actualDaysLeft + " days left ⏳";

            if(Integer.parseInt(actualDaysLeft)==0) {
                daysLeft = "Today is their birthday! \uD83E\uDD73";
            } else if(Integer.parseInt(actualDaysLeft)==1) {
                daysLeft = actualDaysLeft + " day left ⏳";
            }

            CardModel model = new CardModel(bid, day, month, homeCardName, daysLeft, R.drawable.clipboard);
            birthdayList.add(model);
        }
    }

    private String computeDaysLeft(String start_date, String end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            return Long.toString(difference_In_Days);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(birthdayList, db);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnSettings)) {
            Bundle extras = getIntent().getExtras();
            Intent it = new Intent(this, SettingsActivity.class);
            it.putExtra("uid", extras.getString("uid"));
            startActivity(it);
        }
        else if(v.equals(btnAdd)) {
            Bundle extras = getIntent().getExtras();
            Intent it = new Intent(this, AddBirthdayActivity.class);
            it.putExtra("uid", extras.getString("uid"));
            startActivity(it);
        }
    }
}