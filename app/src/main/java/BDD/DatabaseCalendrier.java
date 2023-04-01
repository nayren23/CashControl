package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import modele.Calendrier;

public class DatabaseCalendrier extends DatabasePrincipale {


    // Database Name
    private static final String DATABASE_NAME = "Calendrier_Manager";

    // Table name: Calendrier.
    private static final String TABLE_Calendrier = "Calendrier";

    //On creer la structure de la table
    private static final String COLUMN_ID_Calendrier ="id_utilisateur";
    private static final String COLUMN_Date_Evenement_Calendrier ="nom_utilisateur";
    private static final String COLUMN_Nom_Evenement_CALENDRIER ="prenom_utilisateur";
    private static final String COLUMN_Id_Utilisateur_CALENDRIER ="email_utilisateur";

    public DatabaseCalendrier(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_Calendrier + "("
                + COLUMN_ID_Calendrier + " INTEGER PRIMARY KEY," + COLUMN_Date_Evenement_Calendrier +
                " TEXT," + COLUMN_Nom_Evenement_CALENDRIER +
                " TEXT," + COLUMN_Id_Utilisateur_CALENDRIER +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Calendrier);

        // Create tables again
        onCreate(db);
    }

    // If Calendrier table has no data
    // default, Insert 2 records.
    public void createDefaultCalendrierIfNeed()  {
        int count = this.getCalendrierCount();
        if(count ==0 ) {
            Calendrier calendrierYassine = new Calendrier(0 , "2 avril 2023","Festival de musique local",0);
            Calendrier calendrierRayan = new Calendrier(0 , "9 septembre 2023","Lancement du dernier iPhone",1);
            Calendrier calendrierAyoub = new Calendrier(0 , "15 juillet 2023","Course de color run",2);
        }
    }

    //On ajoute un Calendrier
    public void addCalendrier(Calendrier calendrier) {
        Log.i(TAG, "MyDatabaseHelper.addCalendrier ... " + calendrier.getNom_evenement()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_Date_Evenement_Calendrier, calendrier.getId_calendrier());
        values.put(COLUMN_Nom_Evenement_CALENDRIER, calendrier.getNom_evenement());
        values.put(COLUMN_Id_Utilisateur_CALENDRIER, calendrier.getId_utilisateur());

        // Inserting Row
        db.insert(TABLE_Calendrier, null, values);

        // Closing database connection
        db.close();
    }

    public Calendrier getCalendrier(int id) {
        Log.i(TAG, "MyDatabaseHelper.getCalendrier ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Calendrier, new String[] {COLUMN_ID_Calendrier,
                        COLUMN_Date_Evenement_Calendrier, COLUMN_Nom_Evenement_CALENDRIER, COLUMN_Id_Utilisateur_CALENDRIER}, COLUMN_ID_Calendrier + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Calendrier calendrier = new Calendrier(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        return calendrier;
    }

    public List<Calendrier> getAllCalendrier() {
        Log.i(TAG, "MyDatabaseHelper.Calendrier ... " );

        List<Calendrier> calendrierList = new ArrayList<Calendrier>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Calendrier;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Calendrier calendrier = new Calendrier();
                calendrier.setId_calendrier(Integer.parseInt(cursor.getString(0)));
                calendrier.setDate_evenement((cursor.getString(1)));
                calendrier.setNom_evenement((cursor.getString(2)));
                calendrier.setId_utilisateur(Integer.parseInt(cursor.getString(3)));

                // Adding Calendrier to list
                calendrierList.add(calendrier);
            } while (cursor.moveToNext());
        }
        return calendrierList;
    }

    public int getCalendrierCount() {
        Log.i(TAG, "MyDatabaseHelper.getCalendrierCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_Calendrier;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateCalendrier(Calendrier calendrier) {
        Log.i(TAG, "MyDatabaseHelper.updateCalendrier ... "  + calendrier.getNom_evenement());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_Date_Evenement_Calendrier, calendrier.getDate_evenement());
        values.put(COLUMN_Nom_Evenement_CALENDRIER, calendrier.getId_calendrier());
        values.put(COLUMN_Id_Utilisateur_CALENDRIER, calendrier.getId_calendrier());

        // updating row
        return db.update(TABLE_Calendrier, values, COLUMN_ID_Calendrier + " = ?",
                new String[]{String.valueOf(calendrier.getId_calendrier())});
    }

    public void deleteCalendrier(Calendrier calendrier) {
        Log.i(TAG, "MyDatabaseHelper.deleteCalendrier ... " + calendrier.getNom_evenement());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Calendrier, COLUMN_ID_Calendrier + " = ?",
                new String[] { String.valueOf(calendrier.getId_calendrier()) });
        db.close();
    }
}