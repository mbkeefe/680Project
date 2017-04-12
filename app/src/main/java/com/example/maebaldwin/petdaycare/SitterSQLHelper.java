package com.example.maebaldwin.petdaycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by maebaldwin on 4/12/17.
 */

public class SitterSQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "petdaycare.db";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "sitters";
    public static final String KEY_NAME = "name";
    public static final String LOC = "loc";
    public static final String KEY_ID = "id integer primary key autoincrement";
    public static final String CREATE_TABLE = "CREATE TABLE sitters ("
            + KEY_ID + "," + KEY_NAME + " text,"
            + LOC + " text);";

    private ContentValues values;
    private ArrayList<BrowseSitters.Sitter> sitterList;
    private Cursor cursor;

    public SitterSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String sql = CREATE_TABLE;
        Log.d("SQLiteDemo", "onCreate: " + sql);
        db.execSQL(sql);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion >= newVersion) return;

        Log.d("SQLiteDemo", "onUpgrade: Version = " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addSitter(BrowseSitters.Sitter item){
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(LOC, item.getLoc());
        db.insert(TABLE_NAME, null, values);
        Log.d("SQLiteDemo", item.getName() + " added");
        db.close();

    }

    public void deleteSitter(BrowseSitters.Sitter item, BrowseSitters.Sitter newItem){
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(KEY_NAME, newItem.getName());
        db.update(TABLE_NAME, values, KEY_NAME + "=?", new String[] {item.getName()});
        Log.d("JoyPet", item.getName() + " updated");
        db.close();
    }

    public void updateSitter(BrowseSitters.Sitter item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + "=?", new String[] {item.getName()});
        Log.d("JoyPet", item.getName() + " deleted");
        db.close();
    }

    public ArrayList<BrowseSitters.Sitter> getSitterList () {

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME,
                new String[] {KEY_NAME, LOC},
                null, null, null, null, KEY_NAME);

        //write contents of Cursor to list
        sitterList = new ArrayList<BrowseSitters.Sitter>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String loc = cursor.getString(cursor.getColumnIndex(LOC));
            sitterList.add(new BrowseSitters().new Sitter(name, loc));
        };
        db.close();
        return sitterList;

    }

}
