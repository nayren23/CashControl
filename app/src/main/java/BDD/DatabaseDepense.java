package BDD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
                    " DATE," + COLUMN_MONTANT_DEPENSE +
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
                Depense depense1 = new Depense(0, "2023-01-08", 888.20, 1, 1, "Solde");
                Depense depense2 = new Depense(1, "2023-05-02", 200, 1, 2, "Disney");
                Depense depense3 = new Depense(2, "2023-01-13", 300, 1, 3, "Cinéma");
                Depense depense4 = new Depense(3, "2023-01-24", 400, 1, 4, "Navigo");
                Depense depense5 = new Depense(4, "2023-01-08", 500, 1, 5, "Casserole");
                Depense depense6 = new Depense(5, "2023-01-16", 600, 1, 6, "Ursaaf");
                Depense depense7 = new Depense(6, "2023-01-07", 700, 1, 7, "Loyer");
                Depense depense8 = new Depense(7, "2023-01-28", 800, 1, 8, "Doliprane");
                Depense depense9 = new Depense(8, "2023-01-09", 900, 1, 0, "Kebab");
                Depense depense10 = new Depense(9, "2023-01-10", 150, 1, 1, "Jean");
                Depense depense11 = new Depense(10, "2023-01-10", 25, 1, 1, "Tee-shirt");
                Depense depense12 = new Depense(11, "2023-01-15", 12.5, 1, 1, "Claquette");
                Depense depense13 = new Depense(12, "2023-01-01", 88.99, 1, 1, "Solde mamam");
                Depense depense14 = new Depense(13, "2023-01-01", 76.99, 1, 1, "Casquette");
                Depense depense15 = new Depense(14, "2023-01-01", 87.34, 1, 1, "Tong");
                Depense depense16 = new Depense(15, "2023-01-01", 100.99, 1, 1, "Clavier Gamer");
                Depense depense17 = new Depense(16, "2023-01-01", 100.9, 1, 1, "Clavier Gamer");
                Depense depense18 = new Depense(17, "2023-04-07", 120.19, 1, 1, "Clavier Gamer");
                Depense depense19 = new Depense(18, "2023-04-06", 180.89, 1, 1, "Clavier Gamer");
                Depense depense20 = new Depense(19, "2023-04-06", 190.79, 1, 7, "Loyer");
                Depense depense21 = new Depense(20, "2023-04-21", 10.19, 1, 6, "Ursaaf");


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
                addDepense(depense17);
                addDepense(depense18);
                addDepense(depense19);
                addDepense(depense20);
                addDepense(depense21);

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

    public ArrayList<Depense> getDepensesByUserIdAndCurrentMonth(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentMonth for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND strftime('%m', " + COLUMN_DATE_DEPENSE + ") = strftime('%m', 'now')";

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
                    depense.setDescriptionDepense(cursor.getString(5));

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

    public ArrayList<Depense> getDepensesParUserIdEtJourActuel(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesParUserIdEtJourActuel for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND strftime('%d', " + COLUMN_DATE_DEPENSE + ") = ?" +
                    " AND strftime('%m-%d', " + COLUMN_DATE_DEPENSE + ") = strftime('%m-%d', 'now')";

            SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))});

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

            cursor.close();
            return depenseList;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Depense> getDepensesSemaineActuelle(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesSemaineActuelle for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Get the current day of the week (0-6, where 0 is Sunday)
            Calendar calendar = Calendar.getInstance();
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // Calculate the date of the first day of the week (Sunday)
            calendar.add(Calendar.DAY_OF_WEEK, -currentDayOfWeek);
            String firstDayOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND " + COLUMN_DATE_DEPENSE + " >= date('" + firstDayOfWeek + "')" +
                    " AND " + COLUMN_DATE_DEPENSE + " <= date('now')";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

            // Loop through all rows and add to list
            if (cursor.moveToFirst()) {
                do {
                    Depense depense = new Depense();
                    depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                    depense.setDate((cursor.getString(1)));
                    depense.setMontant(Double.parseDouble((cursor.getString(2))));
                    depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                    depense.setUserId(Integer.parseInt(cursor.getString(4)));
                    depense.setDescriptionDepense(cursor.getString(5));

                    // Add depense to list
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
     * Récupère toutes les dépenses de l'année en cours pour un utilisateur donné.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Une liste de toutes les dépenses de l'année en cours pour l'utilisateur donné.
     */
    public ArrayList<Depense> getDepensesByUserIdAndCurrentYear(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentYear for user " + userId);

        ArrayList<Depense> depenseList = new ArrayList<>();

        try {
            // Récupérer l'année en cours
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);

            // Requête SELECT
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND strftime('%Y', " + COLUMN_DATE_DEPENSE + ") = ?";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(currentYear)});
            Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentYear: nombre de lignes " + cursor.getCount());

            // Boucle à travers toutes les lignes et ajoute chaque dépense à la liste
            if (cursor.moveToFirst()) {
                do {
                    Depense depense = new Depense();
                    depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                    depense.setDate((cursor.getString(1)));
                    depense.setMontant(Double.parseDouble((cursor.getString(2))));
                    depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                    depense.setUserId(Integer.parseInt(cursor.getString(4)));
                    depense.setDescriptionDepense(cursor.getString(5));

                    // Ajouter la dépense à la liste
                    depenseList.add(depense);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return depenseList;
    }


    public ArrayList<Depense> getDepensesParUserIdDateComplete(int userId, String jour, String mois, String annee) {
        ArrayList<Depense> depenseList = new ArrayList<>();

        // Select Query
        String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                " AND strftime('%d', " + COLUMN_DATE_DEPENSE + ") = ?" +
                " AND strftime('%m', " + COLUMN_DATE_DEPENSE + ") = ?" +
                " AND strftime('%Y', " + COLUMN_DATE_DEPENSE + ") = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), jour, mois, annee});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Depense depense = new Depense();
                depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                depense.setDate(cursor.getString(1));
                depense.setMontant(Double.parseDouble(cursor.getString(2)));
                depense.setCategorieId(Integer.parseInt(cursor.getString(3)));
                depense.setUserId(Integer.parseInt(cursor.getString(4)));
                depense.setDescriptionDepense(cursor.getString(5));

                // Adding depense to list
                depenseList.add(depense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return depenseList;
    }

    //Fonction modifier

    public ArrayList<Depense> getDepensesByUserIdAndCurrentMonthAndIdCategorie(int userId, int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentMonth for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND " + COLUMN_ID_CATEGORIE + " = ?" +
                    " AND strftime('%m', " + COLUMN_DATE_DEPENSE + ") = strftime('%m', 'now')";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(idCategorie)});

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

            cursor.close();
            return depenseList;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Depense> getDepensesParUserIdEtJourActuelAndIdCategorie(int userId, int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesParUserIdEtJourActuel for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND " + COLUMN_ID_CATEGORIE + " = ?" +
                    " AND strftime('%d', " + COLUMN_DATE_DEPENSE + ") = ?" +
                    " AND strftime('%m-%d', " + COLUMN_DATE_DEPENSE + ") = strftime('%m-%d', 'now')";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(idCategorie), String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))});

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

            cursor.close();
            return depenseList;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Depense> getDepensesSemaineActuelleAndIdCategorie(int userId, int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesSemaineActuelle for user " + userId);

        try {
            ArrayList<Depense> depenseList = new ArrayList<>();

            // Get the current day of the week (0-6, where 0 is Sunday)
            Calendar calendar = Calendar.getInstance();
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // Calculate the date of the first day of the week (Sunday)
            calendar.add(Calendar.DAY_OF_WEEK, -currentDayOfWeek);
            String firstDayOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            // Select Query
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND " + COLUMN_DATE_DEPENSE + " >= date('" + firstDayOfWeek + "')" +
                    " AND " + COLUMN_DATE_DEPENSE + " <= date('now')" +
                    " AND " + COLUMN_ID_CATEGORIE + " = ?";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(idCategorie)});

            // Loop through all rows and add to list
            if (cursor.moveToFirst()) {
                do {
                    Depense depense = new Depense();
                    depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                    depense.setDate((cursor.getString(1)));
                    depense.setMontant(Double.parseDouble((cursor.getString(2))));
                    depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                    depense.setUserId(Integer.parseInt(cursor.getString(4)));
                    depense.setDescriptionDepense(cursor.getString(5));

                    // Add depense to list
                    depenseList.add(depense);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return depenseList;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Depense> getDepensesByUserIdAndCurrentYearAndIdCategorie(int userId, int idCategorie) {
        Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentYear for user " + userId);

        ArrayList<Depense> depenseList = new ArrayList<>();

        try {
            // Récupérer l'année en cours
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);

            // Requête SELECT
            String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                    " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                    " AND strftime('%Y', " + COLUMN_DATE_DEPENSE + ") = ?" +
                    " AND " + COLUMN_ID_CATEGORIE + " = ?";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), String.valueOf(currentYear), String.valueOf(idCategorie)});
            Log.i(TAG, "MyDatabaseHelper.getDepensesByUserIdAndCurrentYear: nombre de lignes " + cursor.getCount());

            // Boucle à travers toutes les lignes et ajoute chaque dépense à la liste
            if (cursor.moveToFirst()) {
                do {
                    Depense depense = new Depense();
                    depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                    depense.setDate((cursor.getString(1)));
                    depense.setMontant(Double.parseDouble((cursor.getString(2))));
                    depense.setCategorieId(Integer.parseInt((cursor.getString(3))));
                    depense.setUserId(Integer.parseInt(cursor.getString(4)));
                    depense.setDescriptionDepense(cursor.getString(5));

                    // Ajouter la dépense à la liste
                    depenseList.add(depense);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return depenseList;
    }


    public ArrayList<Depense> getDepensesParUserIdDateCompleteAndIdCategorie(int userId, String jour, String mois, String annee, int idCategorie) {
        ArrayList<Depense> depenseList = new ArrayList<>();

        // Select Query
        String selectQuery = "SELECT * FROM " + TABLE_DEPENSE +
                " WHERE " + COLUMN_ID_UTILISATEUR_DEPENSE + " = ?" +
                " AND strftime('%d', " + COLUMN_DATE_DEPENSE + ") = ?" +
                " AND strftime('%m', " + COLUMN_DATE_DEPENSE + ") = ?" +
                " AND strftime('%Y', " + COLUMN_DATE_DEPENSE + ") = ?" +
                " AND " + COLUMN_ID_CATEGORIE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId), jour, mois, annee, String.valueOf(idCategorie)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Depense depense = new Depense();
                depense.setDepenseId(Integer.parseInt(cursor.getString(0)));
                depense.setDate(cursor.getString(1));
                depense.setMontant(Double.parseDouble(cursor.getString(2)));
                depense.setCategorieId(Integer.parseInt(cursor.getString(3)));
                depense.setUserId(Integer.parseInt(cursor.getString(4)));
                depense.setDescriptionDepense(cursor.getString(5));

                // Adding depense to list
                depenseList.add(depense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return depenseList;
    }

}