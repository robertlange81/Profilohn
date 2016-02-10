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
     * @param action
     * @param duration
     */
    public static Snackbar snackbar(Activity activity, String message, int action, int duration)
    {
        Snackbar sb = null;

        try {
            final View v = activity.findViewById(R.id.snackbarPosition);
            sb = Snackbar.make(v, message, duration)
                    .setAction(action, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            sb.show();
        } catch (Exception e) {
            Log.e("Snackbar", "Snackbar position not found.");
        }

        return sb;
    }

    /**
     * Init easy snackbar.
     *
     * @param activity
     * @param message
     */
    public static void snackbar(Activity activity, String message)
    {
        snackbar(activity, message, R.string.action_ok, Snackbar.LENGTH_SHORT);
    }

    /**
     * Init snackbar with duration.
     *
     * @param activity
     * @param message
     * @param duration
     */
    public static void snackbar(Activity activity, String message, int duration)
    {
        snackbar(activity, message, R.string.action_ok, duration);
    }

    /**
     * Init snackbar with action and duration.
     *
     * @param activity
     * @param message
     * @param action
     * @param duration
     */
    public static void snackbar(Activity activity, String message, String action, int duration)
    {
        snackbar(activity, message, action, duration);
    }

}
