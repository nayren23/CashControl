package modele;

import java.io.Serializable;

public class User implements Serializable {

    private int UserId;
    private String identifiant;
    private double argent;
    private String email;
    private String mot_de_passe;
    private String cheminimage;
    private String numerotelephone;

    public User() {
        this.UserId++;
    }

    /**
     *constructeur complet
     * **/
    public User(int userId, String identifiant,double argent, String email, String mot_de_passe, String cheminimage, String numerotelephone) {
        UserId = userId;
        this.identifiant = identifiant;
        this.argent = argent;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.cheminimage = cheminimage;
        this.numerotelephone = numerotelephone;
    }

    /**
     * constructeur sans l'argent
     * @param userId
     * @param identifiant
     * @param email
     * @param mot_de_passe
     * @param cheminimage
     * @param numerotelephone
     */
    public User(int userId, String identifiant,String email, String mot_de_passe, String cheminimage, String numerotelephone) {
        UserId = userId;
        this.identifiant = identifiant;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.cheminimage = cheminimage;
        this.numerotelephone = numerotelephone;
    }

    public User(String identifiant,String email, String mot_de_passe,String photodeprofil, String numerotelephone  ) {

        this.identifiant = identifiant;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.cheminimage = photodeprofil;
        this.numerotelephone = numerotelephone;
    }

    public double getArgent() {
        return argent;
    }

    public void setArgent(double argent) {
        this.argent = argent;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
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

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + UserId +
                ", identifiant='" + identifiant + '\'' +
                ", argent=" + argent +
                ", email='" + email + '\'' +
                ", mot_de_passe='" + mot_de_passe + '\'' +
                ", cheminimage='" + cheminimage + '\'' +
                ", numerotelephone='" + numerotelephone + '\'' +
                '}';
    }
}