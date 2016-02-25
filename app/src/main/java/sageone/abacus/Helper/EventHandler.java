package sageone.abacus.Helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import android.util.Log;

import sageone.abacus.R;

/**
 * Created by otomaske on 02.02.2016.
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
     * if the church tax selected.
     *
     * @param isChecked
     */
    public void OnSwitchChurchType(boolean isChecked)
    {
        TextView l = (TextView) _activity.findViewById(R.id.church_value);

        if (isChecked) {
            Log.i("Checked", isChecked + "");
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_darkgrey));
            l.setText(R.string.label_church_yes);
        } else {
            Log.i("Checked", isChecked + "");
            l.setTextColor(ContextCompat.getColor(_context, R.color.text_grey));
            l.setText(R.string.label_church_no);
        }
    }
}
