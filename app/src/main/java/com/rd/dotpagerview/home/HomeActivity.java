package com.rd.dotpagerview.home;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import com.rd.dotpagerview.R;
import com.rd.dotpagerview.view.animation.AnimationType;
import com.rd.dotpagerview.view.DotPagerView;

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

        DotPagerView dotPagerView = (DotPagerView) findViewById(R.id.dotPagerView);
        dotPagerView.setViewPager(pager);
    }
}
