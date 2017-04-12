package com.example.maebaldwin.petdaycare;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by maebaldwin on 3/7/17.
 */

// Update my comments
    // New comments to commit 2


public class BrowseSitters extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {
    private TabHost tabs;
    private GoogleMap map;
    private float zoom = 11.5f;


    private ListView sitterlv;
    private ArrayList<BrowseSitters.Sitter> sitterArray = new ArrayList<Sitter>();
    private ArrayAdapter<BrowseSitters.Sitter> sitterAdapter;

    private String[] serviceArray;
    private Services services = new Services(); // Created Services object to access static list of services
    private Spinner servicesp;

    private SitterSQLHelper helper;
    private SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_sitters);


        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;

        // Initialize a TabSpec for the maps tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.Map);
        spec.setIndicator("Map");
        tabs.addTab(spec);


        //-------Spinner---------------------------------
        serviceArray = services.getServiceList(); // Get the current list of services from Service class
        servicesp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                serviceArray);

        spinAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        servicesp.setAdapter(spinAdapter);  //connect ArrayAdapter to <Spinner>

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);


        //--------tabs-----------------------------------
        // Initialize a TabSpec for the list tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.List);
        spec.setIndicator("List");
        tabs.addTab(spec);

        // For Sitter list tab
        sitterlv = (ListView) findViewById(R.id.listView);
        sitterlv.setOnItemClickListener(this);
        sitterAdapter = new ArrayAdapter<BrowseSitters.Sitter>(this, R.layout.list, sitterArray);
        sitterlv.setAdapter(sitterAdapter);


        //------------------Database--------------

        helper = new SitterSQLHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            Log.d("JoyPet", "Create database failed");
        }

        helper.addSitter(new BrowseSitters(). new Sitter("Mae","Natick"));

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

    }


    // When a spinner item is clicked
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){

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

    public class Sitter {
        private String name;
        private String loc;

        public Sitter (String name, String loc){
            this.name = name;
            this.loc = loc;
        }

        public String getName(){return this.name;}
        public String getLoc(){return this.loc;}

        public String toString(){return "Name:" + this.name + " Location: " + this.loc;}

    }

}


