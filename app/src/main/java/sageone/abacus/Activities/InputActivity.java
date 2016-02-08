package sageone.abacus.Activities;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;

import android.util.Log;
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

import java.text.NumberFormat;
import java.util.Locale;

import sageone.abacus.EventHandler;
import sageone.abacus.Exceptions.StatusCodeException;
import sageone.abacus.Exceptions.ValidationException;
import sageone.abacus.Exceptions.WebServiceFailureException;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Interfaces.ApiCallbackListener;
import sageone.abacus.Models.CalculationData;
import sageone.abacus.Models.Insurances;
import sageone.abacus.R;
import sageone.abacus.WebService;

/**
 *
 * @author Oliver Tomaske
 * @date 2016-01-25
 *
 */
public class InputActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, ApiCallbackListener {

    public static TextView              wage;
    public static RadioGroup            taxclass;
    public static Spinner               state;
    public static SeekBar               children;
    public static Button                calculate;
    public static AutoCompleteTextView  insuranceAc;

    public static boolean  relevantChange;

    private ArrayAdapter<String> insurancesAdapter;
    private String[] insurancesList = new String[] {};

    private NumberFormat numberFormat;
    private EventHandler eventHandler;
    private WebService webService;

    private Snackbar errorSnackbar;

    private static final int INSURANCES_DEFAULT_SELECTION = 10;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
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
        wage        = (TextView) findViewById(R.id.wage);
        state       = (Spinner) findViewById(R.id.state);
        taxclass    = (RadioGroup) findViewById(R.id.taxclass);
        children    = (SeekBar) findViewById(R.id.children);
        calculate   = (Button) findViewById(R.id.calculate);
        insuranceAc = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
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
        wage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cur = wage.getText().toString();

                if (0 == cur.length() || hasFocus) {
                    return;
                }

                boolean hasComma = cur.contains(",");
                int dec = hasComma ? 100 : 1;

                Double current = Double.valueOf(cur.replaceAll("\\D", ""));
                String formatted = numberFormat.format(current / dec);

                wage.setText(formatted);
            }
        });
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
        insuranceAc.setText(R.string.insurance_initi_value);

        try {
            webService.Insurances();
        } catch (StatusCodeException e) {
            e.printStackTrace();
        } catch (WebServiceFailureException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualizes the view adapter.
     */
    private void _initInsurancesAdapter()
    {
        insurancesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, this.insurancesList);

        insurancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insuranceAc.setAdapter(insurancesAdapter);
    }

    private void _initInsurancesAdapter(int sel)
    {
        _initInsurancesAdapter();
        insuranceAc.setText(this.insurancesList[sel]);
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
        _initInsurancesAdapter(INSURANCES_DEFAULT_SELECTION);
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
        calculate = (Button)findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculationData calculationData = new CalculationData();
                _setData(calculationData);

                try {
                    calculationData.validate();
                } catch (ValidationException e) {
                    _snackbar(e.getMessage());
                }
            }
        });
    }


    /**
     * Set all relevant data for calculation.
     * @param data
     */
    private void _setData(CalculationData data)
    {
        data.Netto = null;
    }


    /**
     * Handles the changement of wage period month and year.
     * @param v
     * @return
     */
    public void onWagePeriodClicked(View v)
    {
        TextView label = (TextView) findViewById(R.id.wage_type_label);
        label.setTextColor(Color.parseColor(String.valueOf(R.color.text_darkgrey)));
    }

    /**
     * Shows a failed validation
     * @param message
     */
    private void _snackbar(String message)
    {
        MessageHelper.snackbar(this, message);
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