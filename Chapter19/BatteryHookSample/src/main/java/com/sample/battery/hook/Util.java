package com.sample.battery.hook;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.sample.battery.SampleApplication;

/**
 * Created by cnting on 2022/7/12
 */
public class Util {
    public static String getStackTrace() {
        StringBuilder sb = new StringBuilder();
        //获取电量信息
        sb.append("是否充电状态：");
        sb.append(isCharging());
        sb.append("\n");
        //获取堆栈
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 0) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                String className = stackTraceElement.getClassName();
                if (Util.class.getName().equals(className)) {
                    continue;
                }
                String methodName = stackTraceElement.getMethodName();
                String fileName = stackTraceElement.getFileName();
                int lineNumber = stackTraceElement.getLineNumber();
                sb.append(className)
                        .append(".")
                        .append(methodName)
                        .append("(").append(fileName).append(":").append(lineNumber).append(")\n");
            }
            return sb.toString();
        }
        return "";
    }

    private static boolean isCharging() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = SampleApplication.context.registerReceiver(null, intentFilter);
        //获取用户是否在充电状态或者已经充满电了
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }
}
