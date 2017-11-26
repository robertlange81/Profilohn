package com.profilohn.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

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
                    break;
                case 1:
                    // employee fragment
                    fragment = Fragment.instantiate(context, ResultEmployeeFragment.class.getName());
                    break;
                case 2:
                    // employer fragment
                    fragment = Fragment.instantiate(context, ResultEmployerFragment.class.getName());
                    break;
                default:
                    fragment = Fragment.instantiate(context, ResultEmployeeFragment.class.getName());
            }

            return fragment;
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {

        if(this.isTablet) {
            this.finish();
        } else {
            if(mViewPager.getCurrentItem() != 0) {
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
