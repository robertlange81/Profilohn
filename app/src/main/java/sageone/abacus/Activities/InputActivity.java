package sageone.abacus.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;

import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import sageone.abacus.Helper.CalculationInputHelper;
import sageone.abacus.Helper.DecimalDigitsInputHelper;
import sageone.abacus.Helper.EventHandler;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Interfaces.ApiCallbackListener;
import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.CalculationInput;
import sageone.abacus.Models.CalculationInputData;
import sageone.abacus.Models.Insurances;
import sageone.abacus.R;
import sageone.abacus.Models.WebService;

/**
 *
 * @author Oliver Tomaske
 * @date 2016-01-25
 *
 */
public class InputActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, ApiCallbackListener {

    public static RadioGroup            calcType;
    public static EditText              wage;
    public static CheckBox              wagePeriod;
    public static Spinner               taxClass;
    public static EditText              taxFree;
    public static Spinner               state;
    public static SeekBar               children;
    public static Button                calculate;
    public static AutoCompleteTextView  insuranceAc;
    public static SwitchCompat          churchTax;

    private Integer selectedInsuranceId = -1;
    private Double  selectedWage;
    private Double  selectedTaxFree = 0.0;
    private String  selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
    private String  selectedWagePeriod = CalculationInputHelper.WAGE_PERIOD_MONTH;
    private Boolean selectedChurchTax = false;
    private Integer selectedTaxClass = 0;
    private String  selectedState;
    private Double  selectedChildAmount = 0.0;

    private ArrayAdapter<String> insurancesAdapter;
    private List<String> insurancesList = new ArrayList<String>();
    private SortedMap<String, Integer> insurancesMap = new TreeMap<String, Integer>();

    private NumberFormat numberFormat;
    private EventHandler eventHandler;
    private WebService webService;
    private CalculationInputData data;

    public static InputActivity instance;
    public Dialog dialog;

    private CalculationInputHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        eventHandler = new EventHandler(this, getApplicationContext());
        webService = WebService.getInstance(getApplicationContext(), this);
        data = new CalculationInputData();
        helper = new CalculationInputHelper(this, data);

        _initializeElements();

        _prepareState();
        _prepareInsurance();

        _initializeListener();
        _initRequestedCalcType();


        instance = this;
    }


    /**
     * Select the requested cal type
     * given by previous activity.
     */
    private void _initRequestedCalcType()
    {
        int c = getIntent().getExtras().getInt("calc_type");
        switch (c) {
            case 1:
                calcType.check(R.id.type_gross);
                break;
            default:
                calcType.check(R.id.type_net);
        }
    }


    /**
     * Initializes all view elements
     * which was set on input activity.
     */
    private void _initializeElements()
    {
        calcType    = (RadioGroup) findViewById(R.id.type);
        wage        = (EditText) findViewById(R.id.wage);
        wagePeriod  = (CheckBox) findViewById(R.id.wage_period);
        state       = (Spinner) findViewById(R.id.state);
        taxClass    = (Spinner) findViewById(R.id.tax_class);
        taxFree     = (EditText) findViewById(R.id.tax_free);
        children    = (SeekBar) findViewById(R.id.children);
        calculate   = (Button) findViewById(R.id.calculate);
        insuranceAc = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
        churchTax   = (SwitchCompat) findViewById(R.id.church);

        wage.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});
        taxFree.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});

        ArrayAdapter<CharSequence> ta = ArrayAdapter.createFromResource(this,
                R.array.taxclasses, android.R.layout.simple_spinner_dropdown_item);
        ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxClass.setAdapter(ta);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }


    /**
     * Initializes user input event listeners for:
     * - calculation type
     * - wage period
     * - tax class
     * - state
     * - insurance
     * - church
     * - child amount
     */
    private void _initializeListener()
    {
        // wage type
        calcType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedWageType = (R.id.type_net == checkedId) ?
                        CalculationInputHelper.WAGE_TYPE_NET : CalculationInputHelper.WAGE_TYPE_GROSS;
            }
        });

        // wage amount
        wage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cur = wage.getText().toString();
                if (hasFocus) {
                    return;
                } else if (0 == cur.length()) {
                    selectedWage = 0.0;
                    return;
                }

                boolean hasComma = cur.contains(",");
                int dec = hasComma ? 100 : 1;

                Double current = Double.valueOf(cur.replaceAll("\\D", ""));
                current = current / dec;
                String formatted = numberFormat.format(current);

                wage.setText(formatted);
                selectedWage = current;
            }
        });

        // tax free amount
        taxFree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cur = taxFree.getText().toString();

                if (hasFocus) {
                    return;
                } else if (0 == cur.length()) {
                    selectedTaxFree = 0.0;
                    return;
                }

                boolean hasComma = cur.contains(",");
                int dec = hasComma ? 100 : 1;

                Double current = Double.valueOf(cur.replaceAll("\\D", ""));
                current = current / dec;
                String formatted = numberFormat.format(current);

                taxFree.setText(formatted);
                selectedTaxFree = current;
            }
        });

        // wage period
        wagePeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedWagePeriod = isChecked
                        ? CalculationInputHelper.WAGE_PERIOD_YEAR : CalculationInputHelper.WAGE_PERIOD_MONTH;
            }
        });

        taxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTaxClass = ++position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // states
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = state.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // health insurance
        insuranceAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView) view;
                String value = t.getText().toString();
                Integer companyNumber = insurancesMap.get(value);
                selectedInsuranceId = companyNumber != null ? Integer.valueOf(companyNumber) : -1;
            }
        });

        // church switch
        churchTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChurchType(isChecked);
                selectedChurchTax = isChecked;
            }
        });

        // child amount
        final TextView childrenValue = (TextView) findViewById(R.id.children_value);
        children.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedChildAmount = Double.valueOf(progress) / 2;
                childrenValue.setText(String.valueOf(selectedChildAmount));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // calculate button
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // triggers the formatting listeners
                wage.clearFocus();
                taxFree.clearFocus();

                if (!_setAndValidateData()) {
                    return;
                }
                dialog = MessageHelper.dialog(instance, true,
                        getResources().getString(R.string.calculation_started));
                dialog.show();
                CalculationInput ci = new CalculationInput(data);
                webService.Calculate(ci);
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
    }


    /**
     * Initializes the service call.
     */
    private void _prepareInsurance()
    {
        // disable and await response ..
        insuranceAc.setHint(getString(R.string.insurance_init_value));
        insuranceAc.setEnabled(false);
        webService.Insurances();
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


    /**
     * Init the adapter with a preset.
     * @param sel
     */
    private void _initInsurancesAdapter(int sel)
    {
        _initInsurancesAdapter();
        insuranceAc.setText(this.insurancesList.get(sel));
        selectedInsuranceId = Integer.valueOf(insurancesMap.get(this.insurancesList.get(sel)));
    }


    @Override
      /**
       *  Callback for insurances api call.
       */
      public void responseFinishInsurances(Insurances i)
    {
        for (int a = 0; a < i.data.size(); a++) {
            if(!insurancesMap.containsKey(i.data.get(a).name.replaceAll("\\s+$", "")))
                insurancesMap.put(i.data.get(a).name.replaceAll("\\s+$", ""), i.data.get(a).id);
        }

        // set the list for binding the array adapter
        insurancesList = new ArrayList<String>(insurancesMap.keySet());
        _initInsurancesAdapter(1);

        insuranceAc.setHint(getString(R.string.insurance_hint));
        insuranceAc.setEnabled(true);
    }


    @Override
    /**
     * What we do if calculation finished.
     */
    public void responseFinishCalculation(Calculation calculation)
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("Calculation", calculation);
        dialog.hide();

        startActivity(i);
    }


    @Override
    /**
     * What we do if calculation failed.
     */
    public void responseFailedCalculation(String message)
    {
        dialog.hide();
        MessageHelper.snackbar(this, message, Snackbar.LENGTH_INDEFINITE);
    }


    /**
     * Set all relevant data for calculation.
     */
    private boolean _setAndValidateData()
    {
        helper.data.Berechnungsart = selectedWageType;
        helper.data.Brutto = selectedWage;
        helper.data.Zeitraum = selectedWagePeriod;
        helper.data.AbrJahr = Calendar.getInstance().get(Calendar.YEAR);
        helper.data.StFreibetrag = selectedTaxFree;
        helper.data.StKl = selectedTaxClass;
        helper.setBundesland(selectedState);
        helper.data.KKBetriebsnummer = selectedInsuranceId;
        helper.data.Kirche = selectedChurchTax;
        helper.setKindFrei(selectedChildAmount);

        try {
            helper.validate();
            return true;
        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
            return false;
        }
    }

}