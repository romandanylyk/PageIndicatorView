package com.rd;

import android.database.DataSetObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

class ViewPagerAdapter implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener, PageIndicatorViewAdapter {
    private final ViewPager viewPager;
    private DataSetObserver setObserver;
    private final PageIndicatorViewAdapterListener listener;
    private boolean monitorDataSetChanges;

    public ViewPagerAdapter(ViewPager pager, PageIndicatorViewAdapterListener listener) {
        this.viewPager = pager;
        this.listener = listener;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        listener.onPageScroll(position, positionOffset);
    }


    @Override
    public void onPageSelected(int position) {
        listener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            listener.onPageScrollIdle();
        }
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        if (monitorDataSetChanges) {
            if (oldAdapter != null && setObserver != null) {
                oldAdapter.unregisterDataSetObserver(setObserver);
                setObserver = null;
            }
            registerSetObserver();
        }
        listener.onDataSetChange();
    }

    @Override
    public void setMonitorDataSetChanges(boolean shouldMonitor) {
        this.monitorDataSetChanges = shouldMonitor;
        if (shouldMonitor) {
            registerSetObserver();
        } else {
            unRegisterSetObserver();
        }
    }

    private void registerSetObserver() {
        if (setObserver != null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        setObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                listener.onDataSetChange();
            }
        };

        try {
            viewPager.getAdapter().registerDataSetObserver(setObserver);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void unRegisterSetObserver() {
        if (setObserver == null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        try {
            viewPager.getAdapter().unregisterDataSetObserver(setObserver);
            setObserver = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        viewPager.removeOnPageChangeListener(this);
        viewPager.removeOnAdapterChangeListener(this);
    }

    @Override
    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    @Override
    public int getItemCount() {
        return viewPager != null && isReady() ? viewPager.getAdapter().getCount() : 0;
    }

    @Override
    public boolean isReady() {
        return viewPager.getAdapter() != null;
    }
}
