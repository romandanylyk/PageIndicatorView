package com.rd.pageindicatorview.viewpager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.rd.pageindicatorview.base.BaseActivity;
import com.rd.pageindicatorview.data.Customization;
import com.rd.pageindicatorview.sample.R;

import static com.rd.pageindicatorview.customize.CustomizeActivity.EXTRAS_CUSTOMIZATION;
import static com.rd.pageindicatorview.customize.CustomizeActivity.EXTRAS_CUSTOMIZATION_REQUEST_CODE;

/**
 * Created by Santhosh on 3/19/2020.
 */
public class ViewPagerActivity extends BaseActivity {

    public static void start(@NonNull Activity activity, @NonNull Customization customization) {
        Intent intent = new Intent(activity, ViewPagerActivity.class);
        intent.putExtra(EXTRAS_CUSTOMIZATION, customization);
        activity.startActivityForResult(intent, EXTRAS_CUSTOMIZATION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_viewpager2);
        initToolbar();
        initViews();
    }

    private void initViews() {
        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(this);
        final ViewPager2 pager = findViewById(R.id.viewPager2);
        pager.setAdapter(pagerAdapter);
    }
}
