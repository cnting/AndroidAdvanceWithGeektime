# Chapter07
这个Sample是学习如何给代码加入Trace Tag, 大家可以将这个代码运用到自己的项目中，然后利用systrace查看结果

结果如下：

```
    protected void onResume() {
        TraceTag.i("com.sample.systrace.MainActivity.onResume.()V");
        super.onResume();
        Log.i("MainActivity", "[onResume]");
        TraceTag.o();
    }
```

## 操作步骤
操作步骤如下：

1. 使用Android Studio打开工程Chapter07
2. 运行gradle task ":systrace-gradle-plugin:buildAndPublishToLocalMaven" 编译plugin插件
3. 使用Android Studio单独打开工程systrace-sample-android
4. 编译app

## 注意事项
在systrace-sample-android工程中，需要注意以下几点：

1. 插桩代码会自动过滤短函数，过滤结果输出到`Chapter07/systrace-sample-android/app/build/systrace_output/Debug.ignoremethodmap`。
2. 我们也可以单独控制不插桩的白名单，配置文件位于`Chapter07/systrace-sample-android/app/blacklist/blackMethodList.txt`， 可以指定class或者包名。
3. 插桩后的class文件在目录`Chapter07/systrace-sample-android/app/build/systrace_output/classes`中查看。

然后运行应用，需要打开systrace
```
python $ANDROID_HOME/platform-tools/systrace/systrace.py gfx view wm am pm ss dalvik app sched -b 90960 -a com.sample.systrace  -o test.log.html
```

---
## 运行有问题？
#### 生成插件有问题
1. 找不到类？在`systrace-gradle-plugin`的`build.gradle`里添加如下
```groovy
repositories {
        google()
        jcenter() 
}

dependencies {
        // Android DSL
        implementation 'com.android.tools.build:gradle:3.6.2'
}

sourceSets {
    main {
        groovy {
            srcDir 'src/main/groovy'
        }

        java {
            srcDir "src/main/java"
        }

        resources {
            srcDir 'src/main/resources'
        }
    }
}
```
2. `Entry META-INF/gradle-plugins ... duplicate `?
在`systrace-gradle-plugin`的`build.gradle`里添加如下
   
```groovy
gradle.taskGraph.whenReady {
    tasks
            .each {
                if(it.hasProperty("duplicatesStrategy")){
                    it.setProperty("duplicatesStrategy", "EXCLUDE")
                }
            }
}
```

#### 运行起来了，但Transform有问题
看这里：[谨慎hook，一个hook Transform源码导致的错误！](https://juejin.cn/post/7070322767568044062)
项目里是hook `transformClassesWithDexBuilderForXXX` 这个task，到AGP 4.x，这个task已经被删除了，这个任务交给了 `DexArchiveBuilderTask`。
可以看这个项目：[TraceFix](https://github.com/Gracker/TraceFix)
