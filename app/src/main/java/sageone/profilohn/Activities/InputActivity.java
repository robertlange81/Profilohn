package sageone.profilohn.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;

import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import sageone.profilohn.Exceptions.ValidationException;
import sageone.profilohn.Exceptions.ValidationInsuranceException;
import sageone.profilohn.Helper.CalculationInputHelper;
import sageone.profilohn.Helper.DecimalDigitsInputHelper;
import sageone.profilohn.Helper.EventHandler;
import sageone.profilohn.Helper.MessageHelper;
import sageone.profilohn.Interfaces.ApiCallbackListener;
import sageone.profilohn.Models.Calculation;
import sageone.profilohn.Models.CalculationInput;
import sageone.profilohn.Models.CalculationInputData;
import sageone.profilohn.Helper.FileStore;
import sageone.profilohn.Models.Insurances;
import sageone.profilohn.R;
import sageone.profilohn.Models.WebService;
import android.webkit.WebView;

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
    public static EditText              taxFree;
    public static Spinner               taxClass;
    public static Spinner               year;
    public static Spinner               state;
    public static Spinner               children;
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
    private Integer selectedYear = 1;
    private String  selectedState;
    private Double  selectedChildAmount = 0.0;

    private ArrayAdapter<String> insurancesAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        wage.requestFocus();
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
        calcType    = (RadioGroup) findViewById(R.id.type);
        wage        = (EditText) findViewById(R.id.wage);
        wagePeriod  = (CheckBox) findViewById(R.id.wage_period);
        state       = (Spinner) findViewById(R.id.state);
        taxClass    = (Spinner) findViewById(R.id.tax_class);
        year        = (Spinner) findViewById(R.id.year);
        taxFree     = (EditText) findViewById(R.id.tax_free);
        children    = (Spinner) findViewById(R.id.children);
        calculate   = (Button) findViewById(R.id.calculate);
        insuranceAc = (AutoCompleteTextView) findViewById(R.id.insuranceAc);
        churchTax   = (SwitchCompat) findViewById(R.id.church);

        wageAmountLabel = (TextView) findViewById(R.id.wageamount_label);

        wage.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});
        taxFree.setFilters(new InputFilter[]{new DecimalDigitsInputHelper(2)});

        // tax classes
        ArrayAdapter<CharSequence> ta = ArrayAdapter.createFromResource(this,
                R.array.taxclasses, android.R.layout.simple_spinner_dropdown_item);
        ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxClass.setAdapter(ta);

        // calc year
        String[] years = new String[]{
                (new Integer(Calendar.getInstance().get(Calendar.YEAR)-1)).toString(),
                (new Integer(Calendar.getInstance().get(Calendar.YEAR))).toString(),
                (new Integer(Calendar.getInstance().get(Calendar.YEAR)+1)).toString()
        };
        final List<String> yearsList = new ArrayList<>(Arrays.asList(years));
        ArrayAdapter<String> ya = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsList);

        ya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(ya);
        year.setSelection(1);

        // child free amount
        ArrayAdapter<CharSequence> ca = ArrayAdapter.createFromResource(this,
                R.array.childfree, android.R.layout.simple_spinner_dropdown_item);
        ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        children.setAdapter(ca);
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
                if (R.id.type_net == checkedId) {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_GROSS;
                    wageAmountLabel.setText(R.string.wageamount_gross);
                } else {
                    selectedWageType = CalculationInputHelper.WAGE_TYPE_NET;
                    wageAmountLabel.setText(R.string.wageamount_net);
                }
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

                boolean hasComma = cur.contains(".");
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

                boolean hasComma = cur.contains(".");
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

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = position;
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
                Log.d("Insurance", "onItemClick");
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
        children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChildAmount = Double.valueOf(position) / 2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // calculate button
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wage.clearFocus();
                taxFree.clearFocus();
                insuranceAc.clearFocus();

                if (!_setAndValidateData()) {
                    return;
                }

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
        insurancesAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_dropdown_item_1line, this.insurancesList);

        insurancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insuranceAc.setAdapter(insurancesAdapter);
    }


    @Override
    /**
     * What we do if calculation finished.
     */
    public void responseFinishCalculation(Calculation calculation)
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("Calculation", calculation);

        // dismissCalculationOverlay();
        startActivity(i);
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
        helper.data.StFreibetrag = selectedTaxFree;
        helper.data.StKl = selectedTaxClass;
        helper.data.AbrJahr = selectedYear + Calendar.getInstance().get(Calendar.YEAR) - 1;
        helper.setBundesland(selectedState);
        helper.data.KKBetriebsnummer = selectedInsuranceId;
        helper.data.Kirche = selectedChurchTax;
        helper.setKindFrei(selectedChildAmount);

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