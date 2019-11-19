package com.profilohn.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.profilohn.Fragments.ResultEmployeeFragment;
import com.profilohn.Fragments.ResultEmployerFragment;
import com.profilohn.Fragments.ResultHomeFragment;
import com.profilohn.Helper.FileStore;
import com.profilohn.Models.Calculation;
import com.profilohn.R;

public class ResultActivity extends AppCompatActivity {

    private boolean isTablet;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    protected ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if(mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        } else {
            View area_for_smartphone = findViewById(R.id.button_result_area_handy);
            if(area_for_smartphone != null)
                area_for_smartphone.setVisibility(View.GONE);
            isTablet = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(isTablet || mViewPager == null) {
            this.finish();
            return true;
        }

        int ci = mViewPager.getCurrentItem();

        if (0 == ci) {
            this.finish();
        } else {
            setCurrentPage(0, true);
        }

        return true;
    }

    public void setCurrentPage(int position, boolean smooth)
    {
        if(mViewPager != null)
            mViewPager.setCurrentItem(position, smooth);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment;
            Context context = ResultActivity.this;

            switch (position) {
                case 0:
                    // home fragment
                    fragment = Fragment.instantiate(context, ResultHomeFragment.class.getName());
                    //getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 1:
                    // employee fragment
                    fragment = Fragment.instantiate(context, ResultEmployeeFragment.class.getName());
                    //getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 2:
                    // employer fragment
                    fragment = Fragment.instantiate(context, ResultEmployerFragment.class.getName());
                    //getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                default:
                    fragment = Fragment.instantiate(context, ResultHomeFragment.class.getName());
                    //getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            return fragment;
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }

    public void rateApp()
    {
        Uri uri = Uri.parse("market://details?id=" + ResultActivity.this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ResultActivity.this.getPackageName())));
        }
    }

    @Override
    public void onBackPressed() {

        this.rateApp();

        if(this.isTablet) {
            this.finish();
        } else {
            if(mViewPager != null && mViewPager.getCurrentItem() != 0) {
                mViewPager.setCurrentItem(0, true);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Calculation data = getIntent().getExtras().getParcelable("Calculation");
        FileStore fs = new FileStore(this);
        fs.writeCalculationResult(data);
    }

    @Override
    public void onStop() {
        super.onStop();
        Calculation data = getIntent().getExtras().getParcelable("Calculation");
        FileStore fs = new FileStore(this);
        fs.writeCalculationResult(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Calculation data = getIntent().getExtras().getParcelable("Calculation");
        FileStore fs = new FileStore(this);
        fs.writeCalculationResult(data);
    }
}
