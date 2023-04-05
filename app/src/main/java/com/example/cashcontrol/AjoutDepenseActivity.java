package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import BDD.DatabaseDepense;
import modele.Category;
import modele.Depense;

public class AjoutDepenseActivity extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur
    private Spinner listCategorie;

    private Button ajoutDepensebtn;

    private EditText montant ;

    private EditText description ;

    private int id_Utilisateur_Courant;

    private DatabaseDepense dbDepense;

    private int idCategorie ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_depense_activity);

        this.listCategorie = findViewById(R.id.category_spinner);
        this.ajoutDepensebtn = (Button) (findViewById(R.id.button_ajout_depense));
        this.montant = findViewById(R.id.edittext_montant_depense);
        this.description = (EditText) (findViewById(R.id.edittext_description_depense));
        this.dbDepense = new DatabaseDepense(this);
        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null



        listCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Récupération de l'objet sélectionné dans le Spinner
                Object selectedItem = adapterView.getItemAtPosition(position);

                // Récupération de l'ID de l'objet sélectionné
                int selectedItemId = (int) id;

                // Utilisation de l'ID pour effectuer des actions
                idCategorie = selectedItemId ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Code à exécuter lorsque aucun élément n'est sélectionné
            }
        });


        this.ajoutDepensebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Depense depense= ajouterdepense();
                Toast.makeText(getApplicationContext(), "Votre dépense " + depense.getDescriptionDepense() + " d'un montant de " + depense.getMontant() + "a été ajoutée avec succés 👍🏼 " , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AjoutDepenseActivity.this , HomeActivity.class);
                startActivity(intent);
            }
        });


    }

    private Depense ajouterdepense(){

        //On récupere les infos saisit par l'utilisateur
        int idUser = this.id_Utilisateur_Courant;
        String description =  this.description.getText().toString();
        double montant = Double.parseDouble(this.montant.getText().toString());
        int idCategorie =this.idCategorie ;
        String date ="0" ;
        Depense depense = new Depense(date,montant,idUser,idCategorie,description);

        this.dbDepense.addDepense(depense);
        return  depense;

    }

}
