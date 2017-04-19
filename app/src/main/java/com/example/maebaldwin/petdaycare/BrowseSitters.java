//This app has 21 errors because the gradle build isn't working properly!!!

package com.example.maebaldwin.petdaycare;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.Serializable;

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

// Update my comments
    // New comments to commit 2



/*
TODO: Custom adaptor for list view
TODO: Layout for SitterProfile
TODO: Book button for Sitter Profile sends SMS/Email
TODO: Logged in user data
TODO: Add photo to sitters


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

    private SitterSQLHelper helper;
    private SQLiteDatabase db;


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

        //possibly create a method for this
        helper.addTestData();
        for(int i = 0; i <helper.getSitterList().size(); i++){
            sitterAdapter.add(helper.getSitterList().get(i));
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        LatLng center = new LatLng(42.3498,-71.2251);
        map.moveCamera(CameraUpdateFactory.newLatLng(center));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));

        map.setOnMapLongClickListener(
                new OnMapLongClickListener() {
                    public void onMapLongClick(LatLng point) {
                        setContentView(R.layout.sitter_profile);
                    }
                }
        );

    }


    /*

    // This method is supposed to dynamically hide/show options menuItems
    // Throwing a null pointer

    public boolean onPrepareOptionsMenu(Menu menu){
        menu.getItem(R.id.PetProfile).setVisible(false);
        return true;
    }
    */


    // When a list item is clicked
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        Intent intent = new Intent(this, SitterProfile.class);
        Sitter sitter = sitterAdapter.getItem(position);
        ArrayList<Service> sitterServices = helper.getSitterServices(sitter);
        Bundle b = new Bundle();
        b.putSerializable("Sitter", (Serializable) sitter);
        b.putSerializable("Services",(Serializable) sitterServices);
        intent.putExtras(b);
        startActivity(intent);



    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    // When a service from the spinner is selected, the List view is updated
    // to only display sitters who offer the selected service
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        mapShowing.setText(spinAdapter.getItem(position));
        ArrayList<Integer> toRemove = new ArrayList<Integer>();

        // Always reset the list to DB copy when spinner item is clicked
        sitterAdapter.clear();
        for (int j = 0; j < helper.getSitterList().size(); j++) {
            sitterAdapter.add(helper.getSitterList().get(j));
        }

        // If something is selected besides the 'All' option at position 0,
        if (position > 0) {
            String selection = spinAdapter.getItem(position);
            // Find out which Sitters should be removed from the array based on selection
            filterList(selection);
        }
        // Add markers based on filtered List
        addMarkers(sitterArray);

    }

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

    public void filterList(String selection){
        ArrayList<Sitter> keepList = new ArrayList<Sitter>();
        keepList = helper.getSittersByService(selection);

        /*
        for (int i = 0; i < sitters.size(); i++){
            String services = sitters.get(i).getSitterServices();
            // If the selected services if not found within the sitter's service string
            // remove from sitter list
            if ((services.indexOf(selection) != -1)){
                keepList.add(sitters.get(i));
            }
        }
        */
        sitterArray.clear();

        for(int j = 0; j < keepList.size(); j++){
            sitterArray.add(keepList.get(j));
        }

        sitterAdapter.notifyDataSetChanged();
    }

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

    public class Service{
        private String service;
        private int fee;
        private String sitter;

        public Service (String sitter, String service, int fee){
            this.sitter = sitter;
            this.service = service;
            this.fee = fee;

        }
        public String getSitter(){return this.sitter;}
        public String getService(){return this.service;}
        public int getFee(){return this.fee;}

        public String toString(){return this.service + "\t$" + this.fee;}
    }


    public class Sitter {
        private String name;
        private String loc;
        private String desc;
        private String lat;
        private String lon;
        private int fee;


        public Sitter (String name, String loc, String desc, String lat, String lon){
            this.name = name;
            this.loc = loc;
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
        }

        public Sitter (String name, String loc, String desc, String lat, String lon, int fee){
            this.name = name;
            this.loc = loc;
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
            this.fee = fee;
        }


        public String getName(){return this.name;}
        public String getLoc(){return this.loc;}
        public String getDesc(){return this.desc;}
        public String getLat(){return this.lat;}
        public String getLon(){return this.lon;}
        public int getFee(){return this.fee;}

        public String toString(){
            String str;
            if (this.fee == 0)
                str = "Name: " + this.name + "\nLocation: " + this.loc + "\n" + this.desc;
            else
                str = "Name: " + this.name + "\nLocation: " + this.loc + "\n" + this.desc + "\n$" + this.fee;
                return str;

        }

    }

}


