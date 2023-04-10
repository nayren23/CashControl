package com.example.cashcontrol;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;



public class SmsActivity extends AppCompatActivity {


    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private static final String LOG_TAG = "AndroidExample";

    protected Handler handler;

    protected int sommeDepenseMois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (handler == null)
            handler = FourniseurHandler.creerHandler();

    }

    protected void askPermissionAndSendSMS() {
        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) { // 23
            // Check if we have send SMS permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.SEND_SMS);
            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                handler.post(() -> {

                    this.requestPermissions(
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSION_REQUEST_CODE_SEND_SMS

                    );
                });

                return;
            }
        }
        // Execute sendSMS_by_smsManager on a background thread
            sendSMS_by_smsManager();

    }



    protected void sendSMS_by_smsManager()  {
        String phoneNumber = "+15555215554";
        String message = "Bonjour, ce mois ci vous avez dépenser  : " + sommeDepenseMois;

        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message
            FournisseurExecutor.creerExecutor().execute(()-> {

                        smsManager.sendTextMessage(phoneNumber,
                                null,
                                message,
                                null,
                                null);
                    });

            Log.i( LOG_TAG,"Your sms has successfully sent!");
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
                        Toast.LENGTH_LONG).show();
            });

            System.out.println("point bloquage ezuyfg ");


        } catch (Exception ex) {
            Log.e( LOG_TAG,"Your sms has failed...", ex);
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Your sms has failed... " + ex.getMessage(),
                        Toast.LENGTH_LONG).show();
            });
            ex.printStackTrace();
        }
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_SEND_SMS: {
                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i( LOG_TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
                    // Execute askPermissionAndSendSMS on a background thread
                        askPermissionAndSendSMS();


                }
                // Cancelled or denied.
                else {
                    Log.i( LOG_TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    /*// When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_SEND_SMS) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
*/

}
