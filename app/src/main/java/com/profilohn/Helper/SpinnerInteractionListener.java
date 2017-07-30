package com.profilohn.Helper;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.profilohn.Activities.InputActivity;

/**
 * Created by RLange on 16.07.2017.
 */

public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    boolean userSelect = false;
    InputActivity inputActivity = null;

    public SpinnerInteractionListener (InputActivity ia) {
        inputActivity = ia;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            inputActivity.updateInsuranceBranches();
            userSelect = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
