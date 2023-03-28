package BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modele.User;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseUser extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "User_Manager";

    // Table name: User.
    private static final String TABLE_USER = "User";

    //On creer la structure de la table
    private static final String COLUMN_ID_UTILISATEUR ="id_utilisateur";
    private static final String COLUMN_NOM_UTILISATEUR ="nom_utilisateur";
    private static final String COLUMN_PRENOM_UTILISATEUR ="prenom_utilisateur";
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
                + COLUMN_ID_UTILISATEUR + " INTEGER PRIMARY KEY," + COLUMN_NOM_UTILISATEUR +
                " TEXT," + COLUMN_PRENOM_UTILISATEUR +
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
            User yassine = new User(0 , "hamidi","yassine",0,"yassine@gmail.com", "yassine", "yassine","0781799878");
            User rayan = new User(0 , "chouchane","rayan",0,"rayan@gmail.com", "rayan","rayan", "0781799878");
            User ayoub = new User(1 , "bouaziz","ayoub", "ayoub@gmail.com", "ayoub", "ayoub","0666766767");
        addUser(yassine);
        addUser(rayan);
        addUser(ayoub);

         }
    }

    //On ajoute un User
    public void addUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.addUser ... " + user.getNom()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_ID_UTILISATEUR, user.getUserId());
        values.put(COLUMN_NOM_UTILISATEUR, user.getNom());
        values.put(COLUMN_PRENOM_UTILISATEUR, user.getPrenom());
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
                        COLUMN_NOM_UTILISATEUR,COLUMN_PRENOM_UTILISATEUR, COLUMN_EMAIL_UTILISATEUR, COLUMN_NUMEROTELEPHONE_UTILISATEUR,COLUMN_CHEMINIMAGE_UTILISATEUR }, COLUMN_ID_UTILISATEUR + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return user;
    }

    public List<User> getAllUser() {
        Log.i(TAG, "MyDatabaseHelper.getAllUsers ... " );

        List<User> userList = new ArrayList<User>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(0)));
                user.setNom((cursor.getString(1)));
                user.setPrenom((cursor.getString(2)));
                user.setEmail((cursor.getString(3)));
                user.setMot_de_passe((cursor.getString(4)));
                user.setNumerotelephone((cursor.getString(5)));
                user.setCheminimage((cursor.getString(6)));

                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
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

    public int updateUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... "  + user.getPrenom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_UTILISATEUR, user.getNom());
        values.put(COLUMN_PRENOM_UTILISATEUR, user.getPrenom());
        values.put(COLUMN_EMAIL_UTILISATEUR, user.getEmail());
        values.put(COLUMN_NUMEROTELEPHONE_UTILISATEUR, user.getNumerotelephone());
        values.put(COLUMN_CHEMINIMAGE_UTILISATEUR, user.getCheminimage());

        // updating row
        return db.update(TABLE_USER, values, COLUMN_ID_UTILISATEUR + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    public void deleteUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... " + user.getNom());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_ID_UTILISATEUR + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
        db.close();
    }
}