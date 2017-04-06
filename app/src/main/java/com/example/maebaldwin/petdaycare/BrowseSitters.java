package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by maebaldwin on 3/7/17.
 */

// Update my comments

public class BrowseSitters extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_sitters);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    /*

    // This method is supposed to dynamically hide/show options menuItems
    // Throwing a null pointer

    public boolean onPrepareOptionsMenu(Menu menu){
        menu.getItem(R.id.PetProfile).setVisible(false);
        return true;
    }
    */

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


