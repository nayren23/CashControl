package modele;

import java.io.Serializable;

public class Notification implements Serializable {

    private int id_notification;
    private String message_notification;
    private String date_notification;
    private int id_utilisateur;

    public Notification() {
        this.id_notification++;
    }

    public Notification(int id_notification, String message_notification, String date_notification, int id_utilisateur) {
        this.id_notification = id_notification;
        this.message_notification = message_notification;
        this.date_notification = date_notification;
        this.id_utilisateur = id_utilisateur;
    }




    @Override
    public String toString() {
        return "Notification{" +
                "id_notification=" + id_notification +
                ", message_notification='" + message_notification + '\'' +
                ", date_notification='" + date_notification + '\'' +
                ", id_utilisateur=" + id_utilisateur +
                '}';
    }

    //Getters Setters
    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
    }

    public String getMessage_notification() {
        return message_notification;
    }

    public void setMessage_notification(String message_notification) {
        this.message_notification = message_notification;
    }

    public String getDate_notification() {
        return date_notification;
    }

    public void setDate_notification(String date_notification) {
        this.date_notification = date_notification;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
