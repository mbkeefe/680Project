//Changed this comment 4/19 6:35PM


package com.example.maebaldwin.petdaycare;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.Serializable;
import android.os.Parcel;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by maebaldwin on 3/7/17.
 */


public class BrowseSitters extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private TabHost tabs;
    private GoogleMap map;
    private TextView mapShowing;
    private float zoom = 11.5f;


    private ListView sitterlv;
    private ArrayList<BrowseSitters.Sitter> sitterArray = new ArrayList<Sitter>();
    private CustomListAdapter sitterAdapter;

    private String[] serviceArray;
    private Services services = new Services(); // Created Services object to access static list of services
    private Spinner servicesp;
    private ArrayAdapter<String> spinAdapter;

    private static SitterSQLHelper helper;
    private SQLiteDatabase db;
    private ArrayList<Service> sitterServices = new ArrayList<Service>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_sitters);


        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;


        //-------Spinner---------------------------------
        serviceArray = services.getServiceList(); // Get the current list of services from Service class
        servicesp = (Spinner) findViewById(R.id.spinner);
        servicesp.setOnItemSelectedListener(this);
        spinAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                serviceArray);

        spinAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        servicesp.setAdapter(spinAdapter);  //connect ArrayAdapter to <Spinner>


        //--------tabs-----------------------------------
        // Initialize a TabSpec for the list tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.List);
        spec.setIndicator("List");
        tabs.addTab(spec);

        // For Sitter list tab
        sitterlv = (ListView) findViewById(R.id.listView);
        sitterlv.setOnItemClickListener(this);

        sitterAdapter = new CustomListAdapter(this, sitterArray);
        sitterlv.setAdapter(sitterAdapter);

        // For the maps tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.Map);
        spec.setIndicator("Map");
        tabs.addTab(spec);

        mapShowing = (TextView) findViewById(R.id.MapShowing);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);


        //------------------Database--------------

        helper = new SitterSQLHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            Log.d("JoyPet", "Create database failed");
        }


        helper.addTestData();
        int t = helper.getSitterList().size();
        for(int i = 0; i <helper.getSitterList().size(); i++){
            sitterAdapter.add(helper.getSitterList().get(i));
        }


    }

    // Create Options Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }


    // Map zooms in on Waltham area when ready
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        LatLng center = new LatLng(42.3498,-71.2251);
        map.moveCamera(CameraUpdateFactory.newLatLng(center));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));

        map.setOnMapLongClickListener(

                new OnMapLongClickListener() {
                    public void onMapLongClick(LatLng point) {

                    }
                }
        );

    }


    // Listener for ListView
    // Passes an intent to SitterProfile containing the sitter object that was selected

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


        Sitter sitter =  sitterAdapter.getItem(position);
       // sitterServices = helper.getSitterServices(sitter);

        Intent intent = new Intent(this, SitterProfile.class);
        Bundle b = new Bundle();
        b.putSerializable("sitter",sitter);
        intent.putExtras(b);

        startActivity(intent);
    }

    public void onNothingSelected(AdapterView<?> parent) {}




    // When a service from the spinner is selected, the List view is updated
    // to only display sitters who offer the selected service

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        mapShowing.setText(spinAdapter.getItem(position));

        String selection = spinAdapter.getItem(position);

        sitterArray.clear();
        filterList(selection);
        sitterAdapter.notifyDataSetChanged();

        // Add markers based on filtered List
        addMarkers(sitterArray);

    }


   // Main Options Menu navigation
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID) {
            case R.id.Home:
                Intent home = new Intent(this, MainActivity.class);
                this.startActivity(home);
                return true;

            case R.id.Services:
                Intent services = new Intent(this,Services.class);
                this.startActivity(services);
                return true;

            case R.id.Reviews:
                //TODO web service call to Yelp
                Toast.makeText(getApplicationContext(),"TODO: Yelp Web Service?",Toast.LENGTH_LONG).show();

                return true;

            case R.id.PetProfile:
                Intent petProfile = new Intent(this,PetProfile.class);
                this.startActivity(petProfile);
                return true;
        }

        return false;

    }



    // Filter list is called when an item in the spinner drop down is selected
    // It filters the list of service to only sitters who offer the service selected in the spinner
    public void filterList(String selection){
        ArrayList<Sitter> keepList;
        keepList = helper.getSittersByService(selection);

        for(int j = 0; j < keepList.size(); j++){
            sitterArray.add(keepList.get(j));
        }

    }



    // Adds a marker for each sitter in the passed array list
    // Called after the list of sitters is filtered so map can be updated in concert
    public void addMarkers (ArrayList<Sitter> sitters){
        LatLng coord;
        map.clear();
        for(int i = 0; i < sitters.size(); i++){
            double lat = Double.parseDouble(sitters.get(i).getLat());
            double lon = Double.parseDouble(sitters.get(i).getLon());
            coord = new LatLng(lat,lon);

            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .position(coord)
                    .title(sitters.get(i).getName())
                    .snippet("$"+ Integer.toString(sitters.get(i).getFee())));
        }

    }




    // ----------------- INNER CLASSES --------------------


    // The service class describes services offered by sitters. Each service has an associated fee
    // Services are sitter specific

    public static class Service {
        private String sitter;
        private String service;
        private int fee;


        public Service (String sitter, String service, int fee){
            this.sitter = sitter;
            this.service = service;
            this.fee = fee;
        }

        public String getSitter(){return this.sitter;}
        public String getService(){return this.service;}
        public int getFee(){return this.fee;}


}

    // Sitter Class

    public static class Sitter implements Serializable{
        private String name;
        private String loc;
        private String desc;
        private String lat;
        private String lon;
        private int fee;

        public Sitter(){}

        public Sitter (String name, String loc, String desc, String lat, String lon){
            this.name = name;
            this.loc = loc;
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
            this.fee = 0;
        }

        public Sitter (String name, String loc, String desc, String lat, String lon, int fee){
            this.name = name;
            this.loc = loc;
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
            this.fee = fee;
        }


        // Getters and Setters
        public String getName(){return this.name;}
        public String getLoc(){return this.loc;}
        public String getDesc(){return this.desc;}
        public String getLat(){return this.lat;}
        public String getLon(){return this.lon;}
        public int getFee(){return this.fee;}

        public ArrayList<Service> sitterServices (){
            ArrayList<Service> sitterServices = helper.getSitterServices(this);
            return sitterServices;
        }

        public String toString(){
            String str;
                str = "Name: " + this.name + "\nLocation: " + this.loc + "\n" + this.desc;
                return str;

        }

    }

}


