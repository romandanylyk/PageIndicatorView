package com.rd;

/**
 * Callbacks for when the paging view changes state.
 */
interface PageIndicatorViewAdapterListener {
    /**
     * This method will be invoked when a new page becomes selected.
     *
     * @param position Position index of the new selected page.
     */
    void onPageSelected(int position);

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     */
    void onPageScroll(int position, float positionOffset);

    /**
     * This method will be invoked when the paging view has reached an idle state
     */
    void onPageScrollIdle();

    /**
     * This method will be invoked when the data set has changed.
     */
    void onDataSetChange();
}
