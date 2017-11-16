

### **PageIndicatorView**
[ ![Download](https://api.bintray.com/packages/romandanylyk/maven/pageindicatorview/images/download.svg) ](https://bintray.com/romandanylyk/maven/pageindicatorview/_latestVersion)[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PageIndicatorView-green.svg?style=true)](https://android-arsenal.com/details/1/4555)  
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)


`PageIndicatorView` is light library to indicate ViewPager's selected page with different animations and ability to customise it as you need.

![](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/preview_anim_drop.gif)

### **Integration**
To add `pageindicatorview` to your project, first make sure in root `build.gradle` you have specified the following repository:
```groovy
    repositories {
        jcenter()
    }
```
>***Note***: by creating new project in Android Studio it will have `jcenter` repository specified by default, so you will not need to add it manually.

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project `build.gradle`.
 
See latest library version [ ![Download](https://api.bintray.com/packages/romandanylyk/maven/pageindicatorview/images/download.svg) ](https://bintray.com/romandanylyk/maven/pageindicatorview/_latestVersion)
```groovy
compile 'com.romandanylyk:pageindicatorview:X.X.X'
```
If your project already use `appcompat-v7` support library, you can omit `PageIndicatorView` dependencies by adding a single .aar file to your project, that will decrease total amount of methods used in your project.

```groovy
compile 'com.romandanylyk:pageindicatorview:X.X.X@aar'
```

Keep in mind, that `PageIndicatorView` has min [API level 14](https://developer.android.com/about/dashboards/index.html) and these dependencies:

```groovy
 compile 'com.android.support:support-annotations:25.3.0'
 compile 'com.android.support:support-compat:25.3.0'
 compile 'com.android.support:support-core-ui:25.3.0'
```

### **Usage Sample**
Usage of `PageIndicatorView` is quite simple. All you need to do is to declare a view in your `layout.xml`  and call `setSelection` method to select specific indicator - that's it!

```java
PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(5); // specify total count of indicators
        pageIndicatorView.setSelection(2);
```

----------

But if you're as lazy as I'm - then there is another option to handle `PageIndicatorView` 

```xml
     <com.rd.PageIndicatorView
        android:id="@+id/pageIndicatorView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:piv_animationType="scale"
        app:piv_dynamicCount="true"
        app:piv_interactiveAnimation="true"
        app:piv_selectedColor="@color/gray_50"
        app:piv_unselectedColor="@color/gray_300"
        app:piv_viewPager="@id/viewPager"
        attrs:piv_padding="12dp"
        attrs:piv_radius="8dp" />
```
All the `piv_` attributes here are specific for `PageIndicatorView` so you can customise it as you want with attributes - pretty handy. 

But what is more important here is  `app:piv_viewPager="@id/viewPager"`.
What it actually do is catch up your `ViewPager` and automatically handles all the event's to selected the right page - so you don't need to call `setSelection` method on your own.

Another handy options here that works with your `ViewPager` as a whole is 
`app:piv_dynamicCount="true"` and ` app:piv_interactiveAnimation="true"` 

Dynamic count will automatically updates `PageIndicatorView` total count as you updates pages count in your `ViewPager` - so that's pretty useful.

While interactive animation will progress the animation process within your swipe position, which makes animation more natural and responsive to end user.

----------

> ***Note***:  Because `setViewPagerId` uses an instance of `ViewPager`, using it in recycler could lead to id conflicts, so `PageIndicatorView` will not know properly what is the right `ViewPager` to work with. Instead you should handle selected indicators on your own programatically.


```java
  pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
```

----------

Here you can see all the animations `PageIndicatorView` support.

Name| Support version| Preview
-------- | --- | ---
`AnimationType.NONE`| 0.0.1 | ![anim_none](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_none.gif)
`AnimationType.COLOR`| 0.0.1 |![anim_color](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_color.gif)
`AnimationType.SCALE`| 0.0.1 |![anim_scale](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_scale.gif)
`AnimationType.SLIDE`| 0.0.1 |![anim_slide](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_slide.gif)
`AnimationType.WORM`| 0.0.1 |![anim_worm](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_worm.gif)
`AnimationType.FILL`| 0.0.6 |![anim_worm](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_fill.gif)
`AnimationType.THIN_WORM`| 0.0.7 |![anim_thin_worm](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_thin_worm.gif)
`AnimationType.DROP`| 0.1.0 |![anim_drop](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_drop.gif)
`AnimationType.SWAP`| 0.1.1 |![anim_swap](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_swap.gif)

### **Documentation**

Methods | Description
------- | -----------
`setCount(int count)` | Set static number of circle indicators to be displayed. Param `count` - total count of indicators.
`getCount()` |  Return number of circle indicators
`setDynamicCount(boolean dynamicCount)` | Dynamic count will automatically update number of circle indicators if ViewPager page count updates on run-time. If new count will be bigger than current count, selected circle will stay as it is, otherwise it will be set to last one. Note: works if `ViewPager` set and already have it's adapter. See {@link #setViewPager(ViewPager)}.
`setRadius(int radiusDp)` | Set radius in dp of each circle indicator. Default value is 6 dp. Make sure you set circle Radius, not a Diameter.
`setRadius(int radiusPx)` | Set radius in px of each circle indicator. Default value is 6 dp. Make sure you set circle Radius, not a Diameter.
`getRadius()` | Returns radius of each circle indicators in px.
`setPadding(int paddingDp)` | Set padding in dp between each circle indicator. Default value is 8 dp.
`setPadding(int paddingPx)` | Set padding in px between each circle indicator. Default value is 8 dp.
`getPadding()` | Returns padding in px between each circle indicator. 
`setScaleFactor(float factor)` | Set scale factor used in {@link AnimationType#SCALE} animation. Defines size of unselected indicator circles in comparing to selected one. Minimum and maximum values are {@link ScaleAnimation#MAX_SCALE_FACTOR} and {@link ScaleAnimation#MIN_SCALE_FACTOR}. See also {@link ScaleAnimation#DEFAULT_SCALE_FACTOR}.
`getScaleFactor()` | Returns scale factor values used in {@link AnimationType#SCALE} animation. Defines size of unselected indicator circles in comparing to selected one.Minimum and maximum values are {@link ScaleAnimation#MAX_SCALE_FACTOR} and {@link ScaleAnimation#MIN_SCALE_FACTOR}. See also {@link ScaleAnimation#DEFAULT_SCALE_FACTOR}. float value that indicate scale factor
`setStrokeWidth(float strokePx)` | Set stroke width in px to set while {@link AnimationType#FILL} is selected.Default value is {@link FillAnimation#DEFAULT_STROKE_DP} strokePx stroke width in px.
`setStrokeWidth(float strokeDp)` | Set stroke width in dp to set while {@link AnimationType#FILL} is selected. Default value is {@link FillAnimation#DEFAULT_STROKE_DP} strokePx stroke width in px.
`getStrokeWidth()` | Return stroke width in px. If custom stroke width is not set and {@link AnimationType#FILL} is selected.
`setSelectedColor(int color)` | Set color of selected state to circle indicator. Default color is white {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
`getSelectedColor()` | Return color of selected circle indicator. If custom unselected color. is not set, return default color {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
`setUnselectedColor(int color)` | Set color of unselected state to each circle indicator. Default color {@link ColorAnimation#DEFAULT_UNSELECTED_COLOR}.@param color color of each unselected circle.
`getUnselectedColor()` | Return color of unselected state of each circle indicator. If custom unselected color is not set, return default color ColorAnimation#DEFAULT_UNSELECTED_COLOR}.
`setAutoVisibility(boolean autoVisibility)` | Automatically hide (View.INVISIBLE) PageIndicatorView while indicator count is <= 1. Default is true.
`setOrientation(@Nullable Orientation orientation)` | Set orientation for indicator, one of HORIZONTAL or VERTICAL. Default is HORIZONTAL.
`setAnimationDuration(long duration)` | Set animation duration time in millisecond. Default animation duration time is {@link BaseAnimation#DEFAULT_ANIMATION_TIME}. (Won't affect on anything unless {@link #setAnimationType(AnimationType type)} is specified and {@link #setInteractiveAnimation(boolean isInteractive)} is false).
`getAnimationDuration()` | Return animation duration time in milliseconds. If custom duration is not set, return default duration time {@link BaseAnimation#DEFAULT_ANIMATION_TIME}. 
`setAnimationType(@Nullable AnimationType type)` | Set animation type to perform while selecting new circle indicator. Default animation type is {@link AnimationType#NONE}.
`setInteractiveAnimation(boolean isInteractive)` | Interactive animation will animate indicator smoothly from position to position based on user's current swipe progress. (Won't affect on anything unless {@link #setViewPager(ViewPager)} is specified). isInteractive value of animation to be interactive or not.
`setViewPager(@Nullable ViewPager pager)`  | Set {@link ViewPager} to add {@link ViewPager.OnPageChangeListener} and automatically handle selecting new indicators (and interactive animation effect if it is enabled).
`releaseViewPager()` | Release {@link ViewPager} and stop handling events of {@link ViewPager.OnPageChangeListener}.
`setRtlMode(RtlMode mode)` | Specify to display PageIndicatorView with Right to left layout or not. One of {@link RtlMode}: Off (Left to right), On (Right to left) or Auto (handle this mode automatically based on users language preferences). Default is Off. 
`setSelection(int position)` | Set specific circle indicator position to be selected. If position < or > total count, accordingly first or last circle indicator will be selected.(Won't affect on anything unless {@link #setInteractiveAnimation(boolean isInteractive)} is false).
`setSelected(int position)` | Set specific circle indicator position to be selected without any kind of animation. If position < or > total count, accordingly first or last circle indicator will be selected.
`getSelection()` | Return position of currently selected circle indicator.
`setProgress(int selectingPosition, float progress)` | Set progress value in range [0 - 1] to specify state of animation while selecting new circle indicator. (Won't affect on anything unless {@link #setInteractiveAnimation(boolean isInteractive)} is true).


### **Release Note**
See release notes on [github releases](https://github.com/romandanylyk/PageIndicatorView/releases) or [Bintray release notes](https://bintray.com/romandanylyk/maven/pageindicatorview#release).

### **License**

    Copyright 2017 Roman Danylyk
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

