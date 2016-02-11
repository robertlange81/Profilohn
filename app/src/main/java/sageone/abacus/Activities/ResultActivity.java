package sageone.abacus.Activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import sageone.abacus.Fragments.ResultEmployeeFragment;
import sageone.abacus.Fragments.ResultEmployerFragment;
import sageone.abacus.Fragments.ResultHomeFragment;
import sageone.abacus.Models.Calculation;
import sageone.abacus.R;

public class ResultActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
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
                    f = ResultHomeFragment.newInstance();
                    break;
                case 1:
                    // employee fragment
                    f = ResultEmployeeFragment.newInstance(args);
                    break;
                case 2:
                    // employer fragment
                    f = ResultEmployerFragment.newInstance(args);
                    break;
            }

            //f.getActivity().setTitle(this.getPageTitle(position));

            return f;
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.title_activity_result_employee);
                case 1:
                    return getResources().getString(R.string.title_activity_result_employer);
                case 2:
                    return getResources().getString(R.string.title_activity_result_sage);
            }
            return null;
        }
    }
}
