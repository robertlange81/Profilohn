package com.profilohn.Activities;

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

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static ResultActivity instance;
    private static boolean isTablet;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    protected ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if(mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        } else {
            isTablet = true;
        }

        instance = this;
    }


    /**
     * On click listener that
     * finish the activity.
     *
     * @param item
     * @return
     */
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


    /**
     * Slide to position.
     *
     * @param position
     * @param smooth
     */
    public void setCurrentPage(int position, boolean smooth)
    {
        mViewPager.setCurrentItem(position, smooth);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment f = new Fragment();
            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    // home fragment
                    f = ResultHomeFragment.getInstance();
                    break;
                case 1:
                    // employee fragment
                    f = ResultEmployeeFragment.getInstance(args);
                    break;
                case 2:
                    // employer fragment
                    f = ResultEmployerFragment.getInstance(args);
                    break;
            }

            return f;
        }


        /**
         * Returns the page amount.
         *
         * @return
         */
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
