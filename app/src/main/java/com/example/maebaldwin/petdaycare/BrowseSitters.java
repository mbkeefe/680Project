package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

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
    private ListView sitters;
    private ArrayList<String> listArray = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_sitters);


        tabs = (TabHost)findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;

        // Initialize a TabSpec for the maps tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.Map);
        spec.setIndicator("Map");
        tabs.addTab(spec);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);

        // Initialize a TabSpec for the list tab
        spec = tabs.newTabSpec("t1");
        spec.setContent(R.id.List);
        spec.setIndicator("List");
        tabs.addTab(spec);

        // For Sitter list tab
        sitters = (ListView)findViewById(R.id.listView);
        sitters.setOnItemClickListener(this);
        adapter = new ArrayAdapter<String>(this, R.layout.list, listArray);
        sitters.setAdapter(adapter);

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
}


