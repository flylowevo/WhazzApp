package com.dajo.whazzapp.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dajo.whazzapp.R;

import java.util.Random;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = SignupActivity.class.getSimpleName();

    TextView mCountryCode;
    TextView mPhoneNumber;
    TextView mPin;
    Button mSignupBtn;
    Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mCountryCode = (TextView) findViewById(R.id.signupCountryCode);
        mPhoneNumber = (TextView) findViewById(R.id.loginNumber);
        mSignupBtn = (Button) findViewById(R.id.signupBtn);
        mVerifyBtn = (Button)findViewById(R.id.verifyBtn);
        mPin = (TextView)findViewById(R.id.pinNumber);

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = mCountryCode.getText().toString().trim();
                String phoneNumber = mPhoneNumber.getText().toString().trim();

                if (countryCode.isEmpty() || phoneNumber.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage(getString(R.string.error_verification))
                            .setTitle(getString(R.string.error_missing_field))
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // TODO: Implement parse signup
                    sendVerification();
                }

            }
        });
    }

    private void sendVerification() {
        Random rnd = new Random();
        int verificationNumber = 1000 + rnd.nextInt(9000);

        String phoneNumber = mCountryCode.getText().toString().trim() + mPhoneNumber.getText().toString().trim();
        final String smsBody = String.valueOf(verificationNumber);

        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

// For when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();

                        mCountryCode.setVisibility(View.INVISIBLE);
                        mPhoneNumber.setVisibility(View.INVISIBLE);
                        mSignupBtn.setVisibility(View.INVISIBLE);
                        mVerifyBtn.setVisibility(View.VISIBLE);
                        mPin.setVisibility(View.VISIBLE);

                        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String pin = mPin.getText().toString().trim();
                                if (pin.equals(smsBody)){
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    intent.putExtra("PHONE_NUMBER", smsBody);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    builder.setMessage(getString(R.string.error_pin_message))
                                            .setTitle(getString(R.string.error_pin_title))
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

// Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
        smsManager.sendTextMessage(phoneNumber, null, smsBody, sentPendingIntent, deliveredPendingIntent);
    }
}

