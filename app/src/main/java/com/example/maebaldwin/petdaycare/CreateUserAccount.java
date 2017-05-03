package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

/**
 * Created by Eugenia on 4/26/17.
 */

public class CreateUserAccount extends Activity{

    private EditText enter_email, enter_pwd, confirm_pwd, user_name, town,phone ;
    private Button submit;
    private AccountSQLHelper helper;
    private Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        enter_email = (EditText)findViewById(R.id.email);
        enter_pwd = (EditText)findViewById(R.id.pwd);
        user_name = (EditText)findViewById(R.id.user_name);
        town = (EditText)findViewById(R.id.town);
        phone = (EditText)findViewById(R.id.phone);
        submit = (Button)findViewById(R.id.submit);

        helper = new AccountSQLHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  String email = enter_email.getText().toString();

                //Make sure all fields are filled in before adding account
                //Should add validation for each field
                  if (email.length()>0 && user_name.length()>0 && phone.length()>0
                          && town.length()>0) {
                      account = new Account(
                              user_name.getText().toString(),
                              phone.getText().toString(),
                              enter_email.getText().toString(),
                              town.getText().toString()
                      );

                      //Insert into DB
                      helper.addAccount(account);

                      // Go to Main page
                      Intent intent = new Intent(CreateUserAccount.this, MainActivity.class);
                      Bundle b = new Bundle();
                      b.putSerializable("user",account);
                      intent.putExtras(b);
                      startActivity(intent);


                  }

                  else
                      Toast.makeText(getApplicationContext(),
                              "Enter an email address",
                              Toast.LENGTH_LONG).show();
            }

          }

        );

    }

}
