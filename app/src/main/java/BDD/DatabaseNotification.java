package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import modele.Notification;

public class DatabaseNotification  extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Notification_Manager";

    // Table name: Notification
    private static final String TABLE_NOTIFICATION= "Notification";

    //On creer la structure de la table
    private static final String COLUMN_ID_NOTIFICATION ="id_notification";
    private static final String COLUMN_MESSAGE_NOTIFICATION ="message_notification";
    private static final String COLUMN_DATE_NOTIFICATION ="date_notification";
    private static final String COLUMN_ID_UTILISATEUR_NOTIFICATION ="id_utilisateur";


    public DatabaseNotification(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + COLUMN_ID_NOTIFICATION + " INTEGER PRIMARY KEY," + COLUMN_MESSAGE_NOTIFICATION +
                " TEXT," + COLUMN_DATE_NOTIFICATION +
                " TEXT," + COLUMN_ID_UTILISATEUR_NOTIFICATION +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);

        // Create tables again
        onCreate(db);
    }

    // If Notification table has no data
    // default, Insert 2 records.
    public void createDefaultNotificationIfNeed()  {
        int count = this.getNotificationCount();
        if(count ==0 ) {
            Notification Notification__saisie = new Notification(0 , "Veuillez saisir vos dépenses pour la journée en cours.","11/03/2023",0);
            Notification Notification_retard = new Notification(1 , "Attention, vous avez des dépenses en retard à saisir !","15/03/2023",1);
            Notification Notification_dépenses_trop_élevées = new Notification(2 , "Vos dépenses ont dépassé votre budget pour ce mois-ci.","20/03/2023",2);
        }
    }

    //On ajoute une Notification
    public void addNotification(Notification notification) {
        Log.i(TAG, "MyDatabaseHelper.addNotification ... " + notification.getMessage_notification()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_ID_NOTIFICATION, notification.getId_notification());
        values.put(COLUMN_MESSAGE_NOTIFICATION, notification.getMessage_notification());
        values.put(COLUMN_DATE_NOTIFICATION, notification.getDate_notification());
        values.put(COLUMN_ID_UTILISATEUR_NOTIFICATION, notification.getId_utilisateur());



        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);

        // Closing database connection
        db.close();
    }

    public Notification getNotification(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNotification ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATION, new String[] { COLUMN_ID_NOTIFICATION,
                        COLUMN_MESSAGE_NOTIFICATION,COLUMN_DATE_NOTIFICATION, COLUMN_ID_UTILISATEUR_NOTIFICATION}, COLUMN_ID_NOTIFICATION + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notification notification = new Notification(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        return notification;
    }

    public List<Notification> getAllNotification() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotification ... " );

        List<Notification> notificationList = new ArrayList<Notification>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification();
                notification.setId_notification(Integer.parseInt(cursor.getString(0)));
                notification.setMessage_notification((cursor.getString(1)));
                notification.setDate_notification((cursor.getString(2)));
                notification.setId_utilisateur(Integer.parseInt(cursor.getString(3)));

                // Adding Notification to list
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }
        return notificationList;
    }

    public int getNotificationCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotificationCountCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateNotification(Notification notification) {
        Log.i(TAG, "MyDatabaseHelper.updateNotification ... "  + notification.getMessage_notification());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_NOTIFICATION, notification.getId_notification());
        values.put(COLUMN_MESSAGE_NOTIFICATION, notification.getMessage_notification());
        values.put(COLUMN_DATE_NOTIFICATION, notification.getDate_notification());
        values.put(COLUMN_ID_UTILISATEUR_NOTIFICATION, notification.getId_utilisateur());

        // updating row
        return db.update(TABLE_NOTIFICATION, values, COLUMN_ID_NOTIFICATION + " = ?",
                new String[]{String.valueOf(notification.getId_notification())});
    }

    public void deleteNotification(Notification notification) {
        Log.i(TAG, "MyDatabaseHelper.updateNotification ... " + notification.getMessage_notification());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, COLUMN_ID_NOTIFICATION + " = ?",
                new String[] { String.valueOf(notification.getId_notification()) });
        db.close();
    }
}
