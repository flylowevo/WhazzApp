package com.dajo.whazzapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dajo.whazzapp.R;
import com.dajo.whazzapp.adapters.ViewPagerAdapter;
import com.dajo.whazzapp.utils.Constants;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.UserService;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    UserService mUserService;
    SharedPreferences mPreferences;
    public static String sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserService = App42API.buildUserService();
        mPreferences = getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE);

        sessionID = mPreferences.getString("sessionID", null);

        if (sessionID == null) {
            navigateToLogin();
            Log.d(TAG, sessionID + "null or empty");
        } else {
            // TODO: Do something here?
            Log.d(TAG, sessionID);
        }

        // Creating the Toolbar and setting it as the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set its PagerAdapter so that it can display items
        ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity.this, getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_logout:
                mUserService.logout(sessionID, new App42CallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        App42Response app42response = (App42Response) o;
                        Log.d(TAG, app42response.toString());
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.remove("sessionID");
                        editor.apply();
                        navigateToLogin();
                    }

                    @Override
                    public void onException(Exception e) {
                        // TODO: Handle logout failure
                        Log.d(TAG, e.getMessage());
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToLogin() {

        // Launch the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}