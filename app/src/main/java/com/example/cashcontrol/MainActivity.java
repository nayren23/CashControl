package com.example.cashcontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import BDD.DatabaseUser;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.User;


public class MainActivity extends AppCompatActivity {
    private Handler handler;
    /*Info User*/
    private EditText mMain_champ_identifiant;
    private EditText mMain_champ_email;
    private EditText mMain_champ_numero_telephone;
    private EditText mMain_champ_mot_de_passe;
    private Button mButtonImage;
    private ImageView imageUser;
    private Button mSauvegarde_compte;
    private Button mConnexion_users;
    private boolean tousremplis = true;// pour verifier si tous les champs sont remplit

    /*User*/
    private DatabaseUser dbUser;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

    //spécifie le nombre de tours de hachage à effectuer
    private static final int WORKLOAD = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Obtention  les Widgets

        this.mMain_champ_identifiant = findViewById(R.id.main_champ_identifiant);
        this.mMain_champ_email = findViewById(R.id.main_champ_adresse);
        this.mMain_champ_numero_telephone = this.findViewById(R.id.main_champ_numero_telephone);
        this.mMain_champ_mot_de_passe = this.findViewById(R.id.main_champ_mot_de_passe);


        this.mButtonImage = this.findViewById(R.id.button_image);
        this.imageUser = (ImageView) this.findViewById(R.id.image_user);
        this.mSauvegarde_compte = this.findViewById(R.id.sauvegarde_compte);
        this.mConnexion_users = this.findViewById(R.id.main_connexion_users);

        //Set bouton
        this.mSauvegarde_compte.setEnabled(false);
        this.mButtonImage.setEnabled(false);

        //On creer une instance la BDD user
        dbUser = new DatabaseUser(this);
        dbUser.createDefaultUsersIfNeed();


        // ou view
        mSauvegarde_compte.setOnClickListener(v -> {
            //On récupere les infos saisit par l'utilisateur
            String identifiant = mMain_champ_identifiant.getText().toString();
            String email =  mMain_champ_email.getText().toString();
            String numeroTelephone =mMain_champ_numero_telephone.getText().toString() ;
            String photoDeProfil = mMain_champ_identifiant.getText().toString();
            String motdepasse = encrypt(mMain_champ_mot_de_passe.getText().toString());

            //On creer notre Utilisateur
            User creationUser = new User(identifiant,email,motdepasse,photoDeProfil,numeroTelephone);

            try {
                //On creer l'utilisateur dans la BDD
                if(dbUser.verificationExistenceIdentifiantDansLaBDD(identifiant)){
                    Toast.makeText(getApplicationContext(), "Le compte a bien été sauvegarder", Toast.LENGTH_SHORT).show();
                    enregistrementUser(creationUser);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Désolé mais cet identifiant existe deja", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Listener pour le bouton de la photo
        this.mButtonImage.setOnClickListener(v -> captureImage());

        //Listener pour le bouton de la connexion
        this.mConnexion_users.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
            startActivity(intent);
        });

        //On verifie si tous les champs sont remplit pour qu'on puisse appuer sur le bouton save
        EditText[] editTexts = {mMain_champ_identifiant,mMain_champ_email,mMain_champ_numero_telephone, mMain_champ_mot_de_passe};
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(isAllEditTextFilled(Arrays.asList(editTexts))){
                        mButtonImage.setEnabled(true);
                    }
                    else {
                        mButtonImage.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }


        //On creer le handler avec le execute
        if(handler == null)
            handler = FourniseurHandler.creerHandler();

        DatabaseUser databaseUser = new DatabaseUser(this);

        //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
        FournisseurExecutor.creerExecutor().execute(()-> {
            databaseUser.createDefaultUsersIfNeed();
        });


    }




    public static String encrypt(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(password, salt);
    }


    public boolean isAllEditTextFilled(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            if (editText.getText().toString().trim().length() == 0) {
                return false;
            }
        }
        return true;
    }


    private void enregistrementUser(User user) throws IOException {
        DatabaseUser dbUser = new DatabaseUser(this);
        dbUser.addUser(user);
        Toast.makeText(this, "Utilisateurs Sauvegarder avec Succées 😍!", Toast.LENGTH_SHORT).show();
    }
    private void saveImage(Bitmap bp, String nomFichier){
        try  { // use the absolute file path here
            FileOutputStream out = this.openFileOutput(nomFichier, MODE_PRIVATE);
            bp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            out.close();
            Toast.makeText(this,"Image Sauvegarder !",Toast.LENGTH_SHORT).show();
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Camera
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageUser.setImageBitmap(bp);

                //Image sauvegarder dans le fichier
                String nomFichierPhotoDeProfil = mMain_champ_identifiant.getText().toString();
                saveImage(bp,nomFichierPhotoDeProfil);
                mSauvegarde_compte.setEnabled(true);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}