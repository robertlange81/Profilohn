package sageone.profilohn.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.Vector;

import sageone.profilohn.R;

/**
 * Created by profilohn on 08.02.2016.
 */
public class MessageHelper extends Activity {

    public static final int DIALOG_TYPE_ALERT = 0;
    public static final int DIALOG_TYPE_INFO = 1;

    public static final Vector<AlertDialog> dialogs = new Vector<AlertDialog>();

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


    /**
     * AlertDialog without message.
     *
     * @param activity
     * @param modal
     * @return
     */
    public static AlertDialog dialog(final Activity activity, boolean modal)
    {
        return dialog(activity, modal, "", DIALOG_TYPE_ALERT);
    }


    /**
     * AlertDialog with message
     * but without type.
     *
     * @param activity
     * @param modal
     * @return
     */
    public static AlertDialog dialog(final Activity activity, boolean modal, String message)
    {
        return dialog(activity, modal, message, DIALOG_TYPE_ALERT);
    }


    /**
     * Instantiates an AlertDialog.
     *
     * @param activity
     * @param modal
     * @param message
     * @return
     */
    public static AlertDialog dialog(final Activity activity, boolean modal, String message, int type)
    {
        AlertDialog.Builder db = new AlertDialog.Builder(activity);

        if (type == DIALOG_TYPE_ALERT) {
            db.setNegativeButton(
                    activity.getResources().getString(android.R.string.cancel)
                    , new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SystemHelper.finish(activity);
                        }
                    }
            );
        } else {
            db.setPositiveButton(
                    activity.getResources().getString(android.R.string.ok)
                    , new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
        }

        AlertDialog d = db.setMessage(message)
                .setCancelable(modal ? false : true)
                .create();

        return d;
    }


    /**
     * Dismiss all registered
     * and open dialogs.
     */
    public static void dismissAllDialogs()
    {
        for (AlertDialog dialog : dialogs)
            if (dialog.isShowing()) dialog.dismiss();
    }

}