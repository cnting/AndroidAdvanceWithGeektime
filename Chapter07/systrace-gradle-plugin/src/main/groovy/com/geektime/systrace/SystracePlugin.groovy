package com.geektime.systrace

import com.android.build.gradle.AppExtension
import com.geektime.systrace.extension.SystraceExtension
import com.geektime.systrace.transform.SystemTraceTransform
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by zhangshaowen on 17/6/16.
 */
class SystracePlugin implements Plugin<Project> {
    private static final String TAG = "SystracePlugin"

    @Override
    void apply(Project project) {
        //在app build.gradle中配置信息，在plugin中可读取这些信息
        project.extensions.create("systrace", SystraceExtension)

        if (!project.plugins.hasPlugin('com.android.application')) {
            throw new GradleException('Systrace Plugin, Android Application plugin required')
        }

        project.afterEvaluate {
            def android = project.extensions.android
            def configuration = project.systrace
            android.applicationVariants.all { variant ->
                //设置文件输出路径
                String output = configuration.output
                if (Util.isNullOrNil(output)) {
                    configuration.output = project.getBuildDir().getAbsolutePath() +  File.separator + "systrace_output"
                    Log.i(TAG, "set Systrace output file to " + configuration.output)
                }

                Log.i(TAG, "Trace enable is %s", configuration.enable)
                if (configuration.enable) {
                    SystemTraceTransform.inject(project, variant)
                }
            }
        }
    }
}
