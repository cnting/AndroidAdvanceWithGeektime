package com.sample.battery.hook;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cnting on 2022/7/12
 */
public abstract class BaseHookHandle {
    private final Map<String, HookMethodHandler> hookMethodHandlerMap = new HashMap<>();

    public abstract void init();

    public BaseHookHandle() {
        init();
    }

    protected void addHookMethodHandler(String methodName, HookMethodHandler hookMethodHandler) {
        hookMethodHandlerMap.put(methodName, hookMethodHandler);
    }

    public HookMethodHandler getHookedMethodHandler(Method method) {
        if (method != null) {
            return hookMethodHandlerMap.get(method.getName());
        } else {
            return null;
        }
    }
}
