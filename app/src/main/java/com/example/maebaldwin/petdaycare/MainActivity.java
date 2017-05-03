package com.example.maebaldwin.petdaycare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.AnimationDrawable;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private final String[] menu = {"View Sitters","My Account","Services","My Reviews"};
    private ImageView catdog;
    private TextView welcome;
    private Account user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = (ImageView) findViewById(R.id.slide);
        img.setBackgroundResource(R.drawable.slid_by_slid);

        AnimationRoutine1 task1 = new AnimationRoutine1();
        AnimationRoutine2 task2 = new AnimationRoutine2();

        Timer t = new Timer();
        t.schedule(task1, 4000);
        Timer t2 = new Timer();
        t2.schedule(task2, 1000);
        welcome = (TextView)findViewById(R.id.title);


        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setOnItemClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.grid,
                menu);

        grid.setAdapter(adapter);


        // Get user

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        user = (Account) b.getSerializable("user");

        welcome.setText("Welcome, " + user.getName() + "!");

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

        }

        return false;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String selected = menu[position];

        switch(selected){
            case "View Sitters":
                Intent browseSitters = new Intent(this,BrowseSitters.class);
                Bundle b = new Bundle();
                b.putSerializable("user",user);
                browseSitters.putExtras(b);
                startActivity(browseSitters);
                break;

            case "My Account":
                Intent accountProfile = new Intent(this,AccountProfile.class);
                Bundle b1 = new Bundle();
                b1.putSerializable("user",user);
                accountProfile.putExtras(b1);
                startActivity(accountProfile);

                break;

            case "Services":
                Toast.makeText(getApplicationContext(),"Coming soon!",Toast.LENGTH_SHORT).show();
                break;

            case "My Reviews":
                Intent reviews = new Intent(this,Reviews.class);
                Bundle b2 = new Bundle();
                b2.putSerializable("user",user);
                reviews.putExtras(b2);
                this.startActivity(reviews);


                break;

        }


    }

    class AnimationRoutine1 extends TimerTask {

        @Override
        public void run() {
            ImageView img = (ImageView) findViewById(R.id.slide);
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.start();
        }
    }

    class AnimationRoutine2 extends TimerTask {

        @Override
        public void run() {
            ImageView img = (ImageView) findViewById(R.id.slide);
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.stop();
        }
    }


}
