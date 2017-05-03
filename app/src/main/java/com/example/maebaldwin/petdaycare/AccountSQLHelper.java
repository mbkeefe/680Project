package com.example.maebaldwin.petdaycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pengchong on 4/25/17.
 */

public class AccountSQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "account_profile.db";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "users";
    public static final String KEY_NAME = "user_name";
    public static final String KEY_ID = "user_id integer primary key autoincrement";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone_num";
    public static final String KEY_HOMETOWN = "hometown";
    public static final String CREATE_TABLE = "CREATE TABLE users ("
            + KEY_ID + "," + KEY_NAME + " text," + KEY_EMAIL + " text,"
            + KEY_PHONE + " text," + KEY_HOMETOWN + " text);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private ContentValues values;
    private ArrayList<Account> accountList;
    private Cursor cursor;
    private String tag = "JoyPet: " + getClass();

    public AccountSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = CREATE_TABLE;
        Log.d(tag, "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion) return;

        Log.d(tag, "onUpgrade: Version = " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // add account into database, use for create account
    public void addAccount(Account item) {
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_EMAIL, item.getEmail());
        values.put(KEY_PHONE, item.getPhone());
        values.put(KEY_HOMETOWN, item.getHometown());
        db.insert(TABLE_NAME, null, values);
        Log.d(tag, item.getName() + " added: " + item.getEmail());
        db.close();
    }

    //update Account profile in database
    public Account updateAccount(String userName, Account newItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                KEY_EMAIL + " = '" + newItem.getEmail() + "', " +
                KEY_PHONE + " = '" + newItem.getPhone() + "', " +
                KEY_HOMETOWN + " = '" + newItem.getHometown() +
                "' WHERE " + KEY_NAME + " = '" + userName + "'";
        Log.d(tag,"Updated " + newItem);
        cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
        return newItem;

    }

    //delete animal from database
    public void deleteAccount(Account item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + "=?", new String[] {item.getName()});
        Log.d(tag, item.getName() + " deleted");
        db.close();
    }

    // get account
    public Account getAccount(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME,
                new String[] {KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_HOMETOWN},
                KEY_ID + " = ?", new String [] {Integer.toString(user_id)}, null, null, KEY_NAME);
        String user_name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
        String phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
        String hometown = cursor.getString(cursor.getColumnIndex(KEY_HOMETOWN));
        Log.d(tag,"Found account for " + user_id + ": " + user_name);
        return new Account(user_name, phone, email, hometown);

    }

    public Account findAccountByEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL
                + " = '" + email + "'";
        cursor = db.rawQuery(query, null);

        Account user;
        if(cursor.moveToNext()){
            String user_name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
            String hometown = cursor.getString(cursor.getColumnIndex(KEY_HOMETOWN));
            user = new Account(user_name, phone, email, hometown);
            Log.d(tag, "Found account for " + email);
        }
        // Return empty user - Validation in login will check if name is empty
        else {
            user = new Account();
            Log.d(tag, "User " + email + "  not found");
        }
        return user;
    }

    public void addTestData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);

        this.addAccount(new Account("Tom", "111-111-1111", "tom@gmail.com", "Boston"));
        this.addAccount(new Account("Joe", "222-222-2222", "joe@gmail.com", "Cambridge"));
        this.addAccount(new Account("Luc", "333-333-3333", "luc@gmail.com", "Waltham"));
        this.addAccount(new Account("Jim", "444-444-4444", "jim@gmail.com", "Belmont"));
        this.addAccount(new Account("Eugenia", "111-111-1111", "eugenia@gmail.com", "Boston"));
        this.addAccount(new Account("Doug", "222-222-2222", "doug@gmail.com", "Cambridge"));
        this.addAccount(new Account("Pengchong", "333-333-3333", "pengchong@gmail.com", "Waltham"));
        this.addAccount(new Account("Mae", "444-444-4444", "mae@gmail.com", "Belmont"));
    }

}
