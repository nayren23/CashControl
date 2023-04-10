package com.example.cashcontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import BDD.DatabaseDepense;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.Depense;
import utilitaires.DateUtil;


public class AjoutDepenseActivity extends ImageActivity implements DatePickerFragment.OnDateSetListener {

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    private static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur

    private Spinner listCategorie;
    private Button ajoutDepensebtn;
    private Button photoBtn;
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
    private boolean tousremplis ;

    private Button button_lire_description;

    private Button button_lire_categorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_depense_activity);

        this.listCategorie = findViewById(R.id.category_spinner);
        this.ajoutDepensebtn = (Button) (findViewById(R.id.button_ajout_depense));
        this.photoBtn = findViewById(R.id.button_prendre_photo);
        this.montant = findViewById(R.id.edittext_montant_depense);
        this.description = (EditText) (findViewById(R.id.edittext_description_depense));
        this.date = findViewById(R.id.date_picker_depense);
        this.button_lire_description = findViewById(R.id.button_lire_description);
        this.button_lire_categorie = findViewById(R.id.button_lire_categorie);

        //lecture du texte
        button_lire_description.setOnClickListener(v ->speak(description.getText().toString()));

        button_lire_categorie.setOnClickListener(v ->speak(listCategorie.getItemAtPosition(listCategorie.getSelectedItemPosition()).toString()));

        
        this.dbDepense = new DatabaseDepense(this);
        this.depenseImage = findViewById(R.id.image_depense);
        this.ajoutDepensebtn.setEnabled(false);
        blocageBouton();
        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null

        // Initialize dateSelectionner to current date
        Calendar calendar = Calendar.getInstance();

        dateSelectionner = new String[]{String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))};

        //On creer le handler avec le execute
        if(handler == null)
            handler = FourniseurHandler.creerHandler();

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

        this.ajoutDepensebtn.setOnClickListener(v -> {
            Depense depense= ajouterdepense();
            Toast.makeText(getApplicationContext(), "Votre dépense " + depense.getDescriptionDepense() + " d'un montant de " + depense.getMontant() + " a été ajoutée avec succés 👍🏼 " , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AjoutDepenseActivity.this , HomeActivity.class);
            startActivity(intent);
        });

        this.photoBtn.setOnClickListener(v -> captureImage());

        setPickersFromView();
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
                        ajoutDepensebtn.setEnabled(true);
                    } else {
                        ajoutDepensebtn.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private Depense ajouterdepense(){

        //On récupere les infos saisit par l'utilisateur
        int idUser = this.id_Utilisateur_Courant;
        String description =  this.description.getText().toString();
        double montant = Double.parseDouble(this.montant.getText().toString());
        int idCategorie =this.idCategorie;

        String cheminimage = nomFichier;
        String date = this.dateSelectionner[2] + "-" + this.dateSelectionner[1] + "-" + this.dateSelectionner[0];

        Depense depense = new Depense(date,montant,idUser,idCategorie,description,cheminimage);

        //Threads pour ne pas bloquer le thread principale, toute les grosses opérations de la BDD
        FournisseurExecutor.creerExecutor().execute(()->{
            this.dbDepense.addDepense(depense);
        });
        return  depense;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Camera
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            System.out.println("c'est presque bon chef");

            if (resultCode == RESULT_OK) {
                System.out.println("c'est bon chef");
                Bitmap bp = (Bitmap) data.getExtras().get("data");
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
}