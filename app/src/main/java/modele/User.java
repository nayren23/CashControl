package modele;

import java.io.Serializable;

public class User implements Serializable {

    private int UserId;
    private String nom;
    private String prenom;
    private String email;
    private String mot_de_passe;
    private String cheminimage;
    private String numerotelephone;


    public User() {
        this.UserId++;
    }


    public User(int userId, String nom, String prenom, String email, String mot_de_passe, String cheminimage,  String numerotelephone) {
        UserId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.cheminimage = cheminimage;
        this.numerotelephone = numerotelephone;
    }


    public User(int userId, String nom, String prenom, String email, String mot_de_passe,  String numerotelephone) {
        UserId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.numerotelephone = numerotelephone;
    }


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getCheminimage() {
        return cheminimage;
    }

    public void setCheminimage(String cheminimage) {
        this.cheminimage = cheminimage;
    }

    public String getNumerotelephone() {
        return numerotelephone;
    }

    public void setNumerotelephone(String numerotelephone) {
        this.numerotelephone = numerotelephone;
    }
}
