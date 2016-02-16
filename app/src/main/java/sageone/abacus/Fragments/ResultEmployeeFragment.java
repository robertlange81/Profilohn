package sageone.abacus.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        _postfixValues();
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
        txtTitle.setText(data.data.Netto);
        txtWageGross.setText(data.data.LohnsteuerPflBrutto);
        txtWageNet.setText(data.data.Netto);
        txtTax.setText(data.data.Steuern);
        txtWageTax.setText(data.data.Lohnsteuer);
        txtSolidarity.setText(data.data.Soli);
        txtChurchTax.setText(data.data.Kirchensteuer);
        txtSocial.setText(data.data.Sozialabgaben);
        txtPension.setText(data.data.RentenversicherungAN);
        txtUnemployment.setText(data.data.ArbeitslosenversicherungAN);
        txtCare.setText(data.data.PflegeversicherungAN);
        txtHealth.setText(data.data.KrankenversicherungAN);
    }


    /**
     * Postfix all view values with currency Euro (€).
     */
    private void _postfixValues()
    {
        TextView views[] = {txtTitle, txtWageGross, txtWageNet, txtTax};

        for (int i = 0; i < views.length; i++)
            views[i].setText(views[i].getText() + "€");
    }

}