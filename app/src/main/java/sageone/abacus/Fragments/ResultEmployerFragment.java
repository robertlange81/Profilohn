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
public class ResultEmployerFragment extends Fragment
{
    private Activity activity;

    TextView txtTitle;
    TextView txtWageGross;
    TextView txtCumCat;

    TextView txtSocialEmployer;
    TextView txtPensionEmployer;
    TextView txtUnemploymentEmployer;
    TextView txtCareEmployer;
    TextView txtHealthEmployer;

    TextView txtContribution;
    TextView txtContribution1;
    TextView txtContribution2;

    public ResultEmployerFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = getActivity();
        activity.setTitle(R.string.title_activity_result_employer);
        View view = inflater.inflate(R.layout.fragment_result_employer, container, false);

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
     * of result employer.
     *
     * @param args
     * @return
     */
    public static ResultEmployerFragment newInstance(Bundle args)
    {
        ResultEmployerFragment fragment = new ResultEmployerFragment();
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
        AppCompatButton btnAgain = (AppCompatButton) view.findViewById(R.id.result_employer_btn_again);
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
        txtTitle = (TextView) view.findViewById(R.id.result_employer_title_wage);
        txtWageGross = (TextView) view.findViewById(R.id.result_employer_wage_gross);

        // Data views

        txtSocialEmployer = (TextView) view.findViewById(R.id.result_employer_social_contribution);
        txtPensionEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_pension);
        txtUnemploymentEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_unemployment);
        txtCareEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_care);
        txtHealthEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_health);

        txtContribution  = (TextView) view.findViewById(R.id.result_employer_contribution);
        txtContribution1 = (TextView) view.findViewById(R.id.result_employer_contribution1);
        txtContribution2 = (TextView) view.findViewById(R.id.result_employer_contribution2);

        txtCumCat = (TextView) view.findViewById(R.id.result_employer_cum_cat);
    }


    /**
     * Set all view data.
     *
     * @param data
     */
    private void _setViewData(Calculation data)
    {
        txtTitle.setText(data.data.Abgaben_AG);
        txtWageGross.setText(data.data.LohnsteuerPflBrutto);
        txtCumCat.setText(data.data.Abgaben_AG);
        txtSocialEmployer.setText(data.data.AGAnteil);
        txtPensionEmployer.setText(data.data.Rentenversicherung_AG);
        txtUnemploymentEmployer.setText(data.data.Arbeitslosenversicherung_AG);
        txtCareEmployer.setText(data.data.Pflegeversicherung_AG);
        txtHealthEmployer.setText(data.data.Krankenversicherung_AG);

        txtContribution.setText(data.data.Umlagen_AG);
        txtContribution1.setText(data.data.Umlage1);
        txtContribution2.setText(data.data.Umlage2);
    }


    /**
     * Postfix all view values with currency Euro (€).
     */
    private void _postfixValues()
    {
        TextView views[] = {txtTitle, txtWageGross, txtCumCat};

        for (int i = 0; i < views.length; i++)
            views[i].setText(views[i].getText() + "€");
    }


}