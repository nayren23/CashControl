package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import modele.Dette;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseDette extends DatabasePrincipale {

    // Database Name
    private static final String DATABASE_NAME = "Dette_Manager";

    // Table name: Dette.
    private static final String TABLE_DETTE = "Dette";

    //On creer la structure de la table
    private static final String COLUMN_ID_DETTE ="id_dette";
    private static final String COLUMN_NOM_DESTINATAIRE="nom_destinataire";
    private static final String COLUMN_MONTANT_DETTE ="montant_dette";
    private static final String COLUMN_DATE_ECHEANCE ="date_echeance";
    private static final String COLUMN_ID_UTILISATEUR ="id_utilisateur";



    public DatabaseDette(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_DETTE + "("
                + COLUMN_ID_DETTE + " INTEGER PRIMARY KEY," + COLUMN_NOM_DESTINATAIRE +
                " TEXT," + COLUMN_MONTANT_DETTE +
                " TEXT," + COLUMN_DATE_ECHEANCE +
                " TEXT," +  COLUMN_ID_UTILISATEUR +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETTE);

        // Create tables again
        onCreate(db);
    }

    // If dette table has no data
    // default, Insert 2 records.
    public void createDefaultDetteIfNeed()  {
        int count = this.getDetteCount();
        if(count == 0 ) {
            Dette detteYassine = new Dette(0 , "rayan",1920000,  "01/01/2023",2);
            Dette detteRayan = new Dette(1 , "ayoub",1,  "01/01/2023",1);
            Dette detteAyoub = new Dette(2 , "yassine",195000,  "01/01/2023",0);
        }
    }

    //On ajoute un Dette
    public void addDette(Dette dette) {
        Log.i(TAG, "MyDatabaseHelper.addDette ... " + dette.getDetteId()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_NOM_DESTINATAIRE, dette.getNom_destinataire());
        values.put(COLUMN_MONTANT_DETTE, dette.getMontant_dette());
        values.put(COLUMN_DATE_ECHEANCE, dette.getDate_echeance());
        values.put(COLUMN_ID_UTILISATEUR, dette.getUserId());


        db.insert(TABLE_DETTE, null, values);

        // Closing database connection
        db.close();
    }

    public Dette getDette(int id) {
        Log.i(TAG, "MyDatabaseHelper.getDette ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DETTE, new String[] { COLUMN_ID_DETTE,
                        COLUMN_NOM_DESTINATAIRE,COLUMN_MONTANT_DETTE, COLUMN_DATE_ECHEANCE, COLUMN_ID_UTILISATEUR}, COLUMN_ID_UTILISATEUR + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Dette dette = new Dette(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)),cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        return dette;
    }

    public List<Dette> getAllDette() {
        Log.i(TAG, "MyDatabaseHelper.getAllDette ... " );

        List<Dette> detteList = new ArrayList<Dette>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DETTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Dette dette = new Dette();
                dette.setDetteId(Integer.parseInt(cursor.getString(0)));
                dette.setNom_destinataire((cursor.getString(1)));
                dette.setMontant_dette(Double.parseDouble((cursor.getString(2))));
                dette.setDate_echeance((cursor.getString(3)));
                dette.setUserId(Integer.parseInt(cursor.getString(4)));


                // Adding depense to list
                detteList.add(dette);
            } while (cursor.moveToNext());
        }
        return detteList;
    }

    public int getDetteCount() {
        Log.i(TAG, "MyDatabaseHelper.getDetteCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DETTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateDette(Dette dette) {
        Log.i(TAG, "MyDatabaseHelper.updateDette ... "  + dette.getDetteId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_DESTINATAIRE, dette.getNom_destinataire());
        values.put(COLUMN_MONTANT_DETTE, dette.getMontant_dette());
        values.put(COLUMN_DATE_ECHEANCE, dette.getDate_echeance());
        values.put(COLUMN_ID_UTILISATEUR, dette.getUserId());

        // updating row
        return db.update(TABLE_DETTE, values, COLUMN_ID_UTILISATEUR + " = ?",
                new String[]{String.valueOf(dette.getUserId())});
    }

    public void deleteDette(Dette dette) {
        Log.i(TAG, "MyDatabaseHelper.updateDette ... " + dette.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETTE, COLUMN_ID_UTILISATEUR + " = ?",
                new String[] { String.valueOf(dette.getUserId()) });
        db.close();
    }



}