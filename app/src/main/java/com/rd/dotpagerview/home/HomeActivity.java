package com.rd.dotpagerview.home;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rd.dotpagerview.R;
import com.rd.dotpagerview.view.animation.AnimationType;
import com.rd.dotpagerview.view.DotPagerView;

public class HomeActivity extends AppCompatActivity {

    private ViewPager pager;
    private DotPagerView dotPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);

        initViews();
    }

    private void initViews() {
        dotPagerView = (DotPagerView) findViewById(R.id.dotPagerView);
        pager = (ViewPager) findViewById(R.id.viewPager);

        HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        dotPagerView.setCount(adapter.getCount());
        dotPagerView.setAnimationType(AnimationType.SLIDE);
        dotPagerView.setPadding(8);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotPagerView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
