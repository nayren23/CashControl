package com.example.cashcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BDD.DatabaseDepense;
import modele.Category;
import modele.Depense;
import modele.DepenseElement;

public class AffichageDetaillerDepenseActiviy extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID";
    private ListView listDepense;
    private TextView nombreDepense;
    private CardView cardAffichageTotal;
    private  TextView card_title;
    private  TextView card_subtitle;
    private  int id_Utilisateur_Courant;
    private int idCategorie;
    private String infoCategorie;
    private  ArrayList<Depense> depenseList;
    private DatabaseDepense databaseDepense;

    private ArrayAdapter<DepenseElement> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_detailler_depense_activity);
        this.databaseDepense = new DatabaseDepense(this);

        //Obtention  des Widgets
        this.listDepense = findViewById(R.id.listeDepense);
        this.nombreDepense = findViewById(R.id.depenseParCateorie);
        this.card_title = findViewById(R.id.card_title);
        this.card_subtitle = findViewById(R.id.card_subtitle);
        this.cardAffichageTotal = findViewById(R.id.card_view_Affichage_Detailler);

        //récupérer l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour verifier si la case n'est pas null

        //On recuperer les extra de l'ancienne activité
        Intent intent = getIntent();
        this.infoCategorie = intent.getStringExtra("infoCategorie");

        //On recupere l'id de la categorie a partir du String
        this.idCategorie = Category.categories.get(infoCategorie);

        refreshActivity();

        listDepense.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int idDepense = arrayAdapter.getItem(position).getId();
                String nomDepense = arrayAdapter.getItem(position).getDepense().getDescriptionDepense();
                Double montant = arrayAdapter.getItem(position).getDepense().getMontant();

                // Créer une boîte de dialogue avec un message et un bouton OK
                AlertDialog.Builder builder = new AlertDialog.Builder(AffichageDetaillerDepenseActiviy.this);
                builder.setMessage("Voulez-vous vraiment supprimer la dépense suivante: " + nomDepense + ".\nD'un montant de : " + montant + "€ ?")
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Ajout ici le code pour supprimer l'élément de la liste

                                //on supprime la depense de la BDD
                                databaseDepense.deleteDepense(idDepense);

                                //On recharge les données de l'activité
                                refreshActivity();
                                //A voir si on laisse le finish et le start
                                //  finish();
                                //   startActivity(getIntent());
                                Toast.makeText(getApplicationContext(), "Votre dépense "+  nomDepense +  " a été supprimée avec succès 😋", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Bouton Annulé
                                Toast.makeText(getApplicationContext(),"Suppression de la dépense annulée", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

    }

    /**
     *Met à jour l'affichage de l'activité en récupérant les nouvelles données de la base de données
     * et en les affichant sur les composants graphiques de l'activité.
     * Cette méthode est appelée à chaque fois que l'utilisateur revient sur l'activité.
     * @return void
     */
    private  void refreshActivity(){
        this.depenseList = this.databaseDepense.getDepensesUtilisateurCategorie(this.id_Utilisateur_Courant,this.idCategorie);

        double sommeDepenseCat = Depense.calculerSommeDepenses(depenseList);
        int nombreDepense = databaseDepense.getDepenseCountCategorie(idCategorie);

        //Changement du texte des composants en fonction du nombre de depense
        if(nombreDepense<=1 && nombreDepense>=0){
            this.nombreDepense.setText(nombreDepense +  " "  +"dépense liée à " + this.infoCategorie );
        }
        else {
            this.nombreDepense.setText(nombreDepense +  " "  +"dépenses liées à " + this.infoCategorie );
        }

        this.card_title.setText("Montant global des dépenses");
        this.card_subtitle.setText("Total: "+sommeDepenseCat + " €");

        //On recupere la liste des dépenses pour la catégories choisit

        List<DepenseElement> elements = new ArrayList<>();
        for (Depense depense : depenseList) {
            elements.add(new DepenseElement(depense, depense.getDepenseId()));
        }
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elements);

        listDepense.setAdapter(arrayAdapter);
    }
}


