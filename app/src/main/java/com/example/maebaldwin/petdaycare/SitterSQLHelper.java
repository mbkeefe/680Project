package com.example.maebaldwin.petdaycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maebaldwin on 4/12/17.
 */

public class SitterSQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "petdaycare5.db";
    public static final int DATABASE_VERSION = 4;

    public static final String T_SITTERS = "sitters";
    public static final String SITTER_NAME = "sitter";
    public static final String LOC = "loc";
    public static final String DESC = "desc";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String KEY_ID = "id integer primary key autoincrement";
    public static final String CREATE_SITTERS = "CREATE TABLE sitters ("
            + KEY_ID + "," + SITTER_NAME + " text,"
            + LOC + " text," + DESC + " text," + LAT + " text," + LON + " text)";
    public static final String DROP_SITTERS = "DROP TABLE IF EXISTS " + T_SITTERS;

    public static final String T_SERVICES = "services";
    public static final String SITTER_ID = "sitterID";
    public static final String SERVICE_NAME = "name";
    public static final String SERVICE_FEE = "fee";
    public static final String CREATE_SERVICES = "CREATE TABLE services ("
            + KEY_ID + "," + SITTER_ID + " text," + SERVICE_NAME + " text," + SERVICE_FEE + " integer)";
    public static final String DROP_SERVICES = "DROP TABLE IF EXISTS " + T_SERVICES;

    //TODO Create Service table and figure out how to join 

    private ContentValues values;
    private ArrayList<BrowseSitters.Sitter> sitterList;
    private Cursor cursor;
    private String tag = "JoyPet: " + getClass();


    public SitterSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = CREATE_SITTERS;
        String sql2 = CREATE_SERVICES;
        db.execSQL(sql);
        Log.d(tag, "onCreate: " + sql);
        db.execSQL(sql2);
        Log.d(tag, "onCreate: " + sql2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) return;

        Log.d(tag, "onUpgrade: Version = " + newVersion);
        db.execSQL(DROP_SITTERS +";" + DROP_SERVICES);
        onCreate(db);
    }

    public void addSitter(BrowseSitters.Sitter item) {
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(SITTER_NAME, item.getName());
        values.put(LOC, item.getLoc());
        values.put(DESC,item.getDesc());
        values.put(LAT, item.getLat());
        values.put(LON, item.getLon());
        db.insert(T_SITTERS, null, values);
        Log.d(tag, item.getName() + " added");
        db.close();
    }

    public void deleteSitter(BrowseSitters.Sitter item, BrowseSitters.Sitter newItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(SITTER_NAME, newItem.getName());
        db.update(T_SITTERS, values, SITTER_NAME + "=?", new String[]{item.getName()});
        Log.d(tag, item.getName() + " updated");
        db.close();
    }

    public void updateSitter(BrowseSitters.Sitter item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(T_SITTERS, SITTER_NAME + "=?", new String[]{item.getName()});
        Log.d(tag, item.getName() + " deleted");
        db.close();
    }

    public ArrayList<BrowseSitters.Sitter> getSitterList() {

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(T_SITTERS,
                new String[]{SITTER_NAME, LOC, DESC, LAT, LON},
                null, null, null, null, SITTER_NAME);

        //write contents of Cursor to list
        int count = 0;
        sitterList = new ArrayList<BrowseSitters.Sitter>();
        while (cursor.moveToNext()) {
            count++;
            String name = cursor.getString(cursor.getColumnIndex(SITTER_NAME));
            String loc = cursor.getString(cursor.getColumnIndex(LOC));
            String desc = cursor.getString(cursor.getColumnIndex(DESC));
            String lat = cursor.getString(cursor.getColumnIndex(LAT));
            String lon = cursor.getString(cursor.getColumnIndex(LON));
            sitterList.add(new BrowseSitters.Sitter(name, loc, desc, lat, lon));
        }
        Log.d(tag, "Sitters Found: " + Integer.toString(count));
        ;
        db.close();
        return sitterList;

    }

    public void addService(BrowseSitters.Service item) {
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(SERVICE_NAME, item.getService());
        values.put(SITTER_ID, item.getSitter());
        values.put(SERVICE_FEE, item.getFee());
        db.insert(T_SERVICES, null, values);
        Log.d(tag, item.getService() + " service added for " + item.getSitter());
        db.close();
    }

    public ArrayList<BrowseSitters.Service> getSitterServices(BrowseSitters.Sitter sitter) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + T_SERVICES + " WHERE " +
                SITTER_ID + "='" + sitter.getName() + "'";
        cursor = db.rawQuery(query, null);

        //write contents of Cursor to list
        ArrayList<BrowseSitters.Service> sitterServices = new ArrayList<BrowseSitters.Service>();
        while (cursor.moveToNext()) {
            String sitterName = cursor.getString(cursor.getColumnIndex(SITTER_ID));
            String service = cursor.getString(cursor.getColumnIndex(SERVICE_NAME));
            int fee = cursor.getInt(cursor.getColumnIndex(SERVICE_FEE));
            sitterServices.add(new BrowseSitters.Service(sitterName,service,fee));
        }
        Log.d(tag, sitterServices.size() + " Services found for  " + sitter.getName());
        db.close();
        return sitterServices;
    }

    public BrowseSitters.Sitter getSitterByName (String sitterName){
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + T_SITTERS + " WHERE " +
                SITTER_NAME + " = " + sitterName,null);
        String name = cursor.getString(cursor.getColumnIndex(SITTER_NAME));
        String loc = cursor.getString(cursor.getColumnIndex(LOC));
        String desc = cursor.getString(cursor.getColumnIndex(DESC));
        String lat = cursor.getString(cursor.getColumnIndex(LAT));
        String lon = cursor.getString(cursor.getColumnIndex(LON));
        BrowseSitters.Sitter sitter = new BrowseSitters.Sitter(name,loc,desc,lat,lon);
        Log.d(tag,"Found: " + sitter.toString());
        db.close();
        return sitter;

    }


        public ArrayList<BrowseSitters.Sitter> getSittersByService (String selectedService){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + SITTER_NAME + "," + LOC + "," + DESC + ","+  LAT + "," + LON + "," + SERVICE_FEE +
                " FROM "+ T_SITTERS +
                " JOIN " + T_SERVICES + " ON " + T_SITTERS+"."+SITTER_NAME+" = " + T_SERVICES + "."+SITTER_ID;
        String groupBy = " GROUP BY " + SITTER_NAME;

        // If selected services is "All", don't include a where clause
        // Otherwise, use the selected service in Where

        if (selectedService.equals("All Services"))
            query = query + groupBy;
        else
            query = query + " WHERE " + SERVICE_NAME + " = '" + selectedService + "'" + groupBy
                    + " ORDER BY " + SERVICE_FEE ;

        cursor = db.rawQuery(query, null);

        ArrayList<BrowseSitters.Sitter> sittersByService = new ArrayList<BrowseSitters.Sitter>();
        while (cursor.moveToNext()) {
            String sitterName = cursor.getString(cursor.getColumnIndex(SITTER_NAME));
            String loc = cursor.getString(cursor.getColumnIndex(LOC));
            String desc = cursor.getString(cursor.getColumnIndex(DESC));
            String lat = cursor.getString(cursor.getColumnIndex(LAT));
            String lon = cursor.getString(cursor.getColumnIndex(LON));
            int fee = cursor.getInt(cursor.getColumnIndex(SERVICE_FEE));

            if(selectedService.equals("All Services"))
                // Use constructor with no fee if all services are selected
                sittersByService.add(new BrowseSitters.Sitter(sitterName,loc,desc,lat,lon));
            else
                sittersByService.add(new BrowseSitters.Sitter(sitterName,loc,desc,lat,lon,fee));
        }

        Log.d(tag, sittersByService.size() + " sitters offer " + selectedService + " services");
        db.close();
        return sittersByService;
    }



    public void addTestData() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_SITTERS);
        db.execSQL(CREATE_SITTERS);
        db.execSQL(DROP_SERVICES);
        db.execSQL(CREATE_SERVICES);

        this.addSitter(new BrowseSitters.Sitter("Mae", "Natick","Best pet sitter of all time","42.2775","-71.3468"));
        this.addSitter(new BrowseSitters.Sitter("Joe", "Waltham","I'll come to you!","42.3465","-71.2456"));
        this.addSitter(new BrowseSitters.Sitter("Jimmy", "Waltham","Fenced in backyard","42.3549","-71.2817"));
        this.addSitter(new BrowseSitters.Sitter("Tina", "Waltham","Animal Lover!","42.3316","-71.2181"));
        this.addSitter(new BrowseSitters.Sitter("Tyler", "Waltham","Ferret enthusiast","42.3532","-71.2997"));
        this.addSitter(new BrowseSitters.Sitter("Gail", "Waltham","Small dogs only please!","42.3811","-71.2199"));
        this.addSitter(new BrowseSitters.Sitter("Alice", "Waltham","Dogs only","42.3541","-71.2999"));
        this.addSitter(new BrowseSitters.Sitter("Stephanie", "Waltham","Crazy cat lady","42.1811","-71.4199"));
        this.addSitter(new BrowseSitters.Sitter("Monica", "Waltham","Crazy dog lady","42.2811","-71.5199"));
        this.addSitter(new BrowseSitters.Sitter("Peter", "Waltham","I like turtles","42.2811","-71.5199"));

        this.addService(new BrowseSitters.Service("Mae","Grooming",20));
        this.addService(new BrowseSitters.Service("Mae","Pet Sitting",35));
        this.addService(new BrowseSitters.Service("Mae","Dog Walking",20));
        this.addService(new BrowseSitters.Service("Mae","Day Care",40));
        this.addService(new BrowseSitters.Service("Mae","Training",30));
        this.addService(new BrowseSitters.Service("Joe","Pet Sitting",45));
        this.addService(new BrowseSitters.Service("Joe","Day Care",50));
        this.addService(new BrowseSitters.Service("Tina","Grooming",20));
        this.addService(new BrowseSitters.Service("Tina","Day Care",60));
        this.addService(new BrowseSitters.Service("Tina","Pet Sitting",35));
        this.addService(new BrowseSitters.Service("Tina","Grooming",20));
        this.addService(new BrowseSitters.Service("Tyler","Dog Walking",20));
        this.addService(new BrowseSitters.Service("Tyler","Pet Sitting",20));
        this.addService(new BrowseSitters.Service("Gail","Dog Walking",15));
        this.addService(new BrowseSitters.Service("Jimmy","Pet Sitting",30));
        this.addService(new BrowseSitters.Service("Alice","Day Care",60));
        this.addService(new BrowseSitters.Service("Alice","Pet Sitting",35));
        this.addService(new BrowseSitters.Service("Stephanie","Grooming",20));
        this.addService(new BrowseSitters.Service("Stephanie","Dog Walking",20));
        this.addService(new BrowseSitters.Service("Monica","Grooming",15));
        this.addService(new BrowseSitters.Service("Monica","Day Care",14));
        this.addService(new BrowseSitters.Service("Peter","Training",26));

        db.close();

    }
}
