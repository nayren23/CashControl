package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import modele.Depense;

public class MainActivity extends AppCompatActivity {
    private Button btnConnexion ;
    private Button btnInscription ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnConnexion = findViewById(R.id.auth_button_login);
        this.btnInscription = findViewById(R.id.auth_button_register);

        this.btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , ConnexionActivity.class);
                startActivity(intent);
            }
        });

        this.btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , InscriptionActivity.class);
                startActivity(intent);
            }
        });

    }
}
