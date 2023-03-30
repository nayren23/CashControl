package BDD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import modele.Depense;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseDepense extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Depense_Manager";

    // Table name: Depense.
    private static final String TABLE_DEPENSE = "Depense";
    private static final String TABLE_UTILISATEUR = "User";

    //On creer la structure de la table
    private static final String COLUMN_ID_DEPENSE ="id_depense";
    private static final String COLUMN_DATE_DEPENSE ="date_depense";
    private static final String COLUMN_MONTANT_DEPENSE ="montant_depense";
    private static final String COLUMN_ID_CATEGORIE ="id_categorie";
    private static final String COLUMN_ID_UTILISATEUR_DEPENSE ="id_utilisateur";
    private static final String COLUMN_ID_UTILISATEUR = "id_utilisateur";


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
                " TEXT," + COLUMN_ID_UTILISATEUR_DEPENSE +
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

    // If depense table has no data
    // default, Insert 2 records.
    public void createDefaultDepenseIfNeed()  {
        int count = this.getDepenseCount();
        if(count == 0 ) {
            //Depense depenseYassine = new Depense(0 , "12/13/2013",1920000,  0,1);

            //Test
            Depense depense1 = new Depense(0, "01/01/2022", 100, 0, 1);
            Depense depense2 = new Depense(1, "02/01/2022", 200, 0, 2);
            Depense depense3 = new Depense(2, "03/01/2022", 300, 0, 3);
            Depense depense4 = new Depense(3, "04/01/2022", 400, 0, 4);
            Depense depense5 = new Depense(4, "05/01/2022", 500, 0, 5);
            Depense depense6 = new Depense(5, "06/01/2022", 600, 0, 6);
            Depense depense7 = new Depense(6, "07/01/2022", 700, 0, 7);
            Depense depense8 = new Depense(7, "08/01/2022", 800, 0, 8);
            Depense depense9 = new Depense(8, "09/01/2022", 900, 0, 0);
            Depense depense10 = new Depense(9, "10/01/2022", 150, 0, 1);
            Depense depense11 = new Depense(9, "10/01/2022", 25, 0, 1);
            Depense depense12 = new Depense(9, "10/01/2022", 12.5, 0, 1);
            Depense depense13 = new Depense(9, "10/01/2022", 7.30, 0, 1);


            addDepense(depense1);
            addDepense(depense2);
            addDepense(depense3);
            addDepense(depense4);
            addDepense(depense5);
            addDepense(depense6);
            addDepense(depense7);
            addDepense(depense8);
            addDepense(depense9);
            addDepense(depense10);
            addDepense(depense11);
            addDepense(depense12);
            addDepense(depense13);

              Depense depenseRayan = new Depense(1 , "01/01/2023",1,  1,2);
              Depense depenseAyoub = new Depense(2 , "01/01/2023",0.5,  2,3);
        }
    }

    //On ajoute un Depense
    public void addDepense(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.addDepense ... " + depense.getDepenseId()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_ID_DEPENSE, depense.getDepenseId());
        values.put(COLUMN_DATE_DEPENSE, depense.getDate());
        values.put(COLUMN_MONTANT_DEPENSE, depense.getMontant());
        values.put(COLUMN_ID_CATEGORIE, depense.getCategorieId());
        values.put(COLUMN_ID_UTILISATEUR_DEPENSE, depense.getUserId());


        db.insert(TABLE_DEPENSE, null, values);

        // Closing database connection
        db.close();
    }

    public Depense getDepense(int id) {
        Log.i(TAG, "MyDatabaseHelper.getDepense ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DEPENSE, new String[] { COLUMN_ID_DEPENSE,
                        COLUMN_DATE_DEPENSE,COLUMN_MONTANT_DEPENSE, COLUMN_ID_CATEGORIE, COLUMN_ID_UTILISATEUR_DEPENSE}, COLUMN_ID_UTILISATEUR_DEPENSE + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Depense depense = new Depense(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
        return depense;
    }

    public ArrayList<Depense> getAllDepense() {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense ... " );

        ArrayList<Depense> depenseList = new ArrayList<Depense>();

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
                depenseList.add(depense);
            } while (cursor.moveToNext());
        }
        return depenseList;
    }

    public int getDepenseCount() {
        Log.i(TAG, "MyDatabaseHelper.getDepenseCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DEPENSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }


    public int updateDepense(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.updateDepense ... "  + depense.getDepenseId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_DEPENSE, depense.getDate());
        values.put(COLUMN_MONTANT_DEPENSE, depense.getMontant());
        values.put(COLUMN_ID_CATEGORIE, depense.getCategorieId());
        values.put(COLUMN_ID_UTILISATEUR_DEPENSE, depense.getUserId());

        // updating row
        return db.update(TABLE_DEPENSE, values, COLUMN_ID_UTILISATEUR_DEPENSE + " = ?",
                new String[]{String.valueOf(depense.getUserId())});
    }

    public void deleteDepense(int depense) {
        Log.i(TAG, "MyDatabaseHelper.updateDepense ... " + depense);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEPENSE, COLUMN_ID_DEPENSE + " = ?",
                new String[] { String.valueOf(depense) });
        db.close();
    }




    /**
     * permet d'obtenir les depenses d'un utilisateurs, en sachant la catégorie  de la dépense
     * @param userId
     * @return
     */
    public ArrayList<Depense> getDepensesUtilisateur(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense for user " + userId);

        ArrayList<Depense> depenseList = new ArrayList<>();

        // Select Query
        String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

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
                depenseList.add(depense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return depenseList;
    }


    /**
     * permet d'obtenir les depenses d'un utilisateurs, en sachant la catégorie  de la dépense
     * @param userId
     * @return
     */
    public ArrayList<Depense> getDepensesUtilisateurCategorie(int userId, int categorieId) {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense for user " + userId);

        ArrayList<Depense> depenseList = new ArrayList<>();

        // Select Query
        String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" + "AND " + COLUMN_ID_CATEGORIE + " = ?" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(categorieId)});

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
                depenseList.add(depense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return depenseList;
    }

    public int getDepenseCountCategorie(int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepenseCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DEPENSE  + " WHERE " + COLUMN_ID_CATEGORIE + " = ?";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(idCategorie)}, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }


}