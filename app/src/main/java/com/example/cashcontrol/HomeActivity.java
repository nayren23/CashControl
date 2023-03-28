package com.example.cashcontrol;

import android.graphics.Color;
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

        DatabaseDepense databaseDepense = new DatabaseDepense(this);
        databaseDepense.createDefaultDepenseIfNeed();

        //récupérer l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant= getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour verifier si la case n'est pas null

        System.out.println(" depense la"+databaseDepense.getDepense(0));
        ArrayList<Depense> depenses_Utilisateur = new ArrayList<>();
        ArrayList<Depense> getAllDepense = new ArrayList<>();

        System.out.println("getAllDepense " +  databaseDepense.getAllDepense());



        depenses_Utilisateur = databaseDepense.getDepensesUtilisateurCategorie(0);

        this.sommeDepensesParCategorie = calculSommeDepensesParCategorie(depenses_Utilisateur);


        System.out.println("affichage de la liste sommeDepensesParCategorie " +  sommeDepensesParCategorie);

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


        PieDataSet camembertDataSet = new PieDataSet(depenseUser, "Depense Utilisateurs");
        camembertDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        camembertDataSet.setValueTextColor(Color.BLACK);
        camembertDataSet.setValueTextSize(20f);
        camemberDepense.setCenterTextSize(16f);


        PieData cameData = new PieData(camembertDataSet);

        camemberDepense.setData(cameData);

        camemberDepense.getDescription().setEnabled(false);
        camemberDepense.setCenterText("Vos Dépenses");
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

            System.out.println("mais nannn" + sommeDepensesParCategorie);

        // Retourne le tableau de sommes des dépenses par catégorie
        return sommeDepensesParCategorie;
    }


}
