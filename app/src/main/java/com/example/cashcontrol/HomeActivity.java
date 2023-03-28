package com.example.cashcontrol;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import BDD.DatabaseDepense;
import modele.Categorie;
import modele.Depense;

public class HomeActivity extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur
    private int id_Utilisateur_Courant;

    private ArrayList<Double> sommeDepensesParCategorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //On creer les depenses
        DatabaseDepense databaseDepense = new DatabaseDepense(this);
        databaseDepense.createDefaultDepenseIfNeed();

        //récupérer l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour verifier si la case n'est pas null

        ArrayList<Depense> depenses_Utilisateur = new ArrayList<>();

        //On recupere toutes les depenses de l'utilisateur depuis la BDD
        depenses_Utilisateur = databaseDepense.getDepensesUtilisateurCategorie(this.id_Utilisateur_Courant);

        //On fait la somme des depenses par catégories
        this.sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);

        //Creation du Camembert
        PieChart camemberDepense = findViewById(R.id.camembert);

        ArrayList<PieEntry> depenseUser = new ArrayList<>();

        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(0)).intValue(),"Alimentation & Restauration"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(1)).intValue(),"Achat & Shopping"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(2)).intValue(),"Loisirs & Sorties"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(3)).intValue(),"Abonnement"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(4)).intValue(),"Transports & auto"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(5)).intValue(),"Divers"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(6)).intValue(),"Impôts"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(7)).intValue(),"Logement"));
        depenseUser.add(new PieEntry((this.sommeDepensesParCategorie.get(8)).intValue(),"Santé"));


        //On change quelque parametre
        PieDataSet camembertDataSet = new PieDataSet(depenseUser, "Dépense Utilisateurs");
        camembertDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        camembertDataSet.setValueTextColor(Color.BLACK); //Couleur des valeurs numériques
        camembertDataSet.setValueTextSize(20f);
        camemberDepense.setCenterTextSize(16f);

        PieData cameData = new PieData(camembertDataSet);
        //On set les Data au diagramme
        camemberDepense.setData(cameData);

        camemberDepense.getDescription().setEnabled(false);
        int sommeDepenseMois = calculerSommeDepenses(depenses_Utilisateur);
        camemberDepense.setCenterText("" +(sommeDepenseMois) +" €");
        camemberDepense.setCenterTextSize(20f);
        camemberDepense.setBackgroundColor(Color.GRAY);
        camemberDepense.animate();
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
        for (int i = 0; i < depenses_Utilisateur.size(); i++) {
            sommeDepensesParCategorie.add(0.0);
        }

        // Parcours du tableau de dépenses et calcul des sommes par catégorie
        for (Depense depense : depenses_Utilisateur) {
            int indiceCategorie = depense.getCategorieId();
            double somme = sommeDepensesParCategorie.get(indiceCategorie) + depense.getMontant();
            sommeDepensesParCategorie.set(indiceCategorie, somme);
        }

        // Retourne le tableau de sommes des dépenses par catégorie
        return sommeDepensesParCategorie;
    }

    public int calculerSommeDepenses(ArrayList<Depense> depenses) {
        int somme = 0;
        for (Depense depense : depenses) {
            somme += depense.getMontant();
        }
        return somme;
    }
}
