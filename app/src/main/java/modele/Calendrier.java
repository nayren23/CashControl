package modele;

public class Calendrier {

    private int id_calendrier;
    private String date_evenement;
    private String nom_evenement;
    private int id_utilisateur;

    public Calendrier() {
        this.id_calendrier++;
    }

    public Calendrier(int id_calendrier, String date_evenement, String nom_evenement, int id_utilisateur) {
        this.id_calendrier = id_calendrier;
        this.date_evenement = date_evenement;
        this.nom_evenement = nom_evenement;
        this.id_utilisateur = id_utilisateur;
    }

    @Override
    public String toString() {
        return "Calendrier{" +
                "id_calendrier=" + id_calendrier +
                ", date_evenement='" + date_evenement + '\'' +
                ", nom_evenement='" + nom_evenement + '\'' +
                ", id_utilisateur=" + id_utilisateur +
                '}';
    }

    //Getters et Setters
    public int getId_calendrier() {
        return id_calendrier;
    }

    public void setId_calendrier(int id_calendrier) {
        this.id_calendrier = id_calendrier;
    }

    public String getDate_evenement() {
        return date_evenement;
    }

    public void setDate_evenement(String date_evenement) {
        this.date_evenement = date_evenement;
    }

    public String getNom_evenement() {
        return nom_evenement;
    }

    public void setNom_evenement(String nom_evenement) {
        this.nom_evenement = nom_evenement;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
