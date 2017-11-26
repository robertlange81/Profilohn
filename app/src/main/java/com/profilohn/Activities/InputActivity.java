package com.profilohn.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
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


public class InputActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, ApiCallbackListener {

    public RadioGroup            calcType;
    public EditText              wage;
    public EditText              taxFree;
    public Spinner               taxClass;
    public Spinner               year;
    public Spinner               state;
    public Spinner               employeeType;
    public Spinner               children;
    public Spinner               kv;
    public Spinner               rv;
    public Spinner               av;
    public Spinner               pv;
    public Button                calculateButton;
    public AutoCompleteTextView  insuranceAc;
    public SwitchCompat          wagePeriod;
    public SwitchCompat          hasChildren;
    public SwitchCompat          churchTax;
    public SwitchCompat          shifting;

    public SwitchCompat          car;
    public SwitchCompat          provision;

    private Integer selectedInsuranceId = -1;
    private String  selectedInsurance_Text = "";
    private Double  selectedWage = 0.00;
    private Double  selectedTaxFree = 0.00;
    private String  selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
    private String  selectedWagePeriod = CalculationInputHelper.WAGE_PERIOD_MONTH;
    private Boolean selectedHasChildren = false;
    private Boolean selectedChurchTax = false;
    private Boolean selectedShifting = false;
    private Boolean selectedHasCar = false;
    private Boolean selectedHasProvision = false;
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
    private ArrayAdapter<CharSequence> _childFreeAmountAdapter;
    private List<String> insurancesList = new ArrayList<>();
    private SortedMap<String, Integer> insurancesMap = new TreeMap<>();

    private NumberFormat numberFormat;
    private EventHandler eventHandler;
    private WebService webService;
    private CalculationInputData data;
    private TextView wageAmountLabel;
    private TextView wagetypeLabel;
    private TextView taxFreeLabel;
    public boolean isCalculationEnabled;
    public boolean doAbortCalculation;
    public static Queue<Integer> queue;

    private CalculationInputHelper helper;

    //private WebView spamWebView;
    AlertDialog calcDialog;

    LinearLayout regionShifting;
    LinearLayout regionChildAmount;
    LinearLayout regionTaxFreeAmount;
    LinearLayout regionProv;
    LinearLayout regionProvGrant;
    LinearLayout regionCarAmount;
    LinearLayout regionCarDistance;

    private ScrollView scrollView;

    public EditText provisionSum;
    public EditText provisionGrant;
    private Double selectedProvisionSum = 0.00;
    private Double selectedProvisionGrant = 0.00;
    private TextView provSumLabel;
    private TextView provGrantLabel;
    public EditText carAmount;
    public EditText carDistance;
    private Integer selectedCarAmount = 0;
    private Integer selectedCarDistance = 0;

    BigDecimal percent_pausch_steuer_minijob = new BigDecimal(0.02);
    BigDecimal anteilAG_pauschRV_Minijob_sv = new BigDecimal(0.15);
    BigDecimal anteilAN_aufstocker_Minijob_sv = new BigDecimal(0.037);
    BigDecimal min__aufstocker_Minijob_sv_ = new BigDecimal(6.48);
    BigDecimal percent_pausch_steuer_kurzfristig = new BigDecimal(0.25);
    BigDecimal percent_soli = new BigDecimal(0.055);
    boolean isFakeBruttolohn = false;
    BigDecimal wunschnetto;

    Map<Integer, BigDecimal> percentErmaessigteKirchensteuer = new HashMap<Integer, BigDecimal>() {{
        put(1, new BigDecimal("0.08"));
        put(2, new BigDecimal("0.08"));
        put(3, new BigDecimal("0.09"));
        put(4, new BigDecimal("0.09"));
        put(5, new BigDecimal("0.09"));
        put(6, new BigDecimal("0.09"));
        put(7, new BigDecimal("0.09"));
        put(8, new BigDecimal("0.09"));
        put(10, new BigDecimal("0.09"));
        put(11, new BigDecimal("0.09"));
        put(12, new BigDecimal("0.09"));
        put(13, new BigDecimal("0.09"));
        put(14, new BigDecimal("0.09"));
        put(15, new BigDecimal("0.09"));
        put(16, new BigDecimal("0.09"));
        put(30, new BigDecimal("0.09"));
    }};
    Map<Integer, BigDecimal> percentNursingInsurance = new HashMap<Integer, BigDecimal>() {{
        put(2016, new BigDecimal("0.01175"));
        put(2017, new BigDecimal("0.01275"));
        put(2018, new BigDecimal("0.01275"));
        put(2019, new BigDecimal("0.01275"));
    }};
    Map<Integer, BigDecimal> bbg_kv = new HashMap<Integer, BigDecimal>() {{
        put(2016, new BigDecimal("4237.50"));
        put(2017, new BigDecimal("4350.00"));
        put(2018, new BigDecimal("4425.00"));
        put(2019, new BigDecimal("4425.00"));
    }};
    Map<Integer, BigDecimal> bbg_rv_west = new HashMap<Integer, BigDecimal>() {{
        put(2016, new BigDecimal("6200.00"));
        put(2017, new BigDecimal("6350.00"));
        put(2018, new BigDecimal("6500.00"));
        put(2019, new BigDecimal("6500.00"));
    }};
    Map<Integer, BigDecimal> bbg_rv_ost = new HashMap<Integer, BigDecimal>() {{
        put(2016, new BigDecimal("5400.00"));
        put(2017, new BigDecimal("5700.00"));
        put(2018, new BigDecimal("5800.00"));
        put(2019, new BigDecimal("5800.00"));
    }};
    BigDecimal add_no_kids  = new BigDecimal("0.0025");
    BigDecimal add_saxony   = new BigDecimal("0.005");

    BigDecimal pvag = new BigDecimal("0.000");
    BigDecimal pvan = new BigDecimal("0.000");

    BigDecimal ag_alt = new BigDecimal("0.000");
    BigDecimal an_alt = new BigDecimal("0.000");


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        super.onCreate(savedInstanceState);

        setContentView(R.layout.input);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

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

        isCalculationEnabled = false;
        queue = new LinkedList<>();

/*
        spamWebView =(WebView) findViewById(R.id.webview_calc);

        spamWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
        }});
        spamWebView.setVisibility(View.INVISIBLE);*/
        //calculateButton.setVisibility(View.VISIBLE);
        //spamWebView.loadUrl("http://robert-lange.eu/loader2.html");
        //InputActivity.this.isCalculationEnabled = true;

        if(scrollView != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.fullScroll(View.FOCUS_UP);
                }
        });
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        dismissCalculationOverlay();
        isCalculationEnabled = true;
    }

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
        scrollView      = (ScrollView) findViewById(R.id.scrollview);
        calcType        = (RadioGroup) findViewById(R.id.type);
        wage            = (EditText) findViewById(R.id.wage);
        wagePeriod      = (SwitchCompat) findViewById(R.id.wage_period);
        state           = (Spinner) findViewById(R.id.state);
        employeeType    = (Spinner) findViewById(R.id.employee_type);
        taxClass        = (Spinner) findViewById(R.id.tax_class);
        year            = (Spinner) findViewById(R.id.year);
        taxFree         = (EditText) findViewById(R.id.tax_free);
        children        = (Spinner) findViewById(R.id.children);

        calculateButton = (Button) findViewById(R.id.calculate_general);

        kv              = (Spinner) findViewById(R.id.kv_value);
        rv              = (Spinner) findViewById(R.id.rv_value);
        av              = (Spinner) findViewById(R.id.av_value);
        pv              = (Spinner) findViewById(R.id.pv_value);

        insuranceAc     = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
        churchTax       = (SwitchCompat) findViewById(R.id.church);
        hasChildren     = (SwitchCompat) findViewById(R.id.has_children);

        wageAmountLabel = (TextView) findViewById(R.id.wageamount_label);
        wagetypeLabel   = (TextView) findViewById(R.id.wage_type_label);
        taxFreeLabel    = (TextView) findViewById(R.id.taxfree_label);
        provSumLabel    = (TextView) findViewById(R.id.prov_amount_label);
        provGrantLabel  = (TextView) findViewById(R.id.prov_grant_label);

        regionShifting  = (LinearLayout) findViewById(R.id.shifting_area);
        regionProv      = (LinearLayout) findViewById(R.id.prov_amount_region);
        regionProvGrant = (LinearLayout) findViewById(R.id.prov_grant_region);
        regionCarAmount = (LinearLayout) findViewById(R.id.car_amount_region);
        regionCarDistance = (LinearLayout) findViewById(R.id.car_distance_region);
        regionChildAmount   = (LinearLayout) findViewById(R.id.child_amount_region);
        regionTaxFreeAmount = (LinearLayout) findViewById(R.id.tax_free_amount_region);

        shifting        = (SwitchCompat) findViewById(R.id.has_shifting);

        provision       = (SwitchCompat) findViewById(R.id.retprov);
        provisionSum    = (EditText) findViewById(R.id.provision_sum);
        provisionGrant  = (EditText) findViewById(R.id.provision_grant);
        car             = (SwitchCompat) findViewById(R.id.car);
        carAmount       = (EditText) findViewById(R.id.car_sum);
        carDistance     = (EditText) findViewById(R.id.car_distance);

        //wage.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});
        //taxFree.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});

        // tax classes
        ArrayAdapter<CharSequence> _taxclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.taxclasses, R.layout.spinner_left_item);
        _taxclassAdapter.setDropDownViewResource(R.layout.spinner_left_item);
        taxClass.setAdapter(_taxclassAdapter);
        taxClass.setSelection(1);

        // calc year
        String[] years = new String[]{
                (Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)-1)).toString(),
                (Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR))).toString(),
                (Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)+1)).toString()
        };
        final List<String> yearsList = new ArrayList<>(Arrays.asList(years));
        _yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_left_item, yearsList);
        year.setAdapter(_yearAdapter);
        year.setSelection(1);

        // insurance classes
        ArrayAdapter<CharSequence> _kvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.kv, R.layout.spinner_left_item);
        kv.setAdapter(_kvclassAdapter);

        ArrayAdapter<CharSequence> _rvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.rv, R.layout.spinner_left_item);
        rv.setAdapter(_rvclassAdapter);

        ArrayAdapter<CharSequence> _avclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.av, R.layout.spinner_left_item);
        av.setAdapter(_avclassAdapter);

        ArrayAdapter<CharSequence> _pvclassAdapter = ArrayAdapter.createFromResource(this,
                R.array.pv, R.layout.spinner_left_item);
        pv.setAdapter(_pvclassAdapter);

        // employee type
        ArrayAdapter<CharSequence> _employeeTypeDataAdapter = ArrayAdapter.createFromResource(this,
                R.array.employeetypes, R.layout.spinner_left_item);
        employeeType.setAdapter(_employeeTypeDataAdapter);

        // child free amount
        _childFreeAmountAdapter = ArrayAdapter.createFromResource(this,
                R.array.childfree, R.layout.spinner_left_item);
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

                if (R.id.type_net == checkedId) {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
                    wagetypeLabel.setText(R.string.wage_type_label_1);
                    if(wagePeriod.isChecked()) {
                        wageAmountLabel.setText(R.string.wageamount_gross_year);
                        taxFreeLabel.setText(R.string.taxfree_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_gross_month);
                        taxFreeLabel.setText(R.string.taxfree_month);
                    }
                } else {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_NET;
                    wagetypeLabel.setText(R.string.wage_type_label_2);
                    if(wagePeriod.isChecked()) {
                        wageAmountLabel.setText(R.string.wageamount_net_year);
                        taxFreeLabel.setText(R.string.taxfree_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_net_month);
                        taxFreeLabel.setText(R.string.taxfree_month);
                    }
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Betrag (Brutto oder Wunsch-Netto)
        wage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!wage.hasFocus()) {
                    try {
                        String cur = wage.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");

                        Double current = 0.00;
                        if(!cur.equals("")) {
                            current = Double.valueOf(cur);
                        }
                        selectedWage = current;
                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMinimumFractionDigits(2);
                        String output = numberFormat.format(current);

                        wage.setText(output);
                    } catch (Exception x) {
                        selectedWage = 0.00;
                        provisionGrant.setText(getResources().getString(R.string.taxfree_hint));
                    }
                }
            }
        });

        wage.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
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

                        Double current = 0.00;
                        if(!cur.equals("")) {
                            current = Double.valueOf(cur);
                        }
                        selectedTaxFree = current;

                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMinimumFractionDigits(2);
                        String output = numberFormat.format(current);

                        taxFree.setText(output);
                    } catch (Exception x) {
                        selectedProvisionGrant = 0.00;
                        provisionGrant.setText(getResources().getString(R.string.taxfree_hint));
                    }
                }
            }});

        taxFree.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });

        // Altersvorsorge
        provisionSum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!provisionSum.hasFocus()) {
                    try {
                        String cur = provisionSum.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");

                        Double current = 0.00;
                        if(!cur.equals("")) {
                            current = Double.valueOf(cur);
                        }
                        selectedProvisionSum = current;

                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMinimumFractionDigits(2);
                        String output = numberFormat.format(current);

                        provisionSum.setText(output);

                    } catch (Exception x) {
                        selectedProvisionSum = 0.00;
                        provisionSum.setText(getResources().getString(R.string.taxfree_hint));
                    }
                }
            }
        });

        provisionSum.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });

        provisionGrant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!provisionGrant.hasFocus()) {
                    try {
                        String cur = provisionGrant.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");

                        Double current = 0.00;
                        if(!cur.equals("")) {
                            current = Double.valueOf(cur);
                        }
                        selectedProvisionGrant = current;

                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMinimumFractionDigits(2);
                        String output = numberFormat.format(current);

                        provisionGrant.setText(output);

                    } catch (Exception x) {
                        selectedProvisionGrant = 0.00;
                        provisionGrant.setText(getResources().getString(R.string.taxfree_hint));
                    }
                }
            }
        });

        provisionGrant.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });

        // Firmenwagen
        carAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!carAmount.hasFocus()) {
                    try {
                        String cur = carAmount.getText().toString();
                        cur = cur.replaceAll("€", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");

                        Integer current = 0;
                        if(!cur.equals("")) {
                            Double tmp = Double.valueOf(cur);
                            current = tmp.intValue();
                        }
                        selectedCarAmount = current;

                        numberFormat.setMaximumFractionDigits(0);
                        String output = numberFormat.format(current);

                        carAmount.setText(output);

                    } catch (Exception x) {
                        selectedCarAmount = 0;
                        carAmount.setText(getResources().getString(R.string.car_amount_hint));
                    }
                }
            }
        });

        carAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });

        carDistance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!carDistance.hasFocus()) {
                    try {
                        String cur = carDistance.getText().toString();
                        cur = cur.replaceAll("km", "");
                        cur = cur.replaceAll("\\s+","");
                        cur = cur.replaceAll("\\.", ",");
                        cur = cur.replaceAll(",(?=.*?,)", "");
                        cur = cur.replaceAll(",", ".");

                        Integer current = 0;
                        if(!cur.equals("")) {
                            Double tmp = Double.valueOf(cur);
                            current = tmp.intValue();
                        }
                        selectedCarDistance = current;

                        String output = current + " km";

                        carDistance.setText(output);
                    } catch (Exception x) {
                        selectedCarDistance = 0;
                        carAmount.setText(getResources().getString(R.string.car_distance_hint));
                    }
                }
            }
        });

        carDistance.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    calculateButton.setFocusableInTouchMode(true);
                    calculateButton.requestFocus();
                    calculateButton.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });

        // Periodenbezug (Monat / Jahr)
        wagePeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                eventHandler.OnSwitchPeriodType(isChecked);

                selectedWagePeriod = isChecked
                        ? CalculationInputHelper.WAGE_PERIOD_YEAR : CalculationInputHelper.WAGE_PERIOD_MONTH;

                if (selectedWageType != null && selectedWageType.equals(CalculationInputHelper.WAGE_TYPE_GROSS)) {
                    if(isChecked) {
                        wageAmountLabel.setText(R.string.wageamount_gross_year);
                        taxFreeLabel.setText(R.string.taxfree_year);
                        provSumLabel.setText(R.string.provision_sum_year);
                        provGrantLabel.setText(R.string.provision_grant_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_gross_month);
                        taxFreeLabel.setText(R.string.taxfree_month);
                        provSumLabel.setText(R.string.provision_sum_month);
                        provGrantLabel.setText(R.string.provision_grant_month);
                    }
                } else {
                    if(isChecked) {
                        wageAmountLabel.setText(R.string.wageamount_net_year);
                        taxFreeLabel.setText(R.string.taxfree_year);
                        provSumLabel.setText(R.string.provision_sum_year);
                        provGrantLabel.setText(R.string.provision_grant_year);
                    } else {
                        wageAmountLabel.setText(R.string.wageamount_net_month);
                        taxFreeLabel.setText(R.string.taxfree_month);
                        provSumLabel.setText(R.string.provision_sum_month);
                        provGrantLabel.setText(R.string.provision_grant_month);
                    }
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        SpinnerInteractionListener sal = new SpinnerInteractionListener(this);
        employeeType.setOnItemSelectedListener(sal);
        employeeType.setOnTouchListener(sal);

        kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 if(position != 0 && insuranceAc.getHint().equals("Nicht erforderlich.") && !selectedInsurance_Text.equals("")) {
                     insuranceAc.setText(selectedInsurance_Text);
                 }

                 switch(position) {
                     case 0: // kein
                         selectedKV = 0;
                         if(selectedPV == 0) {
                             insuranceAc.setEnabled(false);
                             insuranceAc.setText("");
                             insuranceAc.setHint("Nicht erforderlich.");
                         }
                         break;
                     case 1: // allgemeiner
                         selectedKV = 1;
                         insuranceAc.setEnabled(true);
                         insuranceAc.setHint("Name der Krankenkasse ..");
                         break;
                     case 2: // ermäßigter
                         selectedKV = 3;
                         insuranceAc.setEnabled(true);
                         insuranceAc.setHint("Name der Krankenkasse ..");
                         break;
                     case 3: // Pauschalbeitrag
                         selectedKV = 6;
                         insuranceAc.setEnabled(true);
                         insuranceAc.setHint("Name der Krankenkasse ..");
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {
                 calculateButton.setFocusableInTouchMode(true);
                 calculateButton.requestFocus();
                 calculateButton.setFocusableInTouchMode(false);
             }
        });

        rv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        av.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        pv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0 && insuranceAc.getHint().equals("Nicht erforderlich.") && !selectedInsurance_Text.equals("")) {
                    insuranceAc.setText(selectedInsurance_Text);
                }

                switch(position) {
                    case 0: // kein
                        selectedPV = 0;
                        if(selectedKV == 0) {
                            insuranceAc.setEnabled(false);
                            insuranceAc.setText("");
                            insuranceAc.setHint("Nicht erforderlich.");
                        }
                        break;
                    case 1: // voller
                        selectedPV = 1;
                        insuranceAc.setEnabled(true);
                        insuranceAc.setHint("Name der Krankenkasse ..");
                        break;
                    case 2: // halber
                        selectedPV = 2;
                        insuranceAc.setEnabled(true);
                        insuranceAc.setHint("Name der Krankenkasse ..");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });


        // Steuerklasse
        taxClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    selectedTaxClass = 23;
                    regionTaxFreeAmount.setVisibility(View.INVISIBLE);
                    regionChildAmount.setVisibility(View.GONE);
                    regionShifting.setVisibility(View.VISIBLE);
                } else {
                    regionTaxFreeAmount.setVisibility(View.VISIBLE);
                    regionChildAmount.setVisibility(View.VISIBLE);
                    regionShifting.setVisibility(View.GONE);
                    selectedTaxClass = position;
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Abrechnungsjahr
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = position;

                if(selectedEmployeeType > 5 ) {
                    if(selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1 <= 2016) {
                        selectedAV = 2;
                        av.setSelection(2);
                    } else {
                        selectedAV = 0;
                        av.setSelection(0);
                    }
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Bundesländer
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = state.getSelectedItem().toString();

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Beschäftigungsverhältnis
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = state.getSelectedItem().toString();

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Kirchensteuer (ja / nein)
        hasChildren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChildren(isChecked);
                selectedHasChildren = isChecked;

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Krankenkasse
        insuranceAc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetInsuranceId();

                eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Kirchensteuer (ja / nein)
        churchTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchChurchType(isChecked);
                selectedChurchTax = isChecked;

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Kinderfreibetrag
        children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChildAmount = (double) position / 2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Abwälzung (ja / nein)
        shifting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchShifting(isChecked);
                selectedShifting = isChecked;

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Altersvorsorge (ja / nein)
        provision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchProvision(isChecked);
                selectedHasProvision = isChecked;

                if(isChecked) {
                    regionProv.setVisibility(View.VISIBLE);
                    regionProvGrant.setVisibility(View.VISIBLE);
                    if(scrollView != null) {
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                } else {
                    regionProv.setVisibility(View.GONE);
                    regionProvGrant.setVisibility(View.GONE);
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Firmenwagen (ja / nein)
        car.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventHandler.OnSwitchCar(isChecked);
                selectedHasCar = isChecked;

                if(isChecked) {
                    regionCarAmount.setVisibility(View.VISIBLE);
                    regionCarDistance.setVisibility(View.VISIBLE);
                    if(scrollView != null) {
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                } else {
                    regionCarAmount.setVisibility(View.GONE);
                    regionCarDistance.setVisibility(View.GONE);
                }

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);
            }
        });

        // Logik zur Abrechnung - Listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateButton.setFocusableInTouchMode(true);
                calculateButton.requestFocus();
                calculateButton.setFocusableInTouchMode(false);

                if(!isCalculationEnabled) {
                    return;
                }

                if (!_setAndValidateData()) {
                    isCalculationEnabled = true;
                    return;
                }

                _cacheInputs(data);

                // Wunschnetto erhöhen für pauschale Steuern oder Aufstocker Minijobber
                if(data.Berechnungsart.equals("Nettolohn")) {
                    BigDecimal perc = new BigDecimal(0);
                    if(data.Beschaeftigungsart == 2 && data.RV == 1) { // Sonderfall Aufstocker Minijob
                        if(data.Zeitraum.equals("m") && data.Brutto / (1 - 0.053) < 175) {
                            // minumum beitrag
                            data.Brutto += min__aufstocker_Minijob_sv_.doubleValue();
                            data.Berechnungsart = "Bruttolohn";
                        } else if(data.Zeitraum.equals("y") && data.Brutto / (1 - 0.053) < 175 * 12) {
                            // minumum beitrag
                            data.Brutto += min__aufstocker_Minijob_sv_.doubleValue() * 12;
                            data.Berechnungsart = "Bruttolohn";
                        } else {
                            perc = perc.add(anteilAN_aufstocker_Minijob_sv);
                        }
                    }

                    if(data.StKl == 23) {
                        data.Berechnungsart = "Bruttolohn";
                        isFakeBruttolohn = true;
                        if(data.abwaelzung_pauschale_steuer) {
                            if (data.Beschaeftigungsart == 1 || data.Beschaeftigungsart == 2) {
                                perc = perc.add(percent_pausch_steuer_minijob);
                            }

                            if (data.Beschaeftigungsart == 4) {
                                BigDecimal Soli = percent_pausch_steuer_kurzfristig.multiply(percent_soli).setScale(5, RoundingMode.HALF_DOWN);
                                perc = percent_pausch_steuer_kurzfristig.add(Soli).setScale(5, RoundingMode.HALF_DOWN);

                                if (data.Kirche)
                                    perc = perc.add(percent_pausch_steuer_kurzfristig.multiply(percentErmaessigteKirchensteuer.get(data.Bundesland))).setScale(5, RoundingMode.HALF_DOWN);
                            }
                        }
                    }

                    if(perc.doubleValue() > 0) {
                        wunschnetto = new BigDecimal(data.Brutto);
                        data.Brutto = new BigDecimal(data.Brutto).divide(new BigDecimal(1).subtract(perc), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        data.Berechnungsart = "Bruttolohn";
                        isFakeBruttolohn = true;
                    }
                }

                // Altersvorsorge behandeln
                if(data.hatAltersvorsorge) {
                    if(data.Berechnungsart.equals(CalculationInputHelper.WAGE_TYPE_GROSS)) {
                        Double an_anteil = data.Altersvorsorge_summe - data.Altersvorsorge_zuschuss;
                        data.Brutto -= an_anteil;
                        Double freibetrag = an_anteil;
                        if(bbg_rv_west.get(data.AbrJahr) != null) {
                            freibetrag = bbg_rv_west.get(data.AbrJahr).multiply(new BigDecimal(0.04)).doubleValue();
                        }
                        data.Altersvorsorge_pflichtig = Math.max(0.00, an_anteil - freibetrag);

                        if(data.Altersvorsorge_pflichtig > 0.005)
                            data.Brutto += data.Altersvorsorge_pflichtig;
                    } else {
                        // erhöhe bei Simulation das Brutto erst im Ergebnis
                    }
                }

                eventHandler.hideKeyboardInput((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                doAbortCalculation = false;
                showCalculationOverlay();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(!doAbortCalculation) {
                            queue.add(queue.size() + 1);
                            CalculationInput ci = new CalculationInput(data);
                            webService.Calculate(ci);
                        }
                    }

                }, 300);
            }
        };

        // Abrechnungs-Button übergeordnet
        calculateButton.setOnClickListener(listener);
    }

    private void GetInsuranceId() {
        String value = insuranceAc.getText().toString();
        Integer companyNumber = insurancesMap.get(value);

        if(companyNumber != null) {
            selectedInsurance_Text = value;
            selectedInsuranceId = companyNumber;
            insuranceAc.clearFocus();
        } else {
            selectedInsuranceId = -1;
        }
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
                if(taxClass.getSelectedItemPosition() == 0)
                    taxClass.setSelection(1);
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
                insuranceAc.setText(getResources().getString(R.string.insurance_default_pausch));
                taxClass.setSelection(0);
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
                insuranceAc.setText(getResources().getString(R.string.insurance_default_pausch));
                taxClass.setSelection(0);
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
                if(taxClass.getSelectedItemPosition() == 0)
                    taxClass.setSelection(1);
                break;
            case 4: // kurzfristig beschäftigt
                selectedKV = 0;
                selectedRV = 0;
                selectedAV = 0;
                selectedPV = 0;
                kv.setSelection(0);
                rv.setSelection(0);
                av.setSelection(0);
                pv.setSelection(0);
                taxClass.setSelection(0);
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
                if(taxClass.getSelectedItemPosition() == 0)
                    taxClass.setSelection(1);
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
                if(taxClass.getSelectedItemPosition() == 0)
                    taxClass.setSelection(1);
                break;
            default:
                // volle Versicherung
                selectedKV = 1;
                selectedRV = 1;
                selectedAV = 1;
                selectedPV = 1;
                kv.setSelection(1);
                rv.setSelection(1);
                av.setSelection(1);
                pv.setSelection(1);
                if(taxClass.getSelectedItemPosition() == 0)
                    taxClass.setSelection(1);
        }
    }


    /**
     * Prepares and handles the state selection.
     */
    private void _prepareState()
    {
        String[] states = getResources().getStringArray(R.array.states);

        _statesDataAdapter = new ArrayAdapter<>(this, R.layout.spinner_left_item, states);
        state.setAdapter(_statesDataAdapter);
    }

    /**
     * Initializes last inputs
     */
    private void _cacheInputs(CalculationInputData data)
    {
        FileStore fileStore = new FileStore(this);
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
        // Log.w("i", "Wo ist der Debugger?");
        FileStore fileStore = new FileStore(this);
        CalculationInputData i;
        try {
            i = fileStore.readInput();

            if (i != null) {
                // Brutto / Nettobetrag
                wage.requestFocus();
                if (i.Brutto != null) {
                    wage.setText(i.Brutto.toString());
                } else {
                    wage.setText(getResources().getString(R.string.taxfree_hint));
                }
                selectedWage = i.Brutto;
                wage.clearFocus();

                // Jahr oder Monat
                wagePeriod.setChecked(i.Zeitraum.equalsIgnoreCase("y"));

                // Krankenkasse
                if (!i.dummyInsurance) {
                    selectedInsuranceId = i.KKBetriebsnummer;
                    insuranceAc.setText(i.KK_text);
                    selectedInsurance_Text = i.KK_text;
                }

                // Bundesland
                String stateString = helper.retranslateState(i.Bundesland);
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
                if (i.KV == 3)
                    set = 2;
                if (i.KV == 6)
                    set = 3;
                kv.setSelection(
                        set
                );

                // rv
                set = i.RV;
                if (i.RV == 3)
                    set = 2;
                if (i.RV == 5)
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
                if(i.StKl == 23) {
                    // pauschal
                    taxClass.setSelection(0);
                } else {
                    if(i.StKl < 0 || i.StKl > 6)
                        i.StKl = 1;

                    taxClass.setSelection(i.StKl);
                }
                selectedTaxClass = i.StKl;
                selectedShifting = i.abwaelzung_pauschale_steuer;
                shifting.setChecked(selectedShifting);

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
                taxFree.requestFocus();
                if (i.StFreibetrag != null) {
                    taxFree.setText(i.StFreibetrag.toString());
                } else {
                    taxFree.setText(getResources().getString(R.string.taxfree_hint));
                }

                selectedTaxFree = i.StFreibetrag;

                // Kinderfreibetrag
                children.setSelection(
                        _childFreeAmountAdapter.getPosition(
                                i.KindFrei.toString()
                        )
                );
                selectedChildAmount = i.KindFrei;

                // Altersvorsorge
                selectedHasProvision = i.hatAltersvorsorge;
                provision.setChecked(i.hatAltersvorsorge);
                selectedProvisionSum = i.Altersvorsorge_summe;
                provisionSum.requestFocus();
                if (i.Altersvorsorge_summe != null) {
                    provisionSum.setText(i.Altersvorsorge_summe.toString());
                } else {
                    provisionSum.setText(getResources().getString(R.string.taxfree_hint));
                }

                // Zuschuss Altersvorsorge
                selectedProvisionGrant = i.Altersvorsorge_zuschuss;
                provisionGrant.requestFocus();
                if (i.Altersvorsorge_zuschuss != null) {
                    provisionGrant.setText(i.Altersvorsorge_zuschuss.toString());
                } else {
                    provisionGrant.setText(getResources().getString(R.string.taxfree_hint));
                }
                provisionGrant.clearFocus();

                // Firmenwagen
                selectedHasCar = i.hatFirmenwagen;
                car.setChecked(i.hatFirmenwagen);
                selectedCarAmount = i.Firmenwagen_summe;
                carAmount.requestFocus();
                if (i.Firmenwagen_summe != null) {
                    carAmount.setText(i.Firmenwagen_summe.toString());
                } else {
                    carAmount.setText("0 €");
                }
                carAmount.clearFocus();

                // Firmenwagen Kilometer
                selectedCarDistance = i.Firmenwagen_km;
                carDistance.requestFocus();
                if (i.Firmenwagen_km != null) {
                    carDistance.setText(i.Firmenwagen_km.toString() + " km");
                } else {
                    carDistance.setText(getResources().getString(R.string.car_distance_hint));
                }
                carDistance.clearFocus();
            } else {
                // erstes Starten
                employeeType.setSelection(0);
                state.setSelection(0);
                updateInsuranceBranches();
                wage.requestFocus();
            }
        } catch (FileNotFoundException fnfe) {
            employeeType.setSelection(0);
            state.setSelection(0);
            updateInsuranceBranches();
        } catch (Exception e) {
            //Log.w("auauau", e.getMessage());
        }
    }
    /**
     * Initializes the service call.
     */
    private void _prepareInsurance()
    {
        FileStore fileStore = new FileStore(this);
        Insurances i;
        try {
            i = fileStore.readInsurancesResult();
        } catch (Exception e) {
            i = null;
        }

        if(i != null && i.data != null) {
            for (int a = 0; a < i.data.size(); a++) {
                if(!insurancesMap.containsKey(i.data.get(a).name.replaceAll("\\s+$", "")))
                    insurancesMap.put(i.data.get(a).name.replaceAll("\\s+$", ""), i.data.get(a).id);
            }
        }

        // set the list for binding the array adapter
        insurancesList = new ArrayList<>(insurancesMap.keySet());
        _initInsurancesAdapter();
    }

    /**
     * Actualizes the view adapter.
     */
    private void _initInsurancesAdapter()
    {
        ArrayAdapter<String> _insurancesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, this.insurancesList);

        _insurancesAdapter.setDropDownViewResource(R.layout.spinner_left_item);
        insuranceAc.setAdapter(_insurancesAdapter);
    }


    @Override
    public void responseFinishCalculation(Calculation calculation)
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("Calculation", calculation);

        if(data.KV != 6 && (data.PV > 1 || data.PV != data.KV)) {
            if(selectedWageType.equals(CalculationInputHelper.WAGE_TYPE_GROSS)) {
                correctNursingInsurance_Brutto_to_Netto(calculation);
            }
            else {
                correctNursingInsurance_Netto_to_Brutto(calculation);
            }
        }

        if(data.Beschaeftigungsart == 2 && data.RV == 1) { // Sonderfall Aufstocker Minijob
            correctPensionInsurance_Aufstocker_AN(calculation);
            correctPensionInsurance_Pauschal_AG(calculation);
        } else if(data.RV == 5) {
            correctPensionInsurance_Pauschal_AG(calculation);
        }

        if(data.StKl == 23) {
            if(data.Beschaeftigungsart == 1 || data.Beschaeftigungsart == 2) {
                if(data.abwaelzung_pauschale_steuer) {
                    correctPauschaleSteuer_Minijob_Abwaelzung(calculation);
                } else {
                    correctPauschaleSteuer_Minijob(calculation);
                }
            }

            if(data.Beschaeftigungsart == 4) {
                if(data.abwaelzung_pauschale_steuer) {
                    correctPauschaleSteuer_Kurzfristig_Abwaelzung(calculation);
                } else {
                    correctPauschaleSteuer_Kurzfristig(calculation);
                }
            }
        }

        // TODO Grenzen beachten
        if(data.hatAltersvorsorge) {

            // altes Brutto wieder herstellen
            // bei Wunschnetto Simulation erst jetzt erhöhen, aber Pflichtgrenze ignorieren
            BigDecimal Brutto = getBigDecimal(calculation.data.LohnsteuerPflBrutto);
            BigDecimal summe = new BigDecimal(data.Altersvorsorge_summe);
            BigDecimal zuschuss = new BigDecimal(data.Altersvorsorge_zuschuss);
            BigDecimal anAnteil = summe.subtract(zuschuss);

            Brutto = Brutto.add(anAnteil);
            if(data.Berechnungsart.equals("Bruttolohn") && !isFakeBruttolohn) {
                BigDecimal pflichtig = new BigDecimal(data.Altersvorsorge_pflichtig);
                Brutto = Brutto.subtract(pflichtig);
            }

            calculation.data.LohnsteuerPflBrutto = getDecimalString_Down(Brutto);
            calculation.data.SVPflBrutto = calculation.data.LohnsteuerPflBrutto;
            calculation.data.AG_Zuschuss_Altersvorsorge = getDecimalString_Down(zuschuss);
            calculation.data.Summe_Altersvorsorge = getDecimalString_Down(summe);
            calculation.data.AN_Anteil_Altersvorsorge = getDecimalString_Down(anAnteil);
        }

        dismissCalculationOverlay();

        Integer last = 0;
        if(queue.size() > 0)
            last = queue.remove();

        if(doAbortCalculation || last == -1) {
            isCalculationEnabled = true;

            if(queue.size() == 0)
                doAbortCalculation = false;

            return;
        }

        startActivity(i);
    }

    @Override
    public void responseFailedCalculation(String message)
    {
        int last = queue.remove();
        dismissCalculationOverlay();

        if(!doAbortCalculation && last != -1)
            MessageHelper.snackbar(this, message, Snackbar.LENGTH_INDEFINITE);

        isCalculationEnabled = true;
    }

    public void correctNursingInsurance_Brutto_to_Netto(Calculation calculation) {

        if(bbg_kv.get(data.AbrJahr) == null
                || bbg_rv_ost.get(data.AbrJahr) == null
                || bbg_rv_west.get(data.AbrJahr) == null
                || percentNursingInsurance.get(data.AbrJahr) == null)
            return;

        BigDecimal bbg_kv_tmp = data.Zeitraum.equals("m") ?
                bbg_kv.get(data.AbrJahr) : bbg_kv.get(data.AbrJahr).multiply(new BigDecimal(12));

        BigDecimal br = bbg_kv_tmp.min(new BigDecimal(data.Brutto.toString()));

        try {
            ag_alt = getBigDecimal(calculation.data.Pflegeversicherung_AG);
            an_alt = getBigDecimal(calculation.data.Pflegeversicherung_AN);
        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
        }

        if (data.PV > 0) {
                pvag = br.multiply(percentNursingInsurance.get(data.AbrJahr));
                if(data.Bundesland == 13)
                    pvag = pvag.subtract(add_saxony.multiply(br));

            if (data.PV < 2) {
                pvan = br.multiply(percentNursingInsurance.get(data.AbrJahr)).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (data.Bundesland == 13)
                    pvan = pvan.add(add_saxony.multiply(br));

                if (!data.KindU23)
                    pvan = pvan.add(add_no_kids.multiply(br));
            }
        }
        calculation.data.Pflegeversicherung_AG = getDecimalString_Up(pvag);
        calculation.data.Pflegeversicherung_AN = getDecimalString_Up(pvan);

        if(pvan.compareTo(an_alt) != 0) {
            BigDecimal an_netto = getBigDecimal(calculation.data.Netto);
            an_netto = an_netto.subtract(pvan.subtract(an_alt));
            calculation.data.Netto = getDecimalString_Up(an_netto);
            BigDecimal an_anteil = getBigDecimal(calculation.data.ANAnteil);
            an_anteil = an_anteil.add(pvan.subtract(an_alt));
            calculation.data.ANAnteil = getDecimalString_Down(an_anteil);
            BigDecimal an_auszahlung = getBigDecimal(calculation.data.Auszahlung);
            an_auszahlung = an_auszahlung.subtract(pvan.subtract(an_alt));
            calculation.data.Auszahlung = getDecimalString_Down(an_auszahlung);
        }

        if(pvag.compareTo(ag_alt) != 0) {
            BigDecimal ag_anteil = getBigDecimal(calculation.data.AGAnteil);
            ag_anteil = ag_anteil.subtract(pvag.subtract(ag_alt));
            calculation.data.AGAnteil = getDecimalString_Up(ag_anteil);
        }
    }

    public void correctNursingInsurance_Netto_to_Brutto(Calculation calculation) {

        if(bbg_kv.get(data.AbrJahr) == null
                || bbg_rv_ost.get(data.AbrJahr) == null
                || bbg_rv_west.get(data.AbrJahr) == null
                || percentNursingInsurance.get(data.AbrJahr) == null)
            return;

        boolean isUeberBbg_KV = false;
        boolean isUeberBbg_RV = false;

        BigDecimal brutto_alt = getBigDecimal(calculation.data.SVPflBrutto);

        try {
            ag_alt = getBigDecimal(calculation.data.Pflegeversicherung_AG).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_UP);
            an_alt = getBigDecimal(calculation.data.Pflegeversicherung_AN).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_UP);

            if (data.PV > 0) {
                pvag = percentNursingInsurance.get(data.AbrJahr);
                if(data.Bundesland == 13)
                    pvag = pvag.subtract(add_saxony); // nur Prozentsatz

                if (data.PV < 2) {
                    pvan = percentNursingInsurance.get(data.AbrJahr);
                    if (data.Bundesland == 13)
                        pvan = pvan.add(add_saxony);

                    if (!data.KindU23)
                        pvan = pvan.add(add_no_kids); // nur Prozentsatz
                }
            }

            BigDecimal netto_as_dividend = getBigDecimal(calculation.data.Netto);
            BigDecimal kvag = new BigDecimal(0.00);
            BigDecimal kvan = new BigDecimal(0.00);
            BigDecimal rvag = new BigDecimal(0.00);
            BigDecimal rvan = new BigDecimal(0.00);
            BigDecimal avag = new BigDecimal(0.00);
            BigDecimal avan = new BigDecimal(0.00);
            BigDecimal kirc = new BigDecimal(0.00);
            BigDecimal lstr = new BigDecimal(0.00);
            BigDecimal soli = new BigDecimal(0.00);

            BigDecimal igum = new BigDecimal(0.00);
            BigDecimal uml1 = new BigDecimal(0.00);
            BigDecimal uml2 = new BigDecimal(0.00);

            BigDecimal pvan_absolut = new BigDecimal(0.00);
            BigDecimal pvag_absolut = new BigDecimal(0.00);

            BigDecimal kvan_absolut = new BigDecimal(0.00);
            BigDecimal kvag_absolut;

            BigDecimal bbg_kv_tmp = data.Zeitraum.equals("m") ?
                    bbg_kv.get(data.AbrJahr) : bbg_kv.get(data.AbrJahr).multiply(new BigDecimal(12));

            if(bbg_kv_tmp.compareTo(getBigDecimal(calculation.data.SVPflBrutto)) < 0) {
                // falls Brutto schon über KV-BBG: feste Beträge
                isUeberBbg_KV = true;
                pvan_absolut = pvan.multiply(bbg_kv_tmp).setScale(2, BigDecimal.ROUND_HALF_UP);
                pvag_absolut = pvag.multiply(bbg_kv_tmp).setScale(2, BigDecimal.ROUND_HALF_UP);
                kvan_absolut = getBigDecimal(calculation.data.Krankenversicherung_AN);
            } else {
                kvag = getBigDecimal(calculation.data.Krankenversicherung_AG).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_UP);
                kvan = getBigDecimal(calculation.data.Krankenversicherung_AN).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_UP);
            }

            // falls Brutto schon über RV-BBG: feste Beträge
            boolean isBbgOst = GetIsBbgOst(data.Bundesland);
            BigDecimal bbg_rv = isBbgOst ? bbg_rv_ost.get(data.AbrJahr) : bbg_rv_west.get(data.AbrJahr);

            if(data.Zeitraum.equals("y"))
                bbg_rv = bbg_rv.multiply(new BigDecimal(12));

            BigDecimal rvan_absolut = new BigDecimal(0.00);
            BigDecimal rvag_absolut = new BigDecimal(0.00);
            BigDecimal avan_absolut = new BigDecimal(0.00);
            BigDecimal avag_absolut = new BigDecimal(0.00);
            BigDecimal igum_absolut = new BigDecimal(0.00);
            BigDecimal uml1_absolut = new BigDecimal(0.00);
            BigDecimal uml2_absolut = new BigDecimal(0.00);

            if(bbg_rv.compareTo(getBigDecimal(calculation.data.SVPflBrutto)) < 0){
                // falls Brutto schon über RV-BBG: feste Beträge
                isUeberBbg_RV = true;
                rvan_absolut = getBigDecimal(calculation.data.Rentenversicherung_AG);
                rvag_absolut = getBigDecimal(calculation.data.Rentenversicherung_AN);
                avan_absolut = getBigDecimal(calculation.data.Arbeitslosenversicherung_AG);
                avag_absolut = getBigDecimal(calculation.data.Arbeitslosenversicherung_AN);
                igum_absolut = getBigDecimal(calculation.data.IGU); // unnötig
                uml1_absolut = getBigDecimal(calculation.data.Umlage1); // unnötig
                uml2_absolut = getBigDecimal(calculation.data.Umlage2); // unnötig
            } else {
                rvag = getBigDecimal(calculation.data.Rentenversicherung_AG).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_DOWN);
                rvan = getBigDecimal(calculation.data.Rentenversicherung_AN).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_DOWN);
                avag = getBigDecimal(calculation.data.Arbeitslosenversicherung_AG).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_DOWN);
                avan = getBigDecimal(calculation.data.Arbeitslosenversicherung_AN).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_DOWN);
                kirc = getBigDecimal(calculation.data.Kirchensteuer).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_UP);
                lstr = getBigDecimal(calculation.data.Lohnsteuer).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_UP);
                soli = getBigDecimal(calculation.data.Soli).divide(brutto_alt, 6, BigDecimal.ROUND_HALF_UP);

                igum = getBigDecimal(calculation.data.IGU).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_DOWN);
                uml1 = getBigDecimal(calculation.data.Umlage1).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_DOWN);
                uml2 = getBigDecimal(calculation.data.Umlage2).divide(brutto_alt, 5, BigDecimal.ROUND_HALF_DOWN);
            }

            // Double prozentAbgabenAN_alt = kvan + rvan + avan + an_alt + kirc + lstr + soli;
            BigDecimal prozentAbgabenAN_neu = kvan.add(rvan).add(avan).add(pvan).add(kirc).add(lstr).add(soli);

            // Double prozentAbgabenAG_alt = kvag + rvag + avag + ag_alt + igum + uml1 + uml2;
            // BigDecimal prozentAbgabenAG_neu = kvag.add(rvag).add(avag).add(pvag).add(igum).add(uml1).add(uml2);

            netto_as_dividend = netto_as_dividend.add(pvan_absolut).add(kvan_absolut).add(rvan_absolut).add(avan_absolut);
            BigDecimal fiktivesNeuesBrutto = netto_as_dividend.divide(new BigDecimal("1").subtract(prozentAbgabenAN_neu), 2, BigDecimal.ROUND_HALF_UP);
            // Double fiktivesAltesBrutto    = nett / (1 - prozentAbgabenAN_alt); // Probe

            if(isUeberBbg_KV) {
                kvag_absolut = getBigDecimal(calculation.data.Krankenversicherung_AG); // bleibt
                kvan_absolut = getBigDecimal(calculation.data.Krankenversicherung_AN); // bleibt
                calculation.data.Pflegeversicherung_AG  = getDecimalString_Up(pvan_absolut);
                calculation.data.Pflegeversicherung_AN  = getDecimalString_Up(pvag_absolut);
            } else {
                kvag_absolut = fiktivesNeuesBrutto.multiply(kvag);
                calculation.data.Krankenversicherung_AG = getDecimalString_Up(kvag_absolut);
                kvan_absolut = fiktivesNeuesBrutto.multiply(kvan);
                calculation.data.Krankenversicherung_AN = getDecimalString_Up(kvan_absolut);
                pvan_absolut = fiktivesNeuesBrutto.multiply(pvan);
                calculation.data.Pflegeversicherung_AN  = getDecimalString_Up(pvan_absolut);
                pvag_absolut = fiktivesNeuesBrutto.multiply(pvag);
                calculation.data.Pflegeversicherung_AG  = getDecimalString_Up(pvag_absolut);
            }

            if(!isUeberBbg_RV) {
                // Betraege bleiben nicht
                rvag_absolut = fiktivesNeuesBrutto.multiply(rvag);
                calculation.data.Rentenversicherung_AG = getDecimalString_Up(rvag_absolut);
                rvan_absolut = fiktivesNeuesBrutto.multiply(rvan);
                calculation.data.Rentenversicherung_AN = getDecimalString_Up(rvan_absolut);
                avag_absolut = fiktivesNeuesBrutto.multiply(avag);
                calculation.data.Arbeitslosenversicherung_AG = getDecimalString_Up(avag_absolut);
                avan_absolut = fiktivesNeuesBrutto.multiply(avan);
                calculation.data.Arbeitslosenversicherung_AN = getDecimalString_Up(avan_absolut);
                igum_absolut = fiktivesNeuesBrutto.multiply(igum);
                calculation.data.IGU = getDecimalString_Up(igum_absolut);
                uml1_absolut = fiktivesNeuesBrutto.multiply(uml1);
                calculation.data.Umlage1 = getDecimalString_Up(uml1_absolut);
                uml2_absolut = fiktivesNeuesBrutto.multiply(uml2);
                calculation.data.Umlage2 = getDecimalString_Up(uml2_absolut);
                calculation.data.Umlagen_AG = getDecimalString_Up(igum_absolut.add(uml1_absolut).add(uml2_absolut));
            }
            BigDecimal ag_anteil = kvag_absolut.add(rvag_absolut).add(avag_absolut).add(pvag_absolut);
            calculation.data.AGAnteil = getDecimalString_Up(ag_anteil);
            calculation.data.Abgaben_AG = getDecimalString_Up(ag_anteil.add(igum_absolut).add(uml1_absolut).add(uml2_absolut));

            calculation.data.Kirchensteuer = getDecimalString_Down(fiktivesNeuesBrutto.multiply(kirc));
            BigDecimal lstr_abs = fiktivesNeuesBrutto.multiply(lstr);
            calculation.data.Lohnsteuer = getDecimalString_Down(lstr_abs);
            BigDecimal soli_abs = fiktivesNeuesBrutto.multiply(soli);
            calculation.data.Soli = getDecimalString_Up(soli_abs);
            calculation.data.Steuern = getDecimalString_Up(lstr_abs.add(soli_abs));
            calculation.data.ANAnteil = getDecimalString_Up(kvan_absolut.add(rvan_absolut).add(avan_absolut).add(pvan_absolut));

            calculation.data.SVPflBrutto = getDecimalString_Up(fiktivesNeuesBrutto);
            calculation.data.LohnsteuerPflBrutto = getDecimalString_Up(fiktivesNeuesBrutto);
        } catch (Exception e) {
            // Log.d("NursuringInsurance",e.getMessage());
        }
    }

    public void correctPensionInsurance_Pauschal_AG(Calculation calculation) {

        try {
            BigDecimal rvag_neu = getBigDecimal(calculation.data.SVPflBrutto).multiply(anteilAG_pauschRV_Minijob_sv);
            calculation.data.Rentenversicherung_AG = getDecimalString_Up(rvag_neu);
        } catch (Exception e) {
            // ("correct",e.getMessage());
        }
    }

    public void correctPensionInsurance_Aufstocker_AN(Calculation calculation) {

        try {
            BigDecimal rvan_alt = getBigDecimal(calculation.data.Rentenversicherung_AN);
            BigDecimal rvan_neu = getBigDecimal(calculation.data.SVPflBrutto).multiply(anteilAN_aufstocker_Minijob_sv);
            BigDecimal tmp_min_rv = data.Zeitraum.equals("y") ? min__aufstocker_Minijob_sv_.multiply(new BigDecimal(12)) : min__aufstocker_Minijob_sv_;
            if(rvan_neu.compareTo(tmp_min_rv) < 0)
                rvan_neu = tmp_min_rv;

            calculation.data.Rentenversicherung_AN = getDecimalString_Up(rvan_neu);

            BigDecimal anteil_an = getBigDecimal(calculation.data.ANAnteil);
            anteil_an = anteil_an.subtract(rvan_alt);
            anteil_an = anteil_an.add(rvan_neu);
            calculation.data.ANAnteil = getDecimalString_Up(anteil_an);

            BigDecimal netto = getBigDecimal(calculation.data.Netto);
            netto = netto.add(rvan_alt);
            netto = netto.subtract(rvan_neu);
            calculation.data.Netto = getDecimalString_Up(netto);

            BigDecimal auszahlung = getBigDecimal(calculation.data.Auszahlung);
            auszahlung = auszahlung.add(rvan_alt);
            auszahlung = auszahlung.subtract(rvan_neu);
            calculation.data.Auszahlung = getDecimalString_Up(auszahlung);

        } catch (Exception e) {
            //Log.d("correct",e.getMessage());
        }
    }

    public void correctPauschaleSteuer_Minijob(Calculation calculation) {
        try {
            BigDecimal brutto  = getBigDecimal(calculation.data.LohnsteuerPflBrutto);
            BigDecimal pauchSt = brutto.multiply(percent_pausch_steuer_minijob);

            calculation.data.Pausch_LohnSteuer_AG = getDecimalString_Up(pauchSt);
        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
        }
    }

    public void correctPauschaleSteuer_Minijob_Abwaelzung(Calculation calculation) {
        try {
            BigDecimal brutto  = getBigDecimal(calculation.data.LohnsteuerPflBrutto);
            BigDecimal pauchSt = brutto.multiply(percent_pausch_steuer_minijob);

            calculation.data.Pausch_LohnSteuer_AN = getDecimalString_Up(pauchSt);
        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
        }
    }

    public void correctPauschaleSteuer_Kurzfristig(Calculation calculation) {
        try {
            BigDecimal brutto  = getBigDecimal(calculation.data.LohnsteuerPflBrutto);
            BigDecimal pauchSt = brutto.multiply(percent_pausch_steuer_kurzfristig).setScale(2, RoundingMode.DOWN);
            BigDecimal pauchSoli = pauchSt.multiply(percent_soli).setScale(2, RoundingMode.DOWN);
            BigDecimal pauchKiSt = new BigDecimal(0);
            if(data.Kirche) {
                if(percentErmaessigteKirchensteuer.get(data.Bundesland) != null) {
                    pauchKiSt =  pauchSt.multiply(percentErmaessigteKirchensteuer.get(data.Bundesland));
                }
            }

            calculation.data.Pausch_LohnSteuer_AG = getDecimalString_Down(pauchSt);
            calculation.data.Pausch_Soli_AG = getDecimalString_Down(pauchSoli);
            calculation.data.Pausch_Kirchensteuer_AG = getDecimalString_Down(pauchKiSt);
        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
        }
    }

    public void correctPauschaleSteuer_Kurzfristig_Abwaelzung(Calculation calculation) {
        try {
            BigDecimal brutto  = getBigDecimal(calculation.data.LohnsteuerPflBrutto);
            BigDecimal pauchSt = brutto.multiply(percent_pausch_steuer_kurzfristig).setScale(2, RoundingMode.HALF_UP);
            BigDecimal pauchSoli = pauchSt.multiply(percent_soli).setScale(2, RoundingMode.HALF_UP);
            BigDecimal pauchKiSt = new BigDecimal(0);
            if(data.Kirche) {
                if(percentErmaessigteKirchensteuer.get(data.Bundesland) != null) {
                    pauchKiSt =  pauchSt.multiply(percentErmaessigteKirchensteuer.get(data.Bundesland)).setScale(2, RoundingMode.HALF_UP);
                }
            }

            calculation.data.Pausch_LohnSteuer_AN = getDecimalString_Down(pauchSt);
            calculation.data.Pausch_Soli_AN = getDecimalString_Down(pauchSoli);
            calculation.data.Pausch_Kirchensteuer_AN = getDecimalString_Down(pauchKiSt);

            if(data.Berechnungsart.equals("Nettolohn") || isFakeBruttolohn) {
                BigDecimal diff = brutto.subtract(wunschnetto).subtract(pauchSt).subtract(pauchSoli).subtract(pauchKiSt);
                if(Math.abs(diff.doubleValue()) > 0.009) {
                    // Rundungsfehler kaschieren
                    calculation.data.LohnsteuerPflBrutto = getDecimalString_Down(brutto.subtract(diff));
                    calculation.data.SVPflBrutto = calculation.data.LohnsteuerPflBrutto;
                    calculation.data.Netto = calculation.data.SVPflBrutto;
                }
                isFakeBruttolohn = false;
            }

        } catch (Exception e) {
            MessageHelper.snackbar(this, e.getMessage());
        }
    }

    private static boolean GetIsBbgOst(int bundesland) {
        return bundesland == 4 || bundesland == 8 || bundesland == 13 || bundesland == 14 || bundesland == 30;
    }

    public static BigDecimal getBigDecimal(String s) {
        if(s != null && s.length() > 0)
            return new BigDecimal(s.replace(".", "").replace(",", "."));

        return BigDecimal.valueOf(0.00);
    }

    public static String getDecimalString_Up(BigDecimal d) {
        return d.setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",");
    }

    private String getDecimalString_Down(BigDecimal d) {
        return d.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString().replace(".", ",");
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
        helper.data.KK_text = insuranceAc.getText().toString();
        helper.data.Kirche = selectedChurchTax;
        helper.data.KindU23 = selectedHasChildren;
        helper.data.KindFrei = selectedChildAmount;
        helper.data.abwaelzung_pauschale_steuer = selectedShifting;
        helper.data.hatAltersvorsorge = selectedHasProvision;
        helper.data.Altersvorsorge_summe = selectedProvisionSum;
        helper.data.Altersvorsorge_zuschuss = selectedProvisionGrant;
        helper.data.hatFirmenwagen = selectedHasCar;
        helper.data.Firmenwagen_summe = selectedCarAmount;
        helper.data.Firmenwagen_km = selectedCarDistance;

        String message;

        try {
            return helper.validate();
        } catch (ValidationInsuranceException e) {
            message = e.getMessage() + "!";
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        MessageHelper.snackbar(this, message);
        return false;
    }


    /*
     * Renders and displays a popup window
     * with advertisement and cancel button.

    public void showCalculatePopupWindow()
    {
        //spamWebView.setVisibility(View.VISIBLE);
        //calculateButton.setVisibility(View.INVISIBLE);
    }
    */

    /**
     * Renders a calculation dialog.
     */
    private void showCalculationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //builder.setTitle("Berechnung läuft");
        //builder.setMessage("Wollen Sie die Berechnung abbrechen?");

        builder.setTitle("Berechnung läuft");

        builder.setNegativeButton("Berechnung abbrechen", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                doAbortCalculation = true;
                if(queue.size() > 0) {
                    queue.remove();
                    queue.add(-1);
                }
                isCalculationEnabled = true;
                dialog.dismiss();
            }
        });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    doAbortCalculation = true;
                    if(queue.size() > 0) {
                        queue.remove();
                        queue.add(-1);
                    }
                    isCalculationEnabled = true;
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        this.calcDialog = builder.create();
        this.calcDialog.getCurrentFocus();
        calcDialog.show();
        isCalculationEnabled = false;


        /*
        Dialog calcDialog = MessageHelper.dialog(instance, true,
        getResources().getString(R.string.calculation_started), MessageHelper.DIALOG_TYPE_ALERT);
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
        showCalculationDialog();
        // showCalculatePopupWindow();
    }


    /**
     * Dismiss all open dialog or popup windows.
     */
    private void dismissCalculationOverlay()
    {
        //spamWebView.setVisibility(View.INVISIBLE);
        //calculateButton.setVisibility(View.VISIBLE);

        if (null != calcDialog && calcDialog.isShowing())
            calcDialog.dismiss();

        /*
        if (null != calcPopup && calcPopup.isShowing())
            calcPopup.dismiss();*/

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                isCalculationEnabled = true;
                doAbortCalculation = true;
                this.finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
