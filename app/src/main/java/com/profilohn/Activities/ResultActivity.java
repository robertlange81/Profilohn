package com.profilohn.Activities;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    public void onBackPressed() {

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
