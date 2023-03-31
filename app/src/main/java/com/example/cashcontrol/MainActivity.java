package com.example.cashcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import BDD.DatabaseUser;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.User;


public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //On creer le handler avec le execute
        if(handler == null)
            handler = FourniseurHandler.creerHandler();

        DatabaseUser databaseUser = new DatabaseUser(this);

        //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
        FournisseurExecutor.creerExecutor().execute(()-> {
            databaseUser.createDefaultUsersIfNeed();
        });

        int id = 0;
        //on change la valeur dans les shared preferences
        getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                .edit()
                .putInt(SHARED_PREF_USER_INFO_ID, id)
                .apply();

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}