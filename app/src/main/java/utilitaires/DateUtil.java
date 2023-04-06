package utilitaires;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    private DateUtil() { }

    public static String getFormattedDateTimeComponent(int dateComponent) {
        return dateComponent >= 10 // On veut toujours avoir 2 chiffre pour un jour ou un mois.
                ? "" + dateComponent
                : "0" + dateComponent;
    }

    public static Date toDate(@NonNull String dateToConvert) {
        try {
            return DATE_FORMATTER.parse(dateToConvert);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(@Nullable DateUtil date) {
        return date != null
                ? DATE_FORMATTER.format(date)
                : "";
    }

    public static String toDate(@NonNull DateUtil date, @NonNull DateUtil time){
        String convertedDate = dateToString(date);
        return convertedDate + " ";
    }

}