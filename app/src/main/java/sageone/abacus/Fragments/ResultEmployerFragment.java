package sageone.abacus.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sageone.abacus.R;

/**
 * Created by otomaske on 11.02.2016.
 */
public class ResultEmployerFragment extends Fragment
{
    public ResultEmployerFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_result_employer, container, false);
        getActivity().setTitle(R.string.title_activity_result_employer);

        return view;
    }

    public static ResultEmployerFragment newInstance(Bundle args)
    {
        ResultEmployerFragment fragment = new ResultEmployerFragment();

        fragment.setArguments(args);
        return fragment;
    }

}