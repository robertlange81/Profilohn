package com.profilohn.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.profilohn.R;

/**
 * Created by profilohn on 02.02.2016.
 */
public class EventHandler extends AppCompatActivity {

    private Activity _activity;
    private Context _context;

    public EventHandler(Activity activity, Context context)
    {
        _activity = activity;
        _context = context;
    }

    /**
     * Displays the church value text field
     * if the has children selected.
     *
     * @param isChecked
     */
    public void OnSwitchPeriodType(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.wage_period_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }


    /**
     * Displays the church value text field
     * if the church tax selected.
     *
     * @param isChecked
     */
    public void OnSwitchChurchType(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.church_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }

    /**
     * Displays the church value text field
     * if the has children selected.
     *
     * @param isChecked
     */
    public void OnSwitchChildren(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.children_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }

    /**
     * Displays the isShifting value text field
     * if the isShifting selected.
     *
     * @param isChecked
     */
    public void OnSwitchShifting(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.shifting_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }

    /**
     * Displays the isSeizure value text field
     * if the isSeizure selected.
     *
     * @param isChecked
     */
    public void OnSwitchSeizure(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.seizure_value);

        if (isChecked) {
            l.setTextColor(Color.RED);
            l.setText(R.string.label_yes);
        } else {
            int green = Color.parseColor("#008000");
            l.setTextColor(green);
            l.setText(R.string.label_no);
        }
    }

    /**
     * Displays the provision value (Altersvorsorge) text field
     * if the has provision is selected.
     *
     * @param isChecked
     */
    public void OnSwitchProvision(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.retprov_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }

    /**
     * Displays the car value (Firmenwagen) text field
     * if the has car is selected.
     *
     * @param isChecked
     */
    public void OnSwitchCar(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.car_value);

        if (isChecked) {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_yes);
        } else {
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_no);
        }
    }

    /**
     * Hides the input keyboard.
     */
    public void hideKeyboardInput(InputMethodManager imm)
    {
        View view = _activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
