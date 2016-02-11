package sageone.abacus.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public ResultEmployeeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_result_employee, container, false);

        activity.setTitle(R.string.title_activity_result_employee);

        // get the calculation result from the activity
        Calculation data = (Calculation) activity.getIntent().getExtras().getParcelable("Calculation");
        _setViewData(data);

        return view;
    }

    public static ResultEmployeeFragment newInstance(Bundle args)
    {
        ResultEmployeeFragment fragment = new ResultEmployeeFragment();

        fragment.setArguments(args);
        return fragment;
    }

    private void _setViewData(Calculation data)
    {
        TextView wageGross = (TextView) activity.findViewById(R.id.result_employee_wage_gross);
        TextView wageNet = (TextView) activity.findViewById(R.id.result_employee_wage_net);

        wageGross.setText(data.data.LohnsteuerPflBrutto);
        wageNet.setText(data.data.Netto);
    }

}