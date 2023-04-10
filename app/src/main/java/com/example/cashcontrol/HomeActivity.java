package com.example.cashcontrol;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import java.util.Date;

import BDD.DatabaseDepense;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.Category;
import modele.Depense;
import utilitaires.DateUtil;

public class HomeActivity extends AppCompatActivity implements DatePickerFragment.OnDateSetListener {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur
    private static final int PERMISSION_REQUEST_CODE = 123;

    private int id_Utilisateur_Courant;

    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<Double> sommeDepensesParCategorie;
    private ArrayList<Depense> depenses_Utilisateur;
    private DatabaseDepense databaseDepense;
    private PieChart camemberDepense;
    private Handler handler;
    private String[] dateSelectionner;
    private EditText datePicker;

    //Button
    private Button jour_button;
    private Button semaine_button;
    private Button mois_button;
    private Button annee_button;

    private Button ajouterDepenseBtn;

    private int boutonActuel;

    private Double totalDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //On creer le handler avec le execute
        if (handler == null)
            handler = FourniseurHandler.creerHandler();

        this.ajouterDepenseBtn = findViewById(R.id.btn_plus);

        // On crée les dépenses
        this.databaseDepense = new DatabaseDepense(this);

        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null

        this.jour_button = findViewById(R.id.jour_button);
        this.semaine_button = findViewById(R.id.semaine_button);
        this.mois_button = findViewById(R.id.mois_button);
        this.annee_button = findViewById(R.id.annee_button);
        this.notificationManagerCompat = NotificationManagerCompat.from(this);

        // Initialize dateSelectionner to current date
        Calendar calendar = Calendar.getInstance();
        dateSelectionner = new String[]{String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};

        this.datePicker = findViewById(R.id.date_picker);

        // On crée le camembert
        this.camemberDepense = findViewById(R.id.camembert);

        //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
        FournisseurExecutor.creerExecutor().execute(() -> {
            this.databaseDepense.createDefaultDepenseIfNeed();
            // On récupère toutes les dépenses de l'utilisateur depuis la BDD
            this.depenses_Utilisateur = databaseDepense.getDepensesUtilisateur(this.id_Utilisateur_Courant);

            // On fait la somme des dépenses par catégories
            this.sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);
            refreshActivity();
        });


        this.ajouterDepenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AjoutDepenseActivity.class);
                startActivity(intent);
            }
        });

        this.camemberDepense.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                PieEntry pieEntry = (PieEntry) entry;
                String label = pieEntry.getLabel();
                float value = pieEntry.getValue();
                Toast.makeText(getApplicationContext(), "Voici vos dépenses: " + label + " d'un montant de " + value + " €", Toast.LENGTH_SHORT).show();

                // Créer l'objet Bundle
                Bundle bundle = new Bundle();

                String[] tab1 = new String[2];
                tab1[0] = label;
                tab1[1] = String.valueOf(boutonActuel);
                String[] tab2 = dateSelectionner;

                bundle.putStringArray("tableau1", tab1);
                bundle.putStringArray("tableau2", tab2);
                //On démarre une nouvelle activité AffichageDetaillerDepenseActiviy en passant la catégorie sélectionner
                Intent intent = new Intent(HomeActivity.this, AffichageDetaillerDepenseActiviy.class);
                intent.putExtra("bundle", bundle);
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

        // Calcul de la somme des dépenses par catégorie
        for (Depense depense : depenses_Utilisateur) {
            int idCategorie = depense.getCategorieId();
            double montant = depense.getMontant();
            double somme = sommeDepensesParCategorie.get(idCategorie);
            sommeDepensesParCategorie.set(idCategorie, somme + montant);
        }
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
        switch (boutonActuel) {
            case 0://jour
                depenses_Utilisateur = databaseDepense.getDepensesParUserIdEtJourActuel(id_Utilisateur_Courant);
                break;
            case 1://semaine
                depenses_Utilisateur = databaseDepense.getDepensesSemaineActuelle(id_Utilisateur_Courant);
                break;
            case 2://mois
                depenses_Utilisateur = databaseDepense.getDepensesByUserIdAndCurrentMonth(id_Utilisateur_Courant);
                break;
            case 3://année
                depenses_Utilisateur = databaseDepense.getDepensesByUserIdAndCurrentYear(id_Utilisateur_Courant);
                break;
            case 4://choix date
                //On  met au bon format par ex un 2 sera changer en 02 (c'est pour la requetes SQL)
                String jour = dateSelectionner[0];
                String mois = dateSelectionner[1];
                String annee = dateSelectionner[2];
                depenses_Utilisateur = databaseDepense.getDepensesParUserIdDateComplete(id_Utilisateur_Courant, jour, mois, annee);
                break;
            default:
                depenses_Utilisateur = databaseDepense.getDepensesParUserIdEtJourActuel(id_Utilisateur_Courant);
                break;
        }

        // On récupère les nouvelles données de la base de données
        sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);

        int sommeDepenseMois = (int) Depense.calculerSommeDepenses(depenses_Utilisateur);

        // On met à jour le camembert avec les nouvelles données
        ArrayList<PieEntry> depenseUser = new ArrayList<>();
        for (Category category : Category.values()) {
            float sommeDepense = sommeDepensesParCategorie.get(Category.categories.get(category.getLabel())).intValue();
            if (sommeDepense > 0) { // On n'ajoute le label que si la somme est supérieure à 0
                depenseUser.add(new PieEntry(sommeDepense, category.getLabel()));
            }
        }


        PieDataSet camembertDataSet = new PieDataSet(depenseUser, "");

        //Couleurs des segments du diagramme
        int[] MATERIAL_COLORS = {
                rgb("#003366"), rgb("#007A33"), rgb("#FF0000"), rgb("#CCCCCC"), rgb("#FFA500"), rgb("#660099"), rgb("#FFC0CB"), rgb("#ADD8E6"), rgb("#FFFF00")
        };

        camembertDataSet.setColors(MATERIAL_COLORS);
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
        limiteDepenseParJour();
    }

    /**
     * Appelée lorsque l'activité est reprise après avoir été mise en pause.
     * Appelle la méthode refreshActivity() pour mettre à jour l'affichage de l'activité.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée
                limiteDepenseParJour();
            } else {
                // Permission refusée
                Toast.makeText(this, "La permission de notification est nécessaire pour afficher les alertes de dépenses élevées.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void limiteDepenseParJour() {
        FournisseurExecutor.creerExecutor().execute(() -> {
            depenses_Utilisateur = databaseDepense.getDepensesParUserIdEtJourActuel(id_Utilisateur_Courant);
            int sommeDepenseJour = (int) Depense.calculerSommeDepenses(depenses_Utilisateur);
            if (sommeDepenseJour > 100) {
                Notification notification = new NotificationCompat.Builder(this, NotificationApp.CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.logo_cashcontrol)
                        .setContentTitle("Dépenses élevées")
                        .setContentText("Vous avez dépensé plus de 100 euros aujourd'hui.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                int notificationId = 1;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                } else {
                    this.notificationManagerCompat.notify(notificationId, notification);
                }
            }
        });
    }

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

    //Liste de Fonction pour avoir la date selectionner par l'user

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

        dateSelectionner[0] =  DateUtil.getFormattedDateTimeComponent(jour);
        dateSelectionner[1] =  DateUtil.getFormattedDateTimeComponent(mois);
        dateSelectionner[2] =  DateUtil.getFormattedDateTimeComponent(annee);


        String dateSelectionner = jour + "/" + mois + "/" +  annee;
        datePicker.setText(dateSelectionner);
        this.boutonActuel = 4;
        //On refresh l'affichage
        FournisseurExecutor.creerExecutor().execute(()-> {
            refreshActivity();
        });
    }

    /**
     * Attache les listeners aux boutons pour la sélection de la date.
     * Chaque bouton envoie la vue à la méthode onClickDateButton pour le traitement de la sélection de la date.
     */
    public void listenersBoutons() {
        jour_button.setOnClickListener(this::onClickDateButton);
        semaine_button.setOnClickListener(this::onClickDateButton);
        mois_button.setOnClickListener(this::onClickDateButton);
        annee_button.setOnClickListener(this::onClickDateButton);
    }

    /**
     * Méthode appelée lorsqu'un bouton de sélection de date est cliqué.
     * Met à jour la variable infoDate avec la date sélectionnée en fonction du bouton cliqué.
     * Met à jour la variable boutonActuel avec l'identifiant du bouton cliqué.
     * Réinitialise le champ datePicker.
     * Lance la méthode refreshActivity pour rafraîchir l'affichage.
     * @param view La vue du bouton cliqué.
     */
    public void onClickDateButton(View view) {
        Button button = (Button) view;
        switch (button.getId()) {
            case R.id.jour_button:
                boutonActuel = 0;
                break;
            case R.id.semaine_button:
                boutonActuel = 1;
                break;
            case R.id.mois_button:
                boutonActuel = 2;
                break;
            case R.id.annee_button:
                boutonActuel = 3;
                break;
        }
        datePicker.setText(" ");

        // On refresh l'affichage
        FournisseurExecutor.creerExecutor().execute(this::refreshActivity);
    }
}
