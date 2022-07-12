package com.sample.battery.wakelock;

import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.HookMethodHandler;
import com.sample.battery.hook.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by cnting on 2022/7/12
 */
public class WakelockHookHandle extends BaseHookHandle {
    @Override
    public void init() {
        addHookMethodHandler("acquireWakeLock", new AcquireWakeLockHandler());
        addHookMethodHandler("releaseWakeLock", new ReleaseWakeLockHandler());
    }

    static class AcquireWakeLockHandler extends HookMethodHandler {
        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
            Log.i("Hoooooooook", "--acquireWakeLock--");
            Log.i("Hoooooooook","args:"+ Arrays.toString(args)+",receiver:"+receiver);
            Log.i("Hoooooooook", Util.getStackTrace());

            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }

    static class ReleaseWakeLockHandler extends HookMethodHandler {
        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
            Log.i("Hoooooooook", "--releaseWakeLock--");
            Log.i("Hoooooooook", Util.getStackTrace());
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) {
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }
}
