package sageone.abacus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Lange
 *
 */
public class InputActivity extends AppCompatActivity {

    public static TextView bruttolohn;
    public static Spinner stkl;
    public static Spinner bundesland;
    public static Spinner krankenkasse;
    public static Spinner kinder;
    public static Button start;
    public static boolean relevantChange;
    ArrayAdapter<String> adapter;

    private static double wageDefaultValue = 2500;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        this._prepareWage(wageDefaultValue);
        this._prepareTaxClass();
        this._prepareState();
        this._prepareHealthInsurance();
        this._prepareChildAllowance();
        this._prepareCalculate();
    }

    /**
     * Prepares and handle the wage input.
     */
    private void _prepareWage(double init)
    {
        final TextView wage = (TextView) findViewById(R.id.wage);
        wage.setText(String.valueOf(init));
        // Check if no view has focus:
        wage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent ke) {
                if ((ke.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // View view = InputActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    /**
     * Prepares and handles the tax card input actions.
     */
    private void _prepareTaxClass()
    {
        SeekBar taxSeekBar = (SeekBar) findViewById(R.id.taxclass);
        final TextView taxValue = (TextView) findViewById(R.id.taxclass_value);
        taxSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        taxValue.setText(String.valueOf(progressValue+1));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // When user starts working with control
                        // Save the progress if initial value is needed.
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // When user stops interacting
                        // Add your own logic here
                    }
                }
        );
    }

    /**
     * Prepares and handles the state selection.
     */
    private void _prepareState()
    {
        bundesland = (Spinner) findViewById(R.id.bundesland);

        List<String> bundeslandList = new ArrayList<String>();
        bundeslandList.add("Baden-Württemberg");
        bundeslandList.add("Bayern");
        bundeslandList.add("Berlin-Ost");
        bundeslandList.add("Berlin-West");
        bundeslandList.add("Brandenburg");
        bundeslandList.add("Bremen");
        bundeslandList.add("Hamburg");
        bundeslandList.add("Hessen");
        bundeslandList.add("Mecklenburg-Vorpommern");
        bundeslandList.add("Niedersachsen");
        bundeslandList.add("Nordrhein-Westfalen");
        bundeslandList.add("Rheinland-Pfalz");
        bundeslandList.add("Saarland");
        bundeslandList.add("Sachsen");
        bundeslandList.add("Sachsen-Anhalt");
        bundeslandList.add("Schleswig-Holstein");
        bundeslandList.add("Thüringen");
        ArrayAdapter<String> bundeslandDataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bundeslandList);
        bundeslandDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bundesland.setAdapter(bundeslandDataAdapter1);
        bundesland.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // relevantChange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void _prepareHealthInsurance()
    {
        krankenkasse = (Spinner) findViewById(R.id.krankenkasse);
        List<String>kkList = new ArrayList<String>();
        kkList.add("AOK");
        kkList.add("BKK");
        kkList.add("IKK");
        ArrayAdapter<String> sortByDataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kkList);
        sortByDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        krankenkasse.setAdapter(sortByDataAdapter1);
    }

    /**
     * Prepares and handles the children allowance input.
     */
    private void _prepareChildAllowance()
    {
        SeekBar childrenSeekBar = (SeekBar) findViewById(R.id.kinder);
        final TextView childrenValue = (TextView) findViewById(R.id.kinder_value);
        childrenSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        float p = (float) progressValue/2;
                        childrenValue.setText(String.valueOf(p));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // When user starts working with control
                        // Save the progress if initial value is needed.
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // When user stops interacting
                        // Add your own logic here
                    }
                }
        );
    }

    /**
     * Prepares and handle calculation button events.
     */
    private void _prepareCalculate()
    {
        InputActivity.bruttolohn = (TextView)findViewById(R.id.bruttolohn);

        start = (Button)findViewById(R.id.berechnen);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = InputActivity.bruttolohn.getText().toString();
                if (!InputActivity.bruttolohn.getText().toString().matches("[0-9]+([,.][0-9]{1,2})?")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(InputActivity.this).create();
                    alertDialog.setTitle("Fehler");
                    alertDialog.setMessage("Bitte geben Sie einen numerischen Wert bei Bruttolohn ein!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (relevantChange) {
            relevantChange = false;
            return;
        }
    }
}