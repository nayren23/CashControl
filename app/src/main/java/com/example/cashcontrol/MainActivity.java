package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnConnexion ;
    private Button btnInscription ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnConnexion = findViewById(R.id.auth_button_login);
        this.btnInscription = findViewById(R.id.auth_button_register);

        this.btnConnexion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this , ConnexionActivity.class);
            startActivity(intent);
        });

        this.btnInscription.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this , InscriptionActivity.class);
            startActivity(intent);
        });
    }
}
