package com.example.maebaldwin.petdaycare;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SitterProfile extends FragmentActivity implements OnMapReadyCallback, OnItemClickListener {

    private ImageView sitterImg;
    private GoogleMap map;
    private TextView sitterInfo;
    private Button contact;
    private TextView selectedSrvs;
    private GridView serviceGrid;
    private ArrayList<BrowseSitters.Service> services;
    private BrowseSitters.Sitter sitter;

    private final ArrayList<BrowseSitters.Service> sitterServices = new ArrayList<BrowseSitters.Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_profile);

        sitterImg = (ImageView) findViewById(R.id.SitterImage);
        sitterInfo = (TextView) findViewById(R.id.SitterInfo);
        contact = (Button) findViewById(R.id.Contact);
        selectedSrvs = (TextView) findViewById(R.id.SelectedSrv);
        serviceGrid = (GridView) findViewById(R.id.ServiceGrid);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        sitter = (BrowseSitters.Sitter)bundle.getSerializable("Sitter");
        services = (ArrayList<BrowseSitters.Service>)bundle.getSerializable("Service");


    }

    public void onMapReady(GoogleMap googleMap){


    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //append if not already selected
        // Set background color of grid item
    }


}
