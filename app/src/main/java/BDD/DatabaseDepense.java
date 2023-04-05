package BDD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import modele.Depense;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseDepense extends DatabasePrincipale {

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

    private static final String COLUMN_DESCRIPTION_DEPENSE ="description_depense";


    public DatabaseDepense(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        try {
// Commande SQL de suppression de la table
            String deleteQuery = "DROP TABLE IF EXISTS " + TABLE_DEPENSE;

// Commande SQL de création de la table
            String createQuery = "CREATE TABLE " + TABLE_DEPENSE + "("
                    + COLUMN_ID_DEPENSE + " INTEGER PRIMARY KEY," + COLUMN_DATE_DEPENSE +
                    " TEXT," + COLUMN_MONTANT_DEPENSE +
                    " TEXT," + COLUMN_ID_CATEGORIE +
                    " TEXT," + COLUMN_ID_UTILISATEUR_DEPENSE +
                    " TEXT," +  COLUMN_DESCRIPTION_DEPENSE+
                    " TEXT" +")";

// Exécution des commandes SQL
            db.execSQL(deleteQuery);
            db.execSQL(createQuery);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        try {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENSE);

            // Create tables again
            onCreate(db);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // If depense table has no data
    // default, Insert 2 records.
    public void createDefaultDepenseIfNeed()  {
        try {
            int count = this.getDepenseCount();
            if (count == 0) {
                //Test
                Depense depense1 = new Depense(0, "08/01/2022", 888, 1, 1, "Solde");
                Depense depense2 = new Depense(1, "02/05/2022", 200, 1, 2, "Disney");
                Depense depense3 = new Depense(2, "13/01/2022", 300, 1, 3, "Cinéma");
                Depense depense4 = new Depense(3, "24/01/2022", 400, 1, 4, "Navigo");
                Depense depense5 = new Depense(4, "08/01/2022", 500, 1, 5, "Casserole");
                Depense depense6 = new Depense(5, "16/01/2022", 600, 1, 6, "Ursaaf");
                Depense depense7 = new Depense(6, "07/01/2022", 700, 1, 7, "Loyer");
                Depense depense8 = new Depense(7, "28/01/2022", 800, 1, 8, "Doliprane");
                Depense depense9 = new Depense(8, "09/01/2022", 900, 1, 0, "Kebab");
                Depense depense10 = new Depense(9, "10/01/2022", 150, 1, 1, "Jean");
                Depense depense11 = new Depense(10, "10/01/2022", 25, 1, 1, "Tee-shirt");
                Depense depense12 = new Depense(11, "15/01/2021", 12.5, 1, 1, "Claquette");
                Depense depense13 = new Depense(12, "01/01/2022", 88.99, 1, 1, "Solde mamam");
                Depense depense14 = new Depense(13, "01/01/2022", 76.99, 1, 1, "Casquette");
                Depense depense15 = new Depense(14, "01/01/2022", 87.34, 1, 1, "Tong");
                Depense depense16 = new Depense(15, "01/01/2022", 10000.99, 1, 1, "Clavier Gamer");


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
                addDepense(depense14);
                addDepense(depense15);
                addDepense(depense16);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //On ajoute un Depense
    public void addDepense(Depense depense) {
        Log.i(TAG, "MyDatabaseHelper.addDepense ... " + depense.getDepenseId()); // affiche un message dans la console android

        try {
            SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture
            ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

            //on prepare les donnees suivantes
            values.put(COLUMN_ID_DEPENSE, depense.getDepenseId());
            values.put(COLUMN_DATE_DEPENSE, depense.getDate());
            values.put(COLUMN_MONTANT_DEPENSE, depense.getMontant());
            values.put(COLUMN_ID_CATEGORIE, depense.getCategorieId());
            values.put(COLUMN_ID_UTILISATEUR_DEPENSE, depense.getUserId());
            values.put(COLUMN_DESCRIPTION_DEPENSE, depense.getDescriptionDepense());

            db.insert(TABLE_DEPENSE, null, values);

            // Closing database connection
            db.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Depense getDepense(int id) {
        Log.i(TAG, "MyDatabaseHelper.getDepense ... " + id);

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_DEPENSE, new String[]{COLUMN_ID_DEPENSE,
                            COLUMN_DATE_DEPENSE, COLUMN_MONTANT_DEPENSE, COLUMN_ID_CATEGORIE, COLUMN_ID_UTILISATEUR_DEPENSE, COLUMN_DESCRIPTION_DEPENSE}, COLUMN_ID_DEPENSE + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Depense depense = new Depense(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), cursor.getString(5));
            return depense;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
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
                depense.setDescriptionDepense(cursor.getString(5));

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
        values.put(COLUMN_DESCRIPTION_DEPENSE, depense.getDescriptionDepense());


        // updating row
        return db.update(TABLE_DEPENSE, values, COLUMN_ID_DEPENSE + " = ?",
                new String[]{String.valueOf(depense.getDepenseId())});
    }

    public void deleteDepense(int depense) {
        Log.i(TAG, "MyDatabaseHelper.updateDepense ... " + depense);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_DEPENSE, COLUMN_ID_DEPENSE + " = ?",
                    new String[]{String.valueOf(depense)});
            db.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * permet d'obtenir les depenses d'un utilisateurs, en sachant la catégorie  de la dépense
     * @param userId
     * @return
     */
    public ArrayList<Depense> getDepensesUtilisateur(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense for user " + userId);

        try {
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
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * permet d'obtenir les depenses d'un utilisateurs, en sachant la catégorie  de la dépense
     * @param userId
     * @return
     */
    public ArrayList<Depense> getDepensesUtilisateurCategorie(int userId, int categorieId) {
        Log.i(TAG, "MyDatabaseHelper.getAllDepense for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" + "AND " + COLUMN_ID_CATEGORIE + " = ?";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(categorieId)});


            while (cursor.moveToNext()){
                Depense depense = new Depense();
                depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                depense.setDate((cursor.getString(1)));
                depense.setMontant(Double.parseDouble((cursor.getString(2))));
                depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                depense.setUserId(Integer.parseInt(cursor.getString(4)));
                depense.setDescriptionDepense(cursor.getString(5));

                // Adding depense to list
                depenseList.add(depense);
            }

            cursor.close();
            return depenseList;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public int getDepenseCountCategorie(int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepenseCount ... " );

        try {

            String countQuery = "SELECT  * FROM " + TABLE_DEPENSE + " WHERE " + COLUMN_ID_CATEGORIE + " = ?";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(idCategorie)}, null);

            int count = cursor.getCount();

            cursor.close();

            return count;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}