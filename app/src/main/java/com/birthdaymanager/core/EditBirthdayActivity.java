package com.birthdaymanager.core;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.birthdaymanager.util.DBUtils;
import com.birthdaymanager.util.ValidationUtils;

import java.util.Calendar;

public class EditBirthdayActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnAddBday, btnDeleteBday;
    EditText addBdayName, editBdayIdeas;
    DBUtils db;
    ValidationUtils valid;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_birthday);
        db = new DBUtils(this);
        valid = new ValidationUtils();

        initDatePicker();

        dateButton = findViewById(R.id.edit_date_picker);

        addBdayName = findViewById(R.id.edit_name);
        editBdayIdeas = findViewById(R.id.edit_bday_ideas);

        btnAddBday = findViewById(R.id.btn_edit_add_bday);
        btnAddBday.setOnClickListener(this);

        btnDeleteBday = findViewById(R.id.btn_edit_delete_bday);
        btnDeleteBday.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        String bid = extras.getString("bid");
        Cursor birthdayData = db.getBirthdayWithBID(bid);
        if(birthdayData.getCount() == 0) {
            return;
        }
        while(birthdayData.moveToNext()) {
            uid = birthdayData.getString(birthdayData.getColumnIndexOrThrow("uid"));
            String dbName = birthdayData.getString(birthdayData.getColumnIndexOrThrow("name"));
            String dbIdeas = birthdayData.getString(birthdayData.getColumnIndexOrThrow("ideas"));
            String dbDate = birthdayData.getString(birthdayData.getColumnIndexOrThrow("date"));

            addBdayName.setText(dbName);
            editBdayIdeas.setText(dbIdeas);
            dateButton.setText(dbDate);
        }
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, R.style.AlertDialogCustom, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


    @Override
    public void onClick(View view) {
        if(view.equals(btnAddBday)) {
            String name = addBdayName.getText().toString();
            if (!valid.validateName(name)) {
                Toast.makeText(this, "Enter name of a minimum of 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            String editBdayIdeasText = editBdayIdeas.getText().toString();
            Bundle extras = getIntent().getExtras();
            String bid = extras.getString("bid");

            String dateText = dateButton.getText().toString();
            String[] splitDate = dateText.split(" ");
            String newDate = splitDate[0];
            if(Integer.parseInt(newDate)>=0 && Integer.parseInt(newDate)<=9 && !(newDate.substring(0,1).equals("0"))) {
                newDate = "0" + splitDate[0];
            }
            String dbDate = newDate + " " + splitDate[1] + " " + splitDate[2];


            String dbName = name.substring(0, 1).toUpperCase() + name.substring(1);

            Boolean isUpdated = db.updateBirthday(bid, dbName, dbDate, editBdayIdeasText, uid);
            if (isUpdated) {
                Toast.makeText(this, "Successfully edited birthday", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(this, HomeActivity.class);
                it.putExtra("uid", uid);
                startActivity(it);
            } else {
                Toast.makeText(this, "Could not edit birthday", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.equals(btnDeleteBday)) {
            Bundle extras = getIntent().getExtras();
            String bid = extras.getString("bid");
            Boolean isInserted = db.deleteBirthday(bid);
            if (isInserted) {
                Toast.makeText(this, "Successfully deleted birthday", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(this, HomeActivity.class);
                it.putExtra("uid", uid);
                startActivity(it);
            } else {
                Toast.makeText(this, "Could not delete birthday", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
