package com.example.cashcontrol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur
    private int id_Utilisateur_Courant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //récupérer l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant= getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour verifier si la case n'est pas null
    

    }
}
