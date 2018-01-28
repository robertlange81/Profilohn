package com.profilohn.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.profilohn.Exceptions.FormatException;
import com.profilohn.Helper.FileStore;
import com.profilohn.Helper.FormatHelper;
import com.profilohn.Models.Calculation;
import com.profilohn.R;

public class ResultEmployerFragment extends Fragment
{
    private Activity activity;

    TextView txtTitle;
    TextView txtWageGross;
    TextView txtWageGross_compare;
    TextView txtTitleCompare;
    TextView txtCumCat;
    TextView txtCumCat_compare;

    TextView txtSocialEmployer;
    TextView txtSocialEmployer_compare;

    TextView txtProvision;
    TextView txtProvision_compare;

    RelativeLayout regionTax;
    View hr;
    RelativeLayout regionTaxLst;
    RelativeLayout regionTaxSoli;
    RelativeLayout regionTaxKist;
    RelativeLayout regionProvision;

    TextView txtTaxEmployer;
    TextView txtTaxEmployer_compare;
    TextView txtTaxEmployerLst;
    TextView txtTaxEmployerLst_compare;
    TextView txtTaxEmployerSoli;
    TextView txtTaxEmployerSoli_compare;
    TextView txtTaxEmployerKiSt;
    TextView txtTaxEmployerKiSt_compare;

    TextView txtPensionEmployer;
    TextView txtPensionEmployer_compare;
    TextView txtUnemploymentEmployer;
    TextView txtUnemploymentEmployer_compare;
    TextView txtCareEmployer;
    TextView txtCareEmployer_compare;
    TextView txtHealthEmployer;
    TextView txtHealthEmployer_compare;

    TextView txtContribution;
    TextView txtContribution_compare;
    TextView txtContribution1;
    TextView txtContribution1_compare;
    TextView txtContribution2;
    TextView txtContribution2_compare;
    TextView txtContributionIgu;
    TextView txtContributionIgu_compare;

    public ResultEmployerFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employer, container, false);

        // get the calculation result from the activity
        Calculation data = activity.getIntent().getExtras().getParcelable("Calculation");
        Calculation dataCompare;

        // try to fetch previous data and set compare layout if so ..
        try {
            FileStore f = new FileStore(getContext());
            dataCompare = f.readCalculationResult();
        } catch (Exception e) {
            dataCompare = null;
        }

        _initViews(view);
        _setViewData(data, dataCompare);
        _postfixValues();
        _initializeListener(view);

        return view;
    }

    public void setUserVisibleHint(boolean v)
    {
        super.setUserVisibleHint(v);
        if (v) {
            getActivity().setTitle(getResources().getString(R.string.result_employer_title));
        }
    }

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

    private void _initViews(View view)
    {
        // Title views
        txtTitle = (TextView) view.findViewById(R.id.result_employer_title_wage);
        txtTitleCompare = (TextView) view.findViewById(R.id.result_employer_title_wage_compare);

        txtWageGross = (TextView) view.findViewById(R.id.result_employer_wage_gross);
        txtWageGross_compare = (TextView) view.findViewById(R.id.result_employer_wage_gross_compare);

        // Data views
        txtSocialEmployer = (TextView) view.findViewById(R.id.result_employer_social_contribution);
        txtSocialEmployer_compare = (TextView) view.findViewById(R.id.result_employer_social_contribution_compare);
        txtPensionEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_pension);
        txtPensionEmployer_compare = (TextView) view.findViewById(R.id.result_employer_insurance_pension_compare);
        txtUnemploymentEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_unemployment);
        txtUnemploymentEmployer_compare = (TextView) view.findViewById(R.id.result_employer_insurance_unemployment_compare);
        txtCareEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_care);
        txtCareEmployer_compare = (TextView) view.findViewById(R.id.result_employer_insurance_care_compare);
        txtHealthEmployer = (TextView) view.findViewById(R.id.result_employer_insurance_health);
        txtHealthEmployer_compare = (TextView) view.findViewById(R.id.result_employer_insurance_health_compare);

        txtTaxEmployerLst = (TextView) view.findViewById(R.id.result_employer_base_tax);
        txtTaxEmployerLst_compare = (TextView) view.findViewById(R.id.result_employer_base_tax_compare);
        txtTaxEmployerSoli = (TextView) view.findViewById(R.id.result_employer_soli_tax);
        txtTaxEmployerSoli_compare = (TextView) view.findViewById(R.id.result_employer_soli_tax_compare);
        txtTaxEmployerKiSt = (TextView) view.findViewById(R.id.result_employer_church_tax);
        txtTaxEmployerKiSt_compare = (TextView) view.findViewById(R.id.result_employer_church_tax_compare);
        txtTaxEmployer = (TextView) view.findViewById(R.id.result_employer_tax);
        txtTaxEmployer_compare = (TextView) view.findViewById(R.id.result_employer_tax_compare);

        txtProvision = (TextView) view.findViewById(R.id.result_employer_provision);
        txtProvision_compare = (TextView) view.findViewById(R.id.result_employer_provision_compare);

        // regions
        regionTax = (RelativeLayout) view.findViewById(R.id.result_employer_tax_region);
        hr = view.findViewById(R.id.result_employer_tax_hr);

        regionTaxLst = (RelativeLayout) view.findViewById(R.id.result_employer_base_tax_region);
        regionTaxSoli = (RelativeLayout) view.findViewById(R.id.result_employer_soli_tax_region);
        regionTaxKist = (RelativeLayout) view.findViewById(R.id.result_employer_church_tax_region);
        regionProvision = (RelativeLayout) view.findViewById(R.id.result_employer_provision_region);

        txtContribution  = (TextView) view.findViewById(R.id.result_employer_contribution);
        txtContribution_compare  = (TextView) view.findViewById(R.id.result_employer_contribution_compare);
        txtContribution1 = (TextView) view.findViewById(R.id.result_employer_contribution1);
        txtContribution1_compare = (TextView) view.findViewById(R.id.result_employer_contribution1_compare);
        txtContribution2 = (TextView) view.findViewById(R.id.result_employer_contribution2);
        txtContribution2_compare = (TextView) view.findViewById(R.id.result_employer_contribution2_compare);
        txtContributionIgu = (TextView) view.findViewById(R.id.result_employer_insolvency_contribution);
        txtContributionIgu_compare = (TextView) view.findViewById(R.id.result_employer_insolvency_contribution_compare);

        txtCumCat = (TextView) view.findViewById(R.id.result_employer_cum_cat);
        txtCumCat_compare = (TextView) view.findViewById(R.id.result_employer_cum_cat_compare);
    }

    private void _setViewData(Calculation data, Calculation dataCompare)
    {
        txtTitle.setText(_formatCurrency(data.data.Abgaben_AG));
        txtWageGross.setText(_formatCurrency(data.data.LohnsteuerPflBrutto));
        txtCumCat.setText(_formatCurrency(data.data.Abgaben_AG));

        if(data.data.pauschSt_AG.equals("0,00") && data.data.pauschSt_AN.equals("0,00")) {
            regionTax.setVisibility(View.GONE);
            hr.setVisibility(View.GONE);
            regionTaxLst.setVisibility(View.GONE);
            regionTaxSoli.setVisibility(View.GONE);
            regionTaxKist.setVisibility(View.GONE);
        } else {
            regionTax.setVisibility(View.VISIBLE);
            hr.setVisibility(View.VISIBLE);
            regionTaxLst.setVisibility(View.VISIBLE);
            regionTaxSoli.setVisibility(View.VISIBLE);
            regionTaxKist.setVisibility(View.VISIBLE);
            txtTaxEmployerLst.setText(_formatCurrency(data.data.Pausch_LohnSteuer_AG));
            txtTaxEmployerSoli.setText(_formatCurrency(data.data.Pausch_Soli_AG));
            txtTaxEmployerKiSt.setText(_formatCurrency(data.data.Pausch_Kirchensteuer_AG));
            txtTaxEmployer.setText(_formatCurrency(data.data.pauschSt_AG));
        }

        if(data.data.AG_Zuschuss_Altersvorsorge.equals("0,00")
            || data.data.AG_Zuschuss_Altersvorsorge.equals("")) {
            regionProvision.setVisibility(View.GONE);
        } else {
            regionProvision.setVisibility(View.VISIBLE);
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

        txtProvision.setText(_formatCurrency(data.data.AG_Zuschuss_Altersvorsorge));

        _compareView(data, dataCompare);
    }

    private void _compareView(Calculation data, Calculation dataCompare) {

        txtTitleCompare.setVisibility(View.INVISIBLE);
        txtSocialEmployer_compare.setVisibility(View.INVISIBLE);
        txtTaxEmployer_compare.setVisibility(View.INVISIBLE);
        txtContribution_compare.setVisibility(View.INVISIBLE);
        txtProvision_compare.setVisibility(View.INVISIBLE);

        if(dataCompare == null) {
            return;
        }

        int green = Color.parseColor("#008000");

        Double oldBrutto = FormatHelper.toDouble(dataCompare.data.LohnsteuerPflBrutto);
        Double newBrutto = FormatHelper.toDouble(data.data.LohnsteuerPflBrutto);

        if(newBrutto * 10 < oldBrutto || oldBrutto * 10 < newBrutto)
            // anscheinend auf Jahreswerte / Monatswerte umgestellt / in jedem Fall sinnlos zu vergleichen
            return;

        Double diffBrutto = newBrutto - oldBrutto;
        if(diffBrutto >= 0.01) {
            txtWageGross_compare.setVisibility(View.VISIBLE);
            txtWageGross_compare.setTextColor(Color.RED);
        } else if(diffBrutto <= -0.01) {
            txtWageGross_compare.setVisibility(View.VISIBLE);
            txtWageGross_compare.setTextColor(green);
        } else {
            txtWageGross_compare.setVisibility(View.INVISIBLE);
            txtWageGross_compare.setTextColor(Color.WHITE);
        }
        txtWageGross_compare.setText((diffBrutto > 0 ? "+" : "") +_formatCurrency(diffBrutto));

        // veränderte Altersvorsorge
        if(dataCompare.data.AG_Zuschuss_Altersvorsorge != null) {
            Double oldProvision = FormatHelper.toDouble(dataCompare.data.AG_Zuschuss_Altersvorsorge);
            Double newProvision = FormatHelper.toDouble(data.data.AG_Zuschuss_Altersvorsorge);
            Double diffProvision = newProvision - oldProvision;
            if(diffProvision >= 0.01) {
                txtProvision_compare.setVisibility(View.VISIBLE);
                txtProvision_compare.setTextColor(Color.RED);
            } else if(diffProvision <= -0.01) {
                txtProvision_compare.setVisibility(View.VISIBLE);
                txtProvision_compare.setTextColor(green);
            } else {
                txtProvision_compare.setVisibility(View.INVISIBLE);
                txtProvision_compare.setTextColor(Color.WHITE);
            }
            txtProvision_compare.setText((diffProvision > 0 ? "+" : "") +_formatCurrency(diffProvision));

        }

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

        Double oldRv = FormatHelper.toDouble(dataCompare.data.Rentenversicherung_AG);
        Double newRv = FormatHelper.toDouble(data.data.Rentenversicherung_AG);
        Double diffRv = newRv - oldRv;
        if(diffRv >= 0.01) {
            txtPensionEmployer_compare.setVisibility(View.VISIBLE);
            txtPensionEmployer_compare.setTextColor(Color.RED);
        } else if(diffRv <= -0.01) {
            txtPensionEmployer_compare.setVisibility(View.VISIBLE);
            txtPensionEmployer_compare.setTextColor(green);
        } else {
            txtPensionEmployer_compare.setVisibility(View.INVISIBLE);
            txtPensionEmployer_compare.setTextColor(Color.WHITE);
        }
        txtPensionEmployer_compare.setText((diffRv > 0 ? "+" : "") +_formatCurrency(diffRv));

        Double oldAv = FormatHelper.toDouble(dataCompare.data.Arbeitslosenversicherung_AG);
        Double newAv = FormatHelper.toDouble(data.data.Arbeitslosenversicherung_AG);
        Double diffAv = newAv - oldAv;
        if(diffAv >= 0.01) {
            txtUnemploymentEmployer_compare.setVisibility(View.VISIBLE);
            txtUnemploymentEmployer_compare.setTextColor(Color.RED);
        } else if(diffAv <= -0.01) {
            txtUnemploymentEmployer_compare.setVisibility(View.VISIBLE);
            txtUnemploymentEmployer_compare.setTextColor(green);
        } else {
            txtUnemploymentEmployer_compare.setVisibility(View.INVISIBLE);
            txtUnemploymentEmployer_compare.setTextColor(Color.WHITE);
        }
        txtUnemploymentEmployer_compare.setText((diffAv > 0 ? "+" : "") +_formatCurrency(diffAv));

        Double oldKv = FormatHelper.toDouble(dataCompare.data.Krankenversicherung_AG);
        Double newKv = FormatHelper.toDouble(data.data.Krankenversicherung_AG);
        Double diffKv = newKv - oldKv;
        if(diffKv >= 0.01) {
            txtHealthEmployer_compare.setVisibility(View.VISIBLE);
            txtHealthEmployer_compare.setTextColor(Color.RED);
        } else if(diffKv <= -0.01) {
            txtHealthEmployer_compare.setVisibility(View.VISIBLE);
            txtHealthEmployer_compare.setTextColor(green);
        } else {
            txtHealthEmployer_compare.setVisibility(View.INVISIBLE);
            txtHealthEmployer_compare.setTextColor(Color.WHITE);
        }
        txtHealthEmployer_compare.setText((diffKv > 0 ? "+" : "") +_formatCurrency(diffKv));

        Double oldPv = FormatHelper.toDouble(dataCompare.data.Pflegeversicherung_AG);
        Double newPv = FormatHelper.toDouble(data.data.Pflegeversicherung_AG);
        Double diffPv = newPv - oldPv;
        if(diffPv >= 0.01) {
            txtCareEmployer_compare.setVisibility(View.VISIBLE);
            txtCareEmployer_compare.setTextColor(Color.RED);
        } else if(diffPv <= -0.01) {
            txtCareEmployer_compare.setVisibility(View.VISIBLE);
            txtCareEmployer_compare.setTextColor(green);
        } else {
            txtCareEmployer_compare.setVisibility(View.INVISIBLE);
            txtCareEmployer_compare.setTextColor(Color.WHITE);
        }
        txtCareEmployer_compare.setText((diffPv > 0 ? "+" : "") +_formatCurrency(diffPv));

        Double oldU1 = FormatHelper.toDouble(dataCompare.data.Umlage1);
        Double newU1 = FormatHelper.toDouble(data.data.Umlage1);
        Double diffU1 = newU1 - oldU1;
        if(diffU1 >= 0.01) {
            txtContribution1_compare.setVisibility(View.VISIBLE);
            txtContribution1_compare.setTextColor(Color.RED);
        } else if(diffU1 <= -0.01) {
            txtContribution1_compare.setVisibility(View.VISIBLE);
            txtContribution1_compare.setTextColor(green);
        } else {
            txtContribution1_compare.setVisibility(View.INVISIBLE);
            txtContribution1_compare.setTextColor(Color.WHITE);
        }
        txtContribution1_compare.setText((diffU1 > 0 ? "+" : "") +_formatCurrency(diffU1));

        Double oldU2 = FormatHelper.toDouble(dataCompare.data.Umlage2);
        Double newU2 = FormatHelper.toDouble(data.data.Umlage2);
        Double diffU2 = newU2 - oldU2;
        if(diffU2 >= 0.01) {
            txtContribution2_compare.setVisibility(View.VISIBLE);
            txtContribution2_compare.setTextColor(Color.RED);
        } else if(diffU2 <= -0.01) {
            txtContribution2_compare.setVisibility(View.VISIBLE);
            txtContribution2_compare.setTextColor(green);
        } else {
            txtContribution2_compare.setVisibility(View.INVISIBLE);
            txtContribution2_compare.setTextColor(Color.WHITE);
        }
        txtContribution2_compare.setText((diffU2 > 0 ? "+" : "") +_formatCurrency(diffU2));

        Double oldIGU = FormatHelper.toDouble(dataCompare.data.IGU);
        Double newIGU = FormatHelper.toDouble(data.data.IGU);
        Double diffIGU = newIGU - oldIGU;
        if(diffIGU >= 0.01) {
            txtContributionIgu_compare.setVisibility(View.VISIBLE);
            txtContributionIgu_compare.setTextColor(Color.RED);
        } else if(diffIGU <= -0.01) {
            txtContributionIgu_compare.setVisibility(View.VISIBLE);
            txtContributionIgu_compare.setTextColor(green);
        } else {
            txtContributionIgu_compare.setVisibility(View.INVISIBLE);
            txtContributionIgu_compare.setTextColor(Color.WHITE);
        }
        txtContributionIgu_compare.setText((diffIGU > 0 ? "+" : "") +_formatCurrency(diffIGU));

        Double oldLsTPausch = FormatHelper.toDouble(dataCompare.data.Pausch_LohnSteuer_AG);
        Double newLsTPausch = FormatHelper.toDouble(data.data.Pausch_LohnSteuer_AG);
        Double diffLsTPausch = newLsTPausch - oldLsTPausch;
        if(diffLsTPausch >= 0.01) {
            txtTaxEmployerLst_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerLst_compare.setTextColor(Color.RED);
        } else if(diffLsTPausch <= -0.01) {
            txtTaxEmployerLst_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerLst_compare.setTextColor(green);
        } else {
            txtTaxEmployerLst_compare.setVisibility(View.INVISIBLE);
            txtTaxEmployerLst_compare.setTextColor(Color.WHITE);
        }
        txtTaxEmployerLst_compare.setText((diffLsTPausch > 0 ? "+" : "") +_formatCurrency(diffLsTPausch));

        Double oldKiPausch = FormatHelper.toDouble(dataCompare.data.Pausch_Kirchensteuer_AG);
        Double newKiPausch = FormatHelper.toDouble(data.data.Pausch_Kirchensteuer_AG);
        Double diffKiPausch = newKiPausch - oldKiPausch;
        if(diffKiPausch >= 0.01) {
            txtTaxEmployerKiSt_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerKiSt_compare.setTextColor(Color.RED);
        } else if(diffKiPausch <= -0.01) {
            txtTaxEmployerKiSt_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerKiSt_compare.setTextColor(green);
        } else {
            txtTaxEmployerKiSt_compare.setVisibility(View.INVISIBLE);
            txtTaxEmployerKiSt_compare.setTextColor(Color.WHITE);
        }
        txtTaxEmployerKiSt_compare.setText((diffKiPausch > 0 ? "+" : "") +_formatCurrency(diffKiPausch));

        Double oldSoliPausch = FormatHelper.toDouble(dataCompare.data.Pausch_Soli_AG);
        Double newSoliPausch = FormatHelper.toDouble(data.data.Pausch_Soli_AG);
        Double diffSoliPausch = newSoliPausch - oldSoliPausch;
        if(diffSoliPausch >= 0.01) {
            txtTaxEmployerSoli_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerSoli_compare.setTextColor(Color.RED);
        } else if(diffSoliPausch <= -0.01) {
            txtTaxEmployerSoli_compare.setVisibility(View.VISIBLE);
            txtTaxEmployerSoli_compare.setTextColor(green);
        } else {
            txtTaxEmployerSoli_compare.setVisibility(View.INVISIBLE);
            txtTaxEmployerSoli_compare.setTextColor(Color.WHITE);
        }
        txtTaxEmployerSoli_compare.setText((diffSoliPausch > 0 ? "+" : "") +_formatCurrency(diffSoliPausch));

        Double oldAbgabenGes = FormatHelper.toDouble(dataCompare.data.Abgaben_AG);
        Double newAbgabenGes = FormatHelper.toDouble(data.data.Abgaben_AG);
        Double diffAbgabenGes = newAbgabenGes - oldAbgabenGes;
        if(diffAbgabenGes >= 0.01) {
            txtCumCat_compare.setVisibility(View.VISIBLE);
            txtCumCat_compare.setTextColor(Color.RED);
        } else if(diffAbgabenGes <= -0.01) {
            txtCumCat_compare.setVisibility(View.VISIBLE);
            txtCumCat_compare.setTextColor(green);
        } else {
            txtCumCat_compare.setVisibility(View.INVISIBLE);
            txtCumCat_compare.setTextColor(Color.WHITE);
        }
        txtCumCat_compare.setText((diffAbgabenGes > 0 ? "+" : "") +_formatCurrency(diffAbgabenGes));
    }


    /**
     * Postfix all view values with currency Euro (€).
     */
    private void _postfixValues()
    {
        TextView views[] = {txtTitle, txtWageGross};

        for (TextView view : views) {
            CharSequence text = view.getText();
            if (text.charAt(text.length() - 1) != '€') {
                view.setText(text + "€");
            }
        }
    }

    private String _formatCurrency(String text)
    {
        try {
            return FormatHelper.currency(text);
        } catch (FormatException e) {

        }

        return "FormatError";
    }

    private String _formatCurrency(Double number)
    {
        try {
            return FormatHelper.currency(number);
        } catch (FormatException e) {
        }

        return "FormatError";
    }
}