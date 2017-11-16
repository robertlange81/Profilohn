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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.profilohn.Exceptions.FormatException;
import com.profilohn.Helper.FileStore;
import com.profilohn.Helper.FormatHelper;
import com.profilohn.Models.Calculation;
import com.profilohn.R;

/**
 * Created by profilohn on 11.02.2016.
 */
public class ResultEmployerFragment extends Fragment
{
    public static ResultEmployerFragment instance;
    private Activity activity;

    TextView txtTitle;
    TextView txtWageGross;
    TextView txtTitleCompare;
    TextView txtCumCat;

    TextView txtSocialEmployer;
    TextView txtSocialEmployer_compare;

    RelativeLayout regionTax;
    LinearLayout regionTaxLst;
    LinearLayout regionTaxSoli;
    LinearLayout regionTaxKist;
    TextView txtTaxEmployer;
    TextView txtTaxEmployer_compare;
    TextView txtTaxEmployerLst;
    TextView txtTaxEmployerSoli;
    TextView txtTaxEmployerKiSt;

    TextView txtPensionEmployer;
    TextView txtUnemploymentEmployer;
    TextView txtCareEmployer;
    TextView txtHealthEmployer;

    TextView txtContribution;
    TextView txtContribution_compare;
    TextView txtContribution1;
    TextView txtContribution2;
    TextView txtContributionIgu;


    public ResultEmployerFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employer, container, false);

        // get the calculation result from the activity
        Calculation data = activity.getIntent().getExtras().getParcelable("Calculation");
        Calculation dataCompare = null;

        // try to fetch previous data and set compare layout if so ..
        try {
            FileStore f = new FileStore(getContext());
            dataCompare = f.readCalculationResult();
        } catch (Exception e) {

        }

        _initViews(view);
        _setViewData(data, dataCompare);
        _postfixValues();
        _initializeListener(view);

        return view;
    }


    /**
     * Instantiates a ResultEmployerFragment.
     *
     * @param args
     * @return
     */
    public static ResultEmployerFragment getInstance(Bundle args)
    {
        if (null == instance) {
            instance = new ResultEmployerFragment();
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
            instance.getActivity().setTitle(getResources().getString(R.string.result_employer_title));
        }
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
        txtTitleCompare = (TextView) view.findViewById(R.id.result_employer_title_wage_compare);

        // Data views
        txtSocialEmployer = (TextView) view.findViewById(R.id.result_employer_social_contribution);
        txtSocialEmployer_compare = (TextView) view.findViewById(R.id.result_employer_social_contribution_compare);
        txtPensionEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_pension);
        txtUnemploymentEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_unemployment);
        txtCareEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_care);
        txtHealthEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_health);

        txtTaxEmployerLst = (TextView) view.findViewById(R.id.result_employer_base_tax);
        txtTaxEmployerSoli = (TextView) view.findViewById(R.id.result_employer_soli_tax);
        txtTaxEmployerKiSt = (TextView) view.findViewById(R.id.result_employer_church_tax);
        txtTaxEmployer = (TextView) view.findViewById(R.id.result_employer_tax);
        txtTaxEmployer_compare = (TextView) view.findViewById(R.id.result_employer_tax_compare);

        // regions
        regionTax = (RelativeLayout) view.findViewById(R.id.result_employer_tax_region);
        regionTaxLst = (LinearLayout) view.findViewById(R.id.result_employer_base_tax_region);
        regionTaxSoli = (LinearLayout) view.findViewById(R.id.result_employer_soli_tax_region);
        regionTaxKist = (LinearLayout) view.findViewById(R.id.result_employer_church_tax_region);

        txtContribution  = (TextView) view.findViewById(R.id.result_employer_contribution);
        txtContribution_compare  = (TextView) view.findViewById(R.id.result_employer_contribution_compare);
        txtContribution1 = (TextView) view.findViewById(R.id.result_employer_contribution1);
        txtContribution2 = (TextView) view.findViewById(R.id.result_employer_contribution2);
        txtContributionIgu = (TextView) view.findViewById(R.id.result_employer_insolvency_contribution);

        txtCumCat = (TextView) view.findViewById(R.id.result_employer_cum_cat);
    }


    /**
     * Set all view data.
     *
     * @param data
     */
    private void _setViewData(Calculation data, Calculation dataCompare)
    {
        txtTitle.setText(_formatCurrency(data.data.Abgaben_AG));
        txtWageGross.setText(_formatCurrency(data.data.LohnsteuerPflBrutto));
        txtCumCat.setText(_formatCurrency(data.data.Abgaben_AG));

        if(data.data.pauschSt_AG.equals("0,00") && data.data.pauschSt_AN.equals("0,00")) {
            regionTax.setVisibility(View.GONE);
            regionTaxLst.setVisibility(View.GONE);
            regionTaxSoli.setVisibility(View.GONE);
            regionTaxKist.setVisibility(View.GONE);
        } else {
            regionTax.setVisibility(View.VISIBLE);
            regionTaxLst.setVisibility(View.VISIBLE);
            regionTaxSoli.setVisibility(View.VISIBLE);
            regionTaxKist.setVisibility(View.VISIBLE);
            txtTaxEmployerLst.setText(_formatCurrency(data.data.Pausch_LohnSteuer_AG));
            txtTaxEmployerSoli.setText(_formatCurrency(data.data.Pausch_Soli_AG));
            txtTaxEmployerKiSt.setText(_formatCurrency(data.data.Pausch_Kirchensteuer_AG));
            txtTaxEmployer.setText(_formatCurrency(data.data.pauschSt_AG));
        }

        txtSocialEmployer.setText(_formatCurrency(data.data.AGAnteil));
        txtPensionEmployer.setText(_formatCurrency(data.data.Rentenversicherung_AG));
        txtUnemploymentEmployer.setText(_formatCurrency(data.data.Arbeitslosenversicherung_AG));
        txtCareEmployer.setText(_formatCurrency(data.data.Pflegeversicherung_AG));
        txtHealthEmployer.setText(_formatCurrency(data.data.Krankenversicherung_AG));

        txtContribution.setText(_formatCurrency(data.data.Umlagen_AG));
        txtContribution1.setText(_formatCurrency(data.data.Umlage1));
        txtContribution2.setText(_formatCurrency(data.data.Umlage2));
        txtContributionIgu.setText(_formatCurrency(data.data.IGU));

        _compareView(data, dataCompare);
    }

    private void _compareView(Calculation data, Calculation dataCompare) {

        txtTitleCompare.setVisibility(View.VISIBLE);
        txtSocialEmployer_compare.setVisibility(View.INVISIBLE);
        txtTaxEmployer_compare.setVisibility(View.INVISIBLE);
        txtContribution_compare.setVisibility(View.INVISIBLE);

        if(dataCompare == null) {
            return;
        }

        int green = Color.parseColor("#008000");

        Double oldAbgaben = FormatHelper.toDouble(dataCompare.data.Abgaben_AG);
        Double newAbgaben = FormatHelper.toDouble(data.data.Abgaben_AG);

        if(newAbgaben * 10 < oldAbgaben || oldAbgaben * 10 < newAbgaben)
            // anscheinend auf Jahreswerte / Monatswerte umgestellt / in jedem Fall sinnlos zu vergleichen
            return;

        Double diffAbgaben = newAbgaben - oldAbgaben;
        if(diffAbgaben >= 0.01) {
            txtTitleCompare.setVisibility(View.VISIBLE);
            txtTitleCompare.setTextColor(Color.RED);
        } else if(diffAbgaben <= -0.01) {
            txtTitleCompare.setVisibility(View.VISIBLE);
            txtTitleCompare.setTextColor(Color.GREEN);
        } else {
            txtTitleCompare.setVisibility(View.INVISIBLE);
            txtTitleCompare.setTextColor(Color.WHITE);
        }
        txtTitleCompare.setText((diffAbgaben > 0 ? "+" : "") +_formatCurrency(diffAbgaben));

        Double oldSv = FormatHelper.toDouble(dataCompare.data.AGAnteil);
        Double newSv = FormatHelper.toDouble(data.data.AGAnteil);
        Double diffSv = newSv - oldSv;
        if(diffSv >= 0.01) {
            txtSocialEmployer_compare.setVisibility(View.VISIBLE);
            txtSocialEmployer_compare.setTextColor(Color.RED);
        } else if(diffSv <= -0.01) {
            txtSocialEmployer_compare.setVisibility(View.VISIBLE);
            txtSocialEmployer_compare.setTextColor(green);
        } else {
            txtSocialEmployer_compare.setVisibility(View.INVISIBLE);
            txtSocialEmployer_compare.setTextColor(Color.WHITE);
        }
        txtSocialEmployer_compare.setText((diffSv > 0 ? "+" : "") +_formatCurrency(diffSv));

        Double oldTax = FormatHelper.toDouble(dataCompare.data.pauschSt_AG);
        Double newTax = FormatHelper.toDouble(data.data.pauschSt_AG);
        Double diffTax = newTax - oldTax;
        if(diffTax >= 0.01) {
            txtTaxEmployer_compare.setVisibility(View.VISIBLE);
            txtTaxEmployer_compare.setTextColor(Color.RED);
        } else if(diffTax <= -0.01) {
            txtTaxEmployer_compare.setVisibility(View.VISIBLE);
            txtTaxEmployer_compare.setTextColor(green);
        } else {
            txtTaxEmployer_compare.setVisibility(View.INVISIBLE);
            txtTaxEmployer_compare.setTextColor(Color.WHITE);
        }
        txtTaxEmployer_compare.setText((diffTax > 0 ? "+" : "") +_formatCurrency(diffTax));

        Double oldContribution = FormatHelper.toDouble(dataCompare.data.Umlagen_AG);
        Double newContribution = FormatHelper.toDouble(data.data.Umlagen_AG);
        Double diffContribution = newContribution- oldContribution;
        if(diffContribution >= 0.01) {
            txtContribution_compare.setVisibility(View.VISIBLE);
            txtContribution_compare.setTextColor(Color.RED);
        } else if(diffContribution <= -0.01) {
            txtContribution_compare.setVisibility(View.VISIBLE);
            txtContribution_compare.setTextColor(green);
        } else {
            txtContribution_compare.setVisibility(View.INVISIBLE);
            txtContribution_compare.setTextColor(Color.WHITE);
        }
        txtContribution_compare.setText((diffContribution > 0 ? "+" : "") +_formatCurrency(diffContribution));
    }


    /**
     * Postfix all view values with currency Euro (€).
     */
    private void _postfixValues()
    {
        TextView views[] = {txtTitle, txtWageGross};

        for (int i = 0; i < views.length; i++) {
            CharSequence text = views[i].getText();
            if(text.charAt(text.length() -1 ) != '€') {
                views[i].setText(text + "€");
            }
        }
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