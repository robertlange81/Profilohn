package sageone.abacus.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sageone.abacus.Activities.ResultActivity;
import sageone.abacus.Models.Calculation;
import sageone.abacus.R;

/**
 * Created by otomaske on 11.02.2016.
 */
public class ResultHomeFragment extends Fragment
{
    protected static ResultHomeFragment instance;

    private TextView wageGross;
    private TextView wageNet;

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
        View view = inflater.inflate(R.layout.fragment_result_intro, container, false);

        // listen on the button clicks
        AppCompatButton btnEmployee = (AppCompatButton) view.findViewById(R.id.result_intro_btn_employee);
        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ResultActivity)getActivity()).setCurrentPage(1, true);
            }
        });

        AppCompatButton btnEmployer = (AppCompatButton) view.findViewById(R.id.result_intro_btn_employer);
        btnEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ResultActivity)getActivity()).setCurrentPage(2, true);
            }
        });

        // set view data
        Calculation data = (Calculation) getActivity().getIntent().getExtras().getParcelable("Calculation");

        wageGross = (TextView) view.findViewById(R.id.result_intro_wage_gross);
        wageNet = (TextView) view.findViewById(R.id.result_intro_wage_net);

        wageGross.setText(data.data.LohnsteuerPflBrutto);
        wageNet.setText(data.data.Netto);

        return view;
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