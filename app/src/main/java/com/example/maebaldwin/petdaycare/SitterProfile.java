package com.example.maebaldwin.petdaycare;


import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SitterProfile extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private ImageView sitterImg;
    private GoogleMap map;
    private TextView sitterInfo;
    private Button contact;
    private TextView selectedSrvs;
    private GridView serviceGrid;
    private BrowseSitters.Sitter sitter;
    private float zoom = 13.1f;
    private Bundle sitterBundle;
    private String sitterName;
    private SQLiteDatabase db;
    private SitterSQLHelper helper;
    private LatLng loc;
    private TextView sitterDesc;
    private Button clear;
    private CustomGridAdapter serviceAdapter;
    private ArrayList<BrowseSitters.Service> sitterServices = new ArrayList<BrowseSitters.Service>();
    private ArrayList<BrowseSitters.Service> selected = new ArrayList<BrowseSitters.Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_profile);


        sitterImg = (ImageView) findViewById(R.id.SitterImage);
        sitterInfo = (TextView) findViewById(R.id.SitterName);
        clear = (Button)findViewById(R.id.clear);
        contact = (Button) findViewById(R.id.Contact);
        contact.setText("Contact");
        selectedSrvs = (TextView) findViewById(R.id.SelectedSrv);
        serviceGrid = (GridView) findViewById(R.id.ServiceGrid);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.SitterMapFrag);
        mapFragment.getMapAsync(this);


        // Retrieve the sitter that was passed by BrowseSitters Activity
        Intent sitterIntent = getIntent();
        Bundle b = sitterIntent.getExtras();
        sitter = new BrowseSitters.Sitter();
        sitter = (BrowseSitters.Sitter) b.getSerializable("sitter");

        // Retrieves list of services for that sitter
        sitterServices = sitter.sitterServices();


        sitterInfo.setText(sitter.toString());

        // Use custom adapter for grid view for displaying services
        serviceAdapter = new CustomGridAdapter(this,sitterServices);
        serviceGrid.setAdapter(serviceAdapter);
        serviceGrid.setOnItemClickListener(this);

        // Clear button removes any services that have been selected
        // Button is invisible after clearing selections

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSrvs.setText("");
                selected.clear();
                clear.setVisibility(View.INVISIBLE);
            }
        });


        // Sends an email to the sitter with the selected services in the subject line
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

                email.putExtra(Intent.EXTRA_SUBJECT,"Interested in: " + selectedSrvs.getText());

                if (email.resolveActivity(getPackageManager()) != null) {
                    startActivity(email);
                }

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    public void onStart(){
        super.onStart();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
            Intent back = new Intent(this, BrowseSitters.class);
            this.startActivity(back);
            return true;
    }

    // zoom into sitter location
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        double lat = Double.parseDouble(sitter.getLat());
        double lon = Double.parseDouble(sitter.getLon());
        loc = new LatLng(lat,lon);

        LatLng center = new LatLng(42.3498,-71.2251);
        map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));

        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(loc)
                .title(sitter.getName())
        );

    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        boolean found = false;
        BrowseSitters.Service service = serviceAdapter.getItem(position);


        // Show clear button if something has been selected
        if (selected.size()==0) {
            selected.add(service);
            clear.setVisibility(View.VISIBLE);
        }

        else {
            for (int i = 0; i < selected.size() && !found; i++) {
                if ((service.getService().equals(selected.get(i).getService()))) {
                    found = true;
                }
            }
            if (!found) {
                selected.add(service);
             }

         }

         // Display which services have been selected by the user
         selectedSrvs.setText("");
         for(int j = 0; j < selected.size(); j ++){

             if (j == 0)
                 selectedSrvs.append(selected.get(j).getService());
             else
                 selectedSrvs.append(", " + selected.get(j).getService());
         }


    }


}
