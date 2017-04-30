package sageone.abacus.Helper;

import android.app.Activity;
import android.util.Log;

/**
 * Created by profilohn on 10.02.2016.
 */
public class SystemHelper
{
    public final static void finish(Activity a)
    {
        a.finish();
        System.exit(0);
    }
}
