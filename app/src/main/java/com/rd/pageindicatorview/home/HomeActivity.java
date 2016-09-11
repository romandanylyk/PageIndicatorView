package com.rd.pageindicatorview.home;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rd.pageindicatorview.R;
import com.rd.pageindicatorview.view.PageIndicatorView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);

        initViews();
    }

    private void initViews() {
        HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(pager);
    }
}
