package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modele.Depense;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseDepense extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Depense_Manager";

    // Table name: User.
    private static final String TABLE_DEPENSE = "Depense";

    //On creer la structure de la table
    private static final String COLUMN_ID_DEPENSE ="id_depense";
    private static final String COLUMN_DATE_DEPENSE ="date_depense";
    private static final String COLUMN_MONTANT_DEPENSE ="montant_depense";
    private static final String COLUMN_ID_CATEGORIE ="id_categorie";
    private static final String COLUMN_ID_UTILISATEUR ="id_utilisateur";



    public DatabaseDepense(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_DEPENSE + "("
                + COLUMN_ID_DEPENSE + " INTEGER PRIMARY KEY," + COLUMN_DATE_DEPENSE +
                " TEXT," + COLUMN_MONTANT_DEPENSE +
                " TEXT," + COLUMN_ID_CATEGORIE +
                " TEXT," +  COLUMN_ID_UTILISATEUR +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENSE);

        // Create tables again
        onCreate(db);
    }

    // If User table has no data
    // default, Insert 2 records.
    public void createDefaultDepenseIfNeed()  {
        int count = this.getUserCount();
        if(count == 0 ) {
            Depense depenseYassine = new Depense(0 , "12/13/2013",1920000,  0,1);
            Depense depenseRayan = new Depense(1 , "01/01/2023",1,  1,2);
            Depense depenseAyoub = new Depense(2 , "01/01/2023",0.5,  2,3);
        }
    }

    //On ajoute un User
    public void addDepense(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.addUser ... " + depense.getDepenseId()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_DATE_DEPENSE, depense.getDate());
        values.put(COLUMN_MONTANT_DEPENSE, depense.getMontant());
        values.put(COLUMN_ID_CATEGORIE, depense.getCategorieId());
        values.put(COLUMN_ID_UTILISATEUR, depense.getUserId());


        db.insert(TABLE_DEPENSE, null, values);

        // Closing database connection
        db.close();
    }

    public Depense getDepense(int id) {
        Log.i(TAG, "MyDatabaseHelper.getUser ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DEPENSE, new String[] { COLUMN_ID_DEPENSE,
                        COLUMN_DATE_DEPENSE,COLUMN_MONTANT_DEPENSE, COLUMN_ID_CATEGORIE, COLUMN_ID_UTILISATEUR}, COLUMN_ID_UTILISATEUR + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Depense depense = new Depense(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
        return depense;
    }

    public List<Depense> getAllDepense() {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense ... " );

        List<Depense> userList = new ArrayList<Depense>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEPENSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Depense depense = new Depense();
                depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                depense.setDate((cursor.getString(1)));
                depense.setMontant(Double.parseDouble((cursor.getString(2))));
                depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                depense.setUserId(Integer.parseInt(cursor.getString(4)));


                // Adding depense to list
                userList.add(depense);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public int getUserCount() {
        Log.i(TAG, "MyDatabaseHelper.getUsersCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DEPENSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateDepense(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... "  + depense.getDepenseId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_DEPENSE, depense.getDate());
        values.put(COLUMN_MONTANT_DEPENSE, depense.getMontant());
        values.put(COLUMN_ID_CATEGORIE, depense.getCategorieId());
        values.put(COLUMN_ID_UTILISATEUR, depense.getUserId());

        // updating row
        return db.update(TABLE_DEPENSE, values, COLUMN_ID_UTILISATEUR + " = ?",
                new String[]{String.valueOf(depense.getUserId())});
    }

    public void deleteUser(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... " + depense.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEPENSE, COLUMN_ID_UTILISATEUR + " = ?",
                new String[] { String.valueOf(depense.getUserId()) });
        db.close();
    }
}