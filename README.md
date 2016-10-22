
###**PageIndicatorView**
[ ![Download](https://api.bintray.com/packages/romandanylyk/maven/pageindicatorview/images/download.svg) ](https://bintray.com/romandanylyk/maven/pageindicatorview/_latestVersion)

PageIndicatorView will simplify your life while you working with Android ViewPager and need to indicate selected page. It's easy to setup and customize as you need with run-time preview rendering.

![](https://github.com/romandanylyk/PageIndicatorView/blob/master/assets/animation_worm.gif?raw=true)

###**Integration**
To add `pageindicatorview` to your project, first make sure in root `build.gradle` you have specified the following repository:
```java
    repositories {
        jcenter()
    }
```
*Note: by creating new project in Android Studio it will have `jcenter` repository specified by default, so you will not need to add it manually.* 

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project `build.gradle`
```java
compile 'com.romandanylyk:pageindicatorview:X.X.X'
```
See latest library version at [Bintray](https://bintray.com/romandanylyk/maven/pageindicatorview).

###**Usage Sample**
First of all, you need to set count of circles to be drawn, that will indicates number of pages your `ViewPager` has. Then you can call `addViewPager(viewPager)` method to do the rest of job for you. 

```java
PageIndicatorView pageIndicatorView = ...
pageIndicatorView.setCount(4);
pageIndicatorView.addViewPager(viewPager); 
//viewPager instance android.support.v4.view.ViewPager.
```
In case you want to control `PageIndicatorView` manually instead of setting `ViewPager`, you can always do it by calling specific methods.

###**Customization**
One of the most important feature of every custom view is ability to customize its look as user need. By calling the following methods (or attributes) you will be able to customize `PageIndicatorView` as you need.

```java
//set size
setCount(int count)
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

![](https://github.com/romandanylyk/PageIndicatorView/blob/master/assets/attributes.gif?raw=true)

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

