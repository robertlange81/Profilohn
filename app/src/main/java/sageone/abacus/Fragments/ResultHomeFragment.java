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
public class ResultHomeFragment extends Fragment
{
    public ResultHomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_result_home, container, false);
        getActivity().setTitle(R.string.result_home_title);

        return view;
    }

    public static ResultHomeFragment newInstance()
    {
        ResultHomeFragment fragment = new ResultHomeFragment();
        return fragment;
    }

    public static ResultHomeFragment newInstance(Bundle args)
    {
        ResultHomeFragment fragment = new ResultHomeFragment();

        fragment.setArguments(args);
        return fragment;
    }

}