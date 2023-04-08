package com.example.cashcontrol;

import static com.example.cashcontrol.InscriptionActivity.encrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import BDD.DatabaseUser;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;

import org.mindrot.jbcrypt.BCrypt;

public class ConnexionActivity extends AppCompatActivity {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur
    private Handler handler;

    /*Info User*/
    private EditText mConnexion_champ_identifiant;
    private EditText mConnexion_mot_de_passe;
    private TextView mConnexion_text_view_s_inscrire;
    private Button mButtonConnexion;

    /*User*/
    private DatabaseUser dbUser;

    /*Attributs*/
    private boolean tousremplis;
    private boolean verifConnexion;
    private boolean mdp;
    private String mdpHashDansLaBdd;
    private String identifiant;
    private String motdepasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        //Obtention  les Widgets
        this.mConnexion_champ_identifiant = findViewById(R.id.connexion_champ_identifiant);
        this.mConnexion_mot_de_passe = findViewById(R.id.connexion_champ_mot_de_passe);
        this.mConnexion_text_view_s_inscrire = findViewById(R.id.connexion_text_view_s_inscrire);
        this.mButtonConnexion = this.findViewById(R.id.connexion_users);

        //Set bouton
        this.mButtonConnexion.setEnabled(false);

        //On creer une instance la BDD user
        dbUser = new DatabaseUser(this);
        dbUser.createDefaultUsersIfNeed();

        //On creer le handler avec le execute
        if (handler == null)
            handler = FourniseurHandler.creerHandler();

        mConnexion_text_view_s_inscrire.setOnClickListener(view -> {
            Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });

        //On verifie si tous les champs sont remplit pour qu'on puisse appuer sur le bouton save
        EditText[] editTexts = {mConnexion_champ_identifiant, mConnexion_mot_de_passe}; // Ajoutez tous vos EditText ici
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tousremplis = true;
                    for (EditText editText : editTexts) {
                        String value = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(value)) {
                            tousremplis = false;
                        }
                    }
                    if (tousremplis) {
                        mButtonConnexion.setEnabled(true);
                    } else {
                        mButtonConnexion.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mButtonConnexion.setOnClickListener(view -> {
                identifiant = mConnexion_champ_identifiant.getText().toString();
                motdepasse = mConnexion_mot_de_passe.getText().toString();
                //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
                FournisseurExecutor.creerExecutor().execute(() -> {
                    verifConnexion = dbUser.verificationConnexionDansLaBDD(identifiant);
                    mdpHashDansLaBdd = dbUser.verifMdpIdentifiant(identifiant);
                    if (!mdpHashDansLaBdd.equals("")) {
                       /* mdp = checkPassword(motdepasse, mdpHashDansLaBdd);*/
                        mdp = true;

                        if (verifConnexion && mdp) {

                            // post -> ajoute les instructions à la suite de celles du main thread

                            handler.post(() -> {
                                // Notification du main thread pour qu'il mette à jour la UI
                                // tout ce qui touche à la UI doit être exécuté dans le main thread
                                Toast.makeText(getApplicationContext(), "Bienvenue " + identifiant, Toast.LENGTH_SHORT).show();
                            });
                            int id = dbUser.retourneIdUser(identifiant);

                            if (id != -1) {
                                //on change la valeur dans les shared preferences
                                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                                        .edit()
                                        .putInt(SHARED_PREF_USER_INFO_ID, id)
                                        .apply();

                                Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                handler.post(() -> {
                                    Toast.makeText(getApplicationContext(), "Impossible de trouvé l'utilisateur " + identifiant, Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            handler.post(() -> {
                                Toast.makeText(getApplicationContext(), "Connexion impossible " + identifiant, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                    else{
                        handler.post(() -> {
                            Toast.makeText(getApplicationContext(), "Compte inexistant " + identifiant, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        }
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, hashedPassword);
    }
}
