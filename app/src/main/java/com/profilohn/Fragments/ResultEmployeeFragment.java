package com.profilohn.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.profilohn.Exceptions.FormatException;
import com.profilohn.Helper.FileStore;
import com.profilohn.Helper.FormatHelper;
import com.profilohn.Models.Calculation;
import com.profilohn.R;

/**
 * Created by profilohn on 11.02.2016.
 */
public class ResultEmployeeFragment extends Fragment
{
    public static ResultEmployeeFragment instance;
    private Activity activity;

    TextView txtTitle;
    TextView txtTitleCompare;
    TextView txtWageGross;
    TextView txtWageNet;

    TextView txtTax;
    TextView txtTax_compare;
    TextView txtWageTax;
    TextView txtSolidarity;
    TextView txtChurchTax;

    TextView txtSocial;
    TextView txtSocial_compare;
    TextView txtPension;
    TextView txtUnemployment;
    TextView txtCare;
    TextView txtHealth;

    LinearLayout regionTax;
    LinearLayout regionTaxLst;
    LinearLayout regionTaxSoli;
    LinearLayout regionTaxKist;
    TextView txtPauschTaxEmployee;
    TextView txtPauschTaxEmployeeLst;
    TextView txtPauschTaxEmployeeSoli;
    TextView txtPauschTaxEmployeeKiSt;
    TextView txtPauschTaxEmployee_compare;

    public ResultEmployeeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employee, container, false);

        // get the calculation result from the activity
        Calculation data = activity.getIntent().getExtras().getParcelable("Calculation");
        Calculation dataCompare = null;

        // try to fetch previous data and set compare layout if so ..
        try {
            FileStore f = new FileStore(getContext());
            dataCompare = f.readCalculationResult();
        } catch (Exception e) {
            int x = 0;
        }

        _initViews(view);
        _setViewData(data, dataCompare);
        _initializeListener(view);

        return view;
    }


    /**
     * Instantiates a ResultEmployeeFragment.
     *
     * @param args
     * @return
     */
    public static ResultEmployeeFragment getInstance(Bundle args)
    {
        if (null == instance) {
            instance = new ResultEmployeeFragment();
        }
        instance.setArguments(args);

        return instance;
    }


    /**
     * Callback to get the visibility
     * status of this fragment.
     *
     * @param v
     */
    public void setUserVisibleHint(boolean v)
    {
        super.setUserVisibleHint(v);
        if (v) {
            instance.getActivity().setTitle(getResources().getString(R.string.result_employee_title));
        }
    }


    /**
     * Init all view activity listeners.
     *
     * @param view
     */
    private void _initializeListener(View view)
    {
        AppCompatButton btnAgain = (AppCompatButton) view.findViewById(R.id.result_employee_btn_again);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    /**
     * Init all available views.
     *
     * @param view
     */
    private void _initViews(View view)
    {
        // Title views
        txtTitle = (TextView) view.findViewById(R.id.result_employee_title_wage);
        txtTitleCompare = (TextView) view.findViewById(R.id.result_employee_title_wage_compare);
        txtWageGross = (TextView) view.findViewById(R.id.result_employee_wage_gross);

        // Data views
        txtTax = (TextView) view.findViewById(R.id.result_employee_tax);
        txtTax_compare = (TextView) view.findViewById(R.id.result_employee_tax_compare);
        txtWageTax = (TextView) view.findViewById(R.id.result_employee_wage_wagetax);
        txtSolidarity = (TextView) view.findViewById(R.id.result_employee_solidarity);
        txtChurchTax = (TextView) view.findViewById(R.id.result_employee_churchtax);

        txtSocial = (TextView) view.findViewById(R.id.result_employee_social_contribution);
        txtSocial_compare = (TextView) view.findViewById(R.id.result_employee_social_contribution_compare);
        txtPension = (TextView) view.findViewById(R.id.result_employee_insurance_pension);
        txtUnemployment = (TextView) view.findViewById(R.id.result_employee_insurance_unemployment);
        txtCare = (TextView) view.findViewById(R.id.result_employee_insurance_care);
        txtHealth = (TextView) view.findViewById(R.id.result_employee_insurance_health);

        txtWageNet = (TextView) view.findViewById(R.id.result_employee_wage_net);

        txtPauschTaxEmployeeLst = (TextView) view.findViewById(R.id.result_employee_base_tax);
        txtPauschTaxEmployeeSoli = (TextView) view.findViewById(R.id.result_employee_soli_tax);
        txtPauschTaxEmployeeKiSt = (TextView) view.findViewById(R.id.result_employee_church_tax);
        txtPauschTaxEmployee = (TextView) view.findViewById(R.id.result_employee_tax_p);
        txtPauschTaxEmployee_compare = (TextView) view.findViewById(R.id.result_employee_tax_p_compare);

        // regions
        regionTax = (LinearLayout) view.findViewById(R.id.result_employee_tax_region);
        regionTaxLst = (LinearLayout) view.findViewById(R.id.result_employee_base_tax_region);
        regionTaxSoli = (LinearLayout) view.findViewById(R.id.result_employee_soli_tax_region);
        regionTaxKist = (LinearLayout) view.findViewById(R.id.result_employee_church_tax_region);
    }


    /**
     * Set all view data.
     *
     * @param data
     */
    private void _setViewData(Calculation data, Calculation dataCompare)
    {
        int green = Color.parseColor("#008000");
        if(data.data.pauschSt_AN.equals("0,00")) {
            regionTax.setVisibility(View.GONE);
            regionTaxLst.setVisibility(View.GONE);
            regionTaxSoli.setVisibility(View.GONE);
            regionTaxKist.setVisibility(View.GONE);
        } else {
            regionTax.setVisibility(View.VISIBLE);
            regionTaxLst.setVisibility(View.VISIBLE);
            regionTaxSoli.setVisibility(View.VISIBLE);
            regionTaxKist.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeLst.setText(_formatCurrency(data.data.Pausch_LohnSteuer_AN));
            txtPauschTaxEmployeeSoli.setText(_formatCurrency(data.data.Pausch_Soli_AN));
            txtPauschTaxEmployeeKiSt.setText(_formatCurrency(data.data.Pausch_Kirchensteuer_AN));
            txtPauschTaxEmployee.setText(_formatCurrency(data.data.pauschSt_AN));
        }

        txtTitle.setText(_formatCurrency(data.data.Netto));
        if(dataCompare != null) {
            Double oldNetto = FormatHelper.toDouble(dataCompare.data.Netto);
            Double newNetto = FormatHelper.toDouble(data.data.Netto);
            Double diffNetto = newNetto - oldNetto;
            if(diffNetto >= 0.01) {
                txtTitleCompare.setVisibility(View.VISIBLE);
                txtTitleCompare.setTextColor(Color.GREEN);
            } else if(diffNetto <= -0.01) {
                txtTitleCompare.setVisibility(View.VISIBLE);
                txtTitleCompare.setTextColor(Color.RED);
            } else {
                txtTitleCompare.setVisibility(View.INVISIBLE);
                txtTitleCompare.setTextColor(Color.WHITE);
            }
            txtTitleCompare.setText((diffNetto > 0 ? "+" : "") +_formatCurrency(diffNetto));

            Double oldSteuern = FormatHelper.toDouble(dataCompare.data.Steuern);
            Double newSteuern = FormatHelper.toDouble(data.data.Steuern);
            Double diffSteuern = newSteuern - oldSteuern;
            if(diffSteuern >= 0.01) {
                txtTax_compare.setVisibility(View.VISIBLE);
                txtTax_compare.setTextColor(Color.RED);
            } else if(diffSteuern <= -0.01) {
                txtTax_compare.setVisibility(View.VISIBLE);
                txtTax_compare.setTextColor(green);
            } else {
                txtTax_compare.setVisibility(View.INVISIBLE);
                txtTax_compare.setTextColor(Color.WHITE);
            }
            txtTax_compare.setText((diffSteuern > 0 ? "+" : "") +_formatCurrency(diffSteuern));

            Double oldSv = FormatHelper.toDouble(dataCompare.data.ANAnteil);
            Double newSv = FormatHelper.toDouble(data.data.ANAnteil);
            Double diffSv = newSv - oldSv;
            if(diffSv >= 0.01) {
                txtSocial_compare.setVisibility(View.VISIBLE);
                txtSocial_compare.setTextColor(Color.RED);
            } else if(diffSv <= -0.01) {
                txtSocial_compare.setVisibility(View.VISIBLE);
                txtSocial_compare.setTextColor(green);
            } else {
                txtSocial_compare.setVisibility(View.INVISIBLE);
                txtSocial_compare.setTextColor(Color.WHITE);
            }
            txtSocial_compare.setText((diffSv > 0 ? "+" : "") +_formatCurrency(diffSv));

            Double oldTaxPausch = FormatHelper.toDouble(dataCompare.data.pauschSt_AN);
            Double newTaxPausch = FormatHelper.toDouble(data.data.pauschSt_AN);
            Double diffTaxPausch = newTaxPausch - oldTaxPausch;
            if(diffTaxPausch >= 0.01) {
                txtPauschTaxEmployee_compare.setVisibility(View.VISIBLE);
                txtPauschTaxEmployee_compare.setTextColor(Color.RED);
            } else if(diffTaxPausch <= -0.01) {
                txtPauschTaxEmployee_compare.setVisibility(View.VISIBLE);
                txtPauschTaxEmployee_compare.setTextColor(green);
            } else {
                txtPauschTaxEmployee_compare.setVisibility(View.INVISIBLE);
                txtPauschTaxEmployee_compare.setTextColor(Color.WHITE);
            }
            txtPauschTaxEmployee_compare.setText((diffTaxPausch > 0 ? "+" : "") +_formatCurrency(diffTaxPausch));
        }

        txtWageGross.setText(_formatCurrency(data.data.LohnsteuerPflBrutto));
        txtWageNet.setText(_formatCurrency(data.data.Netto));
        txtTax.setText(_formatCurrency(data.data.Steuern));
        txtWageTax.setText(_formatCurrency(data.data.Lohnsteuer));
        txtSolidarity.setText(_formatCurrency(data.data.Soli));
        txtChurchTax.setText(_formatCurrency(data.data.Kirchensteuer));
        txtSocial.setText(_formatCurrency(data.data.ANAnteil));
        txtPension.setText(_formatCurrency(data.data.Rentenversicherung_AN));
        txtUnemployment.setText(_formatCurrency(data.data.Arbeitslosenversicherung_AN));
        txtCare.setText(_formatCurrency(data.data.Pflegeversicherung_AN));
        txtHealth.setText(_formatCurrency(data.data.Krankenversicherung_AN));
    }

    private void _compareView() {

    }

    private String _formatCurrency(String text)
    {
        try {
            return FormatHelper.currency(text.toString());
        } catch (FormatException e) {
            Log.e("FormatHelperError", "");
        }

        return "FormatError";
    }

    private String _formatCurrency(Double number)
    {
        try {
            return FormatHelper.currency(number);
        } catch (FormatException e) {
            Log.e("FormatHelperError", "");
        }

        return "FormatError";
    }

}