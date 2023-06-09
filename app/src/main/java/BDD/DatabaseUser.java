package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import modele.User;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseUser extends DatabasePrincipale {

    // Database Name
    private static final String DATABASE_NAME = "User_Manager";

    // Table name: User.
    private static final String TABLE_USER = "User";

    //On creer la structure de la table
    private static final String COLUMN_ID_UTILISATEUR ="id_utilisateur";
    private static final String COLUMN_IDENTIFIANT_UTILISATEUR ="identifiant_utilisateur";
    private static final String COLUMN_EMAIL_UTILISATEUR ="email_utilisateur";
    private static final String COLUMN_MOT_DE_PASSE_UTILISATEUR ="mot_de_passe_utilisateur";
    private static final String COLUMN_CHEMINIMAGE_UTILISATEUR="cheminimage_utilisateur";
    private static final String COLUMN_NUMEROTELEPHONE_UTILISATEUR ="numerotelephone_utilisateur";

    public DatabaseUser(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID_UTILISATEUR + " INTEGER PRIMARY KEY," + COLUMN_IDENTIFIANT_UTILISATEUR +
                " TEXT," + COLUMN_EMAIL_UTILISATEUR +
                " TEXT," +  COLUMN_MOT_DE_PASSE_UTILISATEUR +
                " TEXT,"+ COLUMN_CHEMINIMAGE_UTILISATEUR +
                " TEXT,"+ COLUMN_NUMEROTELEPHONE_UTILISATEUR +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // If User table has no data
    // default, Insert 2 records.
    public void createDefaultUsersIfNeed()  {
        int count = this.getUserCount();
        if(count ==0 ) {
            User rayan = new User(1 , "rchouchane",0,"rayan@gmail.com", "$2a$12$AY2Oso3K.tWjvgTCM1epEuZsGILbJ7rxYVpOALORK5wm53.7igCrK","rayan", "0781799851");
            addUser(rayan);
        }
    }

    //On ajoute un User
    public void addUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.addUser ... " + user.getIdentifiant()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_IDENTIFIANT_UTILISATEUR, user.getIdentifiant());
        values.put(COLUMN_EMAIL_UTILISATEUR, user.getEmail());
        values.put(COLUMN_MOT_DE_PASSE_UTILISATEUR, user.getMot_de_passe());
        values.put(COLUMN_CHEMINIMAGE_UTILISATEUR, user.getCheminimage());
        values.put(COLUMN_NUMEROTELEPHONE_UTILISATEUR, user.getNumerotelephone());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        // Closing database connection
        db.close();
    }

    public User getUser(int id) {
        Log.i(TAG, "MyDatabaseHelper.getUser ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_ID_UTILISATEUR,
                        COLUMN_IDENTIFIANT_UTILISATEUR,COLUMN_EMAIL_UTILISATEUR, COLUMN_MOT_DE_PASSE_UTILISATEUR, COLUMN_CHEMINIMAGE_UTILISATEUR, COLUMN_NUMEROTELEPHONE_UTILISATEUR}, COLUMN_ID_UTILISATEUR + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return user;
    }

    public int getUserCount() {
        Log.i(TAG, "MyDatabaseHelper.getUsersCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean verificationExistenceIdentifiantDansLaBDD(String identifiant){

        try {
            String query = " SELECT " + COLUMN_IDENTIFIANT_UTILISATEUR + " FROM " + TABLE_USER + " WHERE " + COLUMN_IDENTIFIANT_UTILISATEUR + " = ? ";


            SQLiteDatabase db = this.getReadableDatabase();

            // Définir la valeur du paramètre
            String[] selectionArgs = { identifiant };

            // Exécuter la requête préparée avec la valeur du paramètre
            Cursor cursor = db.rawQuery(query, selectionArgs);

            // Vérifier si le curseur est valide et s'il contient des enregistrements
            if (cursor != null && cursor.moveToFirst()) {
                // Extraire la valeur de la colonne
                String identifiantUtilisateur = cursor.getString(0);
                // Faire quelque chose avec la valeur extraite
                return false;
            }
            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }
    public boolean verificationConnexionDansLaBDD(String identifiant){

        try {
            String query = " SELECT " + COLUMN_IDENTIFIANT_UTILISATEUR  + " FROM " + TABLE_USER + " WHERE " + COLUMN_IDENTIFIANT_UTILISATEUR + " = ? ";
            SQLiteDatabase db = this.getReadableDatabase();
            // Définir la valeur du paramètre
            String[] selectionArgs = { identifiant};
            // Exécuter la requête préparée avec la valeur du paramètre
            Cursor cursor = db.rawQuery(query, selectionArgs);

            // Vérifier si le curseur est valide et s'il contient des enregistrements
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }

            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }
    public int retourneIdUser(String identifiant){

        try {
            String query = " SELECT " + COLUMN_ID_UTILISATEUR + " FROM " + TABLE_USER + " WHERE " + COLUMN_IDENTIFIANT_UTILISATEUR + " = ? ";
            SQLiteDatabase db = this.getReadableDatabase();

            // Définir la valeur du paramètre
            String[] selectionArgs = { identifiant};

            // Exécuter la requête préparée avec la valeur du paramètre
            Cursor cursor = db.rawQuery(query, selectionArgs);

            // Vérifier si le curseur est valide et s'il contient des enregistrements
            if (cursor != null && cursor.moveToFirst()) {
                return Integer.parseInt(cursor.getString(0));
            }

            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public String verifMdpIdentifiant(String identifiant){

        try {
            String query = " SELECT " + COLUMN_MOT_DE_PASSE_UTILISATEUR + " FROM " + TABLE_USER + " WHERE " + COLUMN_IDENTIFIANT_UTILISATEUR + " = ? ";
            SQLiteDatabase db = this.getReadableDatabase();

            // Définir la valeur du paramètre
            String[] selectionArgs = {identifiant};

            // Exécuter la requête préparée avec la valeur du paramètre
            Cursor cursor = db.rawQuery(query, selectionArgs);

            // Vérifier si le curseur est valide et s'il contient des enregistrements
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }

            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}