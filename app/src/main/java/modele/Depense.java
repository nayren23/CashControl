package modele;

import java.io.Serializable;

public class Depense implements Serializable {

    private int DepenseId ;
    private String date;
    private double montant;
    private int UserId;
    private int CategorieId;



    public Depense() {
        this.DepenseId++;
    }


    public Depense(int depenseId, String date, double montant, int userId, int categorieId) {
        DepenseId = depenseId;
        this.date = date;
        this.montant = montant;
        UserId = userId;
        CategorieId = categorieId;
    }


    public Depense(int depenseId, String date, double montant) {
        DepenseId = depenseId;
        this.date = date;
        this.montant = montant;
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


    @Override
    public String toString() {
        return "Depense{" +
                "DepenseId=" + DepenseId +
                ", date='" + date + '\'' +
                ", montant=" + montant +
                ", UserId=" + UserId +
                ", CategorieId=" + CategorieId +
                '}';
    }
}
