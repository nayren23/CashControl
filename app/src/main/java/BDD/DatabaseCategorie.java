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

import modele.Categorie;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseCategorie extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Categorie_Manager";

    // Table name: Depense.
    private static final String TABLE_CATEGORIE = "Categorie";

    //On creer la structure de la table
    private static final String COLUMN_ID_CATEGORIE ="id_categorie";
    private static final String COLUMN_NOM_CATEGORIE ="nom_categorie";



    public DatabaseCategorie(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_CATEGORIE + "("
                + COLUMN_ID_CATEGORIE + " INTEGER PRIMARY KEY," + COLUMN_NOM_CATEGORIE +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE);

        // Create tables again
        onCreate(db);
    }

    // If depense table has no data
    // default, Insert 2 records.
    public void createDefaultCategorieIfNeed()  {
        int count = this.getDepenseCount();
        if(count == 0 ) {
            Categorie Alimentation = new Categorie(0,"Alimentation & Restauration");
            Categorie Achat = new Categorie(1 , "Achat & Shopping");
            Categorie Loisirs = new Categorie(2 , "Loisirs & Sorties");
            Categorie Abonnement = new Categorie(2 , "Loisirs & Sorties");
            Categorie Transports = new Categorie(2 , "Transports & auto");
            Categorie Divers = new Categorie(2 , "Divers");
            Categorie Impôts = new Categorie(2 , "Impôts");
            Categorie Logement = new Categorie(2 , "Logement");
            Categorie Santé = new Categorie(2 , "santé");

        }
    }

    //On ajoute un Depense
    public void addDepense(Categorie categorie) {
        Log.i(TAG, "MyDatabaseHelper.addDepense ... " + categorie.getCategorieId()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_NOM_CATEGORIE, categorie.getNom());



        db.insert(TABLE_CATEGORIE, null, values);

        // Closing database connection
        db.close();
    }

    public Categorie getCategorie(int id) {
        Log.i(TAG, "MyDatabaseHelper.getCategorie ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIE, new String[] { COLUMN_ID_CATEGORIE,
                        COLUMN_NOM_CATEGORIE}, COLUMN_ID_CATEGORIE + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Categorie categorie = new Categorie(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return categorie;
    }

    public List<Categorie> getAllDepense() {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense ... " );

        List<Categorie> depenseList = new ArrayList<Categorie>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Categorie categorie = new Categorie();
                categorie.setCategorieId(Integer.parseInt(cursor.getString(0)));
                categorie.setNom((cursor.getString(1)));



                // Adding depense to list
                depenseList.add(categorie);
            } while (cursor.moveToNext());
        }
        return depenseList;
    }

    public int getDepenseCount() {
        Log.i(TAG, "MyDatabaseHelper.getDepenseCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateDepense(Categorie categorie) {
        Log.i(TAG, "MyDatabaseHelper.updateDepense ... "  + categorie.getCategorieId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_CATEGORIE, categorie.getNom());


        // updating row
        return db.update(TABLE_CATEGORIE, values, COLUMN_ID_CATEGORIE + " = ?",
                new String[]{String.valueOf(categorie.getCategorieId())});
    }

    public void deleteDepense(Categorie categorie) {
        Log.i(TAG, "MyDatabaseHelper.updateDepense ... " + categorie.getCategorieId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIE, COLUMN_ID_CATEGORIE + " = ?",
                new String[] { String.valueOf(categorie.getCategorieId()) });
        db.close();
    }



}