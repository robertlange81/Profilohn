package sageone.abacus.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import sageone.abacus.Exceptions.FormatException;
import sageone.abacus.Helper.FormatHelper;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.Models.Calculation;
import sageone.abacus.R;

/**
 * Created by otomaske on 11.02.2016.
 */
public class ResultEmployeeFragment extends Fragment
{
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

    public ResultEmployeeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = getActivity();
        activity.setTitle(R.string.title_activity_result_employee);
        View view = inflater.inflate(R.layout.fragment_result_employee, container, false);

        // get the calculation result from the activity
        Calculation data = (Calculation) activity.getIntent().getExtras().getParcelable("Calculation");

        _initViews(view);
        _setViewData(data);
        _initializeListener(view);

        return view;
    }


    /**
     * Instantiates new fragment
     * of result employee.
     *
     * @param args
     * @return
     */
    public static ResultEmployeeFragment newInstance(Bundle args)
    {
        ResultEmployeeFragment fragment = new ResultEmployeeFragment();
        fragment.setArguments(args);

        return fragment;
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
    }


    /**
     * Set all view data.
     *
     * @param data
     */
    private void _setViewData(Calculation data)
    {
        txtTitle.setText(_formatCurrency(data.data.Netto));
        txtWageGross.setText(_formatCurrency(data.data.LohnsteuerPflBrutto));
        txtWageNet.setText(_formatCurrency(data.data.Netto));
        txtTax.setText(_formatCurrency(data.data.Steuern));
        txtWageTax.setText(_formatCurrency(data.data.Lohnsteuer));
        txtSolidarity.setText(_formatCurrency(data.data.Soli));
        txtChurchTax.setText(_formatCurrency(data.data.Kirchensteuer));
        txtSocial.setText(_formatCurrency(data.data.AGAnteil));
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