package com.sample.battery.hook;

import java.lang.reflect.Method;

/**
 * Created by cnting on 2022/7/12
 */
public class HookMethodHandler {
    public synchronized Object doHookInner(Object receiver, Method method, Object[] args) throws Throwable {
        boolean success = beforeInvoke(receiver, method, args);
        Object invokeResult = null;
        if (!success) {
            invokeResult = method.invoke(receiver, args);
        }
        afterInvoke(receiver, method, args, invokeResult);
        return invokeResult;
    }

    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
        return false;
    }

    protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) {
    }
}
