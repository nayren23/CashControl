package com.example.cashcontrol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import BDD.DatabaseDepense;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.Category;
import modele.Depense;
import utilitaires.DateUtil;

public class HomeActivity extends AppCompatActivity implements DatePickerFragment.OnDateSetListener {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur

    private static final int REQUEST_CODE_MAIN = 23;

    private int id_Utilisateur_Courant;
    private ArrayList<Double> sommeDepensesParCategorie;
    private ArrayList<Depense> depenses_Utilisateur;
    private DatabaseDepense databaseDepense;
    private  PieChart camemberDepense;
    private Handler handler;

    private int [] dateSelectionner;
    private EditText datePicker;

    //Button

    private Button jour_button;
    private Button semaine_button;
    private Button mois_button;
    private Button annee_button;

    private int boutonActuel;

    private int infoDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //On creer le handler avec le execute
        if(handler == null)
            handler = FourniseurHandler.creerHandler();

        // On crée les dépenses
        this.databaseDepense = new DatabaseDepense(this);

        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null


        this.jour_button = findViewById(R.id.jour_button);
        this.semaine_button = findViewById(R.id.semaine_button);
        this.mois_button = findViewById(R.id.mois_button);
        this.annee_button = findViewById(R.id.annee_button);

        // Initialize dateSelectionner to current date
        Calendar calendar = Calendar.getInstance();
        dateSelectionner = new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};

        this.datePicker = findViewById(R.id.date_picker);

        // On crée le camembert
        this.camemberDepense = findViewById(R.id.camembert);

        //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
        FournisseurExecutor.creerExecutor().execute(()->{
            this.databaseDepense.createDefaultDepenseIfNeed();
            // On récupère toutes les dépenses de l'utilisateur depuis la BDD
            this.depenses_Utilisateur = databaseDepense.getDepensesUtilisateur(this.id_Utilisateur_Courant);

            // On fait la somme des dépenses par catégories
            this.sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);
            refreshActivity();
        });

        this.camemberDepense.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                PieEntry pieEntry = (PieEntry) entry;
                String label = pieEntry.getLabel();
                float value = pieEntry.getValue();
                Toast.makeText(getApplicationContext(), "Voici vos dépenses: " + label + " d'un montant de " + value + " €", Toast.LENGTH_SHORT).show();
                //On démarre une nouvelle activité AffichageDetaillerDepenseActiviy en passant la catégorie sélectionner
                Intent intent = new Intent(HomeActivity.this, AffichageDetaillerDepenseActiviy.class);
                intent.putExtra("infoCategorie", label);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {
                // do nothing
            }
        });

        setPickersFromView();
        listenersBoutons();
    }

    /**
     * Cette méthode prend en entrée un tableau de dépenses depenses_Utilisateur,
     * et retourne un tableau de sommes des dépenses par catégorie sommeDepensesParCategorie.
     * Le tableau retourné a la même longueur que le nombre de catégories possibles,
     * et chaque case correspond à l'id de la catégorie.
     * @param depenses_Utilisateur
     * @return
     */
    public ArrayList<Double> calculSommeDepensesParCategorie(ArrayList<Depense> depenses_Utilisateur) {

        // Déclaration et initialisation du tableau de sommes des dépenses par catégorie
        ArrayList<Double> sommeDepensesParCategorie = new ArrayList<>();
        for (int i = 0; i < Category.values().length; i++) {
            sommeDepensesParCategorie.add(0.0);
        }

        // Vérification que la liste de dépenses par utilisateur n'est pas nulle
        if (depenses_Utilisateur == null) {
            throw new IllegalArgumentException("La liste de dépenses par utilisateur ne peut pas être nulle");
        }

        // Parcours du tableau de dépenses et calcul des sommes par catégorie
        for (Depense depense : depenses_Utilisateur) {
            // Vérification que la dépense n'est pas nulle
            if (depense == null) {
                throw new IllegalArgumentException("La dépense ne peut pas être nulle");
            }
            int indiceCategorie = depense.getCategorieId();
            double somme = sommeDepensesParCategorie.get(indiceCategorie) + depense.getMontant();
            sommeDepensesParCategorie.set(indiceCategorie, somme);
        }


        // Retourne le tableau de sommes des dépenses par catégorie
        return sommeDepensesParCategorie;
    }

    /**
     * L'encapsuler dans un thread
     * Cette méthode permet de rafraîchir l'activité en mettant à jour le camembert des dépenses de l'utilisateur
     * avec les nouvelles données récupérées depuis la base de données.
     * Elle récupère les dépenses de l'utilisateur courant depuis la base de données, calcule la somme des dépenses par catégorie
     * et la somme totale des dépenses, puis elle met à jour le camembert avec les nouvelles données.
     * @throws SQLException si une erreur se produit lors de la récupération des données depuis la base de données
     */
    private void refreshActivity() {
        System.out.println("boutonActuel  " + boutonActuel);
        switch (boutonActuel) {
            case 0://jour
                depenses_Utilisateur = databaseDepense.getDepensesParUserIdEtJourActuel(id_Utilisateur_Courant);
                System.out.println("voici la liste retourner getDepensesParUserIdEtDayId " +  depenses_Utilisateur);

                break;
            case 1://semaine
                depenses_Utilisateur = databaseDepense.getDepensesSemaineActuelle(id_Utilisateur_Courant);
                System.out.println("voici la liste retourner getDepensesSemaineJusquauJour " +  depenses_Utilisateur);

                break;
            case 2://mois
                int dates = Integer.parseInt(DateUtil.getFormattedDateTimeComponent(infoDate));
                depenses_Utilisateur = databaseDepense.getDepensesByUserIdAndCurrentMonth(id_Utilisateur_Courant);
                System.out.println("voici la liste retourner getDepensesByUserIdAndMonthId " +  depenses_Utilisateur);

                break;
            case 3://année
                depenses_Utilisateur = databaseDepense.getDepensesByUserIdAndYear(id_Utilisateur_Courant,infoDate);
                System.out.println("voici la liste retourner getDepensesByUserIdAndYear " +  depenses_Utilisateur);
                break;
            case 4://choix date
                String jour = DateUtil.getFormattedDateTimeComponent(dateSelectionner[0]);
                String mois  = DateUtil.getFormattedDateTimeComponent(dateSelectionner[1]);
                String annee = DateUtil.getFormattedDateTimeComponent(dateSelectionner[2]);

                depenses_Utilisateur = databaseDepense.getDepensesParUserIdDateComplete(id_Utilisateur_Courant,jour, mois,annee );
                System.out.println("voici la liste retourner getDepensesParUserIdDateComplete " +  depenses_Utilisateur);
                break;
            default:
                System.out.println("je suis dans le default");
                depenses_Utilisateur = databaseDepense.getDepensesParUserIdEtJourActuel(id_Utilisateur_Courant);
                break;
        }

        // On récupère les nouvelles données de la base de données
        sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);

        int sommeDepenseMois = (int) Depense.calculerSommeDepenses(depenses_Utilisateur);

        // On met à jour le camembert avec les nouvelles données
        ArrayList<PieEntry> depenseUser = new ArrayList<>();
        for (Category category : Category.values()) {
            depenseUser.add(new PieEntry(sommeDepensesParCategorie.get(Category.categories.get(category.getLabel())).intValue(), category.getLabel()));
        }
        PieDataSet camembertDataSet = new PieDataSet(depenseUser, "Dépense Utilisateurs");
        camembertDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        camembertDataSet.setValueTextColor(Color.BLACK);
        camembertDataSet.setValueTextSize(20f);
        PieData cameData = new PieData(camembertDataSet);
        camemberDepense.setCenterText("" + sommeDepenseMois + " €");
        camemberDepense.setData(cameData);
        camemberDepense.animate();

        // post -> ajoute les instructions à la suite de celles du main thread
        handler.post(() -> {
            // Notification du main thread pour qu'il mette à jour la UI
            // tout ce qui touche à la UI doit être exécuté dans le main thread !!!
            camemberDepense.setVisibility(View.GONE);
            camemberDepense.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Appelée lorsque l'activité est reprise après avoir été mise en pause.
     * Appelle la méthode refreshActivity() pour mettre à jour l'affichage de l'activité.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(handler == null)
            handler = FourniseurHandler.creerHandler();
        //on fait les opérations de la BDD dans un Threads
        FournisseurExecutor.creerExecutor().execute(()-> {
            refreshActivity();
        });
    }

    /**
     * Fonction pour avoir la date selectionner par l'user
     */

    /**
     * Récupère les vues pour les pickers de date et les initialise en ajoutant les listeners correspondants.
     * Cette fonction doit être appelée dans la méthode onCreate de l'activité.
     */
    private void setPickersFromView() {
        datePicker.setOnClickListener(this::showDatePicker);
    }

    /**
     * Affiche la pop-up de choix de date lorsqu'on clique sur le champ de date correspondant.
     * @param view La vue correspondant au champ de date.
     */
    private void showDatePicker(@NonNull View view) {
        final DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(this.getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    /**
     *
     * Fonction appelée lorsque l'utilisateur a sélectionné une date dans la pop-up de choix de date.
     * Met à jour le champ de date avec la date sélectionnée et rafraîchit l'activité.
     * @param annee L'année sélectionnée.
     * @param mois Le mois sélectionné (janvier = 1).
     * @param jour Le jour du mois sélectionné.
     */
    @Override
    public void onDateSet(int annee, int mois, int jour) {
        dateSelectionner[0] = jour;
        dateSelectionner[1] = mois;
        dateSelectionner[2] = annee;

        String dateSelectionner = jour + "/" + mois + "/" +  annee;

        datePicker.setText(dateSelectionner);
        System.out.println("voici la date Selectionner " + dateSelectionner );
        this.boutonActuel = 4;
        FournisseurExecutor.creerExecutor().execute(()-> {

            refreshActivity();
        });    }

    public void listenersBoutons (){

        jour_button.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            infoDate = cal.get(Calendar.DAY_OF_MONTH);
            this.boutonActuel = 0;
            datePicker.setText(" ");

            FournisseurExecutor.creerExecutor().execute(()-> {

                refreshActivity();
            });
        });
        semaine_button.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            infoDate = cal.get(Calendar.DAY_OF_MONTH);
            this.boutonActuel = 1;
            datePicker.setText(" ");
            refreshActivity();

        });
        mois_button.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            infoDate = cal.get(Calendar.MONTH); // récupère le mois actuel (0 pour janvier, 1 pour février, etc.)
            this.boutonActuel = 2;
            datePicker.setText(" ");
            refreshActivity();

        });
        annee_button.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            infoDate = cal.get(Calendar.YEAR);
            this.boutonActuel = 3;
            datePicker.setText(" ");
            refreshActivity();
        });
    }
}
