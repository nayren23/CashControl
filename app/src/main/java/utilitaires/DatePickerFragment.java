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


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = DatePickerFragment.class.getSimpleName();

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int dayOfMonth);
    }

    private OnDateSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), this, currentYear, currentMonth, currentDayOfMonth);
    }

    // Les mois du date picker commencent à 0 pour janvier, on applique ici un décalage de 1 pour gerer ce cas.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.onDateSet(year, month + 1, dayOfMonth);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnDateSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling context must be an activity and implement OnDateSetListener");
        }
    }
}