
###**PageIndicatorView**
[ ![Download](https://api.bintray.com/packages/romandanylyk/maven/pageindicatorview/images/download.svg) ](https://bintray.com/romandanylyk/maven/pageindicatorview/_latestVersion)[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PageIndicatorView-green.svg?style=true)](https://android-arsenal.com/details/1/4555)  
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)


`PageIndicatorView` will simplify your life while you working with Android `ViewPager` and need to indicate selected page. It's easy to setup and customize as you need with run-time preview rendering.

![](https://github.com/romandanylyk/PageIndicatorView/blob/master/assets/animation_worm.gif?raw=true)

###**Integration**
To add `pageindicatorview` to your project, first make sure in root `build.gradle` you have specified the following repository:
```java
    repositories {
        jcenter()
    }
```
*Note: by creating new project in Android Studio it will have `jcenter` repository specified by default, so you will not need to add it manually.* 

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project `build.gradle`.
 
See latest library version [ ![Download](https://api.bintray.com/packages/romandanylyk/maven/pageindicatorview/images/download.svg) ](https://bintray.com/romandanylyk/maven/pageindicatorview/_latestVersion)
```java
compile 'com.romandanylyk:pageindicatorview:X.X.X'
```
Keep in mind, that `PageIndicatorView` has min [API level 14](https://developer.android.com/about/dashboards/index.html)

###**Usage Sample**
During implementation of `PageIndicatorView` I tried to make it's setup as easy as possible. 
After you set adapter to your `ViewPager`, all you need to do is to `setViewPager()` and that's it! `PageIndicatorView` will get count from your adapter and start working with instance of your `ViewPager` automatically.  

```java
ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
viewPager.setAdapter(adapter);
//instance of android.support.v4.view.PagerAdapter adapter

PageIndicatorView pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.pageIndicatorView);
pageIndicatorView.setViewPager(viewPager);
```

Keep in mind that all public methods are also exist as attributes, so you can even setup and customize page indicator without any java code. 

```xml
    <com.rd.PageIndicatorView
        android:id="@+id/pageIndicatorView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        attrs:piv_viewPager="@id/viewPager"/>
```

###**Customization**
One of the most important feature of every custom view is ability to customize its look as user need. By calling the following methods (or attributes) you will be able to customize `PageIndicatorView` as you need.

```java
//set size
setCount(int count)
setDynamicCount(boolean dynamicCount)

setRadius(int radiusDp)
setPadding(int paddingDp)

//set color
setUnselectedColor(int color)
setSelectedColor(int color)

//set animation
setAnimationDuration(long duration)
setAnimationType(AnimationType type)
setInteractiveAnimation(boolean isInteractive)

//set selection
setProgress(int selectingPosition, float progress)
setSelection(int position)
```

![](https://github.com/romandanylyk/PageIndicatorView/blob/master/assets/prev_attributes.gif?raw=true)

Here you can see all the animations `PageIndicatorView` support.

Name| Support version| Preview
-------- | --- | ---
`AnimationType.NONE`| 0.0.1 | ![anim_prev_none](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_none.gif)
`AnimationType.COLOR`| 0.0.1 |![anim_prev_color](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_color.gif)
`AnimationType.SCALE`| 0.0.1 |![anim_prev_scale](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_scale.gif)
`AnimationType.SLIDE`| 0.0.1 |![anim_prev_slide](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_slide.gif)
`AnimationType.WORM`| 0.0.1 |![anim_prev_worm](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_worm.gif)
`AnimationType.FILL`| 0.0.6 |![anim_prev_worm](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/assets/anim_prev_fill.gif)

###**Release Note**
See release notes on [github releases](https://github.com/romandanylyk/PageIndicatorView/releases) or [Bintray release notes](https://bintray.com/romandanylyk/maven/pageindicatorview#release).

###**License**

    Copyright 2016 Roman Danylyk
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

