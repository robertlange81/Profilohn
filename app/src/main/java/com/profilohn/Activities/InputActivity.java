package com.profilohn.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.profilohn.Exceptions.ValidationException;
import com.profilohn.Exceptions.ValidationInsuranceException;
import com.profilohn.Helper.CalculationInputHelper;
import com.profilohn.Helper.EventHandler;
import com.profilohn.Helper.MessageHelper;
import com.profilohn.Helper.SpinnerInteractionListener;
import com.profilohn.Interfaces.ApiCallbackListener;
import com.profilohn.Models.Calculation;
import com.profilohn.Models.CalculationInput;
import com.profilohn.Models.CalculationInputData;
import com.profilohn.Helper.FileStore;
import com.profilohn.Models.Insurances;
import com.profilohn.R;
import com.profilohn.Models.WebService;
import android.webkit.WebView;

/**
 *
 * @author Robert Lange
 * @date 2016-01-25
 *
 */
public class InputActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, ApiCallbackListener {

    public static RadioGroup            calcType;
    public static EditText              wage;
    public static EditText              taxFree;
    public static Spinner               taxClass;
    public static Spinner               year;
    public static Spinner               state;
    public static Spinner               employeeType;
    public static Spinner               children;
    public static Spinner               kv;
    public static Spinner               rv;
    public static Spinner               av;
    public static Spinner               pv;
    public static Button                calculate;
    public static Button                calculateTop;
    public static AutoCompleteTextView  insuranceAc;
    public static SwitchCompat          wagePeriod;
    public static SwitchCompat          hasChildren;
    public static SwitchCompat          churchTax;

    private Integer selectedInsuranceId = -1;
    private Double  selectedWage;
    private Double  selectedTaxFree = 0.0;
    private String  selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
    private String  selectedWagePeriod = CalculationInputHelper.WAGE_PERIOD_MONTH;
    private Boolean selectedHasChildren = false;
    private Boolean selectedChurchTax = false;
    private Integer selectedTaxClass = 0;
    private Integer selectedEmployeeType = 0;
    private int selectedKV = 1;
    private int selectedRV = 1;
    private int selectedAV = 1;
    private int selectedPV = 1;
    private Integer selectedYear = 1;
    private String  selectedState;
    private Double  selectedChildAmount = 0.0;
    private ArrayAdapter<String> _yearAdapter;
    private ArrayAdapter<String> _statesDataAdapter;
    private ArrayAdapter<CharSequence> _employeeTypeDataAdapter;
    private ArrayAdapter<CharSequence> _taxclassAdapter;
    private ArrayAdapter<CharSequence> _childFreeAmountAdapter;
    private ArrayAdapter<String> _insurancesAdapter;
    private ArrayAdapter<CharSequence> _kvclassAdapter;
    private ArrayAdapter<CharSequence> _rvclassAdapter;
    private ArrayAdapter<CharSequence> _avclassAdapter;
    private ArrayAdapter<CharSequence> _pvclassAdapter;
    private List<String> insurancesList = new ArrayList<String>();
    private SortedMap<String, Integer> insurancesMap = new TreeMap<String, Integer>();

    private NumberFormat numberFormat;
    private EventHandler eventHandler;
    private WebService webService;
    private CalculationInputData data;
    private TextView wageAmountLabel;

    public static InputActivity instance;
    public PopupWindow calcPopup;
    public Dialog calcDialog;

    private CalculationInputHelper helper;

    private WebView spamWebView;

    private boolean changeFocusByCode = false;

    Map<Integer, Double> percent    = new HashMap<Integer, Double>() {{
        put(2016, 0.01175);
        put(2017, 0.01275);
    }};
    Map<Integer, Double> bbg_kv = new HashMap<Integer, Double>() {{
        put(2016, 4237.50);
        put(2017, 4350.00);
    }};
    Map<Integer, Double> bbg_rv_west = new HashMap<Integer, Double>() {{
        put(2016, 6200.00);
        put(2017, 6350.00);
    }};
    Map<Integer, Double> bbg_rv_ost = new HashMap<Integer, Double>() {{
        put(2016, 5400.00);
        put(2017, 5700.00);
    }};
    double add_no_kids  = 0.0025;
    double add_saxony   = 0.005;

    Double pvag = new Double(0);
    Double pvan = new Double(0);

    Double ag_alt = new Double(0);
    Double an_alt = new Double(0);

    public class EmployeeTypeListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (userSelect) {
                // Your selection handling code here
                userSelect = false;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        eventHandler = new EventHandler(this, getApplicationContext());
        webService = new WebService(getApplicationContext(), this);
        data = new CalculationInputData();
        helper = new CalculationInputHelper(this, data);

        _initializeElements();

        _prepareState();
        _prepareInsurance();

        _initializeListener();
        _initRequestedCalcType();

        _loadCachedInputs();

        instance = this;

        spamWebView =(WebView) findViewById(R.id.webview);
        spamWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
        }});
        spamWebView.setVisibility(View.INVISIBLE);
        spamWebView.loadUrl("http://www.google.com");
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        wage.requestFocus();
        dismissCalculationOverlay();
    }


    /**
     * On click listener that
     * finish the activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        this.finish();
        return true;
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
        calcType        = (RadioGroup) findViewById(R.id.type);
        wage            = (EditText) findViewById(R.id.wage);
        wagePeriod      = (SwitchCompat) findViewById(R.id.wage_period);
        calculateTop    = (Button) findViewById(R.id.hello_start_calculation_net);
        state           = (Spinner) findViewById(R.id.state);
        employeeType    = (Spinner) findViewById(R.id.employee_type);
        taxClass        = (Spinner) findViewById(R.id.tax_class);
        year            = (Spinner) findViewById(R.id.year);
        taxFree         = (EditText) findViewById(R.id.tax_free);
        children        = (Spinner) findViewById(R.id.children);
        calculate       = (Button) findViewById(R.id.calculate);

        kv              = (Spinner) findViewById(R.id.kv_value);
        rv              = (Spinner) findViewById(R.id.rv_value);
        av              = (Spinner) findViewById(R.id.av_value);
        pv              = (Spinner) findViewById(R.id.pv_value);

        insuranceAc     = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
        churchTax       = (SwitchCompat) findViewById(R.id.church);
        hasChildren     = (SwitchCompat) findViewById(R.id.has_children);

        wageAmountLabel = (TextView) findViewById(R.id.wageamount_label);

        //wage.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});
        //taxFree.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});

        // tax classes
        _taxclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.taxclasses, R.layout.spinner_right_item);
        _taxclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        taxClass.setAdapter(_taxclassAdapter);

        // calc year
        String[] years = new String[]{
                (new Integer(Calendar.getInstance().get(Calendar.YEAR)-1)).toString(),
                (new Integer(Calendar.getInstance().get(Calendar.YEAR))).toString(),
                (new Integer(Calendar.getInstance().get(Calendar.YEAR)+1)).toString()
        };
        final List<String> yearsList = new ArrayList<>(Arrays.asList(years));
        _yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsList);

        _yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(_yearAdapter);
        year.setSelection(1);

        // insurance classes
        _kvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.kv, R.layout.spinner_right_item);
        _kvclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        kv.setAdapter(_kvclassAdapter);

        _rvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.rv, R.layout.spinner_right_item);
        _rvclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        rv.setAdapter(_rvclassAdapter);

        _avclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.av, R.layout.spinner_right_item);
        _avclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        av.setAdapter(_avclassAdapter);

        _pvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.pv, R.layout.spinner_right_item);
        _pvclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        pv.setAdapter(_pvclassAdapter);

        // employee type
        _employeeTypeDataAdapter = ArrayAdapter.createFromResource(this,
                R.array.employeetypes, android.R.layout.simple_spinner_dropdown_item);
        _taxclassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeType.setAdapter(_employeeTypeDataAdapter);

        // child free amount
        _childFreeAmountAdapter = ArrayAdapter.createFromResource(this,
                R.array.childfree, R.layout.spinner_right_item);
        _taxclassAdapter.setDropDownViewResource(R.layout.spinner_right_item);
        children.setAdapter(_childFreeAmountAdapter);
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
        // TODO: Fehler Ergebnisse AN Sozialabgaben des AG
        // Art der Abrechnung (Wunsch-Netto oder Brutto)
        calcType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                wage.clearFocus();
                taxFree.clearFocus();

                if (R.id.type_net == checkedId) {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
                    if(wagePeriod.isChecked()) {
                        wageAmountLabel.setText(R.string.wageamount_gross_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_gross_month);
                    }
                } else {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_NET;
                    if(wagePeriod.isChecked()) {
                        wageAmountLabel.setText(R.string.wageamount_net_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_net_month);
                    }
                }
            }
        });

        // Betrag (Brutto oder Wunsch-Netto)
        wage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!wage.hasFocus() && !changeFocusByCode) {
                    try {
                        String cur = wage.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");
                        Double current = Double.valueOf(cur);
                        selectedWage = current;

                        numberFormat.setMaximumFractionDigits(2);
                        String output = numberFormat.format(current);

                        wage.setText(output);

                    } catch (Exception x) {
                        String mist = "Mist";
                    } finally {

                    }
                }
            }
        });

        // Betrag (Brutto oder Wunsch-Netto)
        wage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Steuerfreibetrag
        taxFree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!taxFree.hasFocus()) {
                    try {
                        String cur = taxFree.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+", "");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");
                        Double current = Double.valueOf(cur);
                        selectedTaxFree = current;

                        numberFormat.setMaximumFractionDigits(2);
                        String output = numberFormat.format(current);

                        taxFree.setText(output);
                    } catch (Exception x) {
                        String mist = "Mist";
                    } finally {

                    }
                }
        }});

        // Periodenbezug (Monat / Jahr)
        wagePeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                wage.clearFocus();
                taxFree.clearFocus();

                eventHandler.OnSwitchPeriodType(isChecked);

                selectedWagePeriod = isChecked
                        ? CalculationInputHelper.WAGE_PERIOD_YEAR : CalculationInputHelper.WAGE_PERIOD_MONTH;

                if (!selectedWageType.equals(null) && selectedWageType == CalculationInputHelper.WAGE_TYPE_GROSS) {
                    if(isChecked) {
                        wageAmountLabel.setText(R.string.wageamount_gross_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_gross_month);
                    }
                } else {
                    if(isChecked) {
                        wageAmountLabel.setText(R.string.wageamount_net_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_net_month);
                    }
                }
            }
        });

        SpinnerInteractionListener sal = new SpinnerInteractionListener(this);
        employeeType.setOnItemSelectedListener(sal);
        employeeType.setOnTouchListener(sal);

        kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 wage.clearFocus();
                 taxFree.clearFocus();

                 switch(position) {
                     case 0: // kein
                         selectedKV = 0;
                         break;
                     case 1: // allgemeiner
                         selectedKV = 1;
                         break;
                     case 2: // ermäßigter
                         selectedKV = 3;
                         break;
                     case 3: // Pauschalbeitrag
                         selectedKV = 6;
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
             }
        });

        rv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wage.clearFocus();
                taxFree.clearFocus();

                switch(position) {
                    case 0: // kein
                        selectedRV = 0;
                        break;
                    case 1: // voller
                        selectedRV = 1;
                        break;
                    case 2: // halber
                        selectedRV = 3;
                        break;
                    case 3: // Pauschalbeitrag
                        selectedRV = 5;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        av.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wage.clearFocus();
                taxFree.clearFocus();

                switch(position) {
                    case 0: // kein
                        selectedAV = 0;
                        break;
                    case 1: // voller
                        selectedAV = 1;
                        break;
                    case 2: // halber
                        selectedAV = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wage.clearFocus();
                taxFree.clearFocus();

                switch(position) {
                    case 0: // kein
                        selectedPV = 0;
                        break;
                    case 1: // voller
                        selectedPV = 1;
                        break;
                    case 2: // halber
                        selectedPV = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Steuerklasse
        taxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wage.clearFocus();
                taxFree.clearFocus();
                selectedTaxClass = ++position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Abrechnungsjahr
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = position;

                wage.clearFocus();
                taxFree.clearFocus();
                if(selectedEmployeeType > 5 ) {
                    if(selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1 <= 2016) {
                        selectedAV = 2;
                        av.setSelection(2);
                    } else {
                        selectedAV = 0;
                        av.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Bundesländer
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = state.getSelectedItem().toString();

                wage.clearFocus();
                taxFree.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Beschäftigungsverhältnis
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = state.getSelectedItem().toString();

                wage.clearFocus();
                taxFree.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Kirchensteuer (ja / nein)
        hasChildren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChildren(isChecked);
                selectedHasChildren = isChecked;

                wage.clearFocus();
                taxFree.clearFocus();
            }
        });

        // Krankenkasse
        insuranceAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Insurance", "onItemClick");
                GetInsuranceId();

                wage.clearFocus();
                taxFree.clearFocus();
            }
        });

        // Kirchensteuer (ja / nein)
        churchTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChurchType(isChecked);
                selectedChurchTax = isChecked;

                wage.clearFocus();
                taxFree.clearFocus();
            }
        });

        // Kinderfreibetrag
        children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChildAmount = Double.valueOf(position) / 2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Logik zur Abrechnung - Listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wage.clearFocus();
                taxFree.clearFocus();
                insuranceAc.clearFocus();

                if (!_setAndValidateData()) {
                    return;
                }

                InputActivity.this._cacheInputs(data);

                eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                showCalculationOverlay();

                // do the calculation delayed for advertising
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        CalculationInput ci = new CalculationInput(data);
                        webService.Calculate(ci);
                    }

                }, getResources().getInteger(R.integer.calculation_timeout));
            }
        };

        // Abrechnungs-Button oben
        calculateTop.setOnClickListener(listener);

        // Abrechnungs-Button unten
        calculate.setOnClickListener(listener);
    }

    private void GetInsuranceId() {
        String value = insuranceAc.getText().toString();
        Integer companyNumber = insurancesMap.get(value);
        selectedInsuranceId = companyNumber != null ? Integer.valueOf(companyNumber) : -1;
    }

    public void updateInsuranceBranches() {
        selectedEmployeeType = employeeType.getSelectedItemPosition();

        switch (selectedEmployeeType) {
            case 0: // normal
                selectedKV = 1;
                selectedRV = 1;
                selectedAV = 1;
                selectedPV = 1;
                kv.setSelection(1);
                rv.setSelection(1);
                av.setSelection(1);
                pv.setSelection(1);
                break;
            case 1: // Minijobber
                selectedKV = 6;
                selectedRV = 5;
                selectedAV = 0;
                selectedPV = 0;
                kv.setSelection(3);
                rv.setSelection(3);
                av.setSelection(0);
                pv.setSelection(0);
                insuranceAc.setText("Knappschaft geringf. Beschäftigte");
                break;
            case 2: // Minijobber mit RV
                selectedKV = 6;
                selectedRV = 1;
                selectedAV = 0;
                selectedPV = 0;
                kv.setSelection(3);
                rv.setSelection(1);
                av.setSelection(0);
                pv.setSelection(0);
                insuranceAc.setText("Knappschaft geringf. Beschäftigte");
                break;
            case 3: // privat versichert
                selectedKV = 0;
                selectedRV = 1;
                selectedAV = 1;
                selectedPV = 0;
                kv.setSelection(0);
                rv.setSelection(1);
                av.setSelection(1);
                pv.setSelection(0);
            case 4: // kurzfristig beschäftigt
                selectedKV = 0;
                selectedRV = 0;
                selectedAV = 0;
                selectedPV = 0;
                kv.setSelection(0);
                rv.setSelection(0);
                av.setSelection(0);
                pv.setSelection(0);
                break;
            case 5: // Rentner
                selectedKV = 3;
                selectedRV = 3;
                selectedPV = 1;
                kv.setSelection(2);
                rv.setSelection(2);
                if(selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1 <= 2016) {
                    selectedAV = 2;
                    av.setSelection(2);
                } else {
                    selectedAV = 0;
                    av.setSelection(0);
                }
                pv.setSelection(1);
                break;
            case 6: // Flexi-Rentner
                selectedKV = 3;
                selectedRV = 1;
                selectedPV = 1;
                kv.setSelection(2);
                rv.setSelection(1);
                if(selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1 <= 2016) {
                    selectedAV = 2;
                    av.setSelection(2);
                } else {
                    selectedAV = 0;
                    av.setSelection(0);
                }
                pv.setSelection(1);
                break;
            default:
                // volle Versicherung
                break;

        }
    }


    /**
     * Prepares and handles the state selection.
     */
    private void _prepareState()
    {
        String[] states = getResources().getStringArray(R.array.states);

        _statesDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, states);
        _statesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(_statesDataAdapter);
    }

    /**
     * Initializes last inputs
     */
    private void _cacheInputs(CalculationInputData data)
    {
        FileStore fileStore = new FileStore(this);
        CalculationInputData i = null;
        try {
            fileStore.writeInput(data);
        } catch (Exception e) {
            // todo
        }
    }


    /**
     * Initializes last inputs
     */
    private void _loadCachedInputs()
    {
        Log.w("i", "Wo ist der Debugger?");
        FileStore fileStore = new FileStore(this);
        CalculationInputData i = null;
        try {
            i = fileStore.readInput();

            if(i != null) {
                // Brutto / Nettobetrag
                wage.requestFocus();
                if(i.Brutto != null) {
                    wage.setText(i.Brutto.toString());
                } else {
                    wage.setText("0,00 €");
                }
                selectedWage = i.Brutto;
                wage.clearFocus();

                // Jahr oder Monat
                wagePeriod.setChecked(i.Zeitraum.equalsIgnoreCase("y"));

                // Krankenkasse
                if(!i.dummyInsurance) {
                    for (String entry : insurancesMap.keySet()) {
                        Integer id = insurancesMap.get(entry);
                        if(id.equals(i.KKBetriebsnummer)) {
                            insuranceAc.setText(entry);
                            selectedInsuranceId = id;
                            break;
                        }
                    }
                }

                // Bundesland
                String stateString = CalculationInputHelper.retranslateState(i.Bundesland);
                state.setSelection(
                        _statesDataAdapter.getPosition(
                                stateString
                        )
                );
                selectedState = stateString;

                // Beschäftigungsverhältnis
                employeeType.setSelection(i.Beschaeftigungsart);
                selectedEmployeeType = i.Beschaeftigungsart;

                // kv
                int set = i.KV;
                if(i.KV == 3)
                    set = 2;
                if(i.KV == 6)
                    set = 3;
                kv.setSelection(
                        set
                );

                // rv
                set = i.RV;
                if(i.RV == 3)
                    set = 2;
                if(i.RV == 5)
                    set = 3;
                rv.setSelection(
                        set
                );

                // av
                av.setSelection(
                        i.AV
                );

                // pv
                pv.setSelection(
                        i.PV
                );

                // Steuerklasse
                taxClass.setSelection(i.StKl - 1);
                selectedTaxClass = i.StKl;

                // Abrechnungsjahr
                year.setSelection(
                        _yearAdapter.getPosition(
                                String.valueOf(i.AbrJahr)
                        )
                );
                selectedYear = i.AbrJahr;

                // Elterneigenschaft
                hasChildren.setChecked(i.KindU23);
                selectedHasChildren = i.KindU23;

                // Kirchensteuer
                churchTax.setChecked(i.Kirche);
                selectedChurchTax = i.Kirche;

                // Steuerfreibetrag
                if(i.StFreibetrag != null) {
                    taxFree.setText(i.StFreibetrag.toString());
                } else {
                    taxFree.setText("0,00 €");
                }
                selectedTaxFree = i.StFreibetrag;

                // Kinderfreibetrag
                children.setSelection(
                        _childFreeAmountAdapter.getPosition(
                                i.KindFrei.toString()
                        )
                );
                selectedChildAmount = i.KindFrei;
            }
        } catch (Exception e) {
            // todo
            Log.w("auauau", e.getMessage());
        }
    }
    /**
     * Initializes the service call.
     */
    private void _prepareInsurance()
    {
        FileStore fileStore = new FileStore(this);
        Insurances i = null;
        try {
            i = fileStore.readInsurancesResult();
        } catch (Exception e) {

        }

        for (int a = 0; a < i.data.size(); a++) {
            if(!insurancesMap.containsKey(i.data.get(a).name.replaceAll("\\s+$", "")))
                insurancesMap.put(i.data.get(a).name.replaceAll("\\s+$", ""), i.data.get(a).id);
        }

        // set the list for binding the array adapter
        insurancesList = new ArrayList<String>(insurancesMap.keySet());
        _initInsurancesAdapter();
    }



    /**
     * Actualizes the view adapter.
     */
    private void _initInsurancesAdapter()
    {
        _insurancesAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_dropdown_item_1line, this.insurancesList);

        _insurancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insuranceAc.setAdapter(_insurancesAdapter);
    }


    @Override
    /**
     * What we do if calculation finished.
     */
    public void responseFinishCalculation(Calculation calculation)
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("Calculation", calculation);

        if(data.PV > 1 || data.PV != data.KV) {
            if(selectedWageType == CalculationInputHelper.WAGE_TYPE_GROSS) {
                correctNursuringInsurance_Brutto_to_Netto(calculation);
            }
            else {
                correctNursuringInsurance_Netto_to_Brutto(calculation);
            }
        }

        // dismissCalculationOverlay();
        startActivity(i);
    }

    public void correctNursuringInsurance_Brutto_to_Netto(Calculation calculation) {

        Double br = Math.min(bbg_kv.get(data.AbrJahr), data.Brutto);

        try {
            ag_alt = Double.parseDouble(calculation.data.Pflegeversicherung_AG.replace(".", "").replace(",", "."));
            an_alt = Double.parseDouble(calculation.data.Pflegeversicherung_AN.replace(".", "").replace(",", "."));
        } catch (Exception e) {
            String s = e.getMessage();
        }

        if (data.PV > 0) {
                pvag = br * percent.get(data.AbrJahr);
                if(data.Bundesland == 13)
                    pvag -= add_saxony * br;

            if (data.PV < 2) {
                pvan = br * percent.get(data.AbrJahr);
                if (data.Bundesland == 13)
                    pvan += add_saxony * br;

                if (!data.KindU23)
                    pvan += add_no_kids * br;
            }
        }
        calculation.data.Pflegeversicherung_AG = pvag.toString().replace(".", ",");
        calculation.data.Pflegeversicherung_AN = pvan.toString().replace(".", ",");

        Double an_netto = Double.parseDouble(calculation.data.Netto.replace(".", "").replace(",", "."));
        an_netto -= (pvan - an_alt);
        calculation.data.Netto = an_netto.toString().replace(".", ",");

        if(Math.abs(pvan - an_alt) > 0.00) {
            Double an_auszahlung = Double.parseDouble(calculation.data.Auszahlung.replace(".", "").replace(",", "."));
            an_auszahlung -= (pvan - an_alt);
            Double an_anteil = Double.parseDouble(calculation.data.ANAnteil.replace(".", "").replace(",", "."));
            an_anteil += (pvan - an_alt);
            calculation.data.ANAnteil = an_anteil.toString().replace(".", ",");
            calculation.data.Auszahlung = an_auszahlung.toString().replace(".", ",");
        }

        if(Math.abs(pvag - ag_alt) > 0.00) {
            Double ag_anteil = Double.parseDouble(calculation.data.AGAnteil.replace(".", "").replace(",", "."));
            ag_anteil += (pvag - ag_alt);
            calculation.data.AGAnteil = ag_anteil.toString().replace(".", ",");
        }
    }

    public void correctNursuringInsurance_Netto_to_Brutto(Calculation calculation) {
        boolean isUeberBbg_KV = false;
        boolean isUeberBbg_RV = false;

        try {
            ag_alt = Double.parseDouble(calculation.data.Pflegeversicherung_AG.replace(".", "").replace(",", ".")) / data.Brutto;
            an_alt = Double.parseDouble(calculation.data.Pflegeversicherung_AN.replace(".", "").replace(",", ".")) / data.Brutto;
        } catch (Exception e) {
            Log.d("NursuringInsurance",e.getMessage());
        }

        if (data.PV > 0) {
            pvag = percent.get(data.AbrJahr);
            if(data.Bundesland == 13)
                pvag -= add_saxony ; // nur Prozentsatz

            if (data.PV < 2) {
                pvan = percent.get(data.AbrJahr);
                if (data.Bundesland == 13)
                    pvan += add_saxony;

                if (!data.KindU23)
                    pvan += add_no_kids; // nur Prozentsatz
            }
        }

        Double nett = Double.parseDouble(calculation.data.Netto);
        Double kvag = 0.00;
        Double kvan = 0.00;
        Double rvag = 0.00;
        Double rvan = 0.00;
        Double avag = 0.00;
        Double avan = 0.00;
        Double kirc = 0.00;
        Double lstr = 0.00;
        Double soli = 0.00;

        Double igum = 0.00;
        Double uml1 = 0.00;
        Double uml2 = 0.00;

        // falls Brutto schon über KV-BBG: feste Beträge
        Double pvan_absolut = 0.00;
        Double pvag_absolut = 0.00;

        Double kvan_absolut = 0.00;
        Double kvag_absolut = 0.00;

        if(bbg_kv.get(data.AbrJahr) <= Double.parseDouble(calculation.data.SVPflBrutto)) {
            isUeberBbg_KV = true;
            pvan_absolut = pvan * bbg_kv.get(data.AbrJahr);
            pvag_absolut = pvag * bbg_kv.get(data.AbrJahr);
            kvan_absolut = Double.parseDouble(calculation.data.Krankenversicherung_AN);
            kvag_absolut = Double.parseDouble(calculation.data.Krankenversicherung_AN); // unnötig
        } else {
            kvag = Double.parseDouble(calculation.data.Krankenversicherung_AG) / data.Brutto;
            kvan = Double.parseDouble(calculation.data.Krankenversicherung_AN) / data.Brutto;
        }

        // falls Brutto schon über RV-BBG: feste Beträge
        boolean isBbgOst = GetIsBbgOst(data.Bundesland);
        Double bbg_rv = isBbgOst ? bbg_rv_ost.get(data.AbrJahr) : bbg_rv_west.get(data.AbrJahr);

        Double rvan_absolut = 0.00;
        Double rvag_absolut = 0.00;
        Double avan_absolut = 0.00;
        Double avag_absolut = 0.00;
        Double igum_absolut = 0.00;
        Double uml1_absolut = 0.00;
        Double uml2_absolut = 0.00;
        Double umlG_absolut = 0.00;

        if(bbg_rv <= Double.parseDouble(calculation.data.SVPflBrutto)) {
            isUeberBbg_RV = true;
            rvan_absolut = Double.parseDouble(calculation.data.Rentenversicherung_AG);
            rvag_absolut = Double.parseDouble(calculation.data.Rentenversicherung_AN); // unnötig
            avan_absolut = Double.parseDouble(calculation.data.Arbeitslosenversicherung_AG);
            avag_absolut = Double.parseDouble(calculation.data.Arbeitslosenversicherung_AN); // unnötig
            igum_absolut = Double.parseDouble(calculation.data.IGU); // unnötig
            uml1_absolut = Double.parseDouble(calculation.data.Umlage1); // unnötig
            uml2_absolut = Double.parseDouble(calculation.data.Umlage2); // unnötig
            umlG_absolut = Double.parseDouble(calculation.data.Umlagen_AG); // unnötig
        } else {
            rvag = Double.parseDouble(calculation.data.Rentenversicherung_AG) / data.Brutto;
            rvan = Double.parseDouble(calculation.data.Rentenversicherung_AN) / data.Brutto;
            avag = Double.parseDouble(calculation.data.Arbeitslosenversicherung_AG) / data.Brutto;
            avan = Double.parseDouble(calculation.data.Arbeitslosenversicherung_AN) / data.Brutto;
            kirc = Double.parseDouble(calculation.data.Kirchensteuer) / data.Brutto;
            lstr = Double.parseDouble(calculation.data.Lohnsteuer) / data.Brutto;
            soli = Double.parseDouble(calculation.data.Soli) / data.Brutto;

            igum = Double.parseDouble(calculation.data.IGU) / data.Brutto ;
            uml1 = Double.parseDouble(calculation.data.Umlage1) / data.Brutto;
            uml2 = Double.parseDouble(calculation.data.Umlage1) / data.Brutto;
        }

        // Double prozentAbgabenAN_alt = kvan + rvan + avan + an_alt + kirc + lstr + soli;
        Double prozentAbgabenAN_neu = kvan + rvan + avan + pvan + kirc + lstr + soli;

        // Double prozentAbgabenAG_alt = kvag + rvag + avag + ag_alt + igum + uml1 + uml2;
        Double prozentAbgabenAG_neu = kvag + rvag + avag + pvag + igum + uml1 + uml2;

        nett += pvan_absolut + kvan_absolut + rvan_absolut + avan_absolut;
        Double fiktivesNeuesBrutto = nett / (1 - prozentAbgabenAN_neu);
        // Double fiktivesAltesBrutto    = nett / (1 - prozentAbgabenAN_alt); // Probe

        if(isUeberBbg_KV) {
            kvag_absolut = Double.parseDouble(calculation.data.Krankenversicherung_AG); // bleibt
            kvan_absolut = Double.parseDouble(calculation.data.Krankenversicherung_AN); // bleibt
            calculation.data.Pflegeversicherung_AG  = Double.toString(pvan_absolut);
            calculation.data.Pflegeversicherung_AN  = Double.toString(pvag_absolut);
        } else {
            kvag_absolut = fiktivesNeuesBrutto * kvag;
            calculation.data.Krankenversicherung_AG = Double.toString(kvag_absolut);
            kvan_absolut = fiktivesNeuesBrutto * kvan;
            calculation.data.Krankenversicherung_AN = Double.toString(kvan_absolut);
            pvan_absolut = fiktivesNeuesBrutto * pvan;
            calculation.data.Pflegeversicherung_AG  = Double.toString(pvan_absolut);
            pvag_absolut = fiktivesNeuesBrutto * pvag;
            calculation.data.Pflegeversicherung_AN  = Double.toString(pvag_absolut);
        }

        if(isUeberBbg_RV) {
            // Betraege bleiben
        } else {
            rvag_absolut = fiktivesNeuesBrutto * rvag;
            calculation.data.Rentenversicherung_AG = Double.toString(rvag_absolut);
            rvan_absolut = fiktivesNeuesBrutto * rvan;
            calculation.data.Rentenversicherung_AN = Double.toString(rvan_absolut);
            avag_absolut = fiktivesNeuesBrutto * avag;
            calculation.data.Arbeitslosenversicherung_AG = Double.toString(avag_absolut);
            avan_absolut = fiktivesNeuesBrutto * avan;
            calculation.data.Arbeitslosenversicherung_AN = Double.toString(avan_absolut);
            igum_absolut = fiktivesNeuesBrutto * igum;
            calculation.data.IGU = Double.toString(igum_absolut);
            uml1_absolut = fiktivesNeuesBrutto * uml1;
            calculation.data.Umlage1 = Double.toString(uml1_absolut);
            uml2_absolut = fiktivesNeuesBrutto * uml2;
            calculation.data.Umlage2 = Double.toString(uml2_absolut);
            calculation.data.Umlagen_AG = Double.toString(igum_absolut + uml1_absolut + uml2_absolut);
        }
        Double ag_anteil = kvag_absolut + rvag_absolut + avag_absolut + pvag_absolut;
        calculation.data.AGAnteil = Double.toString(ag_anteil);
        calculation.data.Abgaben_AG = Double.toString(ag_anteil + igum_absolut + uml1_absolut + uml2_absolut);

        calculation.data.Kirchensteuer = Double.toString(fiktivesNeuesBrutto * kirc);
        calculation.data.Lohnsteuer = Double.toString(fiktivesNeuesBrutto * lstr);
        calculation.data.Soli = Double.toString(fiktivesNeuesBrutto * soli);
        calculation.data.ANAnteil = Double.toString(kvan_absolut + rvan_absolut + avan_absolut + pvan_absolut);
    }

    private static boolean GetIsBbgOst(int bundesland) {
        if(bundesland == 4 || bundesland == 8|| bundesland == 13|| bundesland == 14|| bundesland == 30)
            return true;

        return false;
    }


    @Override
    /**
     * What we do if calculation failed.
     */
    public void responseFailedCalculation(String message)
    {
        dismissCalculationOverlay();
        MessageHelper.snackbar(this, message, Snackbar.LENGTH_INDEFINITE);
    }


    @Override
    public void responseFinishInsurances(Insurances insurances) { }

    @Override
    public void responseFailedInsurances(String message) { }


    /**
     * Set all relevant data for calculation.
     */
    private boolean _setAndValidateData()
    {
        helper.data.Berechnungsart = selectedWageType;
        helper.data.Brutto = selectedWage;
        helper.data.Zeitraum = selectedWagePeriod;
        helper.data.Beschaeftigungsart = selectedEmployeeType;
        helper.data.KV = selectedKV;
        helper.data.RV = selectedRV;
        helper.data.AV = selectedAV;
        helper.data.PV = selectedPV;
        helper.data.StFreibetrag = selectedTaxFree;
        helper.data.StKl = selectedTaxClass;
        helper.data.AbrJahr = selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1;
        helper.setBundesland(selectedState);
        GetInsuranceId();
        helper.data.KKBetriebsnummer = selectedInsuranceId;
        helper.data.Kirche = selectedChurchTax;
        helper.data.KindU23 = selectedHasChildren;

        String message;

        try {
            return helper.validate();
        } catch (ValidationInsuranceException e) {
            insuranceAc.requestFocus();
            message = e.getMessage();
        } catch (ValidationException e) {
            wage.requestFocus();
            message = e.getMessage();
        }

        MessageHelper.snackbar(this, message);
        return false;
    }


    /**
     * Renders and displays a popup window
     * with advertisement and cancel button.
     */
    public void showCalculatePopupWindow()
    {
        spamWebView.setVisibility(View.VISIBLE);
    }


    /**
     * Renders a calculation dialog.
     */
    private void showCalculationDialog()
    {
        /*
        Dialog calcDialog = MessageHelper.dialog(instance, true,
        getResources().getString(R.string.calculation_started));
        calcDialog.show();
        this.calcDialog = calcDialog;
        */
    }


    /**
     * Renders a modal calculation
     * dialog or even a popup window.
     */
    private void showCalculationOverlay()
    {
        // showCalculationDialog();
        showCalculatePopupWindow();
    }


    /**
     * Dismiss all open dialog or popup windows.
     */
    private void dismissCalculationOverlay()
    {
        spamWebView.setVisibility(View.INVISIBLE);

        /*
        if (null != calcDialog && calcDialog.isShowing())
            calcDialog.dismiss();

        if (null != calcPopup && calcPopup.isShowing())
            calcPopup.dismiss();
            */
    }

}
