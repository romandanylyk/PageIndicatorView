package com.rd.pageindicatorview.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.rd.pageindicatorview.sample.R;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);
        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        final HomeAdapter adapter = new HomeAdapter();

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<View> pageList = new ArrayList<>();
                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));
                pageList.add(createPageView(R.color.google_yellow));
                pageList.add(createPageView(R.color.google_green));

                adapter.setData(pageList);
            }
        }, 5000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<View> pageList = new ArrayList<>();
                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));

                adapter.setData(pageList);
            }
        }, 10000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<View> pageList = new ArrayList<>();
                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));
                pageList.add(createPageView(R.color.google_yellow));
                pageList.add(createPageView(R.color.google_green));

                adapter.setData(pageList);
            }
        }, 15000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<View> pageList = new ArrayList<>();
                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));
                pageList.add(createPageView(R.color.google_yellow));
                pageList.add(createPageView(R.color.google_green));

                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));
                pageList.add(createPageView(R.color.google_yellow));
                pageList.add(createPageView(R.color.google_green));

                adapter.setData(pageList);
            }
        }, 20000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<View> pageList = new ArrayList<>();
                pageList.add(createPageView(R.color.google_red));
                pageList.add(createPageView(R.color.google_blue));

                adapter.setData(pageList);
            }
        }, 25000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setData(createPageList());
            }
        }, 30000);
    }

    @NonNull
    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        pageList.add(createPageView(R.color.google_red));
        pageList.add(createPageView(R.color.google_blue));
        pageList.add(createPageView(R.color.google_yellow));
        pageList.add(createPageView(R.color.google_green));

        return pageList;
    }

    @NonNull
    private View createPageView(int color) {
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(color));

        return view;
    }
}
