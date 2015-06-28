package com.dajo.whazzapp.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dajo.whazzapp.R;

public class SignupActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phoneNumber = extras.getString("PHONE_NUMBER");
        }
    }

}
