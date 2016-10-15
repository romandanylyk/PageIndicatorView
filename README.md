
###**PageIndicatorView**

PageIndicatorView will simplify your life while you working with Android ViewPager and need to indicate selected page. It's easy to setup and customize as you need with run-time preview rendering.

![](https://lh6.googleusercontent.com/KrC6sU1G6nRSXemSiQn8Us2-L6pcUECmD_b3ydhMFz2-SjEeb8O5njCKdINgjkDJ_mRGhN5D9VTOeVQ=w1920-h950)

####**Integration**
To add `pageindicatorview` to your project, first make sure in root `build.gradle` you have specified the following repository:
```java
    repositories {
        jcenter()
    }
```

*Note: by creating new project in Android Studio it will have `jcenter` repository specified by default, so you will not need to add it manually.* 

Once you make sure you have `jcenter` repository in your project, all you need to do is to add the following line in `dependencies` section of your project `build.gradle`
```java
compile 'com.romandanylyk:pageindicatorview:0.0.1'
```
Than's it! You are ready to go!

####**Usage Sample**
First of all, you need to set count of circles to be drawn, that will indicates number of pages your `ViewPager` has. Then you can call `addViewPager(viewPager)` method to do the rest of job for you. 

```java
PageIndicatorView pageIndicatorView = ...
pageIndicatorView.setCount(4);
pageIndicatorView.addViewPager(viewPager); 
//viewPager instance android.support.v4.view.ViewPager.
```
In case you want to control `PageIndicatorView` manually instead of setting `ViewPager`, you can always do it by calling specific methods.

####**Customization**
One of the most important feature of every custom view is ability to customize its look as user need. By calling the following methods (or attributes) you will be able to customize `PageIndicatorView` as you need.

![](https://lh3.googleusercontent.com/t7AB5_JRefvcH1iOwf194Wae9H36BYJQ5FJ39J_rYwegnP8MXUMZRzf3FzamXABUi4MIeLEdKWUkL2A=w1920-h950)

| Method name | Description |
| :-------  :---: |
| `setCount(int count)` | Set number of circle indicators to be displayed |
| `setRadius(int radiusDp)` | Set radius in dp of each circle indicator | 
| `setPadding(int paddingDp)` | Set padding in dp between each circle indicator |
| `setUnselectedColor(int color)` | Set color of unselected state to each circle indicator |
| `setSelectedColor(int color)` | Set color of selected state to circle indicator|
| `setAnimationDuration(long duration)` | Set animation duration time in millisecond |
| `setAnimationType(AnimationType type)` | Set animation type to perform while selecting new circle indicator |
| `setInteractiveAnimation(boolean isInteractive)` | Set boolean value to perform interactive animation while selecting new indicator |
| `setProgress(int selectingPosition, float progress)` | Set progress value in range [0 - 1] to specify state of animation while selecting new circle indicator |
| `setSelection(int position)` | Set specific circle indicator position to be selected |




