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



    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        return false;

    }

    public String[] getServiceList(){
        return serviceList;
    }


}

