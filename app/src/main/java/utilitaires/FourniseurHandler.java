package utilitaires;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

public class FourniseurHandler {

    private static Handler handler ;

    public static Handler creerHandler(){
        if(handler == null)
            handler= HandlerCompat.createAsync(Looper.getMainLooper());
        return handler;
    }
}
