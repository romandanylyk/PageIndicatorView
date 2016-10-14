
###**PageIndicatorView**

![](https://raw.githubusercontent.com/romandanylyk/PageIndicatorView/master/gifs/gif_1.gif)

PageIndicatorView will simplify your life while you working with Android ViewPager and need to indicate selected page. It's easy to setup and customize as you need with run-time preview rendering.

####**Integration**
To add `pageindicatorview` to your project, first make sure in root `build.gradle` you have specified the following repository:
```java
    repositories {
        jcenter()
    }
```


----------
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
