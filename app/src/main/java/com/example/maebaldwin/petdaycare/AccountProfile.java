package com.example.maebaldwin.petdaycare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pengchong on 4/25/17.
 */

public class AccountProfile extends Activity{

    private AccountSQLHelper helper;
    private SQLiteDatabase db;
    private ContentValues values;
    private Cursor cursor;
    private Account myAccount;

    private TextView text_user_id, text_user_name;
    private EditText edit_email, edit_phone_num, edit_hometown;
    private Button btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_profile);
        // get the user_id

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        myAccount = (Account) b.getSerializable("user");


        helper = new AccountSQLHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            Log.d("AccountProfile", "Create database failed");
        }

        // set the account profile edit text and button
        text_user_id = (TextView) findViewById(R.id.user_id);
        text_user_name = (TextView) findViewById(R.id.user_name);
        text_user_id.setText(Integer.toString(myAccount.getId()));
        text_user_name.setText(myAccount.getName());

        edit_email = (EditText) findViewById(R.id.email);
        edit_phone_num = (EditText) findViewById(R.id.phone);
        edit_hometown = (EditText) findViewById(R.id.hometown);
        edit_email.setText(myAccount.getEmail());
        edit_phone_num.setText(myAccount.getPhone());
        edit_hometown.setText(myAccount.getHometown());

        btn_update = (Button) findViewById(R.id.update);

        // update account profile
        //TODO fix update
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account newAccount = new Account(myAccount.getName(),
                        edit_phone_num.getText().toString(),
                        edit_email.getText().toString(),
                        edit_hometown.getText().toString());

                // Save new account information to the existing account, and update the database
                myAccount = helper.updateAccount(myAccount.getName(), newAccount);
                Toast.makeText(getApplicationContext(),"Account updated!",Toast.LENGTH_SHORT).show();
            }
        });

        // go back to main activity
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent back = new Intent(AccountProfile.this, MainActivity.class);
        Bundle b1 = new Bundle();
        b1.putSerializable("user",myAccount);
        back.putExtras(b1);
        this.startActivity(back);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null)
            db.close();
    }
}
