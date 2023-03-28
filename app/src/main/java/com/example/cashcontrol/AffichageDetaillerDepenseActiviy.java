package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BDD.DatabaseDepense;
import modele.Category;
import modele.Depense;

public class AffichageDetaillerDepenseActiviy extends AppCompatActivity {


    private ListView listDepense;
    private TextView nombreDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_detailler_depense_activity);

        DatabaseDepense databaseDepense = new DatabaseDepense(this);


        Intent intent = getIntent();
        String infoCategorie = intent.getStringExtra("infoCategorie");

        this.listDepense = findViewById(R.id.listeDepense);
        this.nombreDepense = findViewById(R.id.depenseParCateorie);

        //Creation d'une map
        Map<String, Integer> categories = new HashMap<>();
        categories.put(Category.ALIMENTATION_RESTAURATION.getLabel(), 0);
        categories.put(Category.ACHAT_SHOPPING.getLabel(), 1);
        categories.put(Category.LOISIRS_SORTIES.getLabel(), 2);
        categories.put(Category.ABONNEMENT.getLabel(), 3);
        categories.put(Category.TRANSPORTS_AUTO.getLabel(), 4);
        categories.put(Category.DIVERS.getLabel(), 5);
        categories.put(Category.IMPOTS.getLabel(), 6);
        categories.put(Category.LOGEMENT.getLabel(), 7);
        categories.put(Category.SANTE.getLabel(), 8);

        int idCategorie = categories.get(infoCategorie);
        System.out.println("voici la valeur : " + idCategorie);

        //il faudrait que la fonction dans la bdd prend un enum
        this.nombreDepense.setText("Nombre de dépense pour " + infoCategorie  + ": " + databaseDepense.getDepenseCount());//changer par une methode countparUsercategores

        List<Depense> depenseList = databaseDepense.getAllDepense();//changer par categorie avec un parametre

        List<String> listeNomDepense = new ArrayList<String>();

        for (Depense depense: depenseList){
            listeNomDepense.add(depense.getDepenseId() +"\t Montant : " + depense.getMontant() + " €");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listeNomDepense);
        listDepense.setAdapter(arrayAdapter);
    }

}
