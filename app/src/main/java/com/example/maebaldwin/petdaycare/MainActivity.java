package com.example.maebaldwin.petdaycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.app.ActionBar.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private final String[] menu = {"View Sitters","Pet Profile","Services","My Reviews"};
    private ImageView catdog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catdog = (ImageView) findViewById(R.id.catdog);
        catdog.setImageResource(R.drawable.catdog);

        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setOnItemClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.grid,
                menu);

        grid.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID){
            case R.id.logout:
                Intent logout = new Intent(this, Login.class);
                this.startActivity(logout);
                return true;

            case R.id.myAccount:
                //// TODO: 3/6/17
                return true;
        }

        return false;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String selected = menu[position];

        switch(selected){
            case "View Sitters":
                Intent browseSitters = new Intent(this,BrowseSitters.class);
                this.startActivity(browseSitters);
                break;

            case "Pet Profile":
                Intent petProfile = new Intent(this,PetProfile.class);
                this.startActivity(petProfile);
                break;

            case "Services":
                Intent services = new Intent(this,Services.class);
                this.startActivity(services);
                break;

            case "My Reviews":
                //TODO web services call to Yelp?

                Toast.makeText(getApplicationContext(),"TODO: Yelp Web Service?",Toast.LENGTH_LONG).show();

                break;

        }


    }

}
