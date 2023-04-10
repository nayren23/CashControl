package com.example.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import BDD.DatabaseDepense;
import utilitaires.FourniseurHandler;
import utilitaires.FournisseurExecutor;
import utilitaires.Enum_Categories;
import modele.Depense;
import utilitaires.DepenseElement;

public class AffichageDetaillerDepenseActiviy extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID";
    private ListView listDepense;
    private TextView nombreDepense;
    private CardView cardAffichageTotal;
    private TextView card_title;
    private TextView card_subtitle;
    private int id_Utilisateur_Courant;
    private int idCategorie;
    private String infoCategorie;
    private ArrayList<Depense> depenseList;
    private DatabaseDepense databaseDepense;
    private String[] dateSelectionner;
    private ArrayAdapter<DepenseElement> arrayAdapter;
    private Handler handler;
    private int boutonActuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_detailler_depense_activity);
        this.databaseDepense = new DatabaseDepense(this);

        if (handler == null)
            handler = FourniseurHandler.creerHandler();

        //Obtention  des Widgets
        this.listDepense = findViewById(R.id.listeDepense);
        this.nombreDepense = findViewById(R.id.depenseParCateorie);
        this.card_title = findViewById(R.id.card_title);
        this.card_subtitle = findViewById(R.id.card_subtitle);
        this.cardAffichageTotal = findViewById(R.id.card_view_Affichage_Detailler);

        // Initialize dateSelectionner to current date
        Calendar calendar = Calendar.getInstance();
        dateSelectionner = new String[]{String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};

        //récupérer l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour verifier si la case n'est pas null

        // Récupérez le Bundle qui contient les données envoyées depuis l'activité précédente
        Bundle bundle = getIntent().getBundleExtra("bundle");

        // Récupérer les tableaux à partir du bundle
        String[] tab1 = bundle.getStringArray("tableau1");
        String[] tab2 = bundle.getStringArray("tableau2");

        // Utilisez le tableau récupéré UNIQUEMENT QUAND ON SELECTIONNE LA DATE A LA MAIN
        infoCategorie = tab1[0];
        boutonActuel = Integer.parseInt(tab1[1]);
        dateSelectionner[0] = tab2[0];
        dateSelectionner[1] = tab2[1];
        dateSelectionner[2] = tab2[2];

        //On recupere l'id de la categorie a partir du String
        this.idCategorie = Enum_Categories.categories.get(infoCategorie);

        //Threads pour ne pas bloquer le thread principale
        FournisseurExecutor.creerExecutor().execute(() -> {
            refreshActivity();
        });

        //Pour modifier sa dépense
        listDepense.setOnItemClickListener((adapterView, view, i, l) -> {
            int position = listDepense.getPositionForView(view);
            int idDepense = arrayAdapter.getItem(position).getId();
            Intent intent1 = new Intent(AffichageDetaillerDepenseActiviy.this, AffichageChangementDepenseActivity.class);
            intent1.putExtra("idDepense", idDepense);
            startActivity(intent1);
        });

        listDepense.setOnItemLongClickListener((parent, view, position, id) -> {
            int idDepense = arrayAdapter.getItem(position).getId();
            String nomDepense = arrayAdapter.getItem(position).getDepense().getDescriptionDepense();
            Double montant = arrayAdapter.getItem(position).getDepense().getMontant();

            // Créer une boîte de dialogue avec un message et un bouton OK
            AlertDialog.Builder builder = new AlertDialog.Builder(AffichageDetaillerDepenseActiviy.this);
            builder.setMessage("Voulez-vous vraiment supprimer la dépense suivante: " + nomDepense + ".\nD'un montant de : " + montant + "€ ?")
                    .setPositiveButton("OUI", (dialog, which) -> {
                        // Ajout ici le code pour supprimer l'élément de la liste

                        //Threads pour ne pas bloquer le thread principale
                        FournisseurExecutor.creerExecutor().execute(() -> {
                            //on supprime la depense de la BDD
                            databaseDepense.deleteDepense(idDepense);
                            //On recharge les données de l'activité
                            refreshActivity();
                            //Le andler.post(() doit etre fait dans le execute
                            handler.post(() -> {
                                Toast.makeText(getApplicationContext(), "Votre dépense " + nomDepense + " a été supprimée avec succès 😋", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> {
                        //Bouton Annulé
                        Toast.makeText(getApplicationContext(), "Suppression de la dépense annulée", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }

    /**
     * L'encapsuler dans un thread
     * Met à jour l'affichage de l'activité en récupérant les nouvelles données de la base de données
     * et en les affichant sur les composants graphiques de l'activité.
     * Cette méthode est appelée à chaque fois que l'utilisateur revient sur l'activité.
     *
     * @return void
     */
    private void refreshActivity() {
        switch (boutonActuel) {
            case 0://jour
                depenseList = databaseDepense.getDepensesParUserIdEtJourActuelAndIdCategorie(id_Utilisateur_Courant,idCategorie);
                break;
            case 1://semaine
                depenseList = databaseDepense.getDepensesSemaineActuelleAndIdCategorie(id_Utilisateur_Courant,idCategorie);
                break;
            case 2://mois
                depenseList = databaseDepense.getDepensesByUserIdAndCurrentMonthAndIdCategorie(id_Utilisateur_Courant,idCategorie);
                break;
            case 3://année
                depenseList = databaseDepense.getDepensesByUserIdAndCurrentYearAndIdCategorie(id_Utilisateur_Courant,idCategorie);
                break;
            case 4://choix date
                //On  met au bon format par ex un 2 sera changer en 02 (c'est pour la requetes SQL)
                String jour = dateSelectionner[0];
                String mois = dateSelectionner[1];
                String annee = dateSelectionner[2];
                depenseList = databaseDepense.getDepensesParUserIdDateCompleteAndIdCategorie(id_Utilisateur_Courant, jour, mois, annee,idCategorie);
                break;
        }
        double sommeDepenseCat = Depense.calculerSommeDepenses(depenseList);
        int nombreDepense =depenseList.size();

        //Tout ce qui a bseoin de toucher à la UI va dans le post
        handler.post(() -> {
            //Changement du texte des composants en fonction du nombre de depense
            if (nombreDepense <= 1 && nombreDepense >= 0) {
                this.nombreDepense.setText(nombreDepense + " " + "dépense liée à " + this.infoCategorie);
            } else {
                this.nombreDepense.setText(nombreDepense + " " + "dépenses liées à " + this.infoCategorie);
            }

            this.card_title.setText("Montant global des dépenses");
            this.card_subtitle.setText("Total: " + sommeDepenseCat + " €");

            //On recupere la liste des dépenses pour la catégories choisit

            List<DepenseElement> elements = new ArrayList<>();
            for (Depense depense : depenseList) {
                elements.add(new DepenseElement(depense, depense.getDepenseId()));
            }
            this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elements);

            listDepense.setAdapter(arrayAdapter);
        });
    }

    @Override
    protected void onResume () {
        super.onResume();

        if (handler == null)
            handler = FourniseurHandler.creerHandler();
        //on fait les opérations de la BDD dans un Threads
        FournisseurExecutor.creerExecutor().execute(() -> {
            refreshActivity();
        });
    }
}