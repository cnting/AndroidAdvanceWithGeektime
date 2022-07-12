package com.sample.battery.gps;

import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.HookMethodHandler;
import com.sample.battery.hook.Util;

import java.lang.reflect.Method;

/**
 * Created by cnting on 2022/7/12
 */
public class GPSHookHandle extends BaseHookHandle {

    @Override
    public void init() {
        addHookMethodHandler("requestLocationUpdates", new HookMethodHandler() {
            @Override
            protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
                Log.i("Hoooooooook", "--requestLocationUpdates--");
                Log.i("Hoooooooook", Util.getStackTrace());
                return super.beforeInvoke(receiver, method, args);
            }

            @Override
            protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) {
                super.afterInvoke(receiver, method, args, invokeResult);
            }
        });

        addHookMethodHandler("removeUpdates", new HookMethodHandler() {
            @Override
            protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
                Log.i("Hoooooooook", "--removeUpdates--");
                Log.i("Hoooooooook", Util.getStackTrace());
                return super.beforeInvoke(receiver, method, args);
            }

            @Override
            protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) {
                super.afterInvoke(receiver, method, args, invokeResult);
            }
        });
    }
}
