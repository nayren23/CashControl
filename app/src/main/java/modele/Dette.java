package modele;

import java.io.Serializable;
import java.util.Date;

public class Dette implements Serializable {

    private int DetteId;
    private String nom_destinataire;
    private double montant_dette;
    private Date date_echeance;


    public Dette() {
        this.DetteId++;
    }






}


