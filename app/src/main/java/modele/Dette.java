package modele;

import java.io.Serializable;
import java.util.Date;

public class Dette implements Serializable {

    private int DetteId;
    private String nom_destinataire;
    private double montant_dette;
    private String date_echeance;
    private int UserId;


    public Dette() {
        this.DetteId++;
    }


    public Dette(int detteId, String nom_destinataire, double montant_dette, String date_echeance, int userId) {
        DetteId = detteId;
        this.nom_destinataire = nom_destinataire;
        this.montant_dette = montant_dette;
        this.date_echeance = date_echeance;
        UserId = userId;
    }

    public Dette(int detteId, String nom_destinataire, double montant_dette, String date_echeance) {
        DetteId = detteId;
        this.nom_destinataire = nom_destinataire;
        this.montant_dette = montant_dette;
        this.date_echeance = date_echeance;
    }


    public int getDetteId() {
        return DetteId;
    }

    public void setDetteId(int detteId) {
        DetteId = detteId;
    }

    public String getNom_destinataire() {
        return nom_destinataire;
    }

    public void setNom_destinataire(String nom_destinataire) {
        this.nom_destinataire = nom_destinataire;
    }

    public double getMontant_dette() {
        return montant_dette;
    }

    public void setMontant_dette(double montant_dette) {
        this.montant_dette = montant_dette;
    }

    public String getDate_echeance() {
        return date_echeance;
    }

    public void setDate_echeance(String date_echeance) {
        this.date_echeance = date_echeance;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }


    @Override
    public String toString() {
        return "Dette{" +
                "DetteId=" + DetteId +
                ", nom_destinataire='" + nom_destinataire + '\'' +
                ", montant_dette=" + montant_dette +
                ", date_echeance=" + date_echeance +
                ", UserId=" + UserId +
                '}';
    }
}


