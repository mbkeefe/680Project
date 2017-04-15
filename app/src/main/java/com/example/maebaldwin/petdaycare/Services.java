package com.example.maebaldwin.petdaycare;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by maebaldwin on 3/7/17.
 */

public class Services extends Activity {

    /*
    I created this final array here so I can access the list of services from the BrowseSitters class.
    I also created a getServiceList method at the bottom of this class.
    I had to add "Select a Service" as the first entry in the array, because I couldn't figure out how to set
    the Spinner prompt that I'm using. If having this entry here causes problems for the Services activity
    let me know and I will try and figure something else out.
    -Mae
     */

    private final String[] serviceList = {"All Services","Day Care", "Pet Sitting","Grooming","Dog Walking"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
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

            case R.id.PetProfile:
                Intent petProfile = new Intent(this,PetProfile.class);
                this.startActivity(petProfile);
                return true;

            case R.id.Reviews:
                //TODO web service call to Yelp
                Toast.makeText(getApplicationContext(),"TODO: Yelp Web Service?",Toast.LENGTH_LONG).show();

                return true;

            case R.id.BrowseSitters:
                Intent browseSitters = new Intent(this,BrowseSitters.class);
                this.startActivity(browseSitters);
                return true;
        }

        return false;

    }

    public String[] getServiceList(){
        return serviceList;
    }


}

