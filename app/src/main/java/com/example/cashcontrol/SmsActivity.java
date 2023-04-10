package com.example.cashcontrol;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import BDD.DatabaseDepense;
import BDD.DatabaseUser;
import BDD.FourniseurHandler;
import BDD.FournisseurExecutor;
import modele.User;

public class SmsActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private static final String LOG_TAG = "AndroidExample";
    protected int id_Utilisateur_Courant;
    protected static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO"; //cles
    protected static final String SHARED_PREF_USER_INFO_ID = "SHARED_PREF_USER_INFO_ID"; //on recupere la valeur

    protected Handler handler;
    protected int sommeDepenseMois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On récupère l'ID de l'utilisateur courant stocké dans les préférences partagées.
        this.id_Utilisateur_Courant = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_ID, -1); // -1 pour vérifier si la case n'est pas null

        if (handler == null)
            handler = FourniseurHandler.creerHandler();
    }

    /**
     Cette méthode demande la permission d'envoyer un SMS à l'utilisateur et envoie un SMS avec les informations sur les dépenses du mois courant s'il y a une autorisation. Si l'utilisateur n'a pas encore accordé la permission, elle demande la permission à l'utilisateur.
     Si la version Android est supérieure ou égale à la version M (23), l'autorisation doit être demandée à l'utilisateur.
     Si la demande d'autorisation a été approuvée, la méthode appelle la méthode sendSMS_by_smsManager() sur un thread de fond pour envoyer le SMS.
     */
    protected void askPermissionAndSendSMS() {
        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) { // 23
            // Check if we have send SMS permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.SEND_SMS);
            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                handler.post(() -> {
                    this.requestPermissions(
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSION_REQUEST_CODE_SEND_SMS
                    );
                });
                return;
            }
        }
        // Execute sendSMS_by_smsManager on a background thread
        sendSMS_by_smsManager();
    }

    /**
     Cette fonction permet d'envoyer un SMS avec le montant total des dépenses effectuées pendant le mois en cours
     à un numéro de téléphone prédéfini.
     @throws SecurityException si l'application n'a pas la permission d'envoyer des SMS
     */
    protected void sendSMS_by_smsManager()  {
        FournisseurExecutor.creerExecutor().execute(()-> {

            //On récupere le numéro de téléphone dans la BDD(attention il est sous le format 06 85 88 99 66)
            DatabaseUser databaseUser = new DatabaseUser(this);
            User userAcuel = databaseUser.getUser(id_Utilisateur_Courant);
            String phoneUserAcuel = userAcuel.getNumerotelephone().substring(1);// on enleve donc le 0 devant

            String phoneNumber = "+33" + phoneUserAcuel; //On met l'indicatif à l'avance, l'indicatif pour le tel android studio est 15
            String message = "Salut ! L'équipe Cash Control a remarqué que vos dépenses mensuelles s'élevaient à " + sommeDepenseMois + "€ , et nous souhaitions vous en informer." ;

            try {
                // Récupération de l'instance par défaut du SmsManager
                SmsManager smsManager = SmsManager.getDefault();

                // Envoi du SMS sur un thread séparé
                FournisseurExecutor.creerExecutor().execute(()-> {
                    smsManager.sendTextMessage(phoneNumber,
                            null,
                            message,
                            null,
                            null);
                });
                // Affichage d'un message de confirmation à l'utilisateur
                Log.i( LOG_TAG,"Votre message a été envoyé avec succès !");
                handler.post(() -> {
                    Toast.makeText(getApplicationContext(), "Votre message a été envoyé avec succès !",
                            Toast.LENGTH_LONG).show();
                });

            } catch (Exception ex) {
                // En cas d'erreur lors de l'envoi du SMS, affichage d'un message d'erreur à l'utilisateur
                Log.e( LOG_TAG,"Votre sms a échoué...", ex);
                handler.post(() -> {
                    Toast.makeText(getApplicationContext(), "Votre sms a échoué..." + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
                ex.printStackTrace();
            }
        });
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_SEND_SMS: {
                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i( LOG_TAG,"Permission accordée !");
                    Toast.makeText(this, "Permission accordée !", Toast.LENGTH_LONG).show();
                    // Execute askPermissionAndSendSMS on a background thread
                    askPermissionAndSendSMS();
                }
                // Cancelled or denied.
                else {
                    Log.i( LOG_TAG,"Permission refusée !");
                    Toast.makeText(this, "Permission refusée !", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    /*// When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_SEND_SMS) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
*/

}
