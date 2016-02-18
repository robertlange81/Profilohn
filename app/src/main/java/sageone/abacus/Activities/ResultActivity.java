package sageone.abacus.Activities;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import sageone.abacus.Fragments.ResultEmployeeFragment;
import sageone.abacus.Fragments.ResultEmployerFragment;
import sageone.abacus.Fragments.ResultHomeFragment;
import sageone.abacus.R;

public class ResultActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static ResultActivity instance;


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
}
