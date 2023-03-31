package modele;

import java.io.Serializable;
import java.util.ArrayList;

public class Depense implements Serializable {

    private int DepenseId ;
    private String date;
    private double montant;
    private int UserId;
    private int CategorieId;

    private String descriptionDepense;

    public Depense() {
        this.DepenseId++;
    }


    public Depense(int depenseId, String date, double montant, int userId, int categorieId, String descriptionDepense) {
        DepenseId = depenseId;
        this.date = date;
        this.montant = montant;
        UserId = userId;
        CategorieId = categorieId;
        this.descriptionDepense = descriptionDepense;
    }


    public Depense(int depenseId, String date, double montant,String descriptionDepense) {
        DepenseId = depenseId;
        this.date = date;
        this.montant = montant;
        this.descriptionDepense = descriptionDepense;
    }

    /**
     * Calcule la somme des montants de toutes les dépenses passées en paramètre.
     * @param depenses La liste des dépenses pour lesquelles on veut calculer la somme des montants.
     * @return La somme des montants de toutes les dépenses passées en paramètre.
     */
    public static double calculerSommeDepenses(ArrayList<Depense> depenses) {
        double somme = 0;
        for (Depense depense : depenses) {
            somme += depense.getMontant();
        }
        return Math.round(somme * 100.0) / 100.0;
    }

    public int getDepenseId() {
        return DepenseId;
    }

    public void setDepenseId(int depenseId) {
        DepenseId = depenseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getCategorieId() {
        return CategorieId;
    }

    public void setCategorieId(int categorieId) {
        CategorieId = categorieId;
    }


    public String getDescriptionDepense() {
        return descriptionDepense;
    }

    public void setDescriptionDepense(String descriptionDepense) {
        this.descriptionDepense = descriptionDepense;
    }

    @Override
    public String toString() {
        return "Depense{" +
                "DepenseId=" + DepenseId +
                ", date='" + date + '\'' +
                ", montant=" + montant +
                ", UserId=" + UserId +
                ", CategorieId=" + CategorieId +
                ", descriptionDepense='" + descriptionDepense + '\'' +
                '}';
    }
}
