package sageone.abacus;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sageone.abacus.Exceptions.StatusCodeException;
import sageone.abacus.Exceptions.WebServiceFailureException;
import sageone.abacus.Interfaces.WebServiceListener;
import sageone.abacus.Models.Insurances;
import sageone.abacus.Models.InsurancesData;

/**
 *
 * @author Oliver Tomaske
 * @date 2016-01-25
 *
 */
public class InputAdapter extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, WebServiceListener {

    public static TextView              wage;
    public static RadioGroup            taxclass;
    public static Spinner               state;
    public static SeekBar               children;
    public static Button                calculate;
    public static AutoCompleteTextView  insuranceAc;

    public static boolean  relevantChange;

    private static double  wageDefaultValue = 2500;
    private ArrayAdapter<String> insurancesAdapter;
    private String[] insurancesList = new String[] {"Bitte warten"};

    private EventHandler eventHandler;
    private WebService webService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        eventHandler = new EventHandler(this, getApplicationContext());
        webService = WebService.getInstance(getApplicationContext(), this);

        _initializeElements();

        this._prepareWage();
        this._prepareState();
        this._prepareInsurance();
        this._prepareChildAllowance();
        this._prepareCalculate();

        this._initializeListener();
    }


    /**
     * Initializes all view elements
     * which was set on input activity.
     */
    private void _initializeElements()
    {
        wage  = (TextView) findViewById(R.id.wage);
        state = (Spinner) findViewById(R.id.state);
        taxclass = (RadioGroup) findViewById(R.id.taxclass);
        insuranceAc = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
        children = (SeekBar) findViewById(R.id.children);
        calculate = (Button) findViewById(R.id.calculate);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }


    /**
     * Initializes all switches a.s.o.
     */
    private void _initializeListener()
    {
        // switches
        SwitchCompat wageSwitch = (SwitchCompat) findViewById(R.id.wage_period);

        wageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchWageType(isChecked);
            }
        });

        SwitchCompat churchSwitch = (SwitchCompat) findViewById(R.id.church);

        churchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChurchType(isChecked);
            }
        });

    }

    /**
     * Prepares and handle the wage input.
     */
    private void _prepareWage()
    {
        wage.setText(String.valueOf(wageDefaultValue));
    }


    /**
     * Prepares and handles the state selection.
     */
    private void _prepareState()
    {
        String[] states = getResources().getStringArray(R.array.states);

        ArrayAdapter<String> statesDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, states);
        statesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(statesDataAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    /**
     * Initializes the service call.
     * After that the listener responseFinishInsurances
     * was triggered.
     */
    private void _prepareInsurance()
    {
        _initInsurancesAdapter();

        try {
            webService.Insurances();
        } catch (StatusCodeException e) {
            e.printStackTrace();
        } catch (WebServiceFailureException e) {
            e.printStackTrace();
        }
    }

    private void _initInsurancesAdapter()
    {
        insurancesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, this.insurancesList);

        insurancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insuranceAc.setAdapter(insurancesAdapter);
    }

    @Override
    /**
     *  Callback for insurances api call.
     */
    public void responseFinishInsurances(Insurances i)
    {
        String[] newList = new String[i.data.size()];
        for (int a = 0; a < i.data.size(); a++) {
            newList[a] = i.data.get(a).name;
        }
        insurancesList = newList;
        _initInsurancesAdapter();
    }

    /**
     * Prepares and handles the children allowance input.
     */
    private void _prepareChildAllowance()
    {
        SeekBar childrenSeekBar = (SeekBar) findViewById(R.id.children);
        final TextView childrenValue = (TextView) findViewById(R.id.children_value);
        childrenSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        float p = (float) progressValue / 2;
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
        wage = (TextView)findViewById(R.id.item_brutto);

        calculate = (Button)findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Handles the changement of wage period month and year.
     * @param v
     * @return
     */
    public void onWagePeriodClicked(View v)
    {
        boolean on = findViewById(R.id.wage_period).isActivated();
        TextView label = (TextView) findViewById(R.id.wage_type_label);
        Log.d("Notice", String.valueOf(on));
        label.setTextColor(Color.parseColor(String.valueOf(R.color.text_darkgrey)));
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