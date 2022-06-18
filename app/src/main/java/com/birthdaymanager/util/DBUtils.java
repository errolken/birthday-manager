package com.birthdaymanager.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {

    public DBUtils(Context context) {
        super(context, "birthdaymanager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table User(uid INTEGER primary key AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL)");
        db.execSQL("create Table Birthday(bid INTEGER primary key AUTOINCREMENT, name TEXT, date TEXT, ideas TEXT, uid INTEGER, FOREIGN KEY(uid) references User (uid))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists User");
        db.execSQL("drop Table if exists Birthday");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
//        db.execSQL("drop Table Birthday");
//        db.execSQL("create Table Birthday(bid INTEGER primary key AUTOINCREMENT, name TEXT, date TEXT, ideas TEXT, uid INTEGER, FOREIGN KEY(uid) references User (uid))");
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Inserts
    public Boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = db.insert("User", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertBirthday(String name, String date, String ideas, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("ideas", ideas);
        contentValues.put("uid", uid);
        long result = db.insert("Birthday", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Getters
    public Cursor getUserWithEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User where email=?", new String[]{email}, null);
        return cursor;
    }

    public Cursor getUserWithUID(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User where uid=?", new String[]{uid}, null);
        return cursor;
    }

    public Cursor getBirthdaysWithUID(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Birthday where uid=?", new String[]{uid}, null);
        return cursor;
    }

    public Cursor getBirthdayWithBID(String bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Birthday where bid=?", new String[]{bid}, null);
        return cursor;
    }
    // Updates
    public Boolean updateUser(String uid, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        Cursor cursor = db.rawQuery("select * from User where uid=?", new String[]{uid});
        if(cursor.getCount() > 0) {
            long result = db.update("User", contentValues, "uid=?", new String[]{uid});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateBirthday(String bid, String name, String date, String ideas,String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("ideas", ideas);
        contentValues.put("uid", uid);
        Cursor cursor = db.rawQuery("select * from Birthday where bid = ?", new String[]{bid});
        if(cursor.getCount() > 0) {
            long result = db.update("Birthday", contentValues, "bid=?", new String[]{bid});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateBirthdayWithBID(String bid, String sms_toggle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sms_toggle", sms_toggle);
        Cursor cursor = db.rawQuery("select * from Birthday where bid = ?", new String[]{bid});
        if(cursor.getCount() > 0) {
            long result = db.update("Birthday", contentValues, "bid=?", new String[]{bid});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    // Deletes
    public Boolean deleteBirthday(String bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Birthday where bid = ?", new String[]{bid});
        if(cursor.getCount() > 0) {
            long result = db.delete("Birthday", "bid=?", new String[]{bid});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
