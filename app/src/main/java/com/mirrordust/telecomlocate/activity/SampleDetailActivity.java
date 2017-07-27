package com.mirrordust.telecomlocate.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.Mapbox;
import com.mirrordust.telecomlocate.R;
import com.mirrordust.telecomlocate.fragment.DetailFragment;
import com.mirrordust.telecomlocate.fragment.MapFragment;
import com.mirrordust.telecomlocate.adapter.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SampleDetailActivity extends AppCompatActivity {

    private static final String TAG = "SampleDetailActivity";
    private static final String ARG_SAMPLE_ID = "sample_id";
    private String sampleId;
    private SectionsPagerAdapter mSectionsPagerAdapter; // provide fragments for each of the sections
    private ViewPager mViewPager; // host the section contents
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_public_access_token));
        setContentView(R.layout.activity_sample_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sampleId = getIntent().getStringExtra(ARG_SAMPLE_ID);

        // fragments
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DetailFragment());
        fragments.add(new MapFragment());

        // fragment titles
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.detail_tab_title_1));
        titles.add(getString(R.string.detail_tab_title_2));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
