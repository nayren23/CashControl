package com.example.cashcontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    protected static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    protected static final int SAVE_IMAGE_REQUEST_CODE = 121;

    protected Bitmap bitmap ;

    protected void saveImage(Bitmap bp, String nomFichier){
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
    protected void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    /**
     * Cette méthode permet de sauvegarder un Bitmap dans un fichier en utilisant une activité pour sélectionner l'emplacement de stockage.
     * @param bitmap le Bitmap à sauvegarder
     * @param activity l'activité courante qui appelle cette méthode
     * Il est important de noter que cette méthode ne doit pas être appelée directement depuis le thread principal car elle démarre
     * une activité et attend le résultat de l'utilisateur. Il est donc recommandé de l'appeler à partir d'un thread de fond.
     */
    public void saveBitmapToFile(Bitmap bitmap, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TITLE, "image.png");

        activity.startActivityForResult(intent, SAVE_IMAGE_REQUEST_CODE);
    }

    protected Bitmap readImage(String nomFichier) {
        Bitmap bitmap = null;
        try {
            // Open stream to read file.
            FileInputStream in = new FileInputStream(this.getFilesDir()+"/"+nomFichier);

            // Decode file input stream into a bitmap.
            bitmap = BitmapFactory.decodeStream(in);

            // Close the input stream.
            in.close();

        } catch (Exception e) {
            Toast.makeText(this,"Error Impossible c'est une nouvelle instance de l'app:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }
}
