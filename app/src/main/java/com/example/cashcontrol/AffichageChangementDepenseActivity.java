package com.example.cashcontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import BDD.DatabaseDepense;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.Depense;
import utilitaires.DateUtil;

public class AffichageChangementDepenseActivity extends ImageActivity implements DatePickerFragment.OnDateSetListener {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur

    private Spinner listCategorie;
    private Button modifierDepensebtn;
    private Button photoBtn;

    private Button sauvegarder_imagebtn;

    private EditText montant ;
    private EditText description ;
    private EditText date ;
    private String nomFichier ;
    private  String [] dateSelectionner;
    private int id_Utilisateur_Courant;
    private DatabaseDepense dbDepense;
    private int idCategorie ;
    private ImageView depenseImage;
    private Handler handler;
    private int idDepense;
    private Depense ancienneDepense;
    private boolean tousremplis ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_changement_depense);

        //On creer le handler avec le execute
        if(handler == null)
            handler = FourniseurHandler.creerHandler();

        //On recupere les Widgets
        this.listCategorie = findViewById(R.id.category_spinner);
        this.modifierDepensebtn = (Button) (findViewById(R.id.button_ajout_depense));
        this.photoBtn = findViewById(R.id.button_prendre_photo);
        this.montant = findViewById(R.id.edittext_montant_depense);
        this.description = (EditText) (findViewById(R.id.edittext_description_depense));
        this.date = findViewById(R.id.date_picker_depense);
        this.depenseImage = findViewById(R.id.image_depense);
        this.sauvegarder_imagebtn = findViewById(R.id.button_sauvegarder_image);
        this.modifierDepensebtn.setEnabled(false);
        blocageBouton();

        this.dbDepense = new DatabaseDepense(this);

        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null

        //On recuperer les extra de l'ancienne activité
        Intent intent = getIntent();
        idDepense  = intent.getIntExtra("idDepense",-1);
        FournisseurExecutor.creerExecutor().execute(()-> {
            ancienneDepense = dbDepense.getDepense(idDepense);
        });

        // post -> ajoute les instructions à la suite de celles du main thread
        handler.post(() -> {
            // Notification du main thread pour qu'il mette à jour la UI
            // tout ce qui touche à la UI doit être exécuté dans le main thread !!!
            idCategorie = ancienneDepense.getCategorieId();

            // Initialize dateSelectionner to current date
            String dateStr = ancienneDepense.getDate();

            //On separe les elemeents de la date par ex 2023-04-27 deviens 2023   04   27 separer dans un tab
            String[] parts = dateStr.split("-");
            dateSelectionner = new String[]{(parts[2]),(parts[1]),(parts[0])};
            if(ancienneDepense.getCheminimage()!=null){
                bitmap = readImage(ancienneDepense.getCheminimage());
            }
            preremplirChamp();
        });


        sauvegarder_imagebtn.setOnClickListener(view -> {
            if(this.bitmap !=null){
                FournisseurExecutor.creerExecutor().execute(()-> {
                    saveBitmapToFile(bitmap,this);
                    handler.post(() -> {
                        Toast.makeText(this, "Le téléchargement de votre image va commencer! 😍", Toast.LENGTH_LONG).show();
                    });
                });
            }
            else {
                Toast.makeText(this, "Aucune image n'a été donnée, une erreur est survenue! 😥", Toast.LENGTH_LONG).show();
            }
        });

        listCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Récupération de l'ID de l'objet sélectionné
                int selectedItemId = (int) id;

                // Utilisation de l'ID pour effectuer des actions
                idCategorie = selectedItemId ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Code à exécuter lorsque aucun élément n'est sélectionné
            }
        });

        this.modifierDepensebtn.setOnClickListener(v -> {
            Depense depense= updatedepense();
            Toast.makeText(getApplicationContext(), "Votre dépense " + depense.getDescriptionDepense() + " d'un montant de " + depense.getMontant() + " a été modifié avec succés 👍🏼 " , Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(AffichageChangementDepenseActivity.this , HomeActivity.class);
            startActivity(intent1);
        });

        this.photoBtn.setOnClickListener(v -> captureImage());
        setPickersFromView();
    }

    private Depense updatedepense(){

        try {
            //On récupere les infos saisit par l'utilisateur
            int idUser = this.id_Utilisateur_Courant;
            String description =  this.description.getText().toString();
            double montant = Double.parseDouble(this.montant.getText().toString());
            int idCategorie =this.idCategorie ;
            String cheminimage = nomFichier  ;
            dateSelectionner[0] =  DateUtil.getFormattedDateTimeComponent(Integer.parseInt(dateSelectionner[0]));
            dateSelectionner[1] =  DateUtil.getFormattedDateTimeComponent(Integer.parseInt(dateSelectionner[1]));
            dateSelectionner[2] =  DateUtil.getFormattedDateTimeComponent(Integer.parseInt(dateSelectionner[2]));

            //format 2023-04-21
            String date = this.dateSelectionner[2] + "-" + this.dateSelectionner[1] + "-" + this.dateSelectionner[0] ;

            Depense depense;
            if(cheminimage ==null){
                cheminimage = ancienneDepense.getCheminimage();
            }

            depense = new Depense(date,montant,idUser,idCategorie,description,cheminimage);

            //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
            FournisseurExecutor.creerExecutor().execute(()->{
                dbDepense.updateDepense(depense,idDepense);
            });

            return  depense;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Camera
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bitmap = bp;
                this.depenseImage.setImageBitmap(bp);

                //Image sauvegarder dans le fichier
                String i = String.valueOf(Math.random() +10);
                nomFichier = description.getText().toString() + i ;

                saveImage(bp,nomFichier);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
            else if (requestCode == SAVE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri uri = data.getData();

                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "Félicitations, votre image a été téléchargée avec succès! 😉", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void blocageBouton() {
        //On verifie si tous les champs sont remplit pour qu'on puisse appuer sur le bouton save
        EditText[] editTexts = {montant, description, date}; // Ajoutez tous vos EditText ici
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
                        modifierDepensebtn.setEnabled(true);
                    } else {
                        modifierDepensebtn.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /*
     * Récupère les vues pour les pickers de date et les initialise en ajoutant les listeners correspondants.
     * Cette fonction doit être appelée dans la méthode onCreate de l'activité.
     */
    private void setPickersFromView() {
        date.setOnClickListener(this::showDatePicker);
    }

    /*
     * Affiche la pop-up de choix de date lorsqu'on clique sur le champ de date correspondant.
     * @param view La vue correspondant au champ de date.
     */
    private void showDatePicker(@NonNull View view) {
        final DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(this.getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    @Override
    public void onDateSet(int annee, int mois, int jour) {

        dateSelectionner[0] =  DateUtil.getFormattedDateTimeComponent(jour);
        dateSelectionner[1] =  DateUtil.getFormattedDateTimeComponent(mois);
        dateSelectionner[2] =  DateUtil.getFormattedDateTimeComponent(annee);

        String dateSelectionner = jour + "/" + mois + "/" +  annee;
        date.setText(dateSelectionner);
    }

    private void preremplirChamp(){
        listCategorie.setSelection(idCategorie);
        String i = String.valueOf(ancienneDepense.getMontant());
        montant.setText(i);
        description.setText(ancienneDepense.getDescriptionDepense());
        date.setText(ancienneDepense.getDate());

        if(ancienneDepense.getCheminimage()!=null){
            Bitmap imageDepense = readImage(ancienneDepense.getCheminimage());
            depenseImage.setImageBitmap(imageDepense);
        }
    }
}