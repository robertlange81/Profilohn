package sageone.abacus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Lange
 *
 */
public class SettingsActivity extends Activity  {

    AlertDialog alertdlg;
    public static TextView bruttolohn;
    public static Spinner stkl, bundesland, krankenkasse, kist, kinder;
    public static Button start;
    public static boolean relevantChange;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final TextView bruttolohn = (TextView) findViewById(R.id.bruttolohn);
        // Check if no view has focus:
        bruttolohn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent ke) {
                if ((ke.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //View view = SettingsActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        stkl = (Spinner) findViewById(R.id.stkl);
        List<String> stklList = new ArrayList<String>();
        stklList.add("1");
        stklList.add("2");
        stklList.add("3");
        stklList.add("4");
        stklList.add("5");
        stklList.add("6");
        ArrayAdapter<String> fuelDataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stklList);
        fuelDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stkl.setAdapter(fuelDataAdapter1);
        stkl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                relevantChange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        krankenkasse = (Spinner) findViewById(R.id.krankenkasse);
        List<String>kkList = new ArrayList<String>();
        kkList.add("AOK");
        kkList.add("BKK");
        kkList.add("IKK");
        ArrayAdapter<String> sortByDataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kkList);
        sortByDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        krankenkasse.setAdapter(sortByDataAdapter1);

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

        kist = (Spinner) findViewById(R.id.kist);
        List<String> kistList = new ArrayList<String>();
        kistList.add("Ja");
        kistList.add("Nein");
        ArrayAdapter<String> kistDataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kistList);
        kistDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kist.setAdapter(kistDataAdapter1);

        kinder = (Spinner) findViewById(R.id.kinder);
        List<Float> kinderList = new ArrayList<Float>();
        kinderList.add(0f);
        kinderList.add(0.5f);
        kinderList.add(1f);
        kinderList.add(1.5f);
        kinderList.add(2f);
        kinderList.add(2.5f);
        kinderList.add(3f);
        kinderList.add(3.5f);
        kinderList.add(4f);
        kinderList.add(4.5f);
        kinderList.add(5f);
        ArrayAdapter<Float> kinderDataAdapter1 = new ArrayAdapter<Float>(this,
                android.R.layout.simple_spinner_item, kinderList);
        kinderDataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kinder.setAdapter(kinderDataAdapter1);

        SettingsActivity.bruttolohn = (TextView)findViewById(R.id.bruttolohn);

        start = (Button)findViewById(R.id.berechnen);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = SettingsActivity.bruttolohn.getText().toString();
                if (!SettingsActivity.bruttolohn.getText().toString().matches("[0-9]+([,.][0-9]{1,2})?")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
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