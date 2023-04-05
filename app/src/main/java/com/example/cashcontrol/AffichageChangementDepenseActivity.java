package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import BDD.DatabaseDepense;
import modele.Depense;

public class AffichageChangementDepenseActivity  extends AppCompatActivity {

    private EditText card_description;
    private EditText card_prix;
    private EditText card_date;

    private Button button_sauvegarder;
    private DatabaseDepense databaseDepense;

    private Depense depenseAmodifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_changement_depense);

        //On recuperer les extra de l'ancienne activité
        Intent intent = getIntent();
        int idDepense  = intent.getIntExtra("idDepense",-1);
        System.out.println("l'id de la depense est : " + idDepense);

        //Récrupération des Widgets
        this.card_description = findViewById(R.id.card_description);
        this.card_prix = findViewById(R.id.card_prix);
        this.card_date = findViewById(R.id.card_date);
        this.button_sauvegarder = findViewById(R.id.button_sauvegarder);

        this.databaseDepense = new DatabaseDepense(this);
        this.depenseAmodifier = databaseDepense.getDepense(idDepense);

        this.card_description.setText(depenseAmodifier.getDescriptionDepense());
        this.card_prix.setText(Double.toString(depenseAmodifier.getMontant()));
        this.card_date.setText(depenseAmodifier.getDate());

        button_sauvegarder.setOnClickListener(view -> {
            miseAJourDepense(idDepense);
        });

    }

    private void miseAJourDepense(int idDepense){
        try {
            String date = card_date.getText().toString();
            Double montant = Double.valueOf(card_prix.getText().toString());
            String description = card_description.getText().toString();

            Depense nouvelleDepense = new Depense();
            nouvelleDepense.setDepenseId(idDepense);
            nouvelleDepense.setDescriptionDepense(description);
            nouvelleDepense.setMontant(montant);
            nouvelleDepense.setDate(date);

            //rajouter la catégorie a modifier si jamais
            System.out.println("voici la nv depense" + nouvelleDepense);
            databaseDepense.updateDepense(nouvelleDepense);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

}
