/**
 * La classe DateUtil fournit des méthodes pour convertir des dates au format "dd/MM/yyyy" en objets Date et vice versa.
 */
package utilitaires;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * Format de date utilisé pour la conversion des dates.
     */
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private DateUtil() { }

    /**
     * Retourne le composant de date au format string formaté avec 2 chiffres.
     * @param dateComponent le composant de date à formater
     * @return une chaîne de caractères représentant le composant de date formaté.
     */
    public static String getFormattedDateTimeComponent(int dateComponent) {
        return dateComponent >= 10 // On veut toujours avoir 2 chiffre pour un jour ou un mois.
                ? "" + dateComponent
                : "0" + dateComponent;
    }


    /**
     * Convertit une chaîne de caractères représentant une date au format "dd/MM/yyyy" en objet Date.
     * @param dateToConvert la date à convertir
     * @return l'objet Date représentant la date passée en paramètre, ou null si la conversion a échoué.
     */
    public static Date toDate(@NonNull String dateToConvert) {
        try {
            return DATE_FORMATTER.parse(dateToConvert);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Convertit un objet Date en une chaîne de caractères au format "dd/MM/yyyy".
     * @param date la date à convertir
     * @return une chaîne de caractères représentant la date au format "dd/MM/yyyy", ou une chaîne vide si la date est nulle.
     */
    public static String dateToString(@Nullable DateUtil date) {
        return date != null
                ? DATE_FORMATTER.format(date)
                : "";
    }


    /**
     * Convertit les objets DateUtil de date et de temps en une seule chaîne de caractères.
     * @param date la date à convertir
     * @param time le temps à convertir
     * @return une chaîne de caractères représentant la date et le temps combinés.
     */
    public static String toDate(@NonNull DateUtil date, @NonNull DateUtil time){
        String convertedDate = dateToString(date);
        return convertedDate + " ";
    }
}