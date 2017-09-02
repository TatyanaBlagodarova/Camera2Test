package com.camera2.test.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TextView;

import com.camera2.test.R;
import com.camera2.test.fragment.FullModeFragment;
import com.camera2.test.fragment.InfoFragment;
import com.camera2.test.fragment.KeyModeFragment;

/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */
public class ResultsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons(tabLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_custom_layout, null);
        tabOne.setText(getString(R.string.text_feature));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sort_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_custom_layout, null);
        tabTwo.setText(getString(R.string.text_full));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_all_inclusive_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_custom_layout, null);
        tabThree.setText(getString(R.string.text_info));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info_outline_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
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
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new KeyModeFragment();
                case 1:
                    return new FullModeFragment();
                case 2:
                    return new InfoFragment();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
