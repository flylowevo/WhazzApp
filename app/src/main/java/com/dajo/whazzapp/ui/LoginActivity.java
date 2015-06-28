package com.dajo.whazzapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dajo.whazzapp.R;
import com.dajo.whazzapp.utils.Constants;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;


public class LoginActivity extends Activity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    EditText mUsernameField;
    EditText mPasswordField;
    Button mLoginBtn;
    TextView mSignupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameField = (EditText) findViewById(R.id.loginNumber);
        mPasswordField = (EditText) findViewById(R.id.loginPassword);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mSignupLink = (TextView)findViewById(R.id.signupLink);

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        final UserService userService = App42API.buildUserService();

        if (tManager.getLine1Number() != null) {
            mUsernameField.setText(tManager.getLine1Number());
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             String username = mUsernameField.getText().toString().trim();
                                             String password = mPasswordField.getText().toString().trim();

                                             if (username.isEmpty() || password.isEmpty()) {
                                                 AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                 builder.setMessage(getString(R.string.error_missing_fields))
                                                         .setTitle(R.string.error_missing_field)
                                                         .setPositiveButton(android.R.string.ok, null);
                                                 AlertDialog dialog = builder.create();
                                                 dialog.show();
                                             } else {
                                                 // Login
                                                 userService.authenticate(username, password, new App42CallBack() {
                                                     @Override
                                                     public void onSuccess(Object o) {
                                                         User user = (User) o;
                                                         String sessionID = user.getSessionId();

                                                         SharedPreferences preferences = getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE);
                                                         SharedPreferences.Editor editor = preferences.edit();
                                                         editor.putString("sessionID", sessionID);
                                                         editor.apply();

                                                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                         startActivity(intent);

                                                     }

                                                     @Override
                                                     public void onException(Exception e) {
                                                         // TODO: Handle exceptions

                                                         App42Exception exception = (App42Exception) e;
                                                         int appErrorCode = exception.getAppErrorCode();

                                                         if (appErrorCode == 2001) {
                                                             // Handle here for Bad Request (The request parameters are invalid. Username already exists.)
                                                         } else if (appErrorCode == 2005) {
                                                             // Handle here for Bad Request (The request parameters are invalid. User with emailId already exists.)
                                                         } else if (appErrorCode == 1401) {
                                                             // handle here for Client is not authorized
                                                         } else if (appErrorCode == 1500) {
                                                             // handle here for Internal Server Error
                                                         } else if (appErrorCode == 2002) {
                                                             // Handle ... Username/password did not match
                                                         }
                                                         String jsonText = exception.getMessage();
                                                         Log.d(TAG, jsonText);

                                                     }
                                                 });
                                             }
                                         }
                                     }
        );

        mSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
