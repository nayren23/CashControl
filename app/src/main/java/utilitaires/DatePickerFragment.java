/**
 * Cette classe représente un fragment de sélection de date. Elle permet d'afficher une pop-up de sélection de date et d'obtenir la date sélectionnée par l'utilisateur.
 */
package utilitaires;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * Interface permettant de transmettre la date sélectionnée à la classe appelante.
     */
    public interface OnDateSetListener {
        void onDateSet(int year, int month, int dayOfMonth);
    }

    private OnDateSetListener listener;

    /**
     * Crée et renvoie une nouvelle instance de la boîte de dialogue de sélection de date.
     * @param savedInstanceState Le Bundle contenant l'état de la vue de l'activité.
     * @return La nouvelle instance de la boîte de dialogue de sélection de date.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), this, currentYear, currentMonth, currentDayOfMonth);
    }

    /**
     * Méthode appelée lorsque l'utilisateur sélectionne une date dans la boîte de dialogue.
     * @param view La vue du date picker.
     * @param annee L'année sélectionnée.
     * @param mois Le mois sélectionné.
     * @param jour Le jour sélectionné.
     */
    @Override
    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
        listener.onDateSet(annee, mois + 1, jour);
    }

    /**
     * Attache le listener à l'activité appelante.
     * @param context Le contexte d'appel.
     * @throws ClassCastException si le contexte d'appel n'implémente pas l'interface OnDateSetListener.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnDateSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Le contexte d'appel doit être une activité et implémenter OnDateSetListener");
        }
    }
}