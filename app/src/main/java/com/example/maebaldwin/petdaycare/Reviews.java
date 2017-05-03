package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.*;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.Locale;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;



public class Reviews extends Activity implements AdapterView.OnItemClickListener, OnInitListener {

    private static final String TAG = "MY APP";

    private TabHost tabs;

    private TextView nameDisplay;
    private TextView addressDisplay;
    private TextView sundayDisplay;
    private TextView mondayDisplay;
    private TextView tuesdayDisplay;
    private TextView wednesdayDisplay;
    private TextView thursdayDisplay;
    private TextView fridayDisplay;
    private TextView saturdayDisplay;
    private TextView ratingDisplay;
    private TextView phoneDisplay;
    private TextView urlDisplay;

    private TextToSpeech speaker;

    private EditText enterSearchTerm;
    private EditText enterSearchAdress;

    RadioGroup group1;
    RadioGroup group2;
    RadioButton otherButton;
    RadioButton addressButton;

    Button callButton;

    Location myLocation;
    String searchTerm;
    String searchLat;
    String searchLng;

    //ArrayList to store Yelp objects and a Yelp object variable to be used to access objects in ArrayList
    ArrayList<Yelp> backgroundYelpResults = new ArrayList<Yelp>();

    Yelp yelpObject;

    //will be used to display a small amount of text at bottom of "Search" tab
    private ListView listview;
    ArrayList<String> listYelpResults = new ArrayList<String>();  //stores search results from yelp search API into Yelp objects
    ArrayAdapter<String> adapter;

    //used to collect the businessID of an object in the ListView in order to run a search on the businessAPI of Yelp!
    int pos;
    String yelpID;

    String queryString;

    private String appID = "VXbKlaxlRkGI3xE17ZTazQ";
    private String appSecret = "M5u9XxVAF7scq0Z0qaFakWWLxgKKyxtzMzOXCZgFg93f9E5acS5d5w4jNI3FMN0p";
    private String appToken = "HGBkYBy7Q60Vt8Fr_69BU6CCNjcMrkTyzdGPsoKxINMww8QvP8t7-DR6Og3X5ipjdy3iwlBH9RD2AXzVsq2jNP5I1qdIZcCf1QbY75FFJSsw0_C_4Nnj-NjH59__WHYx";
    private Account myAccount; //logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);

        //bind variables to R.id in reviews.xml
        nameDisplay = (TextView) findViewById(R.id.nameDisplay);
        addressDisplay = (TextView) findViewById(R.id.addressDisplay);
        sundayDisplay = (TextView) findViewById(R.id.sundayDisplay);
        mondayDisplay = (TextView) findViewById(R.id.mondayDisplay);
        tuesdayDisplay = (TextView) findViewById(R.id.tuesdayDisplay);
        wednesdayDisplay = (TextView) findViewById(R.id.wednesdayDisplay);
        thursdayDisplay = (TextView) findViewById(R.id.thursdayDisplay);
        fridayDisplay = (TextView) findViewById(R.id.fridayDisplay);
        saturdayDisplay = (TextView) findViewById(R.id.saturdayDisplay);
        ratingDisplay = (TextView) findViewById(R.id.ratingDisplay);
        phoneDisplay = (TextView) findViewById(R.id.phoneDisplay);
        urlDisplay = (TextView) findViewById(R.id.urlDisplay);
        group1 = (RadioGroup) findViewById(R.id.radioGroup1);
        group2 = (RadioGroup) findViewById(R.id.radioGroup2);
        otherButton = (RadioButton) findViewById(R.id.radio06);
        addressButton = (RadioButton) findViewById(R.id.radio08);
        enterSearchTerm = (EditText) findViewById((R.id.other));
        enterSearchAdress = (EditText) findViewById(R.id.reviews_address);
        enterSearchAdress.setHint("Enter city and/or street");
        speaker = new TextToSpeech(this, this);
        callButton = (Button) findViewById(R.id.callButton);

        group1.check(R.id.radio01);
        searchTerm = "Groomers";
        group2.check(R.id.radio07);

        listview = (ListView) findViewById(R.id.resultsList);
        listview.setOnItemClickListener(this);


        //Receive user from previous activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        myAccount = (Account) b.getSerializable("user");

        /*bind ArrayAdapter to ArrayList, listYelpResults.  This is where the list of search results
         * will be stored each time a Yelp search is run.  Adapter will be used to write the results
         * to the UI*/
        adapter = new ArrayAdapter<String>(this, R.layout.reviews_list, listYelpResults);
        listview.setAdapter(adapter);

        //setup TabHost
        tabs = (TabHost) findViewById(R.id.reviews_tabhost);
        tabs.setup();
        TabHost.TabSpec spec;

        //Adding "Search" Tab to TabHost
        spec = tabs.newTabSpec("searchTab");
        spec.setContent(R.id.searchTab);
        spec.setIndicator("Search");
        tabs.addTab(spec);

        //Adding "Results" Tab to TabHost
        spec = tabs.newTabSpec("resultsTab");
        spec.setContent(R.id.resultsTab);
        spec.setIndicator("Results");
        tabs.addTab(spec);

        /*  This listener method will set the search parameters for the Yelp queries based on the radio
          * button that is selected by altering the search term */
        group1.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup gp, int checkedId) {
                        RadioButton rb = (RadioButton) findViewById(checkedId);
                        searchTerm = rb.getText().toString();
                        enterSearchTerm.setText("");
                    }//onCheckedChanged
                }//OnCheckedChangeListener
        );//setOnCheckedChangeListener

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phone = Uri.parse("tel:" + phoneDisplay.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL, phone);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Reviews.this, new String[]{Manifest.permission.CALL_PHONE},
                            100);
                    return;
                }
                startActivity(callIntent);
            }
        });


//        /* get location set up */
//        //LocationManager
//        LocationManager locManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locListener = new MyLocationListener();
//
//        /*Register for location updates */
//        try {
//            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locListener);
//            myLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } catch (SecurityException e) {Log.e(TAG, "location did not set properly");}

    }//onCreate

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID){
            case R.id.back:
                Intent back = new Intent(Reviews.this, MainActivity.class);
                Bundle b1 = new Bundle();
                b1.putSerializable("user",myAccount);
                back.putExtras(b1);
                this.startActivity(back);
                return true;

        }

        return false;
    }


    /*Handler for runYelpSearch background thread message containing an ArrayList of Yelp objects.
 * Writes data from Yelp ArrayList to the adapter/ListView as Strings so that only the name
 * and rating are displayed on the UI*/
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            ArrayList<Yelp> allYelpResults = (ArrayList) msg.obj;
            String listText;

            adapter.clear();

            for (int i = 0; i < allYelpResults.size(); i++) {
                listText = allYelpResults.get(i).getName() + "\nRating: " +
                        allYelpResults.get(i).getRating().toString();
                adapter.add(listText);
            }
        }//handleMessage
    };//Handler





    /*Handler for the runYelpBusiness background thread message containing Yelp object.
     * writes data from Yelp object to UI on "Results" tab */
    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {

            Yelp yelpResult = (Yelp) msg.obj;

            Log.e(TAG, yelpResult.toString());

            //display name, address, rating, phone, and url
            nameDisplay.setText(yelpResult.getName());
            addressDisplay.setText(yelpResult.getAddress());
            ratingDisplay.setText(yelpResult.getRating().toString());
            phoneDisplay.setText(yelpResult.getPhone());
            urlDisplay.setText(yelpResult.getUrl());

            //display hours of operation
            YelpDay day = yelpResult.getDay(6);
            sundayDisplay.setText(day.getHours());
            day = yelpResult.getDay(0);
            mondayDisplay.setText(day.getHours());
            day = yelpResult.getDay(1);
            tuesdayDisplay.setText(day.getHours());
            day = yelpResult.getDay(2);
            wednesdayDisplay.setText(day.getHours());
            day = yelpResult.getDay(3);
            thursdayDisplay.setText(day.getHours());
            day = yelpResult.getDay(4);
            fridayDisplay.setText(day.getHours());
            day = yelpResult.getDay(5);
            saturdayDisplay.setText(day.getHours());
        }
    };





    /* This method is called when the "Search" button is pressed.  It sets the queryString for the
     * search and calls the thread that stats runYelpSearch. An order of operations follows:
     * 1.
     * 2.
     * 3.
     * 4.
     * 5.
     * 6.
     * 7.
     * 8.
     * 9.
     * 10.
     * 11.
     * */
    public void search(View v) {
        //this if statement sets the search term for the Yelp query if the "Other" radio button is selected
        if (otherButton.isChecked()){
            searchTerm = enterSearchTerm.getText().toString();
        }

        //This Geocoder gets the LatLng for an alternative address if the "Address" radio button is selected
        if (addressButton.isChecked()) {
            Geocoder addressLocation = new Geocoder(this);
            String addressString = enterSearchAdress.getText().toString();

            List<Address> locations = null;
            try {
                locations = addressLocation.getFromLocationName(addressString, 1);
                if (locations.size() == 0) {
                    Toast.makeText(this.getApplicationContext(), "Try again.\nexample:\nCity, State",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                LatLng searchLocationLatLng = new LatLng(locations.get(0).getLatitude(),
                        locations.get(0).getLongitude());
                searchLat = Double.toString(searchLocationLatLng.latitude);
                searchLng = Double.toString((searchLocationLatLng.longitude));
                Log.e(TAG, "searchLocationLatLng = " + searchLocationLatLng.latitude + ", " +
                        searchLocationLatLng.longitude);
            } catch (IOException e) {
                Log.e(TAG, "geocoder failed to set location");
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "Index out of bounds. Likely because" +
                        "there was no address entered.");
            }
        } else if (!addressButton.isChecked()){
            //add myLocation assignment here.
            /* get location set up */
            //LocationManager
            LocationManager locManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            LocationListener locListener = new MyLocationListener();

        /*Register for location updates */
            try {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locListener);
                myLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                searchLat = Double.toString(myLocation.getLatitude());
                searchLng = Double.toString(myLocation.getLongitude());
            } catch (SecurityException e) {Log.e(TAG, "myLocation did not set properly. Problem with LocationManager");}
        } else Log.e(TAG, "Error setting location. Check problem in else-if statement");

        //try and catch ensure that the required query parameters are entered before sending the request.
        try {
            if (searchTerm.equals(null) || searchTerm.equals("") || searchLat.equals(null) ||
                    searchLng.equals(null)) {
                Toast.makeText(this.getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NullPointerException e) {
            Toast.makeText(this.getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
            return;
        }

        queryString = "term=" + searchTerm + "&latitude=" + searchLat + "&longitude=" + searchLng;
        Log.e(TAG, "query string = " + queryString);

        Thread t = new Thread(runYelpSearch);
        t.start();
    }//search()





    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speaker.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            } else {
                Log.e(TAG, "Text to Speech has been initialized correctly");
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }//onInit()





    public void speak(String output){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
        } else speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);
    }//speak()





    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            myLocation = loc;
            Log.e(TAG, "myLocation changed to lat: " + myLocation.getLatitude() + "  lng: " + myLocation.getLongitude());

            String Text = "My current location is: " +
                    "Latitude = " + loc.getLatitude() +
                    "Longitude = " + loc.getLongitude();

            Toast.makeText(getApplicationContext(), Text,
                    Toast.LENGTH_SHORT).show();
        }//onLocationChanged()
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(),
                    "Gps Disabled", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(),
                    "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    }// End MyLocationListener





    Runnable runYelpBusiness = new Runnable() {
        @Override
        public void run() {
            StringBuilder strBuilder = new StringBuilder();
            InputStream is = null;

            String businessAPIurl = "https://api.yelp.com/v3/businesses/" + yelpID;
            Log.e(TAG, "businessAPIurl = " + yelpID);
            try {
                Log.e(TAG, "enter try for HttpURLConnectin");
                //setup connection
                URL bizURL = new URL(businessAPIurl);
                HttpsURLConnection bizConn = (HttpsURLConnection) bizURL.openConnection();

                //setup connection properties
                bizConn.setReadTimeout(10000);
                bizConn.setConnectTimeout(15000);
                bizConn.setRequestMethod("GET");
                bizConn.setRequestProperty("Authorization", "Bearer HGBkYBy7Q60Vt8Fr_69BU6CCNjcMrkTyzdGPsoKxINMww8QvP8t7-DR6Og3X5ipjdy3iwlBH9RD2AXzVsq2jNP5I1qdIZcCf1QbY75FFJSsw0_C_4Nnj-NjH59__WHYx");
                bizConn.setDoInput(true);

                //start query
                bizConn.connect();
                int responseCode1 = bizConn.getResponseCode();
                String responseMessage1 = bizConn.getResponseMessage();
                Log.e(TAG, "HTTP response code: " + responseCode1);
                Log.e(TAG, "HTTP response message: " + responseMessage1);

                //if response code not 200, end thread
                if (responseCode1 != 200) return;
                is = bizConn.getInputStream();

                BufferedReader reader1 = new BufferedReader(
                        new InputStreamReader(is));
                String line;
                while ((line = reader1.readLine()) != null) {
                    strBuilder.append(line);
                }
            }	catch(IOException e) {}

            //checks if inputStream is open and closes it
            finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch(IOException e) {}
                }
            }

            //convert stringBuilder to String
            String readJSONFeed1 = strBuilder.toString();
            Log.e(TAG, "JSONFeed1 = \n" + readJSONFeed1);

            /*convert the String, readJSONFeed, into a JSONObject and use that object to
            * create the Yelp objects that will be used to display detailed business search results*/
            try {
                JSONObject reader1 = new JSONObject(readJSONFeed1);
                /*  figure out order of operations:
                  * 1. Click ListView
                  * 2. Reference Yelp object in ListView
                  * 3. Retrieve Yelp object ID from adapter/ListView
                  * 4. pass ID to runnable thread
                  * 5. use ID as a search parameter in the businessQuery String
                  * 6. get hours and add them to the yelpObject that is passed back to the
                  *         main thread.  Do we create a new Yelp object here?
                  * 7. Pass Yelp object back to main thread.
                  * 8. Write Yelp object to UI in "Results" tab */

                /* if a business does not have hours, it will throw a JSON exception and stop
                 * executing. The catch to this exception ensures that the Yelp object in question
                 * still sends any information that has been captured in the initial search to
                 * the main thread
                 */
                try {
                    JSONArray hours = reader1.getJSONArray("hours");
                    JSONObject hoursObj = hours.getJSONObject(0);
                    JSONArray open = hoursObj.getJSONArray("open");
                    int numDays = open.length();
                    int lastDay = -1;
                    JSONObject dayObject;
                    int day;
                    int start = 0;
                    int end = 0;

                    yelpObject = backgroundYelpResults.get(pos);

                    for (int i = 0; i < numDays; i++) {
                        dayObject = open.getJSONObject(i);
                        day = dayObject.getInt("day");
                        if (day != lastDay) {
                            start = Integer.parseInt(dayObject.getString("start"));
                            end = Integer.parseInt(dayObject.getString("end"));
                        }
                        lastDay = day;
                        yelpObject.setDay(day, start, end);
                        Log.e(TAG, "day=" + day + "  start=" + start + "  end=" + end);
                    }
                } catch(JSONException e) {
                    e.getMessage();
                    e.getStackTrace();
                    yelpObject = backgroundYelpResults.get(pos);
                }
                //send values to main thread
                Message msg = handler1.obtainMessage();

                msg.obj = yelpObject;
                handler1.sendMessage(msg);

            } catch (JSONException e) {e.getMessage();
                e.printStackTrace();
            }

        }//run
    };//Runnable runYelpBusiness





    Runnable runYelpSearch = new Runnable() {
        @Override
        public void run() {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = null;

            String tokenRequestAPI;

            String searchAPIurl = "https://api.yelp.com/v3/businesses/search?" + queryString;
            Log.e(TAG, "searchAPIurl = " + searchAPIurl);
            try {
                Log.e(TAG, "enter try for HttpURLConnection");
                //set up connection
                URL url = new URL(searchAPIurl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


                //set up connection properties
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer HGBkYBy7Q60Vt8Fr_69BU6CCNjcMrkTyzdGPsoKxINMww8QvP8t7-DR6Og3X5ipjdy3iwlBH9RD2AXzVsq2jNP5I1qdIZcCf1QbY75FFJSsw0_C_4Nnj-NjH59__WHYx");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                int responseCode = conn.getResponseCode();
                String responseMessage = conn.getResponseMessage();
                Log.e(TAG, "HTTP response code: " + responseCode);
                Log.e(TAG, "HTTP response message: " + responseMessage);

                //if response code not 200, end thread
                if (responseCode != 200) return;
                inputStream = conn.getInputStream();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }	catch(IOException e) {}

            //checks if inputStream is open and closes it
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch(IOException e) {}
                }
            }

            //convert stringBuilder to String
            String readJSONFeed = stringBuilder.toString();
            Log.e(TAG, "JSONFeed = \n" + readJSONFeed);

            /*convert the String, readJSONFeed, into a JSONObject and use that object to
            * create the Yelp objects that will be used to display search results*/
            try {
                JSONObject reader = new JSONObject(readJSONFeed);

                //get the array of business objects from the response
                JSONArray businesses = reader.getJSONArray("businesses");

                int numBiz = businesses.length();

                JSONObject business;
                JSONObject address;

                backgroundYelpResults.clear();

                //for each result in list of results, create an object and add it to an ArrayList
                for (int i = 0; i < numBiz; i++){
                    business = businesses.getJSONObject(i);

                    Double rating1 = business.getDouble("rating");
                    String phone1 = business.getString("phone");
                    String name1 = business.getString("name");
                    String url1 = business.getString("url");
                    String ID1 = business.getString("id");

                    address = business.getJSONObject("location");
                    String address1 = address.getString("address1") + ", " + address.getString("city") +
                            ", " + address.getString("state") + ", " + address.getString("zip_code");

                    Yelp yelpObject = new Yelp(ID1, name1, address1, url1,phone1, rating1);

                    //add yelpObject to the ArrayList backgroundYelpResults
                    backgroundYelpResults.add(yelpObject);
                }

                //send values to main thread
                Message msg = handler.obtainMessage();
                msg.obj = backgroundYelpResults;
                handler.sendMessage(msg);

            } catch (JSONException e) {e.getMessage();
                e.printStackTrace();
            }
        }//run()
    };//Runnable()





    /* This method is called when an item on the ListView of the "Search" tab is clicked.
     * it starts a new Yelp search for more specific info on the business that was clicked
     * and displays the info in a complete profile on the "Results" tab*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        yelpID = backgroundYelpResults.get(pos).getID();
        Log.e(TAG, "ListView onItemClick() ---------- yelpID = [" + yelpID + "]");

        //use text to speech to speak the name of the business
        speak(backgroundYelpResults.get(pos).getName());

        //switch tab view
        //start thread/runnable/query
        Thread t1 = new Thread(runYelpBusiness);
        t1.start();
        tabs.setCurrentTabByTag("resultsTab");
    }
}
