package com.rd;

/**
 * Adapter interface to use PageIndicatorView with any paging view
 */
interface PageIndicatorViewAdapter {
    /**
     * Called when the delegate is no longer needed and can release any resources.
     */
    void release();

    /**
     * The current item position in the paging view.
     * @return the current item position
     */
    int getCurrentItem();

    /**
     * Returns the number of items in the paging view.
     * @return the number of items
     */
    int getItemCount();

    /**
     * Returns true if the adapter and the underlying view is ready
     * @return true if the adapter is ready
     */
    boolean isReady();

    /**
     * Used to toggle if the delegate should listen to data set changes
     * @param shouldMonitor true if the adapter should monitor data set changes
     */
    void setMonitorDataSetChanges(boolean shouldMonitor);
}
