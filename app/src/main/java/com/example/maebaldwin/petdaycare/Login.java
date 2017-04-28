package com.example.maebaldwin.petdaycare;
//Test COmment
//test comment 2

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Created by maebaldwin on 3/7/17.
 */

public class Login extends Activity {
    private ImageView catdog;
    private EditText userName;
    private EditText password;
    private Button login;
    private Button createAccount;
    AccountSQLHelper helper;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        createAccount = (Button)findViewById(R.id.createAccount);

        helper = new AccountSQLHelper(this);
        helper.addTestData();

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Account user;

                // Returns the user that matches entry
                user = helper.findAccountByEmail(userName.getText().toString());

                // Check to make sure account exists
                if(user.getEmail()==null){
                    Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                }

                // Go to home page if account exists
                else {
                    Intent mainPage = new Intent(Login.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    mainPage.putExtras(b);
                    startActivity(mainPage);
                }


            }
        });

        // Call create account activity
        createAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createUser = new Intent(Login.this, CreateUserAccount.class);
                startActivity(createUser);

            }
        });

    }

}
