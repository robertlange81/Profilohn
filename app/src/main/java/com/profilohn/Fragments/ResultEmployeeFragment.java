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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.profilohn.Exceptions.FormatException;
import com.profilohn.Helper.FileStore;
import com.profilohn.Helper.FormatHelper;
import com.profilohn.Models.Calculation;
import com.profilohn.R;

public class ResultEmployeeFragment extends Fragment
{
    private Activity activity;

    TextView txtTitle;
    TextView txtTitleCompare;
    TextView txtWageGross;
    TextView txtWageGrossCompare;
    TextView txtWageNet;
    TextView txtWageNet_compare;

    TextView txtTax;
    TextView txtTax_compare;
    TextView txtWageTax;
    TextView txtWageTax_compare;
    TextView txtSolidarity;
    TextView txtSolidarity_compare;
    TextView txtChurchTax;
    TextView txtChurchTax_compare;

    TextView txtSocial;
    TextView txtSocial_compare;
    TextView txtPension;
    TextView txtPension_compare;
    TextView txtUnemployment;
    TextView txtUnemployment_compare;
    TextView txtCare;
    TextView txtCare_compare;
    TextView txtHealth;
    TextView txtHealth_compare;

    RelativeLayout regionTax;
    View hr;
    RelativeLayout regionTaxLst;
    RelativeLayout regionTaxSoli;
    RelativeLayout regionTaxKist;
    TextView txtPauschTaxEmployee;
    TextView txtPauschTaxEmployeeLst;
    TextView txtPauschTaxEmployeeSoli;
    TextView txtPauschTaxEmployeeKiSt;
    TextView txtPauschTaxEmployee_compare;
    TextView txtPauschTaxEmployeeLst_compare;
    TextView txtPauschTaxEmployeeSoli_compare;
    TextView txtPauschTaxEmployeeKiSt_compare;

    public ResultEmployeeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employee, container, false);

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
        _initializeListener(view);

        return view;
    }

    public void setUserVisibleHint(boolean v)
    {
        super.setUserVisibleHint(v);
        if (v) {
            getActivity().setTitle(getResources().getString(R.string.result_employee_title));
        }
    }

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

    private void _initViews(View view)
    {
        // Title views
        txtTitle = (TextView) view.findViewById(R.id.result_employee_title_wage);
        txtTitleCompare = (TextView) view.findViewById(R.id.result_employee_title_wage_compare);
        txtWageGross = (TextView) view.findViewById(R.id.result_employee_wage_gross);
        txtWageGrossCompare = (TextView) view.findViewById(R.id.result_employee_wage_gross_compare);

        // Data views
        txtTax = (TextView) view.findViewById(R.id.result_employee_tax);
        txtTax_compare = (TextView) view.findViewById(R.id.result_employee_tax_compare);
        txtWageTax = (TextView) view.findViewById(R.id.result_employee_wage_wagetax);
        txtWageTax_compare = (TextView) view.findViewById(R.id.result_employee_wage_wagetax_compare);
        txtSolidarity = (TextView) view.findViewById(R.id.result_employee_solidarity);
        txtSolidarity_compare = (TextView) view.findViewById(R.id.result_employee_solidarity_compare);
        txtChurchTax = (TextView) view.findViewById(R.id.result_employee_churchtax);
        txtChurchTax_compare = (TextView) view.findViewById(R.id.result_employee_churchtax_compare);

        txtSocial = (TextView) view.findViewById(R.id.result_employee_social_contribution);
        txtSocial_compare = (TextView) view.findViewById(R.id.result_employee_social_contribution_compare);
        txtPension = (TextView) view.findViewById(R.id.result_employee_insurance_pension);
        txtPension_compare = (TextView) view.findViewById(R.id.result_employee_insurance_pension_compare);
        txtUnemployment = (TextView) view.findViewById(R.id.result_employee_insurance_unemployment);
        txtUnemployment_compare = (TextView) view.findViewById(R.id.result_employee_insurance_unemployment_compare);
        txtCare = (TextView) view.findViewById(R.id.result_employee_insurance_care);
        txtCare_compare = (TextView) view.findViewById(R.id.result_employee_insurance_care_compare);
        txtHealth = (TextView) view.findViewById(R.id.result_employee_insurance_health);
        txtHealth_compare = (TextView) view.findViewById(R.id.result_employee_insurance_health_compare);

        txtWageNet = (TextView) view.findViewById(R.id.result_employee_wage_net);
        txtWageNet_compare = (TextView) view.findViewById(R.id.result_employee_wage_net_compare);

        txtPauschTaxEmployeeLst = (TextView) view.findViewById(R.id.result_employee_base_tax);
        txtPauschTaxEmployeeSoli = (TextView) view.findViewById(R.id.result_employee_soli_tax);
        txtPauschTaxEmployeeKiSt = (TextView) view.findViewById(R.id.result_employee_church_tax);
        txtPauschTaxEmployee = (TextView) view.findViewById(R.id.result_employee_tax_p);
        txtPauschTaxEmployeeLst_compare = (TextView) view.findViewById(R.id.result_employee_base_tax_compare);
        txtPauschTaxEmployeeSoli_compare = (TextView) view.findViewById(R.id.result_employee_soli_tax_compare);
        txtPauschTaxEmployeeKiSt_compare = (TextView) view.findViewById(R.id.result_employee_church_tax_compare);
        txtPauschTaxEmployee_compare = (TextView) view.findViewById(R.id.result_employee_tax_p_compare);

        // regions
        regionTax = (RelativeLayout) view.findViewById(R.id.result_employee_tax_region);
        hr = view.findViewById(R.id.result_employee_tax_hr);
        regionTaxLst = (RelativeLayout) view.findViewById(R.id.result_employee_base_tax_region);
        regionTaxSoli = (RelativeLayout) view.findViewById(R.id.result_employee_soli_tax_region);
        regionTaxKist = (RelativeLayout) view.findViewById(R.id.result_employee_church_tax_region);
    }

    private void _setViewData(Calculation data, Calculation dataCompare)
    {
        if(data.data.pauschSt_AN.equals("0,00")
                && data.data.pauschSt_AG.equals("0,00")) {
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
            txtPauschTaxEmployeeLst.setText(_formatCurrency(data.data.Pausch_LohnSteuer_AN));
            txtPauschTaxEmployeeSoli.setText(_formatCurrency(data.data.Pausch_Soli_AN));
            txtPauschTaxEmployeeKiSt.setText(_formatCurrency(data.data.Pausch_Kirchensteuer_AN));
            txtPauschTaxEmployee.setText(_formatCurrency(data.data.pauschSt_AN));
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

        _compareView(data, dataCompare);
    }

    private void _compareView(Calculation data, Calculation dataCompare) {

        txtTitleCompare.setVisibility(View.INVISIBLE);
        txtSocial_compare.setVisibility(View.INVISIBLE);
        txtPauschTaxEmployee_compare.setVisibility(View.INVISIBLE);
        txtWageGrossCompare.setVisibility(View.INVISIBLE);

        if(dataCompare == null) {
            return;
        }

        int green = Color.parseColor("#008000");

        // verändertes Brutto
        Double oldBrutto = FormatHelper.toDouble(dataCompare.data.LohnsteuerPflBrutto);
        Double newBrutto = FormatHelper.toDouble(data.data.LohnsteuerPflBrutto);

        if(newBrutto * 10 < oldBrutto || oldBrutto * 10 < newBrutto)
            // anscheinend auf Jahreswerte / Monatswerte umgestellt / in jedem Fall sinnlos zu vergleichen
            return;

        Double diffBrutto = newBrutto - oldBrutto;

        if(diffBrutto >= 0.01) {
            txtWageGrossCompare.setVisibility(View.VISIBLE);
            txtWageGrossCompare.setTextColor(green);
        } else if(diffBrutto <= -0.01) {
            txtWageGrossCompare.setVisibility(View.VISIBLE);
            txtWageGrossCompare.setTextColor(Color.RED);
        } else {
            txtWageGrossCompare.setVisibility(View.INVISIBLE);
            txtWageGrossCompare.setTextColor(Color.WHITE);
        }
        txtWageGrossCompare.setText((diffBrutto > 0 ? "+" : "") +_formatCurrency(diffBrutto));

        Double oldNetto = FormatHelper.toDouble(dataCompare.data.Netto);
        Double newNetto = FormatHelper.toDouble(data.data.Netto);
        Double diffNetto = newNetto - oldNetto;
        if(diffNetto >= 0.01) {
            txtTitleCompare.setVisibility(View.VISIBLE);
            txtTitleCompare.setTextColor(Color.GREEN);
            txtWageNet_compare.setVisibility(View.VISIBLE);
            txtWageNet_compare.setTextColor(green);
        } else if(diffNetto <= -0.01) {
            txtTitleCompare.setVisibility(View.VISIBLE);
            txtTitleCompare.setTextColor(Color.RED);
            txtWageNet_compare.setVisibility(View.VISIBLE);
            txtWageNet_compare.setTextColor(Color.RED);
        } else {
            txtTitleCompare.setVisibility(View.INVISIBLE);
            txtTitleCompare.setTextColor(Color.WHITE);
            txtWageNet_compare.setVisibility(View.INVISIBLE);
            txtWageNet_compare.setTextColor(Color.WHITE);
        }
        txtTitleCompare.setText((diffNetto > 0 ? "+" : "") +_formatCurrency(diffNetto));
        txtWageNet_compare.setText((diffNetto > 0 ? "+" : "") +_formatCurrency(diffNetto));

        // veränderte Summe Steuern
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

        // veränderte Summe SV
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

        // veränderte pauschale Steuer
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

        // veränderte Lohnsteuer
        Double oldLohnsteuer = FormatHelper.toDouble(dataCompare.data.Lohnsteuer);
        Double newLohnsteuer = FormatHelper.toDouble(data.data.Lohnsteuer);
        Double diffLohnsteuer = newLohnsteuer - oldLohnsteuer;
        if(diffLohnsteuer >= 0.01) {
            txtWageTax_compare.setVisibility(View.VISIBLE);
            txtWageTax_compare.setTextColor(Color.RED);
        } else if(diffLohnsteuer <= -0.01) {
            txtWageTax_compare.setVisibility(View.VISIBLE);
            txtWageTax_compare.setTextColor(green);
        } else {
            txtWageTax_compare.setVisibility(View.INVISIBLE);
            txtWageTax_compare.setTextColor(Color.WHITE);
        }
        txtWageTax_compare.setText((diffLohnsteuer > 0 ? "+" : "") +_formatCurrency(diffLohnsteuer));

        // veränderte Kirchensteuer
        Double oldKirchensteuer = FormatHelper.toDouble(dataCompare.data.Kirchensteuer);
        Double newKirchensteuer = FormatHelper.toDouble(data.data.Kirchensteuer);
        Double diffKirchensteuer = newKirchensteuer - oldKirchensteuer;
        if(diffKirchensteuer >= 0.01) {
            txtChurchTax_compare.setVisibility(View.VISIBLE);
            txtChurchTax_compare.setTextColor(Color.RED);
        } else if(diffKirchensteuer <= -0.01) {
            txtChurchTax_compare.setVisibility(View.VISIBLE);
            txtChurchTax_compare.setTextColor(green);
        } else {
            txtChurchTax_compare.setVisibility(View.INVISIBLE);
            txtChurchTax_compare.setTextColor(Color.WHITE);
        }
        txtChurchTax_compare.setText((diffKirchensteuer > 0 ? "+" : "") +_formatCurrency(diffKirchensteuer));

        // veränderter Soli
        Double oldSoli = FormatHelper.toDouble(dataCompare.data.Soli);
        Double newSoli = FormatHelper.toDouble(data.data.Soli);
        Double diffSoli = newSoli - oldSoli;
        if(diffSoli >= 0.01) {
            txtSolidarity_compare.setVisibility(View.VISIBLE);
            txtSolidarity_compare.setTextColor(Color.RED);
        } else if(diffSoli <= -0.01) {
            txtSolidarity_compare.setVisibility(View.VISIBLE);
            txtSolidarity_compare.setTextColor(green);
        } else {
            txtSolidarity_compare.setVisibility(View.INVISIBLE);
            txtSolidarity_compare.setTextColor(Color.WHITE);
        }
        txtSolidarity_compare.setText((diffSoli > 0 ? "+" : "") +_formatCurrency(diffSoli));

        // veränderte RV
        Double oldRV = FormatHelper.toDouble(dataCompare.data.Rentenversicherung_AN);
        Double newRV = FormatHelper.toDouble(data.data.Rentenversicherung_AN);
        Double diffRV = newRV - oldRV;
        if(diffRV >= 0.01) {
            txtPension_compare.setVisibility(View.VISIBLE);
            txtPension_compare.setTextColor(Color.RED);
        } else if(diffRV <= -0.01) {
            txtPension_compare.setVisibility(View.VISIBLE);
            txtPension_compare.setTextColor(green);
        } else {
            txtPension_compare.setVisibility(View.INVISIBLE);
            txtPension_compare.setTextColor(Color.WHITE);
        }
        txtPension_compare.setText((diffRV > 0 ? "+" : "") +_formatCurrency(diffRV));

        // veränderte AV
        Double oldAV = FormatHelper.toDouble(dataCompare.data.Arbeitslosenversicherung_AN);
        Double newAV = FormatHelper.toDouble(data.data.Arbeitslosenversicherung_AN);
        Double diffAV = newAV - oldAV;
        if(diffAV >= 0.01) {
            txtUnemployment_compare.setVisibility(View.VISIBLE);
            txtUnemployment_compare.setTextColor(Color.RED);
        } else if(diffAV <= -0.01) {
            txtUnemployment_compare.setVisibility(View.VISIBLE);
            txtUnemployment_compare.setTextColor(green);
        } else {
            txtUnemployment_compare.setVisibility(View.INVISIBLE);
            txtUnemployment_compare.setTextColor(Color.WHITE);
        }
        txtUnemployment_compare.setText((diffAV > 0 ? "+" : "") +_formatCurrency(diffAV));

        // veränderte KV
        Double oldKV = FormatHelper.toDouble(dataCompare.data.Krankenversicherung_AN);
        Double newKV = FormatHelper.toDouble(data.data.Krankenversicherung_AN);
        Double diffKV = newKV - oldKV;
        if(diffKV >= 0.01) {
            txtHealth_compare.setVisibility(View.VISIBLE);
            txtHealth_compare.setTextColor(Color.RED);
        } else if(diffKV <= -0.01) {
            txtHealth_compare.setVisibility(View.VISIBLE);
            txtHealth_compare.setTextColor(green);
        } else {
            txtHealth_compare.setVisibility(View.INVISIBLE);
            txtHealth_compare.setTextColor(Color.WHITE);
        }
        txtHealth_compare.setText((diffKV > 0 ? "+" : "") +_formatCurrency(diffKV));

        // veränderte PV
        Double oldPV = FormatHelper.toDouble(dataCompare.data.Pflegeversicherung_AN);
        Double newPV = FormatHelper.toDouble(data.data.Pflegeversicherung_AN);
        Double diffPV = newPV - oldPV;
        if(diffPV >= 0.01) {
            txtCare_compare.setVisibility(View.VISIBLE);
            txtCare_compare.setTextColor(Color.RED);
        } else if(diffPV <= -0.01) {
            txtCare_compare.setVisibility(View.VISIBLE);
            txtCare_compare.setTextColor(green);
        } else {
            txtCare_compare.setVisibility(View.INVISIBLE);
            txtCare_compare.setTextColor(Color.WHITE);
        }
        txtCare_compare.setText((diffPV > 0 ? "+" : "") +_formatCurrency(diffPV));

        // veränderte Pauschale Lst
        Double oldPauschLst = FormatHelper.toDouble(dataCompare.data.Pausch_LohnSteuer_AN);
        Double newPauschLst = FormatHelper.toDouble(data.data.Pausch_LohnSteuer_AN);
        Double diffPauschLst = newPauschLst - oldPauschLst;
        if(diffPauschLst >= 0.01) {
            txtPauschTaxEmployeeLst_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeLst_compare.setTextColor(Color.RED);
        } else if(diffPauschLst <= -0.01) {
            txtPauschTaxEmployeeLst_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeLst_compare.setTextColor(green);
        } else {
            txtPauschTaxEmployeeLst_compare.setVisibility(View.INVISIBLE);
            txtPauschTaxEmployeeLst_compare.setTextColor(Color.WHITE);
        }
        txtPauschTaxEmployeeLst_compare.setText((diffPauschLst > 0 ? "+" : "") +_formatCurrency(diffPauschLst));

        // veränderte Pauschale KirchenSt
        Double oldPauschKirchenSt = FormatHelper.toDouble(dataCompare.data.Pausch_Kirchensteuer_AN);
        Double newPauschKirchenSt = FormatHelper.toDouble(data.data.Pausch_Kirchensteuer_AN);
        Double diffPauschKirchenSt = newPauschKirchenSt - oldPauschKirchenSt;
        if(diffPauschKirchenSt >= 0.01) {
            txtPauschTaxEmployeeKiSt_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeKiSt_compare.setTextColor(Color.RED);
        } else if(diffPauschKirchenSt <= -0.01) {
            txtPauschTaxEmployeeKiSt_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeKiSt_compare.setTextColor(green);
        } else {
            txtPauschTaxEmployeeKiSt_compare.setVisibility(View.INVISIBLE);
            txtPauschTaxEmployeeKiSt_compare.setTextColor(Color.WHITE);
        }
        txtPauschTaxEmployeeKiSt_compare.setText((diffPauschKirchenSt > 0 ? "+" : "") +_formatCurrency(diffPauschKirchenSt));

        // veränderter Pauschaler Soli
        Double oldPauschSoli = FormatHelper.toDouble(dataCompare.data.Pausch_Soli_AN);
        Double newPauschSoli = FormatHelper.toDouble(data.data.Pausch_Soli_AN);
        Double diffPauschSoli = newPauschSoli - oldPauschSoli;
        if(diffPauschSoli >= 0.01) {
            txtPauschTaxEmployeeSoli_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeSoli_compare.setTextColor(Color.RED);
        } else if(diffPauschSoli <= -0.01) {
            txtPauschTaxEmployeeSoli_compare.setVisibility(View.VISIBLE);
            txtPauschTaxEmployeeSoli_compare.setTextColor(green);
        } else {
            txtPauschTaxEmployeeSoli_compare.setVisibility(View.INVISIBLE);
            txtPauschTaxEmployeeSoli_compare.setTextColor(Color.WHITE);
        }
        txtPauschTaxEmployeeSoli_compare.setText((diffPauschSoli > 0 ? "+" : "") +_formatCurrency(diffPauschSoli));
    }

    private String _formatCurrency(String text)
    {
        try {
            return FormatHelper.currency(text);
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