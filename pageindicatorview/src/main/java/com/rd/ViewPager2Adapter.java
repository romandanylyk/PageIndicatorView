package com.rd;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

class ViewPager2Adapter implements PageIndicatorViewAdapter {

    private final ViewPager2 viewPager;
    private final PageIndicatorViewAdapterListener listener;
    private RecyclerView.AdapterDataObserver setObserver;
    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
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
            listener.onPageScrollIdle();
        }
    };

    public ViewPager2Adapter(ViewPager2 pager, PageIndicatorViewAdapterListener listener) {
        this.viewPager = pager;
        this.listener = listener;
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    @Override
    public void release() {
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }

    @Override
    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    @Override
    public int getItemCount() {
        return viewPager.getAdapter().getItemCount();
    }

    @Override
    public boolean isReady() {
        return viewPager.getAdapter() != null;
    }

    @Override
    public void setMonitorDataSetChanges(boolean shouldMonitor) {
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

        setObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                listener.onDataSetChange();

            }
        };

        try {
            viewPager.getAdapter().registerAdapterDataObserver(setObserver);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void unRegisterSetObserver() {
        if (setObserver == null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        try {
            viewPager.getAdapter().unregisterAdapterDataObserver(setObserver);
            setObserver = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
