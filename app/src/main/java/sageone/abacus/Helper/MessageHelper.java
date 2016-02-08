package sageone.abacus.Helper;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import sageone.abacus.R;

/**
 * Created by otomaske on 08.02.2016.
 */
public class MessageHelper extends Activity
{
    /**
     * Displays a snackbar on the given activity.
     * within is a snackbar position.
     *
     * @param activity
     * @param message
     */
    public static void snackbar(Activity activity, String message, int action)
    {
        try {
            final View v = activity.findViewById(R.id.snackbarPosition);
            Snackbar errorSnackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                    .setAction(action, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

            errorSnackbar.show();
        } catch (Exception e) {
            Log.e("Snackbar", "Snackbar position not found.");
        }
    }

    public static void snackbar(Activity activity, String message)
    {
        snackbar(activity, message, R.string.action_ok);
    }
}
