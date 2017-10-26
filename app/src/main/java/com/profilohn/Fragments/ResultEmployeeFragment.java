package com.profilohn.Fragments;

import android.app.Activity;
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
    TextView txtWageGross;
    TextView txtWageNet;

    TextView txtTax;
    TextView txtWageTax;
    TextView txtSolidarity;
    TextView txtChurchTax;

    TextView txtSocial;
    TextView txtPension;
    TextView txtUnemployment;
    TextView txtCare;
    TextView txtHealth;

    LinearLayout regionTax;
    LinearLayout regionTaxLst;
    LinearLayout regionTaxSoli;
    LinearLayout regionTaxKist;
    TextView txtTaxEmployee;
    TextView txtTaxEmployeeLst;
    TextView txtTaxEmployeeSoli;
    TextView txtTaxEmployeeKiSt;

    public ResultEmployeeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employee, container, false);

        // get the calculation result from the activity
        Calculation data = activity.getIntent().getExtras().getParcelable("Calculation");

        _initViews(view);
        _setViewData(data);
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
        txtWageGross = (TextView) view.findViewById(R.id.result_employee_wage_gross);

        // Data views
        txtTax = (TextView) view.findViewById(R.id.result_employee_tax);
        txtWageTax = (TextView) view.findViewById(R.id.result_employee_wage_wagetax);
        txtSolidarity = (TextView) view.findViewById(R.id.result_employee_solidarity);
        txtChurchTax = (TextView) view.findViewById(R.id.result_employee_churchtax);

        txtSocial = (TextView) view.findViewById(R.id.result_employee_social_contribution);
        txtPension = (TextView) view.findViewById(R.id.result_employee_insurance_pension);
        txtUnemployment = (TextView) view.findViewById(R.id.result_employee_insurance_unemployment);
        txtCare = (TextView) view.findViewById(R.id.result_employee_insurance_care);
        txtHealth = (TextView) view.findViewById(R.id.result_employee_insurance_health);

        txtWageNet = (TextView) view.findViewById(R.id.result_employee_wage_net);

        txtTaxEmployeeLst = (TextView) view.findViewById(R.id.result_employee_base_tax);
        txtTaxEmployeeSoli = (TextView) view.findViewById(R.id.result_employee_soli_tax);
        txtTaxEmployeeKiSt = (TextView) view.findViewById(R.id.result_employee_church_tax);
        txtTaxEmployee = (TextView) view.findViewById(R.id.result_employee_tax);

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
    private void _setViewData(Calculation data)
    {
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
            txtTaxEmployeeLst.setText(_formatCurrency(data.data.Pausch_LohnSteuer_AN));
            txtTaxEmployeeSoli.setText(_formatCurrency(data.data.Pausch_Soli_AN));
            txtTaxEmployeeKiSt.setText(_formatCurrency(data.data.Pausch_Kirchensteuer_AN));
            txtTaxEmployee.setText(_formatCurrency(data.data.pauschSt_AN));
        }

        txtTitle.setText(_formatCurrency(data.data.Netto));
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


    private String _formatCurrency(String text)
    {
        try {
            return FormatHelper.currency(text.toString());
        } catch (FormatException e) {
            Log.e("FormatHelperError", "");
        }

        return "FormatError";
    }

}