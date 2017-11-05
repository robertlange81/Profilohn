package com.profilohn.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.profilohn.Activities.ResultActivity;
import com.profilohn.Helper.FormatHelper;
import com.profilohn.Models.Calculation;
import com.profilohn.Helper.FileStore;
import com.profilohn.Models.CalculationInput;
import com.profilohn.R;

/**
 * Created by profilohn on 11.02.2016.
 */
public class ResultHomeFragment extends Fragment
{
    protected static ResultHomeFragment instance;

    private TextView wageGross;
    private TextView wageNet;

    private TextView compareWageGross;
    private TextView compareWageNet;

    private TextView wageDiffGross;
    private TextView wageDiffNet;

    public ResultHomeFragment() { }

    public static ResultHomeFragment getInstance()
    {
        if (null == instance) {
            instance = new ResultHomeFragment();
        }

        return instance;
    }


    /**
     * Instantiates a new Fragment.
     *
     * @param args
     * @return
     */
    public static ResultHomeFragment getInstance(Bundle args)
    {
        instance = getInstance();
        instance.setArguments(args);

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = null;

        // prepare the calculation data
        Calculation data = getActivity().getIntent().getExtras().getParcelable("Calculation");
        FileStore f = new FileStore(getContext());

        // try to fetch previous data and set compare layout if so ..
        try {
            Calculation dataCompare = f.readCalculationResult();
            v = _prepareCompareLayout(inflater, data, dataCompare, container);
        } catch (Exception e) {
            v = _prepareResultLayout(inflater, data, container);
        }

        return v;
    }


    /**
     * Prepares the result layout.
     *
     * @param inflater
     * @param data
     * @param container
     * @return
     */
    private View _prepareResultLayout(LayoutInflater inflater,
                                      Calculation data, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.fragment_result_intro, container, false);
        _initListener(view);

        // result data
        wageGross = (TextView) view.findViewById(R.id.result_intro_wage_gross);
        wageNet = (TextView) view.findViewById(R.id.result_intro_wage_net);

        try {
            wageGross.setText(
                    FormatHelper.currency(data.data.LohnsteuerPflBrutto));
            wageNet.setText(
                    FormatHelper.currency(data.data.Netto));
        } catch (Exception e) {}

        return view;
    }


    /**
     * Prepares the compare layout.
     *
     * @param inflater
     * @param dataResult
     * @param dataCompare
     * @param container
     * @return
     */
    private View _prepareCompareLayout(LayoutInflater inflater, Calculation dataResult,
                                       Calculation dataCompare, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.fragment_compare_intro, container, false);
        _initListener(view);

        // result data
        wageGross = (TextView) view.findViewById(R.id.result_intro_wage_gross);
        wageNet = (TextView) view.findViewById(R.id.result_intro_wage_net);

        // compare data
        compareWageGross = (TextView) view.findViewById(R.id.compare_intro_wage_gross);
        compareWageNet = (TextView) view.findViewById(R.id.compare_intro_wage_net);

        // differences
        if(dataCompare != null) {
            wageDiffGross = (TextView) view.findViewById(R.id.wage_diff_gross);
            wageDiffGross.setText(FormatHelper.percent(
                    dataResult.data.LohnsteuerPflBrutto, dataCompare.data.LohnsteuerPflBrutto));

            wageDiffNet = (TextView) view.findViewById(R.id.wage_diff_net);
            Double oldNetto = FormatHelper.toDouble(dataCompare.data.Netto);
            Double newNetto = FormatHelper.toDouble(dataResult.data.Netto);
            if(newNetto - oldNetto >= 0.01) {
                wageDiffNet.setTextColor(Color.GREEN);
            } else if(newNetto - oldNetto <= -0.01) {
                wageDiffNet.setTextColor(Color.RED);
            } else {
                wageDiffNet.setTextColor(Color.WHITE);
            }
            wageDiffNet.setText(FormatHelper.percent(newNetto, oldNetto));
        }

        try {
            wageGross.setText(
                    FormatHelper.currency(dataResult.data.LohnsteuerPflBrutto));
            wageNet.setText(
                    FormatHelper.currency(dataResult.data.Netto));
            compareWageGross.setText(
                    FormatHelper.currency(dataCompare.data.LohnsteuerPflBrutto));
            compareWageNet.setText(
                    FormatHelper.currency(dataCompare.data.Netto));
        } catch (Exception e) { }

        return view;
    }


    /**
     * Initialize the on click events.
     *
     * @param view
     */
    private void _initListener(View view)
    {
        // listen on the button clicks
        AppCompatButton btnEmployee = (AppCompatButton) view.findViewById(R.id.result_intro_btn_employee);
        if(btnEmployee != null) {
            btnEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            ((ResultActivity) getActivity()).setCurrentPage(1, true);
                        }

                    }, getResources().getInteger(R.integer.calculation_timeout));
                }
            });
        }


        AppCompatButton btnEmployer = (AppCompatButton) view.findViewById(R.id.result_intro_btn_employer);
        if(btnEmployer != null) {
            btnEmployer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            ((ResultActivity) getActivity()).setCurrentPage(2, true);
                        }

                    }, getResources().getInteger(R.integer.calculation_timeout));
                }
            });
        }
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
            instance.getActivity().setTitle(getResources().getString(R.string.result_intro_title));
        }
    }

}