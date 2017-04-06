package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

/**
 * Created by maebaldwin on 3/7/17.
 */

public class Login extends Activity {
    private ImageView catdog;
    private EditText userName;
    private EditText password;
    private Button login;
    private Button createAccount;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

       // catdog = (ImageView)findViewById(R.id.catdog);
        //catdog.setImageResource(R.drawable.catdog);

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        createAccount = (Button)findViewById(R.id.createAccount);


        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for login button

                // TODO validate username and password against DB

                Intent mainPage = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(mainPage,111);

            }
        });

        createAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Code for createAccount button
            }
        });

    }



}
